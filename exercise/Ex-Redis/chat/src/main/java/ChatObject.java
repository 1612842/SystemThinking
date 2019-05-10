
import java.io.Serializable;

public class ChatObject implements Serializable {
    String channel;
    String user;
    String message;
    String date;

    public ChatObject(String channel, String user, String message, String date) {
        this.channel = channel;
        this.user = user;
        this.message = message;
        this.date = date;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return date +"->"+user+": "+message+"\n";
    }
}