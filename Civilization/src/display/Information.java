package display;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import stages.Villages;

public class Information {
	ArrayList<Villages> villagesList;
	final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
	
	public Information(ArrayList<Villages> villagesList) {
		this.villagesList = villagesList;
		createAndShowGUI();
	}
	
	public void addComponentsToPane(Container pane) {
		JPanel panel = (JPanel) pane;
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		if (shouldFill) {
			//natural height, maximum width
			c.fill = GridBagConstraints.HORIZONTAL;
		}
		if (shouldWeightX) {
			c.weightx = 0.5;
		}

		
		
		DefaultListModel<String> items = new DefaultListModel<String>();
		JList<String> itemList = new JList<>(items);
		JScrollPane jp = new JScrollPane();
		jp.setViewportView(itemList);
		c.gridx = 0;
		c.gridy = 0;
		pane.add(jp, c);
		
		for(Villages village : villagesList) {
			items.addElement("Village");
		}
		
		itemList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					
				}
			}
		});
		
		panel.setVisible(true);
	}
	
	private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("GridBagLayoutDemo");

        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());
        

        //Display the window.
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
