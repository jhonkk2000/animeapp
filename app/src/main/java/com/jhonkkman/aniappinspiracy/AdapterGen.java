package com.jhonkkman.aniappinspiracy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jhonkkman.aniappinspiracy.data.models.GeneroItem;

import java.util.List;

public class AdapterGen extends RecyclerView.Adapter<AdapterGen.ViewHolderGen> {

    private List<GeneroItem> generos;
    private int[] select;

    public AdapterGen(List<GeneroItem> generos){
        this.generos = generos;
        select = new int[generos.size()];
    }

    public int[] getSelect(){
        return select;
    }

    @NonNull
    @Override
    public AdapterGen.ViewHolderGen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderGen(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gen_fav,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGen.ViewHolderGen holder, int position) {
        if(select[position]==0){
            holder.cb_gen.setChecked(false);
        }else{
            holder.cb_gen.setChecked(true);
        }
        holder.loadData(generos.get(position));
        holder.cb_gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.cb_gen.isChecked()){
                    select[position] = 1;
                }else {
                    select[position] = 0;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return generos.size();
    }

    public static class ViewHolderGen extends RecyclerView.ViewHolder{

        TextView tv_gen;
        CheckBox cb_gen;

        public ViewHolderGen(@NonNull View v) {
            super(v);
            tv_gen = v.findViewById(R.id.tv_gen_fav);
            cb_gen = v.findViewById(R.id.cb_gen_fav);
        }

        public void loadData(GeneroItem genero){
            tv_gen.setText(genero.getName());
        }
    }
}
