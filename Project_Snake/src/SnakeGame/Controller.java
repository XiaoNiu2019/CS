package SnakeGame;

import java.util.ArrayList;

public class Controller {
	
	private int nbSteps;
	//private long timeStep;
	private ArrayList<BotSnake> botsnakes;
	private ArrayList<PlayerSnake> playersnakes;
	private ArrayList<ServerSnake> serversnakes;
	private Map gamemap;
	
	public Controller(int NbSteps, Map map) {
		nbSteps = NbSteps;
		//timeStep = TimeStep;
		botsnakes = new ArrayList<BotSnake>();
		playersnakes = new ArrayList<PlayerSnake>();
		serversnakes = new ArrayList<ServerSnake>();
		gamemap = new Map (map);
	}
	
	public void addBotSnake(BotSnake botsnake) {
		botsnakes.add(botsnake);
		gamemap.addBotSnake(botsnake);
	}
	
	public void addPlayerSnake(PlayerSnake playersnake) {
		playersnakes.add(playersnake);
		gamemap.addPlayerSnake(playersnake);
	}
	
	public void addServerSnake(ServerSnake serversnake) {
		serversnakes.add(serversnake);
		gamemap.addServerSnake(serversnake);
	}
	
	
	public int getNbSteps() {
		return nbSteps;
	}
	
	public ArrayList<BotSnake> getBotSnakes() {
		return botsnakes;
	}
	
	public ArrayList<PlayerSnake> getPlayerSnakes() {
		return playersnakes;
	}
	
	public ArrayList<ServerSnake> getServerSnakes() {
		return serversnakes;
	}
	
	public Map getGameMap() {
		return gamemap;
	}

	public boolean isTerminated() {	

		ArrayList<Boolean> mapBoxesStats = new ArrayList<Boolean>();
			for (int l=0; l<gamemap.getMapLength(); l++) {
				for(int w=0; w<gamemap.getMapWidth(); w++) {
					mapBoxesStats.add(gamemap.getBox(l,w).isCollied());
				}	
			}
		
		if(!mapBoxesStats.contains(true)) {
			if (serversnakes.isEmpty()) {
					if (!botsnakes.isEmpty() && !playersnakes.isEmpty()) {
						ArrayList<Boolean> BotSnakes_stats = new ArrayList<Boolean>();
						ArrayList<Boolean> PlayerSnakes_stats = new ArrayList<Boolean>();
						for(BotSnake i :  botsnakes) {
							BotSnakes_stats.add(i.getStat());
					}
						for(PlayerSnake j :  playersnakes) {	
							PlayerSnakes_stats.add(j.getStat());
					}
					if (BotSnakes_stats.contains(false) || PlayerSnakes_stats.contains(false)) {	
						return true;
					}
					else	{
						return false;
						}
					}		
					else if (!botsnakes.isEmpty() && playersnakes.isEmpty()) {			
						ArrayList<Boolean> BotSnakes_stats = new ArrayList<Boolean>();
						for(BotSnake i :  botsnakes) {
							BotSnakes_stats.add(i.getStat());
						}
						if (BotSnakes_stats.contains(false)) {			
							return true;			
						}
						else {
							return false;
						}
					}		
					else {			
						ArrayList<Boolean> PlayerSnakes_stats = new ArrayList<Boolean>();
						for(PlayerSnake i :  playersnakes) {
							PlayerSnakes_stats.add(i.getStat());
						}
						if (PlayerSnakes_stats.contains(false)) {			
							return true;			
						}
						else{
							return false;	
						}
					}
				}
			else if (! serversnakes.isEmpty()) {
						if (botsnakes.isEmpty() && playersnakes.isEmpty()) {
							return false;
						}
						else if(! botsnakes.isEmpty() && playersnakes.isEmpty()) {
							ArrayList<Boolean> BotSnakes_stats = new ArrayList<Boolean>();
							ArrayList<Boolean> ServerSnakes_stats = new ArrayList<Boolean>();			
							for(BotSnake i :  botsnakes) {
								BotSnakes_stats.add(i.getStat());
							}				
							for(ServerSnake j :  serversnakes) {	
								ServerSnakes_stats.add(j.getStat());
							}				
							if (BotSnakes_stats.contains(false) || ServerSnakes_stats.contains(false)) {	
								return true;		
							}
							else {
								return false;
							}
						}
	
						else if (botsnakes.isEmpty() && ! playersnakes.isEmpty()) {
							ArrayList<Boolean> PlayerSnakes_stats = new ArrayList<Boolean>();
							ArrayList<Boolean> ServerSnakes_stats = new ArrayList<Boolean>();			
							for(PlayerSnake i :  playersnakes) {
								PlayerSnakes_stats.add(i.getStat());
							}				
							for(ServerSnake j :  serversnakes) {	
								ServerSnakes_stats.add(j.getStat());
							}				
							if (PlayerSnakes_stats.contains(false) || ServerSnakes_stats.contains(false)) {	
								return true;		
							}
							else {
								return false;	
							}
						}			
						else {
							return false;
						}				
				}		
			else{
				return false;
			}			
			}
		else {
			return true;
		}
	
	}
				
				

	
	public void stop() {

	
		if(serversnakes.isEmpty()) {
		
		if (!botsnakes.isEmpty() && !playersnakes.isEmpty()) {
			for(int i = 0; i < botsnakes.size(); i++) {		
				for(int j = 0; j < playersnakes.size(); j++) {		
					int k = 1;
		
					if(botsnakes.get(i).getStat()==false && playersnakes.get(j).getStat()==false) {
						System.out.println("Game draws");

					}
				
					else if	(botsnakes.get(i).getStat()==true && playersnakes.get(j).getStat()==false) {
						if (!botsnakes.get(i).getHeadPos().isEqual(playersnakes.get(j).getHeadPos())) {
							System.out.println("Bot Snake " + k + " wins");
						}
						else {
							System.out.println("Game draws");
						}

					}
					else {
						if (!botsnakes.get(i).getHeadPos().isEqual(playersnakes.get(j).getHeadPos())) {
						System.out.println("Player Snake " + k + " wins");
						}
						else {
							System.out.println("Game draws");
						}

					}
				}
			}
		}
		else if (!botsnakes.isEmpty() && playersnakes.isEmpty()) {
			for(int i = 0; i < botsnakes.size(); i++) {		
					int j = 1;
					int k = 2;
					int l = 0;
					
		
					if(botsnakes.get(l).getStat()==false && botsnakes.get(j).getStat()==false) {
						System.out.println("Game draws");
						break;
					}
				
					else if	(botsnakes.get(l).getStat()==true && botsnakes.get(j).getStat()==false) {
						if (!botsnakes.get(l).getHeadPos().isEqual(botsnakes.get(j).getHeadPos())) {
							System.out.println("Bot Snake " + j + " wins");								
						}
						else {
							System.out.println("Game draws");
						}
					}
					else {
						if (!botsnakes.get(l).getHeadPos().isEqual(botsnakes.get(j).getHeadPos())) {
						System.out.println("Bot Snake " + k + " wins");
						}
						else {
							System.out.println("Game draws");
						}

					}
				}
			}
		else {
			for(int i = 0; i < playersnakes.size(); i++) {		
				int j = 1;
				int k = 2;
				int l = 0;
	
				if(playersnakes.get(i).getStat()==false && playersnakes.get(j).getStat()==false) {
					System.out.println("Game draws");

				}
			
				else if	(playersnakes.get(l).getStat()==true && playersnakes.get(j).getStat()==false) {
					if (!playersnakes.get(l).getHeadPos().isEqual(playersnakes.get(j).getHeadPos())) {
						System.out.println("Player Snake " + j + " wins");
					}
					else {
						System.out.println("Game draws");
					}

				}
				else {
					if (!playersnakes.get(l).getHeadPos().isEqual(playersnakes.get(j).getHeadPos())) {
					System.out.println("Player Snake " + k + " wins");
					}
					else {
						System.out.println("Game draws");
					}
				}
			}
		}
			
		}
		
		
		// with server
		else if (!serversnakes.isEmpty()){
			if (botsnakes.isEmpty() && playersnakes.isEmpty()) {
				System.out.println("No sufficient number of players");
				}
			else if(! botsnakes.isEmpty() && playersnakes.isEmpty()) {

				for(int i = 0; i < botsnakes.size(); i++) {		
					for(int j = 0; j < serversnakes.size(); j++) {		

						if(botsnakes.get(i).getStat()==false && serversnakes.get(j).getStat()==false) {
							System.out.println("Game draws");
							break;

						}
						else if	(botsnakes.get(i).getStat()==true && serversnakes.get(j).getStat()==false) {
							if (!botsnakes.get(i).getHeadPos().isEqual(serversnakes.get(j).getHeadPos())) {
								System.out.println("You win");
								break;
								}
							else {
								System.out.println("Game draws");
								break;
							}

							}
						else if (botsnakes.get(i).getStat()==false && serversnakes.get(j).getStat()==true){
							if (!botsnakes.get(i).getHeadPos().isEqual(serversnakes.get(j).getHeadPos())) {
								System.out.println("Opponent wins");
								break;
							}
							else {
								System.out.println("Game draws");
								break;
							}

						}
					}
				}
			}
			else if (botsnakes.isEmpty() && ! playersnakes.isEmpty()) {

				for(int i = 0; i < serversnakes.size(); i++) {		
					for(int j = 0; j < playersnakes.size(); j++) {		

			
						if(serversnakes.get(i).getStat()==false && playersnakes.get(j).getStat()==false) {
							System.out.println("Game draws");
							break;

						}
						else if	(serversnakes.get(i).getStat()==true && playersnakes.get(j).getStat()==false) {
							if (!serversnakes.get(i).getHeadPos().isEqual(playersnakes.get(j).getHeadPos())) {
								System.out.println("Opponent wins");
								break;
							}
							else {		
								System.out.println("Game draws");
								break;
							}
						}
						else {
							if (!serversnakes.get(i).getHeadPos().isEqual(playersnakes.get(j).getHeadPos())) {
							System.out.println("You wins");
							break;
							}
							else {
							System.out.println("Game draws");
							break;
							}

						}	
					}
				}
			}
			else {
				System.out.println("Errors found");
			}
		}
		
		else {
				System.out.println("Errors found");
			}				

	}

	}


