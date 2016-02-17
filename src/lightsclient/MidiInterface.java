package lightsclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class MidiInterface {
	
	private MidiDevice.Info[] infos;
	private ArrayList<MidiDevice> inputDevices;
	private ArrayList<Transmitter> inputTransmitters;
	private ArrayList<MidiDevice> outputDevices;
	private ArrayList<Receiver> outputReceivers; 
	
	private SynchronousQueue<byte[]> queue;
	
	public MidiInterface() {
		infos = MidiSystem.getMidiDeviceInfo();
		
		inputDevices = new ArrayList<MidiDevice>();
		inputTransmitters = new ArrayList<Transmitter>();
		outputDevices = new ArrayList<MidiDevice>();
		outputReceivers = new ArrayList<Receiver>();
	}
	
	public String[] getInputNames() {
		ArrayList<String> listRet = new ArrayList<String>();
		
		// code found at: http://stackoverflow.com/questions/6937760/java-getting-input-from-midi-keyboard
		for (int i = 0; i < infos.length; i++) {
			try {
				MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
				
				// get name if it is an output port (available input)
				if (device.getMaxTransmitters() != 0) {
					listRet.add(device.getDeviceInfo().getName().toString());
				}
			} catch (MidiUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String[] ret = new String[listRet.size()];
		ret = listRet.toArray(ret);
		return ret;
	}
	
	public String[] getOutputNames() {
		ArrayList<String> listRet = new ArrayList<String>();
		
		// code found at: http://stackoverflow.com/questions/6937760/java-getting-input-from-midi-keyboard
		for (int i = 0; i < infos.length; i++) {
			try {
				MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
				
				// get name if this is an input port (available output)
				if (device.getMaxReceivers() != 0) {
					listRet.add(device.getDeviceInfo().getName().toString());
				}
			} catch (MidiUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		String[] ret = new String[listRet.size()];
		ret = listRet.toArray(ret);
		return ret;
	}
	
	public void connect(byte[] selectionObj) {
		try {
			MidiSelection selection = MidiSelection.deserialize(selectionObj);
			
			// set sizes for MIDI I/O ArrayLists
			int maxInputChannel = selection.getMaxInputChannel() + 1;
			int maxOutputChannel = selection.getMaxOutputChannel();
			inputDevices = new ArrayList<MidiDevice>(maxInputChannel);
			inputTransmitters = new ArrayList<Transmitter>(maxInputChannel);
			outputDevices = new ArrayList<MidiDevice>(maxOutputChannel);
			outputReceivers = new ArrayList<Receiver>(maxOutputChannel);
			
			for (int i = -1; i < maxInputChannel; i++) {
				inputDevices.add(null);
				inputTransmitters.add(null);				
			}
			for (int i = -1; i < maxOutputChannel; i++) {
				outputDevices.add(null);
				outputReceivers.add(null);
			}
			
			// attempt to connect based upon name
			for (int i = 0; i < infos.length; i++) {
				MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
				 
				// get name of device
				String name = device.getDeviceInfo().getName().toString();
				
				// check for output port (available input)
				if (device.getMaxTransmitters() != 0) {					
					// get channel based upon name
					int channel = selection.getInputChannel(name);
					
					// connect if channel is >= 0
					if (channel >= 0) {
						inputTransmitters.set(channel, device.getTransmitter());
						inputDevices.set(channel, device);
					}
				} else if (device.getMaxReceivers() != 0) { // input port (available output)
					// get channel based upon name
					int channel = selection.getOutputChannel(name);
					
					// connect if channel is >= 1
					if (channel >= 0) {
						outputReceivers.set(channel, device.getReceiver());
						outputDevices.set(channel, device);
					}
				}
			}
			
			// connect to input and output devices
			// TODO: REMOVE - 1 AND DEAL WITH CONTROL INPUT
			for (int i = 0; i < inputDevices.size() - 1; i++) {
				inputDevices.get(i).open();
			}
			for (int i = 0; i < outputDevices.size(); i++) {
				outputDevices.get(i).open();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		
	}
	
	public void play(Setlist setlist, SynchronousQueue<byte[]> q) {
		queue = q;
	
		// play songs
		for (int i = 0; i < setlist.getNumSongs(); i++) {
			Song s = setlist.getSong(i);
			
			// TODO: update main, which will update UI
		//	queue.add(new byte[] {0x1});
		//	queue.add(s.toString().getBytes());
			
			// create MyReceiver objects for each required input
			int numInput = s.numInput();
			for (int j = 0; j < numInput; j++) {
				inputTransmitters.get(j).setReceiver(MyReceiver.newInstance(s.getInput(i)));
			}
			
			while (true) {}
			
		}
		
		// close devices
		for (int i = 0; i < inputDevices.size(); i++) {
			inputDevices.get(i).close();
		}
	}
	
}
