package jmusicpackage;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.util.Scanner;
import java.io.File;

public class UI {

	//USAGE NOTES
	//use grt to run GeneralReaderTrigram to analyze a library for a composer
	//run this for every composer you're using
	//this results in several files appearing for each
	//from these, make a single file, head with the composer name on the first line
	//follow on the next line with the word "Pitch"
	//then copy over their pitch percents on the following line
	//then type "Rhythm" on the next line
	//and copy over their rhythm percents
	//on the following line type the next composer name and repeat above

	//two things then need to be done on AnalizerTrigram to set it up
	//first change the path of library percent storage to match the location
	//of the file you just made above
	//Then alter artist 1-4 to match the composer names you used in the file above
	//and in the order you inserted them into the file

	//After this, you can now use w to run WholeSongAnalysisBigram, which is
	//currently set up to actually use its trigram method. This will let you pick
	//an individual file to compare against the percents you generated.
	//This will output the results.txt file which lists combined, pitch, and rhythm
	//likelihoods for every composer in that order.

	//Or you can configure b to point at a folder of songs (or if you set up like i did
	//make the folders "Songs/[composer] Test Songs" in the analysis java project)
	//it will then analyze the results files for each and compose all of them into a single file
	//naming who it picked for combined, only pitch, and only rhythm,
	//it will also output hit/miss rates for everything as well.

	public static void main(String[] args) {

		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Midi files", "mid");
	    chooser.setFileFilter(filter);

		System.out.println("Enter operation (grt/w/b): ");

		Scanner scanIn = new Scanner(System.in);
		String input = scanIn.nextLine();
		input = input.toLowerCase();
		//old read method, use gr now
		/*
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
	    */
	    //really old, use b or w
		/*
	    else if(input.equals("s")){
		    int returnVal = chooser.showOpenDialog(null);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       String filePath = chooser.getSelectedFile().getAbsolutePath();
		       SinglePhraseSelector.read(filePath, 100, 1, 0, 0);
		    }
	    }
	    */
	    //use for single song, use b for a folder of songs
	    if(input.equals("w")){
		    int returnVal = chooser.showOpenDialog(null);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	//choose your song with this
		       String filePath = chooser.getSelectedFile().getAbsolutePath();
		       WholeSongAnalysisBigram.analisisTrigramMethod(filePath);
		    }
	    }
	    //calls the GeneralReader class to analyze a library of music
	    //only does bigrams
	    /*
	    else if(input.equals("gr")){
	    	System.out.println("Enter Composer Name: ");
			input = scanIn.nextLine();
			//path just needs to point at the folder containing
			//the library of songs for a composer. Was set up this ways
			//just from how I organized music into the folder "Songs/[composer]"
	    	File path = new File("Songs/"+input);
	    	File [] files = path.listFiles();
	    	for (int i = 0; i < files.length; i++){
	            if (files[i].isFile()){
	            	GeneralReader.read(files[i].getPath(),input);
	            }
	    	}
	    }
	    */
	    //tests a batch of music against the data from libraries
	    else if(input.equals("b")){
	    	System.out.println("Enter Composer Name: ");
	    	input = scanIn.nextLine();
	    	File path = new File("Songs/"+input+" Test Songs");
	    	File[] files = path.listFiles();
	    	BatchTester.test(files, input);
	    }
	    //calls GeneralReaderTrigram to analyze a library
	    else if(input.equals("grt")){
	    	System.out.println("Enter Composer Name: ");
			input = scanIn.nextLine();
			//path just needs to point at the folder containing
			//the library of songs for a composer. Was set up this ways
			//just from how I organized music into the folder "Songs/[composer]"
	    	File path = new File("Songs/"+input);
	    	File [] files = path.listFiles();
	    	for (int i = 0; i < files.length; i++){
	            if (files[i].isFile()){
	            	GeneralReaderTrigram.read(files[i].getPath(),input);
	            }
	    	}
	    }
	    else{
	    	//default
	    }



	    scanIn.close();
	}

}
