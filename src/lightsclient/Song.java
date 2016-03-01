package lightsclient;

import java.util.ArrayList;

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
	
	// give parts what they need
	// output times, measures, parts, etc
	public void done() {
		
		// give parts measures, parts, and output times
		for (Part p : input) {
			p.addMeasures(measures);
			p.addParts(parts);
			
			for (OutputPart op : output) {
				long[] times = op.getTimes();
				p.addOutputTimes(times);
			}
			
			p.addMeasures(measures);
			p.addParts(parts);
		}
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
	
	// get number of input parts
	public int numInput() {
		return input.size();
	}
	
	// TODO: Figure out how I want to handle this exactly
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
	
	// set measures
	public void setMeasures(ArrayList<Long> m) {
		measures = m;
	}
	
	// get parts
	public ArrayList<Long> getParts() {
		return parts;
	}
	
	// set parts
	public void setParts(ArrayList<Long> p) {
		parts = p;
	}

	public int numMIDIOutput() {
		// -1 because the last is the output for the phone
		return output.size() - 1;
	}
}
