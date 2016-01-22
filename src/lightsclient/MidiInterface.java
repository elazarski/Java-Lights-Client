package lightsclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

import lightsclient.MidiSelection;

public class MidiInterface {
	
	private MidiDevice.Info[] infos;
	private ArrayList<MidiDevice> inputDevices;
	private ArrayList<MidiDevice> outputDevices;
	private ArrayList<Transmitter> transmitters;
	
	public MidiInterface() {
		infos = MidiSystem.getMidiDeviceInfo();
		
		inputDevices = new ArrayList<MidiDevice>();
		outputDevices = new ArrayList<MidiDevice>();
		transmitters = new ArrayList<Transmitter>();
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
	
	public boolean connect(byte[] selectionObj) {
		boolean ret = false;
		try {
			MidiSelection selection = MidiSelection.deserialize(selectionObj);
			
			// attempt to connect based upon name
			for (int i = 0; i < infos.length; i++) {
				MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
				 
				// get name of device
				String name = device.getDeviceInfo().getName().toString();
				
				// check for output port (available input)
				if (device.getMaxTransmitters() != 0) {					
					// get channel based upon name
					int channel = selection.getInputChannel(name);
					
					// connect if channel is >= 1
					if (channel >= 1) {
						inputDevices.add(device);
					}
				} else if (device.getMaxReceivers() != 0) { // input port (available output)
					// get channel based upon name
					int channel = selection.getOutputChannel(name);
					
					// connect if channel is >= 1
					if (channel >= 1) {
						outputDevices.add(device);
					}
				}
			}
			
			// sort based upon channel for devices
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
}
