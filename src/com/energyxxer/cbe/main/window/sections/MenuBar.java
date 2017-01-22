package com.energyxxer.cbe.main.window.sections;

import com.energyxxer.cbe.global.Preferences;
import com.energyxxer.cbe.global.ProjectManager;
import com.energyxxer.cbe.main.window.Window;
import com.energyxxer.cbe.ui.common.MenuItems;
import com.energyxxer.cbe.ui.dialogs.settings.Settings;
import com.energyxxer.cbe.ui.styledcomponents.StyledMenu;
import com.energyxxer.cbe.ui.styledcomponents.StyledMenuItem;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by User on 12/15/2016.
 */
public class MenuBar extends JMenuBar {

    {
        ThemeChangeListener.addThemeChangeListener(t -> {
            this.setBackground(t.getColor("MenuBar.background", new Color(215, 215, 215)));
            this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, t.getColor("MenuBar.border", new Color(150, 150, 150))));
        });

        this.setPreferredSize(new Dimension(0, 20));

        {
            StyledMenu menu = new StyledMenu(" File ");

            menu.setMnemonic(KeyEvent.VK_F);

            // --------------------------------------------------


            StyledMenu newMenu = MenuItems.newMenu("New                    ");
            menu.add(newMenu);

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Save", "save");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Save As", "save_as");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 3));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Save All", "save_all");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 10));
                menu.add(item);
            }

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Close");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Close All");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, 3));
                menu.add(item);
            }


            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Move");
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Rename", "rename");
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Refresh", "reload");
                item.addActionListener(e -> Window.explorer.generateProjectList());
                menu.add(item);
            }

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Exit");
                item.addActionListener(e -> Window.close());
                menu.add(item);
            }

            // --------------------------------------------------

            this.add(menu);
        }

        {
            StyledMenu menu = new StyledMenu(" Edit ");
            menu.setMnemonic(KeyEvent.VK_E);

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Undo", "undo");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Redo", "redo");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Copy");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Cut");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Paste");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Delete");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
                menu.add(item);
            }

            // --------------------------------------------------

            this.add(menu);
        }

        {
            StyledMenu menu = new StyledMenu(" Project ");
            menu.setMnemonic(KeyEvent.VK_P);

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Generate                    ", "export");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 9));
                menu.add(item);
            }

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Properties");
                item.addActionListener(e -> {
                    if (ProjectManager.getSelected() != null) ProjectManager.getSelected().showPropertiesDialog();
                });
                menu.add(item);
            }

            // --------------------------------------------------

            this.add(menu);
        }

        {
            StyledMenu menu = new StyledMenu(" Debug ");
            menu.setMnemonic(KeyEvent.VK_D);

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Reset Preferences", "warn");
                item.addActionListener(e -> {
                    int confirmation = JOptionPane.showConfirmDialog(null,
                            "        Are you sure you want to reset all saved settings?        ",
                            "Reset Preferences? ", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        Preferences.reset();
                    }
                });
                menu.add(item);
            }

            // --------------------------------------------------

            this.add(menu);
        }

        {
            StyledMenu menu = new StyledMenu(" Window ");
            menu.setMnemonic(KeyEvent.VK_W);

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Settings");

                item.addActionListener(e -> Settings.show());
                menu.add(item);
            }

            this.add(menu);
        }
    }
}
