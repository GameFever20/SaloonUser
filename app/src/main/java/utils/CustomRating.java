package utils;

/**
 * Created by bunny on 08/07/17.
 */

public class CustomRating {

    private int rating ;
    private String orderID ,saloonUID ,userUID ;
    private int saloonPoint , saloonRatingSum , saloonTotalRating;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getSaloonUID() {
        return saloonUID;
    }

    public void setSaloonUID(String saloonUID) {
        this.saloonUID = saloonUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public int getSaloonPoint() {
        return saloonPoint;
    }

    public void setSaloonPoint(int saloonPoint) {
        this.saloonPoint = saloonPoint;
    }

    public int getSaloonRatingSum() {
        return saloonRatingSum;
    }

    public void setSaloonRatingSum(int saloonRatingSum) {
        this.saloonRatingSum = saloonRatingSum;
    }

    public int getSaloonTotalRating() {
        return saloonTotalRating;
    }

    public void setSaloonTotalRating(int saloonTotalRating) {
        this.saloonTotalRating = saloonTotalRating;
    }
}
