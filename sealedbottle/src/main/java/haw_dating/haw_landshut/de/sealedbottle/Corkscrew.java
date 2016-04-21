/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import org.apache.commons.collections4.iterators.PermutationIterator;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.linear.BlockFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.util.Combinations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        final PermutationIterator<Integer> permutationIterator = new PermutationIterator<>(CorkscrewUtil.createIntegerRangeList(ownRemainderVector.length));

        while (permutationIterator.hasNext()) {
            permutationPossibilities.addAll(findPermutationPossibilitiesForPermutationVector(
                    permutationIterator.next(),
                    foreignRemainderVector,
                    ownRemainderVector,
                    similarityThreshold));
        }
        return filterSemanticDuplicatePermutations(permutationPossibilities);
    }

    private static List<PermutationPossibility> findPermutationPossibilitiesForPermutationVector(final List<Integer> permutationVector,
                                                                                                 final byte[] foreignRemainderVector,
                                                                                                 final byte[] ownRemainderVector,
                                                                                                 final int similarityThreshold) {

        final ArrayList<PermutationPossibility> permutationPossibilities = new ArrayList<>();
        final int length = ownRemainderVector.length;
        final byte[] swappedVector = new byte[length];
        for (int i = 0; i < length; i++) {
            swappedVector[i] = ownRemainderVector[permutationVector.get(i).intValue()];
        }


        final ArrayList<Integer> possibleFixPoints = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (swappedVector[i] == foreignRemainderVector[i]) {
                possibleFixPoints.add(Integer.valueOf(i));
            }
        }

        if (possibleFixPoints.size() >= similarityThreshold) {
            final Iterator<int[]> combinationIterator = new Combinations(possibleFixPoints.size(), similarityThreshold).iterator();
            int[] permutationArray = new int[permutationVector.size()];

            for (int i = 0; i < permutationArray.length; i++) {
                permutationArray[i] = permutationVector.get(i);
            }


            while (combinationIterator.hasNext()) {
                final int[] combination = combinationIterator.next();
                final int[] fixPoints = new int[combination.length];
                for (int i = 0; i < combination.length; i++) {
                    fixPoints[i] = possibleFixPoints.get(combination[i]);
                }

                final PermutationPossibility permutationPossibility = new PermutationPossibility(permutationArray, fixPoints);
                if (!permutationPossibility.semanticEqualsToAny(permutationPossibilities)) {
                    permutationPossibilities.add(new PermutationPossibility(permutationArray, fixPoints));
                }
            }
        }
        return permutationPossibilities;

    }

    private static List<PermutationPossibility> filterSemanticDuplicatePermutations(ArrayList<PermutationPossibility> permutationPossibilities) {
        final LinkedList<PermutationPossibility> semanticUniques = new LinkedList<>();

        for (PermutationPossibility permutationPossibility : permutationPossibilities) {
            if (!permutationPossibility.semanticEqualsToAny(semanticUniques)) {
                semanticUniques.add(permutationPossibility);
            }
        }
        return semanticUniques;
    }

    public Map<Integer, byte[]> findMissingHashes(final BigFraction[][] hintMatrix,
                                                  final List<byte[]> hashedAttributes,
                                                  final byte[] foreignRemainderVector,
                                                  final byte[] ownRemainderVector,
                                                  final int similarityThreshold) {
        final BigFraction[][] mMatrixArray = new BigFraction[hintMatrix.length][hintMatrix[0].length - 1];
        final BigFraction[] bVectorArray = new BigFraction[hintMatrix.length];

        for (int row = 0; row < hintMatrix.length; row++) {
            final int columns = hintMatrix[0].length - 1;
            for (int column = 0; column < columns; column++) {
                mMatrixArray[row][column] = hintMatrix[row][column];
            }
            bVectorArray[row] = hintMatrix[row][hintMatrix[0].length - 1];
        }
        final List<PermutationPossibility> permutationPossibilities = findPermutationPossibilities(foreignRemainderVector, ownRemainderVector, similarityThreshold);
        System.out.println(Arrays.deepToString(hintMatrix));
        System.out.println("" + mMatrixArray.length + " " + mMatrixArray[0].length);
        FieldMatrix<BigFraction> mMatrix = new BlockFieldMatrix<>(mMatrixArray);


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
        final int[] permutationVector;
        final int[] possibleFixPoints;

        public PermutationPossibility(final int[] permutationVector, final int[] possibleFixPoints) {
            this.permutationVector = permutationVector.clone();
            this.possibleFixPoints = possibleFixPoints.clone();
        }

        public int[] getPermutationVector() {
            return permutationVector.clone();
        }

        public int[] getPossibleFixPoints() {
            return possibleFixPoints.clone();
        }

        @Override
        public String toString() {
            return "PermutationPossibility{" +
                    "permutationVector=" + Arrays.toString(permutationVector) +
                    ", possibleFixPoints=" + Arrays.toString(possibleFixPoints) +
                    '}';
        }

        public boolean semanticEqualsToAny(final Collection<PermutationPossibility> that) {
            for (PermutationPossibility possibility : that) {
                if (this.semanticEquals(possibility)) {
                    return true;
                }
            }
            return false;
        }

        public boolean semanticEquals(final PermutationPossibility that) {
            final int length = this.possibleFixPoints.length;
            if (this.permutationVector.length != that.permutationVector.length
                    || length != that.possibleFixPoints.length) {
                return false;
            } else {
                final Set<Integer> fixPermutation = new HashSet<>();
                for (int i = 0; i < length; i++) {
                    fixPermutation.add(this.permutationVector[this.possibleFixPoints[i]]);
                }
                for (int i = 0; i < length; i++) {
                    if (!fixPermutation.remove(that.permutationVector[that.possibleFixPoints[i]])) {
                        return false;
                    }
                }
                return true;
            }
        }
    }


}
