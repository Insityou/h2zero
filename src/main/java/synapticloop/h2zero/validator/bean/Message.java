package synapticloop.h2zero.validator.bean;

public class Message {
	private String type;
	private String message;

	public Message(String type, String Message) {
		this.type = type;
		this.message = Message;
	}

	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
}
