package Screens;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.geom.Rectangle2D.Double;

import javax.swing.*;
import Shapes.LineAttribute;
import Shapes.Pen;
import Shapes.ShapeObject;
import java.util.ArrayList;
import java.util.Vector;

public class MainScreen extends JComponent implements MouseListener, MouseMotionListener{
	
	private Point2D m_start_point;
	private Point2D m_end_point;  // for last mouse position
	private ArrayList<ShapeObject> shapes = new ArrayList<ShapeObject>();
	private Vector<Point2D> temp_point_vector = new Vector<Point2D>();
	//private OptionFrame of = new OptionFrame();
	private static OptionFrame of = new OptionFrame();
	private Stroke stroke;
	private Color color;
	private boolean selection_box_drawed = false;
	private boolean mouse_point_contained = false;
	private boolean rotate_clicked = false;
	private double ax, by = 0;
	private Point m_start_point_forPen;
	
	public MainScreen() { 
		// Listen for mouse movements and clicks
		addMouseMotionListener(this);
		addMouseListener(this);
		//of = new OptionFrame();
		//memory.add(this);
		//stroke = of.get_stroke_panel().get_stroke();
		color = of.get_color_panel().get_color();
	}
	
	public MainScreen(MainScreen ms) {
		addMouseMotionListener(this);
		addMouseListener(this);
		//of = new OptionFrame();
		//memory.addAll(ms.memory);
		shapes = new ArrayList<ShapeObject>();
		for(ShapeObject shp : ms.shapes) {
			shapes.add(new ShapeObject(shp));
		}
		color = of.get_color_panel().get_color();
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		try {
			draw_shapelist(g2);
			stroke=of.get_stroke_panel().get_stroke();
			if(of.get_shape_panel().get_mode()==ShapeSelectionPanel.PEN) {
				draw_pen(g2, temp_point_vector, stroke, of.get_color_panel().get_color());
			}
			if(of.get_shape_panel().get_mode()!=ShapeSelectionPanel.SELECTION) color=of.get_color_panel().get_color();
			else color=Color.BLACK;
			if(!rotate_clicked)	draw(g2, m_start_point, m_end_point, of.get_shape_panel().get_mode(), of.get_shape_panel().get_fillmode(), stroke, color);
		}catch(Exception e) {}
	}
	
	public void draw_shapelist(Graphics2D g2) {
		for(int i=0; i<shapes.size(); i++) {
			//System.out.println("inside");
			ShapeObject temp_shape = shapes.get(i);
//			Point2D temp_start_point = temp_shape.get_startpoint();
//			Point2D temp_end_point = temp_shape.get_endpoint();
//			int kind = temp_shape.get_kind();
			int fill = temp_shape.get_fill_mode();
			Shape shape = temp_shape.get_shape();
			LineAttribute la = temp_shape.get_LineAttribute();
			Stroke temp_stroke = new BasicStroke(la.get_width(), la.get_cap(), la.get_join(), la.get_miterlimit(), la.get_dash(), la.get_dash_phase());
			//la.set_color(of.get_color_panel().get_color());
			Color temp_color = new Color(la.get_color().getRGB());	         			
			//System.out.println("object"+temp_color.toString());
			//System.out.println("now"+color.toString());
			if(temp_shape.get_selected()) {
				draw_boundary(g2, temp_shape);
			}
			else {
				if(temp_shape.get_kind()==ShapeSelectionPanel.PEN) draw_pen(g2, ((Pen)temp_shape).get_one_stroke(), temp_stroke, temp_color);
				else draw_shape(g2, shape, fill, temp_stroke, temp_color);
			}
		}
		if(count_selected()>0) draw_rotate_mark(g2, this.get_selected_shapes());
	}
	public void draw(Graphics2D g2, Point2D sp, Point2D ep, int kind, int fill, Stroke stroke, Color c) {
		int width = get_width(sp, ep);
		int height = get_height(sp, ep);
		int x = (int)sp.getX();
		int y = (int)sp.getY();
		draw_shape(g2, x, y, width, height, kind, fill, stroke, c);
	}
	
	public void draw_shape(Graphics2D g2, int x, int y, int width, int height, int kind, int fill, Stroke stroke, Color c) {
		//stroke = of.get_stroke_panel().get_stroke();
		g2.setStroke(stroke);
		g2.setColor(c);
		//System.out.println(c.toString());
		//g2.setStroke(new BasicStroke(5, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, new float[]{10,5,5,5}, 0));
		
		switch(kind) {
		case(ShapeSelectionPanel.RECTANGLE):
			int[] rect_xy = convert_coordinate(x,y,width,height);
			Shape rect_shape = new Rectangle2D.Double(rect_xy[0], rect_xy[1], rect_xy[2], rect_xy[3]);
			if(fill==ShapeSelectionPanel.EMPTY) //g2.drawRect(rect_xy[0], rect_xy[1], rect_xy[2], rect_xy[3]);
				g2.draw(rect_shape);
			else g2.fill(rect_shape);
			break;
		case(ShapeSelectionPanel.OVAL):
			int[] oval_xy = convert_coordinate(x,y,width,height);
			Shape oval_shape = new Ellipse2D.Double(oval_xy[0], oval_xy[1], oval_xy[2], oval_xy[3]);
			if(fill==ShapeSelectionPanel.EMPTY) //g2.drawOval(oval_xy[0], oval_xy[1], oval_xy[2], oval_xy[3]);
				g2.draw(oval_shape);
			else g2.fill(oval_shape);
			break;
		case(ShapeSelectionPanel.LINE):
			g2.drawLine(x, y, x+width, y+height);
			break;
		case(ShapeSelectionPanel.SELECTION):
			if(!selection_box_drawed && !mouse_point_contained) {
				int[] selectionbox_xy = convert_coordinate(x,y,width,height);
				g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1f, new float[]{10}, 4f));
				g2.drawRect(selectionbox_xy[0], selectionbox_xy[1], selectionbox_xy[2], selectionbox_xy[3]);
				//repaint();
			}
		}
	}
	
//	
	public void draw_shape(Graphics2D g2, Shape shape, int fill, Stroke stroke, Color c) {
		g2.setStroke(stroke);
		g2.setColor(c);
		if(fill==ShapeSelectionPanel.EMPTY)	g2.draw(shape);
		else if(fill==ShapeSelectionPanel.FILL) g2.fill(shape);
	}
	
	public void draw_boundary(Graphics2D g2, ShapeObject shapeobj) {
		LineAttribute lineattribute = shapeobj.get_LineAttribute(); 
		g2.setStroke(new BasicStroke(lineattribute.get_width(), lineattribute.get_cap(), lineattribute.get_join(), lineattribute.get_miterlimit(), lineattribute.get_dash(), lineattribute.get_dash_phase()));
		Color c = new Color(1,0,0,0.2f);
		g2.setColor(c);
		if(shapeobj.get_kind()==ShapeSelectionPanel.PEN) {
			draw_pen(g2, ((Pen)shapeobj).get_one_stroke(), g2.getStroke(), g2.getColor());
		}
		else if(shapeobj.get_fill_mode()==ShapeSelectionPanel.EMPTY || shapeobj.get_kind()==ShapeSelectionPanel.LINE) g2.draw(shapeobj.get_shape()); // Need to Implement "FILL"!!
		else g2.fill(shapeobj.get_shape());
	}
	
	public void draw_rotate_mark(Graphics2D g2, ArrayList<ShapeObject> selected_shapes){
		double center_x, sp_y;
//		double ep_x, ep_y;
//		sp_x = get_smallest_coordinate(selected_shapes)[0]; sp_y = get_smallest_coordinate(selected_shapes)[1];
//		ep_x = get_largest_coordinate(selected_shapes)[0]; ep_y = get_largest_coordinate(selected_shapes)[1];
		
//		center_x = (sp_x + ep_x)/2.0;
//		center_y = (sp_y + ep_y)/2.0;
		
		//(center_x, ep_y+10)
		ImageIcon imgicon = new ImageIcon("redo.png");
		Image img = imgicon.getImage();
		
		for(int i=0; i<selected_shapes.size(); i++) {
			center_x = selected_shapes.get(i).get_shape().getBounds().getCenterX();
			//center_y = selected_shapes.get(i).get_shape().getBounds().getCenterY();
			sp_y = selected_shapes.get(i).get_shape().getBounds().getMinY();
			if(selected_shapes.get(i).get_kind()!=ShapeSelectionPanel.PEN) g2.drawImage(img, (int)center_x-15, (int)sp_y-30, (int)center_x+15, (int)sp_y, 0, 0, 30, 30, this);
			g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1f, new float[]{10}, 2f));
			g2.setColor(Color.black);
			if(selected_shapes.get(i).get_kind()!=ShapeSelectionPanel.PEN) {
				g2.draw(selected_shapes.get(i).get_shape().getBounds());
			}
			else {
				double[] boundary_rect_forPen = ((Pen)selected_shapes.get(i)).find_largest_rectangle();
				g2.drawRect((int)boundary_rect_forPen[0], (int)boundary_rect_forPen[1], (int)boundary_rect_forPen[2], (int)boundary_rect_forPen[3]);
			}
		}
		//g2.drawRect((int)sp_x, (int)sp_y, (int)(ep_x-sp_x), (int)(ep_y-sp_y));
	}
	
	public void draw_pen(Graphics2D g2, Vector<Point2D> one_stroke, Stroke stroke, Color c) {
		g2.setStroke(stroke);
		g2.setColor(c);
		for(int i=0; i<one_stroke.size()-1; i++) {
			int center_x, center_y;
			center_x = (int)((one_stroke.get(i).getX()+one_stroke.get(i+1).getX())/2);
			center_y = (int)((one_stroke.get(i).getY()+one_stroke.get(i+1).getY())/2);
			//System.out.printf("(%f, %f) to (%f, %f)\n", one_stroke.get(i).getX(), one_stroke.get(i).getY(), one_stroke.get(i+1).getX(), one_stroke.get(i+1).getY());
			g2.drawLine((int)one_stroke.get(i).getX(), (int)one_stroke.get(i).getY(), center_x, center_y);
			g2.drawLine(center_x, center_y, (int)one_stroke.get(i+1).getX(), (int)one_stroke.get(i+1).getY());
		}
		//g2.drawLine((int)temp_point_vector.lastElement().getX(), (int)temp_point_vector.lastElement().getY(), (int)end_point.getX(), (int)end_point.getY());
		//temp_point_vector.add(end_point);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("current mode: "+of.get_shape_panel().get_mode());
		m_end_point = e.getPoint();
		//ystem.out.println(mouse_point_contained);
		if(of.get_shape_panel().get_mode()==ShapeSelectionPanel.PEN) {
			temp_point_vector.add(e.getPoint());
		}
		else if(!rotate_clicked && !mouse_point_contained && !selection_box_drawed && of.get_shape_panel().get_mode()==ShapeSelectionPanel.SELECTION) select_shape();
		else if(of.get_shape_panel().get_mode()!=ShapeSelectionPanel.SELECTION) unselect_all_shapes();
		//ArrayList<ShapeObject> selected_shapes = get_selected_shapes();
		//System.out.println(selection_box_drawed);
		if(count_selected()>0) {
			if(rotate_clicked) rotate_shape(e);
			else if(mouse_point_contained) move_shape(e);
			//repaint();
		}
		repaint();

	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(of.get_shape_panel().get_mode()==ShapeSelectionPanel.SELECTION) {
			//System.out.printf("(%d,%d)",e.getX(),e.getY());
			int x = e.getX();
			int y = e.getY();
			boolean selected_once = false;
			for (int i = 0; i<shapes.size(); i++) {
				Point2D p = new Point2D.Double(x, y);
				if(shapes.get(i).get_kind()==ShapeSelectionPanel.LINE && shapes.get(i).get_shape().getBounds().contains(p) && !selected_once) {
					shapes.get(i).set_selected(true);
					selected_once=true;
				}
				else if(shapes.get(i).get_shape().contains(p.getX(), p.getY()) && !selected_once) {
					shapes.get(i).set_selected(true);
					selected_once = true; 
				}
				else shapes.get(i).set_selected(false); 
			} 
			repaint();
		}		 
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		selection_box_drawed = false;
		m_start_point = e.getPoint();
		m_end_point = e.getPoint();
		m_start_point_forPen = e.getPoint();
		mouse_point_contained = false;
		ArrayList<ShapeObject> selected_shapes = get_selected_shapes();
		ax = 0; by = 0;
		if(of.get_shape_panel().get_mode()!=ShapeSelectionPanel.SELECTION) unselect_all_shapes();
		if(selected_shapes.size()>0) {
			double real_x = get_smallest_coordinate(selected_shapes)[0];
			double real_y = get_smallest_coordinate(selected_shapes)[1];

			if(click_rotate_mark(selected_shapes, e.getPoint())){
				rotate_clicked = true;
			}
			else if(contain_point(selected_shapes, e.getPoint())) {
				mouse_point_contained=true;
				ax = real_x - m_start_point.getX();
				by = real_y - m_start_point.getY();
			}

		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

		m_end_point = e.getPoint();
		//if(mouse_point_contained) if(is_changed()) System.out.println("changed");
		if(of.get_shape_panel().get_mode()==ShapeSelectionPanel.PEN) {
			create_pen();
		}
		else if(of.get_shape_panel().get_mode()!=ShapeSelectionPanel.SELECTION) create_shape();
		else {
			//select_shape();
			selection_box_drawed = true;
			//repaint();
		}
		rotate_clicked = false;
		mouse_point_contained = false;
		repaint();
		if(is_changed()); 
		for(int i=0; i<count_selected(); i++) {//System.out.println("changed");
			this.get_selected_shapes().get(i).set_rotate_selected(false);
		}
		//selection_box_drawed = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public int count_selected() {
		int count=0;
		for(int i=0; i<shapes.size(); i++) {
			if(shapes.get(i).get_selected()) count++;
		}
		return count;
	}
	
	public void create_pen() {
		LineAttribute new_la = new LineAttribute(of.get_stroke_panel().get_lineattribute());
		new_la.set_color(of.get_color_panel().get_color());
		Pen new_pen = new Pen(m_start_point, m_end_point, new_la, of.get_shape_panel().get_mode(), of.get_shape_panel().get_fillmode());
		new_pen.set_one_stroke(new Vector<Point2D>(temp_point_vector));
		shapes.add(new_pen);
		temp_point_vector.removeAllElements();
	}
	
	public void create_shape() {
		LineAttribute new_la = new LineAttribute(of.get_stroke_panel().get_lineattribute());
		new_la.set_color(of.get_color_panel().get_color());
		ShapeObject new_shape = new ShapeObject(m_start_point, m_end_point, new_la, of.get_shape_panel().get_mode(), of.get_shape_panel().get_fillmode());
		//System.out.println(of.get_stroke_panel().get_lineattribute());
		//System.out.println(of.get_color_panel().get_color());
		shapes.add(new_shape);
		//if(is_changed()) System.out.println("changed");
	}
	
	public void select_shape() {
		int[] location = convert_coordinate((int)m_start_point.getX(), (int)m_start_point.getY(), MainScreen.get_width(m_start_point, m_end_point), get_height(m_start_point, m_end_point));
		Shape temp_rect = new Rectangle2D.Double(location[0],location[1],location[2],location[3]);
		for(int i=shapes.size()-1; i>=0; i--) {
			ShapeObject temp_shpobj = shapes.get(i);
			if(temp_rect.contains(temp_shpobj.get_shape().getBounds2D())) {
				temp_shpobj.set_selected(true);
			}
			else temp_shpobj.set_selected(false);
		}
	}
	
	public ArrayList<ShapeObject> get_selected_shapes(){
		ArrayList<ShapeObject> selected_shapes = new ArrayList<ShapeObject>();
		for(int i=0; i<shapes.size(); i++) {
			if(shapes.get(i).get_selected()) selected_shapes.add(shapes.get(i));
		}
		return selected_shapes;
	}
	
	public void unselect_all_shapes() {
		for(int i=0; i<shapes.size(); i++) {
			shapes.get(i).set_selected(false);
		}
	}
	
	public boolean contain_point(ArrayList<ShapeObject> selected_shapes, Point2D p) {
		boolean contain = false;
		for(int i=0; i<selected_shapes.size(); i++) {
			if(selected_shapes.get(i).get_shape().getBounds().contains(p)) {
			//if(selected_shapes.get(i).get_shape().contains(p)) {
				contain = true;
				break;
			}
		}
		return contain;
	}
	
	public boolean click_rotate_mark(ArrayList<ShapeObject> selected_shapes, Point2D p) {
		boolean click = false;
		double center_x, sp_y;
				
		for(int i=0; i<selected_shapes.size(); i++) {
			//System.out.println("shape hash: " +selected_shapes.get(i).get_shape().hashCode());
			center_x = selected_shapes.get(i).get_shape().getBounds().getCenterX();
			sp_y = selected_shapes.get(i).get_shape().getBounds().getMinY();
			Shape rotate_mark_area = new Rectangle2D.Double(center_x-15, sp_y-30, 30, 30);
			if(selected_shapes.get(i).get_kind()==ShapeSelectionPanel.PEN) continue;
			if(rotate_mark_area.getBounds().contains(p)) {
				click = true;
				selected_shapes.get(i).set_rotate_selected(true);
				break;
			}
		}
		return click;
	}
	
	public void delete_shape(ArrayList<ShapeObject> selected_list) {
		//ystem.out.println(selected_list.size());
		//System.out.println(count_selected());
		for(int i=0; i<selected_list.size(); i++) {
			shapes.remove(selected_list.get(i));
		}
		if(is_changed());//System.out.println("changed");
		repaint();
	}
	
	/*
	 * public void make_group(ArrayList<ShapeObject> selected_list) { double
	 * smallest[] = get_smallest_coordinate(selected_list); double largest[] =
	 * get_largest_coordinate(selected_list); Point2D new_start_point = new
	 * Point2D.Double(smallest[0], smallest[1]); Point2D new_end_point = new
	 * Point2D.Double(largest[0], largest[1]); Shape new_shape = new Shape();
	 * 
	 * }
	 */
	
	public void move_shape(MouseEvent e) {
		//AffineTransform at = new AffineTransform();
		int xx = e.getX();
		int yy = e.getY();
		double dx = xx - m_start_point.getX();
		double dy = yy - m_start_point.getY();
		ArrayList<ShapeObject> shapes = get_selected_shapes();
		Point2D new_start_point = new Point2D.Double();
		Point2D new_end_point = new Point2D.Double();
		//System.out.printf("dx = %f, dy = %f\n",ax+dx, by+dy);
		for (int i = 0; i < shapes.size(); i++) {
			if(shapes.get(i).get_kind()==ShapeSelectionPanel.PEN) {
				double dx_forPen = xx - m_start_point_forPen.getX();
				double dy_forPen = yy - m_start_point_forPen.getY();
				//System.out.printf("dx = %f, dy = %f\n",dx_forPen, dy_forPen);
				((Pen)shapes.get(i)).move_pen(dx_forPen, dy_forPen);
				m_start_point_forPen = e.getPoint();
				//m_start_point = ;
			}
			else if(shapes.get(i).get_kind()==ShapeSelectionPanel.LINE){
				new_start_point = new Point2D.Double(shapes.get(i).get_startpoint().getX()+ax+dx, shapes.get(i).get_startpoint().getY()+by+dy);
				new_end_point = new Point2D.Double(shapes.get(i).get_endpoint().getX()+ax+dx, shapes.get(i).get_endpoint().getY()+by+dy);
			}
			else {
				double sp_x = Math.min(shapes.get(i).get_startpoint().getX(), shapes.get(i).get_endpoint().getX())+ax+dx;
				double sp_y = Math.min(shapes.get(i).get_startpoint().getY(), shapes.get(i).get_endpoint().getY())+by+dy;
				double ep_x = Math.max(shapes.get(i).get_startpoint().getX(), shapes.get(i).get_endpoint().getX())+ax+dx;
				double ep_y = Math.max(shapes.get(i).get_startpoint().getY(), shapes.get(i).get_endpoint().getY())+by+dy;
				new_start_point = new Point2D.Double(sp_x, sp_y);
				new_end_point = new Point2D.Double(ep_x, ep_y);
			}//System.out.println(m_start_point.toString()+new_start_point.toString());
			
			shapes.get(i).set_startpoint(new_start_point);
			shapes.get(i).set_endpoint(new_end_point);
			shapes.get(i).reset_shape();
		}
		double[] smallest = get_smallest_coordinate(shapes);
		m_start_point = new Point2D.Double(smallest[0],smallest[1]);
		//m_start_point = m_end_point;
		//if(is_changed()) System.out.println("changed");
	}
	
	public void rotate_shape(MouseEvent e) {
		
		ArrayList<ShapeObject> selected_shapes = this.get_selected_shapes();
		double center_x=0, center_y=0;
		for(int i=0; i<count_selected(); i++) {
			ShapeObject shapeobj = selected_shapes.get(i);
			center_x = shapeobj.get_shape().getBounds().getCenterX();
			center_y = shapeobj.get_shape().getBounds().getCenterY();
			double d_angle = get_theta(selected_shapes)-shapeobj.get_angle();
			AffineTransform at = new AffineTransform();
			at.rotate(d_angle, center_x, center_y);
			shapeobj.set_angle(get_theta(selected_shapes));
			shapeobj.set_shape(at.createTransformedShape(shapeobj.get_shape()));
			if(shapeobj.get_kind()==ShapeSelectionPanel.PEN) {
				//((Pen)shapeobj).reset_coordinates(d_angle, center_x, center_y);
			}
		}
	}
	public double get_theta(ArrayList<ShapeObject> selected_shapes) {
		double center_x=0, center_y =0;

		for(int i=0; i<count_selected(); i++) {
			ShapeObject shapeobj = selected_shapes.get(i);
			if(shapeobj.get_rotate_selected()) {
				center_x = shapeobj.get_shape().getBounds().getCenterX();
				center_y = shapeobj.get_shape().getBounds().getCenterY();
				break;
			}
		}
		double dx = m_end_point.getX() - center_x;
		double dy = m_end_point.getY() - center_y;
		double theta = Math.atan(Math.abs(dx)/Math.abs(dy));
		if(dx>=0 && dy>0) {
			return -theta;
		}
		else if(dx>=0 && dy<0) {
			return -Math.PI+theta;
		}
		else if(dx<0 && dy<0) {
			return -Math.PI-theta;
		}
		else {
			return -Math.PI*2+theta;
		}
//		//System.out.println("Theta: "+Math.toDegrees(theta));
//		return -theta;
	}
	
	public double[] get_smallest_coordinate(ArrayList<ShapeObject> selected_shapes) {
		double smallest_x = Math.min(selected_shapes.get(0).get_startpoint().getX(), selected_shapes.get(0).get_endpoint().getX());
		double smallest_y = Math.min(selected_shapes.get(0).get_startpoint().getY(), selected_shapes.get(0).get_endpoint().getY());
		for(int i=1; i<selected_shapes.size(); i++) {
			smallest_x = Math.min(smallest_x, Math.min(selected_shapes.get(i).get_startpoint().getX(), selected_shapes.get(i).get_endpoint().getX()));
			smallest_y = Math.min(smallest_y, Math.min(selected_shapes.get(i).get_startpoint().getY(), selected_shapes.get(i).get_endpoint().getY()));
		}
		return new double[] {smallest_x, smallest_y};
	}
	
	public double[] get_largest_coordinate(ArrayList<ShapeObject> selected_shapes) {
		double largest_x = Math.max(selected_shapes.get(0).get_startpoint().getX(), selected_shapes.get(0).get_endpoint().getX());
		double largest_y = Math.max(selected_shapes.get(0).get_startpoint().getY(), selected_shapes.get(0).get_endpoint().getY());
		for(int i=1; i<selected_shapes.size(); i++) {
			largest_x = Math.max(largest_x, Math.max(selected_shapes.get(i).get_startpoint().getX(), selected_shapes.get(i).get_endpoint().getX()));
			largest_y = Math.max(largest_y, Math.max(selected_shapes.get(i).get_startpoint().getY(), selected_shapes.get(i).get_endpoint().getY()));
		}
		return new double[] {largest_x, largest_y};
	}

	public boolean is_changed() {
		//MainFrame.ms_previous_list.removeLast();
		if(this.is_same(MainFrame.ms_previous_list.getLast())) {
			//System.out.println("is_same: "+this.is_same(MainFrame.ms_previous_list.getLast()));
			//System.out.println("now: "+this.hashCode()+" Previous: "+MainFrame.ms_previous_list.getLast().hashCode());
			return false;
		}
		//System.out.println("changed, add");
		MainScreen temp = new MainScreen(this);
		//System.out.println("temp hashcode: "+temp.hashCode());
		MainFrame.add_to_mainscreenlist(temp);
		return true;
	}
	
	public OptionFrame get_optionframe() {return of;}
	
	public static int get_width(Point2D sp, Point2D ep) {
		return (int)(ep.getX()-sp.getX());

	}
	public static int get_height(Point2D sp, Point2D ep) {
		return (int)(ep.getY()-sp.getY());
	}
	public static int[] convert_coordinate(int x, int y, int width, int height) {
		if(width>0 && height>0);
		else if(width>0 && height<0) {y += height; height = -height;}
		else if(width<0 && height>0) {x += width; width = -width;}
		else {x += width; y += height; width = - width; height = -height;}
		
		return new int[]{x,y,width,height};
	}
	
	public boolean is_same(MainScreen ms) {
		//System.out.printf("previous each: %d, current each: %d\n", this.shapes.size(), ms.shapes.size());
		if(this.shapes.size()!=ms.shapes.size()) return false;
		else {
			for(int i=0; i<this.shapes.size(); i++) { 
				if(!shapes.get(i).compare_shape(ms.shapes.get(i))) {
					//System.out.printf("%dth shapes same? %b\n", i, shapes.get(i).compare_shape(ms.shapes.get(i)));
					return false;

				}
			}
		}
		return true;
	}
	
	public void new_option_frame() {
		of.quit();
		of = new OptionFrame();
	}
}
