package lightsclient;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import lightsclient.PlayMessage.Type;

public class MyReceiver implements Receiver {
	
	private Queue<PlayMessage> queue;
	private Part part;

	public static MyReceiver newInstance(Part p, LinkedBlockingQueue<PlayMessage> playQueue) {
		MyReceiver ret = new MyReceiver();
		ret.setPart(p);
		ret.setQueue(playQueue);
		
		return ret;
	}
	
	private void setQueue(LinkedBlockingQueue<PlayMessage> playQueue) {
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
			
			if (command == 0x90) {
				if (part.isNext(sm.getData1())) {
					sendData(new PlayMessage(part.getChannel(), Type.OUTPUT_READY));
				}
			}
			
			// check if done
			if (part.isDone()) {
				sendData(new PlayMessage(part.getChannel(), Type.PART_DONE));
				System.out.println("SONG DONE: RECIEVER " + part.getChannel());
			}
		}
	}
	
	private void sendData(PlayMessage data) {
		boolean x = queue.offer(data);
		System.out.println(x);
	}

}
