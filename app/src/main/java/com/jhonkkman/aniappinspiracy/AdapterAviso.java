package com.jhonkkman.aniappinspiracy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jhonkkman.aniappinspiracy.data.models.Aviso;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.jhonkkman.aniappinspiracy.CenterActivity.avisos;

public class AdapterAviso extends RecyclerView.Adapter<AdapterAviso.ViewHolderAviso> {

    private Context context;

    public AdapterAviso(Context context){
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public AdapterAviso.ViewHolderAviso onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolderAviso(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aviso,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterAviso.ViewHolderAviso holder, int position) {
        holder.loadAvisos(context, avisos.get(position));
    }

    @Override
    public int getItemCount() {
        return avisos.size();
    }

    public static class ViewHolderAviso extends RecyclerView.ViewHolder{

        ImageView iv_aviso;
        TextView tv_titulo,tv_desc,tv_hora;

        public ViewHolderAviso(@NonNull @NotNull View v) {
            super(v);
            iv_aviso = v.findViewById(R.id.iv_aviso);
            tv_titulo = v.findViewById(R.id.tv_titulo_comunidad);
            tv_desc = v.findViewById(R.id.tv_desc_comunidad);
            tv_hora = v.findViewById(R.id.tv_hora_comunidad);
        }

        public void loadAvisos(Context context, Aviso aviso){
            tv_titulo.setText(aviso.getTitulo());
            tv_desc.setText(aviso.getDescripcion());
            Date date = new Date(Long.parseLong(aviso.getHora()));
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
            tv_hora.setText(format.format(date));
            Glide.with(context).load(aviso.getImagen()).into(iv_aviso);
        }
    }
}
