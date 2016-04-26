/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David
 * Manhart, Georg Held
 *
 *
 */

package de.haw_landshut.haw_dating.sealedbottle;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 4/26/16 by s-gheldd
 */
public class CorkscrewLinearEquationTest {

    @Test
    public void testSolve() throws Exception {
        Bottle searchBottle = new Bottle(new MockProfiles.Search());
        Bottle matchBottle = new Bottle(new MockProfiles.Match());
        Bottle noMatchBottle = new Bottle(new MockProfiles.NoMatch());


        searchBottle.fill().cork().seal();
        matchBottle.fill().cork().seal();
        noMatchBottle.fill().cork().seal();

        Corkscrew corkscrewMatchOne = new Corkscrew(searchBottle.getHintMatrix(0), matchBottle
                .getHashedOptionalAttributeField(0), searchBottle.getReminderVectorOptional(0),
                matchBottle.getReminderVectorOptional(0), searchBottle.getSimilarityThreshold(0));

        Corkscrew corkscrewMatchTwo = new Corkscrew(searchBottle.getHintMatrix(1), matchBottle
                .getHashedOptionalAttributeField(1), searchBottle.getReminderVectorOptional(1),
                matchBottle.getReminderVectorOptional(1), searchBottle.getSimilarityThreshold(1));

        Corkscrew corkscrewNoMatchOne = new Corkscrew(searchBottle.getHintMatrix(0),
                noMatchBottle.getHashedOptionalAttributeField(0),
                searchBottle.getReminderVectorOptional(0), noMatchBottle
                .getReminderVectorOptional(0), searchBottle.getSimilarityThreshold(0));

        Assert.assertTrue(CorkscrewTest.isOnesWasEqual(searchBottle, corkscrewMatchOne, 0));
        Assert.assertTrue(CorkscrewTest.isOnesWasEqual(searchBottle, corkscrewMatchTwo, 1));
        Assert.assertFalse(CorkscrewTest.isOnesWasEqual(searchBottle,corkscrewNoMatchOne,0));


    }
}

