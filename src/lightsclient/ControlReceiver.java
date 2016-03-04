package lightsclient;

import java.util.concurrent.LinkedBlockingQueue;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class ControlReceiver implements Receiver {

	private LinkedBlockingQueue<MyMessage> out;
	
	public static ControlReceiver newInstance(LinkedBlockingQueue<MyMessage> q) {
		ControlReceiver ret = new ControlReceiver();
		ret.setQueue(q);
		
		return ret;
		
	}
	
	private void setQueue(LinkedBlockingQueue<MyMessage> q) {
		this.out = q;
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		// TODO Auto-generated method stub
		
	}

}
