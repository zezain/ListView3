package com.exemple.listview;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class FicheFilm extends AppCompatActivity {


    String refFilm;
    private SQLiteDatabase newDB;
    String Annee;
    String Titre;
    String Resume;
    String Image_path;
    String Duree;
    String Note;
    String Pays;
    String NomReal;
    String PrenomReal;

    
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
        new DownloadImageTask((ImageView) findViewById(R.id.imageView2)).execute(Image_path);


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

            Cursor cbis = newDB.rawQuery("SELECT pays.Nom as Pays FROM pays, films, produiten WHERE films.NumFilm= produiten.NumFilm AND produiten.NumPays=pays.NumPays AND films.NumFilm = " + refFilm, null);
            if (cbis != null) {
                if (cbis.moveToFirst()) {
                    do {

                        Pays = cbis.getString(cbis.getColumnIndex("Pays"));
                    } while (cbis.moveToNext());
                }
                cbis.close();
            }

            // Requête pour le réalisateur
            Cursor cter = newDB.rawQuery("SELECT realisateurs.Nom as Nom, realisateurs.Prenom as Prenom FROM films, , realise WHERE films.NumFilm= realise.NumFilm AND realisateurs.NumReal=realise.NumReal AND films.NumFilm = " + refFilm, null);
            if (cter!= null) {
                if (cter.moveToFirst()) {
                    do {

                       NomReal = cter.getString(cter.getColumnIndex("Nom"));
                       PrenomReal = cter.getString(cter.getColumnIndex("Prenom"));
                    } while (cter.moveToNext());
                }
                cter.close();
            }

            // Requête pour le réalisateur

        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }
    }

    private void rempliText(){

        TextView titre_film = (TextView)findViewById(R.id.titre_film);
        titre_film.setText(Titre);
        TextView annee_film = (TextView)findViewById(R.id.annee_film);
        annee_film.setText("Année: "+Annee);
        TextView note_film = (TextView)findViewById(R.id.note_film);
        note_film.setText("Note: "+Note);
        TextView resume_film = (TextView)findViewById(R.id.resume_film);
        resume_film.setText("Résumé: \n "+Resume);
        TextView duree_film = (TextView)findViewById(R.id.duree_film);
        duree_film.setText("Durée: "+Duree);
        TextView pays_film = (TextView)findViewById(R.id.pays_film);
        pays_film.setText("Pays: "+Pays);


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        // Voir commentaire sur https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}



