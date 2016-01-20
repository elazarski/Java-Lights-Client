package lightsclient;

public class MidiSelection {
	
	private int[] inputChannels;
	private int[] outputChannels;
	private String[] inputNames;
	private String[] outputNames;
	
	public MidiSelection(String[] inputNames, int[] inputChannels, String[] outputNames, int[] outputChannels) {
		this.inputNames = inputNames;
		this.inputChannels = inputChannels;
		this.outputNames = outputNames;
		this.outputChannels = outputChannels;
	}
	
	public int[] getInputChannels() {
		return inputChannels;
	}
	
	public String[] getInputNames() {
		return inputNames;
	}
	
	public int[] getOutputChannels() {
		return outputChannels;
	}
	
	public String[] getOutputNames() {
		return outputNames;
	}

}
