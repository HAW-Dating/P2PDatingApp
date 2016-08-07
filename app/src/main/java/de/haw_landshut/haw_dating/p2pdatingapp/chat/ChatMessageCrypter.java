package de.haw_landshut.haw_dating.p2pdatingapp.chat;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.haw_landshut.haw_dating.p2pdatingapp.data.ChatMessage;
import de.haw_landshut.haw_dating.p2pdatingapp.data.EncryptedChatMessage;
import de.haw_landshut.haw_dating.sealedbottle.api.BottleCryptoConstants;

/**
 * Created by Georg on 05.07.2016.
 */
public class ChatMessageCrypter {
    private final Cipher cipher;
    private final SecretKeySpec key;

    public ChatMessageCrypter(final String uuid) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(BottleCryptoConstants.HASH_ALGORITHM);
            final byte[] hash = digest.digest(uuid.getBytes(BottleCryptoConstants.CHARSET));
            key = new SecretKeySpec(hash, 0, 256 / 8, BottleCryptoConstants.CRYPTO_ALGORITHM);
            cipher = Cipher.getInstance(BottleCryptoConstants.TRANSFORMATION);
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public synchronized EncryptedChatMessage encrypt(final ChatMessage chatMessage) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            final IvParameterSpec iv = new IvParameterSpec(cipher.getIV());                    ;
            final byte[] encrypted = cipher.doFinal(chatMessage.toJson().getBytes(BottleCryptoConstants.CHARSET));
            return new EncryptedChatMessage(encrypted,iv);
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public synchronized ChatMessage decrypt(final EncryptedChatMessage encryptedChatMessage) {
        try {
            cipher.init(Cipher.DECRYPT_MODE,key,encryptedChatMessage.getIv());
            final String json = new String(cipher.doFinal(encryptedChatMessage.getEncryptedJson()), BottleCryptoConstants.CHARSET);
            return ChatMessage.chatMessageFromJson(json);

        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

}
