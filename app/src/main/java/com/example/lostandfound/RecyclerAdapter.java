package com.example.lostandfound;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static List<LostFoundItem> itemList;

    public RecyclerAdapter(List<LostFoundItem> lostFoundItemList){
        itemList = lostFoundItemList;
    }
    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        LostFoundItem item = itemList.get(position);
        holder.postType.setText(item.getPostType());
        holder.description.setText(item.getDescription());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView postType, description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postType = itemView.findViewById(R.id.postType);
            description = itemView.findViewById(R.id.description);



            //set on click listener. passes clicked item through intent
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    LostFoundItem clickedItem = itemList.get(position);

                    Intent intent = new Intent(itemView.getContext(), PostOverview.class);

                    intent.putExtra("postType", clickedItem.getPostType());
                    intent.putExtra("name", clickedItem.getName());
                    intent.putExtra("phone", clickedItem.getPhone());
                    intent.putExtra("description", clickedItem.getDescription());
                    intent.putExtra("date", clickedItem.getDate());
                    intent.putExtra("id", clickedItem.getId());


                    itemView.getContext().startActivity(intent);

                }
            });
        }
    }
}
