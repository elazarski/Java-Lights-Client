package lightsclient;

import java.util.ArrayList;

import javax.sound.midi.Receiver;

public class OutputPart {
	
	private int channel;
	private ArrayList<Event> notes;
	private Receiver output;
	
	public OutputPart(int channel, String[] lines) {
		this.channel = channel;
		notes = new ArrayList<Event>(lines.length);
		
		for (String line : lines) {
			if (!line.equals("")) {
				notes.add(new Event(line));
			}
		}
	}
	
	public long[] getTimes() {
		long[] ret = new long[notes.size()];
		
		for (int i = 0; i < notes.size(); i++) {
			ret[i] = notes.get(i).getTime();
		}
		
		return ret;
	}

	/**
	 * @return the output
	 */
	public Receiver getOutput() {
		return output;
	}

	/**
	 * @param output the output to set
	 */
	public void setOutput(Receiver output) {
		this.output = output;
	}
}
