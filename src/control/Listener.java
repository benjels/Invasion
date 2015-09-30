package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import ui.GameGui;
import ui.GameSetUpWindow;

public class Listener {

	private GameGui gui;
	private GameSetUpWindow setUpGui;

	public Listener(GameGui gui, GameSetUpWindow setUp){
		this.gui = gui;
		this.setUpGui = setUp;
		this.addGuiListeners();
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
		@Override
		public void keyTyped(KeyEvent e) {}
		@Override
		public void keyPressed(KeyEvent e) {
			System.out.println(e.getSource().toString());
		}
		@Override
		public void keyReleased(KeyEvent e) {
			System.out.println(e.getSource().toString());
		}
	}

	/**
	 * MouseAction class to set actionListeners to components inside the GUI.
	 */
	public class MyMouseAction implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e) {
		}
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
		public void actionPerformed (ActionEvent actionEvent) {
			if(actionEvent.getActionCommand().equalsIgnoreCase("Quit")){
				System.exit(1);
			}
		}
	}

}
