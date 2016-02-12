package lightsclient;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

public class OutputPart {
	
	private int channel;
	private ArrayList<Pair<Long, ArrayList<Integer>>> notes;
	
	public OutputPart(int channel, String[] lines) {
		this.channel = channel;
		notes = new ArrayList<Pair<Long, ArrayList<Integer>>>();
		
		for (String line : lines) {
			System.out.println(line);
			String[] elements = line.split(" ");
			
			Long time = new Long(elements[0]);
			ArrayList<Integer> chord = new ArrayList<Integer>();
			for (int i = 1; i < elements.length; i++) {
				chord.add(new Integer(elements[i]));
			}
			
			Pair p = Pair.of(time,  chord);
			notes.add(p);
		}
	}
}
