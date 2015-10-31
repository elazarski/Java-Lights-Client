package lightsclient;

import java.util.ArrayList;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class MidiInterface {
	
	private ArrayList<MidiDevice> inputDevices;
	private ArrayList<Transmitter> transmitters;
	private ArrayList<Receiver> recievers;
	
	public MidiInterface() {
		inputDevices = new ArrayList<MidiDevice>();
		transmitters = new ArrayList<Transmitter>();
		recievers = new ArrayList<Receiver>();
	}
}
