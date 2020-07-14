package Screens;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MyMenuBar extends JMenuBar implements ActionListener{

		private static MainScreen ms;
		JMenu graphiceditor;
		JMenu file;
		JMenu edit;
		JMenu help;
		
		JMenuItem graphiceditor_exit;
		JMenuItem file_newfile;
		JMenuItem edit_undo; JMenuItem edit_redo; JMenuItem edit_open_optionframe; JMenuItem edit_delete;
		JMenuItem help_welcome; JMenuItem help_contents;
		
		public MyMenuBar(MainScreen ms) {
			this.ms = ms;
			graphiceditor = new JMenu("Graphic Editor");
			file = new JMenu("File");
			edit = new JMenu("Edit");
			help = new JMenu("Help");

			graphiceditor_exit = new JMenuItem("Exit");
			file_newfile = new JMenuItem("New");
			edit_undo = new JMenuItem("Undo ⟲"); edit_redo = new JMenuItem("Redo ⟳"); 
			edit_open_optionframe = new JMenuItem("Option Frame"); edit_delete = new JMenuItem("Delete Object");
			help_welcome = new JMenuItem("Welcome"); help_contents = new JMenuItem("Help Contents");
			
			graphiceditor.add(graphiceditor_exit);
			file.add(file_newfile); 
			edit.add(edit_undo); edit.add(edit_redo); edit.add(edit_open_optionframe); edit.add(edit_delete);
			help.add(help_welcome); help.add(help_contents);
			
			this.add(graphiceditor); this.add(file); this.add(edit); this.add(help);
			
			graphiceditor_exit.addActionListener(this);
			file_newfile.addActionListener(this);
			edit_undo.addActionListener(this); edit_redo.addActionListener(this);
			edit_open_optionframe.addActionListener(this); edit_delete.addActionListener(this);
			help_welcome.addActionListener(this);; help_contents.addActionListener(this);			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource()==graphiceditor_exit) System.exit(0);
			else if(e.getSource()==file_newfile) MainFrame.new_screen();
			else if(e.getSource()==edit_undo) MainFrame.undo();
			else if(e.getSource()==edit_redo) MainFrame.redo();
			else if(e.getSource()==edit_open_optionframe) ms.new_option_frame();
			else if(e.getSource()==edit_delete) ms.delete_shape(ms.get_selected_shapes());
			else if(e.getSource()==help_welcome);
			else if(e.getSource()==help_contents);

		}
		
}
