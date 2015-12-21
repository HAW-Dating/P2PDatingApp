/*
 * Copyright (c) 2015. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 12/15/15 by s-gheldd
 */
public class BottleUtilTest {

    @Test
    public void normalizehello() throws Exception {
        assertEquals("hello", BottleUtil.normalize("hello"));
    }

    @Test
    public void normalizeHello() {
        assertEquals("hello", BottleUtil.normalize("Hello"));
    }

    @Test
    public void normalize_Hell_O() {
        assertEquals("hello", BottleUtil.normalize(" Hell O"));
    }

    @Test
    public void normalize_he110() {
        assertEquals("heoneonezero", BottleUtil.normalize("he110"));
    }

    @Test
    public void normalize_gaertner() {
        assertEquals("gärtner", BottleUtil.normalize("Gärtner"));
    }


    @Test
    public void calculateRemainderTest() throws Exception {
        Bottle bottle = new Bottle(new BottlableTest());
        bottle.fill();
        bottle.seal();
        Field necessaryHash = bottle.getClass().getDeclaredField("hashedNecessaryAttributes");
        necessaryHash.setAccessible(true);
        byte[] bytes = ((ArrayList<byte[]>) necessaryHash.get(bottle)).get(0);

        assertEquals(1, BottleUtil.calculateReminderSeven(new byte[]{1}));
        //129
        assertEquals(3, BottleUtil.calculateReminderSeven(new byte[]{-127}));
        //1 * 256 + 0
        assertEquals(4, BottleUtil.calculateReminderSeven(new byte[]{1, 0}));
        //255 * 256^2 + 18 * 256 + 5
        assertEquals(6, BottleUtil.calculateReminderSeven(new byte[]{-1, 18, 5}));
        // 0xd3751d33f9cd5049c4af2b462735457e4d3baf130bcbb87f389e349fbaeb20b9 mod 7 == 5
        assertEquals(5, BottleUtil.calculateReminderSeven(bytes));
    }

}