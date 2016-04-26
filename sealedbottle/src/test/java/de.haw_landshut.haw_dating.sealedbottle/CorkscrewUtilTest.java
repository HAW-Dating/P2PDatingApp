/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package de.haw_landshut.haw_dating.sealedbottle;

import org.apache.commons.math3.fraction.BigFraction;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 4/20/16 by s-gheldd
 */
public class CorkscrewUtilTest {

    @Test
    public void testRemoveColumn() throws Exception {
        final BigFraction[][] matrix1 = new BigFraction[][]{{new BigFraction(1), new BigFraction(2),
                new BigFraction(3)}, {new BigFraction(4), new BigFraction(5), new BigFraction(6)}};

        final BigFraction[][] matrix2 = new BigFraction[][]{{new BigFraction(1),
                new BigFraction(3)}, {new BigFraction(4), new BigFraction(6)}};

        final BigFraction[][] matrixShrunk = CorkscrewUtil.removeColumn(matrix1, 1);

        Assert.assertFalse(Arrays.deepEquals(matrix1, matrix2));
        Assert.assertTrue(Arrays.deepEquals(CorkscrewUtil.removeColumn(matrix1, 1), matrix2));
    }

}