package lightsclient;

import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class OutputPart {

	int currentNote = 0;
	
	private ArrayList<Event> notes;
	private long[] inputTimes;
	private Receiver output;

	public OutputPart(String[] lines) {
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
	
	public void setNumInput(int numInput) {
		inputTimes = new long[numInput];
	}

	/**
	 * @return the output
	 */
	public Receiver getOutput() {
		return output;
	}

	/**
	 * @param output
	 *            the output to set
	 */
	public void setOutput(Receiver output) {
		this.output = output;
		
		// output notes at time 0
		if (notes.get(currentNote).getTime() == 0) {
			Event currentEvent = notes.get(currentNote);
			// construct MIDI message
//			MidiMessage message = new ShortMessage(ShortMessage.NOTE_ON, 0, , 97);
			for (int note : currentEvent.getAllNotes()) {
				try {
					MidiMessage message = new ShortMessage(ShortMessage.NOTE_ON, 0, note, 97);
					output.send(message, -1);
				} catch (InvalidMidiDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	
	public void checkToSend(Long currentTime, int channel) {
		inputTimes[channel] = currentTime;
		
		// chack to see that most are past the time of the current note
		long noteTime = notes.get(currentNote).getTime();
		int numInput = inputTimes.length;
		
	}
}
