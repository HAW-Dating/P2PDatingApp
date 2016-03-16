/*
 * Copyright (c) 2015. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 12/16/15 by s-gheldd
 */
public class BottleTest {
    Bottlable bottlable;
    Bottle bottle;

    @Before
    public void setup() {
        this.bottlable = new BottlableTest();
    }


    @Test
    public void testConstructor() {
        this.bottle = new Bottle(this.bottlable);
    }

    @Test
    public void testFill() throws Exception {
        this.bottle = new Bottle(this.bottlable);
        this.bottle.fill();
        Field field = this.bottle.getClass().getDeclaredField("necessaryAttributes");
        field.setAccessible(true);
        assertEquals("hallo", ((ArrayList<String>) field.get(this.bottle)).get(0));
    }

    @Test
    public void testSeal() throws Exception {
        this.bottle = new Bottle(this.bottlable);
        this.bottle.fill();
        this.bottle.cork();
        Field necessaryHash = this.bottle.getClass().getDeclaredField("hashedNecessaryAttributes");
        Field optionalHash = this.bottle.getClass().getDeclaredField("hashedOptionalAttributeFields");
        optionalHash.setAccessible(true);
        necessaryHash.setAccessible(true);
        assertArrayEquals(((((ArrayList<ArrayList<byte[]>>) optionalHash.get(this.bottle)).get(0))).get(0),
                ((ArrayList<byte[]>) necessaryHash.get(this.bottle)).get(0));

        byte[] bytes = (  (ArrayList<byte[]>) necessaryHash.get(this.bottle)).get(0);
        StringBuilder builder = new StringBuilder();
        for (byte x : bytes) {
            builder.append(String.format("%02x", x));
        }
        //sha 256 of "hallo" as of google
        assertEquals("d3751d33f9cd5049c4af2b462735457e4d3baf130bcbb87f389e349fbaeb20b9", builder.toString());
    }


    @Test
    public void testGetHintMatrix(){
        this.bottle = new Bottle(this.bottlable);
        this.bottle.fill();
        this.bottle.cork();
        this.bottle.seal();
        this.bottle.getHintMatrix(0);

    }

}