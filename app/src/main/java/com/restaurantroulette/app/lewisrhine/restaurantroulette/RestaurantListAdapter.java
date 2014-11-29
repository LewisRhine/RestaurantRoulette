package com.restaurantroulette.app.lewisrhine.restaurantroulette;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder>  {
    private Context context;
    private ArrayList<Businesses> arrayList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        private ImageView imageView;

        private TextView name_text;

        private TextView distance_text;

        private TextView categories_text;

        private ImageView rating_image;



        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            name_text = (TextView) view.findViewById(R.id.name_text);
            distance_text = (TextView) view.findViewById(R.id.distance_text);
            categories_text = (TextView) view.findViewById(R.id.categories_text);
            rating_image = (ImageView) view.findViewById(R.id.rating_image);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RestaurantListAdapter(Context context, ArrayList<Businesses> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RestaurantListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.businesses_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Picasso.with(context).load(arrayList.get(position).getImage_url()).into(holder.imageView);
        holder.name_text.setText(arrayList.get(position).getName());
        holder.distance_text.setText(arrayList.get(position).getDistance());
        holder.categories_text.setText(arrayList.get(position).getCategories());
        Picasso.with(context).load(arrayList.get(position).getRating_image_url()).into(holder.rating_image);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}



