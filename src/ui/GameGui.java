package ui;

import graphics.GameCanvas;
import graphics.PlayerCanvas;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import control.Listener.MyMouseAction;

public class GameGui{

	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JPanel contentPane;
	private final GameCanvas invasionCanvas;
	private JPanel lowerPanel;
	private JPanel inventoryPanel;
	private JLabel inventoryItem1;
	private JLabel inventoryItem2;
	private JLabel inventoryItem3;
	private JLabel inventoryItem4;
	private JLabel inventoryItem5;
	private JLabel gameIcon;
	private JLabel playerName;
	private JLabel playerFace;
	private JLabel characterType;
	private JLabel extraLabel;
	private JLabel extraLabel2;
	private PlayerCanvas playerCanvas;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem mntmStatgame;
	private JMenu mnGame;
	private JMenuItem mntmExit;

	public GameGui(GameCanvas invasionGameCanvas){ //package protected
		this.invasionCanvas = invasionGameCanvas;
		this.initialize();
	}

	private void initialize() {
		frame = new JFrame("Invasion");
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, 1500,1120);

		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		invasionCanvas.setBackground(new Color(0, 255, 255));
		invasionCanvas.setBounds(0, 20, 1500, 800);
		contentPane.add(invasionCanvas);

		lowerPanel = new JPanel();
		lowerPanel.setBackground(Color.WHITE);
		lowerPanel.setBounds(0, 820, 1500, 200);
		contentPane.add(lowerPanel);
		lowerPanel.setLayout(null);

		inventoryPanel = new JPanel();
		inventoryPanel.setBackground(new Color(240, 248, 255));
		inventoryPanel.setBounds(0, 0, 500, 200);
		lowerPanel.add(inventoryPanel);
		inventoryPanel.setLayout(null);

		inventoryItem1 = new JLabel("inventoryItem");
		inventoryItem1.setBounds(10, 10, 80, 180);
		//inventoryItem1.setActionCommand("button sent");
		inventoryPanel.add(inventoryItem1);

		inventoryItem2 = new JLabel("inventoryItem");
		inventoryItem2.setBounds(110, 10, 80, 180);
		inventoryPanel.add(inventoryItem2);

		inventoryItem3 = new JLabel("inventoryItem");
		inventoryItem3.setBounds(210, 10, 80, 180);
		inventoryPanel.add(inventoryItem3);

		inventoryItem4 = new JLabel("inventoryItem");
		inventoryItem4.setBounds(310, 10, 80, 180);
		inventoryPanel.add(inventoryItem4);

		inventoryItem5 = new JLabel("inventoryItem");
		inventoryItem5.setBounds(410, 10, 80, 180);
		inventoryPanel.add(inventoryItem5);

		gameIcon = new JLabel("gameIcon");
		gameIcon.setBackground(Color.BLACK);
		gameIcon.setForeground(SystemColor.activeCaption);
		gameIcon.setBounds(1300, 0, 200, 200);
		lowerPanel.add(gameIcon);

		playerName = new JLabel("Name");
		playerName.setBounds(900, 0, 200, 50);
		lowerPanel.add(playerName);

		playerFace = new JLabel("playerFace");
		playerFace.setForeground(SystemColor.activeCaption);
		playerFace.setBackground(Color.BLACK);
		playerFace.setBounds(1100, 0, 200, 200);
		lowerPanel.add(playerFace);

		characterType = new JLabel("CharacterType");
		characterType.setBounds(900, 50, 200, 50);
		lowerPanel.add(characterType);

		extraLabel = new JLabel("Extra Label 1");
		extraLabel.setBounds(900, 100, 200, 50);
		lowerPanel.add(extraLabel);

		extraLabel2 = new JLabel("Extra Label 2");
		extraLabel2.setBounds(900, 150, 200, 50);
		lowerPanel.add(extraLabel2);

		playerCanvas = new PlayerCanvas();
		playerCanvas.setBackground(new Color(0, 0, 128));
		playerCanvas.setBounds(500, 0, 400, 200);
		lowerPanel.add(playerCanvas);

		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1500, 20);
		contentPane.add(menuBar);

		menu = new JMenu("File");
		menuBar.add(menu);

		mntmStatgame = new JMenuItem("Start Game");
		menu.add(mntmStatgame);

		mnGame = new JMenu("Game");
		menuBar.add(mnGame);

		mntmExit = new JMenuItem("Exit");
		mnGame.add(mntmExit);

		frame.setVisible(true);

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public GameCanvas getInvasionCanvas() {
		return invasionCanvas;
	}

	public JPanel getLowerPanel() {
		return lowerPanel;
	}

	public JPanel getInventoryPanel() {
		return inventoryPanel;
	}

	public JLabel getInventoryItem1() {
		return inventoryItem1;
	}

	public JLabel getInventoryItem2() {
		return inventoryItem2;
	}

	public JLabel getInventoryItem3() {
		return inventoryItem3;
	}

	public JLabel getInventoryItem4() {
		return inventoryItem4;
	}

	public JLabel getInventoryItem5() {
		return inventoryItem5;
	}

	public JLabel getGameIcon() {
		return gameIcon;
	}

	public JLabel getPlayerName() {
		return playerName;
	}

	public JLabel getPlayerFace() {
		return playerFace;
	}

	public JLabel getCharacterType() {
		return characterType;
	}

	public JLabel getExtraLabel() {
		return extraLabel;
	}

	public JLabel getExtraLabel2() {
		return extraLabel2;
	}

	public Canvas getPlayerCanvas() {
		return playerCanvas;
	}

	public JMenuBar getJMenuBar() {
		return menuBar;
	}

	public JMenu getMenu() {
		return menu;
	}

	public JMenuItem getMntmStatgame() {
		return mntmStatgame;
	}

	public JMenu getMnGame() {
		return mnGame;
	}

	public JMenuItem getMntmExit() {
		return mntmExit;
	}

	public void initializeIntventoryListeners(MyMouseAction mouseAction) {
		this.inventoryItem1.addMouseListener(mouseAction);
		this.inventoryItem2.addMouseListener(mouseAction);
		this.inventoryItem3.addMouseListener(mouseAction);
		this.inventoryItem4.addMouseListener(mouseAction);
		this.inventoryItem5.addMouseListener(mouseAction);
	}

	public void initializeCanvasListeners(MyMouseAction maction,KeyListener saction) {
		this.invasionCanvas.addMouseListener(maction);
		this.invasionCanvas.addKeyListener(saction);
		this.playerCanvas.addMouseListener(maction);
	}

	public void initializeMenuListeners(ActionListener ma) {
		//to do....
	}

}
