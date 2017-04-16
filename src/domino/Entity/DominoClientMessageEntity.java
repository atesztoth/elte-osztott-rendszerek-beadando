package domino.Entity;

/**
 * This class is a helper class for DominoServerParamBag for storing the last message.
 * Created by atesztoth on 2017. 04. 16..
 */
public class DominoClientMessageEntity {

    private String lastMessage;
    private String sender;

    public DominoClientMessageEntity(String lastMessage, String sender) {
        this.lastMessage = lastMessage;
        this.sender = sender;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
