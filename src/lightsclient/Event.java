package lightsclient;

public class Event {
	private int[] notes;
	private long time;
	private int numInChord = 0;
	private int possibleNumInChord = 0;

	public Event(String line) {
		String[] elements = line.split(" ");

		time = Long.parseLong(elements[0]);

		// get notes
		notes = new int[elements.length - 1];
		for (int i = 1; i < elements.length; i++) {
			notes[i - 1] = Integer.parseInt(elements[i]);
		}
	}

	public Event(int[] notes, long time) {
		this.notes = notes;
		this.time = time;
	}

	public boolean isChord() {
		if (notes.length > 1) {
			return true;
		} else {
			return false;
		}
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

	public boolean possiblyContains(int note) {

		for (int current : notes) {
			if (noteEquals(note, current)) {
				possibleNumInChord++;

				return true;
			}
		}

		return false;
	}

	public void resetPossible() {
		possibleNumInChord = 0;
	}

	public void reset() {
		numInChord = 0;
		possibleNumInChord = 0;
	}

	private boolean noteEquals(int input, int current) {
		if ((input >= current - 1) && (input <= current + 1)) {
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

	public boolean possiblyIsDone() {
		if (possibleNumInChord >= notes.length) {
			return true;
		} else {
			return false;
		}
	}

	public long getTime() {
		return time;
	}
}
