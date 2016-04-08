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
     * Get the number of optional attribute fields.
     *
     * @return number of optional attribute fields
     */
    int getNumberOfOptionalAttributeFields();


    /**
     * Get the number of necessary attributes for the key generation.
     *
     * @return number of necessary attributes
     */
    int getNumberOfNecessaryAttributes();

    /**
     * Get the number of optional attributes, for a given field of optional attributes.
     *
     * @param field the number of the optional attribute field, for which the number of
     *              attributes is queried.
     * @return number of optional attributes
     */
    int getNumberOfOptionalAttributes(final int field);

    /**
     * Get the number of optional attributes that must be identical for a successful match.
     *
     * @param field the number of the optional attribute field, for which the similarity threshold is
     *              queried
     * @return number of necessary optional attributes, must be smaller than getNumberOfOptionalAttributes()
     */
    int getSimilarityThreshold(final int field);

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
     * @param field the number of the optional attribute field, for which the similarity threshold is
     *              queried
     * @return an optional Attribute
     */
    @NonNull
    String getOptionalAttribute(final int field);
}
