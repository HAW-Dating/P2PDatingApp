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
 * <p/>
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

    public Corkscrew(BigFraction[][] hintMatrix, List<byte[]> hashedAttributes, byte[]
            foreignRemainderVector, byte[] ownRemainderVector, int similarityThreshold) {
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

    private static List<PermutationPossibility> findPermutationPossibilitiesForPermutationVector
            (final List<Integer> permutationVector,
             final byte[] foreignRemainderVector,
             final byte[] ownRemainderVector,
             final int similarityThreshold) {

        final ArrayList<PermutationPossibility> permutationPossibilities = new ArrayList<>();
        final HashSet<PermutationPossibility> semanticUniques = new HashSet<>();
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

                final PermutationPossibility permutationPossibility =
                        new PermutationPossibility(permutationArray, fixPoints);
                if (!semanticUniques.contains(permutationPossibility)) {
                    permutationPossibilities.add(permutationPossibility);
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
    public static int probeSeal(final byte[] foreignRemainderVector, final byte[]
            ownRemainderVector) {
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

    /**
     * A class representing a combination of permutations and fix points.
     */
    public static final class PermutationPossibility implements Comparable<PermutationPossibility> {
        private final int[] permutationVector;
        private final int[] possibleFixPoints;
        private final int[] orderingVector;

        public PermutationPossibility(final int[] permutationVector, final int[]
                possibleFixPoints) {
            this.permutationVector = permutationVector.clone();
            this.possibleFixPoints = possibleFixPoints.clone();
            final int fixPoints = this.possibleFixPoints.length;
            this.orderingVector = new int[fixPoints];
            for (int i = 0; i < fixPoints; i++) {
                orderingVector[i] = permutationVector[possibleFixPoints[i]];
            }
            Arrays.sort(orderingVector);
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
         * Returns true if this and that are semantic equal e.g.
         * PermutationPossibility{permutationVector=[0, 1, 2], possibleFixPoints=[0, 2]} and
         * PermutationPossibility{permutationVector=[2, 0, 1], possibleFixPoints=[0, 1]}.
         *
         * @param o a second PermutationPossibility
         * @return true if this.equals(that)
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PermutationPossibility that = (PermutationPossibility) o;

            return Arrays.equals(orderingVector, that.orderingVector);

        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(orderingVector);
        }

        @Override
        public int compareTo(PermutationPossibility another) {
            if (this == another) {
                return 0;
            }
            final int comparableLength = this.orderingVector.length <= another.orderingVector
                    .length ? this.orderingVector.length : another.orderingVector.length;

            for (int i = 0; i < comparableLength; i++) {
                final int difference = this.orderingVector[i] - another.orderingVector[i];
                if (difference != 0) {
                    return difference;
                }
            }
            if (this.orderingVector.length < another.orderingVector.length) {
                return -1;
            } else if (this.orderingVector.length == another.orderingVector.length) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public class CorkscrewIterator implements Iterator<CorkscrewLinearEquation> {
        private final BigFraction[][] mMatrixArray;
        private final BigFraction[] bVectorArray;
        private final List<byte[]> hashedAttributes;
        private final byte[] foreignRemainderVector;
        private final byte[] ownRemainderVector;
        private final int similarityThreshold;

        private final PermutationIterator<Integer> permutationIterator;
        final private Queue<PermutationPossibility> permutationQueue;
        private final Set<PermutationPossibility> semanticUniqueSet;


        private CorkscrewIterator(BigFraction[][] mMatrixArray, BigFraction[] bVectorArray,
                                  List<byte[]> hashedAttributes, byte[] foreignRemainderVector,
                                  byte[] ownRemainderVector, int similarityThreshold) {
            this.mMatrixArray = mMatrixArray;
            this.bVectorArray = bVectorArray;
            this.hashedAttributes = hashedAttributes;
            this.foreignRemainderVector = foreignRemainderVector;
            this.ownRemainderVector = ownRemainderVector;
            this.similarityThreshold = similarityThreshold;

            this.permutationIterator = new PermutationIterator<>(CorkscrewUtil
                    .createIntegerRangeList(ownRemainderVector.length));
            this.permutationQueue = new LinkedList<>();
            this.semanticUniqueSet = new HashSet<>();
        }

        private Queue<PermutationPossibility> filterSemanticDuplicates(final
                                                                       Collection<PermutationPossibility> permutationPossibilities) {
            final LinkedList<PermutationPossibility> resultList = new LinkedList<>();
            for (final PermutationPossibility permutationPossibility : permutationPossibilities) {
                if (!semanticUniqueSet.contains(permutationPossibility)) {
                    resultList.add(permutationPossibility);
                    semanticUniqueSet.add(permutationPossibility);
                }
            }
            return resultList;
        }

        @Override
        public boolean hasNext() {
            while (permutationQueue.isEmpty() && permutationIterator.hasNext()) {
                permutationQueue.addAll(filterSemanticDuplicates(
                        findPermutationPossibilitiesForPermutationVector(
                                permutationIterator.next(),
                                foreignRemainderVector,
                                ownRemainderVector,
                                similarityThreshold)));
            }
            return !permutationQueue.isEmpty();
        }

        @Override
        public CorkscrewLinearEquation next() {
            return CorkscrewUtil.prepareLinearEquation(
                    mMatrixArray,
                    bVectorArray,
                    permutationQueue.remove(),
                    hashedAttributes);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


}
