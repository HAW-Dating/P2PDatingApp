package haw_dating.haw_landshut.de.p2pdatingapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by daniel on 28.11.15.
 */
public class MyProfilActivity extends Activity {

    private String[] arraySpinnerGender;
    private String[] arraySpinnerUniversity;
    private String[] arraySpinnerSexual_preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_profil_main);

        /* Fuer Spinner Geschlecht */
        arraySpinnerGender = new String[]{"w" , "m" , "w/m"};

        Spinner gender = (Spinner) findViewById(R.id.gender);
        ArrayAdapter<String> adapterGender =
        new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinnerGender);
        gender.setAdapter(adapterGender);

        /* Fuer Spinner Universität */
        arraySpinnerUniversity = new String[]{"HAW-Landshut" , "Uni-Regensburg" };

        Spinner university = (Spinner) findViewById(R.id.university);
        ArrayAdapter<String> adapterUniversity =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinnerUniversity);
        university.setAdapter(adapterUniversity);

        /* Fuer Spinner Suche-Geschlecht */
        arraySpinnerSexual_preference = new String[]{"w" , "m" , "w/m"};

        Spinner searchSexual_preference = (Spinner) findViewById(R.id.sexual_preference);
        ArrayAdapter<String> adapterSearchGender =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinnerSexual_preference);
        searchSexual_preference.setAdapter(adapterSearchGender);


    }




}

