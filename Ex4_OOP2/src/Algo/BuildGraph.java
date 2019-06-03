package Algo;

import java.util.ArrayList;
import java.util.Iterator;

import Coords.MyCoords;
import GIS.Corner;
import GIS.Fruit;
import GIS.Player;
import graph.Graph;
import graph.Graph_Algo;
import graph.Node;


/**
 * This function will find the shortest path from one point to another, when the line
 * of sight between the two points is blocked.
 * this function uses the dijkstra algorithm. wiki : https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm.

 * @author Alex vaisman, shay naor.
 * 
 *
 */
public class BuildGraph {
	private Player player;
	private ArrayList<Corner> corners;
	private Fruit fruit;
	private Graph G;
	private Node ans;

	
	public BuildGraph(Graph G, Player player, ArrayList<Corner> corners, Fruit fruit) {
		G.clear_meta_data();
		this.corners = corners;
		this.player = player;
		this.fruit = fruit;
		this.G = G;
		InitGraph();
	}

	/**
	 * This function adds all the nodes to the graph.
	 * when one node is for the player , one node is for the target (fruit or pacman).
	 * and the rest of the nodes are the corners of the boxes which are not within a box.
	 */
	private void InitGraph() {
		String source = "player";
		String target = "fruit";

		Node pStart = new Node(source);
		G.add(pStart); // adding player

		// adding all corners to the graph with their id
		Iterator<Corner> cornIt = this.corners.iterator();
		while (cornIt.hasNext()) {
			String id = "" + cornIt.next().getMyId();
			Node d = new Node(id);
			G.add(d);
		}
		Node tFruit = new Node(target);
		G.add(tFruit); // adding fruit
		
		AddEdges();
		Graph_Algo.dijkstra(G, source);

		Node b = G.getNodeByName(target);
		this.ans = b;

	}

	
	/**
	 * This function adds for each node what nodes are connected to it
	 * and how far away are they. 
	 */
	private void AddEdges() {
		MyCoords convert = new MyCoords();
		
		for(int i = 0 ; i < G.size(); i++) {
			G.getNodeByIndex(i).get_ni().clear();
		}
		
		/* added edges that the player sees */
		for (int i = 0; i < this.player.getWhatISee().size(); i++) {
			Corner corn = this.player.getWhatISee().get(i);
			G.addEdge("player", "" + corn.getMyId(), convert.distance3d(player.getGps(), corn.getGps()));
		}

		
		/* add for each corner what it sees and if it sees the target add it as well */
		Iterator<Corner> cornerIt = this.corners.iterator();
		while (cornerIt.hasNext()) {
			Corner corn = cornerIt.next();

			for (int i = 0; i < corn.getWhatISee().size(); i++) {
				double distance = convert.distance3d(corn.getGps(), corn.getWhatISee().get(i).getGps());
				G.addEdge("" + corn.getMyId(), "" + corn.getWhatISee().get(i).getMyId(), distance);
			}
			for (int i = 0; i < corn.getWhatFruitIsee().size(); i++) {
				Fruit seeFruit = corn.getWhatFruitIsee().get(i);

				if (seeFruit.getGps().x() == this.fruit.getGps().x()
						&& seeFruit.getGps().y() == this.fruit.getGps().y()) {
					double distance = convert.distance3d(corn.getGps(), this.fruit.getGps());
					G.addEdge("" + corn.getMyId(), "fruit", distance);
				}
			}
		}
	}

	/* Getters */
	public Node getAns() {
		return ans;
	}
}