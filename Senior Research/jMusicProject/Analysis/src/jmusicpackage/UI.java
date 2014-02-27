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

	    if(input.equals("r")){
	    	File bpath = new File("Songs/Bach");
	    	File [] bfiles = bpath.listFiles();
	    	File cpath = new File("Songs/Chopin");
	    	File [] cfiles = cpath.listFiles();
	        for (int i = 0; i < bfiles.length && i < cfiles.length; i++){
	            if (bfiles[i].isFile()&&cfiles[i].isFile()){
	                Reader.buildLibrary(bfiles[i].getPath(),cfiles[i].getPath());
	                Reader.weightRhythms("trees.txt", "percents.txt");
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
		       //WholeSongAnalysisBigram.analyze(filePath,50);
		       WholeSongAnalysisBigram.analisisMethod2(filePath);
		    }
	    }
	    else if(input.equals("gr")){
	    	System.out.println("Enter Composer Name: ");
			input = scanIn.nextLine();
	    	File path = new File("Songs/"+input);
	    	File [] files = path.listFiles();
	    	for (int i = 0; i < files.length; i++){
	            if (files[i].isFile()){
	            	GeneralReader.read(files[i].getPath(),input);
	            }
	    	}
	    }
	    else{
	    	//default
	    }



	    scanIn.close();
	}

}