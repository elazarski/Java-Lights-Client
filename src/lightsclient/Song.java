package lightsclient;

import java.util.ArrayList;

import javax.sound.midi.Sequence;

public class Song {

	private String title;
	private ArrayList<Part> input;
	private ArrayList<OutputPart> output;
	private ArrayList<Long> measures;
	private ArrayList<Long> parts;
	
	public Song(String title) {
		this.title = title;
		
		input = new ArrayList<Part>();
		output = new ArrayList<OutputPart>();
		measures = new ArrayList<Long>();
		parts = new ArrayList<Long>();
	}
	
	// override toString()
	@Override
	public String toString() {
		return title;
	}
	
	// add input
	public void addInput(Part p) {
		input.add(p);
	}
	
	// get input
	public Part getInput(int index) {
		return input.get(index);
	}
	
	// get all input parts
	public ArrayList<Part> getAllInputParts() {
		return input;
	}
	
	// add output
	public void addOutput(OutputPart o) {
		output.add(o);
		
		// add times to input parts
		for (Part p : input) {
			
		}
	}
	
	// get output
	public OutputPart getOutput(int index) {
		return output.get(index);
	}
	
	// get measures
	public ArrayList<Long> getMeasures() {
		return measures;
	}
	
	// get parts
	public ArrayList<Long> getParts() {
		return parts;
	}
}
