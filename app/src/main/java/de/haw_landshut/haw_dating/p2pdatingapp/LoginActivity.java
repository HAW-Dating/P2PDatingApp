package de.haw_landshut.haw_dating.p2pdatingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by daniel on 27.11.15.
 *
 * Revision by Altrichter Daniel on 15.03.16.
 *  Implements OnTouchListener
 * wird gebraucht für die Wischfunktionen.
 *
 * Revision by Altrichter Daniel on 4.04.16.
 * einfügen eines Navigation Drawers.
 *
 */
public class LoginActivity extends Activity implements View.OnTouchListener{

    private ListView drawerList;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        LinearLayout bildschirm = (LinearLayout) findViewById(R.id.login_activity_linear_layout);
        bildschirm.setOnTouchListener(this);


        // Navigations Drawer
        drawerList = (ListView) findViewById(R.id.main_lv_menu);
        addDrawerItems();

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    // MyProfilActivity
                    case 0:  Toast.makeText(LoginActivity.this, "Eigenes Profil", Toast.LENGTH_SHORT).show();
                        myProfil();
                        break;
                    //
                    case 1:  Toast.makeText(LoginActivity.this, "Nachrichten", Toast.LENGTH_SHORT).show();
                        break;
                    // wer ein treffer ist.
                    case 2:  Toast.makeText(LoginActivity.this, "Matchings", Toast.LENGTH_SHORT).show();
                        findYourLove();
                        break;
                    // SearchProfilActivity
                    case 3:  Toast.makeText(LoginActivity.this, "Suchprofil", Toast.LENGTH_SHORT).show();
                        searchProfil();
                        break;
                    // rausschmeißen da es das selbe wie Eigenes Profil ist.
                    case 4:  Toast.makeText(LoginActivity.this, "Infos bearbeiten", Toast.LENGTH_SHORT).show();
                        break;

                    // Wenn noch Zeit dann Einstellungen hinzufügen!!!
                    default:  Toast.makeText(LoginActivity.this, "So a schmarn", Toast.LENGTH_SHORT).show();
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

            // Links, Rechts, Oben, Unten
            if((touchX - tx) > pixel){
                Toast toast = Toast.makeText(v.getContext(),"Es wurde nach LINKS gewischt! \nLogin -> MyProfil", Toast.LENGTH_SHORT );
                toast.show();

                Intent intent = new Intent(this, MyProfilActivity.class);
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
