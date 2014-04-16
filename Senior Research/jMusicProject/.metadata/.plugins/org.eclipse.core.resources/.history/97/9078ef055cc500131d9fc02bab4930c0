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
	
	public static void analisisMethod2(String songpath){
		GeneralReader.read(songpath, "Random Song");
		treeStitch("Random Song Pitch By Key Tree.txt", "Random Song Rhythm Tree.txt");
		AnalizerBigram.analyze("Random Song Trees.txt");
	}
	
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
