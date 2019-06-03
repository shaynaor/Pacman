package GIS;

import java.util.ArrayList;

import Geom.Point3D;
/**
 * This class represents the 4 virtual points of a box.
 * @author Alex vaisman, Shay naor.
 *
 */
public class BoxHitBox {

	private Point3D VtopRight;
	private Point3D Vtopleft;
	private Point3D VbotRight;
	private Point3D VbotLeft;
	private ArrayList<Point3D> VboxCorners;
	

	public BoxHitBox(Box box) {
		movePoints(box);
	}

	private void movePoints(Box box) {
		this.VbotLeft = new Point3D(box.getBotLeft().x()-0.00004,box.getBotLeft().y()-0.00004,0);
		this.VbotRight = new Point3D(box.getBotRight().x()-0.00004,box.getBotRight().y()+0.00004,0);
		this.Vtopleft = new Point3D(box.getTopLeft().x()+0.00004,box.getTopLeft().y()-0.00004,0);
		this.VtopRight = new Point3D(box.getTopRight().x()+0.00004,box.getTopRight().y()+0.00004,0);
		this.VboxCorners = new  ArrayList<Point3D>();
		this.VboxCorners.add(this.VbotLeft);
		this.VboxCorners.add(this.Vtopleft);
		this.VboxCorners.add(this.VbotRight);
		this.VboxCorners.add(this.VtopRight);
		
	}
	
	
	/* Getters */


	public Point3D getVtopRight() {
		return VtopRight;
	}

	public Point3D getVtopleft() {
		return Vtopleft;
	}

	public Point3D getVbotRight() {
		return VbotRight;
	}

	public Point3D getVbotLeft() {
		return VbotLeft;
	}

	@Override
	public String toString() {
		return "BoxHitBox [VtopRight=" + VtopRight + ", Vtopleft=" + Vtopleft + ", VbotRight=" + VbotRight
				+ ", VbotLeft=" + VbotLeft + "]";
	}

	public ArrayList<Point3D> getVboxCorners() {
		return VboxCorners;
	}




}
