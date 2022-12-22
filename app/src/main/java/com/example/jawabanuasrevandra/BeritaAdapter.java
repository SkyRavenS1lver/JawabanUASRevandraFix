package com.example.jawabanuasrevandra;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.ViewHolder> {
    private final ArrayList<Berita> value;
    private LayoutInflater inflater;

    public BeritaAdapter(Context context, ArrayList<Berita> listBerita) {
        this.value = listBerita;
        this.inflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.itemrvberita, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Berita berita = value.get(position);
        holder.txtName.setText(berita.getJudul());
        holder.txtTanggal.setText(berita.getRilis());
        holder.txtWriter.setText(berita.getPenulis());
        String preferensi = CariBerita.sharedPreferences.getString(CariBerita.GENRE_KEY,"");
        Drawable bg = ContextCompat.getDrawable(inflater.getContext(), R.drawable.bg_rv);
        Drawable bg2 = ContextCompat.getDrawable(inflater.getContext(), R.drawable.bgrv2);
        Drawable bg3 = ContextCompat.getDrawable(inflater.getContext(), R.drawable.bgrv3);
        Drawable bg4 = ContextCompat.getDrawable(inflater.getContext(), R.drawable.bgrv4);
        Drawable bg5 = ContextCompat.getDrawable(inflater.getContext(), R.drawable.bgrv5);
        Drawable bgEdit = ContextCompat.getDrawable(inflater.getContext(), R.drawable.bgev_edit);
        if (preferensi.equals("Edit")){
            holder.itemView.setBackground(bgEdit);
        }
        else if (berita.getCategory().equals("Pariwisata")){
            holder.itemView.setBackground(bg);
        }else if (berita.getCategory().equals("Crime")){
            holder.itemView.setBackground(bg5);
        }else if (berita.getCategory().equals("Sport")){
            holder.itemView.setBackground(bg2);
        }else if (berita.getCategory().equals("Politics")){
            holder.itemView.setBackground(bg3);
        }else if (berita.getCategory().equals("Entertainment")){
            holder.itemView.setBackground(bg4);
        }
//        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(holder.itemView.getContext(), DetailBerita.class);
//            String judul = berita.judul;
//            String tanggal = berita.rilis;
//            int noGambar = berita.picture;
//            String desc = berita.content;
//            intent.putExtra("judul", judul);
//            intent.putExtra("tanggal", tanggal);
//            intent.putExtra("noGambar", noGambar);
//            intent.putExtra("desc", desc);
//            view.getContext().startActivity(intent);
//        });

    }

    @Override
    public int getItemCount() {
        return value.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtTanggal,txtWriter;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtTanggal = itemView.findViewById(R.id.txt_tanggal);
            txtWriter = itemView.findViewById(R.id.txt_writer);
        }
    }
}
