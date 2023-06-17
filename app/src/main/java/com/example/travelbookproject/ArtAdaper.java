package com.example.travelbookproject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbookproject.databinding.RecylerRowBinding;

import java.util.ArrayList;

public class ArtAdaper extends RecyclerView.Adapter<ArtAdaper.ArtHolder> {
    ArrayList<Art> artArrayList;

    public ArtAdaper(ArrayList<Art> artArrayList){
        this.artArrayList=artArrayList;
    }


    @NonNull
    @Override
    public ArtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecylerRowBinding recylerRowBinding=RecylerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ArtHolder(recylerRowBinding);





    }

    @Override
    public void onBindViewHolder(@NonNull ArtHolder holder, int position) {

        holder.binding.recylerViewText.setText(artArrayList.get(position).name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(holder.itemView.getContext(),nextActivity.class);
                intent.putExtra("info","old");
                intent.putExtra("artId",artArrayList.get(position).id);

                holder.itemView.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return artArrayList.size();
    }

    public class ArtHolder extends RecyclerView.ViewHolder{

        private RecylerRowBinding binding;




        public ArtHolder(RecylerRowBinding binding) {
            super(binding.getRoot());

            this.binding=binding;


        }
    }
}
