package SnakeGame;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

import java.util.Random;

import java.lang.ArrayIndexOutOfBoundsException;
public class BotSnake_Smart extends BotSnake {
	
	private double dx, dy;
	
	public BotSnake_Smart(int x0, int y0, Color color) {
		super(x0, y0, color);
		dx = 0;
		dy = 1;
	}
	
	
	public void moveInMap(Map map) throws ArrayIndexOutOfBoundsException{

		
		int x = this.getHeadX();
		int y = this.getHeadY();
		
		int l = map.getMapLength();
		int w = map.getMapWidth();

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
		ArrayList<Position> Positions_Possibles_intermediaire = new ArrayList<Position>();

		for (int i = 0; i <  Positions_Possibles_initial.size(); i++) {
			if (!this.getAllPos().contains(Positions_Possibles_initial.get(i))) {
				Positions_Possibles_intermediaire .add(new Position(Positions_Possibles_initial.get(i)));
			}
		}
		
		
		ArrayList<Position> Positions_Possibles_final = new ArrayList<Position>();
		try {
		for (Position i : Positions_Possibles_intermediaire) {
			if(		   map.getBox(i.getX(), i.getY()).getBotSnakeStat()== false 
					&& map.getBox(i.getX(), i.getY()).getPlayerSnakeStat()== false
					&& map.getBox(i.getX(), i.getY()).getServerSnakeStat()== false) {
					Positions_Possibles_final.add(new Position(i));
			}
		}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("The game is end");
	
		}
		
		
		
		
		Position Position_detected = new Position();
		
		if (Positions_Possibles_final.size() == 0) {
			 Position_detected = new Position(this.getHeadX(), this.getHeadY());
		}
		else {	
		int randomIndex = new Random().nextInt(Positions_Possibles_final.size());
		Position_detected = new Position(Positions_Possibles_final.get(randomIndex));
		

		}
	
			this.moveTo(Position_detected.getX(),Position_detected.getY());
			this.add(new BotSnakeNode (Position_detected.getX(),Position_detected.getY(),this.getBodyColor()));


				if ((this.getHeadX()<0) || (this.getHeadY()<0) ||(this.getHeadX()>=l) || (this.getHeadY()>=w)
						|| map.getBox(this.getHeadX(), this.getHeadY()).getBotSnakeStat()==true 
						|| map.getBox(this.getHeadX(), this.getHeadY()).getPlayerSnakeStat()==true
						|| map.getBox(this.getHeadX(), this.getHeadY()).getServerSnakeStat()==true 
						|| (this.getAllPos().get(this.getLength()-2).isEqual(new Position (this.getHeadX(),this.getHeadY())))) {
					try {
						this.Output("end");
						map.getBox(Position_detected.getX(), Position_detected.getY()).updateBotSnakeStat(new Position(Position_detected.getX(),Position_detected.getY()));

						}
						catch (Exception e) {
							
						}

					this.isDead();
				}
				else {	
					map.getBox(Position_detected.getX(), Position_detected.getY()).updateBotSnakeStat(new Position(Position_detected.getX(),Position_detected.getY()));
			
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


