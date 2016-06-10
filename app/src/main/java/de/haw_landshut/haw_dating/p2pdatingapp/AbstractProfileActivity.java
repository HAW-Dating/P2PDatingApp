package de.haw_landshut.haw_dating.p2pdatingapp;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 6/10/16 by s-gheldd
 */
public abstract class AbstractProfileActivity extends Activity{
    public static final String STRING_DEF_VALUE = "";


    protected String getStringDataById(final int id) {

        final View view = findViewById(id);
        if (view instanceof EditText) {
            return ((EditText) view).getText().toString();
        } else if (view instanceof Spinner) {
            return ((Spinner) view).getSelectedItem().toString();
        }
        return STRING_DEF_VALUE;
    }

    protected void restoreInput(final int id, final String value) {
        final View view = findViewById(id);
        if (view instanceof EditText) {
            ((EditText) view).setText(value);
        } else if (view instanceof Spinner) {
            final Spinner spinner = (Spinner) view;
            final ArrayAdapter<String> adapter = (ArrayAdapter) spinner.getAdapter();
            spinner.setSelection(adapter.getPosition(value));
        }
    }
}
