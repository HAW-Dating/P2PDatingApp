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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import de.haw_landshut.haw_dating.sealedbottle.algorithm.Bottle;
import de.haw_landshut.haw_dating.sealedbottle.algorithm.Corkscrew;
import de.haw_landshut.haw_dating.sealedbottle.algorithm.CorkscrewLinearEquation;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 4/30/16 by s-gheld
 */
public class BottleOpener {

    private final MessageInABottle incomingMessageInABottle;
    private final Bottle ownBottle;
    private final MessageDigest messageDigest;
    private final Cipher cipher;


    /**
     * Class used to combine a incoming MessageInABottle, your own Profile Bottle and the
     * SealedBottle
     * algorithm for deriving a common secret key.
     *
     * @param incomingMessageInABottle a incoming transmitted and deserialized MessageInABottle
     * @param ownBottle                your own Profile as an sealed Bottle
     * @throws IllegalArgumentException if ownBottle is not in state sealed
     */
    public BottleOpener(final MessageInABottle incomingMessageInABottle, final Bottle ownBottle)
            throws IllegalArgumentException {
        try {
            messageDigest = MessageDigest.getInstance(BottleCryptoConstants.HASH_ALGORITHM);
            cipher = Cipher.getInstance(BottleCryptoConstants.TRANSFORMATION);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        if (!Bottle.State.SEALED.equals(ownBottle.getState())) {
            throw new IllegalArgumentException("ownBottle must be sealed");
        }

        this.incomingMessageInABottle = incomingMessageInABottle;
        this.ownBottle = ownBottle;
    }


    /**
     * Compares the remainder vectors of the incomingMessageInABottle and ownBottle.
     *
     * @return guaranteed false if match is not possible
     */
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

    /**
     * Tries to derive the AES SecretKey derived from the original search profile.
     *
     * @return An 256Bit AES SecretKey if a match is achieved, null otherwise
     */
    public SecretKeySpec tryOpening() {
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
        if (calculatedHashedAttributes.size() == nOptionalFields) {
            messageDigest.reset();
            for (final byte[] attribute :
                    ownBottle.getHashedNecessaryAttributes()) {
                messageDigest.update(attribute);
            }
            for (final List<byte[]> attributeList : calculatedHashedAttributes) {
                for (final byte[] attribute :
                        attributeList) {
                    messageDigest.update(attribute);
                }
            }
            final SecretKeySpec secretKeySpec = new SecretKeySpec(messageDigest.digest(),
                    BottleCryptoConstants
                            .CRYPTO_ALGORITHM);
            try {
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, BottleCryptoConstants
                        .IV_PARAMETER_SPEC);
                final String decryptSafeWord = new String(cipher.doFinal(incomingMessageInABottle
                        .getEncryptedSafeWord()), BottleCryptoConstants.CHARSET);
                if (decryptSafeWord.equals(incomingMessageInABottle.getSafeWord())) {
                    return secretKeySpec;
                }
            } catch (BadPaddingException bad) {
                return null;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            return secretKeySpec;
        }
        return null;
    }

    private boolean testSolution(final List<byte[]> hashedAttributes,
                                 final String hintWord,
                                 final byte[] encryptedHintWord) {
        try {
            messageDigest.reset();
            for (final byte[] attribute :
                    hashedAttributes) {
                messageDigest.update(attribute);
            }

            final SecretKey secretKey = new SecretKeySpec(messageDigest.digest(),
                    BottleCryptoConstants.CRYPTO_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, BottleCryptoConstants.IV_PARAMETER_SPEC);
            final String message = new String(cipher.doFinal(encryptedHintWord),
                    BottleCryptoConstants.CHARSET);
            return hintWord.equals(message);
        } catch (BadPaddingException bad) {
            //wrong key
            return false;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
