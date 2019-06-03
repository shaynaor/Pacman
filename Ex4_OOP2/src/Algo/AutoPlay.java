package Algo;

import java.util.ArrayList;
import java.util.Iterator;

import Coords.MyCoords;
import GIS.Box;
import GIS.Corner;
import GIS.Fruit;
import GIS.Game;
import GIS.Pacman;
import GIS.Player;
import Geom.Point3D;
import Robot.Play;
import graph.Graph;

/**
 * This class is the algorithm of the program that decided what the player will
 * do next. The algorithm will find the starting spot for a player depending on
 * how much fruits are in a certain area. after that the player will always go
 * to the closest fruit to him or a pacman if he gets into a certain range.
 * 
 * @author Alex vaisman , Shay naor
 *
 */
public class AutoPlay {
	private Game game;
	private Play play1;
	private Graph G;
	private Path path;

	private int LastId[];
	private int counter;
	private int count;
	private boolean manover;

	public AutoPlay(Game game, Play play1) {
		this.game = game;
		this.play1 = play1;
		this.G = new Graph();
		this.LastId = new int[2];
		this.counter = 0;
		this.manover = false;
		this.count = 0;

		startAlgo();
	}

	/**
	 * This function receives a game and player and updates the values.
	 * 
	 * @param game
	 * @param play1
	 */
	public void UpdateAlgo(Game game, Play play1) {
		this.game = game;
		this.play1 = play1;

		startAlgo();
	}

	/**
	 * This function is the main function of the algorithm. if the game just started
	 * it will call the function that finds the starting spot , and what each corner
	 * sees . else it will just update what each corner sees. at the end of the
	 * function it will call FindClosesFood, which will find what the pacman needs
	 * to eat next and update him.
	 * 
	 */
	private void startAlgo() {
		String playerData = play1.getStatistics();
		String line[] = {};
		line = playerData.split(",");

		/*
		 * If this is the first time the algorithm is called we need to find where to
		 * place the player and what corners see.
		 */
		if (line[3].contains("Time left:0.0")) { // line[3].contains("Time left:100000.
			Point3D start = findCluster();
			play1.setInitLocation(start.x(), start.y());
			ArrayList<String> board_data = play1.getBoard();
			game.updateTheGame(board_data);
			FindWhatCornersCornerSees();
			FindWhatFruitsCornerSees();
		}
		FindWhatCornersPlayerSees();
		FindClosestFood();
	}

	/**
	 * This function finds for each corner what fruits in the game it sees.
	 */
	private void FindWhatFruitsCornerSees() {
		Iterator<Corner> cornerIt = this.game.getCorners().iterator();

		while (cornerIt.hasNext()) {
			Corner corn = cornerIt.next();
			Iterator<Fruit> fruitIt = this.game.getFruits().iterator();
			while (fruitIt.hasNext()) {
				Fruit fruit = fruitIt.next();
				Segment way = new Segment(corn.getGps(), fruit.getGps());
				boolean iSee = CheckIfNotBlocked(way);
				if (iSee == true) {
					corn.getWhatFruitIsee().add(fruit);
				}
			}
		}
	}

	/**
	 * This function finds what corners on the map the player sees.
	 */
	private void FindWhatCornersPlayerSees() {
		Player player = this.game.getPlayers().get(0);
		Iterator<Corner> cornerIt = this.game.getCorners().iterator();

		while (cornerIt.hasNext()) {
			Corner corn = cornerIt.next();
			Segment way = new Segment(player.getGps(), corn.getGps());
			boolean iSee = CheckIfNotBlocked(way);
			if (iSee == true) {
				this.game.getPlayers().get(0).getWhatISee().add(corn);
			}
		}
	}

	/**
	 * This function finds for each corner , what corners this corner sees.
	 */
	private void FindWhatCornersCornerSees() {
		Iterator<Corner> cornerIt = this.game.getCorners().iterator();

		while (cornerIt.hasNext()) {
			Corner corn = cornerIt.next();
			Iterator<Corner> cornerIt2 = this.game.getCorners().iterator();
			while (cornerIt2.hasNext()) {
				Corner corn2 = cornerIt2.next();

				if (corn.getGps().x() != corn2.getGps().x() || corn.getGps().y() != corn2.getGps().y()) {
					Segment way = new Segment(corn.getGps(), corn2.getGps());
					boolean iSee = CheckIfNotBlocked(way);
					if (iSee == true) {
						corn.getWhatISee().add(corn2); 
					}
				}
			}
		}

	}

	/**
	 * This function finds the closest pacman or fruit to the player, regardless to
	 * if its visible or not. the depending on who is closest to the player.
	 * it will find the orientation to that object and update the player orientation.
	 */
	private void FindClosestFood() {

		Iterator<Fruit> fruitIt = this.game.getFruits().iterator();
		double minVisibleFruit = 1000000;
		double minNotVisibleFruit = 1000000;
		MyCoords convert = new MyCoords();
		Fruit fruitmin = new Fruit(0, 0, 0);
		Fruit fruitminNotVisibale = new Fruit(0, 0, 0);

		/* finds the nearest fruit that is visible */
		while (fruitIt.hasNext()) {
			Fruit fruit = fruitIt.next();
			Segment way = new Segment(this.game.getPlayers().get(0).getGps(), fruit.getGps());
			boolean notBlocked = CheckIfNotBlocked(way);
			double distanceAir = convert.distance3d(this.game.getPlayers().get(0).getGps(), fruit.getGps());

			if (distanceAir < minVisibleFruit && notBlocked) {
				minVisibleFruit = distanceAir;
				fruitmin = fruit;
			} 
			/* Find the nearest fruit thats not visible */
			else if (distanceAir < minVisibleFruit && !notBlocked) {

				BuildGraph g = new BuildGraph(this.G, this.game.getPlayers().get(0), this.game.getCorners(), fruit);
				this.path = new Path(g.getAns());
				double distanceNotVisibale = path.getDistance();

				if (distanceNotVisibale < minNotVisibleFruit) {
					minNotVisibleFruit = distanceNotVisibale;
					fruitminNotVisibale = fruit;
				}

			}
		}
		/* finds the nearest pacman that is visible */
		double minVisibalePacman = 1000000;
		Iterator<Pacman> pacIt = game.getPacmans().iterator();
		Pacman pacMin = new Pacman(0, 0, 0);
		while (pacIt.hasNext()) {
			Pacman pac = pacIt.next();
			Segment way = new Segment(this.game.getPlayers().get(0).getGps(), pac.getGps());
			boolean notBlocked = CheckIfNotBlocked(way);
			double distance = convert.distance3d(this.game.getPlayers().get(0).getGps(), pac.getGps());
			if (distance < minVisibalePacman && notBlocked) {
				minVisibalePacman = distance;
				pacMin = pac;
			}
		}
        /* finds which of the 3 objects are closest to the player */
		int target = FindMinDistance(minVisibleFruit, minNotVisibleFruit, minVisibalePacman);
		// visible fruit
		if (target == 1) {
			this.game.getPlayers().get(0).findOrientation(fruitmin.getGps());
		}
		// not visible
		if (target == 2 || this.manover == true) {
			if (this.manover == false) {
				int cornId = this.path.getTheWay().get(0);
				Point3D corn = this.game.getCorners().get(cornId).getGps();
				this.game.getPlayers().get(0).findOrientation(corn);
				this.LastId[this.counter % 2] = cornId;
				this.counter++;
			}

			if (this.LastId[0] == 16 && this.LastId[1] == 17 || this.LastId[1] == 16 && this.LastId[0] == 17) {

				this.manover = true;
				if (count < 25) {
					if (count < 1) {
						this.game.getPlayers().get(0).setOrientation(270.0);
						play1.rotate(game.getPlayers().get(0).getOrientation());
					}
					if (count >= 3 && count < 10) {
						this.game.getPlayers().get(0).setOrientation(0.0);
						play1.rotate(game.getPlayers().get(0).getOrientation());
					}
					if (count >= 10) {
						this.game.getPlayers().get(0).setOrientation(85.0);
						play1.rotate(game.getPlayers().get(0).getOrientation());
					}
					count++;
					try {
						Thread.sleep(65);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (count >= 25) {
						this.manover = false;
					}
				}
			}
		}
		// pacman
		if (target == 3) {
			this.game.getPlayers().get(0).findOrientation(pacMin.getGps());
		}

	}

	/**
	 * This function returns the minimum distance between the 3.
	 * 
	 * @param minVisibleFruit    ,distance to the visible fruit
	 * @param minNotVisibleFruit ,distance to the not visible fruit
	 * @param minVisibalePacman  , distance to the visible pacman
	 * @return , return 1 if the visible fruit. returns 2 if the not visible fruit.
	 *         return 3 if the visible pacman.
	 */
	private int FindMinDistance(double minVisibleFruit, double minNotVisibleFruit, double minVisibalePacman) {
		double min = Math.min(minVisibleFruit, Math.min(minNotVisibleFruit, minVisibalePacman));

		if (min == minVisibleFruit) {
			return 1;
		}
		if (min == minNotVisibleFruit) {
			return 2;
		}
		if (min == minVisibalePacman) {
			return 3;
		}

		return 17;
	}

	/**
	 * This function checks for a segment if its blocked by any box segment
	 * in the game.
	 * @param way , the segment we want to check if its blocked.
	 * @return , returns true if not blocked.
	 */
	private boolean CheckIfNotBlocked(Segment way) {
		Iterator<Box> boxIt = this.game.getBoxes().iterator();
		while (boxIt.hasNext()) {
			Box box = boxIt.next();
			for (int i = 0; i < 4; i++) {
				Segment wall = box.getWallBox().getWalls().get(i);
				if (!isVisibale(way, wall)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * this function finds the best location to place the player in the beginning of
	 * the game. the decision is made depending on how many fruits we have in a
	 * cluster. it will return the fruit with the most neighboring fruits.
	 */
	private Point3D findCluster() {
		double distance = 150;
		MyCoords convert = new MyCoords();
		Iterator<Fruit> fruitIt = game.getFruits().iterator();

		/* for each fruit finds how many are in range from it */
		while (fruitIt.hasNext()) {
			Fruit current = fruitIt.next();
			Iterator<Fruit> fruitIttwo = game.getFruits().iterator();
			while (fruitIttwo.hasNext()) {
				Fruit temp = fruitIttwo.next();
				if (distance > convert.distance3d(current.getGps(), temp.getGps())) {
					current.setFruitsNearMe();
				}
			}
			fruitIttwo = game.getFruits().iterator();
		}
		int max = 0;
		int index = 0;
		int indexstartFruit = 0;
		fruitIt = game.getFruits().iterator();
		/* find the fruit with the most neighbors */
		while (fruitIt.hasNext()) {
			Fruit current = fruitIt.next();
			index++;
			if (current.getFruitsNearMe() > max) {
				max = current.getFruitsNearMe();
				indexstartFruit = index;
			}
		}

		/*
		 * Checking to see if there is a pacman near the cluster if there is start on
		 * top of it.
		 */
		Iterator<Pacman> pacIt = game.getPacmans().iterator();
		while (pacIt.hasNext()) {
			Pacman pac = pacIt.next();
			if (convert.distance3d(game.getFruits().get(indexstartFruit).getGps(), pac.getGps()) < 200) {
				double x = pac.getGps().x();
				double y = pac.getGps().y();
				Point3D startPoint = new Point3D(x, y, 0);
				return startPoint;
			}
		}

		double x = game.getFruits().get(indexstartFruit).getGps().x();
		double y = game.getFruits().get(indexstartFruit).getGps().y();
		Point3D startPoint = new Point3D(x, y, 0);
		return startPoint;
	}

	/**
	 * This function tells if the way between two points is blocked by a segment.
	 * This function work with gps points lat and lon.
	 * 
	 * @param way , the line we check if blocked.
	 * @param     wall, the blocking line.
	 * @return true if way not blocked.
	 */
	private boolean isVisibale(Segment way, Segment wall) {
		double x1 = way.getA().y();
		double x2 = way.getB().y();
		double y1 = way.getA().x();
		double y2 = way.getB().x();

		double wx1 = wall.getA().y();
		double wy1 = wall.getA().x();
		double wx2 = wall.getB().y();
		double wy2 = wall.getB().x();

		if (wall.isVertical() == true) {
			if (x1 <= wx1 && wx1 <= x2 || x2 <= wx1 && wx1 <= x1) {
				double m = (y2 - y1) / (x2 - x1);
				double b = y1 - (m * x1);
				double y = m * (wx1) + b;

				if (wy1 <= y && y <= wy2 || wy2 <= y && y <= wy1) {
					return false;
				}
			}
		} else {
			if (y1 <= wy1 && wy1 <= y2 || y2 <= wy1 && wy1 <= y1) {
				double dx = x2 - x1;
				double dy = y2 - y1;
				if (Math.abs(dx) < 0.00001) {
					x2 = x2 + 0.00001;
				}
				if (Math.abs(dy) < 0.00001) {
					y2 = y2 + 0.00001;
				}
				double m = (y2 - y1) / (x2 - x1);
				double b = y1 - (m * x1);
				double x = (wy1 - b) / m;
				if (wx1 <= x && x <= wx2 || wx2 <= x && x <= wx1) {
					return false;
				}
			}
		}

		return true;
	}

}