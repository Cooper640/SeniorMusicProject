package jmusicpackage;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.util.Scanner;
import java.io.File;

public class UI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Midi files", "mid");
	    chooser.setFileFilter(filter);

		System.out.println("Enter operation (r/s/w): ");

		Scanner scanIn = new Scanner(System.in);
		String input = scanIn.nextLine();
		input = input.toLowerCase();

	    scanIn.close();

	    if(input.equals("r")){
	    	File bpath = new File("Songs/Bach");
	    	File [] bfiles = bpath.listFiles();
	    	File cpath = new File("Songs/Chopin");
	    	File [] cfiles = cpath.listFiles();
	        for (int i = 0; i < bfiles.length && i < cfiles.length; i++){
	            if (bfiles[i].isFile()&&cfiles[i].isFile()){
	                Reader.buildLibrary(bfiles[i].getPath(),cfiles[i].getPath());
	            }
	        }
	    }
	    else if(input.equals("s")){
		    int returnVal = chooser.showOpenDialog(null);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       String filePath = chooser.getSelectedFile().getAbsolutePath();
		       SinglePhraseSelector.read(filePath, 100, 1, 0, 0);
		    }
	    }
	    else if(input.equals("w")){
		    int returnVal = chooser.showOpenDialog(null);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       String filePath = chooser.getSelectedFile().getAbsolutePath();
		       //WholeSongAnalysis analizer = new WholeSongAnalysis();
		       WholeSongAnalysis.analyze(filePath,100);
		    }
	    }
	    else{
	    	//default
	    }
	}

}
