package de.haw_landshut.haw_dating.p2pdatingapp.data_profil_save;

/**
 * Created by daniel on 02.06.16.
 */
public class ProfilDatas {

    private static String name;
    private static String age;
    private static String gender;
    private static String studie;
    private static String university;
    private static String interests;
    private static String hometown;
    private static String postalCode;
    private static String sexualPreference;

    public ProfilDatas(String[] data){
        name = data[0];
        age = data[1];
        gender = data[2];
        studie = data[3];
        university = data[4];
        interests = data[5];
        hometown = data[6];
        postalCode = data[7];
        sexualPreference = data[8];
    }

    public static String getName() {
        return name;
    }

    public static String getAge() {
        return age;
    }

    public static String getGender() {
        return gender;
    }

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


}
