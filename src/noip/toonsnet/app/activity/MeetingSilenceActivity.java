package noip.toonsnet.app.activity;

import noip.toonsnet.app.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class MeetingSilenceActivity extends Activity {
    private static final String TAG = "MeetingSilenceActivity";
    
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Log.d(TAG, "onCreate called");
		
        setContentView(R.layout.main);
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
	}
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
//		Log.d(TAG, "onDestroy called");
    }
}
