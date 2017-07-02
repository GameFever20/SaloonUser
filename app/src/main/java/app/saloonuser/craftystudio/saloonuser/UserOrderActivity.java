package app.saloonuser.craftystudio.saloonuser;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

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

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    downloadCompletedOrderList();
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    downloadPendingOrderList();
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    private void downloadCompletedOrderList() {
        fireBaseHandler.downloadOrderList("userUID", 20, new FireBaseHandler.OnOrderListener() {
            @Override
            public void onOrderUpload(boolean isSuccessful) {

            }

            @Override
            public void onOrderDownload(Order order, boolean isSuccessful) {

            }

            @Override
            public void onOrderListDownload(ArrayList<Order> orderArrayList, boolean isSuccessful) {

                int x = orderArrayList.size() - 1;

                for (int i = 0; i <= x; i++) {
                    Order order = orderArrayList.get(i);
                    if (order.getOrderStatus() == 3) {
                        mUserOrderArraylist.add(order);
                    } else {

                    }
                }
                mAdapter = new OrderAdapter(mUserOrderArraylist);
                mUserOrderRecyclerview.setAdapter(mAdapter);

                //Reverse a arraylist
                Collections.reverse(mUserOrderArraylist);
                mAdapter.notifyDataSetChanged();


            }
        });

    }

    private void downloadPendingOrderList() {
        fireBaseHandler.downloadOrderList("userUID", 20, new FireBaseHandler.OnOrderListener() {
            @Override
            public void onOrderUpload(boolean isSuccessful) {

            }

            @Override
            public void onOrderDownload(Order order, boolean isSuccessful) {

            }

            @Override
            public void onOrderListDownload(ArrayList<Order> orderArrayList, boolean isSuccessful) {

                int x = orderArrayList.size() - 1;

                for (int i = 0; i <= x; i++) {
                    Order order = orderArrayList.get(i);
                    if (order.getOrderStatus() == 2) {
                        mUserOrderArraylist.add(order);
                    } else {

                    }
                }

                mAdapter = new OrderAdapter(mUserOrderArraylist);
                mUserOrderRecyclerview.setAdapter(mAdapter);

                //Reverse a arraylist
                Collections.reverse(mUserOrderArraylist);
                mAdapter.notifyDataSetChanged();

            }
        });

    }


}
