package ui;

import gamelogic.CharacterStrategy;
import gamelogic.FighterPlayerStrategy;
import gamelogic.Server;
import gamelogic.SorcererPlayerStrategy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import control.DummySlave;




/**
 * GameCharacterSelect used to take pre game inputs from the user which are a string for the user and a Character type.
 *
 *
 * @author Quentin Copley
 * @date 12 Oct


 */
public class GameCharacterSelect extends JFrame {
	private JFrame frame;
	private JRadioButton warriorButton;
	private JRadioButton sorcererButton;
	private List<JRadioButton> radioList = new ArrayList<JRadioButton>();
	private JButton startButton;
	private JTextField nameText;
	private String name;
	private CharacterStrategy character = null;
	private DummySlave slave;

	public GameCharacterSelect(DummySlave slave) {
		this.slave = slave;
		initialise();
	}

	/**
	 * Initializes the frame for its labels, radio buttons, etc.
	 */
	public void initialise() {
		frame = new JFrame("Character Select");
		frame.setSize(200, 200);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		startButton = new JButton("Start Game!");
		JLabel nameLabel = new JLabel("Enter name:");
		nameText = new JTextField(10);
		JLabel characterSelect = new JLabel("Choose a Character: ");
		sorcererButton = new JRadioButton("Sorcerer");
		warriorButton = new JRadioButton("Warrior");
		// layout of frame
		frame.setLayout(new java.awt.FlowLayout());
		frame.setLocationRelativeTo(null);
		frame.add(nameLabel);
		frame.add(nameText);
		frame.add(characterSelect);
		// add in radio buttons to list
		radioList.add(warriorButton);
		radioList.add(sorcererButton);
		//stuff for image of character if needed
		JPanel fighterPanel = new JPanel();
		frame.setVisible(true);
		addButtons();
	}

	/**
	 * Method adds componenets to frame and also adds action classes to individual buttons
	 * Helper method that will add in the buttons to the frame with
	 * their specified functionalities

	 */
	public void addButtons() {
		// add in every radio button to frame
		for (JRadioButton r : radioList) {
			frame.add(r);
		}
		// so that you can only select one button at a time
		ButtonGroup group = new ButtonGroup();
		for (JRadioButton r : radioList) {
			group.add(r);
		}

		frame.add(startButton);

		// setup for radio button selection
		for (JRadioButton r : radioList) {
			r.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					if (event.getSource() == warriorButton) {
						character = new FighterPlayerStrategy();
					} else if (event.getSource() == sorcererButton) {
						character = new SorcererPlayerStrategy();
					}
				}

			});
		}

		// setup for start button
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getSource() == startButton) {
					if (nameText.getText().equals("")) {
						JOptionPane.showMessageDialog(null,
								"Please enter in a name!");
						return;
					} else if (nameText.getText().length() > 10) {
						JOptionPane.showMessageDialog(null,
								"Please enter in a shorter name!");
						nameText.setText("");
						return;
					}
					if (character == null) {
						JOptionPane.showMessageDialog(null,
								"Please select a character!");
						return;
					}
				}
				name = nameText.getText();
				Server theServer = new Server(slave, name, character);
				frame.setVisible(false);
			}

		});
	}

	/**
	 * Returns the name of player
	 */
	public String getName(){
		return name;
	}
	/**
	 * Gets the type of character player has selected
	 * @return
	 */
	public CharacterStrategy getCharacter(){
		return character;
	}
}
