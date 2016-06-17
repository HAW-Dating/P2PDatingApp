package de.haw_landshut.haw_dating.p2pdatingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.haw_landshut.haw_dating.p2pdatingapp.data.StorageProfile;
import de.haw_landshut.haw_dating.p2pdatingapp.data.WifiMessage;
import de.haw_landshut.haw_dating.sealedbottle.algorithm.Bottle;
import de.haw_landshut.haw_dating.sealedbottle.algorithm.Corkscrew;
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
public class SearchProfilActivity extends AbstractProfileActivity implements View
        .OnTouchListener, View
        .OnClickListener, FindYourLoveMessageListener {

    public static final Integer[] profileFields = new Integer[]{R.id.search_age, R.id
            .search_gender, R.id.search_hometown, R.id.search_interests, R.id.search_studie, R.id
            .search_postal_code, R.id.search_sexual_preference, R.id.search_university};
    public static final Integer[][] optionalFields = new Integer[][]{{R.id.search_hometown, R.id
            .search_interests, R.id.search_studie}};
    public static final Integer[] necessaryFields = new Integer[]{R.id.search_gender, R.id
            .search_university, R.id.search_sexual_preference, R.id.search_age};
    private static final String TAG = SearchProfilActivity.class.getName();

    final private Map<Integer, String> profileData = new HashMap<>();

    private ListView drawerList;
    private ArrayAdapter<String> adapter;
    private P2pInterface p2pInterface;
    private Button searchButton;
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


        p2pInterface = new P2pInterface(this, this);
        p2pInterface.initiate();

        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);



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
        drawerList = (ListView) findViewById(R.id.main_lv_menu);
        addDrawerItems();

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
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

    @Override
    protected void onPause() {
        super.onPause();
        p2pInterface.onPause();
    }

    private void addDrawerItems() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                (getResources().getStringArray(R.array.drawer_list_menu_array)));
        drawerList.setAdapter(adapter);
    }

    private void myProfil() {
        Intent intent = new Intent(this, MyProfilActivity.class);
        startActivity(intent);
    }

    private void searchProfil() {
        Intent intent = new Intent(this, SearchProfilActivity.class);
        startActivity(intent);
    }

    private void findYourLove() {
        Intent intent = new Intent(this, FindYourLoveActivity.class);
        startActivity(intent);
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
            final StorageProfile storedProfile = StorageProfile.deSerialize(serializedProfile);
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
        StorageProfile myProfile = new StorageProfile(profileData, profileFields,
                necessaryFields, optionalFields);
        // SharedPreferences Datei öffnen
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        // Editorklasse initialisieren
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        // Text mit Schlüsselattribut holen und in Editorklasse schreiben
        preferenceEditor.putString(getStringDataById(R.string.shared_preference_search_profile),
                myProfile.serialize());
        preferenceEditor.commit();

        final Bottle bottle = new Bottle(myProfile);
        bottle.fill().cork().seal();
        final MessageInABottle message = new MessageInABottle(bottle, "loveline", new String[]{"hint"}, 1);
        final String send = MessageInABottle.serialize(message);
        //final BottleOpener opener = new BottleOpener(message,bottle);
        final WifiMessage wifiMessage = WifiMessage.createWifiMessage(send, bottle.getKeyasAESSecretKey(), "hallo");

        final String finalSendString = wifiMessage.serialize();
        p2pInterface.sendProfile(finalSendString);

    }

    @Override
    public void onLoveMessageReceive(String message) {
        Log.d(TAG, "message received: " + message);
        if (!message.equals(P2pInterface.NOT_VALID_YET)) {
            final WifiMessage wifiMessage = WifiMessage.deserialize(message);

            if (wifiMessage != null) {
                try {
                    final String chatRoom = tryDecode(wifiMessage);
                    Log.d(TAG, "onLoveMessageReceive: " + chatRoom);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                Log.d(TAG, "onLoveMessageReceive: Could not deserialize" + message);
            }
        }
    }

    private String tryDecode(final WifiMessage wifiMessage) throws Exception {
        final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        final String profileString = preferences.getString(getStringDataById(R.string.shared_preference_profile), STRING_DEF_VALUE);
        if (!STRING_DEF_VALUE.equals(profileString)) {
            final StorageProfile storageProfile = StorageProfile.deSerialize(profileString);
            final Bottle bottle = new Bottle(storageProfile);
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
}
