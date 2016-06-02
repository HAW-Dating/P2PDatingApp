package de.haw_landshut.haw_dating.p2pdatingapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by alisabuchner on 08.12.15.
 *
 * Revision by Altrichter Daniel on 15.03.16.
 * Implements OnTouchListener
 * wird gebraucht für die Wischfunktionen.
 *
 * Revision by Altrichter Daniel on 4.04.16.
 * einfügen eines Navigation Drawers.
 *
 */
public class MyProfilControlActivity extends Activity implements View.OnTouchListener{

    private ListView drawerList;
    private ArrayAdapter<String> adapter;

    TextView viewTextName, viewTextStudie, viewTextInterests, viewTextHometown, viewTextPostal_code;

    Spinner university,gender, searchSexual_preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profil_control);

        LinearLayout bildschirm = (LinearLayout) findViewById(R.id.my_profil_control_linear_layout);
        bildschirm.setOnTouchListener(this);



        // SharedPreferences Datei öffnen
        /**
         * Funktioniert noch nicht richtig!!!!
         */

        SharedPreferences preferences = getSharedPreferences("Profildata", 0);
        // Viewklasse initialisieren
        viewTextName = (TextView)findViewById(R.id.profil_control_name);
        // Schlüsselwerte aus der Datei lesen und in Textfelder schreiben
        viewTextName.setText(preferences.getString("name", "Huber Sepp"));
        viewTextStudie = (TextView)findViewById(R.id.profil_studie);
        viewTextStudie.setText(preferences.getString("studie", "Soziale Arbeit"));
        viewTextInterests = (TextView)findViewById(R.id.profil_interests);
        viewTextInterests.setText(preferences.getString("intrests", "Fliegen"));
        viewTextHometown = (TextView)findViewById(R.id.profil_hometown);
        viewTextHometown.setText(preferences.getString("hometown", "Hamburg"));
        viewTextPostal_code = (TextView)findViewById(R.id.profil_postal_code);
        viewTextPostal_code.setText(preferences.getString("postal_code", "22113"));

        // Spinner
        gender.setSelection(preferences.getInt("gender",0 ));
        university.setSelection(preferences.getInt("university", 0));
        searchSexual_preference.setSelection(preferences.getInt("searchSexual_preference",0));



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
                Intent intent = new Intent(this, SearchProfilActivity.class);
                startActivity(intent);

            } else if((touchX - tx) <= - pixel){

            }
        }
        return true;
    }
}
