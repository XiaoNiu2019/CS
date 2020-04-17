package SnakeGame;
	
	public class Box{
	
	private Position Pos;
	private boolean BotSnakeStat; 
	private boolean PlayerSnakeStat; 
	private boolean ServerSnakeStat;

	
	public Box() {
		Pos = new Position(0,0);
		BotSnakeStat = false;
		PlayerSnakeStat = false;
		ServerSnakeStat = false;	
	}
	
	public void updatePositionTo(Position newPos) {
		Pos = new Position(newPos);
	}
	
	public void updateBotSnakeStatTo(boolean newStat) {
		BotSnakeStat = newStat;
	}
	
	public void updatePlayerSnakeStatTo(boolean newStat) {
		PlayerSnakeStat = newStat;
	}
	
	public void updateServerSnakeStatTo(boolean newStat) {
		ServerSnakeStat = newStat;
	}
	
	public Box(int x, int y) {
		Pos = new Position(x,y);
		BotSnakeStat = false;
		PlayerSnakeStat = false;
		ServerSnakeStat = false;
	}
	
	
	public Box(Box box) {
		Pos = new Position(box.getPosition());
		BotSnakeStat = box.getBotSnakeStat();	
		PlayerSnakeStat = box.getPlayerSnakeStat();
		ServerSnakeStat = box.getServerSnakeStat();		
	}
	
	
	public Position getPosition() {
		return new Position(Pos);
	}
	
	public boolean getBotSnakeStat() {
		return BotSnakeStat;
	}
	
	public boolean getPlayerSnakeStat() {
		return PlayerSnakeStat;
	}
	
	public boolean getServerSnakeStat() {
		return ServerSnakeStat;
	}
	
	public void updateBotSnakeStat(Position pos) {
		Pos = new Position(pos);
		BotSnakeStat = true;	
	}
	
	public void updatePlayerSnakeStat(Position pos) {
		Pos = new Position(pos);
		PlayerSnakeStat = true;	
	}
	public void updateServerSnakeStat(Position pos) {
		Pos = new Position(pos);
		ServerSnakeStat = true;		
	}
	
	public boolean isCollied() {
		if (this.BotSnakeStat == true && this.PlayerSnakeStat == true && this.ServerSnakeStat == true) {
			return true;
		}
		else if (this.BotSnakeStat == true && this.PlayerSnakeStat == true && this.ServerSnakeStat == false ) {
			return true;
		}
		else if (this.BotSnakeStat == true && this.PlayerSnakeStat == false && this.ServerSnakeStat == true ) {
			return true;
		}
		else if (this.BotSnakeStat == false && this.PlayerSnakeStat == true && this.ServerSnakeStat == true) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean isOccupied() {
		if (this.BotSnakeStat == true || this.PlayerSnakeStat == true || this.ServerSnakeStat == true) {
			return true;
		}
		else {
			return false;
		}
	}
}
