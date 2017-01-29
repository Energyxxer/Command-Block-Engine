package com.energyxxer.cbe.ui.editor.behavior.editmanager.edits;

import com.energyxxer.cbe.ui.editor.behavior.AdvancedEditor;
import com.energyxxer.cbe.ui.editor.behavior.caret.CaretProfile;
import com.energyxxer.cbe.ui.editor.behavior.caret.EditorCaret;
import com.energyxxer.cbe.ui.editor.behavior.editmanager.Edit;
import com.energyxxer.cbe.util.StringUtil;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.ArrayList;

/**
 * Created by User on 1/5/2017.
 */
public class SimpleEdit implements Edit {

    private String value;
    private ArrayList<Integer> locations;
    private ArrayList<String> previousValues = new ArrayList<>();

    public SimpleEdit(String value, CaretProfile locations) {
        this.value = value;
        this.locations = new ArrayList<>(locations.asList());
    }

    public boolean redo(AdvancedEditor editor) {

        if(value.length() <= 0) return false;

        Document doc = editor.getDocument();
        EditorCaret caret = editor.getCaret();
        try {
            String result = doc.getText(0, doc.getLength()); //Result

            int characterDrift = 0;

            previousValues.clear();

            for (int i = 0; i < locations.size() - 1; i += 2) {
                int start = locations.get(i) + characterDrift;
                int end = locations.get(i + 1) + characterDrift;
                if(end < start) {
                    int temp = start;
                    start = end;
                    end = temp;
                }
                previousValues.add(result.substring(start, end));
                result = result.substring(0, start) + value + result.substring(end);

                int diff = value.length() - (end - start);

                int oldDocLength = doc.getLength();

                if(diff > 0) doc.insertString(oldDocLength, StringUtil.repeat(" ", diff), null);
                caret.pushFrom(start, diff);
                ((AbstractDocument) doc).replace(start, end - start, value, null);
                if(diff > 0) doc.remove(oldDocLength+diff, diff);

                characterDrift += value.length() - (end - start);
            }

        } catch(BadLocationException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean undo(AdvancedEditor editor) {

        if(value.length() <= 0) return false;

        Document doc = editor.getDocument();
        EditorCaret caret = editor.getCaret();
        try {
            String str = doc.getText(0, doc.getLength());

            for (int i = 0; i < locations.size() - 1; i += 2) {
                int start = locations.get(i);
                int resultEnd = start + value.length();
                if(resultEnd < start) {
                    int temp = start;
                    start = resultEnd;
                    resultEnd = temp;
                }

                String previousValue = previousValues.get(i / 2);

                str = str.substring(0, start) + previousValue + str.substring(resultEnd);

                int diff = previousValue.length() - (resultEnd - start);

                int oldDocLength = doc.getLength();

                doc.insertString(oldDocLength, StringUtil.repeat(" ", Math.abs(diff)), null);
                ((AbstractDocument) doc).replace(start, resultEnd - start, previousValue, null);
                caret.pushFrom(start, diff);
                doc.remove(oldDocLength+diff, Math.abs(diff));
            }

        } catch(BadLocationException e) {
            e.printStackTrace();
        }
        return true;
    }
}