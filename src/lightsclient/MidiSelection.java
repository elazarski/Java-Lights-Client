package lightsclient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

	
	// code found at: http://stackoverflow.com/questions/5837698/converting-any-object-to-a-byte-array-in-java
	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		
		o.writeObject(obj);
		return b.toByteArray();
	}
	
	public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}
}
