package SnakeGame;

import java.io.BufferedReader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

public abstract class ServerSnake implements Snake {
	
	private boolean Stat; // True if the snake is alive; False if the snake is dead.
	private int Length;
	private Position HeadPos;
	private ArrayList<ServerSnakeNode> AllNodes; 
	private BufferedReader bufferedreader;
	private Color BodyColor;
	
	public ServerSnake(int x0, int y0, Color color) {
		HeadPos = new Position (x0,y0);
		AllNodes = new ArrayList<ServerSnakeNode>();
		AllNodes.add(new ServerSnakeNode(HeadPos, color));
		Length = AllNodes.size();
		Stat = true; 
		BodyColor = color;
	}
	
	
	
	public abstract void moveInMap(Map map);


	public abstract void initializeBufferedReader (InputStream inputstream) ;

	
	public BufferedReader getBufferedReader() {
		return bufferedreader;

	}
	
	public Color getBodyColor() {
		return BodyColor;
	}
	
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
	
	public ArrayList<ServerSnakeNode> getAllNodes() {
		
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
	
	public void add(ServerSnakeNode node) {
		AllNodes.add(new ServerSnakeNode(node));
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
	
	
}









