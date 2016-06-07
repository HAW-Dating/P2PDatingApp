package de.haw_landshut.haw_dating.p2pdatingapp.data_profil_save;

import android.graphics.drawable.LevelListDrawable;
import android.support.annotation.NonNull;
import de.haw_landshut.haw_dating.sealedbottle.api.Bottlable;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by daniel on 02.06.16.
 */
public class StorageProfile implements Bottlable {
    private static LinkedList <String> toBottlable = new LinkedList<>();
    private static String name, age, gender, studie, university, interests, hometown, postalCode, sexualPreference;

    private LinkedList<String> neccassaryAttributes = new LinkedList<>();
    private int numberOfNeccessary;
    private int numberOfOptionalFields = 2;
    private LinkedList<String>[] optionalAttributeFields = new LinkedList[numberOfOptionalFields];
    private int[] numberOfOptionals = new int[numberOfOptionalFields];
    private int[] simmilarityThreshold = new int[numberOfOptionalFields];


    public StorageProfile(String[] data){
        name = data[0];
        age = data[1];
        gender = data[2];
        studie = data[3];
        university = data[4];
        interests = data[5];
        hometown = data[6];
        postalCode = data[7];
        sexualPreference = data[8];

        neccassaryAttributes.addAll(Arrays.asList(gender, university,sexualPreference));
        numberOfNeccessary = neccassaryAttributes.size();

        LinkedList<String> field = new LinkedList<>();
        field.addAll(Arrays.asList(interests,hometown,))



    }

    public static String getName() {return name;}
    public static String getAge() {return age;}
    public static String getGender() {return gender;}
    public static String getStudie() {
        return studie;
    }
    public static String getUniversity() {
        return university;
    }
    public static String getInterests() {
        return interests;
    }
    public static String getHometown() {
        return hometown;
    }
    public static String getPostalCode() {
        return postalCode;
    }
    public static String getSexualPreference() {
        return sexualPreference;
    }


    @Override
    public int getNumberOfOptionalAttributeFields() {
        return 0;
    }

    @Override
    public int getNumberOfNecessaryAttributes() {
        return 0;
    }

    @Override
    public int getNumberOfOptionalAttributes(int field) {
        return 0;
    }

    @Override
    public int getSimilarityThreshold(int field) {
        return 0;
    }

    @NonNull
    @Override
    public String getNecessaryAttribute() {
        return null;
    }

    @NonNull
    @Override
    public String getOptionalAttribute(int field) {
        return null;
    }
}
