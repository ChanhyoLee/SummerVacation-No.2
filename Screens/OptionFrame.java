package Screens;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;

public class OptionFrame extends JFrame{
	
	static ShapeSelectionPanel shape_panel = new ShapeSelectionPanel();
	static StrokeSelectionPanel stroke_panel = new StrokeSelectionPanel();
	static ColorSelectionPanel color_panel = new ColorSelectionPanel();

	
	public OptionFrame() {
		this.setTitle("Option Frame");
		this.setLocation(600,0);
		this.setSize(300,300);
		this.setResizable(false);
		this.setLayout(new FlowLayout());
		this.getContentPane().add(shape_panel);
		this.getContentPane().add(stroke_panel);
		this.getContentPane().add(color_panel);
		this.setVisible(true);
	}
	
	public ShapeSelectionPanel get_shape_panel() {return shape_panel;}
	public StrokeSelectionPanel get_stroke_panel() {return stroke_panel;}
	public ColorSelectionPanel get_color_panel() {return color_panel;}
	public void quit() {this.dispose();}

}
