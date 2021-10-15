package stages;
import misc.*;
import buildings.*;
import units.Explorers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;



public class Villages extends Buildings{
	static ArrayList<Villages> villagesList;
	private ArrayList<Explorers> explorersList;
	private ArrayList<Buildings> buildingsList;
	private Color color;
	private int freeWorkers, totalWorkers;
	private int gold;
	private int aggro;
	//inherted variables: health, defense, pop, hunger
	
	public Villages(String name, int x, int y, int pop, int aggro, Cells[][] map, Buildings[][] territory,
			Units[][] unit, ArrayList<Villages> villagesList) {
		super(name, x, y, map, territory, unit);
		this.villagesList = villagesList;
		this.explorersList = new ArrayList<Explorers>();
		this.buildingsList = new ArrayList<Buildings>();
		
		this.pop = pop;
		this.health = 10*pop;
		this.defense = pop-1;
		this.hunger = (int) (Math.random()*30)+pop*3;
		this.aggro = aggro;
		
		this.gold = 0;
		
		this.freeWorkers = 5;
		this.totalWorkers = 5;
		
		territory[x][y] = this;
		unit[x][y] = null; //TODO make a king or something
		
		this.color = chooseColor();
	}
	
	//GETTERS
	public ArrayList<Explorers> getExplorers() {
		return explorersList;
	}
	
	public ArrayList<Buildings> getBuildings() {
		return buildingsList;
	}
	
	public Color color() {
		return color;
	}
	
	public int gold() {
		return gold;
	}
	
	//METHODS
	public void actions() {
		updateStats();
		unitControl();
		buildingControl();
		chooseAction();
		disposeDeadUnits();
		destroyBrokenBuildings();
	}
	
	private Color chooseColor() {
		/*Cells[][] temp = new Cells[Constants.MAP_WIDTH][Constants.MAP_LENGTH];
		
		for(int i = 0; i < Constants.MAP_WIDTH; i++) {
			for(int j = 0; j < Constants.MAP_LENGTH; j++) {
				temp[i][j] = new Cells("", i, j);
			}
		}
		
		for(int i = 0; i < Constants.MAP_WIDTH; i++) {
			for(int j = 0; j < Constants.MAP_LENGTH; j++) {
				Cells currCell = temp[i][j];
				//North
				if (j-1 >= 0) {
					currCell.neighbors[0] = temp[i][j-1];
				} else {
					currCell.neighbors[0] = null;
				}
				
				//East
				if (i+1 < Constants.MAP_WIDTH) {
					currCell.neighbors[1] = temp[i+1][j];
				} else {
					currCell.neighbors[1] = null;
				}
				
				//South
				if (j+1 < Constants.MAP_LENGTH) {
					currCell.neighbors[2] = temp[i][j+1];
				} else {
					currCell.neighbors[2] = null;
				}
				
				//West
				if (i-1 >= 0) {
					currCell.neighbors[3] = temp[i-1][j];
				} else {
					currCell.neighbors[3] = null;
				}
			}
		}
		
		Color closestColor = null;
		Queue<Cells> q = new LinkedList<>();
		q.add(temp[x][y]);
		
		while(!q.isEmpty()) {
			Cells currCell = q.remove();
			
			if (!currCell.visited()) {
				currCell.switchVisit(true);
				
				if (territory[currCell.x][currCell.y] != this && 
						territory[currCell.x][currCell.y] instanceof Villages) {
					Villages closestVillage = (Villages) territory[currCell.x][currCell.y];
					closestColor = closestVillage.color();
					break;
				}
				
				for(Cells neighbor : currCell.neighbors) {
					if (neighbor != null && !neighbor.visited()) {
						q.add(neighbor);
					}
				}
			}
		}
		
		color = Constants.villageColors[(int) (Math.random()*Constants.villageColors.length)];
		
		while (color == closestColor) {
			color = Constants.villageColors[(int) (Math.random()*Constants.villageColors.length)];
		}
		*/
		
		//if there are more than one village, make sure it has a different color
		//than the nearest village
		if (villagesList.size() > 1) {
			double min = Integer.MAX_VALUE;
			Color nearestVillage = null;
			//for every available village, find the distance between the
			//current village and the rest of the villages
			for(Villages village : villagesList) {
				double length = Math.pow(village.x-this.x, 2) + Math.pow(village.y-village.y, 2);
				length = Math.abs(Math.sqrt(length));
				
				//store the minimum length and the nearest village's color
				if (length < min) {
					min = length;
					nearestVillage = village.color();
				}
			}
			
			//randomly pick a color
			color = Constants.villageColors[(int) (Math.random()*Constants.villageColors.length)];
			
			//make sure the color is not the same as the nearest village
			while (color == nearestVillage) {
				color = Constants.villageColors[(int) (Math.random()*Constants.villageColors.length)];
			}
			
			return color;
		} else {
			//the first village will always be colored red
			return Color.RED;
		}
	}
	
	//choose an action for the current year
	private void chooseAction() {
		int rand = (int) (Math.random()*2);
		
		switch(rand) {
		case 0:
			hireUnits();
			break;
		case 1:
			constructBuildings();
			break;
		}
	}
	
	//Remove any destroyed buildings from the game
	private void destroyBrokenBuildings() {
		Queue<Buildings> brokenBuildings = new LinkedList<>();
		
		//for every building that the village has
		for(Buildings building : buildingsList) {
			//if its dead, add it to the queue to demolish the building
			if (building.health() <= 0) {
				brokenBuildings.add(building);
				
				//make sure to remove the territory that the destroyed
				//building is on from the village's ownership
				for(Buildings occupied : owned) {
					if (occupied.x() == building.x() && occupied.y() == building.y()) {
						owned.remove(occupied);
						break;
					}
				}
			}
		}
		
		//remove all broken buildings
		while(!brokenBuildings.isEmpty()) {
			buildingsList.remove(brokenBuildings.remove());
		}
	}
	
	//make action for all the existing buildings
	private void buildingControl() {
		for(Buildings building : buildingsList) {
			if (building instanceof Farms) {
				Farms farm = (Farms) building;
				farm.produce();
				
				//try to level up if you can
				if (gold >= farm.costToLevelUp()) {
					gold-= farm.costToLevelUp();
					farm.levelUp();
				}
			}
		}
	}
	
	//find open territories to construct buildings
	private Buildings findOpenTerritory() {
		for(Buildings building : owned) {
			//if a building is empty, make it occupied
			if (building.name() == "") {
				building.setName("Occupied");
				return building;
			}
		}
		//if there are no open territories, return null
		return null;
	}
	
	private void constructBuildings() {
		//choose a random building to construct
		int rand = (int) (Math.random()*2);
		Buildings openSpace = findOpenTerritory();
		
		//if an open space is available, continue
		if (openSpace != null) {
			int cost;
			if (rand == 0 && gold >= 40*buildingsList.size() && freeWorkers >= 2) {
				//construct farms
				cost = 40*buildingsList.size();
				
				Farms farm = new Farms("Farm", openSpace.x(), openSpace.y(), map, territory, unit);
				buildingsList.add(farm);
				gold-= cost;
			} else if (rand == 1 && gold >= 5*buildingsList.size()) {
				//construct houses
				cost = 5*buildingsList.size();
				
				Houses house = new Houses("House", openSpace.x(), openSpace.y(), map, territory, unit);
				buildingsList.add(house);
				gold-= cost;
			}
		}
	}
	
	//remove any dead units
	private void disposeDeadUnits() {
		Queue<Explorers> deadExplorers = new LinkedList<>();
		
		//for every explorer, if its dead then add it to queue to remove
		for(Explorers explorer : explorersList) {
			if (explorer.isDead()) {
				deadExplorers.add(explorer);
			}
		}
		
		//remove all explorers in the queue from the game
		while(!deadExplorers.isEmpty()) {
			explorersList.remove(deadExplorers.remove());
		}
	}
	
	//main control for units
	private void unitControl() {
		for(Explorers explorer : explorersList) {
			//let the explorer do its thing
			explorer.actions();
			
			//if the explorer finds new territory, occupy it
			if (explorer.getNewTerritory()) {
				territory[explorer.x()][explorer.y()] = this;
				owned.add(new Buildings("", explorer.x(), explorer.y()));
			}
		}
	}
	
	//to hire new units for the village
	private void hireUnits() {
		//randomly pick a unit
		int randUnit = (int) (Math.random()*3);
		
		if (randUnit == 0 && gold >= 15+(5*explorersList.size()) && freeWorkers >= 1) {
			//for explorers
			int cost = 15+(5*explorersList.size());
			int unitX = 0, unitY = 0;
			
			if (y-1 >= 0 && unit[x][y-1] == null) {
				unitX = x;
				unitY = y-1;
			} else if (x+1 < Constants.MAP_WIDTH && unit[x+1][y] == null) {
				unitX = x+1;
				unitY = y;
			} else if (y+1 < Constants.MAP_LENGTH && unit[x][y+1] == null) {
				unitX = x;
				unitY = y+1;
			} else if (x-1 >= 0 && unit[x-1][y] == null) {
				unitX = x-1;
				unitY = y;
			}
			
			gold-= cost;
			Explorers explorer = new Explorers("Explorer", unitX, unitY, aggro, map, territory, unit);
			
			explorersList.add(explorer);
		}
	}
	
	//update any stats
	private void updateStats() {
		//get total workers in the village
		int temp = 5;
		for(Buildings building : buildingsList) {
			if (building instanceof Houses) {
				Houses house = (Houses) building;
				temp+= house.people();
			}
		}
		totalWorkers = temp;
		
		//get free workers in the village
		for(Buildings building : buildingsList) {
			if (building instanceof Farms) {
				temp-= 2;
			}
		}
		for(Explorers explorer : explorersList) {
			temp-= 1;
		}
		freeWorkers = temp;
		
		//increase wealth
		gold++;
	}

}
