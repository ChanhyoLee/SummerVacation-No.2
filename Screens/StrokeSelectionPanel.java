package Screens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import Shapes.LineAttribute;

public class StrokeSelectionPanel extends JPanel implements ActionListener{
	String style="";
	String thick="";
	OptionFrame of;
	Stroke myStroke = new BasicStroke(3); 
	public static final float SLIM = 1.0f;
	public static final float MEDIUM = 3.0f;
	public static final float THICK = 5.0f;
	public static final float[] SOLID = null;
	public static final float[] DOTTED = {10};
	public static final float[] DOUBLEDOTTED = {30};

	LineAttribute lineattribute = new LineAttribute();
	JButton style1 = new JButton("SOLID");
	JButton style2 = new JButton("DOTTED1");
	JButton style3 = new JButton("DOTTED2");
	JButton width1 = new JButton("SLIM");
	JButton width2 = new JButton("MIDIUM");
	JButton width3 = new JButton("THICK");
	JButton[][] stroke_buttons = new JButton[][]{{style1, style2, style3},{width1, width2, width3}}; 
	
	public StrokeSelectionPanel() {
		this.setSize(300,100);
		this.setLayout(new GridLayout(2,3));
		style1.addActionListener(this);
		style1.setForeground(Color.RED);
		style2.addActionListener(this);
		style3.addActionListener(this);
		width1.addActionListener(this);
		width2.addActionListener(this);
		width2.setForeground(Color.RED);
		width3.addActionListener(this);	
		this.add(style1); this.add(style2); this.add(style3); this.add(width1); this.add(width2); this.add(width3);
	}
	
	public String get_style() {return style;}
	public String get_thick() {return thick;}
	public LineAttribute get_lineattribute() {return lineattribute;}
	public Stroke get_stroke() {return myStroke;}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==style1) {
			style1.setForeground(Color.RED);
			style2.setForeground(Color.BLACK);
			style3.setForeground(Color.BLACK);			
			lineattribute.set_dash(SOLID);
		}
		else if(e.getSource()==style2) {
			style1.setForeground(Color.BLACK);
			style2.setForeground(Color.RED);
			style3.setForeground(Color.BLACK);	
			lineattribute.set_dash(DOTTED);
			lineattribute.set_dash_phase(2.0f);
		}
		else if(e.getSource()==style3) {
			style1.setForeground(Color.BLACK);
			style2.setForeground(Color.BLACK);
			style3.setForeground(Color.RED);	
			lineattribute.set_dash(DOUBLEDOTTED);
			lineattribute.set_dash_phase(4.0f);
		}
		else if(e.getSource()==width1) {
			width1.setForeground(Color.RED);
			width2.setForeground(Color.BLACK);
			width3.setForeground(Color.BLACK);
			lineattribute.set_width(SLIM);
		}
		else if(e.getSource()==width2) {
			width1.setForeground(Color.BLACK);
			width2.setForeground(Color.RED);
			width3.setForeground(Color.BLACK);

			lineattribute.set_width(MEDIUM);
		}
		else {
			width1.setForeground(Color.BLACK);
			width2.setForeground(Color.BLACK);
			width3.setForeground(Color.RED);
			lineattribute.set_width(THICK);
		}
		myStroke = new BasicStroke(lineattribute.get_width(), lineattribute.get_cap(), lineattribute.get_join(), lineattribute.get_miterlimit(), lineattribute.get_dash(), lineattribute.get_dash_phase());
	}
}
