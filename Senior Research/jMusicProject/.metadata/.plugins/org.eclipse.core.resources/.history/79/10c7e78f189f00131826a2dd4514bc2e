package jmusicpackage;

import java.util.*;
import jm.JMC;
import jm.music.data.*;
import jm.music.tools.*;
import jm.util.*;
//import jm.gui.*;
//import jm.gui.sketch.SketchScore;

public class Reader implements JMC {
    public static void main(String[] args){
        Score song = new Score();
        //mary_had_a_little_lamb.mid
        //Bach-CelloSuiteNo1Prelude.mid
        Read.midi(song, "mary_had_a_little_lamb.mid");
        System.out.println(song.toString());
        TreeMap<Integer, Integer> pitchTree = new TreeMap<Integer, Integer>();
        TreeMap<Double, Integer> rhythmTree = new TreeMap<Double, Integer>();
        Part[] songParts = song.getPartArray();
        Phrase[][] maryPhrases = new Phrase[song.getSize()][];
        for(int i = 0; i < song.getSize(); i++){
        	maryPhrases[i] = songParts[i].getPhraseArray();
        	for(int j=0; j<songParts[i].getSize(); j++){
        		int[] pitchintervals = PhraseAnalysis.pitchIntervals(maryPhrases[i][j]);
        		System.out.println("Pitch intervals");
        		if(pitchintervals.length!=0){
        			System.out.println("Part " + i + ": Phrase " + j);
        			for(int n=0; n<pitchintervals.length; n++){
        				System.out.println(pitchintervals[n]);
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
        			System.out.println("Part " + i + ": Phrase " + j);
        			System.out.println("no intervals");
        		}
        		double[] rhythmArray = maryPhrases[i][j].getRhythmArray();
        		System.out.println("Rhythm values");
        		if(rhythmArray.length!=0){
        			System.out.println("Part " + i + ": Phrase " + j);
        			for(int n=0; n<rhythmArray.length; n++){
        				System.out.println(rhythmArray[n]);
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
        			System.out.println("Part " + i + ": Phrase " + j);
        			System.out.println("no notes");
        		}

        	}
        }
        View.sketch(song);
    }

    public TreeMap CalculateProbabilities(TreeMap map){
    	TreeMap<Integer, Float> newMap = new TreeMap<Integer,Float>();

    	return newMap;
    }
}