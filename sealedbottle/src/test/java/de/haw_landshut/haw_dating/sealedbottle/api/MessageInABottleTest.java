package de.haw_landshut.haw_dating.sealedbottle.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.haw_landshut.haw_dating.sealedbottle.MockProfiles;
import de.haw_landshut.haw_dating.sealedbottle.algorithm.Bottle;

import static org.junit.Assert.*;

/**
 * Created by Georg on 30.04.2016.
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
        String message = MessageInABottle.serialize(messageInABottle);
        Assert.assertTrue(messageInABottle.equals(MessageInABottle.deSerialize(message)));
    }


}