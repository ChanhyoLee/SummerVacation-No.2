package Screens;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MainFrame {
	static MainScreen ms;
	static JFrame f = new JFrame("Grahics Editor");
	static LinkedList<MainScreen> ms_previous_list = new LinkedList<MainScreen>();
	static LinkedList<MainScreen> ms_forward_list = new LinkedList<MainScreen>();
	
	public static void main(String[] args){
		MainFrame myFrame = new MainFrame();
		ms = new MainScreen();
		ms_previous_list.add(new MainScreen(ms));
		//System.out.println(ms.hashCode());
		MainFrame.run();
	}
	
	public static void run() {
		f.setSize(600,600);
		Container contentPane = f.getContentPane();
		contentPane.setLayout(new BorderLayout());
		//ms_previous_list.add(ms);
		contentPane.add(ms, BorderLayout.CENTER);
		contentPane.add(new MyMenuBar(ms), BorderLayout.NORTH);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		/*f.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) { System.exit(0); }
		});*/
		f.setVisible(true);
		f.repaint();
	}
	
	public static void new_screen() {
		ms.get_optionframe().quit();
		f.remove(ms);
		ms = new MainScreen();
		remove_all_list(ms_previous_list);
		remove_all_list(ms_forward_list);
//		ms_previous_list.removeAll(ms_previous_list);
//		ms_forward_list.removeAll(ms_forward_list);
		ms_previous_list.add(ms);
		run();
	}
	
	public static void undo() {
		//ms.get_optionframe().quit();
		if(ms_previous_list.size()>=2) {
			ms.get_optionframe().quit();
			f.remove(ms);
			ms_forward_list.add(ms_previous_list.removeLast());
			MainScreen new_ms = new MainScreen(ms_previous_list.getLast());
			//System.out.println("inside undo: "+new_ms.hashCode());
			ms = new_ms;
			ms.new_option_frame();
			run();
		}
		else {
		}
	}
	public static void redo() {
		//ms.get_optionframe().quit();
		if(ms_forward_list.size()>=1) {
			f.remove(ms);
			//ms_previous_list.add(ms_forward_list.removeLast());
			//System.out.println(ms_forward_list.size());
			MainScreen new_ms = new MainScreen(ms_forward_list.removeLast());
			ms_previous_list.add(new_ms);
			//System.out.println("inside redo: "+new_ms.hashCode());
			ms = new_ms;
			ms.new_option_frame();
			run();
		}
		else {
		}
	}
	public static void quit_screen() {
		
	}
	public static void add_to_mainscreenlist(MainScreen ms) {
		//System.out.println("add_to_mainscreenlist");
		//System.out.println("ms hashcode: "+ms.hashCode());
		ms_previous_list.add(ms);
		remove_all_list(ms_forward_list);
		//ms_forward_list.removeAll(ms_forward_list);
	}
	public static void remove_all_list(LinkedList<MainScreen> ms_list) {
		//System.out.println("remove_all_list");
		//System.out.println("size: "+ms_list.size());
		//System.out.println("list empty?"+ms_list.isEmpty());
		while(!ms_list.isEmpty()) {
			//System.out.println("size: "+ms_list.size());
			ms_list.removeLast();
		}
	}
}
