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
