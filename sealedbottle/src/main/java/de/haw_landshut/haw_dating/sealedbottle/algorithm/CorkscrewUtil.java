/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package de.haw_landshut.haw_dating.sealedbottle.algorithm;

import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.BlockFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.FieldVector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 4/8/16 by s-gheldd
 */
public class CorkscrewUtil {
    /**
     * Swaps the a-th and the b-th element in an Array.
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
     * @param matrix      the to be trimmed array
     * @param toBeRemoved row numbers, that get removed
     * @return the new array
     */
    public static BigFraction[][] removeColumns(final BigFraction[][] matrix, final Set<Integer>
            toBeRemoved) {
        final int rows = matrix.length;
        final int columns = matrix[0].length;
        final BigFraction[][] result = new BigFraction[rows][columns - toBeRemoved.size()];

        for (int row = 0; row < rows; row++) {
            int ergColumn = 0;
            for (int column = 0; column < columns; column++) {
                if (!toBeRemoved.contains(column)) {
                    result[row][ergColumn] = matrix[row][column];
                    ergColumn++;
                }
            }
        }


        return result;
    }

    /**
     * Generates one linear equation system, for calculating the missing hashes.
     *
     * @param mMatrixArray           the mMatrix part of the hint matrix of the encryption profile
     * @param bVectorArray           the bVector part of the hint matrix of the encryption profile
     * @param permutationPossibility a PermutationPossibility calculated by comparing remainder
     *                               vectors
     * @param hashedAttributes       the hashed attributes of the decrypting profile
     * @return A system of linear equations, which solution should correspond to the missing hashes
     */
    public static CorkscrewLinearEquation prepareLinearEquation(
            final BigFraction[][] mMatrixArray,
            final BigFraction[] bVectorArray,
            final Corkscrew.PermutationPossibility permutationPossibility,
            final List<byte[]> hashedAttributes) {
        final int[] permutation = permutationPossibility.getPermutationVector();
        final int[] fixPoints = permutationPossibility.getPossibleFixPoints();
        final BigFraction[] reducingVectorArray = new BigFraction[hashedAttributes.size()];

        for (final int fixPoint : fixPoints) {
            reducingVectorArray[fixPoint] = BottleUtil.makeBigFractionFromByteArray(hashedAttributes
                    .get(permutation[fixPoint]));
        }

        for (int i = 0; i < reducingVectorArray.length; i++) {
            if (reducingVectorArray[i] == null) {
                reducingVectorArray[i] = new BigFraction(0);
            }
        }

        final FieldVector<BigFraction> reducingVector = new ArrayFieldVector<>(reducingVectorArray);
        final FieldMatrix<BigFraction> mMatrix = new BlockFieldMatrix<>(mMatrixArray);
        final FieldVector<BigFraction> resultVector = new ArrayFieldVector<>(bVectorArray)
                .subtract(mMatrix.operate(reducingVector));

        final Set<Integer> fixPointSet = new HashSet<>();
        for (int fixPoint : fixPoints) {
            fixPointSet.add(fixPoint);
        }

        final BigFraction[][] resultMatrixArray = removeColumns(mMatrixArray, fixPointSet);


        return new CorkscrewLinearEquation(new BlockFieldMatrix<>(resultMatrixArray),
                resultVector,
                permutationPossibility,
                hashedAttributes);
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
}
