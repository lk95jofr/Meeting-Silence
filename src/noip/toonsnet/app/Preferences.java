package noip.toonsnet.app;
 
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
 
public class Preferences extends PreferenceActivity implements OnPreferenceClickListener, OnPreferenceChangeListener {
    private static final String TAG = "MeetingSilencePreferences";
    
    Preference startPref;
    Preference endPref;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	public void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart called");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart called");
	}

    @Override
    protected void onResume() {
        super.onResume();
		Log.d(TAG, "onResume called");
    }
    
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause called");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop called");
	}
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
		Log.d(TAG, "onDestroy called");
    }
    
	public boolean onPreferenceChange(Preference preference, Object o) {
		if ((preference == startPref) || (preference == endPref)) {
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
			String start =  sp.getString("startPref", "8:00");
			String end =  sp.getString("endPref", "18:00");
			// check for forbidden characters
		}
		
		return true;
	}
	
	public boolean onPreferenceClick(Preference preference) {
		return true;
	}
}