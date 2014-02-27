package jmusicpackage;

import java.io.*;
import java.util.*;

import jm.JMC;
import jm.music.data.*;
import jm.music.tools.*;
import jm.util.*;

public class MusicGeneration implements JMC{

	public static void main(String[] args){

		int SONGLENGTH = 32;
		int STARTINGPITCH = 72;

		TreeMap<Integer, Double> bPercentPitchTree = new TreeMap<Integer, Double>();
	    TreeMap<Double, Double> bPercentRhythmTree = new TreeMap<Double, Double>();

	    TreeMap<Integer, Double> cPercentPitchTree = new TreeMap<Integer, Double>();
	    TreeMap<Double, Double> cPercentRhythmTree = new TreeMap<Double, Double>();

		File libraryPercentStorage = new File("Test 3 Results/Library Percents.txt");
		try{
			if(libraryPercentStorage.exists()){
				Scanner fileScanner = new Scanner(libraryPercentStorage);
				boolean bach = false;
				boolean chopin = false;
				boolean pitch = false;
				boolean rhythm = false;
				while(fileScanner.hasNext()){
					String input=fileScanner.nextLine();
					Scanner stringParser = new Scanner(input);
					double key = 0.0;
					double value = 0.0;
					String currentString = stringParser.next();
					if(!stringParser.hasNext()){
						if(currentString.equals("Bach")){
							bach = true;
							chopin = false;
						}
						else if(currentString.equals("Chopin")){
							bach = false;
							chopin = true;
						}
						else if(currentString.equals("Pitches")){
							pitch = true;
							rhythm = false;
						}
						else if(currentString.equals("Rhythm")){
							pitch = false;
							rhythm = true;
						}
					}
					else {
						double temp = Double.parseDouble(currentString);
						key = temp;
						currentString = stringParser.next();
						temp = Double.parseDouble(currentString);
						value = temp;
						if(bach && pitch){
							bPercentPitchTree.put((int)key,value);
						}
						else if(bach && rhythm){
							bPercentRhythmTree.put(key,value);
						}
						else if(chopin && pitch){
							cPercentPitchTree.put((int)key,value);
						}
						else if(chopin && rhythm){
							cPercentRhythmTree.put(key,value);
						}
					}
					stringParser.close();
				}
				fileScanner.close();
			}
    	}
    	catch(FileNotFoundException e){}

		//make song
		Note[] bachNotes = new Note[SONGLENGTH];
		Note[] chopinNotes = new Note[SONGLENGTH];
		int currentBachPitch = STARTINGPITCH;
		int currentChopinPitch = STARTINGPITCH;
		for(int i = 0; i<SONGLENGTH; i++){
			//bach
	    	Iterator<Integer> bachPitchIterator = bPercentPitchTree.keySet().iterator();
	        Iterator<Double> bachRhythmIterator = bPercentRhythmTree.keySet().iterator();
	        int bPitch = 0;
	        int selecter = (int)(Math.random()*100);
	        while(bachPitchIterator.hasNext() && selecter>0){
	        	bPitch = bachPitchIterator.next();
	        	selecter -= bPercentPitchTree.get(bPitch);
	        }

	        double bRhythm = 0.0;
	        selecter = (int)(Math.random()*100);
	        while(bachRhythmIterator.hasNext() && selecter>0){
	        	bRhythm = bachRhythmIterator.next();
	        	selecter -= bPercentRhythmTree.get(bRhythm);
	        }

	        System.out.println(bPitch);
	        System.out.println(bRhythm);
	        if(i == 0) bachNotes[i]= new Note(STARTINGPITCH, bRhythm);
	        else{
	        	bachNotes[i]= new Note(Math.abs(currentBachPitch+bPitch)%127, bRhythm);
	        	currentBachPitch = Math.abs(currentBachPitch+bPitch)%127;
	        }

	        //chopin
	        Iterator<Integer> chopinPitchIterator = cPercentPitchTree.keySet().iterator();
	        Iterator<Double> chopinRhythmIterator = cPercentRhythmTree.keySet().iterator();
	        int cPitch = 0;
	        selecter = (int)(Math.random()*100);
	        while(chopinPitchIterator.hasNext() && selecter>0){
	        	cPitch = chopinPitchIterator.next();
	        	selecter -= cPercentPitchTree.get(cPitch);
	        }

	        double cRhythm = 0.0;
	        selecter = (int)(Math.random()*100);
	        while(chopinRhythmIterator.hasNext() && selecter>0){
	        	cRhythm = chopinRhythmIterator.next();
	        	selecter -= cPercentRhythmTree.get(cRhythm);
	        }

	        System.out.println(cPitch);
	        System.out.println(cRhythm);
	        if(i==0) chopinNotes[i]= new Note(STARTINGPITCH, cRhythm);
	        else{
	        	chopinNotes[i]= new Note(Math.abs(currentChopinPitch+cPitch)%127, cRhythm);
	        	currentChopinPitch = Math.abs(currentChopinPitch+cPitch)%127;
	        }
		}
		//build and write the bach piece from notes
		Phrase bachPhrase = new Phrase(bachNotes);
		Part bachPart = new Part();
		bachPart.addPhrase(bachPhrase);
		Score bachScore = new Score("Bach");
		bachScore.addPart(bachPart);
		Write.midi(bachScore,"Bach From Library.mid");
		//build and write the chopin piece from notes
		Phrase chopinPhrase = new Phrase(chopinNotes);
		Part chopinPart = new Part();
		chopinPart.addPhrase(chopinPhrase);
		Score chopinScore = new Score("Chopin");
		chopinScore.addPart(chopinPart);
		Write.midi(chopinScore,"Chopin From Library.mid");

	}

}