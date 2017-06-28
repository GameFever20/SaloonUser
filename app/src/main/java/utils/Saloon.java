package utils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Aisha on 6/28/2017.
 */

public class Saloon implements Serializable {

    private int saloonPoint;

    private String saloonLocation;
    private int saloonRating;
    private String saloonName;
    private boolean saloonUpdated;
    private boolean saloonHirePhotographer;
    private String saloonUID = "";

    private String saloonPhoneNumber;//input
    private String saloonAddress;//input

    double saloonLocationLatitude;
    double saloonLocationLongitude;

    private Map<String, String> saloonImageList;

    //timing
    private int openingTimeHour, openingTimeMinute, closingTimeHour, closingTimeMinute;

    public String getSaloonLocation() {
        return saloonLocation;
    }

    public void setSaloonLocation(String saloonLocation) {
        this.saloonLocation = saloonLocation;
    }

    public int getSaloonPoint() {
        return saloonPoint;
    }

    public void setSaloonPoint(int saloonPoint) {
        this.saloonPoint = saloonPoint;
    }

    public int getSaloonRating() {
        return saloonRating;
    }

    public void setSaloonRating(int saloonRating) {
        this.saloonRating = saloonRating;
    }

    public String getSaloonName() {
        return saloonName;
    }

    public void setSaloonName(String saloonName) {
        this.saloonName = saloonName;
    }

    public boolean isSaloonUpdated() {
        return saloonUpdated;
    }

    public void setSaloonUpdated(boolean saloonUpdated) {
        this.saloonUpdated = saloonUpdated;
    }

    public boolean isSaloonHirePhotographer() {
        return saloonHirePhotographer;
    }

    public void setSaloonHirePhotographer(boolean saloonHirePhotographer) {
        this.saloonHirePhotographer = saloonHirePhotographer;
    }

    public String getSaloonUID() {
        return saloonUID;
    }

    public void setSaloonUID(String saloonUID) {
        this.saloonUID = saloonUID;
    }

    public String getSaloonPhoneNumber() {
        return saloonPhoneNumber;
    }

    public void setSaloonPhoneNumber(String saloonPhoneNumber) {
        this.saloonPhoneNumber = saloonPhoneNumber;
    }

    public String getSaloonAddress() {
        return saloonAddress;
    }

    public void setSaloonAddress(String saloonAddress) {
        this.saloonAddress = saloonAddress;
    }

    public double getSaloonLocationLatitude() {
        return saloonLocationLatitude;
    }

    public void setSaloonLocationLatitude(double saloonLocationLatitude) {
        this.saloonLocationLatitude = saloonLocationLatitude;
    }

    public double getSaloonLocationLongitude() {
        return saloonLocationLongitude;
    }

    public void setSaloonLocationLongitude(double saloonLocationLongitude) {
        this.saloonLocationLongitude = saloonLocationLongitude;
    }

    public Map<String, String> getSaloonImageList() {
        return saloonImageList;
    }

    public void setSaloonImageList(Map<String, String> saloonImageList) {
        this.saloonImageList = saloonImageList;
    }

    public int getOpeningTimeHour() {
        return openingTimeHour;
    }

    public void setOpeningTimeHour(int openingTimeHour) {
        this.openingTimeHour = openingTimeHour;
    }

    public int getOpeningTimeMinute() {
        return openingTimeMinute;
    }

    public void setOpeningTimeMinute(int openingTimeMinute) {
        this.openingTimeMinute = openingTimeMinute;
    }

    public int getClosingTimeHour() {
        return closingTimeHour;
    }

    public void setClosingTimeHour(int closingTimeHour) {
        this.closingTimeHour = closingTimeHour;
    }

    public int getClosingTimeMinute() {
        return closingTimeMinute;
    }

    public void setClosingTimeMinute(int closingTimeMinute) {
        this.closingTimeMinute = closingTimeMinute;
    }


}
