import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import display.Information;
import stages.Villages;

public class Civilization {
	static JFrame frame;
	public static void main(String[] args) {
		frame = new JFrame();
		
		MainScreen main = new MainScreen();
		menubar(main.getVillages());
		
		frame.setSize(1300, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		
		
		frame.setLayout(null);
		frame.add(main);
		
		
		frame.setVisible(true);
		
		JFrame debugger = new JFrame();
		
		JLabel octaveName = new JLabel("Octave");
		JLabel persistenceName = new JLabel("Persistence");
		JLabel frequencyName = new JLabel("Frequency");
		JLabel amplitudeName = new JLabel("Amplitude");
		
		JTextField octave = new JTextField("" + main.octave);
		JTextField persistence = new JTextField("" + main.persistence);
		JTextField frequency = new JTextField("" + main.frequency);
		JTextField amplitude = new JTextField("" + main.amplitude);
		
		octaveName.setBounds(0, 50, 100, 50);
		octave.setBounds(100, 50, 100, 50);
		persistenceName.setBounds(0, 100, 100, 50);
		persistence.setBounds(100, 100, 100, 50);
		frequencyName.setBounds(0, 150, 100, 50);
		frequency.setBounds(100, 150, 100, 50);
		amplitudeName.setBounds(0, 200, 100, 50);
		amplitude.setBounds(100, 200, 100, 50);
		
		JButton debug = new JButton();
		debug.setBounds(0, 0, 100, 50);
		
		debug.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				main.octave = Integer.parseInt(octave.getText());
				main.persistence = Double.parseDouble(persistence.getText());
				main.frequency = Double.parseDouble(frequency.getText());
				main.amplitude = Double.parseDouble(amplitude.getText());
				frame.repaint();
			}
			
		});
		
		debugger.setSize(300, 300);
		debugger.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		debugger.setLocationRelativeTo(null);
		
		debugger.setLayout(null);
		debugger.add(debug);
		debugger.add(octaveName);
		debugger.add(octave);
		debugger.add(persistenceName);
		debugger.add(persistence);
		debugger.add(frequencyName);
		debugger.add(frequency);
		debugger.add(amplitudeName);
		debugger.add(amplitude);
		
		debugger.setVisible(true);
		
		
		System.out.println("Done");
	}
	
	private static void menubar(ArrayList<Villages> villagesList) {
		JMenuBar mb = new JMenuBar();
		JMenu mChar = new JMenu("Info");
		//Stats bar in "Character"
		JMenuItem villageInfo = new JMenuItem("Villages");
		
		mChar.add(villageInfo);
		mb.add(mChar);
		
		frame.setJMenuBar(mb);
		
		villageInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Information info = new Information(villagesList);
			}
		});
	}
}
