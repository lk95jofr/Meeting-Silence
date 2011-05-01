package noip.toonsnet.app.activity;

import noip.toonsnet.app.R;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MeetingSilenceDialogActivity extends Activity implements OnClickListener {
    private static final String TAG = "MeetingSilenceDialogActivity";
    
	private static final int NOTIFICATION_ID = 271172;
	
    private Button hideButton;
    private Button cancelButton;
    
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate called");
		
		setContentView(R.layout.dialog);
		
		hideButton = (Button)findViewById(R.id.buttonHide);
		hideButton.setOnClickListener(this);
		cancelButton = (Button)findViewById(R.id.buttonCancel);
		cancelButton.setOnClickListener(this);
		
		Bundle extras = getIntent().getExtras();
		String sTitle = extras.getString("title");
		String sTime = extras.getString("time");
		String sDescription = extras.getString("description");
		
		Log.d(TAG, "title: " + sTitle);
		Log.d(TAG, "time: " + sTime);
		Log.d(TAG, "desc: " + sDescription);
		
		this.setTitle(sTitle);
		TextView time = (TextView)findViewById(R.id.time);
		time.setText(sTime);
		TextView description = (TextView)findViewById(R.id.description);
		description.setText(sDescription);
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
	
	@Override
	public void onClick(View v) {
		if (v == cancelButton) {
			turnOnSound();
			cancelNotification();
		}
		
		finish();
	}
	
    private void turnOnSound() {
		Log.d(TAG, "turnOnSound called");
		setSharedPreference("isPhoneSilent", false);
    	
    	final AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    	mAudioManager.setRingerMode(getSharedPreference("ringerMode", AudioManager.RINGER_MODE_NORMAL));
    	mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, getSharedPreference("vibrateTypeNotification", AudioManager.VIBRATE_SETTING_ON));
    	mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, getSharedPreference("vibrateTypeRinger",  AudioManager.VIBRATE_SETTING_ON));
    }
    
    private void cancelNotification() {
        final NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }
    
//    private boolean getSharedPreference(String key, boolean defaultValue) {
//		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//		return sp.getBoolean(key, defaultValue);
//    }
    
    private void setSharedPreference(String key, boolean value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
    }
    
    private int getSharedPreference(String key, int defaultValue) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		return sp.getInt(key, defaultValue);
    }
    
//    private void setSharedPreference(String key, int value) {
//		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//		SharedPreferences.Editor editor = sp.edit();
//		editor.putInt(key, value);
//		editor.commit();
//    }
    
//    private String getSharedPreference(String key, String defaultValue) {
//		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//		return sp.getString(key, defaultValue);
//    }
    
//    private void setSharedPreference(String key, String value) {
//		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//		SharedPreferences.Editor editor = sp.edit();
//		editor.putString(key, value);
//		editor.commit();
//    }
}
