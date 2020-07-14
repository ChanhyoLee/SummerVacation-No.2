package Shapes;

import java.awt.Shape;
import java.awt.geom.*;
import java.awt.geom.Rectangle2D.Double;

import Screens.ShapeSelectionPanel;
import Screens.MainScreen;

public class ShapeObject {

	protected Shape shape;
	protected Point2D start_point;
	protected Point2D end_point;
	protected boolean selected;
	protected boolean rotate_selected;
	protected int fill_mode;
	protected LineAttribute lineattribute;
	protected int kind;
	protected double angle;
	
	public ShapeObject(Point2D start_point, Point2D end_point, LineAttribute lineattribute, int n, int fillmode){
		this.start_point = start_point;
		this.end_point = end_point;
		selected = false;
		fill_mode = fillmode;
		this.lineattribute =lineattribute;
		kind = n;
		angle = 0;
		rotate_selected = false;
		int[] location = MainScreen.convert_coordinate((int)start_point.getX(), (int)start_point.getY(), MainScreen.get_width(start_point, end_point), MainScreen.get_height(start_point, end_point));
		//System.out.println(Arrays.toString(location));
		//if(kind==ShapeSelectionPanel.PEN) shape = new Rectangle2D.Double((double)location[0], (double)location[1], (double)location[2], (double)location[3]);
		if(kind==ShapeSelectionPanel.RECTANGLE) shape=new Rectangle2D.Double((double)location[0], (double)location[1], (double)location[2], (double)location[3]);
		else if(kind==ShapeSelectionPanel.OVAL) shape=new Ellipse2D.Double((double)location[0], (double)location[1], (double)location[2], (double)location[3]); 
		else if(kind==ShapeSelectionPanel.LINE) shape=new Line2D.Double(start_point.getX(), start_point.getY(), end_point.getX(), end_point.getY());
	}
	
	public ShapeObject(ShapeObject shp) {
		this.start_point = new Point2D.Double();
		start_point.setLocation(shp.start_point);
		this.end_point = new Point2D.Double();
		end_point.setLocation(shp.end_point);
		selected = shp.selected;
		fill_mode = shp.fill_mode;
		this.lineattribute = new LineAttribute(shp.lineattribute);
		kind = shp.kind;
		angle = shp.angle;
		rotate_selected = shp.rotate_selected;
		//int[] location = MainScreen.convert_coordinate((int)start_point.getX(), (int)start_point.getY(), MainScreen.get_width(start_point, end_point), MainScreen.get_height(start_point, end_point));
		//System.out.println(Arrays.toString(location));
//		if(kind==ShapeSelectionPanel.RECTANGLE) shape=new Rectangle2D.Double((double)location[0], (double)location[1], (double)location[2], (double)location[3]);
//		else if(kind==ShapeSelectionPanel.OVAL) shape=new Ellipse2D.Double((double)location[0], (double)location[1], (double)location[2], (double)location[3]); 
//		else if(kind==ShapeSelectionPanel.LINE) shape=new Line2D.Double(start_point.getX(), start_point.getY(), end_point.getX(), end_point.getY());
//		
		if(kind!=ShapeSelectionPanel.PEN) {
			AffineTransform at = new AffineTransform();
			at.rotate(angle, shp.get_shape().getBounds().getCenterX(),shp.get_shape().getBounds().getCenterY());
			shape = at.createTransformedShape(shp.shape);
		}
	}

	public Point2D get_startpoint() {return start_point;}
	public Point2D get_endpoint() {return end_point;}
	public boolean get_selected() {return selected;}
	public int get_fill_mode() {return fill_mode;}
	public LineAttribute get_LineAttribute() {return lineattribute;}
	public int get_kind() {return kind;}
	public Shape get_shape() {return shape;}
	public double get_angle() {return angle;}
	public boolean get_rotate_selected() {return rotate_selected;}

	public void set_startpoint(Point2D x) {start_point = x;}
	public void set_endpoint(Point2D x) {end_point = x;}
	public void set_selected(boolean x) {selected = x;}
	public void set_fill_mode(int x) {fill_mode = x;}
	public void set_LineAttribute(LineAttribute x) {lineattribute = x;}
	public void set_kind(int x) {kind = x;}
	public void set_shape(Shape x) {shape = x;}
	public void set_angle(double x) {
		x = x%(2*Math.PI);
		angle = x;
	}
	public void set_rotate_selected(boolean x) {rotate_selected = x;}

	
	public void reset_shape() {
		int[] location = MainScreen.convert_coordinate((int)start_point.getX(), (int)start_point.getY(), MainScreen.get_width(start_point, end_point), MainScreen.get_height(start_point, end_point));
		//System.out.println(Arrays.toString(location));
		if(kind==ShapeSelectionPanel.RECTANGLE) shape=new Rectangle2D.Double((double)location[0], (double)location[1], (double)location[2], (double)location[3]);
		else if(kind==ShapeSelectionPanel.OVAL) shape=new Ellipse2D.Double((double)location[0], (double)location[1], (double)location[2], (double)location[3]); 
		else if(kind==ShapeSelectionPanel.LINE) shape=new Line2D.Double(start_point.getX(), start_point.getY(), end_point.getX(), end_point.getY());
		AffineTransform at = new AffineTransform();
		//at.translate(location[0]+location[2]/2, location[1]);
		at.rotate(angle,shape.getBounds().getCenterX(), shape.getBounds().getCenterY());
		//at.translate(-location[0], -location[1]);
		shape = at.createTransformedShape(shape);
	}
//	public void reset_shape(Shape shape, double dx, double dy) {
//		shape.
//	}
	
	public boolean compare_shape(ShapeObject shapeobj) {
		//(!this.shape.equals(shapeobj.shape)) return false;
		if(!this.start_point.equals(shapeobj.start_point)) return false;
		else if(!this.end_point.equals(shapeobj.end_point)) return false;
		else if(this.fill_mode!=shapeobj.fill_mode) return false;
		else if(!this.lineattribute.equals(shapeobj.lineattribute)) return false;
		else if(this.kind!=shapeobj.kind) return false;
		else if(this.angle!=shapeobj.angle) return false;
		else return true;
	}
//	public boolean compare_shape(ShapeObject shapeobj) {
//		if(!this.shape.equals(shapeobj.shape)) {
//			//System.out.printf("%d, %d\n", this.shape.hashCode(), shapeobj.shape.hashCode());
//			//System.out.println("Shape unequal");
//			return false;
//		}
//		else if(!this.start_point.equals(shapeobj.start_point)) {
//			System.out.println("SP unequal");
//			return false;
//		}
//		else if(!this.end_point.equals(shapeobj.end_point)) {
//			System.out.println("EP unequal");
//			return false;
//		}
//		else if(this.fill_mode!=shapeobj.fill_mode) {
//			System.out.println("Fillmode unequal");
//			return false;
//		}
//		else if(!this.lineattribute.equals(shapeobj.lineattribute)) {			
//			System.out.println("Stroke unequal");
//			return false;
//		}
//		else if(this.kind!=shapeobj.kind) {
//			System.out.println("kind unequal");
//			return false;
//		}
//		else if(this.angle!=shapeobj.angle) {
//			System.out.println("angle unequal");
//			return false;
//		}
//		else return true;
//	}
}
