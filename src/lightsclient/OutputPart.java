package lightsclient;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

public class OutputPart {
	
	private int channel;
	private ArrayList<Event> notes;
	
	public OutputPart(int channel, String[] lines) {
		this.channel = channel;
		notes = new ArrayList<Event>(lines.length);
		
		for (String line : lines) {
			notes.add(new Event(line));
		}
	}
	
	public long[] getTimes() {
		long[] ret = new long[notes.size()];
		
		for (int i = 0; i < notes.size(); i++) {
			ret[i] = notes.get(i).getTime();
		}
		
		return ret;
	}
}
