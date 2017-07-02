package app.saloonuser.craftystudio.saloonuser;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import utils.FireBaseHandler;
import utils.Order;
import utils.OrderAdapter;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mUserOrderRecyclerview = (RecyclerView) findViewById(R.id.userOrder_order_recyclerview);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mUserOrderRecyclerview.setLayoutManager(mLayoutManager);


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

                    Collections.reverse(orderArrayList);
                    mUserOrderArraylist = orderArrayList;
                    Toast.makeText(UserOrderActivity.this, "Order fetched ", Toast.LENGTH_SHORT).show();
                    setBookingOrderList();


                } else {
                    Toast.makeText(UserOrderActivity.this, "No order Found", Toast.LENGTH_SHORT).show();
                }

            }
        });

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


}
