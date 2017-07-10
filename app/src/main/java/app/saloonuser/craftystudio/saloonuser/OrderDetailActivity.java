package app.saloonuser.craftystudio.saloonuser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import utils.CustomRating;
import utils.FireBaseHandler;
import utils.Order;
import utils.Saloon;
import utils.Service;
import utils.ServiceAdapter;

public class OrderDetailActivity extends AppCompatActivity {

    Order order;
    String orderUID;
    CustomRating customRating;
    Saloon saloon;

    private TextView mOrderPriceTextView, mOrderTimeTextView, mSaloonNameTextView, mSaloonAddressTextView, mSaloonPhoneNumberTextView, mSaloonTimeTextView, mSaloonRatingTextView, mSaloonMadeOfPaymentTextView;

    ArrayList<Service> serviceArrayList = new ArrayList<>();
    ServiceAdapter serviceAdapter = new ServiceAdapter(serviceArrayList);

    RecyclerView serviceRecyclerView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        order = (Order) getIntent().getSerializableExtra("order");


        mSaloonNameTextView = (TextView) findViewById(R.id.orderDetail_saloonName_TextView);
        mSaloonAddressTextView = (TextView) findViewById(R.id.orderDetail_saloonAddress_TextView);
        mSaloonPhoneNumberTextView = (TextView) findViewById(R.id.orderDetail_saloonPhoneNumber_TextView);
        mSaloonRatingTextView = (TextView) findViewById(R.id.orderDetail_saloonRating_TextView);
        mSaloonTimeTextView = (TextView) findViewById(R.id.orderDetail_saloonTiming_TextView);
        mSaloonMadeOfPaymentTextView = (TextView) findViewById(R.id.orderDetail_saloonModesOfPayment_TextView);
        mOrderPriceTextView = (TextView) findViewById(R.id.orderDetail_orderPrice_TextView);

        mOrderTimeTextView =(TextView)findViewById(R.id.orderDetail_orderTime_TextView);

        serviceRecyclerView=(RecyclerView)findViewById(R.id.orderDetail_servicesName_recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        serviceRecyclerView.setLayoutManager(mLayoutManager);
        serviceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        serviceRecyclerView.setAdapter(serviceAdapter);


        if (order != null) {
            fetchSaloon();
            initializeActivity();
        } else {
            orderUID = getIntent().getStringExtra("orderID");

            new FireBaseHandler().downloadOrder(LoginActivity.USER.getUserUID(), orderUID, new FireBaseHandler.OnOrderListener() {
                @Override
                public void onOrderUpload(boolean isSuccessful) {

                }

                @Override
                public void onOrderDownload(Order order, boolean isSuccessful) {

                    if (isSuccessful) {
                        if (order != null) {
                            OrderDetailActivity.this.order = order;
                            fetchSaloon();
                            initializeActivity();

                        } else {
                            Toast.makeText(OrderDetailActivity.this, "No order Found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(OrderDetailActivity.this, "No order Found", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onOrderListDownload(ArrayList<Order> orderArrayList, boolean isSuccessful) {

                }
            });


        }


    }

    private void initializeActivity() {


        if (order.getOrderStatus() > 0) {

            new FireBaseHandler().downloadRating(order, new FireBaseHandler.OnRatingListener() {
                @Override
                public void onRatingUploaded(CustomRating customRating, boolean isSuccessful) {

                }

                @Override
                public void onRatingDownload(CustomRating customRating, boolean isSuccessful) {

                    if (isSuccessful) {
                        if (customRating != null) {

                            OrderDetailActivity.this.customRating = customRating;
                            updateOrderRatingUI();

                        } else {

                            openRatingSystem();
                        }
                    }
                }
            });
        }

        if (order!=null){
            for (Service service : order.getOrderServiceIDList().values()){
                serviceArrayList.add(service);
            }

        }

        updateUI();
    }

    public void updateUI() {

        if (order != null) {

            mOrderPriceTextView.setText(order.getOrderPrice()+"");
            mOrderTimeTextView.setText(order.resolveOrderBookingTime());
            mSaloonNameTextView.setText(order.getSaloonName());

        }

        if (saloon != null) {
            // mSaloonTimeingTextView.setText();
            mSaloonRatingTextView.setText(saloon.resolveSaloonRating());
            mSaloonPhoneNumberTextView.setText(saloon.getSaloonPhoneNumber());
            mSaloonAddressTextView.setText(saloon.getSaloonAddress());
            mSaloonTimeTextView.setText(saloon.resolveSaloonOpeningTime()+"-"+saloon.resolveSaloonClosingTime());

        }

        serviceAdapter.notifyDataSetChanged();

    }

    private void fetchSaloon() {
        new FireBaseHandler().downloadSaloon(order.getSaloonID(), new FireBaseHandler.OnSaloonDownload() {
            @Override
            public void onSaloon(Saloon saloon) {
                if (saloon != null) {
                    OrderDetailActivity.this.saloon = saloon;
                    updateUI();
                } else {

                }
            }

            @Override
            public void onSaloonValueUploaded(boolean isSucessful) {

            }
        });
    }

    private void updateOrderRatingUI() {

    }


    public void getSaloonDirection(View view) {

    }

    private void openRatingSystem() {

        RatingBar ratingBar = (RatingBar) findViewById(R.id.orderDetail_rating_ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                customRating = new CustomRating();
                customRating.setSaloonUID(order.getSaloonID());
                customRating.setOrderID(order.getOrderID());
                customRating.setUserUID(order.getUserID());
                customRating.setRating((int) rating);

                if (saloon != null) {
                    customRating.setSaloonPoint(saloon.getSaloonPoint());
                    customRating.setSaloonRatingSum(saloon.getSaloonRatingSum());
                    customRating.setSaloonTotalRating(saloon.getSaloonTotalRating());
                } else {
                    return;
                }

                new FireBaseHandler().uploadRating(customRating, new FireBaseHandler.OnRatingListener() {
                    @Override
                    public void onRatingUploaded(CustomRating customRating, boolean isSuccessful) {
                        Toast.makeText(OrderDetailActivity.this, "Rating uploaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRatingDownload(CustomRating customRating, boolean isSuccessful) {

                    }
                });


            }
        });

    }

    @Override
    public void onBackPressed() {
        if (orderUID == null) {
            super.onBackPressed();
        }else{
            Intent intent =new Intent(OrderDetailActivity.this , MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }


}
