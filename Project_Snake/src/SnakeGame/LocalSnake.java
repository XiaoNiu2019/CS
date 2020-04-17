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

public class LocalSnake extends PlayerSnake {
	
	private int dx, dy;


	public LocalSnake(int x0, int y0, int dir, Color color) {
		super(x0, y0, dir, color);
		dx = 0;
		dy = 0;
	}
	
	
	public void moveInMap(Map map) throws ArrayIndexOutOfBoundsException{

		int l = map.getMapLength();
		int w = map.getMapWidth();
		
				switch (Keyboard.getLastKeyCode()) {
				case LEFT:  dx = -1; dy = 0; break;
				case UP:    dx = 0 ; dy = -1; break;
				case RIGHT: dx = +1; dy = 0; break;
				case DOWN:	dx = 0 ; dy = +1; break;
				default: // keep horizontal&vertical speed when any other key is pressed
				}
							
				if ((dx == 0) && (dy ==0)) {
					
					this.translate(dx, this.getDirection());
				
					Position Pos_to_go = new Position(this.getHeadPos());
					this.add(new PlayerSnakeNode(Pos_to_go.getX(), Pos_to_go.getY(), this.getBodyColor()));
					
					
					if ((this.getHeadX()<0) || (this.getHeadY()<0) ||(this.getHeadX()>=l) || (this.getHeadY()>=w)
							|| map.getBox(this.getHeadX(), this.getHeadY()).getBotSnakeStat()==true 
							|| map.getBox(this.getHeadX(), this.getHeadY()).getPlayerSnakeStat()==true
							|| map.getBox(this.getHeadX(), this.getHeadY()).getServerSnakeStat()==true ) {
						try {
							this.Output("end");
							map.getBox(Pos_to_go.getX(), Pos_to_go.getY()).updatePlayerSnakeStat(new Position(Pos_to_go.getX(),Pos_to_go.getY()));
							}
							catch (Exception e) {
								
							}
						this.isDead();
					}
					else {	
						map.getBox(Pos_to_go.getX(), Pos_to_go.getY()).updatePlayerSnakeStat(new Position(Pos_to_go.getX(),Pos_to_go.getY()));
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
				
				else {
					
					Position nextPos = new Position(this.getHeadX()+dx,this.getHeadY()+dy);
					Position LastnodePos = new Position(this.getAllNodes().get(this.getAllNodes().size()-2).getSnakeNodePos());
					
					if (LastnodePos .isEqual(new Position (nextPos))  )   {
						
						if (this.getAllPos().get(this.getAllPos().size()-1).getX() == this.getAllPos().get(this.getAllPos().size()-2).getX()) {
							int dy_to_go = this.getAllPos().get(this.getAllPos().size()-1).getY() 
									- this.getAllPos().get(this.getAllPos().size()-2).getY();
							this.translate(0, dy_to_go);
							
							Position Pos_to_go = new Position(this.getHeadPos());
							this.add(new PlayerSnakeNode(Pos_to_go.getX(), Pos_to_go.getY(),this.getBodyColor()));

							if ((this.getHeadX()<0) || (this.getHeadY()<0) ||(this.getHeadX()>=l) || (this.getHeadY()>=w)
									|| map.getBox(this.getHeadX(), this.getHeadY()).getBotSnakeStat()==true 
									|| map.getBox(this.getHeadX(), this.getHeadY()).getPlayerSnakeStat()==true
									|| map.getBox(this.getHeadX(), this.getHeadY()).getServerSnakeStat()==true ) {
								try {
									this.Output("end");
									map.getBox(Pos_to_go.getX(), Pos_to_go.getY()).updatePlayerSnakeStat(new Position(Pos_to_go.getX(),Pos_to_go.getY()));

								}
									catch (Exception e) {
										
									}
								this.isDead();
							}
							else {	
								map.getBox(Pos_to_go.getX(), Pos_to_go.getY()).updatePlayerSnakeStat(new Position(Pos_to_go.getX(),Pos_to_go.getY()));
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

						else {
							int dx_to_go = this.getAllPos().get(this.getAllPos().size()-1).getX() 
									- this.getAllPos().get(this.getAllPos().size()-2).getX();
							this.translate(dx_to_go,0);
							
							Position Pos_to_go = new Position(this.getHeadPos());
							this.add(new PlayerSnakeNode(Pos_to_go.getX(), Pos_to_go.getY(),this.getBodyColor()));


							if ((this.getHeadX()<0) || (this.getHeadY()<0) ||(this.getHeadX()>=l) || (this.getHeadY()>=w)
									|| map.getBox(this.getHeadX(), this.getHeadY()).getBotSnakeStat()==true 
									|| map.getBox(this.getHeadX(), this.getHeadY()).getPlayerSnakeStat()==true
									|| map.getBox(this.getHeadX(), this.getHeadY()).getServerSnakeStat()==true ) {
								try {
									this.Output("end");
									map.getBox(Pos_to_go.getX(), Pos_to_go.getY()).updatePlayerSnakeStat(new Position(Pos_to_go.getX(),Pos_to_go.getY()));
	
								}
									catch (Exception e) {
										
									}
								this.isDead();
							}
							else {	
								map.getBox(Pos_to_go.getX(), Pos_to_go.getY()).updatePlayerSnakeStat(new Position(Pos_to_go.getX(),Pos_to_go.getY()));
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
										this.Output("error");
									}
								}
								catch(Exception e) {
									//System.out.println("error");
								}
									
							}
						}
											
					}
			
						
					
					else {
						//}
							this.translate(dx, dy);
							
							Position Pos_to_go = new Position(this.getHeadPos());
							this.add(new PlayerSnakeNode(Pos_to_go.getX(), Pos_to_go.getY(),this.getBodyColor()));

							if ((this.getHeadX()<0) || (this.getHeadY()<0) ||(this.getHeadX()>=l) || (this.getHeadY()>=w)
									|| map.getBox(this.getHeadX(), this.getHeadY()).getBotSnakeStat()==true 
									|| map.getBox(this.getHeadX(), this.getHeadY()).getPlayerSnakeStat()==true
									|| map.getBox(this.getHeadX(), this.getHeadY()).getServerSnakeStat()==true ) {
								try {
									this.Output("end");
									map.getBox(Pos_to_go.getX(), Pos_to_go.getY()).updatePlayerSnakeStat(new Position(Pos_to_go.getX(),Pos_to_go.getY()));

								}
									catch (Exception e) {
										
									}

								this.isDead();
							}
							else {	
								map.getBox(Pos_to_go.getX(), Pos_to_go.getY()).updatePlayerSnakeStat(new Position(Pos_to_go.getX(),Pos_to_go.getY()));
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

				}				
		}
			

		
	}











