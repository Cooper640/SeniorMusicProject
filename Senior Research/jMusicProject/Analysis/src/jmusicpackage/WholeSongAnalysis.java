package jmusicpackage;

import java.io.*;
import java.util.*;

import jm.JMC;
import jm.music.data.*;
import jm.music.tools.*;
import jm.util.*;

public class WholeSongAnalysis implements JMC{

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
					SinglePhraseSelector.read(songName, CHUNKSIZE, i, j, offset);
					Analizer.analyze("Random Song Trees.txt");
					offset=offset+CHUNKSIZE;
				}
				j++;
			}
			i++;
		}
	}

}
