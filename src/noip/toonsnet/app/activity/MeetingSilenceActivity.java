package noip.toonsnet.app.activity;

import noip.toonsnet.app.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MeetingSilenceActivity extends Activity {
    private static final String TAG = "MeetingSilenceActivity";
    
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate called");
		
        setContentView(R.layout.main);
        
//		Intent mIntent = new Intent(getBaseContext(), MeetingSilenceReceiver.class);
//		mIntent.setAction("noip.toonsnet.app.MeetingSilenceReceiver");
//		
//	    PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, mIntent, 0);
//	    
//		Calendar nextSchedule = Calendar.getInstance();
//		nextSchedule.add(Calendar.MINUTE, 2); // Wait 2 min for first cycle
//	    nextSchedule.set(Calendar.SECOND, 0); // Make sure we run "on the minute"
//
//		AlarmManager mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//		mAlarmManager.set(AlarmManager.RTC_WAKEUP, nextSchedule.getTimeInMillis(), pendingIntent);
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
}
