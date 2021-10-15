package misc;

import stages.Tribes;

public class Units {
	public static Cells[][] map;
	public static Buildings[][] territory;
	public static Units[][] unit;
	protected Units enemy; 
	//TODO allegiance;
	protected String name;
	protected int x, y, prevX, prevY;
	protected int health, attack, defense, slowed, aggro;
	protected boolean fighting, isDead;
	
	public Units(String name, int x, int y, Cells[][] map, Buildings[][] territory, Units[][] unit) {
		this.name = name;
		
		enemy = null;
		//allegiance = null;
		
		this.x = x;
		this.y = y;
		this.prevX = x;
		this.prevY = y;
		
		this.health = 0;
		this.attack = 0;
		this.defense = 0;
		this.slowed = 0;
		this.aggro = 0;
		
		this.fighting = false;
		this.isDead = false;
		
		this.map = map;
		this.territory = territory;
		this.unit = unit;
	}
	
	//GETTERS
	public String name() {
		return name;
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public int prevX() {
		return prevX;
	}
	
	public int prevY() {
		return prevY;
	}
	
	public int aggro() {
		return aggro;
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public boolean isFighting() {
		return fighting;
	}
	
	//METHODS
	protected void fight() {
		Units tempEnemy = null;
		if (!fighting) {
			tempEnemy = possibleEnemy();
		}
		
		if (fighting) {
			int damage = attack+aggro-enemy.defense;
			if (damage < 0) {
				damage = 0;
			}
			enemy.health-= damage;
			
			if (enemy.health <= 0) {
				fighting = false;
			}
		} else if (tempEnemy != null && !fighting) {
			enemy = tempEnemy;
			enemy.enemy = this;
			
			int rand = (int) (Math.random()*100);
			int totalAggro = aggro+enemy.aggro;
			
			if (rand <= totalAggro) {
				fighting = true;
				enemy.fighting = true;
			}
		}
	}
	
	private Units possibleEnemy() {
		if (y-1 >= 0 && unit[x][y-1] != null) {
			return unit[x][y-1];
		} else if (x+1 < 600 && unit[x+1][y] != null) {
			return unit[x+1][y];
		} else if (y+1 < 325 && unit[x][y+1] != null) {
			return unit[x][y+1];
		} else if (x-1 >= 0 && unit[x-1][y] != null) {
			return unit[x-1][y];
		}
		return null;
	}
	
	protected void move() {
		if (!fighting) {
			if (slowed == 0) {
				prevX = x;
				prevY = y;
				
				int dir = (int) (Math.random()*4);
				
				//Pick a random direction to walk
				if (dir == 0) {
					//NORTH
					if (y-1 >= 0) {
						y--;
					} else {
						y = 0;
					}
				} else if (dir == 1) {
					//EAST
					if (x+1 < 600) {
						x++;
					} else {
						x = 599;
					}
				} else if (dir == 2) {
					//SOUTH
					if (y+1 < 325) {
						y++;
					} else {
						y = 324;
					}
				} else if (dir == 3) {
					//WEST
					if (x-1 >= 0) {
						x--;
					} else {
						x = 0;
					}
				}
				
				//no movement for some terrain or if there's another unit
				if (map[x][y].name == "Water" || map[x][y].name == "Mountain" ||
						unit[x][y] != null) {
					x = prevX;
					y = prevY;
				}
				
				unit[prevX][prevY] = null;
				unit[x][y] = this;
				slowed = map[x][y].getSlow();
			} else {
				slowed--;
			}
		}
	}
}
