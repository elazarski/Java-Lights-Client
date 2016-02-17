package lightsclient;

public class Event {
	private int[] notes;
	private long time;
	private int numInChord;
	
	public Event(String line) {
		String[] elements = line.split(" ");
		time = Long.parseLong(elements[0]);
		notes = new int[elements.length - 1];
		// get notes
		for (int i = 1; i < elements.length; i++) {
			notes[i - 1] = Integer.parseInt(elements[i]);
		}
		
		numInChord = 0;
	}
	
	public boolean[] contains(int note) {
		boolean[]ret = {false, false};
		
		for (int current : notes) {
			if (noteEquals(note, current)) {
				ret[0] = true;
				numInChord++;
				
				ret[1] = checkDone();
				return ret;
			}
		}
		
		return ret;
	}
	
	private boolean noteEquals(int input, int current) {
		if ((input >= current - 2) && (input <= current + 2)) {
			return true;
		}
		
		return false;
	}
	
	private boolean checkDone() {
		if (numInChord >= notes.length) {
			return true;
		} else {
			return false;
		}
	}
	
	public long getTime() {
		return time;
	}
}
