package de.haw_landshut.haw_dating.p2pdatingapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import de.haw_landshut.haw_dating.p2pdatingapp.data_profil_save.StorageProfile;

/**
 * Created by daniel on 28.11.15.
 *
 * Revision by Altrichter Daniel on 15.03.16.
 * Implements OnTouchListener
 * wird gebraucht für die Wischfunktionen.
 *
 * Revision by Altrichter Daniel on 4.04.16.
 * einfügen eines Navigation Drawers.
 *
 */
public class MyProfilActivity extends Activity implements View.OnTouchListener {

    private ListView drawerList;
    private ArrayAdapter<String> adapter;

    EditText editTextName, editTextAge,  editTextStudie, editTextInterests, editTextHometown, editTextPostal_code;
    int selectedPosition;

    Spinner university,gender, searchSexual_preference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profil_main);


        /* Fuer Spinner Geschlecht */
        gender = (Spinner) findViewById(R.id.gender);
        ArrayAdapter<String> adapterGender =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sexual_spinner));
        gender.setAdapter(adapterGender);

        /* Fuer Spinner Universität */
        university = (Spinner) findViewById(R.id.university);
        ArrayAdapter<String> adapterUniversity =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.university_spinner));
        university.setAdapter(adapterUniversity);

        /* Fuer Spinner Suche-Geschlecht */
        searchSexual_preference = (Spinner) findViewById(R.id.sexual_preference);
        ArrayAdapter<String> adapterSearchGender =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sexual_spinner));
        searchSexual_preference.setAdapter(adapterSearchGender);

        LinearLayout bildschirm = (LinearLayout) findViewById(R.id.my_profile_activity_linear_layout);
        bildschirm.setOnTouchListener(this);



        // SharedPreferences Datei öffnen
        SharedPreferences preferences = getSharedPreferences("Profildata", 0);
        // Editorklasse initialisieren
        editTextName = (EditText)findViewById(R.id.profil_name);
        editTextAge = (EditText)findViewById(R.id.profil_age);
        editTextStudie = (EditText)findViewById(R.id.profil_studie);
        editTextInterests = (EditText)findViewById(R.id.profil_interests);
        editTextHometown = (EditText)findViewById(R.id.profil_hometown);
        editTextPostal_code = (EditText)findViewById(R.id.profil_postal_code);



    /*
        // Schlüsselwerte aus der Datei lesen und in Textfelder schreiben
        editTextName.setText(preferences.getString("name", "Huber Sepp"));
        editTextStudie.setText(preferences.getString("studie", "Soziale Arbeit"));
        editTextInterests.setText(preferences.getString("intrests", "Fliegen"));
        editTextHometown.setText(preferences.getString("hometown", "Hamburg"));
        editTextPostal_code.setText(preferences.getString("postal_code", "22113"));

        // Spinner
        gender.setSelection(preferences.getInt("gender", 0));
        university.setSelection(preferences.getInt("university", 0));
        searchSexual_preference.setSelection(preferences.getInt("searchSexual_preference",0));
*/

        //Button klicken
        Button button = (Button) findViewById(R.id.profil_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] profilData = new String[9];

                profilData[0] = editTextName.getText().toString();
                profilData[1] = editTextAge.getText().toString();
                profilData[2] = gender.getSelectedItem().toString();
                profilData[3] = editTextStudie.getText().toString();
                profilData[4] = university.getSelectedItem().toString();
                profilData[5] = editTextInterests.getText().toString();
                profilData[6] = editTextHometown.getText().toString();
                profilData[7] = editTextPostal_code.getText().toString();
                profilData[8] = searchSexual_preference.getSelectedItem().toString();

                StorageProfile myProfil = new StorageProfile(profilData);






                // SharedPreferences Datei öffnen
                SharedPreferences preferences = getSharedPreferences("Profildata", 0);
                // Editorklasse initialisieren
                SharedPreferences.Editor editorPreferences = preferences.edit();
                // Text mit Schlüsselattribut holen und in Editorklasse schreiben
                editorPreferences.putString("name", editTextName.getText().toString());
                editorPreferences.putString("studie", editTextStudie.getText().toString());
                editorPreferences.putString("intrests", editTextInterests.getText().toString());
                editorPreferences.putString("hometown", editTextHometown.getText().toString() );
                editorPreferences.putString("postal_code", editTextPostal_code.getText().toString());
                // Spinner
                /*
                Funktioniert nicht!!!
                editorPreferences.putString("gender", gender.getSelectedItem().toString());
                editorPreferences.putString("university", university.getSelectedItem().toString());
                editorPreferences.putString("searchSexual_preference", searchSexual_preference.getSelectedItem().toString());
                */
                selectedPosition = gender.getSelectedItemPosition();
                editorPreferences.putInt("gender", selectedPosition);
                selectedPosition = university.getSelectedItemPosition();
                editorPreferences.putInt("university", selectedPosition);
                selectedPosition = searchSexual_preference.getSelectedItemPosition();
                editorPreferences.putInt("searchSexual_preference", selectedPosition);
                // Speichern
                editorPreferences.apply();

            }
        });







        // Navigations Drawer
        drawerList = (ListView) findViewById(R.id.main_lv_menu);
        addDrawerItems();

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    // MyProfilActivity
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



    private void addDrawerItems(){
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, (getResources().getStringArray(R.array.drawer_list_menu_array)));
        drawerList.setAdapter(adapter);
    }
    private void myProfil(){
        Intent intent = new Intent(this, MyProfilActivity.class);
        startActivity(intent);
    }
    private void searchProfil(){
        Intent intent = new Intent(this, SearchProfilActivity.class);
        startActivity(intent);
    }
    private void findYourLove(){
        Intent intent = new Intent(this, FindYourLoveActivity.class);
        startActivity(intent);
    }

    /** Created by daniel on 15.03.16.
     *
     *  Positionen erkennen und berechnung von Wischereignissen.
     *  Dies Funktioniert nur in den Richtungen die kein Scrollingview besitzen.
     *  Hier nach links bzw. rechts
     *
     *  Pixelangaben müssen evtl noch angepasst werden
     *
     *  Beim wischen nach links wird Activity siehe Code (-> XYZ.class) aufgerufen!
     */

    private int touchX;
    private int touchY;
    public boolean onTouch(View v, MotionEvent event){
        int aktion = event.getAction();

        // 59 Pixel == 0,50cm ; 118 Pixel == 1,00cm
        int pixel = 177;

        if(aktion == MotionEvent.ACTION_DOWN){
            touchX = (int) event.getX();
            touchY = (int) event.getY();
        }
        if(aktion == MotionEvent.ACTION_UP) {
            int tx = (int) event.getX();
            int ty = (int) event.getY();

            if((touchX - tx) > pixel){
                Intent intent = new Intent(this, MyProfilControlActivity.class);
                startActivity(intent);

            } else if((touchX - tx) <= - pixel){

            }
        }
        return true;
    }
}
