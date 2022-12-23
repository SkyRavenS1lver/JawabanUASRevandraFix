package com.example.jawabanuasrevandra;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jawabanuasrevandra.Favorite.Fav;
import com.example.jawabanuasrevandra.Notifikasi.Notif;

import java.util.ArrayList;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.ViewHolder> {
    private ArrayList<Berita> value;
    private LayoutInflater inflater;
    Fav favs = new Fav();


    public BeritaAdapter(Context context, ArrayList<Berita> listBerita) {
        this.value = listBerita;
        this.inflater = LayoutInflater.from(context);

    }
    public void setList(ArrayList<Berita> listBaru){
        this.value = listBaru;
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
            holder.like.setVisibility(View.GONE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(holder.itemView.getContext());
                    alert.setTitle("Perhatian!");
                    alert.setMessage("Apakah anda ingin Menghapus Data Tersebut?");
                    alert.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseController.deleteData(berita);
                            Toast.makeText(holder.itemView.getContext(), "Data Berhasil Di Hapus", Toast.LENGTH_LONG).show();
                        }
                    });
                    alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alert.show();
                }
            });
        }
        if (preferensi.equals("Edit")){
            holder.itemView.setBackground(bgEdit);
            holder.like.setVisibility(View.GONE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(holder.itemView.getContext());
                    alert.setTitle("Perhatian!");
                    alert.setMessage("Apakah anda ingin Menghapus Data Tersebut?");
                    alert.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseController.deleteData(berita);
                            Toast.makeText(holder.itemView.getContext(), "Data Berhasil Di Hapus", Toast.LENGTH_LONG).show();
                        }
                    });
                    alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alert.show();
                }
            });
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
        if (!preferensi.equals("Edit")){
            holder.itemView.setBackground(bgEdit);
            holder.delete.setVisibility(View.GONE);
            TampilBerita activity = new TampilBerita();
            for(Fav fav:Model.allFav){
                if(fav.getKeyBerita().equals(berita.getKey()) && fav.getEmail().equals(FirebaseController.getCurrentUserEmail())){
                    holder.like.setChecked(true);
                    break;
                }
            }
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.like.isChecked()){
                        Fav favBaru = new Fav(berita.getKey(),FirebaseController.getCurrentUserEmail());
                        activity.insertData(favBaru);
//                        favBaru.setId(Model.idBerita);
                        if(!berita.getEmail().equals(FirebaseController.getCurrentUserEmail())){
                        FirebaseController.insertData(new Notif("Berita "+berita.getJudul() +" Anda Disukai Oleh "+FirebaseController.getCurrentUserFullName()
                                ,berita.getEmail()));}
//                        Model.allFav.add(favBaru);
                        if(TampilBerita.getRvAdaper()!=null && preferensi.equals("Fav")){
                            TampilBerita.favUpdate();}
                        System.out.println(Model.allFav.size());}
                    else {
                        for (Fav fav:Model.allFav){
                            if (fav.getKeyBerita().equals(berita.getKey())&&fav.getEmail().equals(FirebaseController.getCurrentUserEmail())){
                                activity.deleteData(fav);
                                Model.allFav.remove(fav);
                                if (preferensi.equals("Fav")){
                                holder.itemView.setVisibility(View.GONE);
                                }

//                                TampilBerita.favUpdate();
                                break;
                            }
//                            for (NotifApk notifApk:Model.allNotif){
//                                if (notifApk.getMessage().equals(berita.getKey())&&fav.getEmail().equals(FirebaseController.getCurrentUserEmail())){
//                                    activity.deleteData(fav);
//                                    holder.itemView.setVisibility(View.GONE);
//                                    TampilBerita.getRvAdaper().notifyDataSetChanged();
//                                    break;
//                                }
//                            }
                        }if(TampilBerita.getRvAdaper()!=null &&preferensi.equals("Fav")){
                            holder.like.setVisibility(View.GONE);
                            TampilBerita.favUpdate();}}
                }
            });
        }
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                if(preferensi.equals("Edit")) {
//                    AlertDialog.Builder alert = new AlertDialog.Builder(holder.itemView.getContext());
//                    alert.setTitle("Perhatian!");
//                    alert.setMessage("Apakah anda ingin Menghapus Data Tersebut?");
//                    alert.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            FirebaseController.deleteData(berita);
//                            Toast.makeText(holder.itemView.getContext(), "Data Berhasil Di Hapus", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                        }
//                    });
//                    alert.show();
//                }
//                return false;
//            }
//        });
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), ManageBerita.class);
            Model.currentBerita = berita;
            intent.putExtra("judul", "Detail Data");
            intent.putExtra("mode", "edit");
            view.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return value.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtTanggal,txtWriter;
        CheckBox like;
        ImageView delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            like = itemView.findViewById(R.id.checkBox);
            delete = itemView.findViewById(R.id.imageView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtTanggal = itemView.findViewById(R.id.txt_tanggal);
            txtWriter = itemView.findViewById(R.id.txt_writer);
        }
    }

}
