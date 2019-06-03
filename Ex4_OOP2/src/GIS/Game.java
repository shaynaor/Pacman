
package GIS;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import Geom.Point3D;
import Gui.Map;

/**
 * This class represents a Game. A game is an object with five ArrayList
 * associated with it. one ArrayList for fruits, one for pacmans, one for ghosts,
 * one for boxes , one for players, one for corners.
 *  This class will receive a list of board data. it will read the list and add 
 *  each element to the correct list.
 *   
 * @author Shay Naor , Alex VAisman
 *
 */
public class Game {
	private Vector<Fruit> fruits;// contains all the Fruits.
	private ArrayList<Pacman> pacmans;// contains all the Pacmans.
	private ArrayList<Ghost> ghosts;
	private ArrayList<Box> boxes;
	private ArrayList<Player> players;
	private ArrayList<Corner> corners;
	private Map map;

    
	/**
	 * This function receives a ArrayList<String> of data and adds
	 * each object to the correct list.
	 * @param board_data , the array list with the daya
	 * @param bounds, the bounds of the map
	 */
	public Game(ArrayList<String> board_data, String bounds) {
		this.fruits = new Vector<Fruit>();
		this.pacmans = new ArrayList<Pacman>();
		this.ghosts = new ArrayList<Ghost>();
		this.boxes = new ArrayList<Box>();
		this.players = new ArrayList<Player>();
		this.corners = new ArrayList<Corner>();
		this.map = new Map(bounds);

		String line[] = {};

		/*
		 * Goes throw the gameData and adds it to the correct array list.
		 */
		for (int i = 0; i < board_data.size(); i++) {

			line = board_data.get(i).split(",");

			if (line[0].contains("M")) {

				try {
					Player player = new Player(line);
					players.add(player);

				} catch (ParseException e) {
					System.err.println("The csv file is corrupted");
					System.err.println("Make sure each line in cvs file writen in the following format");
					System.err.println(
							"type:string/char, id:int/double, lat:double, lon:double, lat:double, speed:int/double, radius:int/double ");
					e.printStackTrace();
				}

			}

			if (line[0].contains("P")) {

				try {
					Pacman pac = new Pacman(line);
					this.pacmans.add(pac);

				} catch (ParseException e) {
					System.err.println("The csv file is corrupted");
					System.err.println("Make sure each line in cvs file writen in the following format");
					System.err.println(
							"type:string/char, id:int/double, lat:double, lon:double, lat:double, speed:int/double, radius:int/double ");
					e.printStackTrace();
				}

			}
			if (line[0].contains("F")) {
				try {
					Fruit fruit = new Fruit(line);
					this.fruits.add(fruit);

				} catch (ParseException e) {
					System.err.println("The csv file is corrupted");
					System.err.println("Make sure each line in cvs file writen in the following format");
					System.err.println(
							"type:string/char, id:int/double, lat:double, lon:double, lat:double, speed:int/double, radius:int/double ");
					e.printStackTrace();
				}
			}

			if (line[0].contains("G")) {
				try {
					Ghost ghost = new Ghost(line);
					this.ghosts.add(ghost);

				} catch (ParseException e) {
					System.err.println("The csv file is corrupted");
					System.err.println("Make sure each line in cvs file writen in the following format");
					System.err.println(
							"type:string/char, id:int/double, lat:double, lon:double, lat:double, speed:int/double, radius:int/double ");
					e.printStackTrace();
				}
			}

			if (line[0].contains("B")) {
				try {
					Box box = new Box(line);
					this.boxes.add(box);

				} catch (ParseException e) {
					System.err.println("The csv file is corrupted");
					System.err.println("Make sure each line in cvs file writen in the following format");
					System.err.println(
							"type:string/char, id:int/double, lat:double, lon:double, lat:double, speed:int/double, radius:int/double ");
					e.printStackTrace();
				}
			}

		}
		CheckIfCornerInBox();

	}

	/**
	 * This function checks if a corner is not inside a box.
	 * if its not in a box it will add it to the arraylist of corners.
	 */
	private void CheckIfCornerInBox() {
		Iterator<Box> boxIt = this.boxes.iterator();
		boolean flag = true;
		
		/* for all box points check if point is in a box */
		while(boxIt.hasNext()) {
			Box box = boxIt.next();
			for(int i = 0 ; i<4;i++) {
				Point3D vpoint = box.getVBox().getVboxCorners().get(i);
				Iterator<Box> boxIt2 = this.boxes.iterator();
              
				/* this checks for all boxes if corner is in */
				while(boxIt2.hasNext()) {
					Box box2 = boxIt2.next();
					if(box2.isIn(vpoint)) {
						flag = false;
					}
				}
				/* if corner is not in add it to arraylist of corners */
				if(flag==true) {
					Corner corner = new Corner (vpoint);
					this.corners.add(corner);
				}
				flag = true;
			}
		}
	}
     
	/**
	 * This function updates the game with the new board data.
	 * it will clear all relevant lists and add the new elements to them.
	 * @param board_data
	 */
	public void updateTheGame(ArrayList<String> board_data) {
		double ori = players.get(0).getOrientation();
		this.fruits.clear();
		this.pacmans.clear();
		this.ghosts.clear();
		this.players.clear();
		this.boxes.clear();

		String line[] = {};

		/*
		 * Goes throw the gameData and adds it to the correct array list.
		 */
		for (int i = 0; i < board_data.size(); i++) {

			line = board_data.get(i).split(",");

			if (line[0].contains("M")) {

				try {
					Player player = new Player(line);
					player.setOrientation(ori);
					players.add(player);

				} catch (ParseException e) {
					System.err.println("The csv file is corrupted");
					System.err.println("Make sure each line in cvs file writen in the following format");
					System.err.println(
							"type:string/char, id:int/double, lat:double, lon:double, lat:double, speed:int/double, radius:int/double ");
					e.printStackTrace();
				}

			}

			if (line[0].contains("P")) {

				try {
					Pacman pac = new Pacman(line);
					this.pacmans.add(pac);

				} catch (ParseException e) {
					System.err.println("The csv file is corrupted");
					System.err.println("Make sure each line in cvs file writen in the following format");
					System.err.println(
							"type:string/char, id:int/double, lat:double, lon:double, lat:double, speed:int/double, radius:int/double ");
					e.printStackTrace();
				}

			}
			if (line[0].contains("F")) {
				try {
					Fruit fruit = new Fruit(line);
					this.fruits.add(fruit);

				} catch (ParseException e) {
					System.err.println("The csv file is corrupted");
					System.err.println("Make sure each line in cvs file writen in the following format");
					System.err.println(
							"type:string/char, id:int/double, lat:double, lon:double, lat:double, speed:int/double, radius:int/double ");
					e.printStackTrace();
				}
			}

			if (line[0].contains("G")) {
				try {
					Ghost ghost = new Ghost(line);
					this.ghosts.add(ghost);

				} catch (ParseException e) {
					System.err.println("The csv file is corrupted");
					System.err.println("Make sure each line in cvs file writen in the following format");
					System.err.println(
							"type:string/char, id:int/double, lat:double, lon:double, lat:double, speed:int/double, radius:int/double ");
					e.printStackTrace();
				}
			}

			if (line[0].contains("B")) {
				try {
					Box box = new Box(line);
					this.boxes.add(box);

				} catch (ParseException e) {
					System.err.println("The csv file is corrupted");
					System.err.println("Make sure each line in cvs file writen in the following format");
					System.err.println(
							"type:string/char, id:int/double, lat:double, lon:double, lat:double, speed:int/double, radius:int/double ");
					e.printStackTrace();
				}
			}

		}

	}
  
	/**
	 * This function clears the game.
	 */
	public void clearGame() {
		this.fruits.clear();
		this.pacmans.clear();
		this.ghosts.clear();
		this.players.clear();
		this.corners.clear();
		this.boxes.clear();
	}

	/* Getters */

	public void Vector(Vector<Fruit> fruits) {
		this.fruits = fruits;
	}

	public void setPacmans(ArrayList<Pacman> pacmans) {
		this.pacmans = pacmans;
	}

	public Vector<Fruit> getFruits() {
		return fruits;
	}

	public ArrayList<Pacman> getPacmans() {
		return pacmans;
	}

	public ArrayList<Ghost> getGhosts() {
		return ghosts;
	}

	public ArrayList<Box> getBoxes() {
		return boxes;
	}

	public Map getMap() {
		return map;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public ArrayList<Corner> getCorners() {
		return corners;
	}

}
