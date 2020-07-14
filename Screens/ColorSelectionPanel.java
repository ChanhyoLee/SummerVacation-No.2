package Screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

public class ColorSelectionPanel extends JPanel implements ActionListener{
	OptionFrame of;
	JButton color = new JButton("Color");
	Color current_color = Color.BLACK;
	
	public ColorSelectionPanel() {
		this.setLayout(new BorderLayout());
		color.addActionListener(this);
		color.setSize(400,50);
		this.add(color, "Center");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Color selectedColor = JColorChooser.showDialog(null,"Color",Color.YELLOW);
		current_color = selectedColor;
		color.setForeground(selectedColor);
		//System.out.println(current_color.toString());
	}
	
	public Color get_color() {return current_color;}
	
}
