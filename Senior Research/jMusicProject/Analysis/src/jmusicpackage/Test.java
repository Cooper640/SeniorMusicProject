package jmusicpackage;

import java.io.*;
import java.util.*;
import jm.JMC;
import jm.music.data.*;
import jm.music.tools.*;
import jm.util.*;

public class Test implements JMC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//pitch relative to key
		int[] scale = MAJOR_SCALE;
		scale[6]=MAJOR_SCALE[6]-1;

		Score song = new Score("Test score");
		song.setKeySignature(-1);
		song.setKeyQuality(0);
		Part inst = new Part("Guitar", SGUITAR, 0);
		Phrase phr = new Phrase();
		Note mahnote= new Note(F3, Q);
		System.out.println(mahnote.isScale(scale));
		//System.out.println(mahnote.nextNote(scale));
		phr.addNote(mahnote);
		mahnote= new Note(G3, Q);
		System.out.println(mahnote.isScale(scale));
		//System.out.println(mahnote.nextNote(scale));
		phr.addNote(mahnote);
		mahnote= new Note(A3, Q);
		System.out.println(mahnote.isScale(scale));
		//System.out.println(mahnote.nextNote(scale));
		phr.addNote(mahnote);
		mahnote= new Note(BF3, Q);
		System.out.println(mahnote.isScale(scale));
		//System.out.println(mahnote.nextNote(scale));
		phr.addNote(mahnote);
		mahnote= new Note(C4, Q);
		System.out.println(mahnote.isScale(scale));
		//System.out.println(mahnote.nextNote(scale));
		phr.addNote(mahnote);
		mahnote= new Note(D4, Q);
		System.out.println(mahnote.isScale(scale));
		//System.out.println(mahnote.nextNote(scale));
		phr.addNote(mahnote);
		mahnote= new Note(E4, Q);
		System.out.println(mahnote.isScale(scale));
		//System.out.println(mahnote.nextNote(scale));
		phr.addNote(mahnote);
		mahnote= new Note(F4, Q);
		System.out.println(mahnote.isScale(scale));
		//System.out.println(mahnote.nextNote(scale));
		phr.addNote(mahnote);

		/*
		int keysig = 0;
		int[] scale = MAJOR_SCALE;
		int sharpsflats = song.getKeySignature();
		System.out.println(sharpsflats+" "+song.getKeyQuality());
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
		for(int m = 0; m<scale.length; m++){
			scale[m]=scale[m]-7;
		}
		Note[] notes = maryPhrases[i][j].getNoteArray();
		for(int m = 0; m<notes.length; m++){
			int middlescalevalue = notes[m].getPitchValue();
			boolean inKey = notes[m].isScale(scale);
			int scaleDegree = 0;
			if(inKey){
    			for(int n=0; n<scale.length; n++){
    				if(scale[n]==middlescalevalue) scaleDegree = scale[n];
    			}
			}
			int distance = Math.abs(middlescalevalue-keysig);
			System.out.println(middlescalevalue+" "+inKey+" "+distance+" "+scaleDegree);
		}
		*/
	}

}
