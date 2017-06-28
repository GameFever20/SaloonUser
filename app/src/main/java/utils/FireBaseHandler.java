package utils;

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




    //interface
    public interface OnSaloonListListner {

        public void onSaloonList(ArrayList<Saloon> saloonArrayList);

        public void onCancel();
    }

    public interface OnSaloonDownload {
        public void onSaloon(Saloon saloon);

        public void onSaloonValueUploaded(boolean isSucessful);


    }

}
