package de.haw_landshut.haw_dating.p2pdatingapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by s-gheldd on 7/30/15.
 */
public class ChatNameDialog extends DialogFragment implements View.OnClickListener {

    Communicator communicator;
    Button button;
    EditText editText;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        communicator = (Communicator) activity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Enter Name:");
        View view = inflater.inflate(R.layout.chat_name_dialog, null);
        button = (Button) view.findViewById(R.id.button_change_name);
        editText = (EditText) view.findViewById(R.id.editText_change_name);
        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (editText.getText().toString().length() > 2) {
            communicator.onDialogMessage(editText.getText().toString());
            this.dismiss();
        } else {
            Toast.makeText(getActivity(), getString(R.string.USER_NAME_TO_SHORT), Toast.LENGTH_LONG).show();
        }

    }

    interface Communicator {
        void onDialogMessage(String message);
    }
}
