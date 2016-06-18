package de.haw_landshut.haw_dating.sealedbottle.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.SecretKey;

import de.haw_landshut.haw_dating.sealedbottle.algorithm.Bottle;
import de.haw_landshut.haw_dating.sealedbottle.algorithm.MockProfiles;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 5/4/16 by s-gheldd
 */
public class BottleOpenerTest {
    private String safeWord = "loveline";
    private String[] hintWords = new String[]{"age", "hobbies"};
    private MessageInABottle incoming;
    private Bottle own;
    private Bottle searchB;
    private BottleOpener matching;
    private BottleOpener notMatching;


    @Before
    public void setUp() {
        final Bottlable search = new MockProfiles.Search();
        final Bottlable match = new MockProfiles.Match();
        final Bottlable noMatch = new MockProfiles.NoMatch();
        searchB = new Bottle(search).fill().cork().seal();
        incoming = new MessageInABottle(searchB, safeWord,
                hintWords, 1);
        own = new Bottle(match).fill().cork().seal();
        matching = new BottleOpener(incoming, own);
        notMatching = new BottleOpener(incoming, new Bottle(noMatch).fill().cork().seal());
    }


    @Test
    public void testIsOpeningPossible() throws Exception {
        Assert.assertTrue(matching.isOpeningPossible());
        Assert.assertFalse(notMatching.isOpeningPossible());
    }

    @Test
    public void testTryOpening() throws Exception {
/*        MessageDigest digest = MessageDigest.getInstance(BottleCryptoConstants.HASH_ALGORITHM);
        Cipher cipher = Cipher.getInstance(BottleCryptoConstants.TRANSFORMATION);
        ArrayList<byte[]> attributes = searchB.getHashedOptionalAttributeField(0);
        for (byte[]a:attributes){
            digest.update(a);
        }
        SecretKey secretKey = new SecretKeySpec(digest.digest(),BottleCryptoConstants
        .CRYPTO_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, BottleCryptoConstants.IV_PARAMETER_SPEC);
        byte[] message = cipher.doFinal("age".getBytes(BottleCryptoConstants.CHARSET));
        System.out.println(Arrays.toString(message));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, BottleCryptoConstants.IV_PARAMETER_SPEC);
        String a = new String(cipher.doFinal(message), BottleCryptoConstants.CHARSET);
        System.out.println(a);*/
        SecretKey key = matching.tryOpening();
        Assert.assertNotNull(key);

        Assert.assertEquals(searchB.getKeyAsAESSecretKey(), key);

        key = notMatching.tryOpening();
        Assert.assertNull(key);
    }
}