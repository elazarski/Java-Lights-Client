package lightsclient;

import java.util.ArrayList;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class Part {
	
	private int currentNote;
	private int currentChord;
	private boolean inChord;
	private int numInChord;
	private int currentMeasure;
	private int currentPart;
	
	private int channel;
	private Track track;
	private ArrayList<ArrayList<Integer> > chords;
	private ArrayList<Track> outputTracks;
	private ArrayList<Long> partTimes;
	private ArrayList<Long> measureTimes;
	
	public Part(int channel, Song s) {
		currentNote = 0;
		currentChord = 0;
		inChord = false;
		numInChord = 0;
		currentMeasure = 0;
		currentPart = 0;
		
		outputTracks = new ArrayList<Track>();
		chords = new ArrayList<ArrayList<Integer> >();
		
		this.channel = channel;
		Sequence seq = s.getInputTrack(channel);
		ArrayList<Sequence> outputSeq = s.getOutputTracks();
		
		Track[] t = seq.getTracks();
		track = t[0];
		
		// add other tracks if needed
		for (int i = 1; i < t.length; i++) {
			for (int j = 0; j < t[i].size(); j++) {
				track.add(t[i].get(j));
			}
		}
		
		// populate chords
		long previousTime = 0;
		int numChords = 0;
		for (int i = 1; i < track.size(); i++) {
			long currentTime = track.get(i).getTick();
			if (currentTime == previousTime) {

				// for first chord
				if (numChords > 0) {
					
					// check if already in chord
					if (chords.get(numChords).size() > 1) {
						
						// check if chord is ending
						long nextTime = track.get(i + 1).getTick();
						if (nextTime != currentTime) {
							
							// finish current chord
							chords.get(numChords).add(i);
							numChords++;
						}
					}
				} else {
					// first chord
					chords.add(new ArrayList<Integer>());
					chords.get(0).add(i - 1);
				}
			}
			
			if (chords.get(0).get(0) == 0) {
				inChord = true;
			}
			
			// update prvious time
			previousTime = currentTime;
		}
	
		// turn sequences in outputSeq into tracks
		for (int i = 0; i < outputSeq.size(); i++) {
			t = outputSeq.get(i).getTracks();
			outputTracks.add(t[0]);
			for (int j = 1; j < t.length; j++) {
				for (int h = 0; h < t[j].size(); h++) {
					outputTracks.get(i).add(t[j].get(h));
				}
			}
		}
		
		this.partTimes = s.getPartTimes();
		this.measureTimes = s.getMeasureTimes();
	}
	
	public int getChannel() {
		return channel;
	}
	
	// checks if input is acceptable to increase current note
	public boolean isNext(int input) {
	
		// check if we are in a chord
		if (inChord) {
			
			// create array to loop through
			int chordLen = chords.get(currentChord).get(1) - chords.get(currentChord).get(0);
			for (int i = 0; i < chordLen; i++) {
				ShortMessage sm = (ShortMessage) track.get(currentNote + i).getMessage();
				int correctNote = sm.getData1();
				
				if ((input >= correctNote - 2) && (input <= correctNote + 2)) {
					currentNote++;
					numInChord++;
					
					// check if chord is done
					if (numInChord >= chordLen) {
						inChord = false;
						numInChord = 0;
						
						//checkNextMP();
					}
					
					return true;
				}
			}
		} else {
			
			ShortMessage sm = (ShortMessage)track.get(currentNote).getMessage();
			int correctNote = sm.getData1();
			
			if ((input >= correctNote - 2) && (input <= correctNote + 2)) {
				currentNote++;
				//checkNextMP();
				
				return true;
			}
		}
		
		return false;
	}
	
	public boolean done() {
		if (currentNote == track.size()) {
			return true;
		} else {
			return false;
		}
	}
	
	private void checkNextMP() {
		// check if starting next measure or part
		long nextTick = track.get(currentNote).getTick();
		if (nextTick == measureTimes.get(currentMeasure + 1)) {
			currentMeasure++;
			if (nextTick == partTimes.get(currentPart + 1)) {
				currentPart++;
			}
		}
	}
	
}
