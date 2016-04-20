/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.linear.FieldMatrix;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 4/8/16 by s-gheldd
 */
public class CorkscrewUtil {
    /**
     * Swaps the ath and the bth element in an Array.
     *
     * @param vector the array
     * @param a      number of the first element
     * @param b      number of the second element
     * @param <T>    the type of the array
     */
    public static <T> void swap(final T[] vector, final int a, final int b) {
        final T tmp = vector[a];
        vector[a] = vector[b];
        vector[b] = tmp;
    }

    /**
     * Creates an integer range 0,1,2,...,n-1.
     *
     * @param n the length of the range
     * @return the range
     */
    public static Integer[] createIntegerRange(final int n) {
        final Integer[] range = new Integer[n];
        for (int i = 0; i < n; i++) {
            range[i] = Integer.valueOf(i);
        }
        return range;
    }

    /**
     * Removes one row from a two dimensional array.
     *
     * @param matrix the to be trimmed array
     * @param i      the row number, that gets removed
     * @return the new array
     */
    public static BigFraction[][] removeColumn(final BigFraction[][] matrix, final int i) {
        final int rows = matrix.length;
        final int columns = matrix[0].length;
        final BigFraction[][] erg = new BigFraction[rows][columns - 1];

        for (int row = 0; row < rows; row++) {
            int ergColumn = 0;
            for (int column = 0; column < columns; column++) {
                if (column != i) {
                    erg[row][ergColumn] = matrix[row][column];
                    ergColumn++;
                }
            }
        }
        return erg;
    }

    /**
     * Creates an integer range 0,1,2,...,n-1.
     *
     * @param n the length of the range
     * @return the range
     */
    public static List<Integer> createIntegerRangeList(final int n) {
        return Arrays.asList(createIntegerRange(n));
    }

    /*
        adapted from http://stackoverflow.com/questions/2799078/permutation-algorithm-without-recursion-java
         */
    public static class CorkscrewPermutationIterator<T> implements Iterator<T[]> {
        private T[] vector;

        private int[] swaps;


        CorkscrewPermutationIterator(final T[] vector) {
            this(vector, vector.length);
        }

        private CorkscrewPermutationIterator(final T[] vector, final int size) {
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
