/*
 * Copyright (c) 2015. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 11/20/15 by s-gheldd
 */

public class Bottle {
    private final Bottlable bottlable;
    private MessageDigest messageDigest;

    private final ArrayList<String> necessaryAttributes = new ArrayList<>();
    private final ArrayList<byte[]> hashedNecessaryAttributes = new ArrayList<>();

    private final ArrayList<String> optionalAttributes = new ArrayList<>();
    private final ArrayList<byte[]> hashedOptionalAttributes = new ArrayList<>();

    private final int[] reminderVectorNecessary;
    private final int[] reminderVectorOptional;

    private final int similarilyThreshold;
    private final int numberOfNecessaryAttributes;
    private final int numberOfOptionalAttributes;

    private int recievedNeccessaryAttributes = 0;
    private int recievedOptionalAttributes = 0;

    private boolean filled = false;
    private boolean sealed = false;
    private boolean corked = false;

    /**
     * Creates a new Bottle fom a Bottlable interface. Checks if the metadata of the Bottlable object
     * conforms to the specifications set in the interface description.
     *
     * @param bottlable A valid Bottlable object.
     * @throws IllegalArgumentException if metadata of bottlable is not specification conform.
     */
    Bottle(final Bottlable bottlable) {
        final int numberOfNecessaryAttributes, numberOfOptionalAttributes, similarityThreshold;

        numberOfNecessaryAttributes = bottlable.getNumberOfNecessaryAttributes();
        numberOfOptionalAttributes = bottlable.getNumberOfOptionalAttributes();
        similarityThreshold = bottlable.getSimilarityThreshold();

        this.bottlable = bottlable;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("something went terribly wrong");
            ex.printStackTrace();
        }
        if (similarityThreshold > numberOfOptionalAttributes) {
            throw new IllegalArgumentException("getNumberOfOptionalAttributes() returned: " + numberOfOptionalAttributes
                    + ", must be at least similarityThreshold+1: " + (similarityThreshold));
        } else {
            this.numberOfNecessaryAttributes = numberOfNecessaryAttributes;
            this.numberOfOptionalAttributes = numberOfOptionalAttributes;
            this.similarilyThreshold = similarityThreshold;
            this.reminderVectorNecessary = new int[numberOfNecessaryAttributes];
            this.reminderVectorOptional = new int[numberOfOptionalAttributes];
        }

    }

    /**
     * Calculates the single hashes. Needs to be invoked before seal().
     */
    public void fill() {
        if (!filled) {
            for (recievedNeccessaryAttributes = 0; recievedNeccessaryAttributes < numberOfNecessaryAttributes;
                 recievedNeccessaryAttributes++) {
                necessaryAttributes.add(StringNormalizer.normalize(bottlable.getNecessaryAttribute()));
            }

            for (recievedOptionalAttributes = 0; recievedOptionalAttributes < numberOfOptionalAttributes;
                 recievedOptionalAttributes++) {
                optionalAttributes.add(StringNormalizer.normalize(bottlable.getOptionalAttribute()));
            }
            filled = true;
        }
    }

    public void seal() throws IllegalStateException {
        if (filled && !sealed) {
            for (final String attribute :
                    necessaryAttributes) {
                messageDigest.update(attribute.getBytes());
                hashedNecessaryAttributes.add(messageDigest.digest());
            }

            for (final String attribute :
                    optionalAttributes) {
                messageDigest.update(attribute.getBytes());
                hashedOptionalAttributes.add(messageDigest.digest());
            }
        } else if (!filled && !sealed) {
            throw new IllegalStateException("Bottle needs to invoke fill(), before seal()");
        }
    }

    public void cork() {
        for (int i = 0; i < numberOfNecessaryAttributes; i++) {
            reminderVectorNecessary[i] = calculateReminderSeven(hashedNecessaryAttributes.get(i));
        }

        for (int i = 0; i < numberOfOptionalAttributes; i++) {
            reminderVectorOptional[i] = calculateReminderSeven(hashedOptionalAttributes.get(i));
        }


    }

    public static int calculateReminderSeven(final byte[] hash) {
        int erg = 0;
        //System.out.println(Arrays.toString(hash) + ":");
        for (int i = 1; hash.length - i >= 0; i++) {
            final int remainder;
            switch (i % 3) {
                case 1:
                    remainder = 1;
                    break;
                case 2:
                    remainder = 4;
                    break;
                case 0:
                    remainder = 2;
                    break;
                default:
                    remainder = 0;
            }
            erg = (((hash[hash.length - i] & 0xFF) % 7) * remainder + erg) % 7;
            //System.out.println(erg);
        }
        return erg;
    }

}
