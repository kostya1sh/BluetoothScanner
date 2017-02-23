package by.fedoit.bluetoothclient.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kostya on 23.02.2017.
 */

public class ServerResponse {

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
