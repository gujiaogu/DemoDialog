package com.gujiao.demodialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import com.fasterxml.jackson.core.JsonGenerator;


public class DialogActivity extends Activity {

    private final static String ACTION_START = "android.intent.action.myaction";

    private Button button;
    private Button btnPick;
    private Button btnStartAnother;
    private Button btnStartNull;
    private MyService.MyBinder myBinder;
    private MyService myService;
    private JsonGenerator generator = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Log.d("=====", "=====666666");
            }
        }
    };

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name1, IBinder service) {
            myBinder = (MyService.MyBinder) service;
            myService = (MyService) myBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] choices = new String[]{"Download from network", "Select from Album"};
                Context context = new ContextThemeWrapper(DialogActivity.this, android.R.style.Theme_DeviceDefault);
                ListAdapter adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, choices);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit Album art");
                builder.setSingleChoiceItems(adapter, 0, null);
                builder.show();
            }
        });

        btnPick = (Button) findViewById(R.id.action_pick);
        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                startActivity(intent);
            }
        });

        btnStartAnother = (Button) findViewById(R.id.start_another);
        btnStartAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ACTION_START);
                //intent.setPackage("com.gujiao.anotherapp");
                if (intent.resolveActivity(getPackageManager()) != null ) {
                    startActivity(intent);
                }
            }
        });

        btnStartNull = (Button) findViewById(R.id.start_null);
        btnStartNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                Intent serviceIntent = new Intent(DialogActivity.this, MyService.class);
                bindService(serviceIntent, sc, Context.BIND_AUTO_CREATE);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                //do something
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dialog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendNotification(View view) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_notification);
        builder.setContentTitle("haha");
        builder.setContentText("666666666666");
        Intent intent = new Intent(this, DialogActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        //builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(111, builder.build());
    }
}
