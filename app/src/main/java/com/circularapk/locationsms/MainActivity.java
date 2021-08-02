package com.circularapk.locationsms;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
public class MainActivity extends AppCompatActivity  {
    private static MainActivity instance;
    String batteryLevel="";
    String c1,c2;
    private final int REQUEST_CHECK=1000000;
    private LocationSettingsRequest.Builder builder;
    private Button btn_Save,btn_dlt,btn_show;
    private EditText et_number;
    private TextView contact1,contact2,btryTv;
    private int count=0;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Contact1 = "contact1Key";
    public static final String Contact2 = "contact2Key";
    SharedPreferences sharedpreferences;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            //OnLocation();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(this,ForeGroundService.class);
        registerReceiver(broadcastReceiver, new IntentFilter("GPS_DISABLED"));

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            startForegroundService(intent);
        }
        else
        {
            startService(intent);
        }
        instance = this;
        btn_Save=(Button)findViewById(R.id.btn_Save);
        btn_show=(Button)findViewById(R.id.btn_show);
        et_number=(EditText)findViewById(R.id.et_number);
        btn_dlt = (Button) findViewById(R.id.btn_dlt);
        contact1=(TextView) findViewById(R.id.contact1);
        contact2=(TextView) findViewById(R.id.contact2);
        btryTv=(TextView)findViewById(R.id.btryTv);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_number.getText().toString().isEmpty())
                {
                    et_number.setError("-");
                    Toast.makeText(MainActivity.this,"Please enter contact number!",Toast.LENGTH_LONG).show();
                }
                else {
                    countClick();
                    if (count == 1&&sharedpreferences.getString(Contact1, null) == null) {
                        contact1.setText(et_number.getText().toString());
                        editor.putString(Contact1, et_number.getText().toString());
                        editor.commit();
                        Toast.makeText(MainActivity.this, "Contact 1 Saved", Toast.LENGTH_LONG).show();
                    } else if (count == 2&&sharedpreferences.getString(Contact2, null) == null) {
                        contact2.setText(et_number.getText().toString());
                        editor.putString(Contact2, et_number.getText().toString());
                        editor.commit();
                        Toast.makeText(MainActivity.this, "Contact 2 Saved", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Only two contact numbers will be added!", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
        btn_dlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear().apply();
                contact1.setText("");
                contact2.setText("");
                count=0;

            }
        });
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedpreferences.getString(Contact1, null) == null&&sharedpreferences.getString(Contact2, null) == null)
                {
                    Toast.makeText(MainActivity.this, "List is empty", Toast.LENGTH_LONG).show();
                }
                else {
                    contact1.setText(sharedpreferences.getString(Contact1,null));
                    contact2.setText(sharedpreferences.getString(Contact2,null));
                }
            }
        });

        //
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},1000);
        }

        if (sharedpreferences.getString(Contact1,null)==null)
        {
            c1=null;
            c2=null;
        }
        else {

            c1=sharedpreferences.getString(Contact1,null);
            c2=sharedpreferences.getString(Contact2,null);
        }
    }

    public static MainActivity getInstance() {
        return instance;
    }
    public void run()
    {
        String batt="battery";
        String on="on";
        String off="off";
        boolean result=c1.contentEquals(DataSMS.num);
        boolean result1=c2.contentEquals(DataSMS.num);

        boolean result2=batt.contentEquals(DataSMS.msgBody.trim().toLowerCase());
        boolean result3=on.contentEquals(DataSMS.msgBody.trim().toLowerCase());

        boolean result5=off.equals(DataSMS.msgBody.toLowerCase());
        if (result||result1)
        {
            if (result3)
            {
                try {
                    OnLocation();
                   // smsBtry(DataSMS.num,batteryLevel);
                }
                catch (Exception e)
                {
                    Log.d("Error",e.toString());
                }

            }
            if(result5)
            {
                try {
                    showSettingsAlert();
                    //smsBtry(DataSMS.num,batteryLevel);
                }
                catch (Exception e)
                {
                    showSettingsAlert();
                   // smsBtry(DataSMS.num,batteryLevel);
                }
            }
            else {
                Log.d("Error","oh oh");
            }
            if(result2)
            {
                try {
                    //showSettingsAlert();
                    smsBtry(DataSMS.num,batteryLevel);
                }
                catch (Exception e)
                {
                    //showSettingsAlert();
                     smsBtry(DataSMS.num,batteryLevel);
                }
            }
            else {
                Log.d("Error","oh oh");
            }
        }

        else
        {
            Log.d("Error","oh oh");
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }
    private void countClick() {
        count++;
    }
    private BroadcastReceiver mBatInfoReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level=intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            int scale=intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
            float batteryPct=level*100/(float)scale;
            btryTv.setText("Battery Level: "+String.valueOf(batteryPct)+"%");
            batteryLevel="Battery Level: "+String.valueOf(batteryPct)+"%";

        }
    };
    public void showSettingsAlert() {
        Toast.makeText(MainActivity.this,"OFF the location",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }
    void OnLocation() {
        LocationRequest request=new LocationRequest()
                .setFastestInterval(15000)
                .setInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        builder=new LocationSettingsRequest.Builder().addLocationRequest(request);
        Task<LocationSettingsResponse> result= LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull  Task<LocationSettingsResponse> task)
            {
                try {
                    task.getResult(ApiException.class);
                } catch (ApiException e) {
                    switch (e.getStatusCode())
                    {
                        case LocationSettingsStatusCodes
                                .RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException= (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this,REQUEST_CHECK);
                            } catch (IntentSender.SendIntentException sendIntentException) {
                                sendIntentException.printStackTrace();
                            }
                            catch (ClassCastException ex)
                            {

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        {
                            break;
                        }
                    }
                    e.printStackTrace();
                }
            }
        });

    }
    void smsBtry(String mblNumVar, String smsMsgVar)
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
        {
            try
            {
                SmsManager smsMgrVar = SmsManager.getDefault();
                smsMgrVar.sendTextMessage(mblNumVar, null, smsMsgVar, null, null);
                Toast.makeText(getApplicationContext(), "Message Sent",
                        Toast.LENGTH_LONG).show();
            }
            catch (Exception ErrVar)
            {
                Toast.makeText(getApplicationContext(),ErrVar.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                ErrVar.printStackTrace();
            }
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
            }
        }

    }
}