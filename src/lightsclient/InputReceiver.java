package lightsclient;

import java.util.concurrent.LinkedBlockingQueue;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import lightsclient.MyMessage.Type;

public class InputReceiver implements Receiver {
	
	private LinkedBlockingQueue<MyMessage> queue;
	private Part part;

	public static InputReceiver newInstance(Part p, LinkedBlockingQueue<MyMessage> playQueue) {
		InputReceiver ret = new InputReceiver();
		ret.setPart(p);
		ret.setQueue(playQueue);
		
		return ret;
	}
	
	private void setQueue(LinkedBlockingQueue<MyMessage> playQueue) {
		queue = playQueue;
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
			
			if (command == ShortMessage.NOTE_ON) {
				if (part.isNext(sm.getData1())) {
					sendData(new MyMessage(part.getChannel(), Type.TIME_UPDATE));
				}
			}
			
			// check if done
			if (part.isDone()) {
				sendData(new MyMessage(part.getChannel(), Type.PART_DONE));
				System.out.println("SONG DONE: RECIEVER " + part.getChannel());
			}
		}
	}
	
	private void sendData(MyMessage data) {
		boolean x = queue.offer(data);
		System.out.println(x);
	}
	
	// notify with MyMessage
	public void notify(MyMessage message) {
		System.out.println(Thread.currentThread().getName());
		
		// parse message
		int channel = message.getChannel();
		if (channel == 1) {						// next part
			
		} else if (channel == 2) {				// next measure
			
		} else if (channel == -1) {				// previous part
			
		} else if (channel == -2) {				// previous measure
			
		}
	}

}
