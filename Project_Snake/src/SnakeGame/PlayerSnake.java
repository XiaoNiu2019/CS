package SnakeGame;
import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

public abstract class PlayerSnake implements Snake {
	private boolean Stat; // True if the snake is alive; False if the snake is dead.
	private int Length;
	private Position HeadPos;
	private ArrayList<PlayerSnakeNode> AllNodes; 
	private Color HeadColor;
	private Color BodyColor;
	private DataOutputStream outputstream;
	private int Direction;
	
	public PlayerSnake(int x0, int y0, int direction, Color color) {
		HeadPos = new Position (x0,y0);
		AllNodes = new ArrayList<PlayerSnakeNode>();
		AllNodes.add(new PlayerSnakeNode(HeadPos, color));
		Length = AllNodes.size();
		Stat = true; 
		BodyColor = color;
		Direction = direction;
	}
	
	
	public int getDirection() {
		return Direction;
	}
	public Color getBodyColor() {
		return BodyColor;
	}
	
	public void initializeDataOutputStream(OutputStream outputstream_) {
		outputstream = new DataOutputStream (outputstream_);
	}
	
	public void Output(String message) throws IOException {
		outputstream.writeBytes(message);
		outputstream.writeBytes("\n");
		outputstream.flush();
	}
	
	public abstract void moveInMap(Map map);
	
	public int getLength() {
		return AllNodes.size();
	}
	
	
	public int getHeadX() {
		return HeadPos.getX();
	}
	
	public int getHeadY() {
		return HeadPos.getY();
	}
	
	public boolean getStat() {
		return Stat;
	}
	
	public ArrayList<PlayerSnakeNode> getAllNodes() {
		
		return AllNodes;
	}
	
	public ArrayList<Position> getAllPos() {
		ArrayList<Position> AllPos = new ArrayList<Position>();
		for (int i = 0; i<AllNodes.size();i++) {
			AllPos.add(AllNodes.get(i).getSnakeNodePos());
		}
		return AllPos;
	}
	
	public void moveTo (int x_new, int y_new) {
		HeadPos.moveTo(x_new, y_new);
	}
	
	public void translate(int dx, int dy) {
		HeadPos.translate(dx, dy);
	}
	
	public void add(PlayerSnakeNode node) {
		AllNodes.add(new PlayerSnakeNode(node));
	}
	
	public void updateLengthTo(int length) {
		Length = length;
	}
	
	public String toString() {
	  	return "(" + HeadPos.getX() + "," + HeadPos.getY() + ")";
	}
	
	public void isDead() {
		Stat = false;
		
	}
	
	public Position getHeadPos() {
		return new Position (HeadPos);
	}
	
	public Position detectedPosition(Map map) throws ArrayIndexOutOfBoundsException{
		
		int x = this.getHeadX();
		int y = this.getHeadY();
		
		int l = map.getMapLength();
		int w = map.getMapWidth();
	if(x>= 0 && x<l && y>=0&& y<w) {
		if (map.getBox(x, y).isOccupied()==false) {	
		ArrayList<Position> All_Positions = new ArrayList<Position>();
		
		All_Positions.add(new Position(x+1,y));
		All_Positions.add(new Position(x-1,y));
		All_Positions.add(new Position(x,y+1));
		All_Positions.add(new Position(x,y-1));
		
		ArrayList<Position> Positions_Possibles_initial = new ArrayList<Position>();
		

		for (int i = 0; i < All_Positions.size(); i++) {
			if ( All_Positions.get(i).getX() >= 0 && All_Positions.get(i).getX() < l 
					&& All_Positions.get(i).getY()>=0 && All_Positions.get(i).getY()< w) {
				Positions_Possibles_initial.add(new Position(All_Positions.get(i)));
			}
		}
				

		ArrayList<Position> Positions_Possibles_final = new ArrayList<Position>();
		try {
		for (Position i : Positions_Possibles_initial) {
			if(map.getBox(i.getX(), i.getY()).getBotSnakeStat()== false 
					&& map.getBox(i.getX(), i.getY()).getPlayerSnakeStat()== false
					&& map.getBox(i.getX(), i.getY()).getServerSnakeStat()== false) {
			Positions_Possibles_final.add(new Position(i));
			}
		}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("The game is end");
	
		}
		
		if (Positions_Possibles_final.size() == 0) {
			Position Position_detected = new Position(-100, -100);
			return Position_detected;

		}
		
		else {	
		int randomIndex = new Random().nextInt(Positions_Possibles_final.size());
		Position Position_detected = new Position(Positions_Possibles_final.get(randomIndex));
		
		while(this.getAllPos().contains(Position_detected)){
			Positions_Possibles_final.remove(randomIndex);
			int randomIndex_new = new Random().nextInt(Positions_Possibles_final.size());
			Position_detected = new Position(Positions_Possibles_final.get(randomIndex_new));
		}
/*		System.out.println(this.getAllPos());
		System.out.println(Position_detected);*/
		
		return Position_detected;

		}
	}	
	else{
		Position Position_detected = new Position(-100, -100);
		return Position_detected;
	}
	}
		else {
			Position Position_detected = new Position(-100, -100);
			return Position_detected;
		}
	
	}
}
	

