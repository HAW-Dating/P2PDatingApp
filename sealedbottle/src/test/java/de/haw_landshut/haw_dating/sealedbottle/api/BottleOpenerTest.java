package de.haw_landshut.haw_dating.sealedbottle.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import de.haw_landshut.haw_dating.sealedbottle.algorithm.Bottle;
import de.haw_landshut.haw_dating.sealedbottle.algorithm.CorkscrewLinearEquation;
import de.haw_landshut.haw_dating.sealedbottle.algorithm.MockProfiles;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p>
 * 5/4/16 by s-gheldd
 */
public class BottleOpenerTest {
    private String safeWord = "loveline";
    private String[] hintWords = new String[]{"age", "hobbies"};
    private MessageInABottle incoming;
    private Bottle own;
    private BottleOpener matching;


    @Before
    public void setUp() {
        final Bottlable search = new MockProfiles.Search();
        final Bottlable match = new MockProfiles.Match();
        incoming = new MessageInABottle(new Bottle(search).fill().cork().seal(), safeWord,
                hintWords, 1);
        own = new Bottle(match).fill().cork().seal();
        matching = new BottleOpener(incoming, own);
    }


    @Test
    public void testIsOpeningPossible() throws Exception {
        Assert.assertTrue(matching.isOpeningPossible());
    }

    @Test
    public void testTestSolution() throws Exception {


    }
}