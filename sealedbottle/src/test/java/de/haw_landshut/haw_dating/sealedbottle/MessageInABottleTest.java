/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package de.haw_landshut.haw_dating.sealedbottle;

import org.junit.Before;
import org.junit.Test;

import de.haw_landshut.haw_dating.sealedbottle.MockProfiles;
import de.haw_landshut.haw_dating.sealedbottle.algorithm.Bottle;
import de.haw_landshut.haw_dating.sealedbottle.api.MessageInABottle;

import static org.junit.Assert.*;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 4/26/16 by s-gheldd
 */
public class MessageInABottleTest {
    String safeWord = "loveline";
    String[] hintWords;
    int version = 0;
    MessageInABottle messageInABottle;

    @Before
    public void setUp() throws Exception {
        hintWords = new String[]{"age", "hobbies"};
        messageInABottle = new MessageInABottle(new Bottle(new MockProfiles.Search()).fill().cork().seal(), safeWord, hintWords, version);
    }

    @Test
    public void testSerialize() throws Exception {
        System.out.println(MessageInABottle.serialize(messageInABottle));
    }

    @Test
    public void testDeSerialize() throws Exception {

    }
}