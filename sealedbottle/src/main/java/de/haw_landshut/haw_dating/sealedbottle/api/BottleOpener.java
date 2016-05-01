/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package de.haw_landshut.haw_dating.sealedbottle.api;

import de.haw_landshut.haw_dating.sealedbottle.algorithm.Bottle;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 4/30/16 by s-gheld
 */
public class BottleOpener {

    private final MessageInABottle incomingMessageInABottle;
    private final Bottle ownBottle;

    public BottleOpener(final  MessageInABottle incomingMessageInABottle, final Bottle ownBottle){

        this.incomingMessageInABottle = incomingMessageInABottle;
        this.ownBottle = ownBottle;
    }

    public boolean isOpeningPossible(){



        return true;
    }
}
