/*
 * Copyright (c) 2015. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 12/15/15 by s-gheldd
 */
public class StringNormalizerTest {

    @Test
    public void normalizehello() throws Exception {
        assertEquals("hello", StringNormalizer.normalize("hello"));
    }

    @Test
    public void normalizeHello() {
        assertEquals("hello", StringNormalizer.normalize("Hello"));
    }

    @Test
    public void normalize_Hell_O() {
        assertEquals("hello", StringNormalizer.normalize(" Hell O"));
    }

    @Test
    public void normalize_he110() {
        assertEquals("heoneonezero", StringNormalizer.normalize("he110"));
    }

    @Test
    public void normalize_gaertner() {
        assertEquals("gärtner", StringNormalizer.normalize("Gärtner"));
    }
}