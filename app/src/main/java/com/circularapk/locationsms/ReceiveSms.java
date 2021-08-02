package com.circularapk.locationsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.android.gms.location.LocationSettingsRequest;

public class ReceiveSms extends BroadcastReceiver {

    String strMessageNo = "";
    String strMessageBody = "";

    private static final String TAG = ReceiveSms.class.getSimpleName();

    LocationManager locationManager ;
    boolean GpsStatus ;
    private LocationSettingsRequest.Builder builder;
    private final int REQUEST_CHECK=8989;

    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;

        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get("pdus");
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM =
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                // Build the message to show.
                strMessageNo =msgs[i].getOriginatingAddress();
                DataSMS.num=strMessageNo;
                strMessageBody =msgs[i].getMessageBody();
                DataSMS.msgBody=strMessageBody;
                MainActivity.getInstance().run();
                // Log and display the SMS message.
                Log.d(TAG, "onReceive: " + strMessageNo + "  " + strMessageBody);
                locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
                assert locationManager != null;
                GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if(GpsStatus == true) {
                } else {
                    context.sendBroadcast(new Intent("GPS_DISABLED"));
                }
            }
        }
    }



}
