package Algo;

import java.util.ArrayList;
import java.util.Iterator;

import graph.Node;

/**
 * This class hold the way the player has to take to get to its target.
 * This class can return the distance to the target and the id of the corners
 * the play has to go to in Int.
 * @author Alex vaisman, Shay naor
 *
 */
public class Path {

	private Node data;
	private ArrayList<Integer> theWay;
	private  double distance;

	
	public Path(Node data) {
		this.data = data;
		this.distance = data.getDist();
		this.theWay = new ArrayList<Integer>();
		ConvertToInt();
	}

    /*
     * This function converts corner ids from String to Int.
     */
	private void ConvertToInt() {
		ArrayList<String> theStringWay = data.getPath();
		Iterator<String> stringIt = theStringWay.iterator();

		if(stringIt.hasNext()) {
			stringIt.next();
		}
		while(stringIt.hasNext()) {
			int num = Integer.parseInt(stringIt.next());
			this.theWay.add(num);
		}
	}

        /* Getters */
	public Node getData() {
		return data;
	}


	public ArrayList<Integer> getTheWay() {
		return theWay;
	}


	public double getDistance() {
		return distance;
	}
}