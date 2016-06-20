package de.haw_landshut.haw_dating.p2pdatingapp.chat;

public class ChatBubble {
    public String message;
    public String sender;
    public int type; // 1 = send bubble, 0 = rcv bubble, 2 = info


    public ChatBubble(String sender, String message, int type) {
        this.message = message;
        this.sender = sender;
        this.type = type;
    }
}
