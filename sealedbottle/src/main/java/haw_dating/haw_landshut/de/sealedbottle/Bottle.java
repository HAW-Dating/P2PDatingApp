/*
 * Copyright (c) 2015. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.linear.BlockFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrix;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 11/20/15 by s-gheldd
 */

public class Bottle {

    private final Bottlable bottlable;
    private final ArrayList<String> necessaryAttributes = new ArrayList<>();
    private final ArrayList<byte[]> hashedNecessaryAttributes = new ArrayList<>();
    private final ArrayList<ArrayList<String>> optionalAttributeFields = new ArrayList<>();
    private final ArrayList<ArrayList<byte[]>> hashedOptionalAttributeFields = new ArrayList<>();
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
    private final ArrayList<BigFraction[][]> constraintMatrixArrays;
    private final ArrayList<FieldMatrix<BigFraction>> constraintMatixes;
    private final ArrayList<FieldMatrix<BigFraction>> bMatrixes;

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
        this.constraintMatrixArrays = new ArrayList<>();
        this.constraintMatixes = new ArrayList<>();
        this.bMatrixes = new ArrayList<>();

        for (int i = 0; i < this.numberOfOptionalAttributeFields; i++) {
            final int numberOfOptionalAttributes = bottlable.getNumberOfOptionalAttributes(i);
            final int similarityThreshold = bottlable.getSimilarityThreshold(i);
            this.numberOfOptionalAttributes[i] = numberOfOptionalAttributes;
            this.similarilyThreshold[i] = similarityThreshold;
            if (this.similarilyThreshold[i] > this.numberOfOptionalAttributes[i]) {
                throw new IllegalArgumentException("Each number of optional attributes must be greater or" +
                        "equal to the corresponding similarity threshold, discrepancy at field number: " + i);
            }
            constraintMatrixArrays.add(new BigFraction[numberOfOptionalAttributes - similarityThreshold][numberOfOptionalAttributes]);
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
            final Random random = new Random(System.currentTimeMillis());
            for (recievedNeccessaryAttributes = 0; recievedNeccessaryAttributes < numberOfNecessaryAttributes;
                 recievedNeccessaryAttributes++) {
                necessaryAttributes.add(BottleUtil.normalize(bottlable.getNecessaryAttribute()));
            }

            for (recievedOptionalAttributeFields = 0; recievedOptionalAttributeFields < numberOfOptionalAttributeFields;
                 recievedOptionalAttributeFields++) {
                optionalAttributeFields.add(new ArrayList<String>());
                for (recievedOptionalAttributes[recievedOptionalAttributeFields] = 0;
                     recievedOptionalAttributes[recievedOptionalAttributeFields] < numberOfOptionalAttributes[recievedOptionalAttributeFields];
                     recievedOptionalAttributes[recievedOptionalAttributeFields]++) {
                    optionalAttributeFields.get(recievedOptionalAttributeFields).add(bottlable.getOptionalAttribute(recievedOptionalAttributeFields));
                }
                final BigFraction[][] constraintMatrixArray = constraintMatrixArrays.get(recievedOptionalAttributeFields);
                final int optionals = this.numberOfOptionalAttributes[recievedOptionalAttributeFields]
                        - this.similarilyThreshold[recievedOptionalAttributeFields];
                if (optionals > 0) {
                    for (int row = 0; row < optionals; row++) {
                        for (int column = 0; column < optionals; column++) {
                            constraintMatrixArray[row][column] = (row == column) ? new BigFraction(1) : new BigFraction(0);
                        }
                    }

                    for (int row = 0; row < optionals; row++) {
                        for (int column = optionals; column < this.numberOfOptionalAttributes[recievedOptionalAttributeFields]; column++) {
                            int ran = random.nextInt();
                            while (ran == 0) {
                                ran = random.nextInt();
                            }
                            constraintMatrixArray[row][column] = new BigFraction(ran);
                        }
                    }
                    constraintMatixes.add(new BlockFieldMatrix<>(constraintMatrixArray));
                    constraintMatrixArrays.set(recievedOptionalAttributeFields, null);
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
                final ArrayList<String> optionalAttributes = optionalAttributeFields.get(i);
                final int fieldSize = optionalAttributes.size();
                final BigFraction[][] fractionArray = new BigFraction[fieldSize][1];
                hashedOptionalAttributeFields.add(new ArrayList<byte[]>());
                for (int j = 0; j < fieldSize; j++) {
                    final String attribute = optionalAttributes.get(j);
                    messageDigest.update(attribute.getBytes());
                    final byte[] hash = messageDigest.digest();
                    hashedOptionalAttributeFields.get(i).add(hash);
                    fractionArray[j][0] = new BigFraction(new BigInteger(hash));
                }
                if (this.numberOfOptionalAttributes[i]
                        - this.similarilyThreshold[i] > 0) {
                    final BlockFieldMatrix<BigFraction> attributeVector = new BlockFieldMatrix<BigFraction>(fractionArray);
                    bMatrixes.add(constraintMatixes.get(i).multiply(attributeVector));
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
                    final byte[] attribute = hashedOptionalAttributeFields.get(i).get(j);
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
    public byte[] getKeyAsByteArray() throws IllegalStateException {
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
     * Creates and returns the Hintmatrix to the corresponding optional attribute set.
     *
     * @param numberOfOptionalAttributeField int containing the number of the optional attribute set
     * @return a Hintmatrix of the form M = {C|b} as a 2D BigFraction array, null if for the given field no
     * Hintmatrix can be created (100% attribute matching)
     * @throws IllegalStateException if Bottle is not in State.SEALED at time of invocation.
     */
    public BigFraction[][] getHintMatrix(final int numberOfOptionalAttributeField) throws IllegalStateException {

        if (!State.SEALED.equals(this.state)) {
            throw new IllegalStateException("Bottle needs to be in State.SEALED");
        } else if (numberOfOptionalAttributeField > this.numberOfOptionalAttributeFields - 1) {
            throw new IllegalArgumentException("no corresponding optional argument field exists");
        } else if (constraintMatixes.size() == 0) {
            return null;
        } else {
            try {
                final FieldMatrix<BigFraction> constraintMatrix = this.constraintMatixes.get(numberOfOptionalAttributeField);
                final FieldMatrix<BigFraction> bMatrix = this.bMatrixes.get(numberOfOptionalAttributeField);
                final int rows = constraintMatrix.getRowDimension();
                final int columns = constraintMatrix.getColumnDimension();
                final BigFraction[][] hintMatrix = new BigFraction[rows][columns + 1];
                for (int row = 0; row < rows; row++) {
                    int column;
                    final BigFraction[] rowArray = constraintMatrix.getRow(row);
                    for (column = 0; column < columns; column++) {
                        hintMatrix[row][column] = rowArray[column];
                    }
                    hintMatrix[row][column] = bMatrix.getRow(row)[0];
                }
                return hintMatrix;
            } catch (IndexOutOfBoundsException ex) {
                return null;
            }
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
