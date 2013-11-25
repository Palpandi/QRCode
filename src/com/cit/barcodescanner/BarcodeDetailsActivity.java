package com.cit.barcodescanner;

import java.util.ArrayList;

import com.cit.barcodescanner.db.BarcodeScannerDBAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
/**
 * 
 * @author Vishnuvardhanan.S
 *
 */
public class BarcodeDetailsActivity extends Activity {
	
	// Initialize all the widgets.
	private TextView title_text,empty_text;
	private LinearLayout appsettingLayout;
	private TableLayout barcode_table;
	private HorizontalScrollView scrollHoriontal;
	private BarcodeScannerDBAdapter _dbAdpter;
	private Cursor _cCursor;
	private Button delete_btn;
	private LinearLayout empty_layout;
	private Typeface font;
	
	//Initialize ArrayList to variable
	private ArrayList<Integer> listRowID=new ArrayList<Integer>();
	private ArrayList<String> listcaptureDate = new ArrayList<String>();
	private ArrayList<String> listBarcode = new ArrayList<String>();
	private Integer[] rowID; 
						
						
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.barcode_details);
		/** Set the custom title bar */
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.app_title_bar);
		
		/** Initialize the user function classes */
		_dbAdpter = new BarcodeScannerDBAdapter(this); 
		font = Typeface.createFromAsset(getAssets(), BarcodeScannerVariable.FONT_NAME);
		
		/** Initialize the widgets */
		title_text=(TextView)findViewById(R.id.title_text);
		appsettingLayout=(LinearLayout)findViewById(R.id.title_bar_home_btn_layout);
		barcode_table =(TableLayout)findViewById(R.id.barcode_table_layout);
		scrollHoriontal=(HorizontalScrollView)findViewById(R.id.barcode_table_scroll);
		delete_btn=(Button)findViewById(R.id.delete_btn);
		empty_layout=(LinearLayout)findViewById(R.id.data_empty_layout);
		empty_text=(TextView)findViewById(R.id.data_empty_text);
		
		empty_text.setTypeface(font);
		appsettingLayout.setVisibility(View.GONE);
		scrollHoriontal.setHorizontalFadingEdgeEnabled(false);
		scrollHoriontal.setHorizontalScrollBarEnabled(false);
		title_text.setText("Barcode Details");
		
		//Calling private function
    	retrieveBarcodedata(); 
    	
    	//Checking for data availablity in table
		if(rowID.length > 0){
			empty_layout.setVisibility(View.GONE);
		}else{
			empty_layout.setVisibility(View.VISIBLE);
			barcode_table.setVisibility(View.GONE);
			delete_btn.setVisibility(View.GONE);
		}
 
    	//Function to delete all data in the table once button is clicked.
		delete_btn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				_dbAdpter.open();
				_dbAdpter.deletAllDeatils();
				_dbAdpter.close();
				startActivity(getIntent());
				BarcodeDetailsActivity.this.finish();
				
			}
		});
 	  	
    }
    	
    	private void  retrieveBarcodedata(){
    		
    		//Creating Table with two column
    		TableRow row;
        	TextView table_capturedate, table_barcode;
        	
        	//Converting to dip unit
        	int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 1, getResources().getDisplayMetrics());
        	
        	//Getting Data from Database
        	_dbAdpter.open();
    		_cCursor = _dbAdpter.getAllDetails();
    		    if (_cCursor != null) {
    			    if (_cCursor.moveToFirst()) {
    			    		do {
    			    			listRowID.add(_cCursor.getInt(0));
    			    			listcaptureDate.add(_cCursor.getString(1));
    			    			listBarcode.add(_cCursor.getString(2));			    			
    			    		}while (_cCursor.moveToNext());
    			    	}
    			   } 
    		    _cCursor.deactivate();
        	
        	_cCursor.close();
        	_dbAdpter.close();
        	
        	rowID = listRowID.toArray(new Integer[listRowID.size()]);
        	String[] capture_date = listcaptureDate.toArray(new String[listcaptureDate.size()]);
        	String[] barcode = listBarcode.toArray(new String[listBarcode.size()]);
        	
        	for (int current = -1; current < rowID.length; current++) {
        		
        		/** Init the widget variables */
        		row = new TableRow(this);
        		
        		table_capturedate = new TextView(this);
        		table_barcode = new TextView(this);
        		
        		/** Set the textview color to white */
        		table_capturedate.setTextColor(Color.BLACK);
        		table_barcode.setTextColor(Color.BLACK);
        		
        		/** Set the textSize */
        		table_capturedate.setTextSize(18);
        		table_barcode.setTextSize(18);
           		
        		/** Set the header titles when the loop is at -1 */
        		if(current == -1) {    			
        			table_capturedate.setText("CaptureDate/Time");
        			table_barcode.setText("Barcode");
        			
        			/** Textview background*/
        			table_capturedate.setBackgroundResource(R.drawable.lable_background);
        			table_barcode.setBackgroundResource(R.drawable.lable_background);
        			
        			/** Textview shadow */
        			//table_capturedate.setShadowLayer(2, 0, 0, 0xFF2DE3E1);
        			//table_barcode.setShadowLayer(2, 0, 0, 0xFF2DE3E1);
        		}else{
        			/** Set alternating row background colors*/
        			if ((current & 1) == 1)
        				row.setBackgroundColor(0x80FFFFFF);
        			
        			table_capturedate.setText(capture_date[current]);
        			table_barcode.setText(barcode[current]);
        		}
        		/** set the padding */
        		table_capturedate.setPadding(5*dip, 5*dip, 5*dip,5*dip);
        		table_barcode.setPadding(5*dip, 5*dip, 5*dip, 5*dip);
        		
        		row.addView(table_capturedate);
        		row.addView(table_barcode);
        		
        		/** Add the rows to the table layout*/
        		barcode_table.addView(row, new TableLayout.LayoutParams(
        				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        		
        	}
    		
    	}
}
