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
	private long[] nextInputTimes;
	private boolean[] listen;

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
		nextInputTimes = new long[numInput];

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
		// this.input = input;

		// System.out.println(Thread.currentThread().getName());

		while (!done) {
			MyMessage message = null;
			try {
				message = input.poll(1, TimeUnit.MICROSECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (message != null) {
				// System.out.println("GOT MESSAGE");
				long partTime = (long) message.getData1();
				long nextTime = (long) message.getData1();
				int partChannel = message.getChannel();
				currentInputTimes[partChannel] = partTime;
				nextInputTimes[partChannel] = nextTime;

				// check if we should listen to this part for now
				long timeDiff = nextTime - partTime;
				if (timeDiff > 0 && timeDiff > 16000) {
					// use 16000 for now
					// this is approximately the time for a measure
					// in book
					listen[partChannel] = false;
				}

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

	public void notify(MyMessage message) {

	}
}
