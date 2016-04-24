/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;


import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.linear.FieldVector;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p>
 * 3/21/16 by s-gheldd
 */
public class CorkscrewTest {
    private final Bottlable firstThree = new BottleableTest.ThreeOptionalDifferentAttributesBottleable();
    private final Bottlable secondThree = new BottleableTest.ThreeOptionalDifferentAttributesBottleable();
    private final Bottlable simple = BottleableTest.SIMPLE_BOTTLEABLE;
    private Bottle firstBottle;
    private Bottle secondBottle;
    private Bottle thirdBottle;

    @Test
    public void testEquals() {
        final Corkscrew.PermutationPossibility possibility1 = new Corkscrew.PermutationPossibility(new int[]{0, 1, 2}, new int[]{0, 2});
        final Corkscrew.PermutationPossibility possibility2 = new Corkscrew.PermutationPossibility(new int[]{1, 2, 0}, new int[]{2, 1});
        final Corkscrew.PermutationPossibility possibility3 = new Corkscrew.PermutationPossibility(new int[]{0, 1, 2}, new int[]{2, 1});
        Assert.assertTrue(possibility1.equals(possibility2));
        Assert.assertTrue(possibility2.equals(possibility1));
        Assert.assertFalse(possibility1.equals(possibility3));
        Assert.assertFalse(possibility3.equals(possibility2));
    }

    @Test
    public void testHashCode() {
        final Corkscrew.PermutationPossibility possibility1 = new Corkscrew.PermutationPossibility(new int[]{0, 1, 2}, new int[]{0, 2});
        final Corkscrew.PermutationPossibility possibility2 = new Corkscrew.PermutationPossibility(new int[]{1, 2, 0}, new int[]{2, 1});
        final Corkscrew.PermutationPossibility possibility3 = new Corkscrew.PermutationPossibility(new int[]{0, 1, 2}, new int[]{2, 1});
        Assert.assertEquals(possibility1.hashCode(),possibility2.hashCode());
        Assert.assertNotEquals(possibility1.hashCode(),possibility3.hashCode());
    }


    @Test
    public void testProbeSeal() throws Exception {
        firstBottle = new Bottle(firstThree);
        secondBottle = new Bottle(secondThree);
        thirdBottle = new Bottle(simple);
        firstBottle.fill().cork().seal();
        secondBottle.fill().cork().seal();
        thirdBottle.fill().cork().seal();
        assertEquals(3, Corkscrew.probeSeal(firstBottle.getReminderVectorOptional(0), secondBottle.getReminderVectorOptional(0)));
        assertEquals(0, Corkscrew.probeSeal(secondBottle.getReminderVectorNecessary(), thirdBottle.getReminderVectorNecessary()));
        assertEquals(2, Corkscrew.probeSeal(firstBottle.getReminderVectorNecessary(), secondBottle.getReminderVectorNecessary()));
    }


    @Test
    public void testFindMissingHashes() throws Exception {
        firstBottle = new Bottle(firstThree);
        firstBottle.fill().cork().seal();
        secondBottle = new Bottle(simple);
        secondBottle.fill().cork().seal();
        final Corkscrew corkscrew = new Corkscrew(
                firstBottle.getHintMatrix(0),
                firstBottle.getHashedOptionalAttributeField(0),
                firstBottle.getReminderVectorOptional(0),
                firstBottle.getReminderVectorOptional(0),
                firstBottle.getSimilarityThreshold(0));

        boolean onesWasEqual = false;
        for (CorkscrewLinearEquation equation :
                corkscrew) {
            int numberOfEquals = 0;
            List<byte[]> attributes = firstBottle.getHashedOptionalAttributeField(0);
            List<byte[]> solution = equation.solve();
            for (int i = 0; i < attributes.size(); i++) {
                if (Arrays.equals(attributes.get(i),solution.get(i))) {
                    numberOfEquals++;
                }
            }
            onesWasEqual = attributes.size() == numberOfEquals;
            if (onesWasEqual) {
                break;
            }
        }
        Assert.assertTrue(onesWasEqual);
    }
}