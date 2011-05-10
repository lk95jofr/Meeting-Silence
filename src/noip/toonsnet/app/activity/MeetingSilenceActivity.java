package noip.toonsnet.app.activity;

import noip.toonsnet.app.R;
import android.app.Activity;
import android.os.Bundle;

public class MeetingSilenceActivity extends Activity {
    private static final String TAG = "MeetingSilenceActivity";
    
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Log.d(TAG, "onCreate called");
		
        setContentView(R.layout.main);
//        
//        AccountManager mgr = AccountManager.get(this);
//        Account[] accts = mgr.getAccountsByType("com.google");
//        Account acct = accts[0];
//        
//        try {
//	        AccountManagerFuture<Bundle> accountManagerFuture = mgr.getAuthToken(acct, "cl", null, this, null, null);
//	        Bundle authTokenBundle = accountManagerFuture.getResult();
//	        String authToken = authTokenBundle.get(AccountManager.KEY_AUTHTOKEN).toString();
//	        
//	        mgr.invalidateAuthToken("com.google", authToken);
//	        
//	        accountManagerFuture = mgr.getAuthToken(acct, "cl", null, this, null, null);
//	        authTokenBundle = accountManagerFuture.getResult();
//	        authToken = authTokenBundle.get(AccountManager.KEY_AUTHTOKEN).toString();
//        } catch (AuthenticatorException e) {
//        } catch (IOException e) {
//        } catch (OperationCanceledException e) {
//        }
//        CalendarService myService = new CalendarService("UserCalendar");
//        myService.setAuthSubToken(authtoken);
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
