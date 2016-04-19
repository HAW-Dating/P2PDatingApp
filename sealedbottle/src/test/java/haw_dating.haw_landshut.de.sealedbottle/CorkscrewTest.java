/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;


import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
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
    public void testSemanticEquals(){
        final Corkscrew.PermutationPossibility possibility1 = new Corkscrew.PermutationPossibility(new int[]{0,1,2},new int[]{0,2});
        final Corkscrew.PermutationPossibility possibility2 = new Corkscrew.PermutationPossibility(new int[]{1,2,0}, new int[]{2,1});
        Assert.assertTrue(possibility1.semanticEquals(possibility2));
    }


    @Test
    public void testProbeSeal() throws Exception {
        firstBottle = new Bottle(firstThree);
        secondBottle = new Bottle(secondThree);
        thirdBottle = new Bottle(simple);
        firstBottle.fill().cork().seal();
        secondBottle.fill().cork().seal();
        thirdBottle.fill().cork().seal();
        assertEquals(3, new Corkscrew().probeSeal(firstBottle.getReminderVectorOptional(0), secondBottle.getReminderVectorOptional(0)));
        assertEquals(0, new Corkscrew().probeSeal(secondBottle.getReminderVectorNecessary(), thirdBottle.getReminderVectorNecessary()));
        assertEquals(2, new Corkscrew().probeSeal(firstBottle.getReminderVectorNecessary(), secondBottle.getReminderVectorNecessary()));
    }


    @Test(timeout = 500)
    public void testFindMissingHashes() throws Exception {
        firstBottle = new Bottle(firstThree);
        firstBottle.fill().cork().seal();
        final Corkscrew corkscrew = new Corkscrew();
        corkscrew.findMissingHashes(firstBottle.getHintMatrix(0),
                firstBottle.getHashedOptionalAttributeField(0),
                firstBottle.getReminderVectorOptional(0),
                firstBottle.getReminderVectorOptional(0),
                firstBottle.getSimilarityThreshold(0));
    }
}