package com.energyxxer.cbe.ui.styledcomponents;

import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by User on 12/14/2016.
 */
public class StyledPopupMenu extends JPopupMenu {

    private String namespace = null;

    public StyledPopupMenu() {
        this(null,null);
    }

    public StyledPopupMenu(String label) {
        this(label,null);
    }

    public StyledPopupMenu(String label, String namespace) {
        if(label != null) setLabel(label);
        if(namespace != null) this.setNamespace(namespace);

        ThemeChangeListener.addThemeChangeListener(t -> {
            if (this.namespace != null) {
                setBackground(t.getColor(this.namespace + ".menu.background",t.getColor("General.menu.background",new Color(215, 215, 215))));
                setBorder(BorderFactory.createMatteBorder(1,1,1,1,t.getColor(this.namespace + ".menu.background",t.getColor("General.menu.border",new Color(200, 200, 200)))));
            } else {
                setBackground(t.getColor("General.menu.background",new Color(215, 215, 215)));
                setBorder(BorderFactory.createMatteBorder(1,1,1,1,t.getColor("General.menu.border",new Color(200, 200, 200))));
            }
        });
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void addSeparator() {
        this.add(new StyledSeparator(namespace));
    }
}