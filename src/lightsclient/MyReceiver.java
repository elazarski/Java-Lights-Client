package lightsclient;

import java.util.concurrent.SynchronousQueue;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MyReceiver implements Receiver {
	
	private SynchronousQueue<Byte[]> queue;
	private Part part;

	public static MyReceiver newInstance(Part p) {
		MyReceiver ret = new MyReceiver();
		ret.setPart(p);
		
		return ret;
	}
	
	
	private void setPart(Part p) {
		this.part = p;
	}
	
		@Override
	public void close() {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		// check for note on before sending to threadFunc
		if (message instanceof ShortMessage) {
			ShortMessage sm = (ShortMessage)message;
			int command = sm.getCommand();
			
			if (command == 0x90) {
				if (part.isNext(sm.getData1())) {
					System.out.println("Correct note!");
				}
			}
		}
	}
	

}
