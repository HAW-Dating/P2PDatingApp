/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.linear.FieldMatrix;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p>
 * 3/17/16 by s-gheldd
 */
public class Corkscrew {

    public final static int NO_MATCHING_ATTRIBUTES = 0;



    public Map<Integer, byte[]> findMissingHashes (final FieldMatrix<BigFraction> hintMatrix,
                                                   final List<byte[]> hashedAttributes,
                                                   final byte[] foreignRemainderVector,
                                                   final byte[] ownRemainderVector,
                                                   int similarityThreshold ) {
        return null;
    }

    /**
     * Tests two remainder vectors on their degree of similarity.
     *
     * @param foreignRemainderVector the to be tested ordered foreign remainder vector
     * @param ownRemainderVector the the ordered remainder vector the foreign vector is to be tested
     *                           against
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

    /*
    adapted from http://stackoverflow.com/questions/2799078/permutation-algorithm-without-recursion-java
     */
    public static class PermutationIterator<T> implements Iterator<T[]> {
        private T[] vector;

        private int[] swaps;


        PermutationIterator(final T[] vector) {
            this(vector, vector.length);
        }

        private PermutationIterator(final T[] vector, final int size) {
            this.vector = vector.clone();
            this.swaps = new int[size];
            for (int i = 0; i < swaps.length; i++)
                swaps[i] = i;
        }

        @Override
        public T[] next() {
            if (this.vector == null) {
                throw new NoSuchElementException();
            }

            T[] res = Arrays.copyOf(this.vector, this.swaps.length);

            int i = this.swaps.length - 1;
            while (i >= 0 && this.swaps[i] == this.vector.length - 1) {
                swap(i, this.swaps[i]); // Undo the swap
                this.swaps[i] = i;
                i--;
            }

            if (i < 0) {
                this.vector = null;
            } else {
                int prev = this.swaps[i];
                swap(i, prev);
                int next = prev + 1;
                this.swaps[i] = next;
                swap(i, next);
            }

            return res;
        }

        final private void swap(final int i, final int j) {
            final T temp = this.vector[i];
            this.vector[i] = this.vector[j];
            this.vector[j] = temp;
        }

        @Override
        public boolean hasNext() {
            return this.vector == null ? false : true;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
