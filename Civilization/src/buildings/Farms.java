package buildings;
import misc.Buildings;
import misc.Cells;
import misc.Units;

public class Farms extends Buildings{
	private int level, production, speed, progress;

	public Farms(String name, int x, int y, Cells[][] map, Buildings[][] territory, Units[][] unit) {
		super(name, x, y, map, territory, unit);
		
		this.health = 50;
		this.defense = 1;
		
		this.level = 1;
		this.production = 2;
		this.progress = 2;
		this.speed = 2;
		
	}
	
	//GETTERS
	public int level() {
		return level;
	}
	
	public int production() {
		return production;
	}
	
	public int speed() {
		return speed;
	}
	
	public int costToLevelUp() {
		return 20*level;
	}
	
	//METHODS
	public void levelUp() {
		level++;
		production+= 2;
		progress = speed;
	}
	
	public int produce() {
		if (progress == 0) {
			//if wait time is over, return production value
			progress = speed;
			return production;
		} else {
			//if wait time is not over, continue to wait
			progress--;
			return 0;
		}
	}
}
