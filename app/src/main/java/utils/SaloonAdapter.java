package utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import app.saloonuser.craftystudio.saloonuser.R;

import static app.saloonuser.craftystudio.saloonuser.R.id.imageView;

/**
 * Created by Aisha on 6/28/2017.
 */

public class SaloonAdapter extends RecyclerView.Adapter<SaloonAdapter.SaloonViewHolder> {

    private ArrayList<Saloon> saloonArrayList;
    Context context;


    public SaloonAdapter(ArrayList<Saloon> saloonArrayList, Context context) {
        this.saloonArrayList = saloonArrayList;
        this.context = context;
    }

    @Override
    public SaloonAdapter.SaloonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saloon_adapter_row, parent, false);
        return new SaloonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SaloonAdapter.SaloonViewHolder holder, int position) {

        Saloon saloon = saloonArrayList.get(position);
        holder.saloonNameTextView.setText(saloon.getSaloonName());
        holder.saloonOpeningTimeTextView.setText(saloon.getOpeningTimeHour() + ":" + saloon.getOpeningTimeMinute());
        holder.saloonClosingTimeTextView.setText(saloon.getClosingTimeHour() + ":" + saloon.getClosingTimeMinute());
        holder.saloonAddressTextView.setText(saloon.getSaloonAddress());
        // holder.saloonRatingTextView.setText(saloon.getSaloonRating()+"");

        //firebase storage connection
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference storageReference = storageRef.child("saloon_image/" + saloon.getSaloonUID() + "/" + "profile_image");

        //Display image


        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .thumbnail(0.5f)
                .override(200, 200)
                .crossFade(100)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.saloonProfileImage);


    }

    @Override
    public int getItemCount() {
        return saloonArrayList.size();
    }

    public class SaloonViewHolder extends RecyclerView.ViewHolder {
        public TextView saloonNameTextView, saloonOpeningTimeTextView, saloonClosingTimeTextView, saloonAddressTextView, saloonRatingTextView;
        public Button saloonServiceBtn;
        public ImageView saloonProfileImage;

        public SaloonViewHolder(View view) {
            super(view);
            saloonNameTextView = (TextView) view.findViewById(R.id.saloonadapterrow_saloonName_textview);
            saloonOpeningTimeTextView = (TextView) view.findViewById(R.id.saloonadapterrow_saloonopening_textview);
            saloonClosingTimeTextView = (TextView) view.findViewById(R.id.saloonadapterrow_saloonclosing_textview);
            saloonAddressTextView = (TextView) view.findViewById(R.id.saloonadapterrow_saloonaddress_textview);
            saloonServiceBtn = (Button) view.findViewById(R.id.saloonadapterrow_saloon_servicebtn_textview);
            saloonProfileImage = (ImageView) view.findViewById(R.id.saloonadapterrow_saloon_profilepic_textview);

        }
    }
}
