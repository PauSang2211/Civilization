import misc.Units;
import misc.Buildings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import misc.Cells;
import misc.PerlinNoise;
import stages.Tribes;
import stages.Villages;
import units.Explorers;

public class MainScreen extends JPanel implements ActionListener{
	Timer timer;
	Cells[][] map; //terrain
	Buildings[][] territory; //buildings or territory
	Units[][] unit; //units
	ArrayList<Tribes> tribesList;
	ArrayList<Villages> villagesList;
	private final int MAP_WIDTH = 1200/2;
	private final int MAP_LENGTH = 650/2;
	private final int SIZE = 2;
	private final int MAX_TRIBES = 500;
	private boolean mapGen;
	private int years, peakPop;
	int octave;
	double persistence, frequency, amplitude;
	
	public MainScreen() {
		map = new Cells[MAP_WIDTH][MAP_LENGTH];
		territory = new Buildings[MAP_WIDTH][MAP_LENGTH];
		unit = new Units[MAP_WIDTH][MAP_LENGTH];
		tribesList = new ArrayList<Tribes>();
		villagesList = new ArrayList<Villages>();
		
		mapGen = false;
		years = 0;
		peakPop = Integer.MIN_VALUE;
		
		octave = 10;
		persistence = 0.5;
		frequency = 0.2;
		amplitude = 2.5;
		
		this.setLayout(null);
		this.setSize(1300, 700);
		this.setVisible(true);
		timer = new Timer(300, this);
		timer.start();
		
		this.addMouseListener(new MouseHandler());
	}
	
	//GETTERS
	public ArrayList<Villages> getVillages() {
		return villagesList;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (mapGen) {
			//Redraw the same map
			for(int i = 0; i < MAP_WIDTH; i++) {
				for(int j = 0; j < MAP_LENGTH; j++) {
					Cells currCell = map[i][j];
					
					if (currCell.name == "Water") {
						g.setColor(new Color(51, 153, 255));
					} else if (currCell.name == "Sand") {
						g.setColor(new Color(255, 255, 153));
					} else if (currCell.name == "Grass") {
						g.setColor(new Color(0, 153, 0));
					} else if (currCell.name == "Mountain") {
						g.setColor(Color.GRAY);
					}
					g.fillRect(SIZE*i, SIZE*j, SIZE, SIZE);
				}
			}
			
			//TODO MAKE THIS BETTER
			for(int i = 0; i < MAP_WIDTH; i++) {
				for(int j = 0; j < MAP_LENGTH; j++) {
					if (territory[i][j] != null) {
						Villages village = (Villages) territory[i][j];
						g.setColor(village.color());
						g.fillRect(SIZE*i, SIZE*j, SIZE, SIZE);
					}
				}
			}
			
			//draw necessities
			drawVillages(g);
			drawTribes(g);
			
			
		} else {
			//map generation and variable initialization
			initialize(g);
			mapGen = true;
		}
	}
	
	//To change properties of game components
	@Override
	public void actionPerformed(ActionEvent e) {
		//add tribes and every alive tribe makes actions
		addTribes();
		for(Tribes tribe : tribesList) {
			tribe.actions();
		}
		//every village makes actions
		for(Villages village : villagesList) {
			village.actions();
		}
		findPeakPop();
		repaint();
	}
	
	private void drawTribes(Graphics g) {
		//Animate tribe actions
		Queue<Tribes> deadTribes = new LinkedList<>();
		for(Tribes tribe : tribesList) {
			//Recolor prev tile for tribes that moved
			if (unit[tribe.prevX()][tribe.prevY()] == null) {
				if (map[tribe.prevX()][tribe.prevY()].name == "Grass") {
					g.setColor(new Color(0, 153, 0));
				} else if (map[tribe.prevX()][tribe.prevY()].name == "Sand") {
					g.setColor(new Color(255, 255, 153));
				}
				g.fillRect(SIZE*tribe.prevX(), SIZE*tribe.prevY(), SIZE, SIZE);
			}
			
			//if a tribe is fighting, draw a circle to indicate the action
			if (tribe.isFighting()) {
				g.setColor(Color.RED);
				g.drawOval(SIZE*tribe.x()-3, SIZE*tribe.y()-3, 9, 9);
			}
			
			//if a tribe is dead
			if (tribe.isDead()) {
				//check if the tribe died because it is now a village
				if (tribe.isNowVillage()) {
					//if a tribe is now a village, create new village
					//and add it to the list
					Villages village = new Villages("Village", tribe.x(), tribe.y(), tribe.pop(), tribe.aggro(),
							map, territory, unit, villagesList);
					villagesList.add(village);
				}
				deadTribes.add(tribe);
				continue;
			}
			g.setColor(Color.BLACK);
			g.fillRect(SIZE*tribe.x(), SIZE*tribe.y(), SIZE, SIZE);
		}
		
		//remove every dead tribe
		while(!deadTribes.isEmpty()) {
			tribesList.remove(deadTribes.remove());
		}
	}
	
	private void drawVillages(Graphics g) {
		//Animate village actions
		for(Villages village : villagesList) {
			g.setColor(village.color());
			g.fillRect(SIZE*village.x(), SIZE*village.y(), SIZE, SIZE);
			
			g.setColor(Color.MAGENTA);
			for(Explorers explorer : village.getExplorers()) {
				g.fillRect(SIZE*explorer.x(), SIZE*explorer.y(), SIZE, SIZE);
			}
			
			g.setColor(Color.BLUE);
			for(Buildings building : village.getBuildings()) {
				g.fillRect(SIZE*building.x(), SIZE*building.y(), SIZE, SIZE);
			}
		}
	}
	
	//find the peak population
	private void findPeakPop() {
		if (peakPop < tribesList.size()) {
			peakPop = tribesList.size()+villagesList.size();
			System.out.println("Peaked Pop of " + peakPop + " during Year " + years);
		}
		years++;
	}
	
	//adding tribes to the game
	private void addTribes() {
		//if the amount of tribes is below the maximum, make tribes
		if (tribesList.size() != MAX_TRIBES) {
			//make from 0-9 new tribes and make sure that it will
			//be less than or equal to the maximum amount of tribes
			int newTribes = (int) (Math.random()*10);
			while(newTribes+tribesList.size() > MAX_TRIBES) {
				newTribes = (int) (Math.random()*10);
			}
			
			//for every new tribe, place it randomly throughout the map
			for(int i = 0; i < newTribes; i++) {
				int randX = (int) (Math.random()*MAP_WIDTH);
				int randY = (int) (Math.random()*MAP_LENGTH);
				
				//make sure the tribe does not spawn in poor locations
				String currTile = map[randX][randY].name;
				while(currTile == "Water" || currTile == "Mountain" || currTile == "Tribe") {
					randX = (int) (Math.random()*MAP_WIDTH);
					randY = (int) (Math.random()*MAP_LENGTH);
					currTile = map[randX][randY].name;
				}
				
				//Update map and create tribe graphics
				Tribes tribe = new Tribes("Tribe", randX, randY, years, map, territory, unit);
				unit[randX][randY] = tribe;
				tribesList.add(tribe);
				//System.out.println("New tribe added");
			}
		}
	}
	
	private void initialize(Graphics g) {
		//Perlin params: seed, persistence, frequency, amplitude, octave
		int seed = (int) (Math.random()*100);
		System.out.println(seed);
		PerlinNoise p = new PerlinNoise(seed, persistence, frequency, amplitude, octave);
		
		//initialize map and other 2D arrays
		for(int i = 0; i < MAP_WIDTH; i++) {
			for(int j = 0; j < MAP_LENGTH; j++) {
				double height = p.getHeight(i, j);
				System.out.println(height);
				
				//Color the different terrains
				Cells currCell = null;
				if (height < -0.8) {
					currCell = new Cells("Water", i, j);
					g.setColor(new Color(51, 153, 255));
				} else if (height < -0.5 && height >= -0.8) {
					currCell = new Cells("Sand", i, j);
					g.setColor(new Color(255, 255, 153));
				} else if (height >= -0.5 && height < 1.2) {
					currCell = new Cells("Grass", i, j);
					g.setColor(new Color(0, 153, 0));
				} else {
					currCell = new Cells("Mountain", i, j);
					g.setColor(Color.GRAY);
				}
				map[i][j] = currCell;
				g.fillRect(SIZE*i, SIZE*j, SIZE, SIZE);
				
				//initialize unit and territory
				unit[i][j] = null;
				territory[i][j] = null;
			}
		}
		
		//Set the neighbor of each cell on the map
		for(int i = 0; i < MAP_WIDTH; i++) {
			for(int j = 0; j < MAP_LENGTH; j++) {
				Cells currCell = map[i][j];
				//North
				if (j-1 >= 0) {
					currCell.neighbors[0] = map[i][j-1];
				} else {
					currCell.neighbors[0] = null;
				}
				
				//East
				if (i+1 < MAP_WIDTH) {
					currCell.neighbors[1] = map[i+1][j];
				} else {
					currCell.neighbors[1] = null;
				}
				
				//South
				if (j+1 < MAP_LENGTH) {
					currCell.neighbors[2] = map[i][j+1];
				} else {
					currCell.neighbors[2] = null;
				}
				
				//West
				if (i-1 >= 0) {
					currCell.neighbors[3] = map[i-1][j];
				} else {
					currCell.neighbors[3] = null;
				}
			}
		}
		
		g.setColor(Color.RED);
		g.fillRect(0, 0, SIZE, SIZE);
		
		System.out.println("Octave: " + octave);
		System.out.println("Persistence: " + persistence);
		System.out.println("Frequency: " + frequency);
		System.out.println("Amplitude: " + amplitude);
	}
	
	private class MouseHandler extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			
		}
		
		public void mouseReleased(MouseEvent e) {
			Point p = e.getPoint();
			int mapX = (p.x)/2;
			int mapY = (p.y)/2;
			System.out.println("X: " + mapX + "\tY: " + mapY + "\tName: " + map[mapX][mapY].name);
			/*
			System.out.println("Neighbor North: " + map[mapX][mapY].neighbors[0].name);
			System.out.println("Neighbor EAST: " + map[mapX][mapY].neighbors[1].name);
			System.out.println("Neighbor SOUTH: " + map[mapX][mapY].neighbors[2].name);
			System.out.println("Neighbor WEST: " + map[mapX][mapY].neighbors[3].name);
			*/
			System.out.println("Class: " + territory[mapX][mapY]);
			if (territory[mapX][mapY] instanceof Villages) {
				Villages village = (Villages) territory[mapX][mapY];
				System.out.println("Gold: " + village.gold());
			}
		}
	}
}
