package buildings;

import misc.Buildings;
import misc.Cells;
import misc.Units;

public class Houses extends Buildings{
	private int people, level;

	public Houses(String name, int x, int y, Cells[][] map, Buildings[][] territory, Units[][] unit) {
		super(name, x, y, map, territory, unit);

		this.level = 1;
		this.people = 2;
	}
	
	//GETTERS
	public int people() {
		return people;
	}
	
	public int costToLevelUp() {
		return 15*level;
	}
	
	//METHODS
	public void levelUp() {
		level++;
		people++;
	}
	
}
