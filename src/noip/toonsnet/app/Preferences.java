package noip.toonsnet.app;
 
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
 
public class Preferences extends PreferenceActivity implements OnPreferenceClickListener {
    private static final String TAG = "MeetingSilencePreferences";
    
	boolean CheckboxPreference;
    String ListPreference;
    String editTextPreference;
    String ringtonePreference;
    String secondEditTextPreference;
    String customPreference;
    
    Preference customPref;
    Preference editTextPref;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		// Get the custom preference
		customPref = (Preference)findPreference("customPref");
		customPref.setOnPreferenceClickListener(this);
		editTextPref = (Preference)findPreference("editTextPref");
		editTextPref.setOnPreferenceClickListener(this);
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
		
		getPrefs();
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
    
	public boolean onPreferenceClick(Preference preference) {
		if (preference == customPref) {
			Toast.makeText(getBaseContext(), "The custom preference has been clicked", Toast.LENGTH_LONG).show();
			SharedPreferences customSharedPreference = getSharedPreferences("myCustomSharedPrefs", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor editor = customSharedPreference.edit();
//		editor.putString("myCustomPref", "The preference has been clicked");
//		editor.commit();
		} else if (preference == editTextPref) {
			Toast.makeText(getBaseContext(), "The edit text preference has been clicked", Toast.LENGTH_LONG).show();
		}
		
		return true;
	}
	
    private void getPrefs() {
    	// Get the xml/preferences.xml preferences
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	CheckboxPreference = prefs.getBoolean("checkboxPref", false);
//    	ListPreference = prefs.getString("listPref", "nr1");
    	editTextPreference = prefs.getString("editTextPref", "Nothing has been entered");
    	ringtonePreference = prefs.getString("ringtonePref", "DEFAULT_RINGTONE_URI");
    	secondEditTextPreference = prefs.getString("SecondEditTextPref", "Nothing has been entered");
    	
    	// Get the custom preference
//    	SharedPreferences mySharedPreferences = getSharedPreferences("myCustomSharedPrefs", Activity.MODE_PRIVATE);
//    	customPref = mySharedPreferences.getString("myCusomPref", "");
    }
}