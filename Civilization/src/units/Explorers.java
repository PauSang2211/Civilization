package units;
import misc.Cells;
import misc.Buildings;
import misc.Units;
import stages.Tribes;

public class Explorers extends Units {
	private boolean gainTerritory;

	public Explorers(String name, int x, int y, int aggro, Cells[][] map, Buildings[][] territory,
			Units[][] unit) {
		super(name, x, y, map, territory, unit);
		
		this.health = 70;
		this.attack = 2;
		this.defense = 1;
		this.slowed = 0;
		this.aggro = aggro;
		
		this.gainTerritory = false;
	}
	
	//GETTERS
	public boolean getNewTerritory() {
		return gainTerritory;
	}
	
	//METHODS
	public void actions() {
		establishTerritory();
		move();
	}
	
	//to establish open territories
	private void establishTerritory() {
		if (territory[x][y] == null) {
			//if open territory, the village will gain the territory
			//and the unit will disband
			gainTerritory = true;
			isDead = true;
		}
	}
	
}