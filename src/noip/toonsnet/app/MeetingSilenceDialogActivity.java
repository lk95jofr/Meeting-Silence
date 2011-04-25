package noip.toonsnet.app;

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
    private Button cancelAddRuleButton;
    
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate called");
		
		setContentView(R.layout.dialog);
		
		hideButton = (Button)findViewById(R.id.buttonHide);
		hideButton.setOnClickListener(this);
		cancelButton = (Button)findViewById(R.id.buttonCancel);
		cancelButton.setOnClickListener(this);
		cancelAddRuleButton = (Button)findViewById(R.id.buttonCancelAddRule);
		cancelAddRuleButton.setOnClickListener(this);
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
		
		
		Bundle extras = getIntent().getExtras();
		String sTitle = extras.getString("title");
		String sTime = extras.getString("time");
		String sDescription = extras.getString("description");
		
		TextView title = (TextView)findViewById(R.id.title);
		title.setText(sTitle);
		TextView time = (TextView)findViewById(R.id.time);
		time.setText(sTime);
		TextView description = (TextView)findViewById(R.id.description);
		description.setText(sDescription);
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
			// change the preferences
			finish();
		} else if (v == hideButton) {
			finish();
		} else if (v == cancelAddRuleButton) {
			// goto activity add rule with title text
			finish();
		}
	}
}
