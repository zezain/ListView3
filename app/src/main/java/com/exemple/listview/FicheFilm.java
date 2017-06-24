package com.exemple.listview;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class FicheFilm extends AppCompatActivity {


    String refFilm;
    private SQLiteDatabase newDB;
    String Annee;
    String Titre;
    String NumFilm;
    String Resume;
    String Image_path;
    String Duree;
    String Note;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_film);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        refFilm = getIntent().getStringExtra("REFERENCE");
        openAndQueryDatabase();
        rempliText();

    }

    public void boutonFilm(View v){
        Log.d("FILM A OUVRIR", refFilm );
    }

    private void openAndQueryDatabase() {
        try {
            DBHelper dbHelper = new DBHelper(this.getApplicationContext());
            newDB = dbHelper.getWritableDatabase();

            Log.d("OOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOO");

            Cursor c = newDB.rawQuery("SELECT Titre, Annee, Resume, Image, Duree, Note FROM films WHERE NumFilm = " + refFilm, null);

            BarreDeNavigation.Reference.clear();

            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        Titre = c.getString(c.getColumnIndex("Titre"));
                        Annee = c.getString(c.getColumnIndex("Annee"));
                        Resume = c.getString(c.getColumnIndex("Resume"));
                        Image_path = c.getString(c.getColumnIndex("Image"));
                        Duree = c.getString(c.getColumnIndex("Duree"));
                        Note = c.getString(c.getColumnIndex("Note"));

                    } while (c.moveToNext());
                }

                c.close();
            }


        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }
    }

    private void rempliText(){
        TextView titre_film = (TextView)findViewById(R.id.titre_film);
        titre_film.setText(Titre);
       // TextView annee_film = (TextView)findViewById(R.id.annee_film);
        //titre_film.setText(Annee);



    }
}



