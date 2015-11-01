package lightsclient;

import java.util.ArrayList;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class MidiInterface {
	
	private ArrayList<MidiDevice> inputDevices;
	private ArrayList<Transmitter> transmitters;
	
	public MidiInterface() {
		inputDevices = new ArrayList<MidiDevice>();
		transmitters = new ArrayList<Transmitter>();
	}
	
	public String[] getInputNames() {
		ArrayList<String> listRet = new ArrayList<String>();
		
		// code found at: http://stackoverflow.com/questions/6937760/java-getting-input-from-midi-keyboard
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
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
	
}
