package lightsclient;

import java.util.ArrayList;

import javax.sound.midi.Sequence;

public class Song {

	private String title;
	private ArrayList<Part> inputTracks;
	private ArrayList<Part> outputTracks;
	private long tickLength;
	private ArrayList<Long> partTimes;
	private ArrayList<Long> measureTimes;
	
	// initialize title and ArrayList<Sequence>'s to "new ArrayList<Sequence>();"
	public Song(String title) {
		// initialize variables
	}
	
	// get number of input and output tracks
	public int numInput() {
		return inputTracks.size();
	}
	
	public int numOutput() {
		return outputTracks.size();
	}
	
	// add input track
	public void addInput(Part p) {
		inputTracks.add(p);
	}
	
	// add outputTrack
	public void addOutput(Part p) {
		outputTracks.add(p);
	}
	
	// add partTimes
	public void addPartTimes(ArrayList<Long> p) {
		partTimes = p;
	}
	
	// add measure Times
	public void addMeasureTimes(ArrayList<Long> m) {
		measureTimes = m;
	}
	
	// set tickLength
	public void setTickLength(long l) {
		tickLength = l;
	}

	// get tickLength
	public long getTickLength() {
		return tickLength;
	}

	// get specific input track
	public Part getInputTrack(int index) {
		return inputTracks.get(index);
	}
	
	// get specific output track
	public Part getOutputTrack(int index) {
		return outputTracks.get(index);
	}
	
	public ArrayList<Part> getOutputTracks() {
		return outputTracks;
	}
	
	public ArrayList<Long> getPartTimes() {
		return partTimes;
	}
	
	public ArrayList<Long> getMeasureTimes() {
		return measureTimes;
	}
	
	// override toString()
	@Override
	public String toString() {
		return title;
	}
}
