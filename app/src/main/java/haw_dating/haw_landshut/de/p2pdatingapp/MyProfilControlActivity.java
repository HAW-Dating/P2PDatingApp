package haw_dating.haw_landshut.de.p2pdatingapp;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by alisabuchner on 08.12.15.
 */
public class MyProfilControlActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// nicht vergessen sonst keine Funktion! :-) ->
        setContentView(R.layout.my_profil_control);
    }
}
