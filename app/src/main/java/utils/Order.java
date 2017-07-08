package utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bunny on 12/06/17.
 */

public class Order implements Serializable ,Parcelable{

    private String saloonID ;
    private String userID;
    private String serviceID;
    private String orderID;
    private int orderStatus;
    private String saloonName ;
    private long orderTime = 0 ;
    private int orderPrice =0 ;
    private String orderServiceName ="" ;
    private Map<String,String> orderServiceIDList =new HashMap<String, String>();
    private int orderTotalServiceCount;

    private long orderBookingTime ;

    private String userName , userPhoneNumber;



    public Order() {
    }


    public String getSaloonID() {
        return saloonID;
    }

    public void setSaloonID(String saloonID) {
        this.saloonID = saloonID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSaloonName() {
        return saloonName;
    }

    public void setSaloonName(String saloonName) {
        this.saloonName = saloonName;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderServiceName() {
        return orderServiceName;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setOrderServiceName(String orderServiceName) {
        this.orderServiceName = orderServiceName;
    }

    public Map<String, String> getOrderServiceIDList() {
        return orderServiceIDList;
    }

    public void setOrderServiceIDList(Map<String, String> orderServiceIDList) {
        this.orderServiceIDList = orderServiceIDList;
    }

    public int getOrderTotalServiceCount() {
        return orderTotalServiceCount;
    }

    public void setOrderTotalServiceCount(int orderTotalServiceCount) {
        this.orderTotalServiceCount = orderTotalServiceCount;
    }

    public long getOrderBookingTime() {
        return orderBookingTime;
    }

    public void setOrderBookingTime(long orderBookingTime) {
        this.orderBookingTime = orderBookingTime;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String resolveOrderStatus() {
        if (orderStatus == 1){
            return "Placed";
        } else if (orderStatus ==2) {
            return "Accepted";
        }else if(orderStatus == 3){
            return "completed";
        }else if(orderStatus==-1){
            return "Cancel";
        } else{
            return "";
        }
    }

    public String resolveOrderDate() {
         String dateFormat = "dd/MM/yyyy  hh:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        Date date = new Date();
        date.setTime(orderTime);
        return simpleDateFormat.format(date);

    }

    public String resolveOrderServiceList(){

        String serviceList ="";

        for (String string : getOrderServiceIDList().values()){
            serviceList =serviceList+"* "+string+"\n";

        }

        return serviceList;


    }


    //parcell implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(orderPrice);
        dest.writeInt(orderStatus);
        dest.writeLong(orderTime);

        dest.writeString(saloonName);
        dest.writeString(orderServiceName);
        dest.writeString(orderID);
        dest.writeString(saloonID);
        dest.writeString(serviceID);
        dest.writeString(userID);



    }

    public Order(Parcel source) {
        orderPrice = source.readInt();
        orderStatus = source.readInt();
        orderTime = source.readLong();

        saloonName = source.readString();
        orderServiceName = source.readString();
        orderID = source.readString();
        saloonID = source.readString();
        serviceID = source.readString();
        userID = source.readString();

    }
    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }

        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }
    };


    //parcel imp over

}
