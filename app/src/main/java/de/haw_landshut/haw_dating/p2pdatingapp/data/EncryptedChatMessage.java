package de.haw_landshut.haw_dating.p2pdatingapp.data;

import com.google.gson.Gson;

import javax.crypto.spec.IvParameterSpec;

/**
 * Created by Georg on 05.07.2016.
 */
public class EncryptedChatMessage {
    private static final Gson gson = new Gson();

    private final byte[] encryptedJson;
    private final IvParameterSpec iv;

    public EncryptedChatMessage(byte[] encryptedJson, IvParameterSpec iv) {
        this.encryptedJson = encryptedJson.clone();
        this.iv = iv;
    }

    public String serialize() {
        return gson.toJson(this);
    }

    public static EncryptedChatMessage deSerialize(final String json) {
        return gson.fromJson(json, EncryptedChatMessage.class);
    }

    public byte[] getEncryptedJson() {
        return encryptedJson;
    }

    public IvParameterSpec getIv() {
        return iv;
    }
}
