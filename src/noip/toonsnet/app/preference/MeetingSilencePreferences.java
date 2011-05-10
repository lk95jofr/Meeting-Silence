package noip.toonsnet.app.preference;
 
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import noip.toonsnet.app.MeetingSilence;
import noip.toonsnet.app.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
 
public class MeetingSilencePreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    private static final String TAG = "MeetingSilencePreferences";
    
    private MeetingSilence meetingSilence;
    
    private EditTextPreference mStartPref;
    private EditTextPreference mEndPref;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Log.d(TAG, "onCreate called");
		
		meetingSilence = new MeetingSilence(getBaseContext());
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String first_time = sp.getString("rulePref", "FIRST_TIME_INSTALLED");
		if ("FIRST_TIME_INSTALLED".equals(first_time)) {
		    Calendar nextSchedule = Calendar.getInstance();
		    nextSchedule.add(Calendar.MINUTE, 5);
		    
			meetingSilence.setAlarmNotification(nextSchedule);
		}
		
		addPreferencesFromResource(R.xml.preferences);
		
		mStartPref = (EditTextPreference)getPreferenceScreen().findPreference("startPref");
//		mEndPref = (EditTextPreference)getPreferenceScreen().findPreference("endPref");
		
//		String calendarList = meetingSilence.getSharedPreference("calendarList", "");
		
//		Cursor calendarCursor = meetingSilence.getCalendarCursor();
//    	if (calendarCursor.moveToFirst()) {
//    		String calName = "";
//    		String calId = "";
//    		int nameColumn = calendarCursor.getColumnIndex("name");
//    		int idColumn = calendarCursor.getColumnIndex("_id");
//    		
//    		do {
//    		    calId = calendarCursor.getString(idColumn);
//    			calName = calendarCursor.getString(nameColumn);
//    		    if (calName == null) { // Exchange always null??? And only exchange???
//    		    	calName = "Exchange";
//    		    }
//    		    
//    		    Log.d(TAG, "Calendar: " + calId + ", " + calName);
//    		} while (calendarCursor.moveToNext());
//    	}
//    	
//    	if (calendarCursor != null) {
//    		calendarCursor.close();
//    	}
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
        
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    
	@Override
	protected void onPause() {
		super.onPause();
//		Log.d(TAG, "onPause called");
        
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
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
    
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//		Log.d(TAG, "onSharedPreferenceChanged called, " + key);
		
//        if (key.equals("startPref")) {
//    		Log.d(TAG, "startPref clicked");
//    		String value = sharedPreferences.getString("startPref", "8:00");
//    		String[] vArr = value.split(":");
//    		Log.d(TAG, "Arr 0 " + vArr[0]);
//    		Log.d(TAG, "Arr 1 " + vArr[1]);
//
//    		try {
//	    		Pattern pattern = Pattern.compile("^[0-9]$");
//	    		Matcher m0 = pattern.matcher(vArr[0]);
//	    		Matcher m1 = pattern.matcher(vArr[1]);
//	    		if(!m0.matches() || !m1.matches()) {
//	        		Log.d(TAG, "No match");
//	      			value = "8:00";
//	    		}
//    		} catch (ArrayIndexOutOfBoundsException e) {
//        		Log.d(TAG, "Exception: " + e.getMessage());
//    			value = "8:00";
//    		}
//
//    		Log.d(TAG, "Value: " + value);
////			mStartPref.setText(value);
//        } else if (key.equals("endPref")) {
//    		Log.d(TAG, "endPref clicked");
//    		String value = sharedPreferences.getString("endPref", "18:00");
////    		mEndPref.setText(value);
//        }
        
    }
}