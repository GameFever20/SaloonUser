package app.saloonuser.craftystudio.saloonuser;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import utils.FireBaseHandler;
import utils.Order;
import utils.Saloon;

public class UserOrderPlacementActivity extends AppCompatActivity {

    private Saloon saloon;
    TextView mSalooonNameTextview, mServiceListTextview, mSaloonAddressTextview, mOrderDateTextview, mOrderTimeTextview ,mOrderPriceTextview;
    private int selectedyear;
    private int selectedMonth;
    private int selectedDay;
    private int selectedHour;
    private int selectedMinute;

    long bookingTime;

    boolean isOrderPlaced = false;

    private ProgressDialog progressDialog;


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

        progressDialog = new ProgressDialog(this);

        showDateDialog();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSalooonNameTextview = (TextView) findViewById(R.id.orderPlacement_saloonName_textview);
        mServiceListTextview = (TextView) findViewById(R.id.orderPlacement_bookingService_textview);
        mSaloonAddressTextview = (TextView) findViewById(R.id.orderPlacement_bookingAddress_textview);
        mOrderDateTextview = (TextView) findViewById(R.id.orderPlacement_bookingOrderDate_textview);
        mOrderTimeTextview = (TextView) findViewById(R.id.orderPlacement_bookingOrderTime_textview);
        mOrderPriceTextview = (TextView)findViewById(R.id.orderPlacement_totalPrice_Textview);

        mOrderDateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOrderPlaced) {
                    showDateDialog();
                }
            }
        });

        mOrderTimeTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOrderPlaced) {
                    showTimeDialog();
                }
            }
        });


    }

    private void showInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setTitle("Time")
                .setMessage("Select Appoinment date and time")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showDateDialog();
                    }
                });

        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }

    private void showDateDialog() {

        Toast.makeText(this, "Select order Date", Toast.LENGTH_SHORT).show();

        try {
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            DatePickerDialog datePicker = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            selectedyear = year;
                            selectedMonth = month;
                            selectedDay = dayOfMonth;


                            showTimeDialog();

                        }
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH));

            datePicker.setCancelable(false);
            datePicker.setTitle("Select appoinment date ");
            datePicker.setCanceledOnTouchOutside(false);
            datePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    showDateDialog();
                }
            });
            datePicker.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void showTimeDialog() {

        Toast.makeText(this, "Select order Time", Toast.LENGTH_SHORT).show();

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                selectedHour = hourOfDay;
                selectedMinute = minute;

                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                long currentTimeInMillis = calendar.getTimeInMillis();

                calendar.set(selectedyear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0);
                Date date = calendar.getTime();


                if ((date.getTime() - 3600000l) > currentTimeInMillis) {

                    bookingTime = date.getTime();

                    placeOrder();
                } else {
                    Toast.makeText(UserOrderPlacementActivity.this, "Order canot be placed for passed time", Toast.LENGTH_SHORT).show();


                    showDateDialog();

                }


            }
        }, calendar.HOUR, calendar.MINUTE, false);

        timePickerDialog.setCanceledOnTouchOutside(false);
        timePickerDialog.setCancelable(false);
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                showTimeDialog();
            }
        });
        timePickerDialog.setTitle("Select appoinment time");
        timePickerDialog.show();

    }

    private void placeOrder() {
        final Order order = ServiceTypeActivity.CURRENTORDER;
        order.setUserID(LoginActivity.USER.getUserUID());

        order.setOrderStatus(1);
        order.setOrderTime(Calendar.getInstance().getTimeInMillis());

        order.setOrderBookingTime(bookingTime);
        //Toast.makeText(this, "time " + checkTimeSelected(), Toast.LENGTH_SHORT).show();
        if (!checkTimeSelected()){
            showTimeDialog();
            return;
        }

        showProgressDialog("Placing your order");

        new FireBaseHandler().uploadOrder(order, new FireBaseHandler.OnOrderListener() {
            @Override
            public void onOrderUpload(boolean isSuccessful) {
                Toast.makeText(UserOrderPlacementActivity.this, "Order placed", Toast.LENGTH_SHORT).show();
                updateUI(order);
                closeProgressDialog();

            }

            @Override
            public void onOrderDownload(Order order, boolean isSuccessful) {

            }

            @Override
            public void onOrderListDownload(ArrayList<Order> orderArrayList, boolean isSuccessful) {

            }
        });

    }

    public void placeOrderButtonClick(View view) {

        placeOrder();

    }

    public void updateUI(Order order) {


        isOrderPlaced = true;

        mSalooonNameTextview.setText(order.getSaloonName());
        mSaloonAddressTextview.setText(saloon.getSaloonAddress());
        mServiceListTextview.setText(order.resolveOrderServiceList());
        mOrderTimeTextview.setText(selectedHour + ":" + selectedMinute);
        mOrderDateTextview.setText(selectedDay + "/" + selectedMonth + "/" + selectedyear);
        mOrderPriceTextview.setText(order.getOrderPrice()+"");

        ServiceTypeActivity.CURRENTORDER = new Order();

    }

    private long calculateBookingTime() {

        DatePicker datePicker = (DatePicker) findViewById(R.id.orderPlacement_bookingDate_datePicker);
        TimePicker timePicker = (TimePicker) findViewById(R.id.orderPlacement_bookingTime_timePicker);


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


        if (selectedHour > saloon.getClosingTimeHour() || selectedHour < saloon.getOpeningTimeHour()) {

            return false;
        }


        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (isOrderPlaced) {
            Intent intent = new Intent(UserOrderPlacementActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }


    public void completeOrderButtonClick(View view) {

        Intent intent = new Intent(UserOrderPlacementActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void closeProgressDialog() {
        progressDialog.dismiss();
    }

}
