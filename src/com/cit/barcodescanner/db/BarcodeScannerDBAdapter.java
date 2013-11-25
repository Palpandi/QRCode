package com.cit.barcodescanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * 
 * @author Vishnuvardhanan.S
 *
 */

public class BarcodeScannerDBAdapter {
	
	private static final String DATABASE_NAME = "BarcodeDB";
	
	private static final String DATABASE_TABLE_BARCODE = "BarcodeDetails";
	public static final String KEY_BARCODE_ID                 = "_id"; 
	public static final String KEY_BARCODE_CAPTURE_DATE       = "_captureDate"; 
	public static final String KEY_BARCODE                    = "_barcode"; 
	private static final String TAG                           = "DBBarcodeDetails";
	
	private static final int DATABASE_VERSION = 1;
	 
	 private static final String DATABASE_CREATE_BARCODETABLE =
	        "create table BarcodeDetails (_id integer primary key autoincrement, _captureDate text not null,_barcode text not null);";

	 private final Context context; 
	    
	    private DatabaseHelper DBHelper;
	    private SQLiteDatabase db;

	    public BarcodeScannerDBAdapter(Context ctx) 
	    {
	        this.context = ctx;
	        DBHelper = new DatabaseHelper(context);
	    }
	        
	    private static class DatabaseHelper extends SQLiteOpenHelper 
	    {
	        DatabaseHelper(Context context) 
	        {
	            super(context, DATABASE_NAME, null, DATABASE_VERSION);
	        }

	        @Override
	        public void onCreate(SQLiteDatabase db) 
	        {
	            db.execSQL(DATABASE_CREATE_BARCODETABLE);

	        }

	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
	        int newVersion) 
	        {
	            Log.w(TAG, "Upgrading database from version " + oldVersion 
	                    + " to "
	                    + newVersion + ", which will destroy all old data");
	            db.execSQL("DROP TABLE IF EXISTS titles");
	            onCreate(db);
	        }
	    }    
	    
	    /**
	     * Opens the database
	     * @return
	     * @throws SQLException
	     */
	    public BarcodeScannerDBAdapter open() throws SQLException 
	    {
	        db = DBHelper.getWritableDatabase();
	        return this;
	    }

	    /**
	     * Closes the database  
	     */
	    public void close() 
	    {
	        DBHelper.close();
	    }
	    
	    /*
	     * This func( )going to insert all values for CaptureDate and Barcode
	     */
	    /**
	     * @param captureDate
	     * @return
	     */
	    public long insertBarcodeDetails(String captureDate,String barcode) 
	    {
	        ContentValues initialValues = new ContentValues();
	        initialValues.put(KEY_BARCODE_CAPTURE_DATE , captureDate);
	        initialValues.put(KEY_BARCODE , barcode);
	        return db.insert(DATABASE_TABLE_BARCODE, null, initialValues);
	    }
       /**
        * 
        * @return
        */
	    public Cursor getAllDetails() 
	    {
	    
	              return db.query(DATABASE_TABLE_BARCODE, new String[] {
	            	KEY_BARCODE_ID, //0
	        		KEY_BARCODE_CAPTURE_DATE,//1
	        		KEY_BARCODE},//2 
	        		null, 
	                null, 
	                null, 
	                null, 
	                null);
	    }
	    /**
	     * Delete all the data in the table
	     */
	    public void deletAllDeatils()
		   {
			 this.db.delete(DATABASE_TABLE_BARCODE, null, null);

		   }
}
