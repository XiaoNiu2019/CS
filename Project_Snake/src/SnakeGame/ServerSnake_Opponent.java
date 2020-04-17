package SnakeGame;

import java.util.ArrayList;

import java.util.Random;
import java.io.IOException;
import java.lang.ArrayIndexOutOfBoundsException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

public class ServerSnake_Opponent extends ServerSnake {
	private BufferedReader bufferedreader;
	
	public ServerSnake_Opponent(int x0, int y0 , Color color) {
		super(x0, y0, color);
	}
	
	public void initializeBufferedReader (InputStream inputstream) {
		bufferedreader = new BufferedReader(new InputStreamReader (inputstream));
	}
	
	public void moveInMap(Map map) throws ArrayIndexOutOfBoundsException{
	//	Position Pos_received = new Position(detectedPosition(map));

		//if (! Pos_received.isEqual(new Position(-100,-100))) {
		int l = map.getMapLength();
		int w = map.getMapWidth();
			
			try {
				
				String direction =  bufferedreader.readLine();
	
				if(direction.compareTo("left")==0) {
					this.translate(-1,0);
					this.add(new ServerSnakeNode(this.getHeadX(), this.getHeadY(), this.getBodyColor()));
					
					if ((this.getHeadX()<0) || (this.getHeadY()<0) ||(this.getHeadX()>=l) || (this.getHeadY()>=w		
							|| map.getBox(this.getHeadX(), this.getHeadY()).getBotSnakeStat()==true 
							|| map.getBox(this.getHeadX(), this.getHeadY()).getPlayerSnakeStat()==true
							|| map.getBox(this.getHeadX(), this.getHeadY()).getServerSnakeStat()==true)){
						try {
							map.getBox(this.getHeadX(), this.getHeadY()).updateServerSnakeStat(new Position(this.getHeadX(),this.getHeadY()));
							}
							catch (Exception e) {
								
							}
						this.isDead();
						
					}
				
						else {

						map.getBox(this.getHeadX(), this.getHeadY()).updateServerSnakeStat(new Position(this.getHeadX(),this.getHeadY()));

					}
					

				}
				else if(direction.compareTo("right")==0) {
					this.translate(1,0);
					
					this.add(new ServerSnakeNode(this.getHeadX(), this.getHeadY(), this.getBodyColor()));
					if ((this.getHeadX()<0) || (this.getHeadY()<0) ||(this.getHeadX()>=l) || (this.getHeadY()>=w						
							|| map.getBox(this.getHeadX(), this.getHeadY()).getBotSnakeStat()==true 
							|| map.getBox(this.getHeadX(), this.getHeadY()).getPlayerSnakeStat()==true
							|| map.getBox(this.getHeadX(), this.getHeadY()).getServerSnakeStat()==true)){
						try {
							map.getBox(this.getHeadX(), this.getHeadY()).updateServerSnakeStat(new Position(this.getHeadX(),this.getHeadY()));
	
						}
							catch (Exception e) {

								
							}	
						this.isDead();

					}
				
						else {
						
						map.getBox(this.getHeadX(), this.getHeadY()).updateServerSnakeStat(new Position(this.getHeadX(),this.getHeadY()));

					}

				}
				else if(direction.compareTo("up")==0) {
					this.translate(0,-1);
					
					this.add(new ServerSnakeNode(this.getHeadX(), this.getHeadY(), this.getBodyColor()));
					if ((this.getHeadX()<0) || (this.getHeadY()<0) ||(this.getHeadX()>=l) || (this.getHeadY()>=w				
							|| map.getBox(this.getHeadX(), this.getHeadY()).getBotSnakeStat()==true 
							|| map.getBox(this.getHeadX(), this.getHeadY()).getPlayerSnakeStat()==true
							|| map.getBox(this.getHeadX(), this.getHeadY()).getServerSnakeStat()==true)){
						try {
							map.getBox(this.getHeadX(), this.getHeadY()).updateServerSnakeStat(new Position(this.getHeadX(),this.getHeadY()));
							}
							catch (Exception e) {
								
							}
						this.isDead();
					}
				
						else {
						
						map.getBox(this.getHeadX(), this.getHeadY()).updateServerSnakeStat(new Position(this.getHeadX(),this.getHeadY()));

					}
	
				}
				else if(direction.compareTo("down")==0) {
					this.translate(0,1);
					this.add(new ServerSnakeNode(this.getHeadX(), this.getHeadY(), this.getBodyColor()));
					
					if ((this.getHeadX()<0) || (this.getHeadY()<0) ||(this.getHeadX()>=l) || (this.getHeadY()>=w					
							|| map.getBox(this.getHeadX(), this.getHeadY()).getBotSnakeStat()==true 
							|| map.getBox(this.getHeadX(), this.getHeadY()).getPlayerSnakeStat()==true
							|| map.getBox(this.getHeadX(), this.getHeadY()).getServerSnakeStat()==true)){
						try {
							map.getBox(this.getHeadX(), this.getHeadY()).updateServerSnakeStat(new Position(this.getHeadX(),this.getHeadY()));
							}
							catch (Exception e) {
								
							}
							this.isDead();
					}
						else {
						map.getBox(this.getHeadX(), this.getHeadY()).updateServerSnakeStat(new Position(this.getHeadX(),this.getHeadY()));

					}

				}
				else {
					this.isDead();

				}


			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

	}



	

}


