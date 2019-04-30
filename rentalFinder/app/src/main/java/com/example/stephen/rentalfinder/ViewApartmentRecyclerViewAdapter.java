package com.example.stephen.rentalfinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ViewApartmentRecyclerViewAdapter extends RecyclerView.Adapter<ViewApartmentRecyclerViewAdapter.MyViewHolder> {

    private List<ViewApartmentDataHandler> apartmentDataHandlerList;
    RequestOptions options;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView apartmentName, rent, contact, location,description;
        String url;

        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            apartmentName = (TextView) view.findViewById(R.id.apartmentF);
            contact = (TextView) view.findViewById(R.id.contactF);
            rent = (TextView) view.findViewById(R.id.rentF);
            location = view.findViewById(R.id.locationF);
            imageView = view.findViewById(R.id.imageView);
            description = view.findViewById(R.id.description);
            this.url = url;
        }
    }


    public ViewApartmentRecyclerViewAdapter(List<ViewApartmentDataHandler> apartmentDataHandlerList, ViewApartmentsActivity viewApartmentsActivity) {
        this.apartmentDataHandlerList = apartmentDataHandlerList;
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.load)
                .error(R.drawable.err);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_apartments, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ViewApartmentDataHandler viewApartmentDataHandler = apartmentDataHandlerList.get(position);

        holder.url = viewApartmentDataHandler.getImageUrl();
        //new code
        // load image from the internet using Glide
        //Glide.with(mContext).load(apartmentDataHandlerList.get(position).getImageUrl()).apply(options).into(holder.imageView);

        Glide.with(ViewApartmentsActivity.getAppContext()).load(holder.url).apply(options).
                into(holder.imageView);

        holder.apartmentName.setText(viewApartmentDataHandler.getApartmentName());
        holder.contact.setText(viewApartmentDataHandler.getContact());
        holder.rent.setText(viewApartmentDataHandler.getRent());
        holder.location.setText(viewApartmentDataHandler.getLocation());
        holder.description.setText(viewApartmentDataHandler.getDescription());


    }

    @Override
    public int getItemCount() {
        return apartmentDataHandlerList.size();
    }

}
