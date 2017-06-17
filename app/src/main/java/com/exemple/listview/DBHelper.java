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
	//public static String tableName = "pays";
	

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
			// do nothing
		} else {
			//DB = currentContext.openOrCreateDatabase(DBName, 0, null);
//			DB.execSQL("CREATE TABLE IF NOT EXISTS " +
//        			tableName +
//        			" (LastName VARCHAR, FirstName VARCHAR," +
//        			" Country VARCHAR, Age INT(3));");
//
//        	DB.execSQL("INSERT INTO " +
//        			tableName +
//        			" Values ('M','Sing','India',25);");
//        	DB.execSQL("INSERT INTO " +
//        			tableName +
//        			" Values ('C','Raje','India',25);");
//        	DB.execSQL("INSERT INTO " +
//        			tableName +
//        			" Values ('D','Phonu','Argentina',20);");
//        	DB.execSQL("INSERT INTO " +
//        			tableName +
//        			" Values ('V','Veera','EU',25);");
//        	DB.execSQL("INSERT INTO " +
//        			tableName +
//        			" Values ('T','Shenoi','Bangla',25);");
//        	DB.execSQL("INSERT INTO " +
//        			tableName +
//        			" Values ('L','Lamha','Australia',20);");

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

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created empty database in the
	 * system folder, from where it can be accessed and handled.
	 * This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		//Open your local db as the input stream

		try {
			InputStream myInput = currentContext.getAssets().open(DBName);
		} catch (IOException e)
		{
			Log.e("PAS DE BDD","PAS DE BDD");
		}
		InputStream myInput = currentContext.getAssets().open(DBName);
		// Path to the just created empty db
		String outFileName = DBPath + DBName;

		Log.d("NOM DE SORTIE", outFileName);

		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}
}
