package jmusicpackage;

import java.io.*;
import java.util.*;

public class BatchTester {

	public static void test(File[] files, String correctArtist){
		//arrange these to match their order in the library file being used
		String ARTISTNAME1 = "Bach";
		String ARTISTNAME2 = "Chopin";
		String ARTISTNAME3 = "Mozart";
		String ARTISTNAME4 = "Byrd";

		File totals = new File("batchResults.txt");
		int hitCounter = 0;
		int hitCounterP = 0;
		int hitCounterR = 0;
		for(int i = 0; i < files.length; i++){

			//bigram method
			//WholeSongAnalysisBigram.analisisMethod2(files[i].getAbsolutePath());
			//trigram method
			WholeSongAnalysisBigram.analisisTrigramMethod(files[i].getAbsolutePath());

			File results = new File("results.txt");
			try{
				if(results.exists()){
					Scanner fileScanner = new Scanner(results);
					double artist1 = fileScanner.nextDouble();
					double artist1p = fileScanner.nextDouble();
					double artist1r = fileScanner.nextDouble();
					double artist2 = fileScanner.nextDouble();
					double artist2p = fileScanner.nextDouble();
					double artist2r = fileScanner.nextDouble();
					double artist3 = -Double.MAX_VALUE;
					double artist3p = -Double.MAX_VALUE;
					double artist3r = -Double.MAX_VALUE;
					if(fileScanner.hasNextDouble()){
						artist3 = fileScanner.nextDouble();
						artist3p = fileScanner.nextDouble();
						artist3r = fileScanner.nextDouble();
					}
					double artist4 = -Double.MAX_VALUE;
					double artist4p = -Double.MAX_VALUE;
					double artist4r = -Double.MAX_VALUE;
					if(fileScanner.hasNextDouble()){
						artist4 = fileScanner.nextDouble();
						artist4p = fileScanner.nextDouble();
						artist4r = fileScanner.nextDouble();
					}
					fileScanner.close();
					//results.delete();

					BufferedWriter writer = new BufferedWriter(new FileWriter(totals, true));
					//overall
					if(artist1>artist2 && artist1>artist3 && artist1>artist4){
						if(correctArtist.equals(ARTISTNAME1)) hitCounter++;
						writer.write(files[i].getPath()+"\nOverall: "+ARTISTNAME1+"\n");
					}
					else if(artist2>artist1 && artist2>artist3 && artist2>artist4){
						if(correctArtist.equals(ARTISTNAME2)) hitCounter++;
						writer.write(files[i].getPath()+"\nOverall: "+ARTISTNAME2+"\n");
					}
					else if(artist3>artist1 && artist3>artist2 && artist3>artist4){
						if(correctArtist.equals(ARTISTNAME3)) hitCounter++;
						writer.write(files[i].getPath()+"\nOverall: "+ARTISTNAME3+"\n");
					}
					else if(artist4>artist1 && artist4>artist2 && artist4>artist3){
						if(correctArtist.equals(ARTISTNAME4)) hitCounter++;
						writer.write(files[i].getPath()+"\nOverall: "+ARTISTNAME4+"\n");
					}
					else writer.write(files[i].getPath()+"\nOverall: Tie Occured\n");

					//by pitch
					if(artist1p>artist2p && artist1p>artist3p && artist1p>artist4p){
						if(correctArtist.equals(ARTISTNAME1)) hitCounterP++;
						writer.write("Pitch only: "+ARTISTNAME1+"\n");
					}
					else if(artist2p>artist1p && artist2p>artist3p && artist2p>artist4p){
						if(correctArtist.equals(ARTISTNAME2)) hitCounterP++;
						writer.write("Pitch only: "+ARTISTNAME2+"\n");
					}
					else if(artist3p>artist1p && artist3p>artist2p && artist3p>artist4p){
						if(correctArtist.equals(ARTISTNAME3)) hitCounterP++;
						writer.write("Pitch only: "+ARTISTNAME3+"\n");
					}
					else if(artist4p>artist1p && artist4p>artist2p && artist4p>artist3p){
						if(correctArtist.equals(ARTISTNAME4)) hitCounterP++;
						writer.write("Pitch only: "+ARTISTNAME4+"\n");
					}
					else writer.write("Pitch only: Tie Occured\n");

					//by rhythm
					if(artist1r>artist2r && artist1r>artist3r && artist1r>artist4r){
						if(correctArtist.equals(ARTISTNAME1)) hitCounterR++;
						writer.write("Rhythm only: "+ARTISTNAME1+"\n");
					}
					else if(artist2r>artist1r && artist2r>artist3r && artist2r>artist4r){
						if(correctArtist.equals(ARTISTNAME2)) hitCounterR++;
						writer.write("Rhythm only: "+ARTISTNAME2+"\n");
					}
					else if(artist3r>artist1r && artist3r>artist2r && artist3r>artist4r){
						if(correctArtist.equals(ARTISTNAME3)) hitCounterR++;
						writer.write("Rhythm only: "+ARTISTNAME3+"\n");
					}
					else if(artist4r>artist1r && artist4r>artist2r && artist4r>artist3r){
						if(correctArtist.equals(ARTISTNAME4)) hitCounterR++;
						writer.write("Rhythm only: "+ARTISTNAME4+"\n");
					}
					else writer.write("Rhythm only: Tie Occured\n");


					//hit miss sections
					if(i == files.length-1){
						writer.write("Overall Hit/Miss rate: "+hitCounter+"/"+files.length+"\n");
						writer.write("Pitch Hit/Miss rate: "+hitCounterP+"/"+files.length+"\n");
						writer.write("Rhythm Hit/Miss rate: "+hitCounterR+"/"+files.length+"\n");
					}
					writer.close();
				}
			}
			catch(FileNotFoundException e){}
	        catch(IOException e){}
		}
	}

}
