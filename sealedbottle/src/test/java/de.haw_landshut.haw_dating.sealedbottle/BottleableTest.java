/*
 * Copyright (c) 2015. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package de.haw_landshut.haw_dating.sealedbottle;

import android.support.annotation.NonNull;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p>
 * 12/18/15 by s-gheldd
 */
public abstract class BottleableTest {
    public final static Bottlable SIMPLE_BOTTLEABLE = new SimpleBottleable();

    final public static class ThreeOptionalDifferentAttributesBottleable implements Bottlable {
        private int optionalsGiven = 0;
        private int necessariesGiven = 0;

        @Override
        public int getNumberOfOptionalAttributeFields() {
            return 1;
        }

        @Override
        public int getNumberOfNecessaryAttributes() {
            return 2;
        }

        @Override
        public int getNumberOfOptionalAttributes(int field) {
            return 3;
        }

        @Override
        public int getSimilarityThreshold(int field) {
            return 1;
        }

        @NonNull
        @Override
        public String getNecessaryAttribute() {
            necessariesGiven++;
            switch (necessariesGiven) {
                case 1:
                    return "hallo";
                case 2:
                    return "mama";
                default:
                    return "";
            }
        }

        @NonNull
        @Override
        public String getOptionalAttribute(int field) {
            switch (field) {
                case 0:
                    optionalsGiven++;
                    switch (optionalsGiven) {
                        case 1:
                            return "icecream";
                        case 2:
                            return "basketball";
                        case 3:
                            return "dancing";
                        default:
                            return "";
                    }
                default:
                    return "";
            }
        }
    }

    final private static class SimpleBottleable implements Bottlable {
        @Override
        public int getNumberOfOptionalAttributeFields() {
            return 1;
        }

        @Override
        public int getNumberOfNecessaryAttributes() {
            return 1;
        }

        @Override
        public int getNumberOfOptionalAttributes(final int field) {
            return 1;
        }

        @Override
        public int getSimilarityThreshold(final int field) {
            return 1;
        }

        @NonNull
        @Override
        public String getNecessaryAttribute() {
            return "hallo";
        }

        @NonNull
        @Override
        public String getOptionalAttribute(final int field) {
            return "hallo";
        }
    }
}
