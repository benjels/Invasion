package control;

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
import gamelogic.events.RightPushedEvent;
import gamelogic.events.RotateMapClockwise;
import gamelogic.events.UpPushedEvent;
import imagehelper.Imagehelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;

import storage.XMLWriter;
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

public class Listener {

	private GameGui gui;
	private GameSetUpWindow setUpGui;
	private DummySlave dummySlave;// hardcoded event field
	private Imagehelper helper;

	public Listener(GameGui gui, GameSetUpWindow setUp, DummySlave slave){
		this.gui = gui;
		this.setUpGui = setUp;
		this.dummySlave = slave;
		this.helper = new Imagehelper();
		this.addGuiLabelImages();
		this.addGuiListeners();
	}

	private void addGuiLabelImages(){
		ImageIcon icon = helper.getPlayerCanvasImages().get("gameIcon");
		ImageIcon faceIcon = helper.getPlayerCanvasImages().get("warriorIcon");

		//this.gui.getGameIcon().setIcon(faceIcon);
		//this.gui.getGameIcon().setCursor(new Color(14,34,0));

		this.gui.getPlayerFace().setIcon(faceIcon);//returns JLabel to setIcon
	}

	private void addGuiListeners(){
		ButtonListener bl = new ButtonListener();
		KeyListener saction = new KeyAction();
		MyMouseAction maction = new MyMouseAction();
		MenuActionListener ma = new MenuActionListener();
		this.gui.initializeCanvasListeners(maction,saction);
		this.gui.initializeMenuListeners(ma);

		this.setUpGui.setListener(bl);
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
			}else if(e.getKeyCode() == KeyEvent.VK_P ){
				XMLWriter g = new XMLWriter();
				g.saveState();// hard coded save operation for integration, check file for save confirmation.
			}else if(e.getKeyCode() == KeyEvent.VK_T ){//maxb added these shits
				dummySlave.sendEventClientToServer(new CarrierOpenEvent(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_Y ){
				dummySlave.sendEventClientToServer(new CarrierCloseEvent(0));// hard coded game I.d
			}else if(e.getKeyCode() == KeyEvent.VK_E ){
				dummySlave.sendEventClientToServer(new RotateMapClockwise(0));// hard coded game I.d
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
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// to be implemented to highlight what item the mouse is hovering on.
			System.out.println(e.getPoint().toString());
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// to be implemented to highlight what item the mouse is hovering on.
			System.out.println(e.getPoint().toString());
		}
	}


	/**
	 * Button Listener for the Setup
	 */
	public class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Printing from ButtonListener, source is : "+e.getSource());

		}
	}

	/**
	 * Action listener class for the menu.
	 */
	public class MenuActionListener implements ActionListener {
		@Override
		public void actionPerformed (ActionEvent actionEvent) {
			if(actionEvent.getActionCommand().equalsIgnoreCase("Quit")){
				System.exit(1);
			}
		}
	}
}

