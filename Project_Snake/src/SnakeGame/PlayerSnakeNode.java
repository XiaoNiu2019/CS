package SnakeGame;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

public class PlayerSnakeNode extends Rectangle {
	
	private static final double SIZE = Game.ELEMENT_SIZE; 

	private Position pos;
	
	public PlayerSnakeNode(Position position,Color color ){
		
		super(SIZE, SIZE, color);
		this.pos = new Position (position.getX(), position.getY());
		setArcWidth(SIZE/2);
		setArcHeight(SIZE/2);
		setStroke(Color.BLACK);
		setStrokeWidth(3.0);
		setStrokeLineCap(StrokeLineCap.ROUND);
		updatePosition();
		
	}
	
	public PlayerSnakeNode(int x, int y, Color color){
		
		super(SIZE, SIZE, color);
		this.pos = new Position (x, y);
		setArcWidth(SIZE/2);
		setArcHeight(SIZE/2);
		setStroke(Color.BLACK);
		setStrokeWidth(3.0);
		setStrokeLineCap(StrokeLineCap.ROUND);
		updatePosition();
	}
	
	public PlayerSnakeNode(PlayerSnakeNode node){
		super(SIZE, SIZE);
		Paint snakenodecolor = node.getFill();
		this.setFill(snakenodecolor);
		this.pos = new Position (node.getSnakeNodePos().getX(), node.getSnakeNodePos().getY());
		setArcWidth(SIZE/2);
		setArcHeight(SIZE/2);
		setStroke(Color.BLACK);
		setStrokeWidth(3.0);
		setStrokeLineCap(StrokeLineCap.ROUND);
		updatePosition();
		
	}
	
/*	
	public PlayerSnakeNode(Position position){
		
		super(SIZE, SIZE, Color.YELLOW);
		this.pos = new Position (position.getX(), position.getY());
		setArcWidth(SIZE/2);
		setArcHeight(SIZE/2);
		setStroke(Color.BLACK);
		setStrokeWidth(3.0);
		setStrokeLineCap(StrokeLineCap.ROUND);
		updatePosition();
		
	}*/
	
/*	public PlayerSnakeNode(int x, int y){
		
		super(SIZE, SIZE, Color.YELLOW);
		this.pos = new Position (x, y);
		setArcWidth(SIZE/2);
		setArcHeight(SIZE/2);
		setStroke(Color.BLACK);
		setStrokeWidth(3.0);
		setStrokeLineCap(StrokeLineCap.ROUND);
		updatePosition();
		
	}*/
	
	public void updatePosition()
	{
		// these are the methods that actually position the Node on screen
		setTranslateX(pos.getX()*Game.ELEMENT_SIZE );
		setTranslateY(pos.getY()*Game.ELEMENT_SIZE );
	}
	

	
	public Position getSnakeNodePos() {
		Position SnakeNodePos = new Position(pos.getX(),pos.getY());
		return SnakeNodePos;
	}


}
