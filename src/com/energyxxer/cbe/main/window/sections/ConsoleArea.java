package com.energyxxer.cbe.main.window.sections;

import com.energyxxer.cbe.global.Console;
import com.energyxxer.cbe.global.TabManager;
import com.energyxxer.cbe.ui.ToolbarButton;
import com.energyxxer.cbe.ui.scrollbar.ScrollbarUI;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;
import com.energyxxer.cbe.util.out.ConsoleOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by User on 12/15/2016.
 */
public class ConsoleArea extends JPanel {

    private static final int CONSOLE_HEIGHT = 200;

    {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(0, CONSOLE_HEIGHT));
        ThemeChangeListener.addThemeChangeListener(t -> this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, t.getColor("Console.header.border",new Color(200, 200, 200)))));

        JPanel consoleHeader = new JPanel(new BorderLayout());
        ThemeChangeListener.addThemeChangeListener(t -> consoleHeader.setBackground(t.getColor("Console.header.background",new Color(235, 235, 235))));
        consoleHeader.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        consoleHeader.setPreferredSize(new Dimension(0, 25));

        JLabel consoleLabel = new JLabel("Console");
        ThemeChangeListener.addThemeChangeListener(t -> {
            consoleLabel.setForeground(t.getColor("Console.header.foreground",Color.BLACK));
            consoleLabel.setFont(new Font(t.getString("Console.header.font",t.getString("General.font","Tahoma")), 0, 12));
        });
        consoleHeader.add(consoleLabel, BorderLayout.WEST);

        JPanel consoleActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        consoleActionPanel.setOpaque(false);

        ToolbarButton toggle = new ToolbarButton("toggle", true);
        toggle.setToolTipText("Toggle Console");
        toggle.setPreferredSize(new Dimension(20,20));

        toggle.addActionListener(e -> {
            if (this.getPreferredSize().height == 25) {
                this.setPreferredSize(new Dimension(0, CONSOLE_HEIGHT));
            } else {
                this.setPreferredSize(new Dimension(0, 25));
            }
            this.revalidate();
            this.repaint();
        });


        ToolbarButton clear = new ToolbarButton("clear", true);
        clear.setToolTipText("Clear Console");
        clear.setPreferredSize(new Dimension(20,20));


        consoleActionPanel.add(clear);
        consoleActionPanel.add(toggle);
        consoleHeader.add(consoleActionPanel, BorderLayout.EAST);

        this.add(consoleHeader, BorderLayout.NORTH);

        JTextPane console = new JTextPane();
        ThemeChangeListener.addThemeChangeListener(t -> {
            console.setBackground(t.getColor("Console.background",Color.WHITE));
            console.setSelectionColor(t.getColor("Console.selection.background",t.getColor("General.textfield.selection.background",new Color(50, 100, 175))));
            console.setSelectedTextColor(t.getColor("Console.selection.foreground",t.getColor("General.textfield.selection.foreground",t.getColor("Console.foreground",t.getColor("General.foreground",Color.BLACK)))));
            console.setFont(new Font(t.getString("Console.font",t.getString("Editor.font","monospaced")), 0, 12));
            console.setForeground(t.getColor("Console.foreground",Color.BLACK));

            if(console.getStyle("warning") != null) console.removeStyle("warning");
            if(console.getStyle("error") != null) console.removeStyle("error");

            Style warningStyle = console.addStyle("warning", null);
            StyleConstants.setForeground(warningStyle, t.getColor("Console.warning", new Color(255, 140, 0)));

            Style errorStyle = console.addStyle("error", null);
            StyleConstants.setForeground(errorStyle, t.getColor("Console.error", new Color(200,50,50)));

            Style debugStyle = console.addStyle("debug", null);
            StyleConstants.setForeground(debugStyle, new Color(104,151,187));
        });
        clear.addActionListener(e -> {
            try {
                console.getDocument().remove(0,console.getDocument().getLength());
            } catch(BadLocationException ble) {}
        });
        console.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AttributeSet hyperlink = console.getStyledDocument().getCharacterElement(console.viewToModel(e.getPoint())).getAttributes();
                if(hyperlink.containsAttribute("IS_HYPERLINK",true)) {
                    String path = (String) hyperlink.getAttribute("PATH");
                    int location = Integer.parseInt((String) hyperlink.getAttribute("LOCATION"));
                    int length = Integer.parseInt((String) hyperlink.getAttribute("LENGTH"));

                    TabManager.openTab(path, location, length);
                }
            }
        });
        console.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                AttributeSet hyperlink = console.getStyledDocument().getCharacterElement(console.viewToModel(e.getPoint())).getAttributes();

                console.setCursor((hyperlink.containsAttribute("IS_HYPERLINK",true))
                        ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
            }
        });
        console.setEditable(false);
        console.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //console.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));

        //ThemeChangeListener.addThemeChangeListener(t -> textConsoleOut.update());

        Console.addInfoStream(new ConsoleOutputStream(console));
        Console.addWarnStream(new ConsoleOutputStream(console,"warning"));
        Console.addErrStream(new ConsoleOutputStream(console,"error"));
        Console.addDebugStream(new ConsoleOutputStream(console,"debug"));

        /*consoleOut = new PrintStream(textConsoleOut);
        System.setOut(new PrintStream(new MultiOutputStream(consoleOut, System.out)));
        System.setErr(new PrintStream(new MultiOutputStream(consoleOut, System.err)));*/

        JScrollPane consoleScrollPane = new JScrollPane(console);

        //consoleScrollPane.setLayout(new OverlayScrollPaneLayout());

        consoleScrollPane.getVerticalScrollBar().setUI(new ScrollbarUI(consoleScrollPane, 20));
        consoleScrollPane.getHorizontalScrollBar().setUI(new ScrollbarUI(consoleScrollPane, 20));
        consoleScrollPane.getVerticalScrollBar().setOpaque(false);
        consoleScrollPane.getHorizontalScrollBar().setOpaque(false);

        ThemeChangeListener.addThemeChangeListener(t -> {
            consoleScrollPane.setBackground(console.getBackground());
            consoleScrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, t.getColor("Console.header.border",new Color(200, 200, 200))));
        });

        this.add(consoleScrollPane, BorderLayout.CENTER);

    }
}