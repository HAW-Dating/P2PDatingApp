package de.haw_landshut.haw_dating.p2pdatingapp.data;

import com.google.gson.Gson;

/**
 * Created by s-gheldd on 6/24/15.
 */
public class ChatMessage {

    private static Gson gson = new Gson();

    private String text;
    private String sender;
    private String senderUUID;


    public ChatMessage(String text, String sender, String senderUUID) {
        this.text = text;
        this.sender = sender;
        this.senderUUID = senderUUID;
    }

    public static ChatMessage chatMessageFromJson(String json){
        return gson.fromJson(json, ChatMessage.class);
    }


    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public String getSenderUUID() {
        return senderUUID;
    }

    public String toJson(){
        return gson.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMessage that = (ChatMessage) o;

        if (!text.equals(that.text)) return false;
        if (!sender.equals(that.sender)) return false;
        return senderUUID.equals(that.senderUUID);

    }

    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + sender.hashCode();
        result = 31 * result + senderUUID.hashCode();
        return result;
    }
}
