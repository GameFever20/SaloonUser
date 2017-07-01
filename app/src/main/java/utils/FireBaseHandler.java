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

    public void uploadUser(User user , final OnUserlistener onUserlistener ) {
        mFirebaseDatabase.getReference().child("user/"+user.getUserUID()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void uploadOrder(Order order , final OnOrderListener onOrderListener) {


        mFirebaseDatabase.getReference().child("Orders/"+order.getSaloonID()).push().setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
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


    //interface
    public interface OnSaloonListListner {

        public void onSaloonList(ArrayList<Saloon> saloonArrayList);

        public void onCancel();
    }

    public interface OnSaloonDownload {
        public void onSaloon(Saloon saloon);

        public void onSaloonValueUploaded(boolean isSucessful);


    }

public interface OnUserlistener{
    public void onUserDownLoad(User user ,boolean isSuccessful);

    public void onUserUpload(boolean isSuccessful);
}

public interface OnOrderListener{
    public void onOrderUpload(boolean isSuccessful);

    public void onOrderDownload(Order order , boolean isSuccessful );

    public void onOrderListDownload(ArrayList<Order > orderArrayList , boolean isSuccessful);

}

}
