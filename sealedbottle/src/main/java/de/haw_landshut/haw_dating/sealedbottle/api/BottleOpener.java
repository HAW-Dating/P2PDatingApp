/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David
 * Manhart, Georg Held
 *
 *
 */

package de.haw_landshut.haw_dating.sealedbottle.api;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import de.haw_landshut.haw_dating.sealedbottle.algorithm.Bottle;
import de.haw_landshut.haw_dating.sealedbottle.algorithm.Corkscrew;

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
            messageDigest = MessageDigest.getInstance(BottleCryptoAlgorithms.HASH_ALGORITHM);
            cipher = Cipher.getInstance(BottleCryptoAlgorithms.TRANSFORMATION);
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
        for (int i = 0; i < nOptionalFields; i++) {





        }


        return null;
    }
}
