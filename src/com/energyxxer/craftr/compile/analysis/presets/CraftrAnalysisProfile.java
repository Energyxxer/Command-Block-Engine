package com.energyxxer.craftr.compile.analysis.presets;

import com.energyxxer.craftr.compile.analysis.presets.data.craftr.CraftrTokenAttributes;
import com.energyxxer.craftr.compile.analysis.profiles.AnalysisContext;
import com.energyxxer.craftr.compile.analysis.profiles.AnalysisContextResponse;
import com.energyxxer.craftr.compile.analysis.profiles.AnalysisProfile;
import com.energyxxer.craftr.compile.analysis.token.Token;
import com.energyxxer.craftr.compile.analysis.token.TokenType;
import com.energyxxer.craftr.minecraft.MinecraftConstants;
import com.energyxxer.craftr.util.StringLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.energyxxer.craftr.util.StringUtil.FALSE;
import static com.energyxxer.craftr.util.StringUtil.TRUE;

/**
 * Created by Energyxxer on 2/4/2017.
 */
public class CraftrAnalysisProfile extends AnalysisProfile {

    private static final List<String>
            modifiers = Arrays.asList("public", "static", "typestatic", "abstract", "final", "protected", "private", "synchronized", "compilation", "ingame"),
            unit_types = Arrays.asList("entity", "item", "feature", "class"),
            unit_actions = Arrays.asList("extends", "implements"),
            data_types = Arrays.asList("int", "String", "float", "boolean", "type", "void", "Thread"),
            keywords = Arrays.asList("if", "else", "while", "for", "switch", "case", "default", "new", "event", "init", "package", "import", "operator"),
            action_keywords = Arrays.asList("break", "continue", "return"),
            booleans = Arrays.asList("true", "false"),
            nulls = Collections.singletonList("null"),
            enums = Arrays.asList("Block", "Item", "Gamemode", "Stat", "Achievement", "Effect", "Particle", "Enchantment", "Dimension"),
            entities = new ArrayList<>(MinecraftConstants.entities),
            abstract_entities = Arrays.asList("entity_base", "living_base"),
            pseudo_keywords = Arrays.asList("this", "that"),
            blockstate_specials = Collections.singletonList("default");
    private String blockstate_end = "|";

    static {
        entities.addAll(abstract_entities);
    }

    private static final List<List<String>> enum_values = Arrays.asList(MinecraftConstants.block_enums,MinecraftConstants.block_enums,MinecraftConstants.gamemode_enums,MinecraftConstants.block_enums,MinecraftConstants.block_enums,MinecraftConstants.effect_enums,MinecraftConstants.particle_enums,MinecraftConstants.enchantment_enums,MinecraftConstants.dimension_enums);

    public CraftrAnalysisProfile() {

        //String
        AnalysisContext stringContext = new AnalysisContext() {

            String delimiters = "\"'`";
            char multiLineDelimiter = '`';

            @Override
            public AnalysisContextResponse analyze(String str) {
                if(str.length() <= 0) return new AnalysisContextResponse(false);
                char startingCharacter = str.charAt(0);

                if(delimiters.contains(Character.toString(startingCharacter))) {

                    StringBuilder token = new StringBuilder(Character.toString(startingCharacter));
                    StringLocation end = new StringLocation(1,0,1);

                    for(int i = 1; i < str.length(); i++) {
                        char c = str.charAt(i);

                        if(c == '\n') {
                            end.line++;
                            end.column = 0;
                        } else {
                            end.column++;
                        }
                        end.index++;

                        if(c == '\n' && startingCharacter != multiLineDelimiter) {
                            throw new RuntimeException("Illegal line end in string literal");
                        }
                        token.append(c);
                        if(c == '\\') {
                            token.append(str.charAt(i+1));
                            i++;
                        } else if(c == startingCharacter) {
                            return new AnalysisContextResponse(true, token.toString(), end, TokenType.STRING_LITERAL);
                        }
                    }
                    //Unexpected end of input
                    throw new RuntimeException("Unexpected end of input");
                } else return new AnalysisContextResponse(false);
            }
        };

        //Comment
        AnalysisContext commentContext = new AnalysisContext() {

            String singleLineComment = "//";
            String multiLineCommentStart = "/*";
            String multiLineCommentEnd = "*/";

            @Override
            public AnalysisContextResponse analyze(String str) {
                if(str.length() <= 0) return new AnalysisContextResponse(false);
                boolean multiline;
                if(str.startsWith(singleLineComment) || str.startsWith(multiLineCommentStart)) {
                    multiline = str.startsWith(multiLineCommentStart);

                    if(multiline) {
                        int end = str.substring(multiLineCommentStart.length()).indexOf(multiLineCommentEnd) + multiLineCommentStart.length() + multiLineCommentEnd.length();
                        if(end < multiLineCommentStart.length() + multiLineCommentEnd.length()) throw new RuntimeException("Unclosed comment");
                        String fullComment = str.substring(0,end);
                        StringLocation endLoc = new StringLocation(multiLineCommentStart.length(),0,multiLineCommentStart.length());

                        for(char c : fullComment.substring(multiLineCommentStart.length()).toCharArray()) {
                            if(c == '\n') {
                                endLoc.line++;
                                endLoc.column = 0;
                            } else {
                                endLoc.column++;
                            }
                            endLoc.index++;
                        }

                        return new AnalysisContextResponse(true, fullComment, endLoc, TokenType.COMMENT);
                    } else {
                        int end = str.substring(singleLineComment.length()).indexOf("\n") + singleLineComment.length();
                        if(end < singleLineComment.length()) end = str.length();
                        String fullComment = str.substring(0,end);
                        return new AnalysisContextResponse(true, fullComment, TokenType.COMMENT);
                    }
                }
                return new AnalysisContextResponse(false);
            }
        };

        //Misc
        AnalysisContext miscellaneousContext = new AnalysisContext() {

            String[] patterns = { "->", ";", ".", ",", ":", "@", "#", "(", ")", "[", "]", "{", "}" };
            String[] types = { TokenType.LAMBDA_ARROW, TokenType.END_OF_STATEMENT, TokenType.DOT, TokenType.COMMA, TokenType.COLON, TokenType.ANNOTATION_MARKER, TokenType.BLOCKSTATE_MARKER, TokenType.BRACE, TokenType.BRACE, TokenType.BRACE, TokenType.BRACE, TokenType.BRACE, TokenType.BRACE };

            @Override
            public AnalysisContextResponse analyze(String str) {
                if(str.length() <= 0) return new AnalysisContextResponse(false);
                for(int i = 0; i < patterns.length; i++) {
                    if(str.startsWith(patterns[i])) {
                        return new AnalysisContextResponse(true, patterns[i], types[i]);
                    }
                }
                return new AnalysisContextResponse(false);
            }
        };

        //Operators
        AnalysisContext operatorContext = new AnalysisContext() {
            private List<String> identifier_operators = Arrays.asList("++", "--");
            private List<String> operators = Arrays.asList("+=", "+", "-=", "-", "*=", "*", "/=", "/", "%=", "%", "<=", ">=", "!=", "==", "=", "<", ">");
            private String logical_negation_operator = "!";

            @Override
            public AnalysisContextResponse analyze(String str) {
                if(str.length() <= 0) return new AnalysisContextResponse(false);
                for(String o : identifier_operators) {
                    if(str.startsWith(o)) {
                        return new AnalysisContextResponse(true, o, TokenType.IDENTIFIER_OPERATOR);
                    }
                }
                for(String o : operators) {
                    if(str.startsWith(o)) {
                        return new AnalysisContextResponse(true, o, TokenType.OPERATOR);
                    }
                }
                if(str.startsWith(logical_negation_operator)) {
                    return new AnalysisContextResponse(true, logical_negation_operator, TokenType.LOGICAL_NEGATION_OPERATOR);
                }
                return new AnalysisContextResponse(false);
            }
        };

        //Numbers
        AnalysisContext numberContext = new AnalysisContext() {

            private Pattern regex = Pattern.compile("(\\d+(\\.\\d+)?[bdfsL]?)");

            @Override
            public AnalysisContextResponse analyze(String str) {
                Matcher matcher = regex.matcher(str);

                if(matcher.lookingAt()) {
                    int length = matcher.end();
                    return new AnalysisContextResponse(true, str.substring(0,length), TokenType.NUMBER);
                } else return new AnalysisContextResponse(false);
            }
        };

        ArrayList<AnalysisContext> craftrContexts = new ArrayList<>();
        craftrContexts.add(stringContext);
        craftrContexts.add(commentContext);
        craftrContexts.add(miscellaneousContext);
        craftrContexts.add(operatorContext);
        craftrContexts.add(numberContext);
        this.contexts = craftrContexts;
    }

    @Override
    public boolean canMerge(char ch0, char ch1) {
        return Character.isJavaIdentifierPart(ch0) && Character.isJavaIdentifierPart(ch1);
    }

    @Override
    public boolean filter(Token token) {
        this.classifyKeyword(token);
        this.giveAttributes(token);

        return analyzeBlockstate(token) | analyzeAnnotation(token);
    }

    private HashMap<String, String> bufferData = new HashMap<>();
    private ArrayList<Token> tokenBuffer = new ArrayList<>();

    {
        bufferData.put("IS_ANNOTATION", FALSE);
        bufferData.put("ANNOTATION_PHASE", "NONE");

        bufferData.put("IS_BLOCKSTATE", FALSE);
        bufferData.put("BLOCKSTATE_PHASE", "NONE");
    }

    private boolean analyzeBlockstate(Token token) {

        boolean cancel = false;

        if(token.type == TokenType.BLOCKSTATE_MARKER && bufferData.get("BLOCKSTATE_PHASE").equals("NONE")) {
            bufferData.put("IS_BLOCKSTATE", TRUE);
            bufferData.put("BLOCKSTATE_PHASE", "KEY_FIRST");
            tokenBuffer.add(token);
            cancel = true;
        } else if((token.type == TokenType.IDENTIFIER && bufferData.get("BLOCKSTATE_PHASE").startsWith("KEY")) || (blockstate_specials.contains(token.value) && bufferData.get("BLOCKSTATE_PHASE").equals("KEY_FIRST"))) {
            tokenBuffer.add(token);
            cancel = true;
            if(blockstate_specials.contains(token.value)) {
                //Is special (#default...)
                token.type = TokenType.IDENTIFIER;
                this.stream.write(Token.merge(TokenType.BLOCKSTATE, tokenBuffer.toArray(new Token[0])),true);
                tokenBuffer.clear();

                bufferData.put("IS_BLOCKSTATE", FALSE);
                bufferData.put("BLOCKSTATE_PHASE", "NONE");
            } else {
                //Not special (#variant...)

                bufferData.put("BLOCKSTATE_PHASE", "EQUALS");
            }
        } else if(token.value.equals("=") && bufferData.get("BLOCKSTATE_PHASE").equals("EQUALS")) {
            tokenBuffer.add(token);
            bufferData.put("BLOCKSTATE_PHASE", "VALUE");
            cancel = true;
        } else if((token.type == TokenType.IDENTIFIER || token.type == TokenType.BOOLEAN) && bufferData.get("BLOCKSTATE_PHASE").equals("VALUE")) {
            tokenBuffer.add(token);
            bufferData.put("BLOCKSTATE_PHASE", "NEXT");
            cancel = true;
        } else if(bufferData.get("BLOCKSTATE_PHASE").equals("NEXT")) {
            if(token.type == TokenType.COMMA) {
                tokenBuffer.add(token);
                bufferData.put("BLOCKSTATE_PHASE", "KEY");
                cancel = true;
            } else if(token.value.equals(blockstate_end)) {
                tokenBuffer.add(token);
                stream.write(Token.merge(TokenType.BLOCKSTATE, tokenBuffer.toArray(new Token[0])),true);
                tokenBuffer.clear();

                bufferData.put("IS_BLOCKSTATE", FALSE);
                bufferData.put("BLOCKSTATE_PHASE", "NONE");
                cancel = true;
            } else {
                stream.write(Token.merge(TokenType.BLOCKSTATE, tokenBuffer.toArray(new Token[0])),true);
                tokenBuffer.clear();

                bufferData.put("IS_BLOCKSTATE", FALSE);
                bufferData.put("BLOCKSTATE_PHASE", "NONE");
            }
        } else if(bufferData.get("IS_BLOCKSTATE") == TRUE) {
            //Whoops something went wrong

            stream.write(Token.merge(TokenType.BLOCKSTATE, tokenBuffer.toArray(new Token[0])),true);
            tokenBuffer.clear();

            bufferData.put("IS_BLOCKSTATE", FALSE);
            bufferData.put("BLOCKSTATE_PHASE", "NONE");

        }

        return cancel;
    }

    private boolean analyzeAnnotation(Token token) {
        if(token.type == TokenType.ANNOTATION_MARKER && bufferData.get("ANNOTATION_PHASE").equals("NONE")) {
            bufferData.put("IS_ANNOTATION", TRUE);
            bufferData.put("ANNOTATION_PHASE", "ANNOTATION");
        }
        if(bufferData.get("IS_ANNOTATION") == TRUE) {
            token.attributes.put("IS_ANNOTATION", true);
            switch(bufferData.get("ANNOTATION_PHASE")) {
                case "ANNOTATION": {
                    bufferData.put("ANNOTATION_PHASE", "IDENTIFIER");
                    break;
                } case "IDENTIFIER": {
                    if(token.type == TokenType.IDENTIFIER) {
                        token.attributes.put("IS_ANNOTATION_HEADER", true);
                        bufferData.put("ANNOTATION_PHASE", "BRACE_OPEN");
                    } else {
                        bufferData.put("IS_ANNOTATION", FALSE);
                        bufferData.put("ANNOTATION_PHASE", "NONE");
                    }
                    break;
                } case "BRACE_OPEN": {
                    if(token.type == TokenType.BRACE && token.attributes.get("BRACE_STYLE") == CraftrTokenAttributes.PARENTHESES && token.attributes.get("BRACE_TYPE") == CraftrTokenAttributes.OPENING_BRACE) {
                        bufferData.put("ANNOTATION_PHASE", "BRACE_CLOSE");
                    } else {
                        bufferData.put("IS_ANNOTATION", FALSE);
                        bufferData.put("ANNOTATION_PHASE", "NONE");
                    }
                    break;
                } case "BRACE_CLOSE": {
                    if(token.type == TokenType.BRACE && token.attributes.get("BRACE_STYLE") == CraftrTokenAttributes.PARENTHESES && token.attributes.get("BRACE_TYPE") == CraftrTokenAttributes.CLOSING_BRACE) {
                        bufferData.put("IS_ANNOTATION", FALSE);
                        bufferData.put("ANNOTATION_PHASE", "NONE");
                    }
                    break;
                } case "NONE": {
                    break;
                } default: {
                    bufferData.put("IS_ANNOTATION", FALSE);
                    bufferData.put("ANNOTATION_PHASE", "NONE");
                }
            }
        }
        return false;
    }

    private void giveAttributes(Token token) {
        token.attributes.put(CraftrTokenAttributes.IS_PSEUDO_KEYWORD, pseudo_keywords.contains(token.value));

        //Enum and entities
        boolean isEnum = enums.contains(token.value);
        if(token.type == TokenType.IDENTIFIER) {
            token.attributes.put(CraftrTokenAttributes.IS_ENUM, isEnum);
            if(isEnum) {
                bufferData.put("ENUM_PHASE","DOT");
                bufferData.put("ENUM_NAME",token.value);
            }
            token.attributes.put(CraftrTokenAttributes.IS_ENTITY, entities.contains(token.value));
        }
        //Braces
        if(token.type == TokenType.BRACE) {
            if("()".contains(token.value)) token.attributes.put("BRACE_STYLE", CraftrTokenAttributes.PARENTHESES);
            if("{}".contains(token.value)) token.attributes.put("BRACE_STYLE", CraftrTokenAttributes.CURLY_BRACES);
            if("[]".contains(token.value)) token.attributes.put("BRACE_STYLE", CraftrTokenAttributes.SQUARE_BRACES);

            if("({[".contains(token.value)) token.attributes.put("BRACE_TYPE", CraftrTokenAttributes.OPENING_BRACE);
            if(")}]".contains(token.value)) token.attributes.put("BRACE_TYPE", CraftrTokenAttributes.CLOSING_BRACE);
        }

        //Enum values
        if(token.type == TokenType.DOT && bufferData.get("ENUM_PHASE").equals("DOT")) {
            bufferData.put("ENUM_PHASE","ENUM_VALUE");
        } else if(token.type == TokenType.IDENTIFIER && bufferData.get("ENUM_PHASE").equals("ENUM_VALUE")) {
            String enumName = bufferData.get("ENUM_NAME");
            String enumValue = token.value;

            if(enums.contains(enumName)) {
                if(enum_values.get(enums.indexOf(enumName)).contains(enumValue)) {
                    token.attributes.put("IS_ENUM_VALUE", true);
                }
            }
            bufferData.put("ENUM_PHASE","NONE");
        } else if(!isEnum) {
            bufferData.put("ENUM_PHASE","NONE");
        }
    }

    private void classifyKeyword(Token t) {
        if(t.type != TokenType.IDENTIFIER) return;
        for(String p : modifiers) {
            if(t.value.equals(p)) {
                t.type = TokenType.MODIFIER;
                return;
            }
        }
        for(String p : unit_types) {
            if(t.value.equals(p)) {
                t.type = TokenType.UNIT_TYPE;
                return;
            }
        }
        for(String p : unit_actions) {
            if(t.value.equals(p)) {
                t.type = TokenType.UNIT_ACTION;
                return;
            }
        }
        for(String p : data_types) {
            if(t.value.equals(p)) {
                t.type = TokenType.DATA_TYPE;
                return;
            }
        }
        for(String p : keywords) {
            if(t.value.equals(p)) {
                t.type = TokenType.KEYWORD;
                return;
            }
        }
        for(String p : action_keywords) {
            if(t.value.equals(p)) {
                t.type = TokenType.ACTION_KEYWORD;
                return;
            }
        }
        for(String p : booleans) {
            if(t.value.equals(p)) {
                t.type = TokenType.BOOLEAN;
                return;
            }
        }
        for(String p : nulls) {
            if(t.value.equals(p)) {
                t.type = TokenType.NULL;
                return;
            }
        }
    }

    @Override
    public boolean isSignificant(Token token) {
        return token.type != TokenType.COMMENT;
    }

    @Override
    public void putHeaderInfo(Token header) {
        header.attributes.put("TYPE","craftr");
        header.attributes.put("DESC","Craftr Unit File");
    }
}