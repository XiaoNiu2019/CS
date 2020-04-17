package SnakeGame;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

public class SnakeGameMain extends Game{

	private static Controller controller;
	private static ArrayList <Node> allNodes;
	private static int colorset;
	private static int supersmart_inv;
	private static Color Snake1_HeadColor;
	private static Color Snake2_HeadColor;
	private static Color BodyColor1;
	private static Color BodyColor2;
	

	public SnakeGameMain() {
		Snake1_HeadColor = Color.RED;
		Snake2_HeadColor = Color.LIGHTGREEN;
		BodyColor1 = Color.CYAN;
		BodyColor2= Color.YELLOW;
	}

	
	
	public static void main(String[] args) throws IOException  { 
		int snake1_x = 4;
		int snake1_y = 4;
		int snake2_x = GRID_SIZE-4;
		int snake2_y = GRID_SIZE-4;	
		
		Map GameMap = new Map(Game.GRID_SIZE,Game.GRID_SIZE);
		System.out.println("Choose Game (Please enter: '1' for Local Game; '2' for Online Game");
		Scanner sc = new Scanner(System.in);
		String gamechoice = sc.nextLine();
		
		//Local Game
		if(gamechoice.compareTo("1")==0) {
		System.out.println("Choose Game type (Please enter: '1' for Player vs Computer; '2' for Computer vs Computer");
		String choicetype = sc.nextLine();
			//Local Player vs Bot
			if(choicetype.compareTo("1")==0) { 
				System.out.println("Choose BotSnake (Please enter: '1' for RandomBotSnake; '2' for SmartBotSnake; '3' for SuperSmartBotSnake");
				String choice1 = sc.nextLine();
				if (choice1.compareTo("1")==0) {
					BotSnake snake1 = new BotSnake_Random(snake1_x,snake1_y, Snake1_HeadColor);
					PlayerSnake snake2 = new LocalSnake(snake2_x,snake2_y , -1 ,Snake2_HeadColor);
					controller = new Controller(50, GameMap);
					controller.addBotSnake(snake1);
					controller.addPlayerSnake(snake2);
					allNodes = new ArrayList<Node>();
					allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
					allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
				}
				else if (choice1.compareTo("2")==0) {
					BotSnake snake1 = new BotSnake_Smart(snake1_x,snake1_y , Snake1_HeadColor);
					PlayerSnake snake2 = new LocalSnake(snake2_x,snake2_y , -1, Snake2_HeadColor);
					controller = new Controller(50, GameMap);
					controller.addBotSnake(snake1);
					controller.addPlayerSnake(snake2);
					allNodes = new ArrayList<Node>();
					allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
					allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
				}
				else if (choice1.compareTo("3")==0) {
					supersmart_inv=1;
					BotSnake snake1 = new BotSnake_SuperSmart(snake1_x,snake1_y, Snake1_HeadColor);
					PlayerSnake snake2 = new LocalSnake(snake2_x,snake2_y ,-1, Snake2_HeadColor);
					controller = new Controller(50, GameMap);
					controller.addBotSnake(snake1);
					controller.addPlayerSnake(snake2);
					allNodes = new ArrayList<Node>();
					allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
					allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));

				}
				else {
					System.out.println("Wrong input");
				}	
				
			}
			else if (choicetype.compareTo("2")==0) { //Local Bot vs Bot
				System.out.println("Choose Snake 1 (Please enter: '1' for RandomBotSnake; '2' for SmartBotSnake; '3' for SuperSmartBotSnake");
				String choice1 = sc.nextLine();
				if (choice1.compareTo("1")==0) {
					System.out.println("Choose Snake 2 (Please enter '1' for RandomBotSnake; '2' for SmartBotSnake; '3' for SuperSmartBotSnake");
					String choice2 = sc.nextLine();
					if(choice2.compareTo("1")==0) {
						BotSnake snake1 = new BotSnake_Random(snake1_x,snake1_y, Snake1_HeadColor);
						BotSnake snake2 = new BotSnake_Random(snake2_x,snake2_y , Snake2_HeadColor);
						controller = new Controller(50, GameMap);
						controller.addBotSnake(snake1);
						controller.addBotSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
					}
					else if(choice2.compareTo("2")==0) {
						BotSnake snake1 = new BotSnake_Random(snake1_x,snake1_y , Snake1_HeadColor);
						BotSnake snake2 = new BotSnake_Smart(snake2_x,snake2_y , Snake2_HeadColor);
						controller = new Controller(50, GameMap);
						controller.addBotSnake(snake1);
						controller.addBotSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
					}
					else if(choice2.compareTo("3")==0) {
						supersmart_inv = 1;
						BotSnake snake1 = new BotSnake_Random(snake1_x,snake1_y , Snake1_HeadColor);
						BotSnake snake2 = new BotSnake_SuperSmart(snake2_x,snake2_y , Snake2_HeadColor);
						controller = new Controller(50, GameMap);
						controller.addBotSnake(snake1);
						controller.addBotSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
					}
					else {
						System.out.println("Wrong input");
					}	
				}
		
				else if(choice1.compareTo("2")==0) {
					System.out.println("Choose Snake 2 (Please enter '1' for RandomBotSnake; '2' for SmartBotSnake; '3' for SuperSmartBotSnake");
					String choice2 = sc.nextLine();
					if(choice2.compareTo("1")==0) {
						BotSnake snake1 = new BotSnake_Smart(snake1_x,snake1_y , Snake1_HeadColor);
						BotSnake snake2 = new BotSnake_Random(snake2_x,snake2_y , Snake2_HeadColor);
						controller = new Controller(50, GameMap);
						controller.addBotSnake(snake1);
						controller.addBotSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
					}
					else if(choice2.compareTo("2")==0) {
						BotSnake snake1 = new BotSnake_Smart(snake1_x,snake1_y , Snake1_HeadColor);
						BotSnake snake2 = new BotSnake_Smart(snake2_x,snake2_y , Snake2_HeadColor);
						controller = new Controller(50, GameMap);
						controller.addBotSnake(snake1);
						controller.addBotSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
					}
					else if(choice2.compareTo("3")==0) {
						supersmart_inv = 1;
						BotSnake snake1 = new BotSnake_Smart(snake1_x,snake1_y , Snake1_HeadColor);
						BotSnake snake2 = new BotSnake_SuperSmart(snake2_x,snake2_y , Snake2_HeadColor);
						controller = new Controller(50, GameMap);
						controller.addBotSnake(snake1);
						controller.addBotSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
					}
					else {
						System.out.println("Wrong input");
					}
				}
		
				else if(choice1.compareTo("3")==0) {
					System.out.println("Choose Snake 2 (Please enter '1' for RandomBotSnake; '2' for SmartBotSnake; '3' for SuperSmartBotSnake)");
					String choice2 = sc.nextLine();
					if(choice2.compareTo("1")==0) {
						supersmart_inv = 1;
						BotSnake snake1 = new BotSnake_SuperSmart(snake1_x,snake1_y , Snake1_HeadColor);
						BotSnake snake2 = new BotSnake_Random(snake2_x,snake2_y , Snake2_HeadColor);
						controller = new Controller(50, GameMap);
						controller.addBotSnake(snake1);
						controller.addBotSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
					}
					else if(choice2.compareTo("2")==0) {
						supersmart_inv = 1;
						BotSnake snake1 = new BotSnake_SuperSmart(snake1_x,snake1_y ,Snake1_HeadColor);
						BotSnake snake2 = new BotSnake_Smart(snake2_x,snake2_y , Snake2_HeadColor);
						controller = new Controller(50, GameMap);
						controller.addBotSnake(snake1);
						controller.addBotSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
					}	
					else if(choice2.compareTo("3")==0) {
						System.out.println("Can not use 2 SmartSnake!");
/*						BotSnake snake1 = new BotSnake_SuperSmart(snake1_x,snake1_y ,Color.CYAN);
						BotSnake snake2 = new BotSnake_SuperSmart(snake2_x,snake2_y , Color.YELLOW);
						controller = new Controller(50, GameMap);
						controller.addBotSnake(snake1);
						controller.addBotSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));*/
					}
					else {
						System.out.println("Wrong input");
					}

				}	
		
				else {
					System.out.println("Wrong input");
				}
			}
			else {
				System.out.println("Wrong input");
			}
			
		}
		
	
		
		//Online Game
		else if(gamechoice.compareTo("2")==0) {
			
		System.out.println("Choose Role (Please enter '1' for Host Player; '2' for Client Player");	
		String choicetype = sc.nextLine();
				
					//Host Player
			if(choicetype.compareTo("1")==0) {	
				System.out.println("Choose your snake : (Please enter '1' for Player controlled; '2' for Random ; '3' for Smart ; '4' for SuperSmart");	
				String snaketype = sc.nextLine();

				
				if (snaketype.compareTo("1")==0) {
					colorset = 1;
						PlayerSnake snake1 = new LocalSnake(snake1_x,snake1_y , +1, Snake1_HeadColor);
						ServerSnake snake2 = new ServerSnake_Opponent(snake2_x,snake2_y , Snake2_HeadColor);	
						controller = new Controller(50, GameMap);
						controller.addPlayerSnake(snake1);
						controller.addServerSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
					
						int listenPort = 6789;
						System.out.println("Listening to port " + listenPort + " ...");
						ServerSocket listenSocket = new ServerSocket(listenPort);
						Socket socket = listenSocket.accept();
						System.out.println("Connected - Client Snake: " + socket.getLocalSocketAddress());	
			
			
						OutputStream outputStream = socket.getOutputStream();
						snake1.initializeDataOutputStream(outputStream);
			
						InputStream inputStream = socket.getInputStream();
						snake2.initializeBufferedReader(inputStream);
				}
				else if (snaketype.compareTo("2")==0) {
					colorset = 1;
						BotSnake snake1 = new BotSnake_Random(snake1_x,snake1_y , Snake1_HeadColor);
						ServerSnake snake2 = new ServerSnake_Opponent(snake2_x,snake2_y , Snake2_HeadColor);	
						controller = new Controller(50, GameMap);
						controller.addBotSnake(snake1);
						controller.addServerSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
				
						int listenPort = 6789;
						System.out.println("Listening to port " + listenPort + " ...");
						ServerSocket listenSocket = new ServerSocket(listenPort);
						Socket socket = listenSocket.accept();
						System.out.println("Connected - Client Snake: " + socket.getLocalSocketAddress());	
		
		
						OutputStream outputStream = socket.getOutputStream();
						snake1.initializeDataOutputStream(outputStream);
		
						InputStream inputStream = socket.getInputStream();
						snake2.initializeBufferedReader(inputStream);
					
				}
				else if (snaketype.compareTo("3")==0) {
					colorset = 1;
						BotSnake snake1 = new BotSnake_Smart(snake1_x,snake1_y , Snake1_HeadColor);
						ServerSnake snake2 = new ServerSnake_Opponent(snake2_x,snake2_y , Snake2_HeadColor);	
						controller = new Controller(50, GameMap);
						controller.addBotSnake(snake1);
						controller.addServerSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
			
						int listenPort = 6789;
						System.out.println("Listening to port " + listenPort + " ...");
						ServerSocket listenSocket = new ServerSocket(listenPort);
						Socket socket = listenSocket.accept();
						System.out.println("Connected - Client Snake: " + socket.getLocalSocketAddress());	
	
	
						OutputStream outputStream = socket.getOutputStream();
						snake1.initializeDataOutputStream(outputStream);
	
						InputStream inputStream = socket.getInputStream();
						snake2.initializeBufferedReader(inputStream);							
				}
				
				else if (snaketype.compareTo("4")==0) {
					colorset = 1;
					supersmart_inv =1;
						BotSnake snake1 = new BotSnake_SuperSmart(snake1_x,snake1_y , Snake1_HeadColor);
						ServerSnake snake2 = new ServerSnake_Opponent(snake2_x,snake2_y , Snake2_HeadColor);	
						controller = new Controller(50, GameMap);
						controller.addBotSnake(snake1);
						controller.addServerSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
			
						int listenPort = 6789;
						System.out.println("Listening to port " + listenPort + " ...");
						ServerSocket listenSocket = new ServerSocket(listenPort);
						Socket socket = listenSocket.accept();
						System.out.println("Connected - Client Snake: " + socket.getLocalSocketAddress());	
	
	
						OutputStream outputStream = socket.getOutputStream();
						snake1.initializeDataOutputStream(outputStream);
	
						InputStream inputStream = socket.getInputStream();
						snake2.initializeBufferedReader(inputStream);
					
				}
				
				else {
					System.out.println("Wrong input");	
				}
				
			}
				
					//Client Player
			else if(choicetype.compareTo("2")==0) {
				System.out.println("Choose your snake : (Please enter '1' for Player controlled; '2' for Random ; '3' for Smart ; '4' for SuperSmart");	
				String snaketype = sc.nextLine();
				
				if (snaketype.compareTo("1")==0) {
					colorset = 2;
						PlayerSnake snake1 = new LocalSnake(snake2_x,snake2_y , -1, Snake2_HeadColor);
						ServerSnake snake2 = new ServerSnake_Opponent(snake1_x,snake1_y , Snake1_HeadColor);	
						controller = new Controller(50, GameMap);
						controller.addPlayerSnake(snake1);
						controller.addServerSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
						
						int Port = 6789;
						System.out.println("Listening to port " + Port  + " ...");
						String machine = "127.0.0.1";
						Socket socket = new Socket(machine, Port);
						System.out.println("Connected - Client Snake: " + socket.getLocalSocketAddress());	
								
						OutputStream outputStream = socket.getOutputStream();
						snake1.initializeDataOutputStream(outputStream);
				
						InputStream inputStream = socket.getInputStream();
						snake2.initializeBufferedReader(inputStream);
				}
				else if (snaketype.compareTo("2")==0) {
					colorset = 2;
						BotSnake snake1 = new BotSnake_Random(snake2_x,snake2_y , Snake2_HeadColor);
						ServerSnake snake2 = new ServerSnake_Opponent(snake1_x,snake1_y ,Snake1_HeadColor);	
						controller = new Controller(50, GameMap);
						controller.addBotSnake(snake1);
						controller.addServerSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
					
						int Port = 6789;
						System.out.println("Listening to port " + Port  + " ...");
						String machine = "127.0.0.1";
						Socket socket = new Socket(machine, Port);
						System.out.println("Connected - Client Snake: " + socket.getLocalSocketAddress());	
							
						OutputStream outputStream = socket.getOutputStream();
						snake1.initializeDataOutputStream(outputStream);
			
						InputStream inputStream = socket.getInputStream();
						snake2.initializeBufferedReader(inputStream);	
				
					
				}
				else if (snaketype.compareTo("3")==0) {
					colorset = 2;
						BotSnake snake1 = new BotSnake_Smart(snake2_x,snake2_y , Snake2_HeadColor);
						ServerSnake snake2 = new ServerSnake_Opponent(snake1_x,snake1_y ,Snake1_HeadColor);	
						controller = new Controller(50, GameMap);
						controller.addBotSnake(snake1);
						controller.addServerSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
					
						int Port = 6789;
						System.out.println("Listening to port " + Port  + " ...");
						String machine = "127.0.0.1";
						Socket socket = new Socket(machine, Port);
						System.out.println("Connected - Client Snake: " + socket.getLocalSocketAddress());	
							
						OutputStream outputStream = socket.getOutputStream();
						snake1.initializeDataOutputStream(outputStream);
			
						InputStream inputStream = socket.getInputStream();
						snake2.initializeBufferedReader(inputStream);
					
				}
				else if (snaketype.compareTo("4")==0) {
					colorset = 2;
					supersmart_inv = 1;
						BotSnake snake1 = new BotSnake_SuperSmart(snake2_x,snake2_y , Snake2_HeadColor);
						ServerSnake snake2 = new ServerSnake_Opponent(snake1_x,snake1_y , Snake1_HeadColor);	
						controller = new Controller(50, GameMap);
						controller.addBotSnake(snake1);
						controller.addServerSnake(snake2);
						allNodes = new ArrayList<Node>();
						allNodes.addAll(new ArrayList<Node>(snake1.getAllNodes()));
						allNodes.addAll(new ArrayList<Node>(snake2.getAllNodes()));
				
						int Port = 6789;
						System.out.println("Listening to port " + Port  + " ...");
						String machine = "127.0.0.1";
						Socket socket = new Socket(machine, Port);
						System.out.println("Connected - Client Snake: " + socket.getLocalSocketAddress());	
						
						OutputStream outputStream = socket.getOutputStream();
						snake1.initializeDataOutputStream(outputStream);
		
						InputStream inputStream = socket.getInputStream();
						snake2.initializeBufferedReader(inputStream);
					
				}
				else {
					System.out.println("Wrong input");						
				}

			}
			
			
			else {
				System.out.println("Wrong input");	
			}
				
				
			}
			
				else {
						System.out.println("Wrong input");
				}
		Application.launch(args);

	}



	@Override
	public Collection<Node> gameStep() {

		
		if(controller.getServerSnakes().isEmpty()) {

					if(!controller.getBotSnakes().isEmpty() && !controller.getPlayerSnakes().isEmpty()) {

						if (supersmart_inv != 1) {

							if(!controller.isTerminated()==true) {	
								for (BotSnake i :  controller.getBotSnakes()) {

									for(PlayerSnake j : controller.getPlayerSnakes()) {	

							
										i.moveInMap(controller.getGameMap());
										j.moveInMap(controller.getGameMap());	
										allNodes.remove(allNodes.size()-1);
										allNodes.remove(allNodes.size()-1);
										allNodes.add(new BotSnakeNode(i.getAllPos().get(i.getLength()-2).getX(),i.getAllPos().get(i.getLength()-2).getY(), BodyColor1 ));
										allNodes.add(new PlayerSnakeNode(j.getAllPos().get(j.getLength()-2).getX(),j.getAllPos().get(j.getLength()-2).getY(), BodyColor2 ));
										allNodes.add(new BotSnakeNode(i.getHeadX(),i.getHeadY(), Snake1_HeadColor));
										allNodes.add(new PlayerSnakeNode(j.getHeadX(),j.getHeadY(), Snake2_HeadColor));


									}
								}			
							}
							else {
								controller.stop();
							}
						}
						else {
							if(!controller.isTerminated()==true) {	
								for (BotSnake i :  controller.getBotSnakes()) {
									for(PlayerSnake j : controller.getPlayerSnakes()) {	

										j.moveInMap(controller.getGameMap());	
										i.moveInMap(controller.getGameMap(),j);
										allNodes.remove(allNodes.size()-1);
										allNodes.remove(allNodes.size()-1);
										allNodes.add(new BotSnakeNode(i.getAllPos().get(i.getLength()-2).getX(),i.getAllPos().get(i.getLength()-2).getY(), BodyColor1 ));
										allNodes.add(new PlayerSnakeNode(j.getAllPos().get(j.getLength()-2).getX(),j.getAllPos().get(j.getLength()-2).getY(), BodyColor2 ));
										allNodes.add(new BotSnakeNode(i.getHeadX(),i.getHeadY(), Snake1_HeadColor));
										allNodes.add(new PlayerSnakeNode(j.getHeadX(),j.getHeadY(), Snake2_HeadColor));

									}
								}			
							}
							else {
								controller.stop();
							}
						
							
						}
					}
					else if (!controller.getBotSnakes().isEmpty() && controller.getPlayerSnakes().isEmpty()) {
						if (supersmart_inv != 1) {
							if(!controller.isTerminated()==true) {	
								BotSnake i = controller.getBotSnakes().get(0);
								BotSnake j = controller.getBotSnakes().get(1);
								i.moveInMap(controller.getGameMap());
								j.moveInMap(controller.getGameMap());
								allNodes.remove(allNodes.size()-1);
								allNodes.remove(allNodes.size()-1);
								allNodes.add(new BotSnakeNode(i.getAllPos().get(i.getLength()-2).getX(),i.getAllPos().get(i.getLength()-2).getY(), BodyColor1 ));
								allNodes.add(new BotSnakeNode(j.getAllPos().get(j.getLength()-2).getX(),j.getAllPos().get(j.getLength()-2).getY(), BodyColor2 ));
								allNodes.add(new BotSnakeNode(i.getHeadX(),i.getHeadY(), Snake1_HeadColor));
								allNodes.add(new BotSnakeNode(j.getHeadX(),j.getHeadY(), Snake2_HeadColor));

							}
							else {
								controller.stop();
							}
						}
						else {

							if(!controller.isTerminated()==true) {	
								BotSnake i = controller.getBotSnakes().get(0);
								BotSnake j = controller.getBotSnakes().get(1);
								i.moveInMap(controller.getGameMap());
								j.moveInMap(controller.getGameMap(),i);
								allNodes.remove(allNodes.size()-1);
								allNodes.remove(allNodes.size()-1);
								allNodes.add(new BotSnakeNode(i.getAllPos().get(i.getLength()-2).getX(),i.getAllPos().get(i.getLength()-2).getY(), BodyColor1 ));
								allNodes.add(new BotSnakeNode(j.getAllPos().get(j.getLength()-2).getX(),j.getAllPos().get(j.getLength()-2).getY(), BodyColor2 ));
								allNodes.add(new BotSnakeNode(i.getHeadX(),i.getHeadY(), Snake1_HeadColor));
								allNodes.add(new BotSnakeNode(j.getHeadX(),j.getHeadY(), Snake2_HeadColor));

							}
							else {
								controller.stop();
							}
						
							
						}
					}
					else {
							if(!controller.isTerminated()==true) {						
								PlayerSnake i = controller.getPlayerSnakes().get(0);
								PlayerSnake j = controller.getPlayerSnakes().get(1);
								i.moveInMap(controller.getGameMap());
								j.moveInMap(controller.getGameMap());
								allNodes.remove(allNodes.size()-1);
								allNodes.remove(allNodes.size()-1);
								allNodes.add(new BotSnakeNode(i.getAllPos().get(i.getLength()-2).getX(),i.getAllPos().get(i.getLength()-2).getY(), BodyColor1 ));
								allNodes.add(new BotSnakeNode(j.getAllPos().get(j.getLength()-2).getX(),j.getAllPos().get(j.getLength()-2).getY(), BodyColor2 ));
								allNodes.add(new BotSnakeNode(i.getHeadX(),i.getHeadY(), Snake1_HeadColor));
								allNodes.add(new BotSnakeNode(j.getHeadX(),j.getHeadY(), Snake2_HeadColor));
							}
							else {
								controller.stop();
							}	

					}	
				}
		else 		{

				if(!controller.getBotSnakes().isEmpty() && controller.getPlayerSnakes().isEmpty()) {
						if(!controller.isTerminated()==true) {	
							
							if(colorset == 1) {
								if (supersmart_inv != 1) {
								for (BotSnake i :  controller.getBotSnakes()) {
									for(ServerSnake j : controller.getServerSnakes()) {	
										i.moveInMap(controller.getGameMap());
										j.moveInMap(controller.getGameMap());	
										allNodes.remove(allNodes.size()-1);
										allNodes.remove(allNodes.size()-1);
										allNodes.add(new BotSnakeNode(i.getAllPos().get(i.getLength()-2).getX(),i.getAllPos().get(i.getLength()-2).getY(), BodyColor2 ));
										allNodes.add(new ServerSnakeNode(j.getAllPos().get(j.getLength()-2).getX(),j.getAllPos().get(j.getLength()-2).getY(), BodyColor1 ));
										allNodes.add(new BotSnakeNode(i.getHeadX(),i.getHeadY(), Snake2_HeadColor));
										allNodes.add(new ServerSnakeNode(j.getHeadX(),j.getHeadY(), Snake1_HeadColor));

									}
							
								}
								}
								else {

									for (BotSnake i :  controller.getBotSnakes()) {
										for(ServerSnake j : controller.getServerSnakes()) {	
											j.moveInMap(controller.getGameMap());	
											i.moveInMap(controller.getGameMap(),j);
											allNodes.remove(allNodes.size()-1);
											allNodes.remove(allNodes.size()-1);
											allNodes.add(new BotSnakeNode(i.getAllPos().get(i.getLength()-2).getX(),i.getAllPos().get(i.getLength()-2).getY(), BodyColor2 ));
											allNodes.add(new ServerSnakeNode(j.getAllPos().get(j.getLength()-2).getX(),j.getAllPos().get(j.getLength()-2).getY(), BodyColor1 ));
											allNodes.add(new BotSnakeNode(i.getHeadX(),i.getHeadY(), Snake2_HeadColor));
											allNodes.add(new ServerSnakeNode(j.getHeadX(),j.getHeadY(), Snake1_HeadColor));
										}
								
									}
									
									
								}
							}
							else {
							if (supersmart_inv != 1) {
								for (BotSnake i :  controller.getBotSnakes()) {
									for(ServerSnake j : controller.getServerSnakes()) {	
										i.moveInMap(controller.getGameMap());
										j.moveInMap(controller.getGameMap());															
										allNodes.remove(allNodes.size()-1);
										allNodes.remove(allNodes.size()-1);
										allNodes.add(new BotSnakeNode(i.getAllPos().get(i.getLength()-2).getX(),i.getAllPos().get(i.getLength()-2).getY(), BodyColor1 ));
										allNodes.add(new ServerSnakeNode(j.getAllPos().get(j.getLength()-2).getX(),j.getAllPos().get(j.getLength()-2).getY(), BodyColor2 ));
										allNodes.add(new BotSnakeNode(i.getHeadX(),i.getHeadY(), Snake1_HeadColor));
										allNodes.add(new ServerSnakeNode(j.getHeadX(),j.getHeadY(), Snake2_HeadColor));

									}
								}						
							}
							else {
								for (BotSnake i :  controller.getBotSnakes()) {
									for(ServerSnake j : controller.getServerSnakes()) {	
										j.moveInMap(controller.getGameMap());	
										i.moveInMap(controller.getGameMap(),j);						
										allNodes.remove(allNodes.size()-1);
										allNodes.remove(allNodes.size()-1);
										allNodes.add(new BotSnakeNode(i.getAllPos().get(i.getLength()-2).getX(),i.getAllPos().get(i.getLength()-2).getY(), BodyColor1 ));
										allNodes.add(new ServerSnakeNode(j.getAllPos().get(j.getLength()-2).getX(),j.getAllPos().get(j.getLength()-2).getY(), BodyColor2 ));
										allNodes.add(new BotSnakeNode(i.getHeadX(),i.getHeadY(), Snake1_HeadColor));
										allNodes.add(new ServerSnakeNode(j.getHeadX(),j.getHeadY(), Snake2_HeadColor));

									}
								}						
							
								
							}
							}
						}
						else {
							controller.stop();
						}

					}
			else if (controller.getBotSnakes().isEmpty() && !controller.getPlayerSnakes().isEmpty()) {

				if(!controller.isTerminated()==true) {	
					if(colorset == 1) {
						for (PlayerSnake i :  controller.getPlayerSnakes()) {
							for(ServerSnake j : controller.getServerSnakes()) {	

								i.moveInMap(controller.getGameMap());
								j.moveInMap(controller.getGameMap());	
								allNodes.remove(allNodes.size()-1);
								allNodes.remove(allNodes.size()-1);
								allNodes.add(new PlayerSnakeNode(i.getAllPos().get(i.getLength()-2).getX(),i.getAllPos().get(i.getLength()-2).getY(), BodyColor2 ));
								allNodes.add(new ServerSnakeNode(j.getAllPos().get(j.getLength()-2).getX(),j.getAllPos().get(j.getLength()-2).getY(), BodyColor1 ));
								allNodes.add(new PlayerSnakeNode(i.getHeadX(),i.getHeadY(), Snake2_HeadColor));
								allNodes.add(new ServerSnakeNode(j.getHeadX(),j.getHeadY(), Snake1_HeadColor));

							}
						}

					}
					else {
						for (PlayerSnake i :  controller.getPlayerSnakes()) {
							for(ServerSnake j : controller.getServerSnakes()) {	

								i.moveInMap(controller.getGameMap());
								j.moveInMap(controller.getGameMap());	
								allNodes.remove(allNodes.size()-1);
								allNodes.remove(allNodes.size()-1);
								allNodes.add(new PlayerSnakeNode(i.getAllPos().get(i.getLength()-2).getX(),i.getAllPos().get(i.getLength()-2).getY(), BodyColor1 ));
								allNodes.add(new ServerSnakeNode(j.getAllPos().get(j.getLength()-2).getX(),j.getAllPos().get(j.getLength()-2).getY(), BodyColor2 ));
								allNodes.add(new PlayerSnakeNode(i.getHeadX(),i.getHeadY(), Snake1_HeadColor));
								allNodes.add(new ServerSnakeNode(j.getHeadX(),j.getHeadY(), Snake2_HeadColor));

							}
						}

						
					}
				}
				else {
					controller.stop();
				}
				
			
								
			}
			
		}
		return allNodes;
	
	}
}
