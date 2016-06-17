package de.haw_landshut.haw_dating.p2pdatingapp.data;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import de.haw_landshut.haw_dating.sealedbottle.api.Bottlable;

/**
 * Created by daniel on 02.06.16.
 */
public class StorageProfile implements Bottlable {

    public static int[] similarityThresholds = new int[]{3};
    private static Gson gson = new Gson();
    private final Map<Integer, String> profileData;
    private final Integer[] profileFields;
    private final Integer[] necessaryFields;
    private final Integer[][] optionalFields;
    private Queue<String> necessaryQueue;
    private Queue<String>[] optionalQueue = new Queue[getNumberOfOptionalAttributeFields()];


    public StorageProfile(final Map<Integer, String> profileData, final Integer[] profileFields,
                          final Integer[] necessaryFields, final Integer[][] optionalFields) {
        this.necessaryFields = necessaryFields;
        this.optionalFields = optionalFields;
        this.profileData = profileData;
        this.profileFields = profileFields;
    }

    public static StorageProfile deSerialize(String json) {
        return gson.fromJson(json, StorageProfile.class);
    }

    public String serialize() {
        return gson.toJson(this);
    }

    public Map<Integer, String> getProfileData() {
        return profileData;
    }

    public Integer[] getProfileFields() {
        return profileFields;
    }

    @Override
    public int getNumberOfOptionalAttributeFields() {
        return 1;
    }

    @Override
    public int getNumberOfNecessaryAttributes() {
        necessaryQueue = new LinkedList<>();

        for (final Integer attributeId : necessaryFields) {
            final String attributeString = profileData.get(attributeId);
            final String[] attributeStringArray = attributeString.split(",");
            necessaryQueue.addAll(Arrays.asList(attributeStringArray));
        }
        return necessaryQueue.size();
    }

    @Override
    public int getNumberOfOptionalAttributes(int field) {
        optionalQueue[field] = new LinkedList<>();

        for (final Integer attributeId : optionalFields[field]) {
            final String attributeString = profileData.get(attributeId);
            final String[] attributeStringArray = attributeString.split(",");
            optionalQueue[field].addAll(Arrays.asList(attributeStringArray));
        }

        return optionalQueue[field].size();
    }

    @Override
    public int getSimilarityThreshold(int field) {
        return similarityThresholds[field];
    }

    @NonNull
    @Override
    public String getNecessaryAttribute() {
        return necessaryQueue.remove();
    }

    @NonNull
    @Override
    public String getOptionalAttribute(int field) {
        return optionalQueue[field].remove();
    }
}
