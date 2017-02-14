package by.fedoit.bluetoothclient;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.fedoit.bluetoothclient.dao.BluetoothDeviceInfo;
import by.fedoit.bluetoothclient.dao.ClientData;
import by.fedoit.bluetoothclient.network.DataService;
import by.fedoit.bluetoothclient.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<Void> {

    private static final int REQUEST_ENABLE_BT = 44;
    private static final int REQUEST_SET_BT_DISCOVERABLE = 55;
    private static final int REQUEST_LOCATION = 7777;

    private BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDeviceInfo> foundDevices = new ArrayList<>();


    private BroadcastReceiver discoverStartedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.i("BluetoothClient", "Discover started.");
                foundDevices.clear();
                onDiscoverStarted();
            }
        }
    };

    private BroadcastReceiver discoverFinishedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.i("BluetoothClient", "Discover ended.");
                onDiscoverFinished();
            }
        }
    };

    private BroadcastReceiver deviceFoundReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            onDeviceFound(intent);
        }
    };

    // update listview if receive intent
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("MainActivity", "received alarm!");
            onAlarmReceived();
            long newAlarmTime = intent.getLongExtra("newAlarmTime", 0);
            if (newAlarmTime > 0) {
                setupAlarm(newAlarmTime);
            } else {
                Toast.makeText(getBaseContext(), "Alarm failed!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @BindView(R.id.textView)
    TextView textView;

    @TargetApi(24)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                REQUEST_LOCATION);


        registerReceiver(broadcastReceiver, new IntentFilter("mainActivity"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(MainActivity.this, "Location permissions denied.", Toast.LENGTH_SHORT).show();
                } else {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        // If the adapter is null it means that the device does not support Bluetooth
                        finish();
                    } else {
                        if (!mBluetoothAdapter.isEnabled()) {
                            // We need to enable the Bluetooth, so we ask the user
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            // REQUEST_ENABLE_BT es un valor entero que vale 1
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        } else {
                            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
                            startActivityForResult(discoverableIntent, REQUEST_SET_BT_DISCOVERABLE);
                        }

                    }
                }
            }

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivity(discoverableIntent);
        }

        if (requestCode == REQUEST_SET_BT_DISCOVERABLE) {


            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = 00;
            calendar.set(year, month, day, hour, minute);
            setupAlarm(calendar.getTime().getTime());

            // start to search bluetooth devices
            Toast.makeText(this, "BT now discoverable!", Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(24)
    private void setupAlarm(long time) {
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && currentApiVersion < Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pi);
        } else if (currentApiVersion >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pi);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pi);
        }
    }


    @Override
    protected void onResume() {

        // Register the broadcast receivers
        IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(deviceFoundReceiver, foundFilter);

        IntentFilter startFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(discoverStartedReceiver, startFilter);

        IntentFilter finishFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoverFinishedReceiver, finishFilter);

        super.onResume();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(discoverStartedReceiver);
        unregisterReceiver(discoverFinishedReceiver);
        unregisterReceiver(deviceFoundReceiver);
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        super.onStop();
    }

    public void onAlarmReceived() {
        mBluetoothAdapter.startDiscovery();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothAdapter.cancelDiscovery();
            }
        }, 12000);
    }

    private void onDiscoverStarted() {

    }

    private void onDeviceFound(Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // A Bluetooth device was found
            // Getting device information from the intent
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

            BluetoothDeviceInfo bluetoothData = new BluetoothDeviceInfo();
            bluetoothData.setTime(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime().getTime()));
            bluetoothData.setSourceName(mBluetoothAdapter.getName());
            bluetoothData.setSourceMacAddress(mBluetoothAdapter.getAddress());
            bluetoothData.setDestinationName(device.getName());
            bluetoothData.setDestinationMacAddress(device.getAddress());
            bluetoothData.setRssi(rssi);
            foundDevices.add(bluetoothData);

            Log.i("BluetoothClient", device.getName() + "; MAC " + device.getAddress() + "; RSSI = " + rssi);
        }
    }

    private void onDiscoverFinished() {
        showFoundDevices();
        Gson gson = new GsonBuilder().create();
        ClientData clientData = new ClientData();
        clientData.setDevices(foundDevices);
        DataService dataService = ServiceGenerator.createService(DataService.class);
        Call<Void> dataCall = dataService.sendBTData(gson.toJson(clientData, ClientData.class));
        dataCall.enqueue(this);
    }


    private void showFoundDevices() {
        textView.setText("");
        for (BluetoothDeviceInfo device : foundDevices) {
            textView.append(device + "\n");
        }
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        Toast.makeText(this, "bt data sent!", Toast.LENGTH_SHORT).show();
    }
}
