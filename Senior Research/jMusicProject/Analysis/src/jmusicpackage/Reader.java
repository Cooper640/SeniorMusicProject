package jmusicpackage;

import java.io.*;
import java.util.*;

import jm.JMC;
import jm.music.data.*;
import jm.music.tools.*;
import jm.util.*;
//import jm.gui.*;
//import jm.gui.sketch.SketchScore;

public class Reader implements JMC {

	static int bPitchTotal = 0;
	static int bRhythmTotal = 0;
    static int cPitchTotal = 0;
    static int cRhythmTotal = 0;

    static boolean useWeighted = false;


	static String bachSongName = "Songs/Bach/cs2-4.mid";
	static String chopinSongName = "Songs/Chopin/chpn-p10.mid";

    public static void main(String[] args){
    	//Bach-CelloSuiteNo1Prelude.mid
    	//chopin_p15(raindrop).mid

    	TreeMap<Integer, Integer> pitchTree = new TreeMap<Integer, Integer>();
        TreeMap<Double, Integer> rhythmTree = new TreeMap<Double, Integer>();

        TreeMap<Integer, Integer> cPitchTree = new TreeMap<Integer, Integer>();
        TreeMap<Double, Integer> cRhythmTree = new TreeMap<Double, Integer>();

    	File treeStorage = new File("trees.txt");
    	try{
			if(treeStorage.exists()){
				Scanner fileScanner = new Scanner(treeStorage);
				int totalNumber = 0;
				while(fileScanner.hasNext()){
					String input=fileScanner.nextLine();
					Scanner stringParser = new Scanner(input);
					double key = 0.0;
					int value = 0;
					int numberOfNumbers = 0;
					while(stringParser.hasNext()){
						double temp = Double.parseDouble(stringParser.next());
						if(numberOfNumbers == 0){
							key = temp;
						}
						else if(numberOfNumbers == 1){
							value = (int)temp;
						}
						numberOfNumbers++;
					}
					if(numberOfNumbers == 1){
						if(totalNumber == 0){
							Reader.bPitchTotal = (int)key;
							totalNumber++;
						}
						else if(totalNumber == 1){
							Reader.bRhythmTotal = (int)key;
							totalNumber++;
						}
						else if(totalNumber == 2){
							Reader.cPitchTotal = (int)key;
							totalNumber++;
						}
						else if(totalNumber == 3){
							Reader.cRhythmTotal = (int)key;
							totalNumber++;
						}
					}
					else if(numberOfNumbers == 2){
						if(totalNumber == 1){
							pitchTree.put((int)key,value);
						}
						else if(totalNumber == 2){
							rhythmTree.put(key,value);
						}
						else if(totalNumber == 3){
							cPitchTree.put((int)key,value);
						}
						else if(totalNumber == 4){
							cRhythmTree.put(key,value);
						}
					}
				}
				fileScanner.close();
			}
    	}
    	catch(FileNotFoundException e){}

        Score song = new Score();

        Read.midi(song, bachSongName);
        //System.out.println(song.toString());
        Part[] songParts = song.getPartArray();
        Phrase[][] maryPhrases = new Phrase[song.getSize()][];
        for(int i = 0; i < song.getSize(); i++){
        	maryPhrases[i] = songParts[i].getPhraseArray();
        	for(int j=0; j<songParts[i].getSize(); j++){

        		//pitch relative to key
        		int keysig = 0;
        		int[] scale = MAJOR_SCALE;
        		int sharpsflats = song.getKeySignature();
        		System.out.println(sharpsflats+" "+song.getKeyQuality());
        		if(sharpsflats<0){
        			if(sharpsflats==-1)
        			{
        				keysig=5;
        				scale[6]--;
        			}
        			else if(sharpsflats==-2){
        				keysig=10;
        				scale[6]--;
        				scale[2]--;
        			}
        			else if(sharpsflats==-3){
        				keysig=3;
        				scale[6]--;
        				scale[2]--;
        				scale[5]--;
        			}
        			else if(sharpsflats==-4){
        				keysig=8;
        				scale[6]--;
        				scale[2]--;
        				scale[5]--;
        				scale[1]--;
        			}
        			else if(sharpsflats==-5){
        				keysig=1;
        				scale[6]--;
        				scale[2]--;
        				scale[5]--;
        				scale[1]--;
        				scale[4]--;
        			}
        			else if(sharpsflats==-6){
        				keysig=6;
        				scale[6]--;
        				scale[2]--;
        				scale[5]--;
        				scale[1]--;
        				scale[4]--;
        				scale[0]=11;
        			}
        			else if(sharpsflats==-7){
        				keysig=11;
        				scale[6]--;
        				scale[2]--;
        				scale[5]--;
        				scale[1]--;
        				scale[4]--;
        				scale[0]=11;
        				scale[3]--;
        			}
        		}
        		else if(sharpsflats>0){
        			if(sharpsflats==1){
        				keysig=7;
        				scale[3]++;
        			}
        			else if(sharpsflats==2){
        				keysig=2;
        				scale[0]++;
        				scale[3]++;
        			}
        			else if(sharpsflats==3){
        				keysig=9;
        				scale[4]++;
        				scale[0]++;
        				scale[3]++;
        			}
        			else if(sharpsflats==4){
        				keysig=4;
        				scale[1]++;
        				scale[4]++;
        				scale[0]++;
        				scale[3]++;
        			}
        			else if(sharpsflats==5){
        				keysig=11;
        				scale[5]++;
        				scale[1]++;
        				scale[4]++;
        				scale[0]++;
        				scale[3]++;
        			}
        			else if(sharpsflats==6){
        				keysig=6;
        				scale[2]++;
        				scale[5]++;
        				scale[1]++;
        				scale[4]++;
        				scale[0]++;
        				scale[3]++;
        			}
        			else if(sharpsflats==7){
        				keysig=1;
        				scale[6]++;
        				scale[2]++;
        				scale[5]++;
        				scale[1]++;
        				scale[4]++;
        				scale[0]++;
        				scale[3]++;
        			}
        		}
        		Note[] notes = maryPhrases[i][j].getNoteArray();
        		for(int m = 0; m<notes.length; m++){
        			int temp = notes[m].getPitch();
        			while(temp>12){
        				temp=temp-12;
        			}
        			int middlescalevalue = notes[m].getPitchValue();
        			boolean inKey = notes[m].isScale(scale);
        			int scaleDegree = 0;
        			if(inKey){
	        			for(int n=0; n<scale.length; n++){
	        				if(scale[n]==temp) scaleDegree = n;
	        			}
        			}
        			int distance = Math.abs(temp-keysig);
        			System.out.println(temp+" "+inKey+" "+distance+" "+scaleDegree);
        		}

        		//Attempt to use the get all statistics phrase analysis tool
        		/*
        		String[] stats = PhraseAnalysis.getAllStatisticsAsStrings(maryPhrases[i][j], maryPhrases[i][j].getBeatLength(), keysig, scale);
        		String bachtitle = bachSongName.substring(bachSongName.lastIndexOf('\\')+1, bachSongName.lastIndexOf('.'));
        		try{
        			File bachDestination = new File("Songs/Bach song stats/"+bachtitle+"statistics.txt");
        			BufferedWriter writer = new BufferedWriter(new FileWriter(bachDestination,true));
        			writer.write(String.valueOf(maryPhrases[i][j].getSize()) + " ");
        			for (int m=0; m<stats.length; m++){
        				if(m!=stats.length-1) writer.write(stats[m]+" ");
        				else writer.write(stats[m]+"\n");
        			}
        			writer.close();
        		}
        		catch(IOException e){}
        		*/


        		int[] pitchintervals = PhraseAnalysis.pitchIntervals(maryPhrases[i][j]);
        		//System.out.println("Pitch intervals");
        		if(pitchintervals.length!=0){
        			Reader.bPitchTotal+=pitchintervals.length;
        			//System.out.println("Part " + i + ": Phrase " + j);
        			for(int n=0; n<pitchintervals.length; n++){
        				//System.out.println(pitchintervals[n]);
        				if(pitchTree.get(pitchintervals[n]) == null){
        					pitchTree.put(pitchintervals[n], 1);
        				}
        				else{
        					int pitchCount = pitchTree.get(pitchintervals[n]);
        					pitchTree.put(pitchintervals[n], pitchCount+1);
        				}
        			}
        		}
        		else{
        			//System.out.println("Part " + i + ": Phrase " + j);
        			//System.out.println("no intervals");
        		}
        		double[] rhythmArray = maryPhrases[i][j].getRhythmArray();
        		//System.out.println("Rhythm values");
        		if(rhythmArray.length!=0){
        			Reader.bRhythmTotal+=rhythmArray.length;
        			//System.out.println("Part " + i + ": Phrase " + j);
        			for(int n=0; n<rhythmArray.length; n++){
        				//System.out.println(rhythmArray[n]);
        				if(rhythmTree.get(rhythmArray[n]) == null){
        					rhythmTree.put(rhythmArray[n], 1);
        				}
        				else{
        					int rhythmCount = rhythmTree.get(rhythmArray[n]);
        					rhythmTree.put(rhythmArray[n], rhythmCount+1);
        				}
        			}
        		}
        		else{
        			//System.out.println("Part " + i + ": Phrase " + j);
        			//System.out.println("no notes");
        		}

        	}
        }

        //View.sketch(song);

        Score cSong = new Score();

        Read.midi(cSong, chopinSongName);
        //System.out.println(song.toString());
        Part[] cSongParts = cSong.getPartArray();
        Phrase[][] chopinPhrases = new Phrase[cSong.getSize()][];
        for(int i = 0; i < cSong.getSize(); i++){
        	chopinPhrases[i] = cSongParts[i].getPhraseArray();
        	for(int j=0; j<cSongParts[i].getSize(); j++){

        		/*
        		int keysig = 0;
        		int[] scale = MAJOR_SCALE;
        		int sharpsflats = cSong.getKeySignature();
        		System.out.println(sharpsflats+" "+cSong.getKeyQuality());
        		if(sharpsflats<0){
        			if(sharpsflats==-1) keysig=5;
        			else if(sharpsflats==-2) keysig=10;
        			else if(sharpsflats==-3) keysig=3;
        			else if(sharpsflats==-4) keysig=8;
        			else if(sharpsflats==-5) keysig=1;
        			else if(sharpsflats==-6) keysig=6;
        			else if(sharpsflats==-7) keysig=11;
        		}
        		else if(sharpsflats>0){
        			if(sharpsflats==1) keysig=7;
        			else if(sharpsflats==2) keysig=2;
        			else if(sharpsflats==3) keysig=9;
        			else if(sharpsflats==4) keysig=4;
        			else if(sharpsflats==5) keysig=11;
        			else if(sharpsflats==6) keysig=6;
        			else if(sharpsflats==7) keysig=1;
        		}
        		if(song.getKeyQuality()==1) scale=MINOR_SCALE;
        		String[] stats = PhraseAnalysis.getAllStatisticsAsStrings(chopinPhrases[i][j], chopinPhrases[i][j].getBeatLength(), keysig, scale);
        		String chopintitle = chopinSongName.substring(chopinSongName.lastIndexOf('\\')+1, chopinSongName.lastIndexOf('.'));
        		try{
        			File chopinDestination = new File("Songs/Chopin song stats/"+chopintitle+"statistics.txt");
        			BufferedWriter writer = new BufferedWriter(new FileWriter(chopinDestination,true));
        			writer.write(String.valueOf(chopinPhrases[i][j].getSize()) + " ");
        			for (int m=0; m<stats.length; m++){
        				if(m!=stats.length-1) writer.write(stats[m]+" ");
        				else writer.write(stats[m]+"\n");
        			}
        			writer.close();
        		}
        		catch(IOException e){}
        		*/

        		int[] pitchintervals = PhraseAnalysis.pitchIntervals(chopinPhrases[i][j]);
        		//System.out.println("Pitch intervals");
        		if(pitchintervals.length!=0){
        			Reader.cPitchTotal+=pitchintervals.length;
        			//System.out.println("Part " + i + ": Phrase " + j);
        			for(int n=0; n<pitchintervals.length; n++){
        				//System.out.println(pitchintervals[n]);
        				if(cPitchTree.get(pitchintervals[n]) == null){
        					cPitchTree.put(pitchintervals[n], 1);
        				}
        				else{
        					int pitchCount = cPitchTree.get(pitchintervals[n]);
        					cPitchTree.put(pitchintervals[n], pitchCount+1);
        				}
        			}
        		}
        		else{
        			//System.out.println("Part " + i + ": Phrase " + j);
        			//System.out.println("no intervals");
        		}
        		double[] rhythmArray = chopinPhrases[i][j].getRhythmArray();
        		//System.out.println("Rhythm values");
        		if(rhythmArray.length!=0){
        			Reader.cRhythmTotal+=rhythmArray.length;
        			//System.out.println("Part " + i + ": Phrase " + j);
        			for(int n=0; n<rhythmArray.length; n++){
        				//System.out.println(rhythmArray[n]);
        				if(cRhythmTree.get(rhythmArray[n]) == null){
        					cRhythmTree.put(rhythmArray[n], 1);
        				}
        				else{
        					int rhythmCount = cRhythmTree.get(rhythmArray[n]);
        					cRhythmTree.put(rhythmArray[n], rhythmCount+1);
        				}
        			}
        		}
        		else{
        			//System.out.println("Part " + i + ": Phrase " + j);
        			//System.out.println("no notes");
        		}

        	}
        }

        //trunkate and round trees, correcting pitch total
        pitchTree = pitchRangeTrunkate(pitchTree,'b');
        rhythmTree = rhythmRound(rhythmTree);
        cPitchTree = pitchRangeTrunkate(cPitchTree,'c');
        cRhythmTree = rhythmRound(cRhythmTree);

        //write(update) tree.txt file and update percents.txt file
        try{
	        BufferedWriter writer = new BufferedWriter(new FileWriter(treeStorage,false));
	        File percentStorage = new File("percents.txt");
	        BufferedWriter percentWriter = new BufferedWriter(new FileWriter(percentStorage,false));

	        //Bach
	        Iterator<Integer> bPitchIterator = pitchTree.keySet().iterator();
	        Iterator<Double> bRhythmIterator = rhythmTree.keySet().iterator();
	        //System.out.println("Bach pitch interval total: " + Reader.bPitchTotal);
	        writer.write(Reader.bPitchTotal + "\n");
	        //System.out.println("Pitchtes:");
	        percentWriter.write("Bach" + "\n" + "Pitches" + "\n");
	        while(bPitchIterator.hasNext()){
	        	int cur = bPitchIterator.next();
	        	//System.out.print(cur + " ");
	        	//System.out.println(pitchTree.get(cur));
	        	writer.write(cur + " " + pitchTree.get(cur) + "\n");
	        	percentWriter.write(cur + " " + (((double)pitchTree.get(cur)/(double)Reader.bPitchTotal)*100) + "\n");
	        }
	        //System.out.println("Bach note total: " + Reader.bRhythmTotal);
	        writer.write(Reader.bRhythmTotal + "\n");
	        //System.out.println("Rhytmic Lengths:");
	        percentWriter.write("Rhythm" + "\n");
	        while(bRhythmIterator.hasNext()){
	        	double cur = bRhythmIterator.next();
	        	//System.out.print(cur + " ");
	        	//System.out.println(rhythmTree.get(cur));
	        	writer.write(cur + " " + rhythmTree.get(cur) + "\n");
	        	percentWriter.write(cur + " " + (((double)rhythmTree.get(cur)/(double)Reader.bRhythmTotal)*100) + "\n");
	        }

	        //Chopin
	        Iterator<Integer> cPitchIterator = cPitchTree.keySet().iterator();
	        Iterator<Double> cRhythmIterator = cRhythmTree.keySet().iterator();
	        //System.out.println("Chopin pitch interval total: " + Reader.cPitchTotal);
	        writer.write(Reader.cPitchTotal + "\n");
	        //System.out.println("Pitchtes:");
	        percentWriter.write("Chopin" + "\n" + "Pitches" + "\n");
	        while(cPitchIterator.hasNext()){
	        	int cur = cPitchIterator.next();
	        	//System.out.print(cur + " ");
	        	//System.out.println(cPitchTree.get(cur));
	        	writer.write(cur + " " + cPitchTree.get(cur) + "\n");
	        	percentWriter.write(cur + " " + (((double)cPitchTree.get(cur)/(double)Reader.cPitchTotal)*100) + "\n");
	        }
	        //System.out.println("Chopin note total: " + Reader.cRhythmTotal);
	        writer.write(Reader.cRhythmTotal + "\n");
	        //System.out.println("Rhytmic Lengths:");
	        percentWriter.write("Rhythm" + "\n");
	        while(cRhythmIterator.hasNext()){
	        	double cur = cRhythmIterator.next();
	        	//System.out.print(cur + " ");
	        	//System.out.println(cRhythmTree.get(cur));
	        	writer.write(cur + " " + cRhythmTree.get(cur) + "\n");
	        	percentWriter.write(cur + " " + (((double)cRhythmTree.get(cur)/(double)Reader.cRhythmTotal)*100) + "\n");
	        }
	        writer.close();
	        percentWriter.close();

	        //doing weighted percents

        }
        catch(IOException e){}


    }

    public static TreeMap<Integer,Integer> pitchRangeTrunkate(TreeMap<Integer,Integer> pitchTreeIn, char switcher){
    	TreeMap<Integer,Integer> pitchTreeOut = new TreeMap<Integer,Integer>();
    	Iterator<Integer> pitchIterator = pitchTreeIn.keySet().iterator();
    	while(pitchIterator.hasNext()){
    		int key = pitchIterator.next();
    		if(-24<=key && key<=24){
    			pitchTreeOut.put(key, pitchTreeIn.get(key));
    		}
    		else if(switcher=='b'){
    			Reader.bPitchTotal = Reader.bPitchTotal-pitchTreeIn.get(key);
    		}
    		else if(switcher=='c'){
    			Reader.cPitchTotal = Reader.cPitchTotal-pitchTreeIn.get(key);
    		}
    	}
    	return pitchTreeOut;
    }

    public static TreeMap<Double,Integer> rhythmRound(TreeMap<Double,Integer> rhythmTreeIn){
    	TreeMap<Double,Integer> rhythmTreeOut = new TreeMap<Double,Integer>();
    	Iterator<Double> rhythmIterator = rhythmTreeIn.keySet().iterator();
    	while(rhythmIterator.hasNext()){
    		double origKey = rhythmIterator.next();
    		double newKey = Math.round(origKey*(double)100)/(double)100;
    		int curValue = 0;
    		if(rhythmTreeOut.get(newKey)!= null) curValue = rhythmTreeOut.get(newKey);
    		rhythmTreeOut.put(newKey, (rhythmTreeIn.get(origKey)+curValue));
    	}
    	return rhythmTreeOut;
    }

    public static void buildLibrary(String bachPath, String chopinPath){
    	bachSongName = bachPath;
    	chopinSongName = chopinPath;
    	String[] a = new String[0];
    	main(a);
    }

    static void weightRhythms(String treeFile, String percentFile){
	    try{
	        File treeStorage = new File(treeFile);
	        File percentStorage = new File(percentFile);
	        if(treeStorage.exists()){
				Scanner fileScanner = new Scanner(treeStorage);
		        File weightedRhythms = new File("weightedRhythms.txt");
		        BufferedWriter weightedWriter = new BufferedWriter(new FileWriter(weightedRhythms,false));
		        String input = new String();
		        boolean bach = false;
		        boolean chopin = false;
		        boolean pitch = false;
		        boolean rhythm = false;
		        int count = 0;
		        while(fileScanner.hasNext()){
		        	input = fileScanner.nextLine();
	        		Scanner lineScanner = new Scanner(input);
	        		float key = Float.parseFloat(lineScanner.next());
		        	if(!lineScanner.hasNext()&&count==0){
		        		bach = true;
		        		chopin = false;
		        	}
		        	else if (!lineScanner.hasNext()&&count==2){
		        		chopin = true;
		        		bach = false;
		        	}
		        	if(!lineScanner.hasNext()&&(count==0||count==2)){
		        		pitch = true;
		        		rhythm = false;
		        		weightedWriter.write((int)key+"\n");
		        		count++;
		        	}
		        	else if(!lineScanner.hasNext()){
		        		rhythm = true;
		        		pitch = false;
		        		weightedWriter.write((int)key+"\n");
		        		count++;
		        	}
		        	else{
		        		int value = Integer.parseInt(lineScanner.next());
		        		if(rhythm){
		        			weightedWriter.write(key+" "+(key*value)+"\n");
		        		}
		        	}
		        }
		        weightedWriter.close();
	        }

	        if(percentStorage.exists()){
				Scanner fileScanner = new Scanner(percentStorage);
		        File weightedPercents = new File("weightedPercents.txt");
		        BufferedWriter percentWriter = new BufferedWriter(new FileWriter(weightedPercents,false));
		        String input = new String();
		        boolean bach = false;
		        boolean chopin = false;
		        boolean pitch = false;
		        boolean rhythm = false;
		        while(fileScanner.hasNext()){
		        	input = fileScanner.nextLine();
		        	if(input.equals("Bach")){
		        		bach = true;
		        		chopin = false;
		        		percentWriter.write("Bach\n");
		        	}
		        	else if (input.equals("Chopin")){
		        		chopin = true;
		        		bach = false;
		        		percentWriter.write("Chopin\n");
		        	}
		        	else if(input.equals("Pitches")){
		        		pitch = true;
		        		rhythm = false;
		        		percentWriter.write("Pitches\n");
		        	}
		        	else if(input.equals("Rhythm")){
		        		rhythm = true;
		        		pitch = false;
		        		percentWriter.write("Rhythm\n");
		        	}
		        	else{
		        		Scanner lineScanner = new Scanner(input);
		        		float key = Float.parseFloat(lineScanner.next());
		        		float value = Float.parseFloat(lineScanner.next());
		        		if(pitch){
		        			percentWriter.write((int)key+" "+value+"\n");
		        		}
		        		if(rhythm){
		        			percentWriter.write(key+" "+(key*value)+"\n");
		        		}
		        	}
		        }
		        percentWriter.close();
	        }
    	}
	    catch(IOException e){}
	}

    static void percentsUsingWeightedRhythm(){
    	Reader.useWeighted = true;

    }

}