/*
 * Copyright (c) 2015. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import android.support.annotation.NonNull;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 12/18/15 by s-gheldd
 */
public class BottlableTest implements Bottlable {


    @Override
    public int getNumberOfOptionalAttributeFields() {
        return 1;
    }

    @Override
    public int getNumberOfNecessaryAttributes() {
        return 1;
    }

    @Override
    public int getNumberOfOptionalAttributes(int field) {
        return 1;
    }

    @Override
    public int getSimilarityThreshold(int field) {
        return 1;
    }

    @NonNull
    @Override
    public String getNecessaryAttribute() {
        return "hallo";
    }

    @NonNull
    @Override
    public String getOptionalAttribute(int field) {
        return "hallo";
    }
}
