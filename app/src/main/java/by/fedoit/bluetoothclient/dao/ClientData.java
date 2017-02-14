package by.fedoit.bluetoothclient.dao;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kostya on 13.02.2017.
 */

public class ClientData {
    @SerializedName("data")
    private List<BluetoothDeviceInfo> devices;

    public List<BluetoothDeviceInfo> getDevices() {
        return devices;
    }

    public void setDevices(List<BluetoothDeviceInfo> devices) {
        this.devices = devices;
    }
}
