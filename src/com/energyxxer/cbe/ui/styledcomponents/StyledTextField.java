package com.energyxxer.cbe.ui.styledcomponents;

import com.energyxxer.cbe.ui.components.XTextField;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;

import java.awt.*;

/**
 * Provides a text field that reacts to theme changes and
 * adjusts their own color. It also provides a namespace for
 * more specific paths on the theme file. If the namespace is
 * not specified, it defaults to the general style.
 */
public class StyledTextField extends XTextField {

    private String namespace = null;

    public StyledTextField() {
        this(null,null,-1);
    }

    public StyledTextField(String text) {
        this(text,null,-1);
    }

    public StyledTextField(String text, String namespace) {
        this(text,namespace,-1);
    }

    public StyledTextField(int columns) {
        this(null,null,columns);
    }

    public StyledTextField(int columns, String namespace) {
        this(null,namespace,columns);
    }

    public StyledTextField(String text, String namespace, int columns) {
        if(text != null) this.setText(text);
        if(namespace != null) this.setNamespace(namespace);
        if(columns >= 0) this.setColumns(columns);

        ThemeChangeListener.addThemeChangeListener(t -> {
            if(this.namespace != null) {
                setBackground       (t.getColor(this.namespace + ".textfield.background",           t.getColor("General.textfield.background",          new Color(220, 220, 220))));
                setForeground       (t.getColor(this.namespace + ".textfield.foreground",           t.getColor("General.textfield.foreground",          t.getColor("General.foreground", Color.BLACK))));
                setSelectionColor   (t.getColor(this.namespace + ".textfield.selection.background", t.getColor("General.textfield.selection.background",new Color(50, 100, 175))));
                setSelectedTextColor(t.getColor(this.namespace + ".textfield.selection.foreground", t.getColor("General.textfield.selection.foreground",getForeground())));
                setBorderColor      (t.getColor(this.namespace + ".textfield.border",               t.getColor("General.textfield.border",              new Color(200, 200, 200))));
                setFont(new Font   (t.getString(this.namespace + ".textfield.font",                t.getString("General.textfield.font",                t.getString("General.font","Tahoma"))),0,12));
            } else {
                setBackground       (t.getColor("General.textfield.background",          new Color(220, 220, 220)));
                setForeground       (t.getColor("General.textfield.foreground",          t.getColor("General.foreground", Color.BLACK)));
                setSelectionColor   (t.getColor("General.textfield.selection.background",new Color(50, 100, 175)));
                setSelectedTextColor(t.getColor("General.textfield.selection.foreground",getForeground()));
                setBorderColor      (t.getColor("General.textfield.border",              new Color(200, 200, 200)));
                setFont(new Font   (t.getString("General.textfield.font",                t.getString("General.font","Tahoma")),0,12));
            }
        });
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return this.namespace;
    }
}