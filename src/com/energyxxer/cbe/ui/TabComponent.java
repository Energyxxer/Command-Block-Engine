package com.energyxxer.cbe.ui;

import com.energyxxer.cbe.global.Commons;
import com.energyxxer.cbe.global.TabManager;
import com.energyxxer.cbe.ui.styledcomponents.StyledMenuItem;
import com.energyxxer.cbe.ui.styledcomponents.StyledPopupMenu;
import com.energyxxer.cbe.ui.theme.Theme;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;
import com.energyxxer.cbe.util.ImageManager;
import com.energyxxer.cbe.util.StringUtil;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import static com.energyxxer.cbe.ui.Draggable.AXIS_X;

/**
 * Representation of a tab in the interface.
 */
public class TabComponent extends JLabel implements MouseListener, ThemeChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2118880845845349145L;

	/**Whether this tab component is the one selected and active.*/
	public boolean selected = false;

	private String name;
	private boolean saved = true;

	private TabCloseButton close;

	private Tab associatedTab;

	private boolean rollover;

	private Color normal_bg;
	private Color normal_line;
	private Color rollover_bg;
	private Color rollover_line;
	private Color selected_bg;
	private Color selected_line;

	private Draggable dragState = new Draggable(this, AXIS_X);

	TabComponent(Tab associatedTab) {
		super();
		this.associatedTab = associatedTab;

		setName(new File(this.getLinkedTab().path).getName());

		setLayout(new BorderLayout());

		close = new TabCloseButton(getPreferredSize().height, this);

		add(close, BorderLayout.EAST);

		setAlignmentX(FlowLayout.LEFT);
		setHorizontalAlignment(SwingConstants.LEFT);

		int gap = (30 - this.getPreferredSize().height) / 2;
		setBorder(BorderFactory.createEmptyBorder(gap + 1, 5, gap - 1, 5));

		setOpaque(false);
		setBackground(new Color(0,0,0,0));
		
		addMouseListener(this);
		addThemeChangeListener();

		dragState.addDragListener(new DragListener() {
			@Override
			public void onDrag(Point offset) {
				TabComponent.this.repaint();
			}

			@Override
			public void onDrop(Point point) {
				TabComponent.this.repaint();
			}
		});
	}

	@Override
	public void setName(String name) {
		this.name = name;
		setToolTipText(getLinkedTab().path);

		updateIcon();
		updateName();
	}

	private void updateIcon() {
		if (name.endsWith(".mcbe")) {
			this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Commons.themeAssetsPath + "entity.png").getScaledInstance(16, 16,
					java.awt.Image.SCALE_SMOOTH)));
		} else {
			this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Commons.themeAssetsPath + "file.png").getScaledInstance(16, 16,
					java.awt.Image.SCALE_SMOOTH)));
		}
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	private void updateName() {
		setText(((!saved) ? "*" : "") + StringUtil.ellipsis(name, 32));
		setPreferredSize(null);
		setPreferredSize(new Dimension(getPreferredSize().width + 30, 30));
		revalidate();
	}

	public Tab getLinkedTab() {
		return associatedTab;
	}

	void setSaved(boolean saved) {
		this.saved = saved;
		updateName();
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void repaint() {
		super.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		Point offset = dragState.getOffset();
		g2.translate(offset.x, offset.y);
		//Rectangle tabListRect = Window.editArea.tabList.getBounds();
		//g2.setClip(new Rectangle(0,0,tabListRect.width,tabListRect.height));
		//System.out.println(getParent().getGraphics());

		if (selected) {
			g2.setColor(selected_line);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());

			g2.setColor(selected_bg);
			g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight());

		} else if (rollover || close.getModel().isRollover()) {
			g2.setColor(rollover_line);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());

			g2.setColor(rollover_bg);
			g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

		} else {
			g2.setColor(normal_line);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());

			g2.setColor(normal_bg);
			g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		}

		super.paintComponent(g);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		rollover = true;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		rollover = false;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
			showContextMenu(e);
		} else if (e.getButton() == MouseEvent.BUTTON1) {
			TabManager.setSelectedTab(this.getLinkedTab());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			showContextMenu(e);
		}
	}

	private void showContextMenu(MouseEvent e) {
		TabPopup menu = new TabPopup(getLinkedTab());
		menu.show(this, e.getX(), e.getY());
	}

	public boolean isSaved() {
		return saved;
	}

	@Override
	public void themeChanged(Theme t) {
		setFont(new Font(t.getString("Tab.font",t.getString("General.font","Tahoma")), 0, 11));
		setForeground(t.getColor("Tab.foreground",t.getColor("General.foreground",Color.BLACK)));

		this.normal_bg = t.getColor("Tab.background",new Color(200, 202, 205));
		this.normal_line = t.getColor("Tab.border",new Color(200, 200, 200));
		this.rollover_bg = t.getColor("Tab.hover.background",Color.WHITE);
		this.rollover_line = t.getColor("Tab.hover.border",new Color(200, 200, 200));
		this.selected_bg = t.getColor("Tab.selected.background",Color.WHITE);
		this.selected_line = t.getColor("Tab.selected.border",new Color(200, 200, 200));

		updateIcon();
		updateName();
	}
}

class TabPopup extends StyledPopupMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7968631495164738852L;

	TabPopup(Tab tab) {
		{
			StyledMenuItem item = new StyledMenuItem("Close");
			item.addActionListener(e -> TabManager.closeTab(tab));
			add(item);
		}
		{
			StyledMenuItem item = new StyledMenuItem("Close Others");
			item.addActionListener(e -> {
				for (int i = 0; i < TabManager.openTabs.size();) {
					if (TabManager.openTabs.get(i) != tab) {
						TabManager.closeTab(TabManager.openTabs.get(i));
					} else {
						i++;
					}
				}
			});
			add(item);
		}
		{
			StyledMenuItem item = new StyledMenuItem("Close Tabs to the Left");
			item.addActionListener(e -> {
				for (int i = 0; i < TabManager.openTabs.size();) {
					if (TabManager.openTabs.get(i) == tab) {
						return;
					} else {
						TabManager.closeTab(TabManager.openTabs.get(i));
					}
				}
			});
			add(item);
		}

		{
			StyledMenuItem item = new StyledMenuItem("Close Tabs to the Right");
			item.addActionListener(e -> {
				boolean close = false;
				for (int i = 0; i < TabManager.openTabs.size(); i++) {
					if (TabManager.openTabs.get(i) == tab) {
						close = true;
					} else if (close) {
						TabManager.closeTab(TabManager.openTabs.get(i));
						i--;
					}
				}
			});
			add(item);
		}

		addSeparator();
		{
			StyledMenuItem item = new StyledMenuItem("Close All");
			item.addActionListener(e -> {
				for (int i = 0; i < TabManager.openTabs.size();) {
					TabManager.closeTab(TabManager.openTabs.get(i));
				}
			});
			add(item);
		}
	}
}

class TabCloseButton extends JButton implements ActionListener, MouseListener {

	private TabComponent parent;

	TabCloseButton(int size, TabComponent parent) {
		super();
		this.parent = parent;
		setContentAreaFilled(false);
		setFocusPainted(false);
		setIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/close.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
		setRolloverIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/close_hover.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
		setPressedIcon(getRolloverIcon());
		setToolTipText("Close");
		setPreferredSize(new Dimension(size, size));
		setOpaque(false);
		setBackground(new Color(0,0,0,0));
		setBorder(BorderFactory.createEmptyBorder());

		addActionListener(this);
		addMouseListener(this);
	}
	

	@Override
	protected void paintComponent(Graphics g) {

		if (getModel().isEnabled()) {
			setBackground(new Color(0,0,0,0));

			super.paintComponent(g);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		TabManager.closeTab(parent.getLinkedTab());
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		parent.mouseEntered(arg0);
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		parent.mouseExited(arg0);
	}


	@Override
	public void mousePressed(MouseEvent arg0) {}


	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
	
}