package de.haw_landshut.haw_dating.p2pdatingapp.data;

import com.google.gson.Gson;

import java.util.UUID;

/**
 * Created by s-gheldd on 17.06.16.
 */
public class WifiMessage {
    private static final Gson gson = new Gson();
    private final String serializedMessageInABottle;
    private final UUID uuid;

    public WifiMessage(String serializedMessageInABottle) {

        uuid = UUID.randomUUID();
        this.serializedMessageInABottle = serializedMessageInABottle;
    }

    public String getSerializedMessageInABottle() {
        return serializedMessageInABottle;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String serialize() {
        return gson.toJson(this);
    }

    public static WifiMessage deserialize(String json) {
        return gson.fromJson(json, WifiMessage.class);
    }
}
