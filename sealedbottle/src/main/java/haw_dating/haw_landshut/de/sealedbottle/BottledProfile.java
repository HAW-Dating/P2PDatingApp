package haw_dating.haw_landshut.de.sealedbottle;

import java.text.CharacterIterator;
import java.text.Normalizer;
import java.text.StringCharacterIterator;
import java.util.ArrayList;

/**
 * Created by Georg on 20.11.2015.
 */
public class BottledProfile {

    private final ArrayList<String> necessaryAttributes = new ArrayList<>();
    private final ArrayList<char[]> hashedNecessaryAttributes = new ArrayList<>();

    private final ArrayList<String> optionalAttributes = new ArrayList<>();
    private final ArrayList<char[]> hashedOptionalAttributes = new ArrayList<>();

    private final int similarilyThreshold;
    private final int numberOfNecessaryAttributes;
    private final int numberOfOptionalAttributes;

    private int recievedNeccessaryAttributes = 0;
    private int recievedOptionalAttributes = 0;

    BottledProfile(int numberOfNecessaryAttributes, int numberOfOptionalAttributes, int similarilyThreshold) throws IllegalArgumentException {
        if (numberOfOptionalAttributes <= similarilyThreshold)
            throw new IllegalArgumentException("numberOfOptionalAttributes = "
                    + numberOfOptionalAttributes + ", needs to be at least: " + (similarilyThreshold + 1));
        else {
            this.similarilyThreshold = similarilyThreshold;
            this.numberOfNecessaryAttributes = numberOfNecessaryAttributes;
            this.numberOfOptionalAttributes = numberOfOptionalAttributes;
        }
    }

    public boolean addNecessaryAttribute(String attribute) {
        if (recievedNeccessaryAttributes < numberOfNecessaryAttributes) {
        }
        return true;
    }


    private String normaliseString(String input) {
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
                continue;
            } else if (Character.isLetter(c));
        }
        return null;
    }

}
