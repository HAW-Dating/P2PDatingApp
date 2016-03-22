package haw_dating.haw_landshut.de.p2pdatingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by daniel on 28.11.15.
 *
 * Revision by Altrichter Daniel on 15.03.16.
 * Implements OnTouchListener
 * wird gebraucht für die Wischfunktionen.
 */
public class MyProfilActivity extends Activity implements View.OnTouchListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profil_main);

        /* Fuer Spinner Geschlecht */
        Spinner gender = (Spinner) findViewById(R.id.gender);
        ArrayAdapter<String> adapterGender =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sexual_spinner));
        gender.setAdapter(adapterGender);

        /* Fuer Spinner Universität */
        Spinner university = (Spinner) findViewById(R.id.university);
        ArrayAdapter<String> adapterUniversity =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.university_spinner));
        university.setAdapter(adapterUniversity);

        /* Fuer Spinner Suche-Geschlecht */
        Spinner searchSexual_preference = (Spinner) findViewById(R.id.sexual_preference);
        ArrayAdapter<String> adapterSearchGender =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sexual_spinner));
        searchSexual_preference.setAdapter(adapterSearchGender);

        LinearLayout bildschirm = (LinearLayout) findViewById(R.id.my_profile_activity_linear_layout);
        bildschirm.setOnTouchListener(this);
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

            // Links, Rechts, Oben, Unten
            if((touchX - tx) > pixel){
                Toast toast = Toast.makeText(v.getContext(),"Es wurde nach LINKS gewischt! \nMyProfil -> MyProfilControl", Toast.LENGTH_SHORT );
                toast.show();

                Intent intent = new Intent(this, MyProfilControlActivity.class);
                startActivity(intent);

            } else if((touchX - tx) <= - pixel){
                Toast toast = Toast.makeText(v.getContext(),"Es wurde nach RECHTS gewischt!", Toast.LENGTH_SHORT );
                toast.show();
            }
            /*
            else if((touchY - ty) > pixel){
                Toast toast = Toast.makeText(v.getContext(),"Es wurde nach OBEN gewischt!", Toast.LENGTH_SHORT );
                toast.show();
            } else if((touchY - ty) <= - pixel){
                Toast toast = Toast.makeText(v.getContext(),"Es wurde nach UNTEN gewischt!", Toast.LENGTH_SHORT );
                toast.show();
            }
            */
        }
        return true;
    }
}
