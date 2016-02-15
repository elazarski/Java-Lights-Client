package lightsclient;

import java.util.ArrayList;

public class Event {
	private int[] notes;
	private long time;
	
	public Event(String line) {
		String[] elements = line.split(" ");
		time = Long.parseLong(elements[0]);
		notes = new int[elements.length - 1];
		// get notes
		for (int i = 1; i < elements.length; i++) {
			notes[i - 1] = Integer.parseInt(elements[i]);
		}
	}
	
	public boolean inEvent(int note) {
		for (int current : notes) {
			if (note == current) {
				return true;
			}
		}
		
		return false;
	}
	
	public long getTime() {
		return time;
	}
}
