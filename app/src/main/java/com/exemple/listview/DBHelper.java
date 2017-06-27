package com.exemple.listview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;

import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper{
	
	public SQLiteDatabase DB;
	public String DBPath;
	public static String DBName = "filmotec.db";
	public static final int version = '1';
	public static Context currentContext;


	public DBHelper(Context context) {
		super(context, DBName, null, version);
		currentContext = context;


		DBPath = "/data/data/" + context.getPackageName() + "/databases/";

		try {
			String myPath = DBPath + DBName; // also check the extension of you db file
			File dbfile = new File(myPath);
			if (dbfile.exists()) {
				Toast.makeText(context, "database exists", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "cant find database", Toast.LENGTH_LONG).show();
			}
		}catch(SQLiteException e){
			System.out.println("Database doesn't exist");
		}
		createDatabase();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	private void createDatabase() {
		boolean dbExists = checkDbExists();


		
		if (dbExists) {
			// on ne fait rien
		} else {
			//DB = currentContext.openOrCreateDatabase(DBName, 0, null);

			this.getReadableDatabase();


			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}

		}
	}

	private boolean checkDbExists() {
		SQLiteDatabase checkDB = null;

		try {
			String myPath = DBPath + DBName;
			Log.d("OOOOO",myPath);
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// la bdd n'existe pas encore

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copie la bdd depuis notre fichier local asset vers la bdd vide qui a été créée
	 * dans le fichier système, de là on peut y accéder.
	 * Cela est effectué par flux d'octets.
	 * */
	private void copyDataBase() throws IOException {

		//ouvre notre bdd comme flux d'entrée

		try {
			InputStream myInput = currentContext.getAssets().open(DBName);
		} catch (IOException e)
		{
			Log.e("PAS DE BDD","PAS DE BDD");
		}
		InputStream myInput = currentContext.getAssets().open(DBName);
		// Chemin de la bdd vide qui a été créée
		String outFileName = DBPath + DBName;

		Log.d("NOM DE SORTIE", outFileName);

		//Ouvre la bdd vide comme flux de sortie
		OutputStream myOutput = new FileOutputStream(outFileName);

		//transfert d'octets du fichier d'entrée au fichier de sortie
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}

		//Fermeture des flux
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}
}
