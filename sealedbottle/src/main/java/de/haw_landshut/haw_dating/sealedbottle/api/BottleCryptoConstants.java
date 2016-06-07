package de.haw_landshut.haw_dating.sealedbottle.api;

import javax.crypto.spec.IvParameterSpec;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 5/4/16 by s-gheldd
 */
public class BottleCryptoConstants {
    public static final String HASH_ALGORITHM = "SHA-256";
    public static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    public static final String CRYPTO_ALGORITHM = "AES";
    public static final String CHARSET = "UTF-8";
    public static final IvParameterSpec IV_PARAMETER_SPEC = new IvParameterSpec(
            new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});

    private BottleCryptoConstants() {
    }
}
