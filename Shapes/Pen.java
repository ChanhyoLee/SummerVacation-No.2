package Shapes;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.util.Arrays;
import java.util.Vector;

import Screens.ShapeSelectionPanel;

public class Pen extends ShapeObject{
	
	Vector<Point2D> one_stroke;

	public Pen(Point2D start_point, Point2D end_point, LineAttribute lineattribute, int n, int fillmode) {
		super(start_point, end_point, lineattribute, n, fillmode);
	}

	public void set_one_stroke(Vector<Point2D> point_vector) {
		one_stroke = point_vector;
		double[] outside_rectangle = find_largest_rectangle();
		shape = new Rectangle2D.Double(outside_rectangle[0],outside_rectangle[1],outside_rectangle[2],outside_rectangle[3]);
		}
	public Vector<Point2D> get_one_stroke() {return one_stroke;}
	
	public double[] find_largest_rectangle() {
		double min_x = one_stroke.get(0).getX(), min_y = one_stroke.get(0).getY();
		double max_x = one_stroke.get(0).getX(), max_y = one_stroke.get(0).getY();
		for(Point2D p: one_stroke) {
			if(p.getX()<min_x) min_x = p.getX();
			else if(p.getX()>max_x) max_x = p.getX();
			if(p.getY()<min_y) min_y = p.getY();
			else if(p.getY()>max_y) max_y = p.getY();
		}
		return new double[] {min_x,min_y,max_x-min_x,max_y-min_y};
	}

	public void reset_coordinates(double d_angle, double center_x, double center_y) {
		//Point2D[] temp_one_stroke = (Point2D[])(one_stroke.toArray());
		center_x = (this.get_startpoint().getX() + this.get_endpoint().getX())/2;
		center_y = (this.get_startpoint().getY() + this.get_endpoint().getY())/2;
		Point2D[] temp_one_stroke = new Point2D[one_stroke.size()];
		//temp_one_stroke = Vector.toArray();
		for(int i=0; i<one_stroke.size(); i++) {
			temp_one_stroke[i] = one_stroke.get(i);
		}
		System.out.println("previous: "+temp_one_stroke[0]);
		AffineTransform.getRotateInstance(d_angle, center_x, center_y).transform(temp_one_stroke,0,temp_one_stroke,0,one_stroke.size());
		this.set_one_stroke(new Vector<Point2D>(Arrays.asList(temp_one_stroke)));
		System.out.println("after: "+temp_one_stroke[0]);
	}

	public void move_pen(double dx, double dy) {
		Vector<Point2D> temp_point_vector = new Vector<Point2D>();
		for(int i=0; i<one_stroke.size(); i++) {
			Point2D temp_point = new Point2D.Double(one_stroke.get(i).getX()+dx, one_stroke.get(i).getY()+dy);
			temp_point_vector.add(temp_point);
		}
		this.set_one_stroke(temp_point_vector);
	}
}
