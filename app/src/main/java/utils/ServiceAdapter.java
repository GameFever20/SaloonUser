package utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.saloonuser.craftystudio.saloonuser.R;


/**
 * Created by bunny on 18/06/17.
 */

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>  {

    private ArrayList<Service> serviceArrayList;


    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        public TextView serviceIDTextView, serviceTypeNameTextView, serviceSaloonNameTextView , servicePriceTextView ,serviceNameTextView;

        public ServiceViewHolder(View view) {
            super(view);
            serviceIDTextView = (TextView) view.findViewById(R.id.serviceAdapterRow_serviceId_textview);
            serviceTypeNameTextView = (TextView) view.findViewById(R.id.serviceAdapterRow_serviceTypeName_textview);
            serviceSaloonNameTextView = (TextView) view.findViewById(R.id.serviceAdapterRow_serviceSaloonName_textview);
            servicePriceTextView = (TextView) view.findViewById(R.id.serviceAdapterRow_servicePrice_textview);
            serviceNameTextView = (TextView) view.findViewById(R.id.serviceAdapterRow_serviceName_textview);


        }



    }


    public ServiceAdapter(ArrayList<Service> serviceArrayList) {
        this.serviceArrayList = serviceArrayList;
    }

    @Override
    public ServiceAdapter.ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_adapter_row, parent, false);

        return new ServiceAdapter.ServiceViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(ServiceAdapter.ServiceViewHolder holder, int position) {
        Service service = serviceArrayList.get(position);
        holder.serviceIDTextView.setText(service.getServiceUID());
        holder.serviceTypeNameTextView.setText(service.getServiceTypeName());
        holder.servicePriceTextView.setText(service.getServiceOfferPrice() +"");
        holder.serviceSaloonNameTextView.setText(service.getSaloonName());
        holder.serviceNameTextView.setText(service.getServiceName());

    }


    @Override
    public int getItemCount() {
        return serviceArrayList.size();
    }
}
