package SnakeGame;

public class Position {
	
		private int x;
		private int y;
		
	public Position() {
			x=0;
			y=0;			
		}
	
	public Position(int x0, int y0) {
			x=x0;
			y=y0;		
	}
	
	public Position(Position pos) {
			x=pos.x;
			y=pos.y;
	}

	public int getX() {
			return x;
	}

	public int getY() {
			return y;

	}
	
	public boolean isEqual(Position pos) {
		if (this.getX() == pos.getX() && this.getY() == pos.getY()) {
			return true;
		}
		
		return false;
		
	
	}
	
	public void moveTo (int x_new, int y_new) {
		x = x_new;
		y = y_new;
	}

	public void translate(int dx, int dy) {
			x = x + dx;
			y = y + dy;

	}

	public void display() {
			System.out.println("(" + x + "," + y + ")");		
	}
	
	public String toString() {
		  	return "(" + x + "," + y + ")";
		}
	
	
	
	
	}






