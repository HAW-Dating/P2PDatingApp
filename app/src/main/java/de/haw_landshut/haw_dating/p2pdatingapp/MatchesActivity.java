/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package de.haw_landshut.haw_dating.p2pdatingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.haw_landshut.haw_dating.p2pdatingapp.P2p.FindYourLoveMessageListener;
import de.haw_landshut.haw_dating.p2pdatingapp.P2p.P2pInterface;
import de.haw_landshut.haw_dating.p2pdatingapp.data.StoredProfile;
import de.haw_landshut.haw_dating.p2pdatingapp.data.WifiMessage;
import de.haw_landshut.haw_dating.p2pdatingapp.dataBase.DataBaseApplication;
import de.haw_landshut.haw_dating.p2pdatingapp.dataBase.MessagesHelper;
import de.haw_landshut.haw_dating.p2pdatingapp.match.Match;
import de.haw_landshut.haw_dating.p2pdatingapp.match.MatchAdapter;
import de.haw_landshut.haw_dating.sealedbottle.algorithm.Bottle;
import de.haw_landshut.haw_dating.sealedbottle.api.BottleCryptoConstants;
import de.haw_landshut.haw_dating.sealedbottle.api.BottleOpener;
import de.haw_landshut.haw_dating.sealedbottle.api.MessageInABottle;

/**
 * Created by Altrichter Daniel on 10.05.16.
 */
public class MatchesActivity extends AbstractProfileActivity implements View.OnTouchListener, FindYourLoveMessageListener {

    private static final String TAG = MatchesActivity.class.getName();
    private static Context context;
    private List<Match> matchList = new ArrayList<>();
    private MatchAdapter matchAdapter;
    private ListView matchListView;
    private final MessagesHelper db = DataBaseApplication.getInstance().getDataBase();
    private P2pInterface p2pInterface;

    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matches_main);

        p2pInterface = new P2pInterface(this, this);
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
     * <p>
     * Positionen erkennen und berechnung von Wischereignissen.
     * Dies Funktioniert nur in den Richtungen die kein Scrollingview besitzen.
     * Hier nach links bzw. rechts
     * <p>
     * Pixelangaben müssen evtl noch angepasst werden
     * <p>
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

    @Override
    public void onLoveMessageReceive(String message) {
        Log.d(TAG, "message received: " + message);
        if (!message.equals(P2pInterface.NOT_VALID_YET)) {
            final WifiMessage wifiMessage = WifiMessage.deserialize(message);

            if (wifiMessage != null) {
                try {
                    final String secret = tryDecode(wifiMessage);
                    Log.d(TAG, "onLoveMessageReceive: decoded " + secret);
                    if (secret != null) {
                        db.storeMessage(wifiMessage.getUuid().toString(), message, wifiMessage.getDate(), true, false, secret);
                        //enterChatActivity(secret);
                    } else {
                        db.storeMessage(wifiMessage.getUuid().toString(), message, wifiMessage.getDate(), false, false, null);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                Log.d(TAG, "onLoveMessageReceive: Could not deserialize" + message);
            }
        } else {
            Log.d(TAG, "onLoveMessageReceive: no Profile returned");
        }
    }

    private String tryDecode(final WifiMessage wifiMessage) throws Exception {
        final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        final String profileString = preferences.getString(getStringDataById(R.string.shared_preference_profile), STRING_DEF_VALUE);
        if (!STRING_DEF_VALUE.equals(profileString)) {
            final StoredProfile storedProfile = StoredProfile.deSerialize(profileString);
            final Bottle bottle = new Bottle(storedProfile);
            bottle.fill();
            bottle.cork();
            bottle.seal();

            final BottleOpener bottleOpener = new BottleOpener(MessageInABottle.deSerialize(wifiMessage.getSerializedMessageInABottle()), bottle);
            if (bottleOpener.isOpeningPossible()) {
                final SecretKeySpec secretKeySpec = bottleOpener.tryOpening();
                if (secretKeySpec != null) {
                    final Cipher cipher = Cipher.getInstance(BottleCryptoConstants.TRANSFORMATION);
                    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, BottleCryptoConstants.IV_PARAMETER_SPEC);
                    final String chatRoom = new String(cipher.doFinal(wifiMessage.getEncryptedMessage()), BottleCryptoConstants.CHARSET);
                    return chatRoom;
                }
                Log.d(TAG, "tryDecode: Could not decipher");
            }
        }
        return null;
    }


    @Override
    public void onPeersDiscovered(final P2pInterface p2pInterface) {
        // p2pInterface.sendProfile();
    }
}

