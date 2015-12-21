/*
 * Copyright (c) 2015. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p>
 * 11/20/15 by s-gheldd
 */

public class Bottle {
    private final Bottlable bottlable;
    private MessageDigest messageDigest;

    private final ArrayList<String> necessaryAttributes = new ArrayList<>();
    private final ArrayList<byte[]> hashedNecessaryAttributes = new ArrayList<>();

    private final ArrayList<ArrayList<String>> optionalAttributes = new ArrayList<>();
    private final ArrayList<ArrayList<byte[]>> hashedOptionalAttributes = new ArrayList<>();

    private final byte[] reminderVectorNecessary;
    private final byte[][] reminderVectorOptional;

    private final int[] similarilyThreshold;
    private final int numberOfNecessaryAttributes;
    private final int numberOfOptionalAttributeFields;
    private final int[] numberOfOptionalAttributes;

    private int recievedOptionalAttributeFields = 0;
    private int recievedNeccessaryAttributes = 0;
    private final int recievedOptionalAttributes[];

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
        this.bottlable = bottlable;
        this.numberOfOptionalAttributeFields = bottlable.getNumberOfOptionalAttributeFields();
        this.numberOfNecessaryAttributes = bottlable.getNumberOfNecessaryAttributes();
        this.similarilyThreshold = new int[bottlable.getNumberOfOptionalAttributeFields()];
        this.numberOfOptionalAttributes = new int[bottlable.getNumberOfOptionalAttributeFields()];
        this.reminderVectorNecessary = new byte[bottlable.getNumberOfNecessaryAttributes()];
        this.reminderVectorOptional = new byte[bottlable.getNumberOfOptionalAttributeFields()][];
        this.recievedOptionalAttributes = new int[bottlable.getNumberOfOptionalAttributeFields()];

        for (int i = 0; i < this.numberOfOptionalAttributeFields; i++) {
            this.numberOfOptionalAttributes[i] = bottlable.getNumberOfOptionalAttributes(i);
            this.similarilyThreshold[i] = bottlable.getSimilarityThreshold(i);
            if (this.similarilyThreshold[i] > this.numberOfOptionalAttributes[i]) {
                throw new IllegalArgumentException("Each number of optional attributes must be greater or" +
                        "equal to the corresponding similarityt hreshold, discrepancy at fieldnumber: " + i);
            }
        }
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");

        } catch (Exception ex ){
            ex.printStackTrace();
            System.out.println("Something went very wrong");
        }
    }

    /**
     * Calculates the single hashes. Needs to be invoked before seal().
     */
    public void fill() {
        if (!filled) {
            for (recievedNeccessaryAttributes = 0; recievedNeccessaryAttributes < numberOfNecessaryAttributes;
                 recievedNeccessaryAttributes++) {
                necessaryAttributes.add(BottleUtil.normalize(bottlable.getNecessaryAttribute()));
            }

            for (recievedOptionalAttributeFields = 0; recievedOptionalAttributeFields < numberOfOptionalAttributeFields;
                 recievedOptionalAttributeFields++) {
                optionalAttributes.add(new ArrayList<String>());
                for (recievedOptionalAttributes[recievedOptionalAttributeFields] = 0;
                     recievedOptionalAttributes[recievedOptionalAttributeFields] < numberOfOptionalAttributes[recievedOptionalAttributeFields];
                        recievedOptionalAttributes[recievedOptionalAttributeFields]++){
                    optionalAttributes.get(recievedOptionalAttributeFields).add(bottlable.getOptionalAttribute(recievedOptionalAttributeFields));
                }

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

            for (int i = 0; i < numberOfOptionalAttributeFields; i++){
                hashedOptionalAttributes.add(new ArrayList<byte[]>());
                for(final String attribute : optionalAttributes.get(i) ){
                    messageDigest.update(attribute.getBytes());
                    hashedOptionalAttributes.get(i).add(messageDigest.digest());
                }
            }
            sealed = true;
        } else if (!filled && !sealed) {
            throw new IllegalStateException("Bottle needs to invoke fill(), before seal()");
        }
    }

    public void cork() {
        for (int i = 0; i < numberOfNecessaryAttributes; i++) {
            reminderVectorNecessary[i] = BottleUtil.calculateReminderSeven(hashedNecessaryAttributes.get(i));
        }

        for (int i = 0; i < numberOfOptionalAttributeFields; i++) {
            reminderVectorOptional[i]= new byte[numberOfOptionalAttributes[i]];
            for(int j=0;j<numberOfOptionalAttributes[i];j++){
                reminderVectorOptional[i][j] = BottleUtil.calculateReminderSeven(hashedOptionalAttributes.get(i).get(j));
            }
        }
    }

}
