/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package de.haw_landshut.haw_dating.sealedbottle;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 3/17/16 by s-gheldd
 */
public class CorkscrewPermutationIteratorTest extends TestCase {
    final Character[] vector = new Character[]{'a', 'b', 'c'};

    public void testHasNext() throws Exception {
        final Iterator<Character[]> iterator = new CorkscrewUtil.CorkscrewPermutationIterator<>(vector);
        Assert.assertTrue(iterator.hasNext());
        for (int i = 0; i < 6; i++) {
            iterator.next();
        }
        Assert.assertFalse(iterator.hasNext());
    }

    public void testNext() throws Exception {
        final Set<Character[]> set = new TreeSet<>(new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                if (lhs == rhs) {
                    return 0;
                } else if (lhs == null || ! (lhs instanceof Character[])){
                    return -1;
                } else if (rhs == null || ! (rhs instanceof Character[])) {
                    return 1;
                } else {
                    Character[] left = (Character[]) lhs;
                    Character[] right = (Character[]) rhs;
                    if (left.length != right.length) {
                        return left.length - right.length;
                    } else {
                        for (int i = 0; i< left.length; i++) {
                            int cmp;
                            if ((cmp = left[i]-right[i]) != 0) {
                                return cmp;
                            }
                        }
                    }
                    return 0;
                }
            }
        });
        set.addAll(Arrays.asList(new Character[]{'a', 'b', 'c'},
                new Character[]{'a', 'c', 'b'},
                new Character[]{'b', 'a', 'c'},
                new Character[]{'b', 'c', 'a'},
                new Character[]{'c', 'b', 'a'},
                new Character[]{'c', 'a', 'b'}));
        final Iterator<Character[]> iterator = new CorkscrewUtil.CorkscrewPermutationIterator<>(vector);
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
        final Iterator<Character[]> iterator = new CorkscrewUtil.CorkscrewPermutationIterator<>(vector);
        iterator.next();
        try {
            iterator.remove();
        } catch (UnsupportedOperationException ex) {
            exception = ex;
        }
        Assert.assertNotNull(exception);
    }
}