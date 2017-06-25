package com.exemple.listview;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class GridFragment extends Fragment {

    //Variables locales
    View myView;
    private ArrayList<String> results = new ArrayList<String>();
    private SQLiteDatabase newDB;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private ImageView logo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.grid_layout, container, false);
        return myView;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Requête SQL
        openAndQueryDatabase();

        //On ajoute lo logo
        logo = (ImageView)getActivity().findViewById(R.id.imageLogo) ;
        logo.setImageResource(R.drawable.img_logo_dark);

        // Gestion de grdView
        gridView = (GridView) getActivity().findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(getActivity().getApplicationContext(), R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);


                BarreDeNavigation.loadArray(BarreDeNavigation.myContext);
                Intent openNewIntent = new Intent(getActivity(), FicheFilm.class);
                openNewIntent.putExtra("REFERENCE",BarreDeNavigation.Reference.get(position));

                startActivity(openNewIntent);
            }
        });

    }


    private void openAndQueryDatabase() {
        try {
            DBHelper dbHelper = new DBHelper(getActivity().getApplicationContext());
            newDB = dbHelper.getWritableDatabase();

            Log.d("OOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOO");
            
            Cursor c = newDB.rawQuery("SELECT NumFilm, Image, Titre FROM films ORDER BY NumFilm", null);

            BarreDeNavigation.Reference.clear();

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        BarreDeNavigation.Reference.add(c.getString(c.getColumnIndex("NumFilm")));
                        String Titre = c.getString(c.getColumnIndex("Titre"));
                        results.add(Titre);
                    } while (c.moveToNext());
                }

                c.close();
            }

            BarreDeNavigation.saveArray();


        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        } //finally {
//        	if (newDB != null)
//        		newDB.execSQL("DELETE FROM " + tableName);
//        		newDB.close();
//        }

    }

    //getData - Deuxième façon de recuperer une image : a partir du stockage interne
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);

        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));



            imageItems.add(new ImageItem(bitmap, results.get(i)));
        }
        return imageItems;
    }


}