/*
 * Copyright (c) 2015. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import java.security.MessageDigest;
import java.util.ArrayList;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p>
 * 11/20/15 by s-gheldd
 */

public class Bottle {

    private final Bottlable bottlable;
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
    private final int recievedOptionalAttributes[];
    private MessageDigest messageDigest;
    private int recievedOptionalAttributeFields = 0;
    private int recievedNeccessaryAttributes = 0;
    private Bottle.State state;
    private byte[] hashOfBottle;

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

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Something went very wrong");
        }
        this.state = State.OPEN;
    }

    /**
     * Fills the Bottle. All attributes get normalized in the process. Needs to be invoked before Bottle.cork().
     */
    public void fill() {
        if (State.OPEN.equals(this.state)) {
            for (recievedNeccessaryAttributes = 0; recievedNeccessaryAttributes < numberOfNecessaryAttributes;
                 recievedNeccessaryAttributes++) {
                necessaryAttributes.add(BottleUtil.normalize(bottlable.getNecessaryAttribute()));
            }

            for (recievedOptionalAttributeFields = 0; recievedOptionalAttributeFields < numberOfOptionalAttributeFields;
                 recievedOptionalAttributeFields++) {
                optionalAttributes.add(new ArrayList<String>());
                for (recievedOptionalAttributes[recievedOptionalAttributeFields] = 0;
                     recievedOptionalAttributes[recievedOptionalAttributeFields] < numberOfOptionalAttributes[recievedOptionalAttributeFields];
                     recievedOptionalAttributes[recievedOptionalAttributeFields]++) {
                    optionalAttributes.get(recievedOptionalAttributeFields).add(bottlable.getOptionalAttribute(recievedOptionalAttributeFields));
                }

            }
            this.state = State.FILLED;
        }
    }

    /**
     * Hashes the attributes. Bottle.fill() needs to be invoked first.
     *
     * @throws IllegalStateException if Bottle.fill() was not invoked prior.
     */
    public void cork() throws IllegalStateException {
        if (State.FILLED.equals(this.state)) {
            for (final String attribute :
                    necessaryAttributes) {
                messageDigest.update(attribute.getBytes());
                hashedNecessaryAttributes.add(messageDigest.digest());
            }

            for (int i = 0; i < numberOfOptionalAttributeFields; i++) {
                hashedOptionalAttributes.add(new ArrayList<byte[]>());
                for (final String attribute : optionalAttributes.get(i)) {
                    messageDigest.update(attribute.getBytes());
                    hashedOptionalAttributes.get(i).add(messageDigest.digest());
                }
            }
            this.state = State.CORKED;
        } else if (State.OPEN.equals(this.state)) {
            throw new IllegalStateException("Bottle needs to invoke fill(), before cork()");
        }
    }

    /**
     * Generates the Key. Bottle.cork() needs to be invoked first.
     *
     * @throws IllegalStateException if Bottle.fill() was not invoked prior.
     */
    public void seal() throws IllegalStateException {
        if (State.CORKED.equals(this.state)) {
            for (int i = 0; i < numberOfNecessaryAttributes; i++) {
                final byte[] attribute = hashedNecessaryAttributes.get(i);
                reminderVectorNecessary[i] = BottleUtil.calculateReminderSeven(attribute);
                messageDigest.update(attribute);
            }

            for (int i = 0; i < numberOfOptionalAttributeFields; i++) {
                reminderVectorOptional[i] = new byte[numberOfOptionalAttributes[i]];
                for (int j = 0; j < numberOfOptionalAttributes[i]; j++) {
                    final byte[] attribute = hashedOptionalAttributes.get(i).get(j);
                    reminderVectorOptional[i][j] = BottleUtil.calculateReminderSeven(attribute);
                    messageDigest.update(attribute);
                }
            }
            this.hashOfBottle = messageDigest.digest();
            this.state = State.SEALED;
        } else if (State.OPEN.equals(this.state) || State.FILLED.equals(this.state)) {
            throw new IllegalStateException("Bottle needs to invoke cork(), before cork()");
        }
    }

    /**
     * Returns the generated key as an array of byte.
     *
     * @return a byte[] of length 256 representing the key.
     * @throws IllegalStateException if Bottle is not in State.SEALED at time of invocation.
     */
    public byte[] getKeyAsByteArray() throws IllegalStateException{
        if (State.SEALED.equals(this.state)) {
            final byte[] key = new byte[hashOfBottle.length];
            System.arraycopy(hashOfBottle, 0, key, 0, hashOfBottle.length);
            return key;
        } else {
            throw new IllegalStateException("Bottle needs to be in State.SEALED");
        }
    }

    /**
     * Returns the generated key as an SecretKey of type "AES".
     *
     * @return a SecretKey of type "AES".
     * @throws IllegalStateException if Bottle is not in State.SEALED at time of invocation.
     */
    public SecretKey getKeyasAESSecretKey() throws IllegalStateException {
        if (State.SEALED.equals(this.state)) {
            return new SecretKeySpec(hashOfBottle, "AES");
        } else {
            throw new IllegalStateException("Bottle needs to be in State.SEALED");
        }
    }

    /**
     * Returns the current Bottle.State.
     *
     * @return the current Bottle.State
     */
    public Bottle.State getState() {
        return this.state;
    }

    /**
     * Represents the different states a Bottle Object can assume.
     */
    public enum State {
        SEALED,
        CORKED,
        FILLED,
        OPEN
    }
}
