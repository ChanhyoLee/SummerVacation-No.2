package Shapes;

import java.awt.BasicStroke;
import java.awt.Color;

public class LineAttribute {
	private float line_width, line_miterlimit, line_dash_phase, line_dash[];
	private int line_cap, line_join;
	private Color line_color;
	
	public LineAttribute() {
		line_width = 3.0f;
		line_miterlimit = 1.0f;
		line_dash_phase = 0.0f;
		line_dash = null;
		line_cap = BasicStroke.CAP_SQUARE;
		line_join = BasicStroke.JOIN_MITER;
		line_color = Color.BLACK;
	}
	
	public LineAttribute(LineAttribute la) {
		line_width = la.get_width();
		line_miterlimit = la.get_miterlimit();
		line_dash_phase = la.get_dash_phase();
		line_dash = la.get_dash();
		line_cap = la.get_cap();
		line_join = la.get_join();
		line_color = la.get_color();
	}
	
	public float get_width() {return line_width;}
	public float get_miterlimit() {return line_miterlimit;}
	public float get_dash_phase() {return line_dash_phase;}
	public float[] get_dash() {return line_dash;}
	public int get_cap() {return line_cap;}
	public int get_join() {return line_join;}
	public Color get_color() {return line_color;}

	public void set_width(float x) {line_width = x;}
	public void set_miterlimit(float x) {line_miterlimit = x;}
	public void set_dash_phase(float x) {line_dash_phase = x;}
	public void set_dash(float[] x) {line_dash = x;}
	public void set_cap(int x) {line_cap = x;}
	public void set_join(int x) {line_join = x;}
	public void set_color(Color x) {line_color = x;}

}
