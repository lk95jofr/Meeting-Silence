package noip.toonsnet.app.activity;

import noip.toonsnet.app.MeetingSilence;
import noip.toonsnet.app.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MeetingSilenceDialogActivity extends Activity implements OnClickListener {
    private static final String TAG = "MeetingSilenceDialogActivity";
    
    private Button hideButton;
    private Button cancelButton;
    
	private MeetingSilence meetingSilence = null;
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate called");

		setContentView(R.layout.dialog);
		
		hideButton = (Button)findViewById(R.id.buttonHide);
		hideButton.setOnClickListener(this);
		cancelButton = (Button)findViewById(R.id.buttonCancel);
		cancelButton.setOnClickListener(this);
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
		
		meetingSilence = new MeetingSilence(getBaseContext());
		
		String sTitle = meetingSilence.getSharedPreference("title", "");
		String sTime = meetingSilence.getSharedPreference("time", "");
		
//		Log.d(TAG, "title: " + sTitle);
//		Log.d(TAG, "time: " + sTime);
		
		this.setTitle(sTitle);
		TextView time = (TextView)findViewById(R.id.time);
		time.setText(sTime);
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
	
	@Override
	public void onClick(View v) {
		if (v == cancelButton) {
			meetingSilence.cancelNotification();
			meetingSilence.turnOnSound();
			meetingSilence.setIsPhoneSilent(false);
		}
		
		finish();
	}
}
