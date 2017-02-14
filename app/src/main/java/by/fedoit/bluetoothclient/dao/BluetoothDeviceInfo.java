package by.fedoit.bluetoothclient.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kostya on 13.02.2017.
 */

public class BluetoothDeviceInfo {
    @SerializedName("time")
    private String time;

    @SerializedName("source_name")
    private String sourceName;

    @SerializedName("source_mac")
    private String sourceMacAddress;

    @SerializedName("dest_name")
    private String destinationName;

    @SerializedName("dest_mac")
    private String destinationMacAddress;

    @SerializedName("rssi")
    private int rssi;

    public String getDestinationMacAddress() {
        return destinationMacAddress;
    }

    public void setDestinationMacAddress(String destinationMacAddress) {
        this.destinationMacAddress = destinationMacAddress;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getSourceMacAddress() {
        return sourceMacAddress;
    }

    public void setSourceMacAddress(String sourceMacAddress) {
        this.sourceMacAddress = sourceMacAddress;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "time: " + time +
                "; sname: " + sourceName +
                "; saddr: " + sourceMacAddress +
                "; dname: " + destinationName +
                "; daddr: " + destinationMacAddress +
                "; rssi: " + rssi;
    }
}
