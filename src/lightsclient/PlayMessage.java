package lightsclient;

public class PlayMessage {
	public static enum Type {
		PART_DONE, OUTPUT_READY
	}
	
	private int channel;
	private Type type;
	private long data;
	
	public PlayMessage(int channel, Type type) {
		this.channel = channel;
		this.type = type;
	}
	
	public PlayMessage(int channel, Type type, long data) {
		this.channel = channel;
		this.type = type;
		this.data = data;
	}

	/**
	 * @return the channel
	 */
	public int getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(int channel) {
		this.channel = channel;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the data
	 */
	public long getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(long data) {
		this.data = data;
	}
}
