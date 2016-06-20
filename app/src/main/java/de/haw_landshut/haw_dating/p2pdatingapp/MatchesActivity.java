/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package de.haw_landshut.haw_dating.p2pdatingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import de.haw_landshut.haw_dating.p2pdatingapp.data.WifiMessage;
import de.haw_landshut.haw_dating.p2pdatingapp.dataBase.DataBaseApplication;
import de.haw_landshut.haw_dating.p2pdatingapp.dataBase.MessagesHelper;
import de.haw_landshut.haw_dating.p2pdatingapp.match.Match;
import de.haw_landshut.haw_dating.p2pdatingapp.match.MatchAdapter;

/**
 * Created by Altrichter Daniel on 10.05.16.
 */
public class MatchesActivity extends AbstractP2pDatingActivity implements View.OnTouchListener {

    private static Context context;
    private List<Match> matchList = new ArrayList<>();
    private MatchAdapter matchAdapter;
    private ListView matchListView;
    private final MessagesHelper db = DataBaseApplication.getInstance().getDataBase();

    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matches_main);

        context = this.getApplicationContext();
        matchList.addAll(db.getMatches(context));


        matchAdapter = new MatchAdapter(matchList);
        matchListView = (ListView) findViewById(R.id.matchesActivityListView);
        matchListView.setAdapter(matchAdapter);


        LinearLayout bildschirm = (LinearLayout) findViewById(R.id.mathesLinearLayout);
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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            } else if ((touchX - tx) <= -pixel) {

            }
        }
        return true;
    }
}

