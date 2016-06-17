package de.haw_landshut.haw_dating.p2pdatingapp.data;

import com.google.gson.Gson;

import java.security.MessageDigest;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.haw_landshut.haw_dating.sealedbottle.api.BottleCryptoConstants;

/**
 * Created by s-gheldd on 17.06.16.
 */
public class WifiMessage {
    private static final Gson gson = new Gson();
    private final String serializedMessageInABottle;
    private final UUID uuid;
    private byte[] encryptedMessage;

    public static WifiMessage createWifiMessage(String serializedMessageInABottle, SecretKeySpec aesKey, String message) {
        final WifiMessage wifiMessage = new WifiMessage(serializedMessageInABottle);
        try {
            final Cipher cipher = Cipher.getInstance(BottleCryptoConstants.TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, BottleCryptoConstants.IV_PARAMETER_SPEC);
            wifiMessage.encryptedMessage = cipher.doFinal(message.getBytes(BottleCryptoConstants.CHARSET));

            return wifiMessage;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    private WifiMessage(String serializedMessageInABottle) {

        uuid = UUID.randomUUID();
        this.serializedMessageInABottle = serializedMessageInABottle;
    }

    public String getSerializedMessageInABottle() {
        return serializedMessageInABottle;
    }

    public UUID getUuid() {
        return uuid;
    }

    public byte[] getEncryptedMessage() {
        return encryptedMessage;
    }

    public String serialize() {
        return gson.toJson(this);
    }

    public static WifiMessage deserialize(String json) {
        return gson.fromJson(json, WifiMessage.class);
    }


}
