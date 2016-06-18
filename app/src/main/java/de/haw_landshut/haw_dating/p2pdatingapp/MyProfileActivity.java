package de.haw_landshut.haw_dating.p2pdatingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

import de.haw_landshut.haw_dating.p2pdatingapp.data.StorageProfile;

/**
 * Created by daniel on 28.11.15.
 * <p/>
 * Revision by Altrichter Daniel on 15.03.16.
 * Implements OnTouchListener
 * wird gebraucht für die Wischfunktionen.
 * <p/>
 * Revision by Altrichter Daniel on 4.04.16.
 * einfügen eines Navigation Drawers.
 */
public class MyProfileActivity extends AbstractProfileActivity implements View.OnTouchListener, View
        .OnClickListener {


    public static final Integer[] necessaryFields = new Integer[]{
            R.id.profile_gender,
            R.id.profile_university,
            R.id.profile_sexual_preference,
            R.id.profile_age};

    public static final Integer[][] optionalFields = new Integer[][]{{
            R.id.profile_hometown,
            R.id.profile_interests_1,
            R.id.profile_interests_2,
            R.id.profile_interests_3,
            R.id.profile_studies}
    };

    public static final Integer[] profileFields = new Integer[]{
            R.id.profile_gender,
            R.id.profile_university,
            R.id.profile_sexual_preference,
            R.id.profil_name,
            R.id.profile_age,
            R.id.profile_studies,
            R.id.profile_interests_1,
            R.id.profile_interests_2,
            R.id.profile_interests_3,
            R.id.profile_hometown,
            R.id.profil_postal_code};


    private EditText editTextName, editTextAge, editTextStudie, editTextInterests, editTextHometown,
            editTextPostal_code;
    private int selectedPosition;
    private Spinner university, gender, searchSexual_preference;
    private ListView drawerList;
    private ArrayAdapter<String> adapter;
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
    private Map<Integer, String> profileData = new HashMap<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profil_main);


        /* Fuer Spinner Geschlecht */
        gender = (Spinner) findViewById(R.id.profile_gender);
        ArrayAdapter<String> adapterGender =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources
                        ().getStringArray(R.array.sexual_spinner));
        gender.setAdapter(adapterGender);

        /* Fuer Spinner Universität */
        university = (Spinner) findViewById(R.id.profile_university);
        ArrayAdapter<String> adapterUniversity =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources
                        ().getStringArray(R.array.university_spinner));
        university.setAdapter(adapterUniversity);

        /* Fuer Spinner Suche-Geschlecht */
        searchSexual_preference = (Spinner) findViewById(R.id.profile_sexual_preference);
        ArrayAdapter<String> adapterSearchGender =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources
                        ().getStringArray(R.array.sexual_spinner));
        searchSexual_preference.setAdapter(adapterSearchGender);

        LinearLayout bildschirm = (LinearLayout) findViewById(R.id
                .my_profile_activity_linear_layout);
        bildschirm.setOnTouchListener(this);


        // SharedPreferences Datei öffnen
        // Editorklasse initialisieren
        editTextName = (EditText) findViewById(R.id.profil_name);
        editTextAge = (EditText) findViewById(R.id.profile_age);
        editTextStudie = (EditText) findViewById(R.id.profile_studies);
        editTextInterests = (EditText) findViewById(R.id.profile_interests_1);
        editTextHometown = (EditText) findViewById(R.id.profile_hometown);
        editTextPostal_code = (EditText) findViewById(R.id.profil_postal_code);

        //Button klicken
        final Button button = (Button) findViewById(R.id.profil_button);
        button.setOnClickListener(this);


        // Navigations Drawer
        drawerList = (ListView) findViewById(R.id.main_lv_menu);
        addDrawerItems();

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    // MyProfileActivity
                    case 0:
                        myProfil();
                        break;
                    // SuchProfil
                    case 1:
                        searchProfil();
                        break;
                    // FindYourLove
                    case 2:
                        findYourLove();
                        break;
                    // Wenn noch Zeit dann Einstellungen hinzufügen!!!
                    default:
                        break;
                }
            }
        });
    }

    private void addDrawerItems() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                (getResources().getStringArray(R.array.drawer_list_menu_array)));
        drawerList.setAdapter(adapter);
    }

    private void myProfil() {
        Intent intent = new Intent(this, MyProfileActivity.class);
        startActivity(intent);
    }

    private void searchProfil() {
        Intent intent = new Intent(this, SearchProfileActivity.class);
        startActivity(intent);
    }

    private void findYourLove() {
        Intent intent = new Intent(this, FindYourLoveActivity.class);
        startActivity(intent);
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
                Intent intent = new Intent(this, MyProfileControlActivity.class);
                startActivity(intent);

            } else if ((touchX - tx) <= -pixel) {

            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        final String serializedProfile = preferences.getString(getStringDataById(
                R.string.shared_preference_profile), STRING_DEF_VALUE);
        Log.d("stored profile", serializedProfile);
        if (serializedProfile != STRING_DEF_VALUE) {
            final StorageProfile storedProfile = StorageProfile.deSerialize(serializedProfile);
            for (final int id : storedProfile.getProfileFields()) {
                restoreInput(id, storedProfile.getProfileData().get(id));
            }

        }

    }

    @Override
    public void onClick(View v) {


        for (Integer attributeId : profileFields) {
            profileData.put(attributeId, getStringDataById(attributeId));

        }


        StorageProfile myProfile = new StorageProfile(profileData, profileFields,
                necessaryFields, optionalFields);


        // SharedPreferences Datei öffnen
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        // Editorklasse initialisieren
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        // Text mit Schlüsselattribut holen und in Editorklasse schreiben
        preferenceEditor.putString(getStringDataById(R.string.shared_preference_profile),
                myProfile.serialize());
        preferenceEditor.commit();
    }
}
