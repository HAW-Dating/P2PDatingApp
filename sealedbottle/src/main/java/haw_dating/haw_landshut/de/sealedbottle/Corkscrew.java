/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 3/17/16 by s-gheldd
 */
public class Corkscrew {

    public boolean probeSeal(final byte[] foreignRemainderVektor, final byte[] ownRemainderVector) {
        if (foreignRemainderVektor == null
                || ownRemainderVector == null
                || foreignRemainderVektor.length != ownRemainderVector.length) {
            return false;
        } else {

        }


        return false;
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

        private PermutationIterator(final T[] vector,final int size) {
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
            }
            else {
                int prev = this.swaps[i];
                swap(i, prev);
                int next = prev + 1;
                this.swaps[i] = next;
                swap(i, next);
            }

            return res;
        }

        final private void swap(final int i,final int j) {
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
