package stages;

import misc.Cells;
import misc.Buildings;
import misc.Units;

public class Tribes extends Units {
	private static int years;
	private int pop, hunger;
	private boolean nowVillage;
	
	public Tribes(String name, int x, int y, int years, Cells[][] map, Buildings[][] territory, Units[][] unit) {
		super(name, x, y, map, territory, unit);
		
		this.years = years;
		this.pop = (int) (Math.random()*10)+3;
		this.hunger = (int) (Math.random()*30)+this.pop*3;
		
		//TODO Add attack and defense and speed
		this.health = 100;
		this.aggro = (int) (Math.random()*40)+10;
		this.attack = 3;
		this.defense = 2;
	}
	
	//GETTERS
	public boolean isNowVillage() {
		return nowVillage;
	}
	
	public int pop() {
		return pop;
	}
	
	//METHODS
	public void actions() {
		checkStatus();
		turnToVillage();
		if (!isDead) {
			grabFood();
			move();
			fight();
		}
	}
	
	private void turnToVillage() {
		if (years >= 20) {
			int rand = (int) (Math.random()*100);
			int success = (int) (Math.random()*2);
			
			for(Cells neighbor : map[x][y].neighbors) {
				if (neighbor != null && neighbor.name == "Water") {
					success+= 3;
				}
			}
			
			if (rand <= success) {
				isDead = true;
				nowVillage = true;
			}
		}
	}
	
	private void grabFood() {
		boolean hasEaten = false;
		for(int i = 0; i < 5; i++) {
			if (i == 0) {
				if (map[x][y].name == "Grass") {
					hunger+= (int) (Math.random()*(pop-3));
					hasEaten = true;
				}
			} else {
				Cells currCell = map[x][y].neighbors[i-1];
				if (currCell != null && currCell.name == "Grass") {
					hunger+= (int) (Math.random()*(pop-3));
					hasEaten = true;
				} else if (currCell != null && currCell.name == "Water") {
					hunger+= (int) (Math.random()*(pop-3)) + 2;
					hasEaten = true;
				}
			}
			
			if (hasEaten) {
				break;
			}
		}
	}
	
	private void checkStatus() {
		if (health <= 0 || hunger <= 0) {
			unit[x][y] = null;
			isDead = true;
		}
	}
	
}