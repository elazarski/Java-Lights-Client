package lightsclient;

import java.util.concurrent.SynchronousQueue;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MyReceiver implements Receiver {
	
	private SynchronousQueue<Long> queue;
	private Part part;

	public static MyReceiver newInstance(Part p, SynchronousQueue<Long> q) {
		MyReceiver ret = new MyReceiver();
		ret.setPart(p);
		ret.setQueue(q);
		
		return ret;
	}
	
	private void setQueue(SynchronousQueue<Long> q) {
		queue = q;
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
			
			// check if done
			if (part.isDone()) {
				sendData((long) -1);
				System.out.println("SONG DONE: RECIEVER " + part.getChannel());
			}
		}
	}
	
	private void sendData(Long data) {
		queue.add(data);
	}

}
