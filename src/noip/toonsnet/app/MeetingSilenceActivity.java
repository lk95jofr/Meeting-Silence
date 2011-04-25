package noip.toonsnet.app;

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
		
		// start service if stopped
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
    
//    private class ServiceCenterAdapter extends ArrayAdapter<ServiceCenter> {
//    	private ArrayList<ServiceCenter> items;
//
//        public ServiceCenterAdapter(Context context, int textViewResourceId, ArrayList<ServiceCenter> items) {
//        	super(context, textViewResourceId, items);
//        	this.items = items;
//        }
//        
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//        	View v = convertView;
//        	if (v == null) {
//        		LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        		v = li.inflate(R.layout.servicecenterlist, null);
//        	}
//        	
//        	ServiceCenter o = items.get(position);
//        	if (o != null) {
//        		TextView name = (TextView) v.findViewById(R.id.dname);
//        		TextView phone = (TextView) v.findViewById(R.id.dphn);
//        		TextView addr = (TextView) v.findViewById(R.id.daddr_dcity);
//        		TextView url = (TextView) v.findViewById(R.id.durl);
//        		
//        		if (name != null) {
//        			name.setText(o.getDName());
//        		}
//        		
//        		if ((phone != null) && (!"".equals(o.getDPhn()))) {
//        			phone.setText(o.getDPhn());
//        		}
//        		
//        		if ((addr != null) && (!"".equals(o.getDAddr())) || (!"".equals(o.getDCity()))) {
//        			addr.setText(o.getDAddr() + ", " + o.getDCity());
//        		}
//        		
//        		if ((url != null) && (!"".equals(o.getDUrl()))) {
//        			url.setText(o.getDUrl());
//        		}
//        	}
//        	
//        	return v;
//        }
//    }
}
