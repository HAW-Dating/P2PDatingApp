/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David
 * Manhart, Georg Held
 *
 *
 */

package de.haw_landshut.haw_dating.sealedbottle.api;

import com.google.gson.Gson;

import org.apache.commons.math3.fraction.BigFraction;

import de.haw_landshut.haw_dating.sealedbottle.algorithm.Bottle;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 4/26/16 by s-gheldd
 */

/**
 * MessageInABottle is the serializable class (Gson), that can be send over the network.
 */
public class MessageInABottle {
    private final static Gson gson = new Gson();

    private static final String NOT_ENOUGH_HINT_WORDS = "for all optional arguments a hint word " +
            "must be supplied";
    private static final String NO_NULL_PARAMETERS = "all parameters must not be null";
private static final String BOTTLE_NOT_SEALED = "bottle is not in state sealed";

    private final int versionNumber;
    private final String safeWord;
    private final String[] hintWords;

    private final byte[] remainderVectorNecessary;
    private final byte[][] remainderVectorOptional;
    private final BigFraction[][][] hintMatrixes;

    public MessageInABottle(final Bottle bottle, final String safeWord, final String[] hintWords,
                            final int versionNumber) throws IllegalArgumentException {

        if (bottle == null || safeWord == null || hintWords == null ) {
            throw new IllegalArgumentException(NO_NULL_PARAMETERS);
        }

        if ( !Bottle.State.SEALED.equals(bottle.getState())){
            throw new IllegalArgumentException(BOTTLE_NOT_SEALED);
        }

        final int optionalFields = bottle.getNumberOfOptionalAttributeFields();
        if (hintWords.length < optionalFields) {
            throw new IllegalArgumentException(NOT_ENOUGH_HINT_WORDS);
        }
        for (String hintWord :
                hintWords) {
            if (hintWord == null) {
                throw new IllegalArgumentException(NO_NULL_PARAMETERS);
            }
        }

        this.hintWords = hintWords.clone();
        this.versionNumber = versionNumber;
        this.safeWord = safeWord;
        this.remainderVectorNecessary = bottle.getReminderVectorNecessary().clone();
        this.remainderVectorOptional = new byte[optionalFields][];
        for (int i = 0; i < optionalFields; i++) {
            this.remainderVectorOptional[i] = bottle.getReminderVectorOptional(i).clone();
        }
        this.hintMatrixes = new BigFraction[optionalFields][][];
        for (int i = 0; i < optionalFields; i++) {
            final BigFraction[][] hintMatrix = bottle.getHintMatrix(i);
            final int rows = hintMatrix.length;
            hintMatrixes[i] = new BigFraction[rows][];
            for (int row = 0; row < rows; row++) {
                hintMatrixes[i][row] = hintMatrix[row].clone();
            }
        }
    }

    public static String serialize(final MessageInABottle messageInABottle) {
        return gson.toJson(messageInABottle);
    }

    public static MessageInABottle deSerialize(final String jsonMessage) {
        return gson.fromJson(jsonMessage, MessageInABottle.class);
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public String getSafeWord() {
        return safeWord;
    }

    public String getHintWord(final int i) {
        if (i >= this.hintWords.length) {
            return null;
        } else {
            return this.hintWords[i];
        }
    }

    public byte[] getRemainderVectorNecessary() {
        return remainderVectorNecessary.clone();
    }

    public byte[] getRemainderVectorOptional(final int i) {
        if (i >= this.remainderVectorOptional.length) {
            return null;
        } else {
            return this.remainderVectorOptional[i].clone();
        }
    }

    public BigFraction[][] getHintMatrix(final int i) {
        if (i >= hintMatrixes.length) {
            return null;
        } else {
            final int rows = this.hintMatrixes[i].length;
            final BigFraction[][] result = new BigFraction[rows][];
            for (int row = 0; row < rows; row++) {
                result[row] = this.hintMatrixes[i][row].clone();
            }
            return result;
        }
    }


}
