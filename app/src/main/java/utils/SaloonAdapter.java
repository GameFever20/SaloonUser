package utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

import app.saloonuser.craftystudio.saloonuser.MainActivity;
import app.saloonuser.craftystudio.saloonuser.R;
import app.saloonuser.craftystudio.saloonuser.SaloonDetailActivity;

import static app.saloonuser.craftystudio.saloonuser.R.id.imageView;

/**
 * Created by Aisha on 6/28/2017.
 */

public class SaloonAdapter extends RecyclerView.Adapter<SaloonAdapter.SaloonViewHolder> {

    private ArrayList<Saloon> saloonArrayList;
    Context context;

    int previousPosition=0;


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

        final Saloon saloon = saloonArrayList.get(position);
        holder.saloonNameTextView.setText(saloon.getSaloonName());
       // holder.saloonOpeningTimeTextView.setText(saloon.getOpeningTimeHour() + ":" + saloon.getOpeningTimeMinute());
        //holder.saloonClosingTimeTextView.setText(saloon.getClosingTimeHour() + ":" + saloon.getClosingTimeMinute());

        holder.saloonOpeningTimeTextView.setText(saloon.resolveSaloonOpeningTime());
        holder.saloonClosingTimeTextView.setText(saloon.resolveSaloonClosingTime());

        holder.saloonAddressTextView.setText(saloon.getSaloonAddress());
        holder.saloonRatingTextView.setText(saloon.getSaloonTotalRating() + "");

        //firebase storage connection
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference storageReference = storageRef.child("saloon_image/" + saloon.getSaloonUID() + "/" + "profile_image");


        //Display image
        if (saloon.getSaloonImageList() != null) {
            if (saloon.getSaloonImageList().containsKey("profile_image")) {
                Glide.with(context)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .listener(new RequestListener<StorageReference, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                                Toast.makeText(context, "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                //Toast.makeText(context, "loaded", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        })
                        .thumbnail(0.5f)
                        .into(holder.saloonProfileImage);
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions
            //getting position to know about moving up or down
            if (position > previousPosition) {//we are going Down

                AnimationUtil.animate(holder,true);

            } else { //we are going UP
                AnimationUtil.animate(holder,false);

            }
            previousPosition = position;

        } else{
            // do something for phones running an SDK before lollipop
        }



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
            saloonRatingTextView = (TextView) view.findViewById(R.id.saloonadapterrow_saloonrating_textview);
            //  saloonServiceBtn = (Button) view.findViewById(R.id.saloonadapterrow_saloon_servicebtn_textview);
            saloonProfileImage = (ImageView) view.findViewById(R.id.saloonadapterrow_saloon_profilepic_imageView);

        }
    }
}
