package lightsclient;

import java.util.ArrayList;

import javax.sound.midi.Sequence;

public class Song {

	private String title;
	private ArrayList<Sequence> inputTracks;
	private ArrayList<Sequence> outputTracks;
	private long tickLength;
	private ArrayList<Long> partTimes;
	private ArrayList<Long> measureTimes;
	
	// initialize title and ArrayList<Sequence>'s to "new ArrayList<Sequence>();"
	public Song(String title) {
		// initialize variables
		inputTracks = new ArrayList<Sequence>();
		outputTracks = new ArrayList<Sequence>();
		this.title = title;
	}
	
	// get number of input and output tracks
	public int numInput() {
		return inputTracks.size();
	}
	
	public int numOutput() {
		return outputTracks.size();
	}
	
	// add input track
	public void addInput(Sequence track) {
		inputTracks.add(track);
	}
	
	// add outputTrack
	public void addOutput(Sequence track) {
		outputTracks.add(track);
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
	public Sequence getInputTrack(int track) {
		return inputTracks.get(track);
	}
	
	// get specific output track
	public Sequence getOutputTrack(int track) {
		return outputTracks.get(track);
	}
	
	public ArrayList<Sequence> getOutputTracks() {
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
