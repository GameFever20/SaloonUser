package utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Aisha on 7/2/2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.UserViewHolder> {

    private ArrayList<User> userOrderArrayList;


    public OrderAdapter(ArrayList<User> mUserOrderArrayList) {
        this.userOrderArrayList = mUserOrderArrayList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public UserViewHolder(View itemView) {
            super(itemView);
        }
    }
}
