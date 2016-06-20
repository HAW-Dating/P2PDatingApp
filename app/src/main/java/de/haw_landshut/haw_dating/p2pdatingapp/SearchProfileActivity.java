package de.haw_landshut.haw_dating.p2pdatingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.haw_landshut.haw_dating.p2pdatingapp.P2p.FindYourLoveMessageListener;
import de.haw_landshut.haw_dating.p2pdatingapp.P2p.P2pInterface;
import de.haw_landshut.haw_dating.p2pdatingapp.data.StoredProfile;
import de.haw_landshut.haw_dating.p2pdatingapp.data.WifiMessage;
import de.haw_landshut.haw_dating.p2pdatingapp.dataBase.DataBaseApplication;
import de.haw_landshut.haw_dating.p2pdatingapp.dataBase.MessagesHelper;
import de.haw_landshut.haw_dating.sealedbottle.algorithm.Bottle;
import de.haw_landshut.haw_dating.sealedbottle.api.BottleCryptoConstants;
import de.haw_landshut.haw_dating.sealedbottle.api.BottleOpener;
import de.haw_landshut.haw_dating.sealedbottle.api.MessageInABottle;

/**
 * Created by daniel on 08.12.15.
 * <p>
 * Revision by Altrichter Daniel on 15.03.16.
 * <p>
 * Implements OnTouchListener
 * wird gebraucht für die Wischfunktionen.
 * <p>
 * Revision by Altrichter Daniel on 4.04.16.
 * einfügen eines Navigation Drawers.
 */
public class SearchProfileActivity extends AbstractProfileActivity implements View.OnTouchListener, View.OnClickListener, FindYourLoveMessageListener {

    public static final Integer[] necessaryFields = new Integer[]{
            R.id.search_gender,
            R.id.search_university,
            R.id.search_sexual_preference,
            R.id.search_age};

    public static final Integer[][] optionalFields = new Integer[][]{{
            R.id.search_hometown,
            R.id.search_interests_1,
            R.id.search_interests_2,
            R.id.search_interests_3,
            R.id.search_studies}
    };

    public static final Integer[] profileFields = new Integer[]{
            R.id.search_age,
            R.id.search_gender,
            R.id.search_hometown,
            R.id.search_interests_1,
            R.id.search_interests_2,
            R.id.search_interests_3,
            R.id.search_studies,
            R.id.search_postal_code,
            R.id.search_sexual_preference,
            R.id.search_university};

    private static final String TAG = SearchProfileActivity.class.getSimpleName();

    final private Map<Integer, String> profileData = new HashMap<>();

    private P2pInterface p2pInterface;
    private Button searchButton;
    private MessagesHelper db;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_profil_main);

        db = DataBaseApplication.getInstance().getDataBase();

        p2pInterface = new P2pInterface(this, this);
        p2pInterface.initiate();

        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);

        Button debug = (Button) findViewById(R.id.debug_button);
        debug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterChatActivity("hallo");
            }
        });



        /* Fuer Spinner Geschlecht */
        Spinner gender = (Spinner) findViewById(R.id.search_gender);
        ArrayAdapter<String> adapterGender =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources
                        ().getStringArray(R.array.sexual_spinner));
        gender.setAdapter(adapterGender);

        /* Fuer Spinner Universität */
        Spinner university = (Spinner) findViewById(R.id.search_university);
        ArrayAdapter<String> adapterUniversity =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources
                        ().getStringArray(R.array.university_spinner));
        university.setAdapter(adapterUniversity);

        /* Fuer Spinner Suche-Geschlecht */
        Spinner searchSexual_preference = (Spinner) findViewById(R.id.search_sexual_preference);
        ArrayAdapter<String> adapterSearchGender =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources
                        ().getStringArray(R.array.sexual_spinner));
        searchSexual_preference.setAdapter(adapterSearchGender);

        LinearLayout bildschirm = (LinearLayout) findViewById(R.id
                .search_profil_activity_linear_layout);
        bildschirm.setOnTouchListener(this);


        // Navigations Drawer
    }

    @Override
    protected void onPause() {
        super.onPause();
        p2pInterface.onPause();
    }

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

            // Links, Rechts, Oben, Unten
            if ((touchX - tx) > pixel) {
                Intent intent = new Intent(this, FindYourLoveActivity.class);
                startActivity(intent);

            } else if ((touchX - tx) <= -pixel) {

            }
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        p2pInterface.onResume();
        final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        final String serializedProfile = preferences.getString(getStringDataById(
                R.string.shared_preference_search_profile), STRING_DEF_VALUE);
        Log.d("stored search profile", serializedProfile);
        if (serializedProfile != STRING_DEF_VALUE) {
            final StoredProfile storedProfile = StoredProfile.deSerialize(serializedProfile);
            for (final int id : storedProfile.getProfileFields()) {
                restoreInput(id, storedProfile.getProfileData().get(id));
            }

        }
    }

    @Override
    public void onClick(View v) {

        for (Integer attributeId : profileFields) {
            profileData.put(attributeId, getStringDataById(attributeId));

        }
        final StoredProfile myProfile = new StoredProfile(profileData, profileFields, necessaryFields, optionalFields);
        // SharedPreferences Datei öffnen
        final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        // Editorklasse initialisieren
        final SharedPreferences.Editor preferenceEditor = preferences.edit();
        // Text mit Schlüsselattribut holen und in Editorklasse schreiben
        preferenceEditor.putString(getStringDataById(R.string.shared_preference_search_profile),
                myProfile.serialize());
        preferenceEditor.apply();

        final Bottle bottle = new Bottle(myProfile);
        bottle.fill().cork().seal();
        final MessageInABottle message = new MessageInABottle(bottle, "loveline", new String[]{"hint"}, 1);
        final String send = MessageInABottle.serialize(message);
        final UUID secret = UUID.randomUUID();
        //final BottleOpener opener = new BottleOpener(message,bottle);
        final WifiMessage wifiMessage = WifiMessage.createWifiMessage(send, bottle.getKeyAsAESSecretKey(), secret.toString());

        final String finalSendString = wifiMessage.serialize();
        db.storeMessage(wifiMessage.getUuid().toString(), finalSendString, wifiMessage.getDate(), true, true, secret.toString());

        //p2pInterface.sendProfile(finalSendString);

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

    public void enterChatActivity(final String chatRoom) {

        final Intent intent = new Intent(this, ChatActivity.class);

        final ArrayList<String> data = new ArrayList<String>();
        final String ip = getString(R.string.server_name);
        final String roomID = chatRoom;
        data.add(ip);
        data.add(roomID);

        final Bundle bundle = new Bundle();
        bundle.putStringArrayList("data", data);
        intent.putExtra(FindYourLoveActivity.CHAT_MESSAGE, bundle);
        startActivity(intent);

    }
}
