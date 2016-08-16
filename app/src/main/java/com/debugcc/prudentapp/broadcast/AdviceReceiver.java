package com.debugcc.prudentapp.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.debugcc.prudentapp.MainActivity;
import com.debugcc.prudentapp.R;
import com.debugcc.prudentapp.db.PrudentAppDB;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AdviceReceiver extends BroadcastReceiver {
    public AdviceReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        PrudentAppDB mDB = new PrudentAppDB(context);
        Cursor c = mDB.selectRecords();

        Calendar mCalendarToday = Calendar.getInstance();
        int day = mCalendarToday.get(Calendar.DAY_OF_MONTH);
        int month = mCalendarToday.get(Calendar.MONTH);
        int year = mCalendarToday.get(Calendar.YEAR);

        Log.e("NOTIFICATION", "onReceive: " + day + " " + month + " "+year );

        int ini_pos;
        long start_date, end_date;

        //while(c.isAfterLast() == false){
        if (c.isAfterLast() == false) {

            start_date = Long.parseLong(c.getString(c.getColumnIndex("start_date")));


            long diff = mCalendarToday.getTimeInMillis() - start_date;
            long diff_days = diff / (24 * 60 * 60 * 1000);

            if(diff >= 0) {
                int pos = (int) (diff_days % 28);

                Intent notificationIntent = new Intent(context, MainActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(notificationIntent);

                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

                builder.setContentTitle("Consejo del Día")
                        .setContentText(context.getResources().getStringArray(R.array.advices)[pos])
                                //.setTicker("New Message Alert!")
                        .setSmallIcon(R.mipmap.ic_launcher);


                /*NotificationCompat.InboxStyle inboxStyle =
                        new NotificationCompat.InboxStyle();
                String[] events = new String[6];
                inboxStyle.setBigContentTitle("Event tracker details:");
                for (int i=0; i < events.length; i++) {

                    inboxStyle.addLine(context.getResources().getStringArray(R.array.advices)[pos]);
                }
                builder.setStyle(inboxStyle);*/
                Notification notification = builder.setContentIntent(pendingIntent).build();

                notification.defaults |= Notification.DEFAULT_SOUND;
                notification.defaults |= Notification.DEFAULT_VIBRATE;

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notification);

            }



        }

    }
}
