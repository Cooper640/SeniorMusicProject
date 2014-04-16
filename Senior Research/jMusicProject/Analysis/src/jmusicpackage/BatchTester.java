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
		for(int i = 0; i < files.length; i++){

			//bigram method
			//WholeSongAnalysisBigram.analisisMethod2(files[i].getAbsolutePath());
			//trigram method *experimental*
			WholeSongAnalysisBigram.analisisTrigramMethod(files[i].getAbsolutePath());

			File results = new File("results.txt");
			try{
				if(results.exists()){
					Scanner fileScanner = new Scanner(results);
					double artist1 = fileScanner.nextDouble();
					double artist2 = fileScanner.nextDouble();
					double artist3 = -Double.MAX_VALUE;
					if(fileScanner.hasNextDouble()) artist3 = fileScanner.nextDouble();
					double artist4 = -Double.MAX_VALUE;
					if(fileScanner.hasNextDouble()) artist4 = fileScanner.nextDouble();
					fileScanner.close();
					//results.delete();

					BufferedWriter writer = new BufferedWriter(new FileWriter(totals, true));
					if(artist1>artist2 && artist1>artist3 && artist1>artist4){
						if(correctArtist.equals(ARTISTNAME1)) hitCounter++;
						writer.write(files[i].getPath()+": "+ARTISTNAME1+"\n");
					}
					else if(artist2>artist1 && artist2>artist3 && artist2>artist4){
						if(correctArtist.equals(ARTISTNAME2)) hitCounter++;
						writer.write(files[i].getPath()+": "+ARTISTNAME2+"\n");
					}
					else if(artist3>artist1 && artist3>artist2 && artist3>artist4){
						if(correctArtist.equals(ARTISTNAME3)) hitCounter++;
						writer.write(files[i].getPath()+": "+ARTISTNAME3+"\n");
					}
					else if(artist4>artist1 && artist4>artist2 && artist4>artist3){
						if(correctArtist.equals(ARTISTNAME4)) hitCounter++;
						writer.write(files[i].getPath()+": "+ARTISTNAME4+"\n");
					}
					else writer.write(files[i].getPath()+": Tie Occured\n");
					//ties should pretty much never happen
					if(i == files.length-1) writer.write("Hit/Miss rate: "+hitCounter+"/"+files.length+"\n");
					writer.close();
				}
			}
			catch(FileNotFoundException e){}
	        catch(IOException e){}
		}
	}

}
