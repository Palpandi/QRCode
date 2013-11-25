package com.cit.barcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
/**
 * 
 * @author Vishnuvardhanan.S
 *
 */

public class BarcodeSplashActivity extends Activity {
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_view);
		
		/** Run thread to change screen once the display time is over */
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
	             startActivity(new Intent(BarcodeSplashActivity.this,BarcodeScannerActivity.class));
	             BarcodeSplashActivity.this.finish();
			}
		}, BarcodeScannerVariable.SPLASH_DISPLAY_TIME);
	}


}
