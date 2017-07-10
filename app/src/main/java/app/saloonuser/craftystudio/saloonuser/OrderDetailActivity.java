package app.saloonuser.craftystudio.saloonuser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.ArrayList;

import utils.CustomRating;
import utils.FireBaseHandler;
import utils.Order;
import utils.Saloon;

public class OrderDetailActivity extends AppCompatActivity {

    Order order;
    String orderUID;
    CustomRating customRating;
    Saloon saloon;

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

        if (order != null) {
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
                            fetchSaloon();
                            openRatingSystem();
                        }
                    }
                }
            });
        }


    }

    private void fetchSaloon() {
        new FireBaseHandler().downloadSaloon(order.getSaloonID(), new FireBaseHandler.OnSaloonDownload() {
            @Override
            public void onSaloon(Saloon saloon) {
                if (saloon != null) {
                    OrderDetailActivity.this.saloon = saloon;
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

}
