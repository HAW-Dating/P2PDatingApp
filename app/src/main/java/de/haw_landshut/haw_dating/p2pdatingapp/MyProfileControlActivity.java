package de.haw_landshut.haw_dating.p2pdatingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import de.haw_landshut.haw_dating.p2pdatingapp.data.StoredProfile;

/**
 * Created by alisabuchner on 08.12.15.
 * <p/>
 * Revision by Altrichter Daniel on 15.03.16.
 * Implements OnTouchListener
 * wird gebraucht für die Wischfunktionen.
 * <p/>
 * Revision by Altrichter Daniel on 4.04.16.
 * einfügen eines Navigation Drawers.
 */
public class MyProfileControlActivity extends AbstractProfileActivity implements View
        .OnTouchListener {

    public static final Integer[] profileFields = new Integer[]{R.id.profile_control_gender, R
            .id.profile_control_university, R.id.profile_control_sexual_preference, R.id
            .profile_control_name, R.id.profile_control_age, R.id.profile_control_studie, R.id
            .profile_control_interests, R.id.profile_control_hometown, R.id
            .profile_control_postal_code};

    private static final Map<Integer, Integer> translationMap = new HashMap<>();

    /**
     * Created by daniel on 15.03.16.
     * <p/>
     * Positionen erkennen und berechnung von Wischereignissen.
     * Dies Funktioniert nur in den Richtungen die kein Scrollingview besitzen.
     * Hier nach links bzw. rechts
     * <p/>
     * Pixelangaben müssen evtl noch angepasst werden
     * <p/>
     * Beim wischen nach links wird Activity siehe Code (-> XYZ.class) aufgerufen!
     */

    private int touchX;
    private int touchY;


    private static void fillTranslationMap() {
        if (translationMap.isEmpty()) {
            for (int i = 0; i < profileFields.length; i++) {
                translationMap.put(MyProfileControlActivity.profileFields[i], MyProfileActivity
                        .profileFields[i]);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profil_control);

        LinearLayout bildschirm = (LinearLayout) findViewById(R.id.my_profil_control_linear_layout);
        bildschirm.setOnTouchListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        final String serializedProfile = preferences.getString(getStringDataById(R.string
                .shared_preference_profile), STRING_DEF_VALUE);
        Log.d("control profile", serializedProfile);
        if (serializedProfile != STRING_DEF_VALUE) {
            fillTranslationMap();
            final Map<Integer, String> data = StoredProfile.deSerialize(serializedProfile)
                    .getProfileData();
            for (final Integer id : profileFields) {
                ((TextView) findViewById(id)).setText(data.get(translationMap.get(id)));
            }
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        int aktion = event.getAction();

        // 59 Pixel == 0,50cm ; 118 Pixel == 1,00cm
        int pixel = 177;

        if (aktion == MotionEvent.ACTION_DOWN) {
            touchX = (int) event.getX();
            touchY = (int) event.getY();
        }
        if (aktion == MotionEvent.ACTION_UP) {
            int tx = (int) event.getX();
            int ty = (int) event.getY();

            if ((touchX - tx) > pixel) {
                Intent intent = new Intent(this, SearchProfileActivity.class);
                startActivity(intent);

            } else if ((touchX - tx) <= -pixel) {

            }
        }
        return true;
    }
}
