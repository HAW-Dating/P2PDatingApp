/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 3/17/16 by s-gheldd
 */
public class Corkscrew {

    public final static int NO_MATCHING_ATTRIBUTES = 0;

    private static List<PermutationPossibility> findPermutationPossibilities(
            final byte[] foreignRemainderVector,
            final byte[] ownRemainderVector,
            final int similarityThreshold) {
        if (foreignRemainderVector == null
                || ownRemainderVector == null
                || foreignRemainderVector.length != ownRemainderVector.length) {
            return null;
        }
        final Byte[] bigByteRemainderVector = new Byte[ownRemainderVector.length];
        for (int i = 0; i < ownRemainderVector.length; i++) {
            bigByteRemainderVector[i] = Byte.valueOf(ownRemainderVector[i]);
        }
        final ArrayList<PermutationPossibility> permutationPossibilities = new ArrayList<>();
        final CorkscrewUtil.CorkscrewPermutationIterator<Integer> corkscrewPermutationIterator
                = new CorkscrewUtil.CorkscrewPermutationIterator<>(CorkscrewUtil.createIntegerRange(ownRemainderVector.length));
        while (corkscrewPermutationIterator.hasNext()) {
            permutationPossibilities.addAll(findPermutationPossibilitiesForPermutationVector(
                    corkscrewPermutationIterator.next(),
                    foreignRemainderVector,
                    ownRemainderVector,
                    similarityThreshold));
        }
        return permutationPossibilities;
    }

    private static List<PermutationPossibility> findPermutationPossibilitiesForPermutationVector(final Integer[] permutationVector,
                                                                                                 final byte[] foreignRemainderVector,
                                                                                                 final byte[] ownRemainderVector,
                                                                                                 final int similarityThreshold) {

        final ArrayList<PermutationPossibility> permutationPossibilities = new ArrayList<>();
        final int length = ownRemainderVector.length;
        final byte[] swappedVector = new byte[length];
        for (int i = 0; i < length; i++) {
            swappedVector[i] = ownRemainderVector[permutationVector[i].intValue()];
        }


        final ArrayList<Integer> possibleFixPoints = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (swappedVector[i] == foreignRemainderVector[i]) {
                possibleFixPoints.add(Integer.valueOf(i));
            }
        }

        while (possibleFixPoints.size() >= similarityThreshold) {

        }
        return permutationPossibilities;

    }

    public Map<Integer, byte[]> findMissingHashes(final BigFraction[][] hintMatrix,
                                                  final List<byte[]> hashedAttributes,
                                                  final byte[] foreignRemainderVector,
                                                  final byte[] ownRemainderVector,
                                                  final int similarityThreshold) {
        final BigFraction[][] mMatrix = new BigFraction[hintMatrix.length][hintMatrix[0].length - 1];
        final BigFraction[] bVector = new BigFraction[hintMatrix.length];

        for (int row = 0; row < hintMatrix.length; row++) {
            final int columns = hintMatrix[0].length - 1;
            for (int column = 0; column < columns; column++) {
                mMatrix[row][column] = hintMatrix[row][column];
            }
            bVector[row] = hintMatrix[row][hintMatrix[0].length - 1];
        }
        System.out.println("hint" + Arrays.deepToString(hintMatrix));
        System.out.println("m" + Arrays.deepToString(mMatrix));
        System.out.println("b" + Arrays.deepToString(bVector));
        findPermutationPossibilities(foreignRemainderVector, ownRemainderVector, similarityThreshold);

        return null;
    }

    /**
     * Tests two remainder vectors on their degree of similarity.
     *
     * @param foreignRemainderVector the to be tested ordered foreign remainder vector
     * @param ownRemainderVector     the the ordered remainder vector the foreign vector is to be tested
     *                               against
     * @return the degree of matching 0 <= matches <= foreignRemainderVector.length
     */
    public int probeSeal(final byte[] foreignRemainderVector, final byte[] ownRemainderVector) {
        int matches = 0;
        if (foreignRemainderVector == null
                || ownRemainderVector == null
                || foreignRemainderVector.length != ownRemainderVector.length) {
            matches = NO_MATCHING_ATTRIBUTES;
        } else {
            final int length = foreignRemainderVector.length;
            for (int i = 0; i < length; i++) {
                if ((foreignRemainderVector[i] - ownRemainderVector[i]) == 0) {
                    matches++;
                }
            }
        }
        return matches;
    }

    public static final class PermutationPossibility {
        final int[] permutations;
        final int[] possibleFixPoints;

        public PermutationPossibility(final int[] permutations, final int[] possibleFixPoints) {
            this.permutations = permutations.clone();
            this.possibleFixPoints = possibleFixPoints.clone();
        }

        public int[] getPermutations() {
            return permutations.clone();
        }

        public int[] getPossibleFixPoints() {
            return possibleFixPoints.clone();
        }
    }

}
