/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David
 * Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.FieldVector;

import java.util.List;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 4/22/16 by s-gheldd
 */
public class LinearEquation {
    final FieldMatrix<BigFraction> matrix;
    final FieldVector<BigFraction> resultVector;
    final Corkscrew.PermutationPossibility permutationPossibility;
    final List<byte[]> hashedAttributes;


    LinearEquation(final FieldMatrix<BigFraction> matrix, final FieldVector<BigFraction> vector,
                   final Corkscrew.PermutationPossibility permutationPossibility,
                   final List<byte[]> hashedAttributes) {
        this.matrix = matrix;
        this.resultVector = vector;
        this.permutationPossibility = permutationPossibility;
        this.hashedAttributes = hashedAttributes;
    }
}
