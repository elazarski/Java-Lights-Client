package lightsclient;

public class Phrase {
	public static int[] generate(Event[] events) {
		int[] temp = new int[events.length];

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
		// boolean timeSplit = false;
		int startIndex = 0;
		int currentPhrase = 0;
		for (int i = 1; i < events.length; i++) {
			long timeDiff = events[i].getTime() - events[i - 1].getTime();
			// System.out.println(events[i].getTime() + "-" + events[i -
			// 1].getTime() + "=" + timeDiff);
			if (timeDiff > averageTime) {
				// timeSplit = true;
				// int endIndex = i - 1;
				temp[currentPhrase] = startIndex;
				// System.out.println(startIndex + "->" + endIndex);
				startIndex = i;
				currentPhrase++;
			}
		}

		int[] ret = new int[currentPhrase + 1];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = temp[i];
		}
		ret[currentPhrase] = startIndex;
		// System.out.println(startIndex + "->" + (events.length - 1));

		return ret;
	}
	
	public static boolean similar(Event[] phrase, Event[] recentEvents) {
		boolean ret = false;
		
		
		
		return ret;
	}
}
