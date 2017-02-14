package by.fedoit.bluetoothclient.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kostya on 24.01.2017.
 */

/**
 * Data requests to server
 * you can use it if you have session id
 */
public interface DataService {
    String PARKING_OPERATION_RESERVE_START = "book_parking";
    String PARKING_OPERATION_RESERVE_CANCEL = "book_cancel";
    String PARKING_OPERATION_FREE_PLACES = "free_places";
    String GATE_OPERATION_OPEN = "open_gate";
    String GATE_OPERATION_CHECK_STATUS = "check_online";
    String PARKING_OPERATION_RESERVATION_TIME = "booking_timer";


    // example: https://178.124.147.109:44555/index.php?parking_id=8&template=&pd=&md=application&inst=
    /**
     * Request for HTML page from server
     * DON'T FORGET TO CLOSE ResponseBody!!!
     * @param parkingId parking id on server
     * @param template idk
     * @param pd idk
     * @param md idk
     * @param inst idk
     * @return raw response body
     */
    @GET("/")
    Call<ResponseBody> getHTMLPage(
            @Query("parking_id") int parkingId,
            @Query("template") String template,
            @Query("pd") String pd,
            @Query("md") String md,
            @Query("inst") String inst
    );

    // https://178.124.147.109:44555/index.php?pd=&md=application&inst=&&ajax=1&op=open_gate&parking_id=7&gate_id=34
    /**
     * Request for HTML page from server
     * DON'T FORGET TO CLOSE ResponseBody!!!
     * @param pd idk
     * @param md idk
     * @param inst idk
     * @param ajax ajax
     * @param op operation name
     * @param parkingId parking id from server
     * @param gateId gate id form server
     * @return raw response body
     */
    @GET("/index.php")
    Call<ResponseBody> gateOperation(
            @Query("pd") String pd,
            @Query("md") String md,
            @Query("inst") String inst,
            @Query("ajax") int ajax,
            @Query("op") String op,
            @Query("parking_id") int parkingId,
            @Query("gate_id") int gateId
    );


    // https://178.124.147.109:44555/index.php?pd=&md=application&inst=&&ajax=1&op=booking_timer&parking_id=8
    /**
     * Request for HTML page from server
     * DON'T FORGET TO CLOSE ResponseBody!!!
     * @param pd idk
     * @param md idk
     * @param inst idk
     * @param ajax ajax
     * @param op operation name
     * @param parkingId parking id from server
     * @return raw response body
     */
    @GET("/index.php")
    Call<ResponseBody> parkingOperation(
            @Query("pd") String pd,
            @Query("md") String md,
            @Query("inst") String inst,
            @Query("ajax") int ajax,
            @Query("op") String op,
            @Query("parking_id") int parkingId
    );

    @GET("/")
    Call<Void> sendBTData(
            @Query("data") String json
    );

}
