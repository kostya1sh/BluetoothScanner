package by.fedoit.bluetoothclient.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kostya on 23.02.2017.
 */

public class ClientInitData {

    @SerializedName("name")
    private String name;

    @SerializedName("mac")
    private String mac;

    @SerializedName("x")
    private float x;

    @SerializedName("y")
    private float y;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
