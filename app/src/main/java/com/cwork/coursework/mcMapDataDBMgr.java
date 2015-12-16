package com.cwork.coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class mcMapDataDBMgr extends SQLiteOpenHelper {

    private static final int DB_VER = 1;
    private static final String DB_PATH = "/data/data/com.cwork.coursework/databases/";
    private static final String DB_NAME = "mapEKFamous5.s3db";
    private static final String TBL_MAPEKFAME = "mapEKFame5";

    public static final String COL_ENTRYID = "entryID";
    public static final String COL_SURNAME = "Surname";
    public static final String COL_FIRSTNAME = "FirstName";
    public static final String COL_STARSIGN = "StarSign";
    public static final String COL_OCCUPATION = "Occupation";
    public static final String COL_LATITUDE = "Latitude";
    public static final String COL_LONGITUDE = "Longitude";

    private final Context appContext;

    public mcMapDataDBMgr(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MAPEKFAME_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TBL_MAPEKFAME + "("
                + COL_ENTRYID + " INTEGER PRIMARY KEY," + COL_SURNAME
                + " TEXT," + COL_FIRSTNAME + " TEXT," + COL_STARSIGN + " TEXT,"
                + COL_OCCUPATION + " TEXT" + COL_LATITUDE + " FLOAT" + COL_LONGITUDE + " FLOAT" +")";
        db.execSQL(CREATE_MAPEKFAME_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TBL_MAPEKFAME);
            onCreate(db);
        }
    }

    // ================================================================================
    // Creates a empty database on the system and rewrites it with your own database.
    // ================================================================================
    public void dbCreate() throws IOException {

        boolean dbExist = dbCheck();

        if(!dbExist){
            //By calling this method an empty database will be created into the default system path
            //of your application so we can overwrite that database with our database.
            this.getReadableDatabase();

            try {
                copyDBFromAssets();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    // ============================================================================================
    // Check if the database already exist to avoid re-copying the file each time you open the application.
    // @return true if it exists, false if it doesn't
    // ============================================================================================
    private boolean dbCheck(){

        SQLiteDatabase db = null;

        try{
            String dbPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
            db.setLocale(Locale.getDefault());
            db.setVersion(1);

        }catch(SQLiteException e){
            Log.e("SQLHelper","Database not Found!");
        }

        if(db != null){
            db.close();
        }

        return db != null ? true : false;
    }

    // ============================================================================================
    // Copies your database from your local assets-folder to the just created empty database in the
    // system folder, from where it can be accessed and handled.
    // This is done by transfering bytestream.
    // ============================================================================================
    private void copyDBFromAssets() throws IOException{

        InputStream dbInput = null;
        OutputStream dbOutput = null;
        String dbFileName = DB_PATH + DB_NAME;

        try {
            dbInput = appContext.getAssets().open(DB_NAME);
            dbOutput = new FileOutputStream(dbFileName);
            //transfer bytes from the dbInput to the dbOutput
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dbInput.read(buffer)) > 0) {
                dbOutput.write(buffer, 0, length);
            }

            Log.e("Database copy","Database copied successfuly");

            //Close the streams
            dbOutput.flush();
            dbOutput.close();
            dbInput.close();
        } catch (IOException e)
        {
            throw new Error("Problems copying DB!");
        }
    }


    public void addaMapEKFameEntry(mcMapData aMapEKFame) {

        ContentValues values = new ContentValues();
        values.put(COL_SURNAME, aMapEKFame.getSurname());
        values.put(COL_FIRSTNAME, aMapEKFame.getFirstname());
        values.put(COL_STARSIGN, aMapEKFame.getStarSign());
        values.put(COL_OCCUPATION, aMapEKFame.getOccupation());
        values.put(COL_LATITUDE, aMapEKFame.getLatitude());
        values.put(COL_LONGITUDE, aMapEKFame.getLongitude());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TBL_MAPEKFAME, null, values);
        db.close();
    }

    public mcMapData getMapEKFameEntry(String aMapEKFameEntry) {
        String query = "Select * FROM " + TBL_MAPEKFAME + " WHERE " + COL_SURNAME + " =  \"" + aMapEKFameEntry + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        mcMapData MapDataEntry = new mcMapData();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            MapDataEntry.setEntryID(Integer.parseInt(cursor.getString(0)));
            MapDataEntry.setSurname(cursor.getString(1));
            MapDataEntry.setFirstname(cursor.getString(2));
            MapDataEntry.setStarSign(cursor.getString(3));
            MapDataEntry.setOccupation(cursor.getString(4));
            MapDataEntry.setLatitude(Float.parseFloat(cursor.getString(5)));
            MapDataEntry.setLatitude(Float.parseFloat(cursor.getString(6)));
            cursor.close();
        } else {
            MapDataEntry = null;
        }
        db.close();
        return MapDataEntry;
    }

    public List<mcMapData> allMapData()
    {
        String query = "Select * FROM " + TBL_MAPEKFAME;
        List<mcMapData> mcMapDataList = new ArrayList<mcMapData>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast()==false) {
                mcMapData MapDataEntry = new mcMapData();
                MapDataEntry.setEntryID(Integer.parseInt(cursor.getString(0)));
                MapDataEntry.setSurname(cursor.getString(1));
                MapDataEntry.setFirstname(cursor.getString(2));
                MapDataEntry.setStarSign(cursor.getString(3));
                MapDataEntry.setOccupation(cursor.getString(4));
                MapDataEntry.setLatitude(Float.parseFloat(cursor.getString(5)));
                MapDataEntry.setLongitude(Float.parseFloat(cursor.getString(6)));
                mcMapDataList.add(MapDataEntry);
                cursor.moveToNext();
            }
        } else {
            mcMapDataList.add(null);
        }
        db.close();
        return mcMapDataList;
    }
}