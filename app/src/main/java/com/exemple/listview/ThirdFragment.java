package com.exemple.listview;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.ArrayAdapter;

/**
 * Created by Elise on 01/05/2017.
 */

public class ThirdFragment extends ListFragment{

    View myView;
    private ArrayList<String> results = new ArrayList<String>();
    private SQLiteDatabase newDB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.first_layout, container, false);
        return myView;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        openAndQueryDatabase();
        setListAdapter(new ArrayAdapter<String>(getActivity(),R.layout.textview, results));
        getListView().setTextFilterEnabled(true);
    }


    private void openAndQueryDatabase() {
        try {
            DBHelper dbHelper = new DBHelper(getActivity().getApplicationContext());
            newDB = dbHelper.getWritableDatabase();

            Log.d("OOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOO");

            Cursor c = newDB.rawQuery("SELECT films.NumFilm as NumFilm, films.Titre as Titre, acteurs.Nom as Nom, acteurs.Prenom as Prenom FROM films, acteurs, joue WHERE films.NumFilm=joue.NumFilm AND acteurs.NumActeur=joue.NumActeur ORDER BY acteurs.Prenom" , null);

            BarreDeNavigation.Reference.clear();

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        BarreDeNavigation.Reference.add(c.getString(c.getColumnIndex("NumFilm")));
                        String Titre = c.getString(c.getColumnIndex("Titre"));
                        String Nom = c.getString(c.getColumnIndex("Nom"));
                        String Prenom = c.getString(c.getColumnIndex("Prenom"));
                        results.add(Prenom +" " + Nom +" - "+ Titre +"");
                    } while (c.moveToNext());
                }

                c.close();
            }


        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        } //finally {
//        	if (newDB != null)
//        		newDB.execSQL("DELETE FROM " + tableName);
//        		newDB.close();
//        }

    }

}