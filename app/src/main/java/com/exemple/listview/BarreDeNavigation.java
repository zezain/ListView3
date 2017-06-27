package com.exemple.listview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

public class BarreDeNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static ArrayList<String> Reference = new ArrayList<String>();
    static Context myContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barre_de_navigation);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myContext = this.getApplicationContext(); //Reference statique du contexte

        // Ici on appelle le GridFragment/page d'accueil
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new GridFragment()).commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.barre_de_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_camera) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new FirstFragment()).commit();

        } else if (id == R.id.nav_gallery) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new SecondFragment()).commit();

        } else if (id == R.id.nav_slideshow) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new ThirdFragment()).commit();

        } else if (id == R.id.nav_manage) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new FourthFragment()).commit();

        } else if (id == R.id.nav_pays) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new FifthFragment()).commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Fonction appellée quand on choisit un film dans la liste
    public void textClicked(View v) {

        ListView lv = (ListView) findViewById(android.R.id.list);
        int position = lv.getPositionForView(v);

        //Debug
        Log.d("FRAGMENT ------", String.valueOf(position));
        //Animation du choix de film
        Animation animation = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.animation);
        v.startAnimation(animation);

        //Lecture de la dernière liste sauvegardée
        loadArray(myContext);

        //Debug
        Log.d("ARRAY REFERENCE ------",Reference.toString());

        //On appelle l'activité FicheFilm
        Intent openNewIntent = new Intent(this, FicheFilm.class);
        //On transmet à la prochaine activité la référence du film que l'utilisateur a sélectionné
        openNewIntent.putExtra("REFERENCE",Reference.get(position));
        startActivity(openNewIntent);

    }


    public static boolean saveArray()
    //Comment rendre persistante une liste?
    // Voir commentaire avec 78 votes: https://stackoverflow.com/questions/7057845/save-arraylist-to-sharedpreferences
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(myContext);
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putInt("Status_size", Reference.size());

        for(int i=0;i<Reference.size();i++)
        {
            mEdit1.remove("Status_" + i);
            mEdit1.putString("Status_" + i, Reference.get(i));
        }

        return mEdit1.commit();
    }

    public static void loadArray(Context mContext) //Complement pour récupérer la liste
    {
        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(mContext);
        Reference.clear();
        int size = mSharedPreference1.getInt("Status_size", 0);

        for(int i=0;i<size;i++)
        {
            Reference.add(mSharedPreference1.getString("Status_" + i, null));
        }

    }

}

