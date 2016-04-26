/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David
 * Manhart, Georg Held
 *
 *
 */

package de.haw_landshut.haw_dating.sealedbottle;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import de.haw_landshut.haw_dating.sealedbottle.api.Bottlable;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 4/26/16 by s-gheldd
 */
public abstract class MockProfiles implements Bottlable {

    ArrayList<Queue<String>> optionalAttributes = new ArrayList<>();
    Queue<String> necessaryAttributes;

    MockProfiles() {
        this.necessaryAttributes = new LinkedList<>(Arrays.asList("männlich", "Student", "ledig",
                "Landshut", "HaW Landshut"));
    }

    @NonNull
    @Override
    public String getNecessaryAttribute() {
        return necessaryAttributes.remove();
    }

    @Override
    public int getNumberOfOptionalAttributeFields() {
        return 2;
    }

    @Override
    public int getNumberOfNecessaryAttributes() {
        return 5;
    }

    @Override
    public int getNumberOfOptionalAttributes(int field) {
        switch (field) {
            case 0:
                return 5;
            case 1:
                return 3;
            default:
                return -1;
        }
    }

    @Override
    public int getSimilarityThreshold(int field) {
        switch (field) {
            case 0:
                return 3;
            case 1:
                return 1;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public String getOptionalAttribute(int field) {
        return optionalAttributes.get(field).remove();
    }

    public static class Search extends MockProfiles {

        public Search() {
            super();
            optionalAttributes.add(new LinkedList<>(Arrays.asList("Segeln", "Basketball",
                    "Programmieren", "Sex", "Wandern")));
            optionalAttributes.add(new LinkedList<String>(Arrays.asList("25", "26",
                    "27")));
        }


    }

    public static class Match extends MockProfiles {
        public Match() {
            super();
            optionalAttributes.add(new LinkedList<>(Arrays.asList("Bauklötze", "Basketball",
                    "Programmieren", "Sex", "Pfadfinder")));
            optionalAttributes.add(new LinkedList<String>(Arrays.asList("27", "28",
                    "29")));
        }

    }

    public static class NoMatch extends MockProfiles {
        public NoMatch(){
            super();
            optionalAttributes.add(new LinkedList<>(Arrays.asList("Bauklötze", "Baseball",
                    "Programmieren", "Sex", "Pfadfinder")));
            optionalAttributes.add(new LinkedList<String>(Arrays.asList("29", "30",
                    "31")));
        }
    }
}
