package misc;


public class Cells {
	public String name;
	public int x, y;
	private int slow;
	private boolean visited;
	public Cells[] neighbors;
	
	protected final int MAP_WIDTH = 1200/2;
	protected final int MAP_LENGTH = 650/2;
	
	public Cells(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
		
		this.neighbors = new Cells[4];
		
		//change the wait time for terrains
		if (name == "Water" || name == "Mountain") {
			slow = -1;
		} else if (name == "Sand") {
			slow = 1;
		} else if (name == "Grass") {
			slow = 0;
		}
		
		this.visited = false;
	}
	
	//GETTERS
	public boolean visited() {
		return visited;
	}
	
	public int getSlow() {
		return slow;
	}
	
	//SETTERS
	public void switchVisit(boolean visit) {
		visited = visit;
	}
}
