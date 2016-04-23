/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David
 * Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import org.apache.commons.collections4.iterators.PermutationIterator;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.util.Combinations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p>
 * 3/17/16 by s-gheldd
 */
public class Corkscrew implements Iterable<CorkscrewLinearEquation> {

    public final static int NO_MATCHING_ATTRIBUTES = 0;

    private final BigFraction[][] mMatrixArray;
    private final BigFraction[] bVectorArray;
    private final List<byte[]> hashedAttributes;
    private final byte[] foreignRemainderVector;
    private final byte[] ownRemainderVector;
    private final int similarityThreshold;

    public Corkscrew(BigFraction[][] hintMatrix, List<byte[]> hashedAttributes, byte[] foreignRemainderVector, byte[] ownRemainderVector, int similarityThreshold) {
        this.hashedAttributes = hashedAttributes;
        this.foreignRemainderVector = foreignRemainderVector;
        this.ownRemainderVector = ownRemainderVector;
        this.similarityThreshold = similarityThreshold;

        mMatrixArray = new BigFraction[hintMatrix.length][hintMatrix[0]
                .length - 1];
        bVectorArray = new BigFraction[hintMatrix.length];

        for (int row = 0; row < hintMatrix.length; row++) {
            final int columns = hintMatrix[0].length - 1;
            for (int column = 0; column < columns; column++) {
                mMatrixArray[row][column] = hintMatrix[row][column];
            }
            bVectorArray[row] = hintMatrix[row][hintMatrix[0].length - 1];
        }
    }

    @Override
    public Iterator<CorkscrewLinearEquation> iterator() {
        return new CorkscrewIterator(
                mMatrixArray,
                bVectorArray,
                hashedAttributes,
                foreignRemainderVector,
                ownRemainderVector,
                similarityThreshold);
    }

    private class CorkscrewIterator implements Iterator<CorkscrewLinearEquation> {
        private final BigFraction[][] mMatrixArray;
        private final BigFraction[] bVectorArray;
        private final List<byte[]> hashedAttributes;
        private final byte[] foreignRemainderVector;
        private final byte[] ownRemainderVector;
        private final int similarityThreshold;

        private final PermutationIterator<Integer> permutationIterator;
        private Queue<PermutationPossibility> semanticUniqueQueue;


        private CorkscrewIterator(BigFraction[][] mMatrixArray, BigFraction[] bVectorArray, List<byte[]> hashedAttributes, byte[] foreignRemainderVector, byte[] ownRemainderVector, int similarityThreshold) {
            this.mMatrixArray = mMatrixArray;
            this.bVectorArray = bVectorArray;
            this.hashedAttributes = hashedAttributes;
            this.foreignRemainderVector = foreignRemainderVector;
            this.ownRemainderVector = ownRemainderVector;
            this.similarityThreshold = similarityThreshold;

            this.permutationIterator = new PermutationIterator<>(CorkscrewUtil.createIntegerRangeList(ownRemainderVector.length));
            this.semanticUniqueQueue = new LinkedList<>();
        }

        @Override
        public boolean hasNext() {
            while (semanticUniqueQueue.isEmpty() && permutationIterator.hasNext()) {
                semanticUniqueQueue = PermutationPossibility.mergeAndRemoveSemanticEuals(
                        semanticUniqueQueue,
                        findPermutationPossibilitiesForPermutationVector(
                                permutationIterator.next(),
                                foreignRemainderVector,
                                ownRemainderVector,
                                similarityThreshold));
            }
            return !semanticUniqueQueue.isEmpty();
        }

        @Override
        public CorkscrewLinearEquation next() {
            return CorkscrewUtil.prepareLinearEquation(
                    mMatrixArray,
                    bVectorArray,
                    semanticUniqueQueue.remove(),
                    hashedAttributes);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static List<PermutationPossibility> findPermutationPossibilitiesForPermutationVector
            (final List<Integer> permutationVector,
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
            final Iterator<int[]> combinationIterator = new Combinations(possibleFixPoints.size()
                    , similarityThreshold).iterator();
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

                final PermutationPossibility permutationPossibility = new PermutationPossibility
                        (permutationArray, fixPoints);
                if (!permutationPossibility.semanticEqualsToAny(permutationPossibilities)) {
                    permutationPossibilities.add(new PermutationPossibility(permutationArray,
                            fixPoints));
                }
            }
        }
        return permutationPossibilities;

    }

    /**
     * Tests two remainder vectors on their degree of similarity.
     *
     * @param foreignRemainderVector the to be tested ordered foreign remainder vector
     * @param ownRemainderVector     the the ordered remainder vector the foreign vector is to be
     *                               tested
     *                               against
     * @return the degree of matching 0 <= matches <= foreignRemainderVector.length
     */
    public static int probeSeal(final byte[] foreignRemainderVector, final byte[] ownRemainderVector) {
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

    /**
     * A class representing a combination of permutations and fix points.
     */
    public static final class PermutationPossibility {
        private final int[] permutationVector;
        private final int[] possibleFixPoints;

        public PermutationPossibility(final int[] permutationVector, final int[]
                possibleFixPoints) {
            this.permutationVector = permutationVector.clone();
            this.possibleFixPoints = possibleFixPoints.clone();
        }

        /**
         * Merges two collections of PermutationPossibility, filters out any semantic duplicates
         * from the second collection.
         *
         * @param semanticUniqueCollection Collection of semantic unique PermutationPossibility
         * @param permutationPossibilities To be merged collection
         * @return new List of semantic unique PermutationPossibility
         */
        //TODO: needs better implementation, perhaps with binary search?
        public static LinkedList<PermutationPossibility> mergeAndRemoveSemanticEuals(
                final Collection<PermutationPossibility> semanticUniqueCollection,
                final Collection<PermutationPossibility> permutationPossibilities) {

            final LinkedList<PermutationPossibility> result = new LinkedList<>();
            if (semanticUniqueCollection != null) {
                result.addAll(semanticUniqueCollection);
            }


            for (final PermutationPossibility permutationPossibility : permutationPossibilities) {
                if (!permutationPossibility.semanticEqualsToAny(result)) {
                    result.add(permutationPossibility);
                }
            }
            return result;
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

        /**
         * Returns true if this is semantic equal to any in that.
         *
         * @param that a collection of PermutationPossibilities
         * @return true if semantic equal to any in that
         */
        public boolean semanticEqualsToAny(final Collection<PermutationPossibility> that) {
            for (PermutationPossibility possibility : that) {
                if (this.semanticEquals(possibility)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Returns true if this and that are semantic equal e.g.
         * PermutationPossibility{permutationVector=[0, 1, 2], possibleFixPoints=[0, 2]} and
         * PermutationPossibility{permutationVector=[2, 0, 1], possibleFixPoints=[0, 1]}.
         *
         * @param that a second PermutationPossibility
         * @return true if this.semanticEquals(that)
         */
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
