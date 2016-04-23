/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David
 * Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.linear.FieldLUDecomposition;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.FieldVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 4/22/16 by s-gheldd
 */
public class CorkscrewLinearEquation {
    final FieldMatrix<BigFraction> matrix;
    final FieldVector<BigFraction> bVector;
    final Corkscrew.PermutationPossibility permutationPossibility;
    final List<byte[]> hashedAttributes;


    CorkscrewLinearEquation(final FieldMatrix<BigFraction> matrix, final FieldVector<BigFraction> vector,
                            final Corkscrew.PermutationPossibility permutationPossibility,
                            final List<byte[]> hashedAttributes) {
        this.matrix = matrix;
        this.bVector = vector;
        this.permutationPossibility = permutationPossibility;
        this.hashedAttributes = hashedAttributes;
    }


    public List<byte[]> solve(){
        final FieldVector<BigFraction> solutionVector =  new FieldLUDecomposition<>(matrix).getSolver().solve(bVector);
        final int resultSize = bVector.getDimension() + permutationPossibility.getPossibleFixPoints().length;
        final byte[][] calculatedHashArray = new byte[resultSize][];

        return Arrays.asList(calculatedHashArray);
    }
}
