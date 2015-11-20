/*
 * Copyright (c) 2015. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import java.util.ArrayList;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 11/20/15 by s-gheldd
 */

public class Bottle {
    private final Bottlable bottlable;

    private final ArrayList<String> necessaryAttributes = new ArrayList<>();
    private final ArrayList<char[]> hashedNecessaryAttributes = new ArrayList<>();

    private final ArrayList<String> optionalAttributes = new ArrayList<>();
    private final ArrayList<char[]> hashedOptionalAttributes = new ArrayList<>();

    private final int similarilyThreshold;
    private final int numberOfNecessaryAttributes;
    private final int numberOfOptionalAttributes;

    private int recievedNeccessaryAttributes = 0;
    private int recievedOptionalAttributes = 0;


    Bottle(Bottlable bottlable) {
        final int numberOfNecessaryAttributes, numberOfOptionalAttributes, similarityThreshold;

        numberOfNecessaryAttributes = bottlable.getNumberOfNecessaryAttributes();
        numberOfOptionalAttributes = bottlable.getNumberOfOptionalAttributes();
        similarityThreshold = bottlable.getSimilarityThreshold();

        this.bottlable = bottlable;

        if (similarityThreshold >= numberOfOptionalAttributes) {
            throw new IllegalArgumentException("getNumberOfOptionalAttributes() returned: " + numberOfOptionalAttributes
                    + ", must be at least: " + (similarityThreshold + 1));
        } else {
            this.numberOfNecessaryAttributes = numberOfNecessaryAttributes;
            this.numberOfOptionalAttributes = numberOfOptionalAttributes;
            this.similarilyThreshold = similarityThreshold;
        }
    }



/*    private String normaliseString(String input) {
        StringCharacterIterator iterator = new StringCharacterIterator(input);
        StringBuilder builder = new StringBuilder();
        for (char c = iterator.first(); c != CharacterIterator.DONE; c = iterator.next()) {
            if (Character.isWhitespace(c)) {
                continue;
            } else if (Character.isDigit(c)) {
                switch (c) {
                    case '0':
                        builder.append("zero");
                        break;
                    case '1':
                        builder.append("one");
                        break;
                    case '2':
                        builder.append("two");
                        break;
                    case '3':
                        builder.append("three");
                        break;
                    case '4':
                        builder.append("four");
                        break;
                    case '5':
                        builder.append("five");
                        break;
                    case '6':
                        builder.append("six");
                        break;
                    case '7':
                        builder.append("seven");
                        break;
                    case '8':
                        builder.append("eight");
                        break;
                    case '9':
                        builder.append("nine");
                        break;
                    default:
                        continue;
                }
            } else if (Character.isLetter(c));
        }
        return null;
    }*/

}
