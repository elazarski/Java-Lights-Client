package lightsclient;

import java.util.ArrayList;

public class Part {
	
	private int currentEvent;
	private int currentMeasure;
	private int currentPart;
	
	private int possibleEvent;
	private int numPossibles;
	
	private int channel;
	private ArrayList<Event> notes;		
	private ArrayList<Long[]> outputTimes;
	private ArrayList<Long> partTimes;
	private ArrayList<Long> measureTimes;
	
	private int[] partIndexes;
	private int[] measureIndexes;
	
	
	public Part(int channel, String[] lines) {
		this.channel = channel;
		partTimes = new ArrayList<Long>();
		measureTimes = new ArrayList<Long>();
		outputTimes = new ArrayList<Long[]>();
		
		// initialize notes
		notes = new ArrayList<Event>(lines.length);
		
		for (String line : lines) {
			if (!line.equals("")) {
				notes.add(new Event(line));
			}
		}
		
		// initialize CURRENT variables
		currentEvent = 0;
		currentMeasure = 0;
		currentPart = 0;
		
		// initiate POSSIBLE values
		possibleEvent = 0;
		numPossibles = 0;
	}
	
	
	public int getChannel() {
		return channel;
	}
	
	
	public long getTime() {
		return notes.get(currentEvent).getTime();
	}
	
	
	public long getPartTime() {
		return partTimes.get(currentPart);
	}
	
	
	public long getMeasureTime() {
		return partTimes.get(currentMeasure);
	}
	
	
	public void addOutputTimes(long[] p) {
		Long[] temp = new Long[p.length];
		for (int i = 0; i < p.length; i++) {
			temp[i] = p[i];
		}
		
		outputTimes.add(temp);
	}
	
	public void addParts(ArrayList<Long> parts) {
		this.partTimes = parts;
		
		// populate partIndexes
		this.partIndexes = new int[partTimes.size()];
		
		// go through notes and part times
		int part = 0;
		for (int i = 0; i < notes.size(); i++) {
			Event ev = notes.get(i);
			
			// if current event time == current part time, then add value to partIndexes
			if (ev.getTime() == partTimes.get(part)) {
				partIndexes[part] = i;
				part++;
			}
		}
	}
	
	public void addMeasures(ArrayList<Long> measures) {
		this.measureTimes = measures;
		
		// populate measureIndexes
		this.measureIndexes = new int[measureTimes.size()];
		
		// go through notes and measure times
		int measure = 0;
		for (int i = 0; i < notes.size(); i++) {
			Event ev = notes.get(i);
			
			// if current event time == current measure time, then add value to measureIndexes
			if (ev.getTime() == measureTimes.get(measure)) {
				measureIndexes[measure] = i;
				measure++;
			}
		}
	}
	
	public boolean isNext(int input) {
		System.out.println(Thread.currentThread().getName());
		// if we have had 4 possible notes in a row, move to that note
		if (numPossibles >= 4) {
			currentEvent = possibleEvent;
			numPossibles = 0;
		}
		
		// ask current event if this is the correct input
		Event ev = notes.get(currentEvent);
		
		boolean correct = ev.contains(input);
		
		// check for next MP if correct note
		if (correct) {
			nextMP();
			
			// check isDone to see if we should increment currentNote
			if (ev.isDone()) {
				currentEvent++;
				possibleEvent = currentEvent;
			}
		}

		return correct;
	}
	
	public Long isPossible(int input) {		
		// make sure that we aren't already looking at a possible new place
		if (possibleEvent != currentEvent) {
			Event ev = notes.get(possibleEvent);
			
			if (ev.possiblyContains(input)) {
				
				if (ev.possiblyIsDone()) {
					possibleEvent++;
					numPossibles++;
					
					return ev.getTime();
				}
			}
		}
		
		// check next handful of notes
		Long time = possibleEvent(input, currentEvent);
		if (time != null) {
			return time;
		}
		
		// check next few of measures
		for (int i = currentMeasure + 1; i < currentMeasure + 4; i++) {
			if (i > measureIndexes.length) {
				break;
			}
			int index = measureIndexes[i];
			time = possibleEvent(input, index);
			
			if (time != null) {
				return time;
			}
		}
		
		// check next couple of parts
		for (int i = currentPart + 1; i < currentPart + 3; i++) {
			if (i > partIndexes.length) {
				break;
			}
			int index = partIndexes[i];
			time = possibleEvent(input, index);
			
			if (time != null) {
				return time;
			}
		}
		
		//else {
//			// first attempt at a new possible note
//			// check through measures and check to see if this could be a
//			// possible start to one in the future
//			for (int i = currentMeasure; i < measureIndexes.length; i++) {
//				Event ev = notes.get(i + numPossibles);
//
//				if (ev.possiblyContains(input)) {
//					possibleEvent = i + numPossibles;
//					
//					if (ev.possiblyIsDone()) {
//						numPossibles++;
//					}
//
//					return ev.getTime();
//				}
//			}
//		}
		
		return null;
	}
	
	private Long possibleEvent(int input, int index) {
		// check next handful of events
		for (int i = 1; i < 5; i++) {
			Event ev = notes.get(index + i);

			if (ev.possiblyContains(input)) {

				if (ev.possiblyIsDone()) {
					possibleEvent = index + i;
					numPossibles++;

					return ev.getTime();
				}
			}
		}
		
		return null;
	}
	
	public boolean isDone() {
		if (currentEvent == notes.size()) {
			System.out.println("SONG DONE: " + channel);
			return true;
		} else {
			return false;
		}
	}
	
	// check if we have changed measures or parts
	private void nextMP() {
		// check measure first
		// get time
		long nextMeasure = measureTimes.get(currentMeasure + 1);
		if (getTime() >= nextMeasure) {
			currentMeasure++;
		}
		
		// check parts next
		// get time
		long nextPart = partTimes.get(currentPart + 1);
		if (getTime() >= nextPart) {
			currentPart++;
		}
	}
	
	private void findForwardMP() {
		// measures first
		long measure = measureTimes.get(currentMeasure);
		while (getTime() >= measure) {
			currentMeasure++;
			
			measure  = measureTimes.get(currentMeasure);
		}
		
		// parts next
		long part = partTimes.get(currentPart);
		while (getTime() >= part) {
			currentPart++;
			
			part = partTimes.get(currentPart);
		}
	}
	
	private void findBackwardMP() {
		// measures first
		long measure = measureTimes.get(currentMeasure);
		while (getTime() < measure) {
			currentMeasure--;
			
			measure = measureTimes.get(currentMeasure);
		}
		
		// parts next
		long part = partTimes.get(currentPart);
		while (getTime() < part) {
			currentPart--;
			
			part = partTimes.get(currentPart);
		}
	}
	
	public Long nextPart() {
		// make sure we are not out of parts
		if (currentPart == partTimes.size()) {
			return null;
		}
		
		System.out.print(channel + ": Part " + currentPart + " -> ");
		currentPart++;
		System.out.print(currentPart + ", Measure " + currentMeasure + " -> ");
		
		// check to see if we are waiting for the start of a new part
		// if new currentPart == next note
		if (partTimes.get(currentPart) == notes.get(currentEvent + 1).getTime()) {
			currentPart++;
		}
		
		// find new currentNote and currentMeasure
		long partTIme = partTimes.get(currentPart);
		
		// measures first
		while (measureTimes.get(currentMeasure) < partTIme) {
			currentMeasure++;
		}
		
		System.out.print(currentMeasure + ", Event " + currentEvent + " -> ");
		// note
		while (notes.get(currentEvent).getTime() < partTIme) {
			currentEvent++;
		}
		
		System.out.println(currentEvent);
		
		return partTimes.get(currentPart);
	}
	
	public Long nextMeasure() {
		// make sure we are not out of measures
		if (currentMeasure == measureTimes.size()) {
			return null;
		}
		
		System.out.print(channel + ": Measure " + currentMeasure + " -> ");
		currentMeasure++;
		System.out.print(currentMeasure + ", Part " + currentPart + " -> ");
		
		// make sure we are not waiting for the current measure to start
		// if the new currentMeasure == next note
		if (measureTimes.get(currentMeasure) == notes.get(currentEvent + 1).getTime()) {
			currentMeasure++;
		}
		
		// find new currentNote and currentPart
		long measureTime = measureTimes.get(currentMeasure);
		
		// part first
		// check if we even have to move
		long nextPart = partTimes.get(currentPart + 1);
		if (nextPart <= measureTime) {
			while (partTimes.get(currentPart) < measureTime) {
				currentPart++;
			}
		}
		System.out.print(currentPart + ", Event " + currentEvent + " -> ");
		
		// new note
		while (notes.get(currentEvent).getTime() < measureTime) {
			currentEvent++;
		}
		
		System.out.println(currentEvent);
		
		return measureTimes.get(currentMeasure);
	}
	
	public Long previousPart() {
		// make sure we are not at the beginning of the song still
		if (currentPart == 0) {
			currentMeasure = 0;
			currentEvent = 0;
			return new Long(0);
		}
		
		currentPart--;
		
		// find new currentNote and currentMeasure
		long partTime = partTimes.get(currentPart);
		
		// measures first
		while (measureTimes.get(currentMeasure - 1) > partTime) {
			currentMeasure--;
		}
		
		// new note
		while (notes.get(currentEvent - 1).getTime() > partTime) {
			currentEvent--;
		}
		
		return partTimes.get(currentPart);
	}
	
	public Long previousMeasure() {
		// make sure we are not at the beginning of the song still
		if (currentMeasure == 0) {
			currentPart = 0;
			currentEvent = 0;
			return new Long(0);
		}
		
		currentMeasure--;
		
		// find new currentNote and currentPart
		long measureTime = measureTimes.get(currentMeasure);
		
		// check if even necessary for part
		if (partTimes.get(currentPart - 1) > measureTime) {
			while (partTimes.get(currentPart) > measureTime) {
				currentPart--;
			}
		}
		
		// notes
		// make sure next note is not less than (before) measureTime
		while (notes.get(currentEvent - 1).getTime() > measureTime) {
			currentEvent--;
		}
		
		return measureTimes.get(currentMeasure);
	}
	
	public void changeTime(long newTime) {
		if (newTime > getTime()) {
			// if we are behind, find new note
			while (getTime() < newTime) {
				currentEvent++;
			}
			
			// move MP accordingly
			findForwardMP();
			
		} else {
			// if we are ahead, find new note
			while (getTime() > newTime) {
				currentEvent--;
			}
			
			// move MP accordingly
			findBackwardMP();
		}
	}
}


//// checks if input is acceptable to increase current note
//public boolean isNext(int input) {
//
//	// check if we are in a chord
//	if (inChord) {
//		
//		// create array to loop through
//		int chordLen = chords.get(currentChord).get(1) - chords.get(currentChord).get(0);
//		for (int i = 0; i < chordLen; i++) {
//			ShortMessage sm = (ShortMessage) track.get(currentNote + i).getMessage();
//			int correctNote = sm.getData1();
//			
//			if ((input >= correctNote - 2) && (input <= correctNote + 2)) {
//				currentNote++;
//				numInChord++;
//				
//				// check if chord is done
//				if (numInChord >= chordLen) {
//					inChord = false;
//					numInChord = 0;
//					
//					//checkNextMP();
//				}
//				
//				return true;
//			}
//		}
//	} else {
//		
//		ShortMessage sm = (ShortMessage)track.get(currentNote).getMessage();
//		int correctNote = sm.getData1();
//		
//		if ((input >= correctNote - 2) && (input <= correctNote + 2)) {
//			currentNote++;
//			//checkNextMP();
//			
//			return true;
//		}
//	}
//	
//	return false;
//}
//
//public boolean done() {
//	if (currentNote == track.size()) {
//		return true;
//	} else {
//		return false;
//	}
//}
//
//private void checkNextMP() {
//	// check if starting next measure or part
//	long nextTick = track.get(currentNote).getTick();
//	if (nextTick == measureTimes.get(currentMeasure + 1)) {
//		currentMeasure++;
//		if (nextTick == partTimes.get(currentPart + 1)) {
//			currentPart++;
//		}
//	}
//}

//public Part(int channel, Song s) {
//currentNote = 0;
//currentChord = 0;
//inChord = false;
//numInChord = 0;
//currentMeasure = 0;
//currentPart = 0;
//
//outputTracks = new ArrayList<Track>();
//chords = new ArrayList<ArrayList<Integer> >();
//
//this.channel = channel;
//Sequence seq = s.getInputTrack(channel);
//ArrayList<Sequence> outputSeq = s.getOutputTracks();
//
//Track[] t = seq.getTracks();
//track = t[0];
//
//// make sure all events are 0x90
//for (int i = 0; i < track.size();) {
//	MidiEvent ev = track.get(i);
//	MidiMessage m = ev.getMessage();
//	
//	if (m instanceof ShortMessage) {
//		ShortMessage sm = (ShortMessage)m;
//
//		if (sm.getCommand() != ShortMessage.NOTE_ON) {
//		//	System.out.println("Not Note_on: " + sm.getCommand());
//			track.remove(ev);
//		} else {
//		//	System.out.println("NOTE_ON");
//			i++;
//		}
//	} else {
//	//	System.out.println("Not ShortMessage");
//		i++;
//	}
//}
//
//// add other tracks if needed
//for (int i = 1; i < t.length; i++) {
//	//System.out.println(t[i].size());
//	for (int j = 0; j < t[i].size(); j++) {
//		
//		// make sure event is 0x90
//		MidiEvent ev = t[i].get(i);
//		MidiMessage m = ev.getMessage();
//		
//		if (m instanceof ShortMessage) {
//			ShortMessage sm = (ShortMessage)m;
//		//	System.out.println(sm.getCommand());
//			if (sm.getCommand() == 0x90) {
//				System.out.println("NOTE_ON");
//				track.add(ev);
//			}
//		}
//	}
//}
//
//System.out.println("Track length: " + track.size());
//
//// populate chords
//long previousTime = 0;
//int numChords = 0;
//chords.add(new ArrayList<Integer>(2));
//boolean inChord = false;
//for (int i = 1; i < track.size(); i++) {
//	// get current time
//	long currentTime = track.get(i).getTick();
//	System.out.println(track.get(i).getTick());
//	
//	// check if the past two notes have the same time
//	if (currentTime == previousTime) {
//		// the past two notes are played at the same time (a chord)
//		
//		// check if we are already in a chord by checking chords
//		if (inChord) {
//			// previous chord is unfinished
//			
//			// check if this is the last note in a chord
//			long nextTime = track.get(i + 1).getTick();
//			if (currentTime != nextTime) {
//				// chord is ending
//				chords.get(numChords).add(i);
//				inChord = false;
//				System.out.println("Chord ends at " + i);
//			}
//		} else {
//			// we must be at the second note in a chord
//			// start new chord in chords
//			chords.add(new ArrayList<Integer>(2));
//			numChords++;
//			inChord = true;
//			chords.get(numChords).add(i - 1);
//			System.out.println("Chord begins at " + (i - 1));
//		}
//		
//	}
//}
//
//// turn sequences in outputSeq into tracks
//for (int i = 0; i < outputSeq.size(); i++) {
//	t = outputSeq.get(i).getTracks();
//	outputTracks.add(t[0]);
//	for (int j = 1; j < t.length; j++) {
//		for (int h = 0; h < t[j].size(); h++) {
//			outputTracks.get(i).add(t[j].get(h));
//		}
//	}
//}
//
//this.partTimes = s.getPartTimes();
//this.measureTimes = s.getMeasureTimes();
//}
//=======
//package lightsclient;
//
//import java.util.ArrayList;
//
//public class Part {
//	
//	private int currentEvent;
//	private int currentMeasure;
//	private int currentPart;
//	
//	private int channel;
//	private ArrayList<Event> notes;		
//	private ArrayList<Long[]> outputTimes;
//	private ArrayList<Long> partTimes;
//	private ArrayList<Long> measureTimes;
//	
//
//	
//	public Part(int channel, String[] lines) {
//		this.channel = channel;
//		partTimes = new ArrayList<Long>();
//		measureTimes = new ArrayList<Long>();
//		outputTimes = new ArrayList<Long[]>();
//		
//		// initialize notes
//		notes = new ArrayList<Event>(lines.length);
//		
//		for (String line : lines) {
//			if (!line.equals("")) {
//				notes.add(new Event(line));
//			}
//		}
//		
//		// initialize CURRENT variables
//		currentEvent = 0;
//		currentMeasure = 0;
//		currentPart = 0;
//	}
//	
//	public int getChannel() {
//		return channel;
//	}
//	
//	public void addOutputTimes(long[] p) {
//		Long[] temp = new Long[p.length];
//		for (int i = 0; i < p.length; i++) {
//			temp[i] = p[i];
//		}
//		
//		outputTimes.add(temp);
//	}
//	
//	public void addMeasures(ArrayList<Long> measures) {
//		this.measureTimes = measures;
//	}
//	
//	public void addParts(ArrayList<Long> parts) {
//		this.partTimes = parts;
//	}
//	
//	public boolean isNext(int input) {
//		// ask current event if this is the correct input
//		Event ev = notes.get(currentEvent);
//		
//		boolean correct = ev.contains(input);
//		
//		// check for next MP if correct note
//		if (correct) {
//			nextMP(ev);
//			
//			// check isDone to see if we should increment currentNote
//			if (ev.isDone()) {
//				currentEvent++;
//			}
//		}
//
//		return correct;
//	}
//	
//	public boolean isDone() {
//		if (currentEvent == notes.size()) {
//			System.out.println("SONG DONE: " + channel);
//			return true;
//		} else {
//			return false;
//		}
//	}
//	
//	// check if we have changed measures or parts
//	private void nextMP(Event ev) {
//		// check measure first
//		// get time
//		long nextMeasure = measureTimes.get(currentMeasure + 1);
//		if (ev.getTime() >= nextMeasure) {
//			currentMeasure++;
//		}
//		
//		// check parts next
//		// get time
//		long nextPart = partTimes.get(currentPart + 1);
//		if (ev.getTime() >= nextPart) {
//			currentPart++;
//		}
//	}
//	
//	public void nextPart() {
//		currentPart++;
//		long partTime = partTimes.get(currentPart);
//		
//		// get to next measure
//		boolean found = false;
//		int newMeasure = currentMeasure;
//		System.out.println("OLD MEASURE: " + currentMeasure);
//		while (!found) {
//			long time = measureTimes.get(newMeasure);
//			if (time >= partTime) {
//				currentMeasure = newMeasure;
//				found = true;
//				System.out.println("NEW MEASURE: " + currentMeasure);
//			} else {
//				newMeasure++;
//			}
//		}
//		
//		// find new event
//		found = false;
//		int newEvent = currentEvent;
//		System.out.println("OLD NOTE: " + currentEvent);
//		while (!found) {
//			long time = notes.get(newEvent).getTime();
//			if (time >= partTime) {
//				currentEvent = newEvent;
//				found = true;
//				System.out.println("NEW NOTE: " + currentEvent);
//			} else {
//				newEvent++;
//			}
//		}
//	}
//	
//	public void nextMeasure() {
//		currentMeasure++;
//		long measureTime = measureTimes.get(currentMeasure);
//		
//		// check for part update
//		if (measureTime >= partTimes.get(currentPart)) {
//			currentPart++;
//		}
//		
//		// find next event
//		boolean found = false;
//		int newEvent = currentEvent;
//		while (!found) {
//			long time = notes.get(newEvent).getTime();
//			if (time >= measureTime) {
//				currentEvent = newEvent;
//				found = true;
//			} else {
//				newEvent++;
//			}
//		}
//	}
//}


//// checks if input is acceptable to increase current note
//public boolean isNext(int input) {
//
//	// check if we are in a chord
//	if (inChord) {
//		
//		// create array to loop through
//		int chordLen = chords.get(currentChord).get(1) - chords.get(currentChord).get(0);
//		for (int i = 0; i < chordLen; i++) {
//			ShortMessage sm = (ShortMessage) track.get(currentNote + i).getMessage();
//			int correctNote = sm.getData1();
//			
//			if ((input >= correctNote - 2) && (input <= correctNote + 2)) {
//				currentNote++;
//				numInChord++;
//				
//				// check if chord is done
//				if (numInChord >= chordLen) {
//					inChord = false;
//					numInChord = 0;
//					
//					//checkNextMP();
//				}
//				
//				return true;
//			}
//		}
//	} else {
//		
//		ShortMessage sm = (ShortMessage)track.get(currentNote).getMessage();
//		int correctNote = sm.getData1();
//		
//		if ((input >= correctNote - 2) && (input <= correctNote + 2)) {
//			currentNote++;
//			//checkNextMP();
//			
//			return true;
//		}
//	}
//	
//	return false;
//}
//
//public boolean done() {
//	if (currentNote == track.size()) {
//		return true;
//	} else {
//		return false;
//	}
//}
//
//private void checkNextMP() {
//	// check if starting next measure or part
//	long nextTick = track.get(currentNote).getTick();
//	if (nextTick == measureTimes.get(currentMeasure + 1)) {
//		currentMeasure++;
//		if (nextTick == partTimes.get(currentPart + 1)) {
//			currentPart++;
//		}
//	}
//}

//public Part(int channel, Song s) {
//currentNote = 0;
//currentChord = 0;
//inChord = false;
//numInChord = 0;
//currentMeasure = 0;
//currentPart = 0;
//
//outputTracks = new ArrayList<Track>();
//chords = new ArrayList<ArrayList<Integer> >();
//
//this.channel = channel;
//Sequence seq = s.getInputTrack(channel);
//ArrayList<Sequence> outputSeq = s.getOutputTracks();
//
//Track[] t = seq.getTracks();
//track = t[0];
//
//// make sure all events are 0x90
//for (int i = 0; i < track.size();) {
//	MidiEvent ev = track.get(i);
//	MidiMessage m = ev.getMessage();
//	
//	if (m instanceof ShortMessage) {
//		ShortMessage sm = (ShortMessage)m;
//
//		if (sm.getCommand() != ShortMessage.NOTE_ON) {
//		//	System.out.println("Not Note_on: " + sm.getCommand());
//			track.remove(ev);
//		} else {
//		//	System.out.println("NOTE_ON");
//			i++;
//		}
//	} else {
//	//	System.out.println("Not ShortMessage");
//		i++;
//	}
//}
//
//// add other tracks if needed
//for (int i = 1; i < t.length; i++) {
//	//System.out.println(t[i].size());
//	for (int j = 0; j < t[i].size(); j++) {
//		
//		// make sure event is 0x90
//		MidiEvent ev = t[i].get(i);
//		MidiMessage m = ev.getMessage();
//		
//		if (m instanceof ShortMessage) {
//			ShortMessage sm = (ShortMessage)m;
//		//	System.out.println(sm.getCommand());
//			if (sm.getCommand() == 0x90) {
//				System.out.println("NOTE_ON");
//				track.add(ev);
//			}
//		}
//	}
//}
//
//System.out.println("Track length: " + track.size());
//
//// populate chords
//long previousTime = 0;
//int numChords = 0;
//chords.add(new ArrayList<Integer>(2));
//boolean inChord = false;
//for (int i = 1; i < track.size(); i++) {
//	// get current time
//	long currentTime = track.get(i).getTick();
//	System.out.println(track.get(i).getTick());
//	
//	// check if the past two notes have the same time
//	if (currentTime == previousTime) {
//		// the past two notes are played at the same time (a chord)
//		
//		// check if we are already in a chord by checking chords
//		if (inChord) {
//			// previous chord is unfinished
//			
//			// check if this is the last note in a chord
//			long nextTime = track.get(i + 1).getTick();
//			if (currentTime != nextTime) {
//				// chord is ending
//				chords.get(numChords).add(i);
//				inChord = false;
//				System.out.println("Chord ends at " + i);
//			}
//		} else {
//			// we must be at the second note in a chord
//			// start new chord in chords
//			chords.add(new ArrayList<Integer>(2));
//			numChords++;
//			inChord = true;
//			chords.get(numChords).add(i - 1);
//			System.out.println("Chord begins at " + (i - 1));
//		}
//		
//	}
//}
//
//// turn sequences in outputSeq into tracks
//for (int i = 0; i < outputSeq.size(); i++) {
//	t = outputSeq.get(i).getTracks();
//	outputTracks.add(t[0]);
//	for (int j = 1; j < t.length; j++) {
//		for (int h = 0; h < t[j].size(); h++) {
//			outputTracks.get(i).add(t[j].get(h));
//		}
//	}
//}
//
//this.partTimes = s.getPartTimes();
//this.measureTimes = s.getMeasureTimes();
//}

