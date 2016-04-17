package lightsclient;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class OutputPart {

	private int channel;
	private Receiver receiver;
	
	private int currentNote = 0;
	private ArrayList<Event> notes;
	boolean done = false;
	
	private long[] currentInputTimes;
	private long[] timesUntilNextEvent;
	private boolean[] listen;
	
	private LinkedBlockingQueue<MyMessage> input;
	

	public OutputPart(String[] lines, int channel) {
		notes = new ArrayList<Event>(lines.length);

		for (String line : lines) {
			if (!line.equals("")) {
				notes.add(new Event(line));
			}
		}
		
		this.channel = channel;
	}

	public long[] getTimes() {
		long[] ret = new long[notes.size()];

		for (int i = 0; i < notes.size(); i++) {
			ret[i] = notes.get(i).getTime();
		}

		return ret;
	}

	public void setNumInput(int numInput) {
		currentInputTimes = new long[numInput];
		timesUntilNextEvent = new long[numInput];
		
		listen = new boolean[numInput];
		for (int i = 0; i < numInput; i++) {
			listen[i] = false;
		}
	}

	/**
	 * @return the output
	 */
	public Receiver getOutput() {
		return receiver;
	}

	/**
	 * @param output
	 *            the output to set
	 */
	public void setOutput(Receiver output) {
		this.receiver = output;

		// output notes at time 0
		if (notes.get(currentNote).getTime() == 0) {
			Event currentEvent = notes.get(currentNote);
			// construct MIDI message
			// MidiMessage message = new ShortMessage(ShortMessage.NOTE_ON, 0, ,
			// 97);
			for (int note : currentEvent.getAllNotes()) {
				try {
					MidiMessage message = new ShortMessage(ShortMessage.NOTE_ON, 0, note, 97);
					output.send(message, -1);
				} catch (InvalidMidiDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			currentNote++;
		}
	}

	private boolean ready() {
		long noteTime = notes.get(currentNote).getTime();
		for (long current : currentInputTimes) {
			if (noteTime > current) {
				return false;
			}
		}
		return true;
	}
	
	public int getChannel() {
		return channel;
	}
	
	public void play(LinkedBlockingQueue<MyMessage> input) {
		this.input = input;
		
		while (!done) {
			MyMessage message = null;
			try {
				message = input.poll(1, TimeUnit.MICROSECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (message != null) {
				long partTime = (long)message.getData1();
				int partChannel = message.getChannel();
				currentInputTimes[partChannel] = partTime;
				checkToSend();
			}
		}
	}
	
	private void checkToSend() {
		// send event if ready
		while (ready()) {
			Event currentEvent = notes.get(currentNote);
			for (int note : currentEvent.getAllNotes()) {
				try {
					MidiMessage message = new ShortMessage(ShortMessage.NOTE_ON, 0, note, 97);
					receiver.send(message, -1);
				} catch (InvalidMidiDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			currentNote++;
		}
	}
}
