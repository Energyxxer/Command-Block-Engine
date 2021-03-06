package com.energyxxer.craftr.ui.editor;

import com.energyxxer.craftr.global.TabManager;
import com.energyxxer.craftr.ui.Tab;
import com.energyxxer.craftr.ui.display.DisplayModule;
import com.energyxxer.craftr.ui.scrollbar.OverlayScrollPaneLayout;
import com.energyxxer.craftr.ui.theme.Theme;
import com.energyxxer.craftr.ui.theme.ThemeManager;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;
import com.energyxxer.craftr.util.linenumber.TextLineNumber;
import com.energyxxer.craftrlang.compiler.Lang;
import com.energyxxer.util.out.Console;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Display module for the main text editor of the program.
 */
public class CraftrEditorModule extends JScrollPane implements DisplayModule, UndoableEditListener, MouseListener, ThemeChangeListener {

    Tab associatedTab;

    public CraftrEditorComponent editorComponent;
    private TextLineNumber tln;
	protected Theme syntax;

	private ArrayList<String> styles = new ArrayList<>();
	HashMap<String, String[]> parserStyles = new HashMap<>();

    //public long lastToolTip = new Date().getTime();

	public CraftrEditorModule(Tab tab) {
		super();
        associatedTab = tab;

        editorComponent = new CraftrEditorComponent(this);
        //editorComponent.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));

		JPanel container = new JPanel(new BorderLayout());
		container.add(editorComponent);
        super.setViewportView(container);

        tln = new TextLineNumber(editorComponent, this);
        tln.setPadding(10);

		this.setBorder(BorderFactory.createEmptyBorder());

		KeyStroke saveKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK);

		KeyStroke reloadKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);

		//editorComponent.getInputMap().put(undoKeystroke, "undoKeystroke");
		//editorComponent.getInputMap().put(redoKeystroke, "redoKeystroke");
		editorComponent.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(reloadKeystroke, "reloadKeystroke");
		editorComponent.getInputMap().put(saveKeystroke, "saveKeystroke");

		editorComponent.getActionMap().put("saveKeystroke", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Tab st = TabManager.getSelectedTab();
				if(st != null) st.save();
			}
		});

		editorComponent.getActionMap().put("reloadKeystroke", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setTextToFileContents();
			}
		});

		// editorComponent.addMouseMotionListener(hints = new EditorHints(this));
		editorComponent.addMouseListener(this);

		this.setRowHeaderView(tln);

		this.setLayout(new OverlayScrollPaneLayout(this));

		this.getVerticalScrollBar().setUnitIncrement(17);
		this.getHorizontalScrollBar().setUnitIncrement(17);

		/*linePainter.addPaintListener(() -> {
			this.getVerticalScrollBar().repaint();
			this.getHorizontalScrollBar().repaint();
		});*/

		addThemeChangeListener();

		setTextToFileContents();
	}

	private void setTextToFileContents() {
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(this.associatedTab.path));
			String s = new String(encoded);
			setText(s);
			editorComponent.setCaretPosition(0);
			startEditListeners();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startEditListeners() {
		editorComponent.getDocument().addUndoableEditListener(this);
	}

	private void clearStyles() {
		for(String key : this.styles) {
			editorComponent.removeStyle(key);
		}
		for(String key : this.parserStyles.keySet()) {
			editorComponent.removeStyle(key);
		}
		this.styles.clear();
		this.parserStyles.clear();
	}

	private void setSyntax(Theme newSyntax) {
		if(newSyntax == null) {
			syntax = null;
			clearStyles();
			return;
		}
		if(newSyntax.getThemeType() != Theme.ThemeType.SYNTAX_THEME) {
			Console.err.println("Theme \"" + newSyntax + "\" is not a syntax theme!");
			return;
		}

		this.syntax = newSyntax;
		clearStyles();
		for(String value : syntax.getValues().keySet()) {
			if(!value.contains(".")) continue;
			//if(sections.length > 2) continue;

			String name = value.substring(0,value.lastIndexOf("."));
			Style style = editorComponent.getStyle(name);
			if(style == null) {
				style = editorComponent.addStyle(name, null);
				this.styles.add(name);
				if(name.startsWith("$") && name.contains(".")) {
					parserStyles.put(name, name.substring(1).toUpperCase().split("\\."));
				}
			}
			switch(value.substring(value.lastIndexOf(".")+1)) {
				case "foreground": {
					StyleConstants.setForeground(style, syntax.getColor(value));
					break;
				}
				case "background": {
					StyleConstants.setBackground(style, syntax.getColor(value));
					break;
				}
				case "italic": {
					StyleConstants.setItalic(style, syntax.getBoolean(value));
					break;
				}
				case "bold": {
					StyleConstants.setBold(style, syntax.getBoolean(value));
					break;
				}
			}
		}
	}

	public void setText(String text) {
		editorComponent.setText(text);

		editorComponent.highlight();
	}

	public String getText() {
		return editorComponent.getText();
	}

	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		if (!e.getEdit().getPresentationName().equals("style change")) {
			editorComponent.highlight();
			associatedTab.onEdit();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

	@Override
	public void themeChanged(Theme t) {
		editorComponent.setBackground(t.getColor(Color.WHITE, "Editor.background"));
		setBackground(editorComponent.getBackground());
		editorComponent.setForeground(t.getColor(Color.BLACK, "Editor.foreground","General.foreground"));
		editorComponent.setCaretColor(editorComponent.getForeground());
		editorComponent.setSelectionColor(t.getColor(new Color(50, 100, 175), "Editor.selection.background"));
		editorComponent.setSelectedTextColor(t.getColor(editorComponent.getForeground(), "Editor.selection.foreground"));
		editorComponent.setCurrentLineColor(t.getColor(new Color(235, 235, 235), "Editor.currentLine.background"));
		editorComponent.setFont(new Font(t.getString("Editor.font","default:monospaced"), 0, 12));
		tln.setBackground(t.getColor(new Color(235, 235, 235), "Editor.lineNumber.background"));
		tln.setForeground(t.getColor(new Color(150, 150, 150), "Editor.lineNumber.foreground"));
		//tln current line background
		tln.setCurrentLineForeground(t.getColor(tln.getForeground(), "CraftrEditorModule.lineNumber.currentLine.foreground"));
		tln.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createMatteBorder(
								0,
								0,
								0,
								Math.max(t.getInteger(1,"Editor.lineNumber.border.thickness"),0),
								t.getColor(new Color(200, 200, 200), "Editor.lineNumber.border.color","General.line")
						),
						BorderFactory.createEmptyBorder(
								0,
								0,
								0,
								15
						)
				)
		);
		tln.setFont(new Font(t.getString("CraftrEditorModule.lineNumber.font","default:monospaced"),0,12));

		Lang lang = Lang.getLangForFile(associatedTab.path);
		if(lang != null) {
			setSyntax(ThemeManager.getSyntaxForGUITheme(lang, t));
			editorComponent.highlight();
		}
	}

	@Override
    public void displayCaretInfo() {
        editorComponent.displayCaretInfo();
    }

	@Override
	public Object getValue() {
		return getText().intern().hashCode();
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public Object save() {
		PrintWriter writer;
		try {
			writer = new PrintWriter(associatedTab.path, "UTF-8");

			String text = getText();
			if(!text.endsWith("\n")) {
				text = text.concat("\n");
				try {
					editorComponent.getDocument().insertString(text.length()-1,"\n",null);
				} catch(BadLocationException e) {
					e.printStackTrace();
				}
			}
			writer.print(text);
			writer.close();
			return getValue();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void focus() {
		editorComponent.requestFocus();
	}
}