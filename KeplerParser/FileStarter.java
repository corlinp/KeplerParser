package KeplerParser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;

public class FileStarter {
	
	static String fileLoc = "Z:/gogogo";
	
	public static void main(String args[]){
		new FileStarter(fileLoc);
	}
	
	public FileStarter(String fileLocation){
		fileLoc = fileLocation;

		File[] files = new File(fileLoc).listFiles();
		if(files.length > 20){
			File[] fn = new File[20];
			for(int i = 0; i < 20; i++)
				fn[i] = files[i];
			files = fn;
		}
		String[] fileNames = new String[files.length];

		for (int i = 0; i < files.length; i++) {
			fileNames[i] = files[i].getName();
		}
		
		GridLayout gl = new GridLayout(files.length, 1);
		final JFrame fileButtons = new JFrame();
		fileButtons.setLayout(gl);
		for (String s : fileNames){
			final JButton jb = new JButton(s);
			jb.setBackground(Color.white);
			jb.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					File selected = new File(fileLoc + "/" + jb.getText());
					if(selected.isDirectory()){
						new FileStarter(fileLoc + "/" + jb.getText());
						fileButtons.dispose();
						return;
					}
					new KeplerParser( new Star(fileLoc + "/" + jb.getText()));
				}
			});
			jb.setPreferredSize(new Dimension(300,30));
			fileButtons.add(jb);
		}
		fileButtons.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		fileButtons.setSize(300,files.length*40);
		fileButtons.setVisible(true);
	}

}
