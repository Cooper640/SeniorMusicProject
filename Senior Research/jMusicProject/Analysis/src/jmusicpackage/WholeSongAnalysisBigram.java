package jmusicpackage;

import java.io.*;
import java.util.*;

import jm.JMC;
import jm.music.data.*;
import jm.music.tools.*;
import jm.util.*;

public class WholeSongAnalysisBigram implements JMC{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	//old chunking method
	/*
	public static void analyze(String songName, int CHUNKSIZE){
		Score song = new Score();
		Read.midi(song, songName);
		Part[] songParts = song.getPartArray();
		int i=0;
		for(Part curPart:songParts){
			int j=0;
			Phrase[] phrases = curPart.getPhraseArray();
			for(Phrase curPhrase: phrases){
				int offset = 0;
				int length = curPhrase.size();
				while(offset<length){
					SinglePhraseSelectorBigram.read(songName, CHUNKSIZE, i, j, offset);
					AnalizerBigram.analyze("Random Song Trees.txt");
					offset=offset+CHUNKSIZE;
				}
				j++;
			}
			i++;
		}
	}
	*/

	//bigram method
	public static void analisisMethod2(String songpath){
		try{
    		File file1 = new File("Random Song Pitch By Key Tree.txt");
    		File file2 = new File("Random Song Pitch Percents.txt");
    		File file3 = new File("Random Song Rhythm Percents.txt");
    		File file4 = new File("Random Song Rhythm Tree.txt");
    		File file5 = new File("Random Song Trees.txt");

    		if(file1.exists() && file2.exists() && file3.exists() && file4.exists() && file5.exists()){
	    		if(file1.delete() && file2.delete() && file3.delete() && file4.delete() && file5.delete()){
	    			System.out.println("Delete worked.");
	    		}else{
	    			System.out.println("Delete operation is failed.");
	    		}
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		GeneralReader.read(songpath, "Random Song");
		treeStitch("Random Song Pitch By Key Tree.txt", "Random Song Rhythm Tree.txt");
		AnalizerBigram.analyze("Random Song Trees.txt");
	}

	//used for Trigrams
	public static void analisisTrigramMethod (String songpath) {
		try{
    		File file1 = new File("Random Song Pitch By Key Tree.txt");
    		File file2 = new File("Random Song Pitch Percents.txt");
    		File file3 = new File("Random Song Rhythm Percents.txt");
    		File file4 = new File("Random Song Rhythm Tree.txt");
    		File file5 = new File("Random Song Trees.txt");

    		if(file1.exists() && file2.exists() && file3.exists() && file4.exists() && file5.exists()){
	    		if(file1.delete() && file2.delete() && file3.delete() && file4.delete() && file5.delete()){
	    			System.out.println("Delete worked.");
	    		}else{
	    			System.out.println("Delete operation is failed.");
	    		}
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		GeneralReaderTrigram.read(songpath, "Random Song");
		treeStitch("Random Song Pitch By Key Tree.txt", "Random Song Rhythm Tree.txt");
		AnalizerTrigram.analyze("Random Song Trees.txt");
	}

	//used to create a single file which AnalizerTrigram can read
	public static void treeStitch(String pitchTreePath, String rhythmTreePath){
		String trees[] = new String[2];
		trees[0] = pitchTreePath;
		trees[1] = rhythmTreePath;
		File treeStorage = new File("Random Song Trees.txt");
		try{
			OutputStream out = new FileOutputStream(treeStorage);
		    byte[] buf = new byte[100];
		    for (String file : trees) {
		        InputStream in = new FileInputStream(file);
		        int b = 0;
		        while ( (b = in.read(buf)) >= 0) {
		            out.write(buf, 0, b);
		            out.flush();
		        }
		    }
		    out.close();
		}
	    catch(IOException e){}
	}

}
