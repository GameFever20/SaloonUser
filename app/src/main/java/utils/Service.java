package utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by bunny on 12/06/17.
 */

public class Service implements Serializable ,Parcelable {

    String saloonName ;
    String serviceUID ;
    String saloonUID ;

    String serviceName ;//input
    String serviceDuration ;
    String serviceDescription ;
    String serviceTypeName ;
    String serviceSubTypeName;

    int servicePrice;
    int serviceOfferPrice;
    int serviceType ;
    int serviceSubType;

    boolean selected;




    public Service() {
    }

    public String getSaloonUID() {
        return saloonUID;
    }

    public void setSaloonUID(String saloonUID) {
        this.saloonUID = saloonUID;
    }

    public String getServiceUID() {
        return serviceUID;
    }

    public void setServiceUID(String serviceUID) {
        this.serviceUID = serviceUID;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public int getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(int servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(String serviceDuration) {
        this.serviceDuration = serviceDuration;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getSaloonName() {
        return saloonName;
    }

    public void setSaloonName(String saloonName) {
        this.saloonName = saloonName;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public int getServiceOfferPrice() {
        return serviceOfferPrice;
    }

    public void setServiceOfferPrice(int serviceOfferPrice) {
        this.serviceOfferPrice = serviceOfferPrice;
    }


    public String getServiceSubTypeName() {
        return serviceSubTypeName;
    }

    public void setServiceSubTypeName(String serviceSubTypeName) {
        this.serviceSubTypeName = serviceSubTypeName;
    }

    public int getServiceSubType() {
        return serviceSubType;
    }

    public void setServiceSubType(int serviceSubType) {
        this.serviceSubType = serviceSubType;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    //parcel start
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(serviceName);
        dest.writeString(serviceUID);
        dest.writeString(saloonUID);
        dest.writeString(saloonName);
        dest.writeString(serviceTypeName);

        dest.writeInt(servicePrice);
        dest.writeInt(serviceOfferPrice);
        dest.writeInt(serviceType);








    }

    public Service(Parcel source) {
        serviceName = source.readString();
        serviceUID = source.readString();
        saloonUID = source.readString();
        saloonName = source.readString();
        serviceTypeName = source.readString();

        servicePrice = source.readInt();
        serviceOfferPrice =source.readInt();
        serviceType = source.readInt();



    }
    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }

        @Override
        public Service createFromParcel(Parcel source) {
            return new Service(source);
        }
    };

    public static Creator<Service> getCREATOR() {
        return CREATOR;
    }

    //parcel imp over



}
