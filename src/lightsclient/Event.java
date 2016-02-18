package lightsclient;

public class Event {
	private int[] notes;
	private long time;
	private int numInChord;
	
	public Event(String line) {
		String[] elements = line.split(" ");
	
		time = Long.parseLong(elements[0]);

		// get notes
		notes = new int[elements.length - 1];
		for (int i = 1; i < elements.length; i++) {
			notes[i - 1] = Integer.parseInt(elements[i]);
		}
		
		numInChord = 0;
	}
	
	public boolean contains(int note) {
		
		for (int current : notes) {
			if (noteEquals(note, current)) {
				numInChord++;
				
				return true;
			}
		}
		
		return false;
	}
	
	private boolean noteEquals(int input, int current) {
		if ((input >= current - 2) && (input <= current + 2)) {
			return true;
		}
		
		return false;
	}
	
	public boolean isDone() {
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
