package noip.toonsnet.app;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
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
import android.widget.Toast;

public class MeetingSilenceReceiver extends BroadcastReceiver {
    private static final String TAG = "MeetingSilenceReceiver";
    
    private Context context;
    
	private Calendar nextSchedule;
	
	private static final int NOTIFICATION_ID = 271172;
	
	@Override
	public void onReceive(Context mContext, Intent intent) {
		context = mContext;

		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			nextSchedule = Calendar.getInstance();
			nextSchedule.add(Calendar.MINUTE, 2); // Wait 2 min for boot to complete
		} else if (intent.getAction().equals("noip.toonsnet.app.MeetingSilenceReceiver")) {
			iterateThruCalendars();
		}
		
		// To remove, only debug
		Toast.makeText(context, intent.getAction() + ", " + calendarToString(nextSchedule), Toast.LENGTH_LONG).show();
		setAlarmNotification();
	}
	
	private void setAlarmNotification() {
		Intent mIntent = new Intent(context, MeetingSilenceReceiver.class);
		mIntent.setAction("noip.toonsnet.app.MeetingSilenceReceiver");
		
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, mIntent, 0);
	    
	    nextSchedule.set(Calendar.SECOND, 0); // Make sure we run "on the minute"
    	Log.d(TAG, "Next schedule: " + calendarToString(nextSchedule));

		AlarmManager mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, nextSchedule.getTimeInMillis(), pendingIntent);
	}
	
	private void iterateThruCalendars() {
		Log.d(TAG, "iterateCalendars called");
    	
    	boolean isInMeeting = false;
    	boolean isPhoneSilent = getSharedPreference("isPhoneSilent", false);
    	Log.d(TAG, "isPhoneSilent: " + isPhoneSilent);
    	boolean ignoreSpanOverDaysEvents = getSharedPreference("ignoreSpanOverDaysEventsPref", true);
    	boolean ignoreAllDayEvents = getSharedPreference("ignoreAllDayEventPref", true);
    	
    	boolean checkTimeAndDay = getSharedPreference("allDayPref", false);
    	boolean hasSchedule = !checkTimeAndDay;
    	
    	if (checkTimeAndDay) {
	    	String startPref = getSharedPreference("startPref", "8:00");
	    	startPref = startPref.replace(".", ":");
	    	String endPref = getSharedPreference("endPref", "18:00");
	    	endPref = endPref.replace(".", ":");
	    	
	    	String[] startTime = startPref.split(":");
	    	String[] endTime = endPref.split(":");
	    	
	    	Calendar cStartTime = Calendar.getInstance();
	    	try {
	    		cStartTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTime[0]));
	    		cStartTime.set(Calendar.MINUTE, Integer.parseInt(startTime[1]));
	    	} catch (NumberFormatException e) {
	    		// If this fails we iterate
	    		hasSchedule = true;
	    	}
	    	
	    	Calendar cEndTime = Calendar.getInstance();
	    	try {
	    		cEndTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTime[0]));
	    		cEndTime.set(Calendar.MINUTE, Integer.parseInt(endTime[1]));
	    	} catch (NumberFormatException e) {
	    		// If this fails we iterate
	    		hasSchedule = true;
	    	}
	    	
			Calendar now = Calendar.getInstance();
	    	now.set(Calendar.SECOND, 0);
	    	
	    	Log.d(TAG, "START: " + calendarToString(cStartTime) + ", " + cStartTime.getTimeInMillis());
	    	Log.d(TAG, "NOW: " + calendarToString(now) + ", " + now.getTimeInMillis());
	    	Log.d(TAG, "END: " + calendarToString(cEndTime) + ", " + cEndTime.getTimeInMillis());
	    	
	    	if ((now.getTimeInMillis() >= cStartTime.getTimeInMillis()) && (now.getTimeInMillis() < cEndTime.getTimeInMillis())) {
	    		Log.d(TAG, "We have the right time");
		    	String value = getSharedPreference("multiListPref", "");
		    	String[] vals = ListPreferenceMultiSelect.parseStoredValue(value);
		    	if (vals != null) {
		        	for (int i = 0; i < vals.length; i++) {
		        		int val = Integer.parseInt(vals[i].trim());
		        		if (now.get(Calendar.DAY_OF_WEEK) == val) {
		    	    		Log.d(TAG, "We have the right day");
		        			hasSchedule = true;
		        			break;
		        		}
		        	}
		    	}
	    	}
    	}
		Log.d(TAG, "hasSchedule: " + hasSchedule);
    	
    	if (!isPhoneSilent && !hasSchedule) {
    		nextSchedule = Calendar.getInstance();
    		nextSchedule.add(Calendar.MINUTE, 5); // Next schedule is now + 5 min
        	
    		return;
    	}

		Uri calendarUri = Uri.parse("content://com.android.calendar/calendars");
		Uri eventUri = Uri.parse("content://com.android.calendar/instances/when");
		
    	if (android.os.Build.VERSION.SDK_INT <= 7 ) {
    		calendarUri = Uri.parse("content://calendar/calendars"); 
    		eventUri    = Uri.parse("content://calendar/events"); // ??????
    	}
    	
    	Log.d(TAG, "calendarUri: " + calendarUri.toString());
    	Log.d(TAG, "eventUri: " + eventUri.toString());
	     
    	String[] projectionCalendar = new String[] { "_id", "name" };
    	Cursor calendarCursor = context.getContentResolver().query(calendarUri, projectionCalendar, "selected=1", null, null);
    	if (calendarCursor.moveToFirst()) {
    		String calName = "";
    		String calId = "";
    		int nameColumn = calendarCursor.getColumnIndex("name");
    		int idColumn = calendarCursor.getColumnIndex("_id");
    		
    		nextSchedule = Calendar.getInstance();
    		nextSchedule.add(Calendar.MINUTE, 5); // Next schedule is now + 5 min
    		
    		Log.d(TAG, "Possible schedule: " + calendarToString(nextSchedule));
    		
    		do {
    			calName = calendarCursor.getString(nameColumn);
    		    if (calName == null) { // Exchange always null??? And only exchange???
    		    	calName = "Exchange";
    		    }
    		    
    		    calId = calendarCursor.getString(idColumn);
    			
    		    Log.d(TAG, "Name: " + calName + ", ID: " + calId);
    		    Uri.Builder builder = eventUri.buildUpon();
    		    long now = new Date().getTime();
    		    ContentUris.appendId(builder, now); // From
    		    ContentUris.appendId(builder, now + (DateUtils.MINUTE_IN_MILLIS * 10)); // To

    	    	String[] projectionEvents = new String[] {"event_id", "title", "begin", "end", "allDay","description"};
    	        Cursor eventCursor = context.getContentResolver().query(builder.build(), projectionEvents, "Calendars._id=" + calId, null, "startDay ASC, startMinute ASC");    		    
    	        if (eventCursor.moveToFirst()) {
    	        	do {
	    		    	String title = eventCursor.getString(eventCursor.getColumnIndex("title"));
	    		    	
	    		    	Calendar begin = Calendar.getInstance();
	    		    	begin.setTimeInMillis(eventCursor.getLong(eventCursor.getColumnIndex("begin")));
	    		    	
	    		    	Calendar end = Calendar.getInstance();
	    		    	end.setTimeInMillis(eventCursor.getLong(eventCursor.getColumnIndex("end")));
	    		    	
	    		    	Boolean allDay = !eventCursor.getString(eventCursor.getColumnIndex("allDay")).equals("0");
	    		    	
	    		    	String description = eventCursor.getString(eventCursor.getColumnIndex("description"));
	    		    	
	    		    	Log.d(TAG, "Title: " + title + " Begin: " + calendarToString(begin) + " End: " + calendarToString(end) + " All Day: " + allDay);
	    		    	if ((description != null) && (!"".equals(description))) {
	    		    		Log.d(TAG, "Description: " + description);
	    		    	}

	    		    	if (ignoreAllDayEvents && allDay) {
	    		    		Log.d(TAG, "ignoreAllDayEvents");
	    		    		continue;
	    		    	}
	    		    	
	    		    	if (ignoreSpanOverDaysEvents) {
	    		    		if (begin.get(Calendar.DAY_OF_MONTH) != end.get(Calendar.DAY_OF_MONTH)) {
		    		    		Log.d(TAG, "ignoreLongEvents");
	    		    			continue;
	    		    		}
	    		    	}
	    		    	
	    		    	if (hasSubject(title)) {
	    		    		Log.d(TAG, "We have the right title");
	    		    		isInMeeting = hasMeeting(begin, end, allDay);
	    		    		if (isInMeeting) {
		    		    		Log.d(TAG, "We have a meeting");
		    		    		
	    		    			triggerNotification(title, calendarToString(begin), calendarToString(end), description);
		    		    		if (!isPhoneSilent) {
		    		    			turnOffSound();
		    		    			isPhoneSilent = true;
		    		    		}
		    		    
		    		    		nextSchedule = end;
		    		    		Log.d(TAG, "End schedule: " + calendarToString(nextSchedule));
		    		    	} else {
		    		    		if (begin.before(nextSchedule)) {
		    		    			nextSchedule = begin;
			    		    		Log.d(TAG, "Begin schedule: " + calendarToString(nextSchedule));
		    		    		}
		    		    	}
	    		    	}
    	        	} while (eventCursor.moveToNext() && !isInMeeting);
    	        }
    	        
    	        if (eventCursor != null) {
    	        	eventCursor.close();
    	        }
    		} while (calendarCursor.moveToNext() && !isInMeeting);
    	}
    	
    	if (calendarCursor != null) {
    		calendarCursor.close();
    	}

    	
    	if (!isInMeeting) {
    		if (isPhoneSilent) {
    			cancelNotification();
    			turnOnSound();
    		}
    	}
    }
    
    private boolean hasMeeting(Calendar begin, Calendar end, boolean allDay) {
		Log.d(TAG, "hasMeeting called");

		Calendar now = Calendar.getInstance();
    	now.set(Calendar.SECOND, 0);
    	
    	Log.d(TAG, "BEGIN: " + calendarToString(begin) + ", " + begin.getTimeInMillis());
    	Log.d(TAG, "NOW: " + calendarToString(now) + ", " + now.getTimeInMillis());
    	Log.d(TAG, "END: " + calendarToString(end) + ", " + end.getTimeInMillis());
    	
    	if ((now.getTimeInMillis() >= begin.getTimeInMillis()) && (now.getTimeInMillis() < end.getTimeInMillis())) {
    		return true;
    	}
    	
    	return false;
    }
    
    private boolean hasSubject(String title) {
    	boolean hasSubject = true;
//    	if (title.toLowerCase().contains("test")) {
//    		hasSubject = true;
//    	}
    	
    	return hasSubject;
    }
    
    private void turnOffSound() {
		Log.d(TAG, "turnOffSound called");
		setSharedPreference("isPhoneSilent", true);
    	
    	final AudioManager mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    	setSharedPreference("ringerMode", mAudioManager.getRingerMode());
    	setSharedPreference("vibrateTypeNotification", mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION));
    	setSharedPreference("vibrateTypeRinger", mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER));
    	
    	boolean soundPref = getSharedPreference("soundPref", true);
    	boolean vibratePref = getSharedPreference("vibratePref", true);
    	
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//    	if (soundPref && vibratePref) {
//    		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//    	} else if (soundPref) {
//    		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//    	} else if (vibratePref) {
//    		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//    	}
    }
    
    private void turnOnSound() {
		Log.d(TAG, "turnOnSound called");
		setSharedPreference("isPhoneSilent", false);
    	
    	final AudioManager mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    	mAudioManager.setRingerMode(getSharedPreference("ringerMode", AudioManager.RINGER_MODE_NORMAL));
    	mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, getSharedPreference("vibrateTypeNotification", AudioManager.VIBRATE_SETTING_ON));
    	mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, getSharedPreference("vibrateTypeRinger",  AudioManager.VIBRATE_SETTING_ON));
    }
    
    private void triggerNotification(String title, String begin, String end, String description) {
        CharSequence notificationTitle = " In Meeting";
        CharSequence notificationMessage = "Phone is in silence until " + end;
 
        Intent notificationIntent = new Intent(context, MeetingSilenceDialogActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Needed when calling from Service
        notificationIntent.putExtra("title", title);
        notificationIntent.putExtra("time", begin + " - " + end);
        notificationIntent.putExtra("description", description);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
 
        final NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.mute, "Phone is in silence until " + end, System.currentTimeMillis());
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT | Notification.FLAG_ONLY_ALERT_ONCE;
        notification.setLatestEventInfo(context, notificationTitle, notificationMessage, pendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID, notification);
        
        Log.d(TAG, "notificationMessage" + notificationMessage);
    }
    
    private void cancelNotification() {
        final NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }
    
    private String calendarToString(Calendar calendar) {
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append((hour < 10 ? ("0" + hour) : hour));
		sBuilder.append(":");
		sBuilder.append((minute < 10 ? ("0" + minute) : minute));
		
		return sBuilder.toString();
    }
    
    private boolean getSharedPreference(String key, boolean defaultValue) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(key, defaultValue);
    }
    
    private void setSharedPreference(String key, boolean value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
    }
    
    private int getSharedPreference(String key, int defaultValue) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getInt(key, defaultValue);
    }
    
    private void setSharedPreference(String key, int value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
    }
    
    private String getSharedPreference(String key, String defaultValue) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(key, defaultValue);
    }
    
    private void setSharedPreference(String key, String value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
    }
}