/*
 * Copyright (c) 2015. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package haw_dating.haw_landshut.de.sealedbottle;

import java.text.CharacterIterator;
import java.text.Normalizer;
import java.text.StringCharacterIterator;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 12/15/15 by s-gheldd
 */
public class StringNormalizer {

    public static String normalize(String input) {
        final StringCharacterIterator iterator = new StringCharacterIterator(Normalizer.normalize(input, Normalizer.Form.NFKC));
        final StringBuilder builder = new StringBuilder();
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
            } else if (Character.isLetter(c)) {
                if (Character.isUpperCase(c)) {
                    builder.append(Character.toLowerCase(c));
                } else {
                    builder.append(c);
                }
            }
        }
        return builder.toString();
    }

}
