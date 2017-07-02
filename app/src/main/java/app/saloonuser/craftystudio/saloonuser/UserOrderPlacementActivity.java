package app.saloonuser.craftystudio.saloonuser;

import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import utils.FireBaseHandler;
import utils.Order;
import utils.Saloon;

public class UserOrderPlacementActivity extends AppCompatActivity {

    private Saloon saloon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_placement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            saloon = (Saloon) getIntent().getSerializableExtra("Saloon");
        } catch (Exception e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void placeOrderButtonClick(View view) {

        Order order = ServiceTypeActivity.CURRENTORDER;
        order.setUserID(LoginActivity.USER.getUserUID());

        order.setOrderStatus(1);
        order.setOrderTime(new Date().getTime());

        order.setOrderBookingTime(calculateBookingTime());

        new FireBaseHandler().uploadOrder(order, new FireBaseHandler.OnOrderListener() {
            @Override
            public void onOrderUpload(boolean isSuccessful) {
                Toast.makeText(UserOrderPlacementActivity.this, "Order placed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOrderDownload(Order order, boolean isSuccessful) {

            }

            @Override
            public void onOrderListDownload(ArrayList<Order> orderArrayList, boolean isSuccessful) {

            }
        });

    }

    private long calculateBookingTime() {

        DatePicker datePicker = (DatePicker) findViewById(R.id.orderPlacement_bookingDate_datePicker);
        TimePicker timePicker = (TimePicker) findViewById(R.id.orderPlacement_bookingTime_timePicker);

        Toast.makeText(this, "time "+checkTimeSelected(), Toast.LENGTH_SHORT).show();

        Calendar calendar = Calendar.getInstance();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                    timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);

        } else {
            calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                    timePicker.getHour(), timePicker.getMinute(), 0);

        }


        long startTime = calendar.getTimeInMillis();
        return startTime;
    }


    public boolean checkTimeSelected() {
        TimePicker timePicker = (TimePicker) findViewById(R.id.orderPlacement_bookingTime_timePicker);
        int selectedHour, selectedMinute;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            selectedHour=timePicker.getCurrentHour();
            selectedMinute=timePicker.getCurrentMinute();

        } else {
            selectedHour = timePicker.getHour();
            selectedMinute = timePicker.getMinute();

        }


        if (selectedHour> saloon.getClosingTimeHour() || selectedHour < saloon.getOpeningTimeHour()){

            return false;
        }



        return true;
    }


}
