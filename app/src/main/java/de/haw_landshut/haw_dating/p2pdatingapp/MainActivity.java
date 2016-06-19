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

/**
 * Revision by Altrichter Daniel on 4.04.16.
 * einfügen eines Navigation Drawers.
 * <p/>
 * Revision by Altrichter Daniel on 15.03.16.
 * <p/>
 * Implements OnTouchListener
 * wird gebraucht für die Wischfunktionen.
 */
public class MainActivity extends AbstractP2pDatingActivity implements View.OnTouchListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //addDrawer();
        LinearLayout bildschirm = (LinearLayout) findViewById(R.id.activity_main_linear_layout);
        bildschirm.setOnTouchListener(this);

    }


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

    /*
    Hier ein Beispiel für einen Toast:
    Toast toast = Toast.makeText(v.getContext(),"Es wurde nach LINKS gewischt! \nSearchProfil -> FindYourLove", Toast.LENGTH_SHORT );
    toast.show();
     */

    private int touchX;
    private int touchY;

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
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);

            } else if ((touchX - tx) <= -pixel) {

            }
        }
        return true;
    }
}
