package com.energyxxer.craftr.ui.editor.behavior.editmanager.edits;

import com.energyxxer.craftr.ui.editor.behavior.AdvancedEditor;
import com.energyxxer.craftr.ui.editor.behavior.caret.CaretProfile;
import com.energyxxer.craftr.ui.editor.behavior.caret.EditorCaret;
import com.energyxxer.craftr.ui.editor.behavior.editmanager.Edit;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.ArrayList;

/**
 * Created by User on 1/10/2017.
 */
public class InsertionEdit implements Edit {
    private String value;
    private ArrayList<String> previousValues = new ArrayList<>();
    private CaretProfile previousProfile = new CaretProfile();
    //private CaretProfile nextProfile = null;

    public InsertionEdit(String value, AdvancedEditor editor) {
        this.value = value;
        previousProfile = editor.getCaret().getProfile();
    }

    @Override
    public boolean redo(AdvancedEditor editor) {

        if(value.length() <= 0) return false;

        Document doc = editor.getDocument();
        EditorCaret caret = editor.getCaret();
        try {
            String result = doc.getText(0, doc.getLength()); //Result

            int characterDrift = 0;

            previousValues.clear();
            CaretProfile nextProfile = new CaretProfile();

            for (int i = 0; i < previousProfile.size() - 1; i += 2) {
                int start = previousProfile.get(i) + characterDrift;
                int end = previousProfile.get(i + 1) + characterDrift;
                if(end < start) {
                    int temp = start;
                    start = end;
                    end = temp;
                }
                previousValues.add(result.substring(start, end));
                result = result.substring(0, start) + value + result.substring(end);

                nextProfile.add(start+value.length(),start+value.length());

                ((AbstractDocument) doc).replace(start, end - start, value, null);

                characterDrift += value.length() - (end - start);
            }

            caret.setProfile(nextProfile);

        } catch(BadLocationException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean undo(AdvancedEditor editor) {

        if(value.length() <= 0) return false;

        Document doc = editor.getDocument();
        EditorCaret caret = editor.getCaret();
        try {
            String str = doc.getText(0, doc.getLength());

            for (int i = 0; i < previousProfile.size() - 1; i += 2) {
                int start = previousProfile.get(i);
                int resultEnd = start + value.length();
                if(resultEnd < start) {
                    int temp = start;
                    start = resultEnd;
                    resultEnd = temp;
                }

                String previousValue = previousValues.get(i / 2);

                str = str.substring(0, start) + previousValue + str.substring(resultEnd);

                ((AbstractDocument) doc).replace(start, resultEnd - start, previousValue, null);
            }

            caret.setProfile(previousProfile);

        } catch(BadLocationException e) {
            e.printStackTrace();
        }
        return true;
    }
}