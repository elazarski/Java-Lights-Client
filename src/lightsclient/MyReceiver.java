package lightsclient;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.SynchronousQueue;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MyReceiver implements Receiver {
	
	private SynchronousQueue<Byte[]> queue;
	private Part part;
	private Thread thread;

	public static MyReceiver newInstance(Part p) {
		MyReceiver ret = new MyReceiver();
		ret.setPart(p);
		ret.setThread(p);
		ret.initQueue();
		
		return ret;
	}
	
	private void initQueue() {
		this.queue = new SynchronousQueue<Byte[]>();
	}
	
	private void setPart(Part p) {
		this.part = p;
	}
	
	private void setThread(Part p) {
		this.thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				threadFunc(p, queue);
			}
		});
		
		thread.setName("i-" + part.getChannel());
		thread.start();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void close() {
		// TODO Auto-generated method stub
		thread.stop();
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		// check for note on before sending to threadFunc
		if (message instanceof ShortMessage) {
			ShortMessage sm = (ShortMessage)message;
			int command = sm.getCommand();
			
			if (command == 0x90) {
				ByteBuffer b = ByteBuffer.allocate(12);
				b.putInt(sm.getData1());
				b.putLong(timeStamp);
				
				byte[] prim = b.array();
				Byte[] data = new Byte[prim.length];
				
				int i = 0;
				for (byte p: prim) {
					data[i++] = p;
				}
				
				queue.add(data);
			}
		}
	}
	
	private static void threadFunc(Part p, SynchronousQueue<Byte[]> queue) {
		try {
			while (!p.done()) {
				
				// get and convert input
				Byte[] data = queue.take();
				byte[] d1 = new byte[4];
				byte[] d2 = new byte[8];
				
				for (int i = 0; i < 4; i++) {
					d1[i] = data[i];
				}
				for (int i = 4; i < 12; i++) {
					d2[i - 4] = data[i];
				}
				
				int note = ByteBuffer.wrap(d1).getInt();
				long timeStamp = ByteBuffer.wrap(d2).getLong();
				
				// check if note is correct
				if (p.isNext(note)) {
					System.out.println("CORRECT!!!");
				}
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
