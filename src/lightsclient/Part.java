package lightsclient;

import java.util.ArrayList;
import java.util.Map;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.swt.widgets.Tracker;

public class Part {
	
	private int currentEvent;
	private int currentMeasure;
	private int currentPart;
	
	private int channel;
	private Track track;
	private ArrayList<Event> notes;		// index: time: notes
	private ArrayList<Long> outputTimes;
	private ArrayList<Long> partTimes;
	private ArrayList<Long> measureTimes;
	
//	public Part(int channel, Song s) {
//		currentNote = 0;
//		currentChord = 0;
//		inChord = false;
//		numInChord = 0;
//		currentMeasure = 0;
//		currentPart = 0;
//		
//		outputTracks = new ArrayList<Track>();
//		chords = new ArrayList<ArrayList<Integer> >();
//		
//		this.channel = channel;
//		Sequence seq = s.getInputTrack(channel);
//		ArrayList<Sequence> outputSeq = s.getOutputTracks();
//		
//		Track[] t = seq.getTracks();
//		track = t[0];
//		
//		// make sure all events are 0x90
//		for (int i = 0; i < track.size();) {
//			MidiEvent ev = track.get(i);
//			MidiMessage m = ev.getMessage();
//			
//			if (m instanceof ShortMessage) {
//				ShortMessage sm = (ShortMessage)m;
//		
//				if (sm.getCommand() != ShortMessage.NOTE_ON) {
//				//	System.out.println("Not Note_on: " + sm.getCommand());
//					track.remove(ev);
//				} else {
//				//	System.out.println("NOTE_ON");
//					i++;
//				}
//			} else {
//			//	System.out.println("Not ShortMessage");
//				i++;
//			}
//		}
//		
//		// add other tracks if needed
//		for (int i = 1; i < t.length; i++) {
//			//System.out.println(t[i].size());
//			for (int j = 0; j < t[i].size(); j++) {
//				
//				// make sure event is 0x90
//				MidiEvent ev = t[i].get(i);
//				MidiMessage m = ev.getMessage();
//				
//				if (m instanceof ShortMessage) {
//					ShortMessage sm = (ShortMessage)m;
//				//	System.out.println(sm.getCommand());
//					if (sm.getCommand() == 0x90) {
//						System.out.println("NOTE_ON");
//						track.add(ev);
//					}
//				}
//			}
//		}
//		
//		System.out.println("Track length: " + track.size());
//
//		// populate chords
//		long previousTime = 0;
//		int numChords = 0;
//		chords.add(new ArrayList<Integer>(2));
//		boolean inChord = false;
//		for (int i = 1; i < track.size(); i++) {
//			// get current time
//			long currentTime = track.get(i).getTick();
//			System.out.println(track.get(i).getTick());
//			
//			// check if the past two notes have the same time
//			if (currentTime == previousTime) {
//				// the past two notes are played at the same time (a chord)
//				
//				// check if we are already in a chord by checking chords
//				if (inChord) {
//					// previous chord is unfinished
//					
//					// check if this is the last note in a chord
//					long nextTime = track.get(i + 1).getTick();
//					if (currentTime != nextTime) {
//						// chord is ending
//						chords.get(numChords).add(i);
//						inChord = false;
//						System.out.println("Chord ends at " + i);
//					}
//				} else {
//					// we must be at the second note in a chord
//					// start new chord in chords
//					chords.add(new ArrayList<Integer>(2));
//					numChords++;
//					inChord = true;
//					chords.get(numChords).add(i - 1);
//					System.out.println("Chord begins at " + (i - 1));
//				}
//				
//			}
//		}
//	
//		// turn sequences in outputSeq into tracks
//		for (int i = 0; i < outputSeq.size(); i++) {
//			t = outputSeq.get(i).getTracks();
//			outputTracks.add(t[0]);
//			for (int j = 1; j < t.length; j++) {
//				for (int h = 0; h < t[j].size(); h++) {
//					outputTracks.get(i).add(t[j].get(h));
//				}
//			}
//		}
//		
//		this.partTimes = s.getPartTimes();
//		this.measureTimes = s.getMeasureTimes();
//	}
	
	public Part(int channel, String[] lines) {
		this.channel = channel;
		partTimes = new ArrayList<Long>();
		measureTimes = new ArrayList<Long>();
		outputTimes = new ArrayList<Long>();
		
		// initialize notes
		notes = new ArrayList<Event>(lines.length);
		
		for (String line : lines) {
			notes.add(new Event(line));
		}
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
