/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David
 * Manhart, Georg Held
 *
 *
 */

package de.haw_landshut.haw_dating.sealedbottle.api;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import de.haw_landshut.haw_dating.sealedbottle.algorithm.Bottle;
import de.haw_landshut.haw_dating.sealedbottle.algorithm.Corkscrew;
import de.haw_landshut.haw_dating.sealedbottle.algorithm.CorkscrewLinearEquation;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p>
 * 4/30/16 by s-gheld
 */
public class BottleOpener {

    private final MessageInABottle incomingMessageInABottle;
    private final Bottle ownBottle;
    private final MessageDigest messageDigest;
    private final Cipher cipher;

    public BottleOpener(final MessageInABottle incomingMessageInABottle, final Bottle ownBottle) {
        try {
            messageDigest = MessageDigest.getInstance(BottleCryptoConstants.HASH_ALGORITHM);
            cipher = Cipher.getInstance(BottleCryptoConstants.TRANSFORMATION);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        this.incomingMessageInABottle = incomingMessageInABottle;
        this.ownBottle = ownBottle;
    }

    public boolean isOpeningPossible() {
        final int nNecessary = incomingMessageInABottle.getRemainderVectorNecessary().length;
        if (Corkscrew.probeSeal(incomingMessageInABottle.getRemainderVectorNecessary(), ownBottle
                .getReminderVectorNecessary()) != nNecessary) {
            return false;
        } else {
            final int nOptionalFields = incomingMessageInABottle.getNOptionalFields();
            for (int i = 0; i < nOptionalFields; i++) {
                final int threshold = incomingMessageInABottle.getSimilarityThreshold(i);
                final int matchingRate = Corkscrew.probeSeal(incomingMessageInABottle
                        .getRemainderVectorOptional(i), ownBottle.getReminderVectorOptional(i));
                if (matchingRate < threshold) {
                    return false;
                }
            }
        }
        return true;
    }

    public SecretKey tryOpening() {

        final int nOptionalFields = incomingMessageInABottle.getNOptionalFields();
        final ArrayList<List<byte[]>> calculatedHashedAttributes = new ArrayList<>(nOptionalFields);
        for (int i = 0; i < nOptionalFields; i++) {

            final Iterator<CorkscrewLinearEquation> equationIterator = new Corkscrew(
                    incomingMessageInABottle.getHintMatrix(i),
                    ownBottle.getHashedOptionalAttributeField(i),
                    incomingMessageInABottle.getRemainderVectorOptional(i),
                    ownBottle.getReminderVectorOptional(i),
                    incomingMessageInABottle.getSimilarityThreshold(i)
            ).iterator();

            while (equationIterator.hasNext()) {
                final List<byte[]> hashedAttributes = equationIterator.next().solve();
                if (testSolution(hashedAttributes, incomingMessageInABottle.getHintWord(i),
                        incomingMessageInABottle.getEncryptedHintWord(i))) {
                    calculatedHashedAttributes.add(hashedAttributes);
                    break;
                }
            }
        }
        if(calculatedHashedAttributes.size() == nOptionalFields) {
            messageDigest.reset();
            for (final byte[] attribute :
                    ownBottle.getHashedNecessaryAttributes()) {
                messageDigest.digest(attribute);
            }
            for ()
        }


        return null;
    }

    boolean testSolution(final List<byte[]> hashedAttributes, final String hintWord, final byte[]
            encryptedHintWord) {
        try {
            messageDigest.reset();
            for (final byte[] attribute :
                    hashedAttributes) {
                messageDigest.update(attribute);
            }

            final SecretKey secretKey = new SecretKeySpec(messageDigest.digest(),
                    BottleCryptoConstants.CRYPTO_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            cipher.update(encryptedHintWord);
            final String message = new String(cipher.doFinal(), BottleCryptoConstants.CHARSET);
            return hintWord.equals(message);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
