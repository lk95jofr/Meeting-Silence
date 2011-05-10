package noip.toonsnet.app;

import java.util.Calendar;

import noip.toonsnet.app.preference.ListPreferenceMultiSelect;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class MeetingSilenceReceiver extends BroadcastReceiver {
    private static final String TAG = "MeetingSilenceReceiver";
    
    private Context context;
    
	private Calendar nextSchedule;
	
	private MeetingSilence meetingSilence = null;
	
	@Override
	public void onReceive(Context mContext, Intent intent) {
		context = mContext;
		
		meetingSilence = new MeetingSilence(context);
		
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			nextSchedule = Calendar.getInstance();
			nextSchedule.add(Calendar.MINUTE, 2); // Wait 2 min for boot to complete
		} else if (intent.getAction().equals("noip.toonsnet.app.MeetingSilenceReceiver")) {
			iterateCalendars();
		}
		
		// To remove, only debug
		Toast.makeText(context, intent.getAction() + ", " + meetingSilence.calendarToString(nextSchedule), Toast.LENGTH_LONG).show();
		meetingSilence.setAlarmNotification(nextSchedule);
	}
	
	private void iterateCalendars() {
//		Log.d(TAG, "iterateCalendars called");
    	
    	boolean isInMeeting = false;
    	boolean isPhoneSilent = meetingSilence.isPhoneSilent();
    	boolean ignoreSpanOverDaysEvents = meetingSilence.getSharedPreference("ignoreSpanOverDaysEventsPref", true);
    	boolean ignoreAllDayEvents = meetingSilence.getSharedPreference("ignoreAllDayEventPref", true);
    	boolean hasTimeAndDay = !meetingSilence.getSharedPreference("allDayPref", false);
    	
    	if (!hasTimeAndDay) {
	    	String startPref = meetingSilence.getSharedPreference("startPref", "8.00");
	    	startPref = startPref.replace(":", ".");
	    	String endPref = meetingSilence.getSharedPreference("endPref", "18.00");
	    	endPref = endPref.replace(":", ".");
	    	
	    	String[] startTime = startPref.split(".");
	    	String[] endTime = endPref.split(".");
	    	
	    	Calendar cStartTime = Calendar.getInstance();
	    	try {
	    		cStartTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTime[0]));
	    		cStartTime.set(Calendar.MINUTE, Integer.parseInt(startTime[1]));
	    	} catch (ArrayIndexOutOfBoundsException e) {
	    		hasTimeAndDay = true;
	    	} catch (NumberFormatException e) {
	    		hasTimeAndDay = true;
	    	}
	    	
	    	Calendar cEndTime = Calendar.getInstance();
	    	try {
	    		cEndTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTime[0]));
	    		cEndTime.set(Calendar.MINUTE, Integer.parseInt(endTime[1]));
	    	} catch (ArrayIndexOutOfBoundsException e) {
	    		// If this fails we iterate
	    		hasTimeAndDay = true;
	    	} catch (NumberFormatException e) {
	    		// If this fails we iterate
	    		hasTimeAndDay = true;
	    	}
	    	
			Calendar now = Calendar.getInstance();
	    	now.set(Calendar.SECOND, 0);
	    	
	    	Log.d(TAG, "START: " + meetingSilence.calendarToString(cStartTime) + ", " + cStartTime.getTimeInMillis());
	    	Log.d(TAG, "NOW: " + meetingSilence.calendarToString(now) + ", " + now.getTimeInMillis());
	    	Log.d(TAG, "END: " + meetingSilence.calendarToString(cEndTime) + ", " + cEndTime.getTimeInMillis());
	    	
	    	if ((now.getTimeInMillis() >= cStartTime.getTimeInMillis()) && (now.getTimeInMillis() < cEndTime.getTimeInMillis())) {
	    		Log.d(TAG, "We have the right time");
		    	String value = meetingSilence.getSharedPreference("multiListPref", "");
		    	String[] vals = ListPreferenceMultiSelect.parseStoredValue(value);
		    	if (vals != null) {
		        	for (int i = 0; i < vals.length; i++) {
		        		int val = Integer.parseInt(vals[i].trim());
		        		if (now.get(Calendar.DAY_OF_WEEK) == val) {
		    	    		Log.d(TAG, "We have the right day");
		        			hasTimeAndDay = true;
		        			break;
		        		}
		        	}
		    	}
	    	}
    	}
    	
		Log.d(TAG, "hasTimeAndDay: " + hasTimeAndDay);
    	
		nextSchedule = Calendar.getInstance();
		nextSchedule.add(Calendar.MINUTE, 5); // Next schedule is now + 5 min
		
		Log.d(TAG, "Possible schedule: " + meetingSilence.calendarToString(nextSchedule));
		
		if (hasTimeAndDay) {
	    	Cursor calendarCursor = meetingSilence.getCalendarCursor();
	    	if (calendarCursor.moveToFirst()) {
	    		String calName = "";
	    		String calId = "";
	    		int nameColumn = calendarCursor.getColumnIndex("name");
	    		int idColumn = calendarCursor.getColumnIndex("_id");
	    		
	    		do {
	    			calName = calendarCursor.getString(nameColumn);
	    		    if (calName == null) { // Exchange always null??? And only exchange???
	    		    	calName = "Exchange";
	    		    }
	    		    
	    		    calId = calendarCursor.getString(idColumn);
//	    		    Log.d(TAG, "Name: " + calName + ", ID: " + calId);
	    		    
	    	        Cursor eventCursor = meetingSilence.getEventCursor(calId);
	    	        if (eventCursor.moveToFirst()) {
	    	        	do {
		    		    	String title = eventCursor.getString(eventCursor.getColumnIndex("title"));
		    		    	if (title == null) {
		    		    		title = "";
		    		    	}
		    		    	
		    		    	Calendar begin = Calendar.getInstance();
		    		    	begin.setTimeInMillis(eventCursor.getLong(eventCursor.getColumnIndex("begin")));
		    		    	
		    		    	Calendar end = Calendar.getInstance();
		    		    	end.setTimeInMillis(eventCursor.getLong(eventCursor.getColumnIndex("end")));
		    		    	
		    		    	Boolean allDay = !eventCursor.getString(eventCursor.getColumnIndex("allDay")).equals("0");
		    		    	
		    		    	String description = eventCursor.getString(eventCursor.getColumnIndex("description"));
		    		    	if (description == null) {
		    		    		description = "";
		    		    	}
		    		    	
		    		    	String eventLocation = eventCursor.getString(eventCursor.getColumnIndex("eventLocation"));
		    		    	if (eventLocation == null) {
		    		    		eventLocation = "";
		    		    	}
		    		    	
		    		    	Log.d(TAG, "Title: " + title + " Begin: " + meetingSilence.calendarToString(begin) + " End: " + meetingSilence.calendarToString(end) + " All Day: " + allDay + " Location: " + eventLocation);
		    		    	Log.d(TAG, "Description: " + description);
	
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
		    		    	
		    		    	boolean bKeywords = meetingSilence.getSharedPreference("keywordPref", true);
		    		    	boolean bLocation = meetingSilence.getSharedPreference("locationPref", false);
		    		    	if ((bKeywords && hasSubject(title)) || (bLocation && hasEventLocation(eventLocation))) {
		    		    		Log.d(TAG, "We have the right title or location");
		    		    		isInMeeting = hasMeeting(begin, end, allDay);
		    		    		if (isInMeeting) {
		    		    			Log.d(TAG, "We have a meeting");
				    		    		
		    		    			meetingSilence.triggerNotification(title, meetingSilence.calendarToString(begin), meetingSilence.calendarToString(end), description);
		    		    			if (!isPhoneSilent) {
		    		    				meetingSilence.turnOffSound();
		    		        			isPhoneSilent = true;
		    		    			}
				    		    
		    		    			nextSchedule = end;
		    		    			Log.d(TAG, "End schedule: " + meetingSilence.calendarToString(nextSchedule));
		    		    		} else {
		    		    			if (begin.before(nextSchedule)) {
		    		    				nextSchedule = begin;
		    		    				Log.d(TAG, "Begin schedule: " + meetingSilence.calendarToString(nextSchedule));
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
		}
		
    	if (!isInMeeting) {
    		if (isPhoneSilent) {
    			meetingSilence.cancelNotification();
    			meetingSilence.turnOnSound();
    			isPhoneSilent = false;
    		}
    	}
    	
    	meetingSilence.setIsPhoneSilent(isPhoneSilent);
    }
    
    private boolean hasMeeting(Calendar begin, Calendar end, boolean allDay) {
//		Log.d(TAG, "hasMeeting called");

		Calendar now = Calendar.getInstance();
    	now.set(Calendar.SECOND, 0);
    	
    	Log.d(TAG, "BEGIN: " + meetingSilence.calendarToString(begin) + ", " + begin.getTimeInMillis());
    	Log.d(TAG, "NOW: " + meetingSilence.calendarToString(now) + ", " + now.getTimeInMillis());
    	Log.d(TAG, "END: " + meetingSilence.calendarToString(end) + ", " + end.getTimeInMillis());
    	
    	if ((now.getTimeInMillis() >= begin.getTimeInMillis()) && (now.getTimeInMillis() < end.getTimeInMillis())) {
    		return true;
    	}
    	
    	return false;
    }
    
    private boolean hasSubject(String mTitle) {
//		Log.d(TAG, "hasSubject called");
		
    	boolean hasSubject = false;
    	String title = mTitle.toLowerCase();
    	String rule = meetingSilence.getSharedPreference("rulePref", "");
    	rule = rule.toLowerCase();
    	
    	if ("".equals(rule)) {
    		hasSubject = true;
    	} else {
        	String[] ruleArr = rule.split(";");
        	for (String word : ruleArr) {
        		if ("".equals(word.trim())) {
        			continue;
        		}
        		
        		if (title.trim().contains(word.trim())) {
            		hasSubject = true;
            		break;
        		}
        	}
    	}
    	
//    	Log.d(TAG, "hasSubject: " + hasSubject);
    	return hasSubject;
    }
    
    private boolean hasEventLocation(String mLocation) {
//		Log.d(TAG, "hasEventLocation called");
		
    	boolean hasLocation = false;
    	String location = mLocation.toLowerCase();
    	String eventLocationPref = meetingSilence.getSharedPreference("eventLocationPref", "");
    	eventLocationPref = eventLocationPref.toLowerCase();
    	
    	if ("".equals(eventLocationPref)) {
    		hasLocation = true;
    	} else {
        	String[] locationArr = eventLocationPref.split(";");
        	for (String word : locationArr) {
        		if ("".equals(word.trim())) {
        			continue;
        		}
        		
        		if (location.trim().contains(word.trim())) {
            		hasLocation = true;
            		break;
        		}
        	}
    	}
    	
//    	Log.d(TAG, "hasLocation: " + hasLocation);
    	return hasLocation;
    }
}