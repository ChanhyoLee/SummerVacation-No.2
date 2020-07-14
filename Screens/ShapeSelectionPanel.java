package Screens;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ShapeSelectionPanel extends JPanel implements ActionListener{
	
	public static final int SELECTION = -1;
	public static final int RECTANGLE = 0;
	public static final int OVAL = 1;
	public static final int LINE = 2;
	public static final int PEN = 3;
	
	public static final int EMPTY = 0;
	public static final int FILL = 1;
	private int mode = SELECTION;
	private int fill_mode = EMPTY;

	OptionFrame of;
	JButton pen = new JButton("Pen");
	JButton rect = new JButton("Rect - ▢");
	JButton oval = new JButton("Oval - ◯");
	JButton line = new JButton("Line - ▬");
	JButton fill = new JButton("Fill - ■");
	JButton empty = new JButton("Empty - □");
	JButton selection = new JButton("Select Tool");
	JButton[] shape_buttons =  new JButton[]{pen, rect, oval, line, fill, empty, selection};
	
	JPanel upper_panel = new JPanel();
	JPanel lower_panel = new JPanel();
	JPanel medium_panel = new JPanel();
	
	public ShapeSelectionPanel() {
		this.setLayout(new GridLayout(3,1));
		pen.addActionListener(this);
		rect.addActionListener(this);
		oval.addActionListener(this);
		line.addActionListener(this);
		fill.addActionListener(this);
		empty.addActionListener(this);
		empty.setForeground(Color.RED);
		selection.addActionListener(this);
		selection.setForeground(Color.RED);
		upper_panel.setLayout(new GridLayout(1,2));
		medium_panel.setLayout(new GridLayout(1,3));
		lower_panel.setLayout(new GridLayout(1,2));
		upper_panel.add(pen);
		upper_panel.add(selection);
		medium_panel.add(rect);
		medium_panel.add(oval);
		medium_panel.add(line);
		lower_panel.add(fill);
		lower_panel.add(empty);

		this.add(upper_panel);
		this.add(medium_panel);
		this.add(lower_panel);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==pen) {
			fill.setForeground(Color.BLACK);
			empty.setForeground(Color.BLACK);
			line.setForeground(Color.BLACK);
			rect.setForeground(Color.BLACK);
			oval.setForeground(Color.BLACK);
			pen.setForeground(Color.RED);
			selection.setForeground(Color.BLACK);
			set_mode(PEN);
		}
		else if(e.getSource()==rect) {
			rect.setForeground(Color.RED);
			line.setForeground(Color.BLACK);
			oval.setForeground(Color.BLACK);
			pen.setForeground(Color.BLACK);
			selection.setForeground(Color.BLACK);
			set_mode(RECTANGLE);	
		}
		else if(e.getSource()==oval) {
			rect.setForeground(Color.BLACK);
			line.setForeground(Color.BLACK);
			rect.setForeground(Color.BLACK);
			oval.setForeground(Color.RED);
			pen.setForeground(Color.BLACK);
			selection.setForeground(Color.BLACK);
			set_mode(OVAL);
		}
		else if(e.getSource()==line) {
			rect.setForeground(Color.BLACK);
			line.setForeground(Color.RED);
			oval.setForeground(Color.BLACK);
			pen.setForeground(Color.BLACK);
			fill.setForeground(Color.BLACK);
			empty.setForeground(Color.BLACK);
			selection.setForeground(Color.BLACK);
			set_mode(LINE);
		}
		else if(e.getSource()==fill) {
			empty.setForeground(Color.BLACK);
			fill.setForeground(Color.RED);
			set_fillmode(FILL);
		}
		else if(e.getSource()==empty) {
			empty.setForeground(Color.RED);
			fill.setForeground(Color.BLACK);
			set_fillmode(EMPTY);
		}
		else if(e.getSource()==selection) {
			pen.setForeground(Color.BLACK);
			fill.setForeground(Color.BLACK);
			empty.setForeground(Color.BLACK);
			line.setForeground(Color.BLACK);
			rect.setForeground(Color.BLACK);
			oval.setForeground(Color.BLACK);
			line.setForeground(Color.BLACK);
			selection.setForeground(Color.RED);

			set_mode(SELECTION);
		}

	}
	public int get_mode() {return mode;}
	public int get_fillmode() {return fill_mode;}
	
	public void set_mode(int x) {mode = x;}
	public void set_fillmode(int x) {fill_mode = x;}

}
