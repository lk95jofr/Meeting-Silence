package noip.toonsnet.app.preference;
 
import java.util.Calendar;

import noip.toonsnet.app.MeetingSilence;
import noip.toonsnet.app.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
 
public class MeetingSilencePreferences extends PreferenceActivity implements OnPreferenceClickListener, OnPreferenceChangeListener {
    private static final String TAG = "MeetingSilencePreferences";
    
//    Preference startPref;
//    Preference endPref;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Log.d(TAG, "onCreate called");
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String first_time = sp.getString("rulePref", "FIRST_TIME_INSTALLED");
		if ("FIRST_TIME_INSTALLED".equals(first_time)) {
		    Calendar nextSchedule = Calendar.getInstance();
		    nextSchedule.add(Calendar.MINUTE, 5);
		    
			MeetingSilence meetingSilence = new MeetingSilence(getBaseContext());
			meetingSilence.setAlarmNotification(nextSchedule);
		}
		
		addPreferencesFromResource(R.xml.preferences);
		
//		startPref.setOnPreferenceChangeListener(this);
//		endPref.setOnPreferenceChangeListener(this);
	}

	@Override
	public void onRestart() {
		super.onRestart();
//		Log.d(TAG, "onRestart called");
	}

	@Override
	public void onStart() {
		super.onStart();
//		Log.d(TAG, "onStart called");
	}

    @Override
    protected void onResume() {
        super.onResume();
//		Log.d(TAG, "onResume called");
    }
    
	@Override
	protected void onPause() {
		super.onPause();
//		Log.d(TAG, "onPause called");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
//		Log.d(TAG, "onStop called");
		finish(); // To make sure onCreate always run
	}
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
//		Log.d(TAG, "onDestroy called");
    }
    
	public boolean onPreferenceChange(Preference preference, Object o) {
//		if ((preference == startPref) || (preference == endPref)) {
//			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//			String start =  sp.getString("startPref", "8:00");
//			String end =  sp.getString("endPref", "18:00");
//			// check for forbidden characters
//		}
		
		return true;
	}
	
	public boolean onPreferenceClick(Preference preference) {
		return true;
	}
}