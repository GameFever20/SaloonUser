package utils;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aisha on 6/28/2017.
 */

public class FireBaseHandler {

    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase mFirebaseDatabase;


    public FireBaseHandler() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();

    }

    public void downloadSaloon(String saloonUID, final OnSaloonDownload onSaloonDownload) {

        mDatabaseRef = mFirebaseDatabase.getReference().child("saloon/" + saloonUID);


        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //String saloonName = dataSnapshot.child("saloonName").getValue(String.class);

                Saloon saloon = dataSnapshot.getValue(Saloon.class);
                onSaloonDownload.onSaloon(saloon);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


    }

    public void downloadSaloonList(int limit, final OnSaloonListListner onSaloonListListner) {

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("saloon");

        Query myref2 = myRef.orderByChild("saloonPoint").limitToLast(limit).startAt(1);

        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Saloon> saloonArrayList = new ArrayList<Saloon>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Saloon saloon = snapshot.getValue(Saloon.class);
                    saloonArrayList.add(saloon);
                }
                onSaloonListListner.onSaloonList(saloonArrayList);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                databaseError.toException().printStackTrace();

                onSaloonListListner.onCancel();
            }
        });


    }

    public void downloadMoreSaloonList(int limit, int lastSaloonPoint, final OnSaloonListListner onSaloonListListner) {

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("saloon");

        //Query myref2 = myRef.limitToLast(limit).endAt(lastOrderID);

        Query myref2 = myRef.orderByChild("saloonPoint").endAt(lastSaloonPoint).limitToLast(limit).startAt(1);


        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Saloon> saloonArrayList = new ArrayList<Saloon>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Saloon saloon = snapshot.getValue(Saloon.class);
                    saloonArrayList.add(saloon);
                }
                onSaloonListListner.onSaloonList(saloonArrayList);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onSaloonListListner.onCancel();
            }
        });


    }

    public void uploadUser(User user, final OnUserlistener onUserlistener) {
        mFirebaseDatabase.getReference().child("user/" + user.getUserUID()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                onUserlistener.onUserUpload(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                onUserlistener.onUserUpload(false);
            }
        });

    }

    public void downloadUser(String userUID, final OnUserlistener onUserlistener) {


        mDatabaseRef = mFirebaseDatabase.getReference().child("user/" + userUID);


        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                User user = dataSnapshot.getValue(User.class);

                onUserlistener.onUserDownLoad(user, true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onUserlistener.onUserUpload(false);


            }
        });


    }

    public void uploadOrder(Order order, final OnOrderListener onOrderListener) {

        String pushKey = mFirebaseDatabase.getReference().child("Orders/" + order.getSaloonID()).push().getKey();


        order.setOrderID(pushKey);
        Map post = new HashMap();
        post.put("Orders/" + order.getSaloonID()+"/"+pushKey, order);
        post.put("userOrders/" + order.getUserID()+"/"+pushKey, order);


        mFirebaseDatabase.getReference().updateChildren(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                onOrderListener.onOrderUpload(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                onOrderListener.onOrderUpload(false);

            }
        });
    }

    public void downloadOrderList(String userUID, int limitTo, final OnOrderListener onOrderListener) {

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("userOrders/" + userUID);

        Query myref2 = myRef.limitToLast(limitTo);
        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Order> orderArrayList = new ArrayList<Order>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Order order = snapshot.getValue(Order.class);
                    order.setOrderID(snapshot.getKey());
                    orderArrayList.add(order);
                }

                onOrderListener.onOrderListDownload(orderArrayList, true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void downloadServiceList(String saloonUID, int limitTo, final OnServiceListener onServiceListener) {

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("services/");

        Query myref2 = myRef.orderByChild("saloonUID").equalTo(saloonUID).limitToLast(limitTo);
        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Service> serviceArrayList = new ArrayList<Service>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Service service = snapshot.getValue(Service.class);
                    if (service != null) {
                        service.setServiceUID(snapshot.getKey());
                    }
                    serviceArrayList.add(service);
                }


                onServiceListener.onServiceList(serviceArrayList, true);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onServiceListener.onServiceList(null, false);

            }
        });

    }


    public void uploadRating(String orderID , final CustomRating customRating , final OnRatingListener onRatingListener){

        if (customRating.getSaloonPoint()<10){
            return;
        }

        Map post = new HashMap();
        post.put("rating/" + customRating.getOrderID(), customRating);
        post.put("saloon/" + customRating.getSaloonUID()+"/saloonPoint", customRating.getSaloonPoint()+customRating.getRating());
        post.put("saloon/" + customRating.getSaloonUID()+"/saloonRatingSum", customRating.getSaloonRatingSum()+customRating.getRating());
        post.put("saloon/" + customRating.getSaloonUID()+"/saloonTotalRating", customRating.getSaloonTotalRating()+5);



        mFirebaseDatabase.getReference().updateChildren(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                onRatingListener.onRatingUploaded(customRating , true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                onRatingListener.onRatingUploaded(null , false);

            }
        });
    }

    //interface
    public interface OnSaloonListListner {

        public void onSaloonList(ArrayList<Saloon> saloonArrayList);

        public void onCancel();
    }

    public interface OnSaloonDownload {
        public void onSaloon(Saloon saloon);

        public void onSaloonValueUploaded(boolean isSucessful);


    }

    public interface OnUserlistener {


        public void onUserDownLoad(User user, boolean isSuccessful);

        public void onUserUpload(boolean isSuccessful);
    }

    public interface OnOrderListener {
        public void onOrderUpload(boolean isSuccessful);

        public void onOrderDownload(Order order, boolean isSuccessful);

        public void onOrderListDownload(ArrayList<Order> orderArrayList, boolean isSuccessful);

    }

    public interface OnServiceListener {
        public void onSeviceUpload(boolean isSuccesful);

        public void onServiceList(ArrayList<Service> serviceArrayList, boolean isSuccesful);
    }

    public interface OnRatingListener{
        public void onRatingUploaded(CustomRating customRating , boolean isSuccessful);
    }


}
