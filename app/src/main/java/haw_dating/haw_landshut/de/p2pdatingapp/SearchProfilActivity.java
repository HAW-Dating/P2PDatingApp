package haw_dating.haw_landshut.de.p2pdatingapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by daniel on 08.12.15.
 */
public class SearchProfilActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_profil_main);

        /* Fuer Spinner Geschlecht */
        Spinner gender = (Spinner) findViewById(R.id.search_gender);
        ArrayAdapter<String> adapterGender =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sexual_spinner));
        gender.setAdapter(adapterGender);

        /* Fuer Spinner Universität */
        Spinner university = (Spinner) findViewById(R.id.search_university);
        ArrayAdapter<String> adapterUniversity =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.university_spinner));
        university.setAdapter(adapterUniversity);

        /* Fuer Spinner Suche-Geschlecht */
        Spinner searchSexual_preference = (Spinner) findViewById(R.id.search_sexual_preference);
        ArrayAdapter<String> adapterSearchGender =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sexual_spinner));
        searchSexual_preference.setAdapter(adapterSearchGender);

    }
}
