package lightsclient;

public class MyMessage {
	static enum Type {
		PART_DONE, TIME_UPDATE, SYSTEM_EXIT, SONG_UPDATE, READ_FILE, MIDI_SELECTION, SETLIST_REORDER, START, STOP
	}
	
	private int channel = 0;
	private Type type = null;
	private Object data1 = null;
	private Object data2 = null;
	
	public MyMessage(Type type) {
		this.type = type;
	}
	
	public MyMessage(Object data1) {
		this.data1 = data1;
	}
	
	public MyMessage(Object data1, Object data2) {
		this.data1 = data1;
		this.data2 = data2;
	}
	
	public MyMessage(Type type, Object data1) {
		this.type = type;
		this.data1 = data1;
	}
	
	public MyMessage(Type type, Object data1, Object data2) {
		this.type = type;
		this.data1 = data1;
		this.data2 = data2;
	}
	
	public MyMessage(int channel, Type type) {
		this.channel = channel;
		this.type = type;
	}
	
	public MyMessage(int channel, Type type, Object data) {
		this.channel = channel;
		this.type = type;
		this.data1 = data;
	}
	
	public MyMessage(int channel, Type type, Object data1, Object data2) {
		this.channel = channel;
		this.type = type;
		this.data1 = data1;
		this.data2 = data2;
	}

	/**
	 * @return the channel
	 */
	public int getChannel() {
		return channel;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @return the data1
	 */
	public Object getData1() {
		return data1;
	}

	/**
	 * @param data1 the data1 to set
	 */
	public void setData1(Object data1) {
		this.data1 = data1;
	}

	/**
	 * @return the data2
	 */
	public Object getData2() {
		return data2;
	}

	/**
	 * @param data2 the data2 to set
	 */
	public void setData2(Object data2) {
		this.data2 = data2;
	}
}
