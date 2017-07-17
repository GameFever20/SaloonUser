package app.saloonuser.craftystudio.saloonuser;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Visibility;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import utils.ClickListener;
import utils.FireBaseHandler;
import utils.Order;
import utils.OrderAdapter;
import utils.RecyclerTouchListener;
import utils.SaloonAdapter;

public class UserOrderActivity extends AppCompatActivity {

    private TextView mTextMessage;
    RecyclerView mUserOrderRecyclerview;
    private OrderAdapter mAdapter;
    //private OrderAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    FireBaseHandler fireBaseHandler;

    ArrayList<Order> mUserOrderArraylist;

    ArrayList<Order> mTempOrderArraylist = new ArrayList<>();

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            //getting window component
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } catch (Exception e) {

        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            //animation
            Explode explode = new Explode();
            explode.setDuration(500);
            explode.setMode(Visibility.MODE_IN);
            getWindow().setEnterTransition(explode);
            getWindow().setAllowEnterTransitionOverlap(true);


        } else {
            // do something for phones running an SDK before lollipop
        }


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        mUserOrderRecyclerview = (RecyclerView) findViewById(R.id.userOrder_order_recyclerview);


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mUserOrderRecyclerview.setLayoutManager(mLayoutManager);

        mUserOrderRecyclerview.addOnItemTouchListener(new RecyclerTouchListener(this, mUserOrderRecyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(UserOrderActivity.this, OrderDetailActivity.class);
                intent.putExtra("order", mTempOrderArraylist.get(position));
                OrderDetailActivity.order = mTempOrderArraylist.get(position);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        progressDialog = new ProgressDialog(this);
        showProgressDialog("Fetching orders");

        fireBaseHandler = new FireBaseHandler();
        fireBaseHandler.downloadOrderList(LoginActivity.USER.getUserUID(), 20, new FireBaseHandler.OnOrderListener() {
            @Override
            public void onOrderUpload(boolean isSuccessful) {

            }

            @Override
            public void onOrderDownload(Order order, boolean isSuccessful) {

            }

            @Override
            public void onOrderListDownload(ArrayList<Order> orderArrayList, boolean isSuccessful) {
                if (isSuccessful) {

                    closeProgressDialog();


                    if (orderArrayList != null && orderArrayList.size() > 0) {
                        Collections.reverse(orderArrayList);
                        mUserOrderArraylist = orderArrayList;
                        Toast.makeText(UserOrderActivity.this, "Order fetched ", Toast.LENGTH_SHORT).show();
                        setBookingOrderList();
                    } else {
                        Toast.makeText(UserOrderActivity.this, "No Order ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(UserOrderActivity.this, "No order Found", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void openGPSDirection() {
       /*
        String uri = "http://maps.google.com/maps?daddr=" + 23f + "," + 41f + " (" + "Parlour Location" + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(UserOrderActivity.this, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
        */
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();

        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setBookingOrderList();
                    return true;
                case R.id.navigation_dashboard:
                    setPendingOrderList();
                    return true;
                case R.id.navigation_notifications:
                    setCompletedOrderList();
                    return true;
            }
            return false;

        }

    };


    public void setCompletedOrderList() {
        mTempOrderArraylist = new ArrayList<>();
        for (Order order : mUserOrderArraylist) {
            if (order.getOrderStatus() == 3) {
                mTempOrderArraylist.add(order);
            }
        }
        mAdapter = new OrderAdapter(mTempOrderArraylist);
        mUserOrderRecyclerview.setAdapter(mAdapter);

        //Reverse a arraylist
        mAdapter.notifyDataSetChanged();


    }

    public void setBookingOrderList() {
        mTempOrderArraylist = new ArrayList<>();
        for (Order order : mUserOrderArraylist) {
            if (order.getOrderStatus() == 2) {
                mTempOrderArraylist.add(order);
            }
        }
        mAdapter = new OrderAdapter(mTempOrderArraylist);
        mUserOrderRecyclerview.setAdapter(mAdapter);

        //Reverse a arraylist
        mAdapter.notifyDataSetChanged();


    }

    private void setPendingOrderList() {
        mTempOrderArraylist = new ArrayList<>();
        for (Order order : mUserOrderArraylist) {
            if (order.getOrderStatus() == -1 || order.getOrderStatus() == 1) {
                mTempOrderArraylist.add(order);
            }
        }
        mAdapter = new OrderAdapter(mTempOrderArraylist);
        mUserOrderRecyclerview.setAdapter(mAdapter);

        //Reverse a arraylist
        mAdapter.notifyDataSetChanged();

    }

    public void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void closeProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
        super.onBackPressed();

    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
}
