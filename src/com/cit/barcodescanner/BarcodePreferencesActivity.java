package com.cit.barcodescanner;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * @author Arindam Nath
 * 
 */
public final class BarcodePreferencesActivity extends PreferenceActivity
		implements OnSharedPreferenceChangeListener {

	public static final String KEY_DECODE_1D = "preferences_decode_1D";
	public static final String KEY_DECODE_QR = "preferences_decode_QR";
	public static final String KEY_DECODE_DATA_MATRIX = "preferences_decode_Data_Matrix";
	public static final String KEY_CUSTOM_PRODUCT_SEARCH = "preferences_custom_product_search";

	public static final String KEY_REVERSE_IMAGE = "preferences_reverse_image";
	public static final String KEY_PLAY_BEEP = "preferences_play_beep";
	public static final String KEY_VIBRATE = "preferences_vibrate";
	public static final String KEY_COPY_TO_CLIPBOARD = "preferences_copy_to_clipboard";
	public static final String KEY_FRONT_LIGHT = "preferences_front_light";
	public static final String KEY_BULK_MODE = "preferences_bulk_mode";
	public static final String KEY_REMEMBER_DUPLICATES = "preferences_remember_duplicates";
	public static final String KEY_SUPPLEMENTAL = "preferences_supplemental";
	public static final String KEY_SEARCH_COUNTRY = "preferences_search_country";

	public static final String KEY_HELP_VERSION_SHOWN = "preferences_help_version_shown";

	private CheckBoxPreference decode1D;
	private CheckBoxPreference decodeQR;
	private CheckBoxPreference decodeDataMatrix;
	
	private TextView title_text;
	private LinearLayout llAppBarSettingsBtnLayout;

	@Override
	protected void onCreate(Bundle icicle) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.preferences);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.app_title_bar);
				
		title_text =(TextView) findViewById(R.id.title_text);
		llAppBarSettingsBtnLayout = (LinearLayout) findViewById(R.id.title_bar_home_btn_layout);
		
		title_text.setText("Settings");
		llAppBarSettingsBtnLayout.setVisibility(View.GONE);

		getListView().setCacheColorHint(Color.TRANSPARENT);
		getListView().setVerticalScrollBarEnabled(false);
		getListView().setVerticalFadingEdgeEnabled(false);
		getListView().setDividerHeight(0);

		PreferenceScreen preferences = getPreferenceScreen();
		preferences.getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		decode1D = (CheckBoxPreference) preferences
				.findPreference(KEY_DECODE_1D);
		decodeQR = (CheckBoxPreference) preferences
				.findPreference(KEY_DECODE_QR);
		decodeDataMatrix = (CheckBoxPreference) preferences
				.findPreference(KEY_DECODE_DATA_MATRIX);

		disableLastCheckedPref();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		disableLastCheckedPref();
	}

	private void disableLastCheckedPref() {
		Collection<CheckBoxPreference> checked = new ArrayList<CheckBoxPreference>(
				3);
		if (decode1D.isChecked()) {
			checked.add(decode1D);
		}
		if (decodeQR.isChecked()) {
			checked.add(decodeQR);
		}
		if (decodeDataMatrix.isChecked()) {
			checked.add(decodeDataMatrix);
		}
		boolean disable = checked.size() < 2;
		CheckBoxPreference[] checkBoxPreferences = { decode1D, decodeQR,
				decodeDataMatrix };
		for (CheckBoxPreference pref : checkBoxPreferences) {
			pref.setEnabled(!(disable && checked.contains(pref)));
		}
	}

}
