package noip.toonsnet.app;

import java.util.Calendar;

import noip.toonsnet.app.activity.MeetingSilenceDialogActivity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;

public class MeetingSilence {
    private static final String TAG = "MeetingSilence";
    
	private static final int NOTIFICATION_ID = 271172;
	
    private Context context;
    
	private static final Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/calendars");
	private static final Uri EVENT_URI = Uri.parse("content://com.android.calendar/instances/when");
    
    public MeetingSilence(Context mContext) {
    	context = mContext;
    	
//    	if (android.os.Build.VERSION.SDK_INT <= 7 ) {
//    		calendarUri = Uri.parse("content://calendar/calendars"); 
//    		eventUri    = Uri.parse("content://calendar/events"); // ??????
//    	}
    	
//    	Log.d(TAG, "calendarUri: " + calendarUri.toString());
//    	Log.d(TAG, "eventUri: " + eventUri.toString());
    }
    
	public void setAlarmNotification(Calendar nextSchedule) {
		Intent mIntent = new Intent(context, MeetingSilenceReceiver.class);
		mIntent.setAction("noip.toonsnet.app.MeetingSilenceReceiver");
		
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, mIntent, 0);
	    
	    nextSchedule.set(Calendar.SECOND, 0); // Make sure we run "on the minute"
    	Log.d(TAG, "Next schedule: " + calendarToString(nextSchedule));

		AlarmManager mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, nextSchedule.getTimeInMillis(), pendingIntent);
	}
	
    public void turnOffSound() {
//		Log.d(TAG, "turnOffSound called");
    	
    	final AudioManager mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    	setSharedPreference("ringerMode", mAudioManager.getRingerMode());
    	setSharedPreference("vibrateTypeNotification", mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION));
    	setSharedPreference("vibrateTypeRinger", mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER));
    	
    	boolean soundPref = getSharedPreference("soundPref", true);
    	boolean vibratePref = getSharedPreference("vibratePref", true);
    	
    	if (soundPref && vibratePref) {
    		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    	} else if (soundPref) {
        	mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
        	mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
    	} else if (vibratePref) {
    		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        	mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, getSharedPreference("vibrateTypeNotification", AudioManager.VIBRATE_SETTING_ON));
        	mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, getSharedPreference("vibrateTypeRinger",  AudioManager.VIBRATE_SETTING_ON));
    	}
    }
    
    public void turnOnSound() {
//		Log.d(TAG, "turnOnSound called");
    	
    	final AudioManager mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    	mAudioManager.setRingerMode(getSharedPreference("ringerMode", AudioManager.RINGER_MODE_NORMAL));
    	mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, getSharedPreference("vibrateTypeNotification", AudioManager.VIBRATE_SETTING_ON));
    	mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, getSharedPreference("vibrateTypeRinger",  AudioManager.VIBRATE_SETTING_ON));
    }
    
    public void triggerNotification(String title, String begin, String end, String description) {
        CharSequence notificationTitle = " In Meeting";
        CharSequence notificationMessage = "Phone is in silence until " + end;
		
        Intent notificationIntent = new Intent(context, MeetingSilenceDialogActivity.class);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        setSharedPreference("title", title);
        setSharedPreference("time", begin + " - " + end);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
 
        final NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.mute, notificationMessage, System.currentTimeMillis());
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT | Notification.FLAG_ONLY_ALERT_ONCE;
        notification.setLatestEventInfo(context, notificationTitle, notificationMessage, pendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID, notification);
        
//        Log.d(TAG, "notificationMessage: " + notificationMessage);
    }
    
    public void cancelNotification() {
        final NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
        setSharedPreference("title", "");
        setSharedPreference("time", "");
    }
    
    public String calendarToString(Calendar calendar) {
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append((hour < 10 ? ("0" + hour) : hour));
		sBuilder.append(":");
		sBuilder.append((minute < 10 ? ("0" + minute) : minute));
		
		return sBuilder.toString();
    }
    
    public Cursor getCalendarCursor() {
    	final String[] projectionCalendar = new String[] { "_id", "name" };
    	Cursor calendarCursor = context.getContentResolver().query(CALENDAR_URI, projectionCalendar, "selected=1", null, null);
    	
    	return calendarCursor;
    }
    
    public Cursor getEventCursor(String id) {
	    Uri.Builder builder = EVENT_URI.buildUpon();
	    long now = System.currentTimeMillis();
	    ContentUris.appendId(builder, now); // From
	    ContentUris.appendId(builder, now + (DateUtils.MINUTE_IN_MILLIS * 10)); // To

    	final String[] projectionEvents = new String[] {"event_id", "title", "begin", "end", "allDay","description","eventLocation"};
        Cursor eventCursor = context.getContentResolver().query(builder.build(), projectionEvents, "Calendars._id=" + id, null, "startDay ASC, startMinute ASC");    		    
    	
    	return eventCursor;
    }
    
    public boolean isPhoneSilent() {
		boolean isPhoneSilent = getSharedPreference("isPhoneSilent", false);
    	return isPhoneSilent;
    }
    
    public void setIsPhoneSilent(boolean value) {
    	setSharedPreference("isPhoneSilent", value);
    }

    public boolean getSharedPreference(String key, boolean defaultValue) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(key, defaultValue);
    }
    
    public void setSharedPreference(String key, boolean value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
    }
    
    public int getSharedPreference(String key, int defaultValue) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getInt(key, defaultValue);
    }
    
    public void setSharedPreference(String key, int value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
    }
    
    public String getSharedPreference(String key, String defaultValue) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(key, defaultValue);
    }
    
    public void setSharedPreference(String key, String value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
    }
}
