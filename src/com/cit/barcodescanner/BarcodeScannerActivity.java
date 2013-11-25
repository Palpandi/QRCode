package com.cit.barcodescanner;

import com.cit.barcodescanner.actionitem.BarcodeScannerActionItem;
import com.cit.barcodescanner.actionitem.BarcodeScannerQuickAction;
import com.cit.barcodescanner.db.BarcodeScannerDBAdapter;
import com.google.zxing.client.android.BarCodeCaptureActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author Vishnuvardhanan.S
 *
 */
public class BarcodeScannerActivity extends Activity {
	
	
	private final int CODE_SCANNER_INTENT = 1;
	
	//Initialize all the widgets
	private Button scan_btn;
	private TextView title_text,barcode_text;
	private ImageButton quickAction_btn;
	
	//Intialize other class function
	private BarcodeScannerActionItem aiSetting,aiDataBase;
	private BarcodeScannerQuickAction quickAction;
	private BarcodeScannerDBAdapter _dbAdpter;
	
	//Intialize ID for QuickActionItem
	private final int ID_SETTINGS = 1;
	private final int ID_DATA = 2; 
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		/** Set the custom title bar */
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.app_title_bar);
		
		/** Initialize the user function classes */
		_dbAdpter =new BarcodeScannerDBAdapter(this); 
        
		/** Initialize the widgets */
        title_text=(TextView)findViewById(R.id.title_text);
        quickAction_btn=(ImageButton)findViewById(R.id.title_bar_home_btn);
        barcode_text=(TextView)findViewById(R.id.barcode_text);
        scan_btn =(Button)findViewById(R.id.btn_scan);
        
        title_text.setText("Barcode Scanner");
        
        /** Initialize the ID and functionality for QuickAction */
        aiSetting = new BarcodeScannerActionItem(ID_SETTINGS,
				"Settings", null);
        aiDataBase = new BarcodeScannerActionItem(ID_DATA,
				"Barcode Details", null);
        quickAction = new BarcodeScannerQuickAction(this,
        		BarcodeScannerQuickAction.VERTICAL);
		quickAction.addActionItem(aiSetting);
		quickAction.addActionItem(aiDataBase);
		
		quickAction
		.setOnActionItemClickListener(new BarcodeScannerQuickAction.OnActionItemClickListener() {	
			@Override
			public void onItemClick(BarcodeScannerQuickAction source, int pos,
					int actionId) {
				switch(actionId){
					case ID_SETTINGS:
						BarcodeScannerActivity.this
								.startActivity(new Intent(
										BarcodeScannerActivity.this,
										BarcodePreferencesActivity.class));
						break;
						
					case ID_DATA:
						BarcodeScannerActivity.this
						.startActivity(new Intent(
								BarcodeScannerActivity.this,
								BarcodeDetailsActivity.class));
						break;
				}
				
			}
		});
		
		
		quickAction_btn.setOnClickListener(new OnClickListener(){			
			@Override
			public void onClick(View v) {
				quickAction.show(v);
			}
		});
        
        scan_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(Intent.ACTION_PICK);
				i.setDataAndType(null,
						BarCodeCaptureActivity.MIME_TYPE);
				startActivityForResult(i, CODE_SCANNER_INTENT);
			}
		});
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CODE_SCANNER_INTENT) {
			switch (resultCode) {
			case -1:
				if (data.getStringExtra("scan_code") != null) {
					
					barcode_text.setText(data.getStringExtra("scan_code"));
					_dbAdpter.open();
					_dbAdpter.insertBarcodeDetails(data.getStringExtra("scan_date_time"), 
							data.getStringExtra("scan_code"));
					_dbAdpter.close();
					scan_btn.setText("Scan Next");
					
				}else
					Toast.makeText(BarcodeScannerActivity.this,
							data.getStringExtra("Unable to retrieve scan information."),3000).show();
				break;
			case 0:
				Toast.makeText(BarcodeScannerActivity.this, 
						"Scan Cancelled",3000).show();
				break;
			}
		}
	}
}