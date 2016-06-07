/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package de.haw_landshut.haw_dating.sealedbottle.algorithm;

import org.apache.commons.math3.fraction.BigFraction;

import java.math.BigInteger;
import java.text.CharacterIterator;
import java.text.Normalizer;
import java.text.StringCharacterIterator;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 12/15/15 by s-gheldd
 */
public class BottleUtil {

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

    public static byte calculateReminderSeven(final byte[] hash) {
        byte erg = 0;
        //System.out.println(Arrays.toString(hash) + ":");
        for (int i = 1; hash.length - i >= 0; i++) {
            final int remainder;
            switch (i % 3) {
                case 1:
                    remainder = 1;
                    break;
                case 2:
                    remainder = 4;
                    break;
                case 0:
                    remainder = 2;
                    break;
                default:
                    remainder = 0;
            }
            erg = (byte)(((hash[hash.length - i] & 0xFF) * remainder + erg) % 7);
            //System.out.println(erg);
        }
        return erg;
    }

    public static BigFraction makeBigFractionFromByteArray(final byte[] bytes){
        return new BigFraction(new BigInteger(bytes));
    }
}
