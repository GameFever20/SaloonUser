package utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import app.saloonuser.craftystudio.saloonuser.R;

/**
 * Created by Aisha on 7/2/2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.UserViewHolder> {

    private ArrayList<Order> userOrderArrayList;


    public OrderAdapter(ArrayList<Order> mUserOrderArrayList) {
        this.userOrderArrayList = mUserOrderArrayList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_order_adapter_row, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {

        Order order = userOrderArrayList.get(position);
        holder.saloonNameTextView.setText(order.getSaloonName());
        holder.orderDateTimeTextView.setText(order.resolveOrderDate());
        holder.orderPiceTextView.setText(order.getOrderPrice());


        holder.saloonRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });
        //changes to be continued
        holder.allServiceListTextView.setText(order.getOrederServiceIDList().get(position));


    }

    @Override
    public int getItemCount() {
        return userOrderArrayList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView saloonNameTextView, orderDateTimeTextView, orderPiceTextView, allServiceListTextView;
        public Button saloonServiceBtn;
        RatingBar saloonRatingBar;

        public UserViewHolder(View view) {
            super(view);
            saloonNameTextView = (TextView) view.findViewById(R.id.orderAdapter_saloonName_textview);
            orderDateTimeTextView = (TextView) view.findViewById(R.id.orderAdapter_orderDate_textview);
            orderPiceTextView = (TextView) view.findViewById(R.id.orderAdapter_orderPrice_textview);
            allServiceListTextView = (TextView) view.findViewById(R.id.orderAdapter_allServiceName_textview);
           // saloonServiceBtn = (Button) view.findViewById(R.id.saloonadapterrow_saloon_servicebtn_textview);
            saloonRatingBar=(RatingBar)view.findViewById(R.id.orderAdapter_saloonRating_ratingBar);
        }
    }
}
