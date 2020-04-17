package SnakeGame;


public class Map {
	
	private Box[][] boxes;
	private int MapLength;
	private int MapWidth;
	
	
	public Map (int length, int width) {
		
		MapLength = length;
		MapWidth = width;
		boxes = new Box[MapLength][MapWidth];
		
		for (int i = 0; i<MapLength; i++) {
			for (int j = 0 ; j<MapWidth; j++) {
				boxes[i][j] = new Box(i,j);
			}
		}
	}
	
	public Map (Map map) {
		this.MapLength = map.getMapLength();
		this.MapWidth = map.getMapWidth();
		this.boxes = new Box[MapLength][MapWidth];
		for (int i = 0; i<MapLength; i++) {
			for (int j = 0 ; j<MapWidth; j++) {
				boxes[i][j] = new Box(map.getBox(i,j));
			}
		}
		
	}
	
	
	public int getMapLength(){
		return MapLength;
	}
	
	public int getMapWidth(){
		return MapWidth;
	}
	
	public Box getBox(int x, int y) {

		return boxes[x][y];
		

	}

	
	public void  addBotSnake(BotSnake botsnake) {
		int snake_size = botsnake.getLength();
		for (int i = 0; i < snake_size; i++) {
			int x = botsnake.getAllPos().get(i).getX();
			int y = botsnake.getAllPos().get(i).getY();
			boxes[x][y].updatePositionTo (new Position(x,y));
			boxes[x][y].updateBotSnakeStatTo(true);	
		}
		
	}
	
	
	public void  addPlayerSnake(PlayerSnake playersnake) {
		int snake_size = playersnake.getLength();
		for (int i = 0; i < snake_size; i++) {
			int x = playersnake.getAllPos().get(i).getX();
			int y = playersnake.getAllPos().get(i).getY();
			boxes[x][y].updatePositionTo (new Position(x,y));
			boxes[x][y].updatePlayerSnakeStatTo(true);	
		}
		
	}
	
	public void  addServerSnake(ServerSnake serversnake) {
		int snake_size = serversnake.getLength();
		for (int i = 0; i < snake_size; i++) {
			int x = serversnake.getAllPos().get(i).getX();
			int y = serversnake.getAllPos().get(i).getY();
			boxes[x][y].updatePositionTo (new Position(x,y));
			boxes[x][y].updateServerSnakeStatTo(true);	
		}
		
	}
	

/*	public void displayMap() {

		//System.out.println(Arrays.deepToString(boxes));
		for (int i = 0; i <MapLength; i++) {
			for (int j = 0; j <MapWidth; j++) {
				boxes[i][j].display();
			}
			System.out.println();
		}
	
		
	}*/
	
}
