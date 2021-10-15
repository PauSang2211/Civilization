import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class TPanel extends JPanel implements ActionListener{
	Timer timer;
	int x, y, prevx, prevy;
	public TPanel() {
		x = 0;
		y = 0;
		prevx = 0;
		prevy = 0;
		
		this.setBounds(0, 0, 600, 600);
		this.setVisible(true);
		timer = new Timer(1000, this);
		timer.start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawOval(0, 0, 16, 16);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
