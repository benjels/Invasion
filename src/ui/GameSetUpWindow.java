package ui;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import control.Listener.ButtonListener;

public class GameSetUpWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton button = new JButton("Add Player");
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;
	private JLabel lblNewLabel;


	/**
	 * Create the frame which contains all the components for the setup gui.
	 */
	public GameSetUpWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 170, 210);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 206, 209));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnNewButton = new JButton("Start Server");
		btnNewButton.setBounds(10, 75, 150, 25);
		contentPane.add(btnNewButton);

		btnNewButton_1 = new JButton("Start Game");
		btnNewButton_1.setBounds(10, 110, 150, 25);
		contentPane.add(btnNewButton_1);

		btnNewButton_2 = new JButton("");
		btnNewButton_2.setBounds(10, 145, 150, 25);
		contentPane.add(btnNewButton_2);

		button = new JButton("Add Player");
		button.setBounds(10, 40, 150, 25);
		contentPane.add(button);

		lblNewLabel = new JLabel("Invasion set up");
		lblNewLabel.setBounds(10, 10, 150, 20);
		contentPane.add(lblNewLabel);
		setVisible(true);
	}

	public void setListener(ButtonListener bl) {
		this.button.addActionListener(bl);
		this.btnNewButton.addActionListener(bl);
		this.btnNewButton_1.addActionListener(bl);;
		this.btnNewButton_2.addActionListener(bl);;
	}
}


