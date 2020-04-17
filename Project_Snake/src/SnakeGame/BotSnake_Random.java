package SnakeGame;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import java.util.Random;

import java.lang.ArrayIndexOutOfBoundsException;


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

public class BotSnake_Random extends BotSnake{
	
	private int dx, dy;
	
	public BotSnake_Random(int x0, int y0, Color color) {
		super(x0, y0, color);
	}
	
	public void moveInMap(Map map) throws ArrayIndexOutOfBoundsException{
		//Position Pos_received = new Position(detectedPosition(map));
		//if (! Pos_received.isEqual(new Position(-100,-100))) {
			

			int x = this.getHeadX();
			int y = this.getHeadY();
			int l = map.getMapLength();			
			int w = map.getMapLength();	
			
			ArrayList<Position> All_Positions = new ArrayList<Position>();
			
			All_Positions.add(new Position(x+1,y));
			All_Positions.add(new Position(x-1,y));
			All_Positions.add(new Position(x,y+1));
			All_Positions.add(new Position(x,y-1));
			
			try {
			for (Position i : All_Positions) {
				if((this.getAllNodes().get(this.getAllNodes().size()-2)).getSnakeNodePos().isEqual(i) ){	
					All_Positions.remove(i);
				}
			}
			}
			catch (Exception e) {
				
			}
			
			int randomIndex = new Random().nextInt(All_Positions.size());
			Position Pos_to_go= new Position(All_Positions.get(randomIndex));
			this.moveTo(Pos_to_go.getX(),Pos_to_go.getY());
			this.add(new BotSnakeNode(Pos_to_go.getX(), Pos_to_go.getY(), this.getBodyColor()));
			
			if ((this.getHeadX()<0) || (this.getHeadY()<0) ||(this.getHeadX()>=l) || (this.getHeadY()>=w)
					|| map.getBox(this.getHeadX(), this.getHeadY()).getBotSnakeStat()==true 
					|| map.getBox(this.getHeadX(), this.getHeadY()).getPlayerSnakeStat()==true
					|| map.getBox(this.getHeadX(), this.getHeadY()).getServerSnakeStat()==true ) {
				try {
					this.Output("end");
				map.getBox(Pos_to_go.getX(), Pos_to_go.getY()).updateBotSnakeStat(new Position(Pos_to_go.getX(),Pos_to_go.getY()));

				}
				catch (Exception e) {

				}
				this.isDead();

			}
			else {	
				map.getBox(Pos_to_go.getX(), Pos_to_go.getY()).updateBotSnakeStat(new Position(Pos_to_go.getX(),Pos_to_go.getY()));
				dx = this.getAllPos().get(this.getAllPos().size()-1).getX()- 
						this.getAllPos().get(this.getAllPos().size()-2).getX();
				dy = this.getAllPos().get(this.getAllPos().size()-1).getY()- 
						this.getAllPos().get(this.getAllPos().size()-2).getY();

				try {
					
					if(dx == -1 && dy ==0) {
						this.Output("left");
						
					}
					else if (dx == 1 && dy ==0) {
						this.Output("right");
					}
					else if (dx == 0 && dy == -1) {
						this.Output("up");
					}
					else if (dx == 0 && dy == +1){
						this.Output("down");
					}
					else {
						this.Output("end");
					}
				}
				catch(Exception e) {
					//System.out.println("error");
				}
			
			
			
			}


	}

	@Override
	public void moveInMap(Map map, Snake snake) {
		// TODO Auto-generated method stub
		
	}

	
	
}
