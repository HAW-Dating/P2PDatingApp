package haw_dating.haw_landshut.de.sealedbottle;

import android.support.annotation.NonNull;
/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 11/20/15 by s-gheldd
 */

public interface Bottlable {
    /**
     * Get the number of necessary attributes for the key generation.
     *
     * @return number of necessary attributes
     */
    int getNumberOfNecessaryAttributes();

    /**
     * Get the number of optional attributes for the key generation.
     *
     * @return number of optional attributes
     */
    int getNumberOfOptionalAttributes();

    /**
     * Get the number of optional attributes that must be identical for a successful match.
     *
     * @return number of necessary optional attributes, must be smaller than getNumberOfOptionalAttributes()
     */
    int getSimilarityThreshold();

    /**
     * Return a single necessary Attribute. Must return at least getNumberOfNecessaryAttributes()
     * times a valid not null String.
     *
     * @return a necessary Attribute
     */
    @NonNull
    String getNecessaryAttribute();

    /**
     * Return a single optional Attribute. Must return at least getNumberOfOptionalAttributes() times a valid not null String.
     *
     * @return an optional Attribute
     */
    @NonNull
    String getOptionalAttribute();
}
