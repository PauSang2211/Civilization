package misc;

import java.util.ArrayList;

public class Buildings {
	public Cells[][] map;
	public Buildings[][] territory;
	public Units[][] unit;
	protected ArrayList<Buildings> owned;
	protected String name;
	protected int x, y;
	protected int health, defense, pop, hunger;
	protected boolean destroyed;
	
	//for the "owned" variable to make empty buildings
	public Buildings(String name, int x, int y) {
		this.name = name;
		
		this.x = x;
		this.y = y;
	}
	
	//Main constructor
	public Buildings(String name, int x, int y, Cells[][] map, Buildings[][] territory,
			Units[][] unit) {
		this.map = map;
		this.territory = territory;
		this.unit = unit;
		
		//TODO make sure this isn't available for non-village buildings
		this.owned = new ArrayList<Buildings>();
		
		this.name = name;
		
		this.x = x;
		this.y = y;
		
		this.health = 0;
		this.defense = 0;
		this.pop = 0;
		this.hunger = 0;
		
		this.destroyed = false;
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
	
	public int health() {
		return health;
	}
	
	//SETTERS
	public void setName(String newName) {
		name = newName;
	}
}
