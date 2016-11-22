package com.energyxxer.cbe.compile.analysis;


import com.energyxxer.cbe.compile.analysis.token.TokenType;
import com.energyxxer.cbe.compile.analysis.token.matching.TokenGroupMatch;
import com.energyxxer.cbe.compile.analysis.token.matching.TokenItemMatch;
import com.energyxxer.cbe.compile.analysis.token.matching.TokenListMatch;
import com.energyxxer.cbe.compile.analysis.token.matching.TokenStructureMatch;

public class LangStructures {
	public final static TokenStructureMatch UNIT_DECLARATION;
	public final static TokenStructureMatch DATA_TYPE;
	public final static TokenStructureMatch METHOD_CALL;
	public final static TokenStructureMatch VALUE;
	public final static TokenStructureMatch EXPRESSION;
	public final static TokenStructureMatch STATEMENT;
	public final static TokenStructureMatch CODE_BLOCK;
	public final static TokenStructureMatch ANNOTATION;

	static {
		UNIT_DECLARATION = new TokenStructureMatch("UNIT_DECLARATION");
		DATA_TYPE = new TokenStructureMatch("DATA_TYPE");
		EXPRESSION = new TokenStructureMatch("EXPRESSION");
		VALUE = new TokenStructureMatch("VALUE");
		METHOD_CALL = new TokenStructureMatch("METHOD_CALL");
		STATEMENT = new TokenStructureMatch("STATEMENT");
		CODE_BLOCK = new TokenStructureMatch("CODE_BLOCK");
		ANNOTATION = new TokenStructureMatch("ANNOTATION");
		
		{
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenItemMatch(TokenType.DATA_TYPE));
			{
				TokenGroupMatch g2 = new TokenGroupMatch(true);
				g2.append(new TokenItemMatch(TokenType.BRACE,"["));
				g2.append(new TokenItemMatch(TokenType.BRACE,"]"));
				g.append(new TokenListMatch(g2));
			}
		}
		
		{
			// <ANNOTATION> <IDENTIFIER> <BRACE: (> [-VALUE-...] <BRACE: )>
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenItemMatch(TokenType.ANNOTATION_MARKER));;
			g.append(METHOD_CALL);
			ANNOTATION.add(g);
		}

		{
			// <-ANNOTATION-> <QUALIFIER> [UNIT_TYPE:entity] [IDENTIFIER] <[UNIT_ACTION:base] [IDENTIFIER]>
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenGroupMatch(true).append(ANNOTATION));
			g.append(new TokenListMatch(TokenType.QUALIFIER, true));
			g.append(new TokenItemMatch(TokenType.UNIT_TYPE));
			g.append(new TokenItemMatch(TokenType.IDENTIFIER).setName("UNIT_NAME"));
			
			{
				TokenGroupMatch g2 = new TokenGroupMatch(true).setName("UNIT_ACTION");
				g2.append(new TokenItemMatch(TokenType.UNIT_ACTION));
				g2.append(new TokenListMatch(new TokenItemMatch(TokenType.IDENTIFIER).setName("UNIT_ACTION_REFERENCE"),new TokenItemMatch(TokenType.COMMA)));
				g.append(new TokenListMatch(g2));
			}

			

			UNIT_DECLARATION.add(g);
		}
		
		{
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenListMatch(TokenType.IDENTIFIER,TokenType.DOT));
			g.append(new TokenItemMatch(TokenType.BRACE,"("));
			g.append(new TokenListMatch(VALUE));
			g.append(new TokenItemMatch(TokenType.BRACE,")"));
			METHOD_CALL.add(g);
		}
		
		
		{
			
			// [VALUE OPERATOR...]
			
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenItemMatch(TokenType.OPERATOR, "-", true));
				g.append(new TokenListMatch(VALUE, new TokenItemMatch(TokenType.OPERATOR)));
				EXPRESSION.add(g);
			}

			// [VALUE OPERATOR...]
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenItemMatch(TokenType.BRACE,"("));
				g.append(new TokenListMatch(VALUE, new TokenItemMatch(TokenType.OPERATOR)));
				g.append(new TokenItemMatch(TokenType.BRACE,")"));
				EXPRESSION.add(g);
			}
			
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenItemMatch(TokenType.IDENTIFIER));
				g.append(new TokenItemMatch(TokenType.IDENTIFIER_OPERATOR));
				EXPRESSION.add(g);
			}
			
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenItemMatch(TokenType.IDENTIFIER_OPERATOR));
				g.append(new TokenItemMatch(TokenType.IDENTIFIER));
				EXPRESSION.add(g);
			}
			
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenListMatch(TokenType.QUALIFIER,true));
				g.append(DATA_TYPE);
				{
					TokenGroupMatch g2 = new TokenGroupMatch(true);
					g2.append(new TokenItemMatch(TokenType.BRACE,"["));
					g2.append(new TokenItemMatch(TokenType.BRACE,"]"));
					g.append(g2);
				}
				g.append(new TokenItemMatch(TokenType.IDENTIFIER));
				{
					TokenGroupMatch g2 = new TokenGroupMatch(true);
					g2.append(new TokenItemMatch(TokenType.OPERATOR,"="));
					g2.append(VALUE);
					g.append(g2);
				}
				EXPRESSION.add(g);
			}
		}
		

		{
			// [IDENTIFIER]
			VALUE.add(new TokenItemMatch(TokenType.IDENTIFIER));
			// [NUMBER]
			VALUE.add(new TokenItemMatch(TokenType.NUMBER));
			// [BOOLEAN]
			VALUE.add(new TokenItemMatch(TokenType.BOOLEAN));
			// [-EXPRESSION-]
			VALUE.add(EXPRESSION);
			// [-METHOD_CALL-]
			VALUE.add(METHOD_CALL);
			// [STRING_LITERAL]
			VALUE.add(new TokenItemMatch(TokenType.STRING_LITERAL));
			// [IDENTIFIER DOT...]
			VALUE.add(new TokenListMatch(TokenType.IDENTIFIER,TokenType.DOT));
			// [NEGATION_OPERATOR][-VALUE-]
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenItemMatch(TokenType.LOGICAL_NEGATION_OPERATOR));
			g.append(VALUE);
			VALUE.add(g);
		}
		
		
		{
			{
				TokenGroupMatch g = new TokenGroupMatch();
					g.append(EXPRESSION);
				
				g.append(new TokenItemMatch(TokenType.END_OF_STATEMENT));
				STATEMENT.add(g);
			}
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenItemMatch(TokenType.ACTION_KEYWORD));
			
				g.append(new TokenItemMatch(TokenType.END_OF_STATEMENT));
				STATEMENT.add(g);
			}
		}
		
		
		{
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenItemMatch(TokenType.BRACE,"{"));
				g.append(new TokenListMatch(STATEMENT));
				g.append(new TokenItemMatch(TokenType.BRACE,"}"));
				
				CODE_BLOCK.add(g);
			}
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(STATEMENT);
				
				CODE_BLOCK.add(g);
			}
		}
		
		TokenStructureMatch EXPRESSION_BLOCK = new TokenStructureMatch("EXPRESSION_BLOCK",true);

		EXPRESSION_BLOCK.add(EXPRESSION);
		{
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenItemMatch(TokenType.BRACE,"{"));
			g.append(new TokenListMatch(STATEMENT));
			g.append(new TokenItemMatch(TokenType.BRACE,"}"));
			
			EXPRESSION_BLOCK.add(g);
		}
		
		TokenGroupMatch IF_STATEMENT = new TokenGroupMatch();
		TokenStructureMatch ELSE_STATEMENT = new TokenStructureMatch("ELSE_STATEMENT",true);
		
		{
			TokenGroupMatch g = new TokenGroupMatch(true);
			g.append(new TokenItemMatch(TokenType.KEYWORD,"else"));
			g.append(CODE_BLOCK);
			ELSE_STATEMENT.add(g);
		}
		{
			TokenGroupMatch g = new TokenGroupMatch(true);
			g.append(new TokenItemMatch(TokenType.KEYWORD,"else"));
			g.append(IF_STATEMENT);
			ELSE_STATEMENT.add(g);
		}
		
		
		{
			IF_STATEMENT.append(new TokenItemMatch(TokenType.KEYWORD,"if"));
			IF_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,"("));
			IF_STATEMENT.append(EXPRESSION);
			IF_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,")"));
			IF_STATEMENT.append(CODE_BLOCK);
			IF_STATEMENT.append(ELSE_STATEMENT);
		}
		STATEMENT.add(IF_STATEMENT);
		
		TokenGroupMatch FOR_STATEMENT = new TokenGroupMatch();
		{
			//for
			FOR_STATEMENT.append(new TokenItemMatch(TokenType.KEYWORD,"for"));
			//(
			FOR_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,"("));
			//int i = 0
			FOR_STATEMENT.append(EXPRESSION_BLOCK);
			//;
			FOR_STATEMENT.append(new TokenItemMatch(TokenType.END_OF_STATEMENT));
			//i < str.length();
			FOR_STATEMENT.append(EXPRESSION_BLOCK);
			//;
			FOR_STATEMENT.append(new TokenItemMatch(TokenType.END_OF_STATEMENT));
			//i++
			FOR_STATEMENT.append(EXPRESSION_BLOCK);
			//)
			FOR_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,")"));
			//{...}
			FOR_STATEMENT.append(CODE_BLOCK);
			STATEMENT.add(FOR_STATEMENT);
		}
		
		TokenGroupMatch WHILE_STATEMENT = new TokenGroupMatch();
		{
			WHILE_STATEMENT.append(new TokenItemMatch(TokenType.KEYWORD,"while"));
			WHILE_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,"("));
			WHILE_STATEMENT.append(EXPRESSION);
			WHILE_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,")"));
			WHILE_STATEMENT.append(CODE_BLOCK);
		}
		STATEMENT.add(WHILE_STATEMENT);
		
		TokenGroupMatch SWITCH_STATEMENT = new TokenGroupMatch();
		{
			SWITCH_STATEMENT.append(new TokenItemMatch(TokenType.KEYWORD,"switch"));
			SWITCH_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,"("));
			SWITCH_STATEMENT.append(VALUE);
			SWITCH_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,")"));
			SWITCH_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,"{"));
			{
				TokenStructureMatch s = new TokenStructureMatch("switch_case");
				
				{
					TokenGroupMatch g = new TokenGroupMatch();
					g.append(new TokenItemMatch(TokenType.KEYWORD,"case"));
					g.append(VALUE);
					g.append(new TokenItemMatch(TokenType.COLON));
					g.append(CODE_BLOCK);
					s.add(g);
				}
				{
					TokenGroupMatch g = new TokenGroupMatch();
					g.append(new TokenItemMatch(TokenType.KEYWORD,"default"));
					g.append(new TokenItemMatch(TokenType.COLON));
					g.append(CODE_BLOCK);
					s.add(g);
				}
				
				SWITCH_STATEMENT.append(new TokenListMatch(s));
			}
			SWITCH_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,"}"));
		}
		STATEMENT.add(SWITCH_STATEMENT);
	}
}