package lightsclient;

public class Phrase {
	private int[] notes;

	public Phrase(int[] indexes) {
		notes = indexes;
	}

	public Phrase(int startIndex, int endIndex) {
		notes = new int[endIndex - startIndex];

		for (int i = 0; i < endIndex - startIndex; i++) {
			notes[i] = startIndex + i;
		}
	}

	public static Phrase[] generate(Event[] events) {
		Phrase[] temp = new Phrase[events.length];

		// get average time between events
		double averageTime = 0;
		long t1 = events[0].getTime();
		for (int i = 1; i < events.length; i++) {
			long t2 = events[i].getTime();
			averageTime += t2 - t1;
			t1 = t2;
		}
		averageTime = averageTime / (double) events.length;
		System.out.println(averageTime);

		// attempt splitting up by time between notes
		boolean timeSplit = false;
		int startIndex = 0;
		int currentPhrase = 0;
		for (int i = 1; i < events.length; i++) {
			long timeDiff = events[i].getTime() - events[i - 1].getTime();
			// System.out.println(events[i].getTime() + "-" + events[i -
			// 1].getTime() + "=" + timeDiff);
			if (timeDiff > averageTime) {
				timeSplit = true;
				int endIndex = i;
				temp[currentPhrase] = new Phrase(startIndex, endIndex);
				System.out.println(startIndex + "->" + endIndex);
				startIndex = i + 1;
				currentPhrase++;
			}
		}

		Phrase[] ret = new Phrase[currentPhrase + 1];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = temp[i];
		}
		ret[currentPhrase] = new Phrase(startIndex, events.length);
		System.out.println(startIndex + "->" + events.length);

		return ret;
	}

	public void offset(int offset) {
		for (int i = 0; i < notes.length; i++) {
			notes[i] += offset;
		}
	}

	public int length() {
		return notes.length;
	}

	public int getNote(int index) {
		if (index < notes.length) {
			return notes[index];
		}

		return -1;
	}
}
