package com.energyxxer.craftr.ui.editor;

import com.energyxxer.craftr.global.Console;
import com.energyxxer.craftr.global.TabManager;
import com.energyxxer.craftr.ui.Tab;
import com.energyxxer.craftr.ui.explorer.ExplorerItemLabel;
import com.energyxxer.craftr.ui.scrollbar.OverlayScrollPaneLayout;
import com.energyxxer.craftr.ui.scrollbar.ScrollbarUI;
import com.energyxxer.craftr.ui.theme.Theme;
import com.energyxxer.craftr.ui.theme.ThemeManager;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;
import com.energyxxer.craftr.util.linenumber.TextLineNumber;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Main text editorComponent of the program. Has support for syntax highlighting, undo,
 * and is linked to abstract tabs.
 */
public class CraftrEditor extends JScrollPane implements UndoableEditListener, MouseListener, ThemeChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8584609859858654496L;

    Tab associatedTab;

    public CraftrEditorComponent editorComponent;
    private TextLineNumber tln;
	private Theme syntax;

	private ArrayList<String> styles = new ArrayList<>();

    //public long lastToolTip = new Date().getTime();

	public CraftrEditor(Tab tab) {
		super();
        associatedTab = tab;

        editorComponent = new CraftrEditorComponent(this);
        editorComponent.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
        super.setViewportView(editorComponent);

        tln = new TextLineNumber(editorComponent, this);
        tln.setPadding(10);

		this.setBorder(BorderFactory.createEmptyBorder());

		KeyStroke closeKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK);

		KeyStroke saveKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);

		//editorComponent.getInputMap().put(undoKeystroke, "undoKeystroke");
		//editorComponent.getInputMap().put(redoKeystroke, "redoKeystroke");
		editorComponent.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(closeKeystroke, "closeKeystroke");
		editorComponent.getInputMap().put(saveKeystroke, "saveKeystroke");

		editorComponent.getActionMap().put("closeKeystroke", new AbstractAction() {
			/**
			 *
			 */
			private static final long serialVersionUID = 2L;

			@Override
			public void actionPerformed(ActionEvent e) {
				TabManager.closeSelectedTab();
			}
		});

		editorComponent.getActionMap().put("saveKeystroke", new AbstractAction() {
			/**
			 *
			 */
			private static final long serialVersionUID = 2L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Tab st = TabManager.getSelectedTab();
				if(st != null) st.save();
			}
		});

		// editorComponent.addMouseMotionListener(hints = new EditorHints(this));
		editorComponent.addMouseListener(this);

		this.setRowHeaderView(tln);
		//tln.setPreferredSize(new Dimension(10,0));

		this.getVerticalScrollBar().setUI(new ScrollbarUI(this, 20));
		this.getHorizontalScrollBar().setUI(new ScrollbarUI(this, 20));
		this.getVerticalScrollBar().setOpaque(false);
		this.getHorizontalScrollBar().setOpaque(false);

		this.setLayout(new OverlayScrollPaneLayout());

		/*linePainter.addPaintListener(() -> {
			this.getVerticalScrollBar().repaint();
			this.getHorizontalScrollBar().repaint();
		});*/

		setComponentZOrder(getVerticalScrollBar(), 0);
		setComponentZOrder(getHorizontalScrollBar(), 1);
		setComponentZOrder(getViewport(), 2);


		addThemeChangeListener();
	}

	public void startEditListeners() {
		editorComponent.getDocument().addUndoableEditListener(this);
	}

	public void setSyntax(Theme newSyntax) {
		if(newSyntax == null) {
			for(String key : this.styles) {
				editorComponent.removeStyle(key);
			}
			return;
		}
		if(newSyntax.getThemeType() != Theme.ThemeType.SYNTAX_THEME) {
			Console.err.println("Theme \"" + newSyntax + "\" is not a syntax theme!");
			return;
		}

		this.styles.clear();
		syntax = newSyntax;
		for(String value : syntax.getValues().keySet()) {
			if(!value.contains(".")) continue;
			String[] sections = value.split("\\.");
			if(sections.length > 2) continue;

			String name = sections[0];
			Style style = editorComponent.getStyle(name);
			if(style == null) {
				style = editorComponent.addStyle(name, null);
				this.styles.add(name);
			}
			switch(sections[1]) {
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
		try {
			return editorComponent.getDocument().getText(0, editorComponent.getDocument().getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
			return null;
		}
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
		ExplorerItemLabel.setNewSelected(null, false);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

	@Override
	public void themeChanged(Theme t) {
		editorComponent.setBackground(t.getColor("Editor.background",Color.WHITE));
		setBackground(editorComponent.getBackground());
		editorComponent.setForeground(t.getColor("Editor.foreground",t.getColor("General.foreground",Color.BLACK)));
		editorComponent.setCaretColor(editorComponent.getForeground());
		editorComponent.setSelectionColor(t.getColor("Editor.selection.background",new Color(50, 100, 175)));
		editorComponent.setSelectedTextColor(t.getColor("Editor.selection.foreground", editorComponent.getForeground()));
		editorComponent.setCurrentLineColor(t.getColor("Editor.currentLine.background",new Color(235, 235, 235)));
		editorComponent.setFont(new Font(t.getString("Editor.font","monospaced"), 0, 12));
		tln.setBackground(t.getColor("Editor.lineNumber.background",new Color(235, 235, 235)));
		tln.setForeground(t.getColor("Editor.lineNumber.foreground",new Color(150, 150, 150)));
		//tln current line background
		tln.setCurrentLineForeground(t.getColor("CraftrEditor.lineNumber.currentLine.foreground",tln.getForeground()));
		tln.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createMatteBorder(
								0,
								0,
								0,
								1,
								t.getColor(
										"Editor.lineNumber.border",
										t.getColor(
												"General.line",
												new Color(200, 200, 200)
										)
								)
						),
						BorderFactory.createEmptyBorder(
								0,
								0,
								0,
								15
						)
				)
		);
		tln.setFont(new Font(t.getString("CraftrEditor.lineNumber.font","monospaced"),0,12));

		for(Theme.Lang lang : Theme.Lang.values()) {
			if(associatedTab.path.endsWith("." + lang.toString().toLowerCase())) {
				setSyntax(ThemeManager.getSyntaxForGUITheme(lang, t));
				editorComponent.highlight();
				break;
			}
		}
	}

    public void displayCaretInfo() {
        editorComponent.displayCaretInfo();
    }

}