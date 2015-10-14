package ui;

import graphics.GameCanvas;
import graphics.PlayerCanvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import control.Controller.MyMouseAction;


/**
 *
 * GameGui is the frame that holds all the components for the game graphical user interface.
 * One frame holds two canvas objects that draws the game world and the lower canvas displays the player statistics.
 * A menu bar is placed at the top to allow players to LoadGame, SaveGame and Quit.
 *
 * @author Quentin Copley
 *
 */
public class GameGui{

    private static final long serialVersionUID = 1L;
    private JFrame frame;
    private JPanel contentPane;
    private final GameCanvas invasionCanvas;
    private JPanel lowerPanel;
    private PlayerCanvas playerCanvas;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem mntmStatgame;
    private JMenuItem mntmSavegame;
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
        invasionCanvas.setBackground(new Color(14,34,0));
        invasionCanvas.setBounds(0, 20, 1500, 800);
        contentPane.add(invasionCanvas);

        lowerPanel = new JPanel();
        lowerPanel.setBackground(new Color(14,34,0));
        lowerPanel.setBounds(0, 820, 1500, 200);
        contentPane.add(lowerPanel);
        lowerPanel.setLayout(null);

        playerCanvas = new PlayerCanvas();//rgb(192,192,192)
        playerCanvas.setBackground(new Color(14,34,0));
        playerCanvas.setBounds(0, 0, 1500, 200);
        lowerPanel.add(playerCanvas);

        menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 1500, 20);
        contentPane.add(menuBar);

        menu = new JMenu("File");
        menuBar.add(menu);

        mntmStatgame = new JMenuItem("Start Game");
        menu.add(mntmStatgame);
        mntmSavegame = new JMenuItem("Save Game");
        menu.add(mntmSavegame);

        mnGame = new JMenu("Game");
        menuBar.add(mnGame);

        mntmExit = new JMenuItem("Exit");
        mnGame.add(mntmExit);

        frame.setVisible(true);
        frame.setResizable(false);

    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public JFrame getFrame(){
    	return frame;
    }

    public GameCanvas getInvasionCanvas() {
        return invasionCanvas;
    }

    public JPanel getLowerPanel() {
        return lowerPanel;
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
    /**
     * Initailization method to add listeners to individual components. This is set from the controller class.
     * @param maction MyMouseAction
     * @param saction KeyListener
     */
    public void initializeCanvasListeners(MyMouseAction maction,KeyListener saction) {
        //this.invasionCanvas.addMouseListener(maction);
        this.invasionCanvas.addKeyListener(saction);
        this.playerCanvas.addKeyListener(saction);
        this.playerCanvas.addMouseListener(maction);
    }
    /**
     * Initailization method to add listeners to individual components. This is set from the controller class.
     * @param ma ActionListener
     */
    public void initializeMenuListeners(ActionListener ma) {
        mntmExit.addActionListener(ma);
        mntmStatgame.addActionListener(ma);
        mntmSavegame.addActionListener(ma);

    }

    public void setVisiblity(boolean bool){
    	frame.setVisible(bool);
    }

}
