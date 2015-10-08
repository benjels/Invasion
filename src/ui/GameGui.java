package ui;

import graphics.GameCanvas;
import graphics.PlayerCanvas;

import java.awt.BorderLayout;
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
//		invasionCanvas.setBackground(new Color(0, 255, 255)); // original
//		invasionCanvas.setBackground(new Color(242,236,247)); //next original
		invasionCanvas.setBackground(new Color(14,34,0));
		invasionCanvas.setBounds(0, 20, 1500, 800);
		contentPane.add(invasionCanvas);

		lowerPanel = new JPanel();
		lowerPanel.setBackground(Color.WHITE);
		lowerPanel.setBounds(0, 820, 1500, 200);
		contentPane.add(lowerPanel);
		lowerPanel.setLayout(null);

		playerFace = new JLabel("Player Face");
		playerFace.setBounds(1300, 0, 200, 200);
		lowerPanel.add(playerFace);

		playerCanvas = new PlayerCanvas();//rgb(192,192,192)
		playerCanvas.setBackground(new Color(14,34,0));
		playerCanvas.setBounds(0, 0, 1300, 200);
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

	public PlayerCanvas getPlayerCanvas() {
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

	public void initializeCanvasListeners(MyMouseAction maction,KeyListener saction) {
		this.invasionCanvas.addMouseListener(maction);
		this.invasionCanvas.addKeyListener(saction);
		//this.playerCanvas.addKeyListener(saction);
		this.playerCanvas.addMouseListener(maction);
	}

	public void initializeMenuListeners(ActionListener ma) {
		//to do....
	}

}
