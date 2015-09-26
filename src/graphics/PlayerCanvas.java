package graphics;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class PlayerCanvas extends Canvas{

	public PlayerCanvas(){
	}

	@Override
	public Dimension getPreferredSize() {
		//playerCanvas.setBounds(500, 0, 400, 200);
		Dimension d = new Dimension(400,200);
		return d;
	}

	public void paint(Graphics g) {

	}
}