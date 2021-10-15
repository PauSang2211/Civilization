import java.awt.Dimension;

import javax.swing.JFrame;

public class Trial {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		TPanel p = new TPanel();
		
		frame.setSize(new Dimension(1000, 700));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.add(p);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
