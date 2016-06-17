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

import java.util.ArrayList;

/**
 * Created by alisabuchner on 08.12.15.
 *
 * Revision by Altrichter Daniel on 15.03.16.
 *
 * Implements OnTouchListener
 * wird gebraucht für die Wischfunktionen.
 */
public class FindYourLoveActivity extends Activity implements View.OnTouchListener{
    public final static String CHAT_MESSAGE = "com.example.haw_mobile.beaconchat.CHAT_MESSAGE";

    private ListView drawerList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_your_love);

        LinearLayout bildschirm = (LinearLayout) findViewById(R.id.find_your_love_linear_layout);
        bildschirm.setOnTouchListener(this);

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

                Intent intent = new Intent(this, MatchingsActivity.class);
                startActivity(intent);

            } else if((touchX - tx) <= - pixel){

            }
        }
        return true;
    }

    public void enterChatActivity(String minorID) {

        Intent intent = new Intent(this, ChatActivity.class);

        ArrayList<String> data = new ArrayList<String>();
        String ip = getString(R.string.server_name);
        String roomID = minorID;
        data.add(ip);
        data.add(roomID);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("data", data);
        intent.putExtra(FindYourLoveActivity.CHAT_MESSAGE, bundle);
        startActivity(intent);

    }

}
