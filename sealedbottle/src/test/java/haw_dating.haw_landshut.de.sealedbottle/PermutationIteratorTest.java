/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 3/17/16 by s-gheldd
 */
public class PermutationIteratorTest extends TestCase {
    final Character[] vector = new Character[]{'a', 'b', 'c'};

    public void testHasNext() throws Exception {
        final Iterator<Character[]> iterator = new Corkscrew.PermutationIterator<>(vector);
        Assert.assertTrue(iterator.hasNext());
        for (int i = 0; i < 6; i++) {
            iterator.next();
        }
        Assert.assertFalse(iterator.hasNext());
    }

    public void testNext() throws Exception {
        final Set<Character[]> set = new HashSet<>();
        set.addAll(Arrays.asList(new Character[]{'a', 'b', 'c'},
                new Character[]{'a', 'c', 'b'},
                new Character[]{'b', 'a', 'c'},
                new Character[]{'b', 'c', 'a'},
                new Character[]{'c', 'b', 'a'},
                new Character[]{'c', 'a', 'b'}));
        final Iterator<Character[]> iterator = new Corkscrew.PermutationIterator<>(vector);
        while (iterator.hasNext()) {
            Assert.assertTrue(set.remove(iterator.next()));
        }
        Assert.assertTrue(set.isEmpty());
        NoSuchElementException exception = null;
        try {
            iterator.next();
        } catch (NoSuchElementException ex) {
            exception = ex;
        }
        Assert.assertNotNull(exception);
    }

    public void testRemove() throws Exception {
        UnsupportedOperationException exception = null;
        final Iterator<Character[]> iterator = new Corkscrew.PermutationIterator<>(vector);
        iterator.next();
        try {
            iterator.remove();
        } catch (UnsupportedOperationException ex) {
            exception = ex;
        }
        Assert.assertNotNull(exception);
    }
}