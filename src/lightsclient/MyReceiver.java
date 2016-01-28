package lightsclient;

import java.util.concurrent.SynchronousQueue;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class MyReceiver implements Receiver {
	
	private SynchronousQueue<Byte> thread;

	public static MyReceiver newInstance(SynchronousQueue<Byte> q) {
		MyReceiver ret = new MyReceiver();
		ret.setQueue(q);
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				threadFunc();
			}
		});
		
		return ret;
	}
	
	private void setQueue(SynchronousQueue<Byte> q) {
		this.thread = q;
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		// TODO Auto-generated method stub
		
	}
	
	private static void threadFunc() {
		
	}

}
