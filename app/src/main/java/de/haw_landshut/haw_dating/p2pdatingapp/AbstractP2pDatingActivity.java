package de.haw_landshut.haw_dating.p2pdatingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Georg on 19.06.2016.
 */
public abstract class AbstractP2pDatingActivity extends AppCompatActivity {
    private ListView drawerList;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


        drawerList = (ListView) findViewById(R.id.main_lv_menu);
        addDrawerItems();

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    // MyProfileActivity
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


    private void addDrawerItems() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, (getResources().getStringArray(R.array.drawer_list_menu_array)));
        drawerList.setAdapter(adapter);
    }

    private void myProfil() {
        Intent intent = new Intent(this, MyProfileActivity.class);
        startActivity(intent);
    }

    private void searchProfil() {
        Intent intent = new Intent(this, SearchProfileActivity.class);
        startActivity(intent);
    }

    private void findYourLove() {
        Intent intent = new Intent(this, FindYourLoveActivity.class);
        startActivity(intent);
    }
}
