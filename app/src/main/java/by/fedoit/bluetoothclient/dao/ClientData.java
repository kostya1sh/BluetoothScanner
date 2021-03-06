package by.fedoit.bluetoothclient.dao;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kostya on 13.02.2017.
 */

public class ClientData {
    @SerializedName("x")
    private float x;

    @SerializedName("y")
    private float y;

    @SerializedName("data")
    private List<BluetoothDeviceInfo> devices;

    public List<BluetoothDeviceInfo> getDevices() {
        return devices;
    }

    public void setDevices(List<BluetoothDeviceInfo> devices) {
        this.devices = devices;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
