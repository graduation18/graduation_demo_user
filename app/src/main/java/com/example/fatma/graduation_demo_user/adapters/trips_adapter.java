package com.example.fatma.graduation_demo_user.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alexzh.circleimageview.CircleImageView;
import com.example.fatma.graduation_demo_user.Models.driver_model;
import com.example.fatma.graduation_demo_user.Models.trip_model;
import com.example.fatma.graduation_demo_user.R;
import com.example.fatma.graduation_demo_user.custom.PicassoCircleTransformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class trips_adapter extends RecyclerView.Adapter<trips_adapter.MyViewHolder> {
    private Context context;
    private List<trip_model> datalist;
    private driver_model driver;
    public class MyViewHolder extends RecyclerView.ViewHolder {

       public CardView cardView ;
       public TextView driverName , startPoint , destinationPoint , tripDuration , price;
        public RatingBar driverRate;

        public CircleImageView driverImage ;
                public MyViewHolder(CardView view) {

                    super(view);
                    cardView = (CardView) view.findViewById(R.id.card_view);
                    driverImage = (CircleImageView) view.findViewById(R.id.driver_image);
                    driverName =(TextView) view.findViewById(R.id.driver_name);
                    destinationPoint=(TextView) view.findViewById(R.id.trip_destination_address);
                    startPoint =(TextView) view.findViewById(R.id.trip_start_address);
                    price =(TextView) view.findViewById(R.id.trip_price);
                    tripDuration =(TextView) view.findViewById(R.id.trip_duration);
                    driverRate = (RatingBar) view.findViewById(R.id.ratingBar) ;



        }
    }
    public trips_adapter(Context context, List<trip_model> datalist) {
        this.context = context;
        this.datalist = datalist;
    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup viewGroup, int position) {

        CardView itemView = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trip_item, viewGroup, false);
        return new MyViewHolder(itemView);


    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        trip_model data = datalist.get(position);
        search_for_driver(data.driver_id,holder);
        holder.startPoint.setText(data.Leave_from_city);
        holder.destinationPoint.setText(data.going_to_city);
        Date d = new Date(data.time);
        SimpleDateFormat df2 = new SimpleDateFormat("hh:mm a");
        String dateText = df2.format(d);
        holder.tripDuration.setText(dateText);
        holder.price.setText(String .valueOf(data.cost)+"L.E");
    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }
    private void search_for_driver(String driver_id, final MyViewHolder holder)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("drivers");
        Query query = reference.orderByKey().equalTo(driver_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot sub_type : dataSnapshot.getChildren()) {
                      driver=sub_type.getValue(driver_model.class);
                        holder.driverName.setText(driver.name);
                        Picasso.with(context)
                                .load(driver.image)
                                .placeholder(R.mipmap.ic_launcher).transform(new PicassoCircleTransformation())
                                .into(holder.driverImage, new Callback() {
                            @Override
                            public void onSuccess() {}
                            @Override public void onError() {
                                Toast.makeText(context,"error loading image",Toast.LENGTH_LONG).show();
                            }
                        });
                        holder.driverRate.setRating(driver.rate);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
