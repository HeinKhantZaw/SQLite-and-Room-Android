package com.example.roomassignment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    // Adapter used for recyclerView
    // To know more about recyclerView, you can read in here (https://github.com/HeinKhantZaw/HuaweiAndroidTraining/tree/main/Day2/RecyclerViewExample)

    List<EventData> eventDataList; // List of EventData Objects
    private Context mContext;

    public CustomAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.event_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventData data = eventDataList.get(position);
        final String eventName, eventDate, eventLocation, eventDesc;
        final int id = data.getId();
        eventName = data.getName();
        eventDate = data.getDate();
        eventLocation = data.getLocation();
        eventDesc = data.getDescription();

        holder.tvName.setText(eventName);
        holder.tvDate.setText(eventDate);
        holder.tvDesc.setText(eventDesc);
        holder.tvLocation.setText(eventLocation);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EventActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", eventName);
                intent.putExtra("date", eventDate);
                intent.putExtra("location", eventLocation);
                intent.putExtra("description", eventDesc);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (eventDataList == null) {
            return 0;
        }
        return eventDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvLocation, tvDesc;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txtEventTitle);
            tvDate = itemView.findViewById(R.id.txtEventDate);
            tvLocation = itemView.findViewById(R.id.txtEventLocation);
            tvDesc = itemView.findViewById(R.id.txtEventDesc);
            view = itemView;
        }
    }

    public void setData(List<EventData> data) {
        eventDataList = data;
        notifyDataSetChanged();  // Notifies that data has been changed and makes adapter to refresh the data itself.
    }

    public List<EventData> getData() {
        return eventDataList;
    }

}
