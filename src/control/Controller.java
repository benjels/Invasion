package control;

import gamelogic.CardinalDirection;
import gamelogic.CharacterStrategy;
import gamelogic.ClockThread;
import gamelogic.GameWorldTimeClockThread;
import gamelogic.IndependentActorManager;
import gamelogic.Server;
import gamelogic.SorcererPlayerStrategy;
import gamelogic.WorldGameState;
import gamelogic.entities.Player;
import gamelogic.events.Action1PushedEvent;
import gamelogic.events.Action2PushedEvent;
import gamelogic.events.CarrierCloseEvent;
import gamelogic.events.CarrierOpenEvent;
import gamelogic.events.DownPushedEvent;
import gamelogic.events.LeftPushedEvent;
import gamelogic.events.PlayerDropEvent;
import gamelogic.events.PlayerPickupEvent;
import gamelogic.events.PlayerSelectInvSlot1;
import gamelogic.events.PlayerSelectInvSlot2;
import gamelogic.events.PlayerSelectInvSlot3;
import gamelogic.events.PlayerSelectInvSlot4;
import gamelogic.events.PlayerSelectInvSlot5;
import gamelogic.events.PlayerHealEvent;
import gamelogic.events.RightPushedEvent;
import gamelogic.events.RotateMapClockwise;
import gamelogic.events.SaveGameEvent;
import gamelogic.events.UpPushedEvent;
import graphics.GameCanvas;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import main.LoadNewGame;
import storage.XMLParser;
import storage.XMLWriter;
import ui.GameCharacterSelect;
import ui.GameGui;
import ui.GameSetUpWindow;

/**
 *
 * Listener class directly receives gui actions. Stores public classes
 * and passes events to game master.
 *
 * @date 5 Oct 2015
 * @author maxcopley
 *
 */

public class Controller {

	private GameGui gui;
	private DummySlave dummySlave;// hardcoded event field
	private WorldGameState game;
	private String name;
	private CharacterStrategy character;


	public Controller(GameGui gui, DummySlave slave){
		this.gui = gui;
		this.dummySlave = slave;
		this.addGuiListeners();

	}

	public void addGuiListeners(){
		ButtonListener bl = new ButtonListener();
		KeyListener saction = new KeyAction();
		MyMouseAction maction = new MyMouseAction();
		MenuActionListener ma = new MenuActionListener();
		this.gui.initializeCanvasListeners(maction,saction);
		this.gui.initializeMenuListeners(ma);

		//this.setUpGui.setListener(bl);
		gui.getFrame().addKeyListener(saction);
	}


	public class KeyAction implements KeyListener{
		//calling , this.master.sendEventSlaveToMaster(eventToSend);

		@Override
		public void keyTyped(KeyEvent e) {}
		@Override
		public void keyPressed(KeyEvent e) {
			System.out.println(e.getSource().toString());
		}
		@Override
		public void keyReleased(KeyEvent e) {
			System.out.println("prints from GameCanvas"+e.getSource().toString()); //debugging here.
			if(e.getKeyCode() == KeyEvent.VK_A ){
				dummySlave.sendEventClientToServer(new LeftPushedEvent(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_D ){
				dummySlave.sendEventClientToServer(new RightPushedEvent(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_W ){
				dummySlave.sendEventClientToServer(new UpPushedEvent(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_S){
				dummySlave.sendEventClientToServer(new DownPushedEvent(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_Z ){// pick up
				dummySlave.sendEventClientToServer(new PlayerPickupEvent(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_X ){// drop item
				dummySlave.sendEventClientToServer(new PlayerDropEvent(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_LEFT ){// drop item
				dummySlave.sendEventClientToServer(new Action1PushedEvent(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_RIGHT ){// drop item
				dummySlave.sendEventClientToServer(new Action2PushedEvent(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_1 ){
				dummySlave.sendEventClientToServer(new PlayerSelectInvSlot1(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_2 ){
				dummySlave.sendEventClientToServer(new PlayerSelectInvSlot2(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_3 ){
				dummySlave.sendEventClientToServer(new PlayerSelectInvSlot3(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_4 ){
				dummySlave.sendEventClientToServer(new PlayerSelectInvSlot4(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_5 ){
				dummySlave.sendEventClientToServer(new PlayerSelectInvSlot5(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_T ){//maxb added these shits
				dummySlave.sendEventClientToServer(new CarrierOpenEvent(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_Y ){
				dummySlave.sendEventClientToServer(new CarrierCloseEvent(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_E ){
				dummySlave.sendEventClientToServer(new RotateMapClockwise(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_F ){
				dummySlave.sendEventClientToServer(new PlayerHealEvent(0));// hard coded game I.d
			}
		}
	}


	/**
	 * MouseAction class to set actionListeners to components inside the GUI.
	 */
	public class MyMouseAction implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println(e.getPoint().toString());
			Point point = e.getPoint();
			// actioned position.
			if(point.getX() < 500 && point.getY() < 197){
				if(point.getX() < 100){
					dummySlave.sendEventClientToServer(new PlayerSelectInvSlot1(0));
				}
				else if(point.getX() < 200){
					dummySlave.sendEventClientToServer(new PlayerSelectInvSlot2(0));
				}
				else if(point.getX() < 300){
					dummySlave.sendEventClientToServer(new PlayerSelectInvSlot3(0));
				}
				else if(point.getX() < 400){
					dummySlave.sendEventClientToServer(new PlayerSelectInvSlot4(0));
				}
				else if(point.getX() < 500){
					dummySlave.sendEventClientToServer(new PlayerSelectInvSlot5(0));
				}
			}else if(point.getX() > 720){
				if(point.getX() < 800){
					if(point.getY() > 54){
						if(point.getY() < 80){
						}
						else if(point.getY() < 111){
						}
						else if(point.getY() < 141){
						}
						else if(point.getY() < 171){
						}
						else if(point.getY() < 197){
						}
					}

				}
			}
		}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	}

	public void setGameGUIVisible(boolean bool){
		this.gui.setVisiblity(bool);
	}


	/**
	 * Button Listener for the Setup
	 */
	public class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Printing from ButtonListener, source is : "+e.getSource());
			if (e.getActionCommand().equals("New Game")){
//		  	Server theServer = new Server(dummySlave);
			}
		}
	}

	/**
	 * Action listener class for the menu.
	 */
	public class MenuActionListener implements ActionListener {
		@Override
		public void actionPerformed (ActionEvent actionEvent) {
			if(actionEvent.getActionCommand().equalsIgnoreCase("Exit")){
				System.exit(1);
			}else if(actionEvent.getActionCommand().equalsIgnoreCase("Save Game")){
				dummySlave.sendEventClientToServer(new SaveGameEvent(0));//hardcoded uid
			}
			else if (actionEvent.getActionCommand().equalsIgnoreCase("Start Game")){
				GameCharacterSelect select = new GameCharacterSelect(dummySlave);

				//Server theServer = new Server(dummySlave, name, character);
				gui.setVisiblity(true);
			}
		}
	}
}

