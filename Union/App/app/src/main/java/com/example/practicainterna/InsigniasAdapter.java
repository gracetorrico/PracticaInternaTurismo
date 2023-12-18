package com.example.practicainterna;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InsigniasAdapter extends RecyclerView.Adapter<InsigniasAdapter.InsigniaViewHolder> {

    private Context context;
    private List<Insignia> listaInsignias;

    public InsigniasAdapter(Context context, List<Insignia> listaInsignias) {
        this.context = context;
        this.listaInsignias = listaInsignias;
    }

    @NonNull
    @Override
    public InsigniaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_insignia, parent, false);
        return new InsigniaViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull InsigniaViewHolder holder, int position) {
        Insignia insignia = listaInsignias.get(position);

        holder.tvNombre.setText(insignia.getNombre());
        holder.tvDescripcion.setText(insignia.getDescripcion());

        if (insignia.isUsuarioTieneInsignia()) {
            int color = ContextCompat.getColor(context, R.color.lavander);

            holder.tvUsuarioTiene.setText("Usted tiene la insignia y es nivel " + insignia.getCantidadInsignias());
            holder.tvNombre.setTextColor(color);
            holder.tvDescripcion.setTextColor(color);
            holder.tvUsuarioTiene.setTextColor(color);
        } else {
            holder.tvUsuarioTiene.setText("Usted no tiene la insignia");
        }
    }

    @Override
    public int getItemCount() {
        return listaInsignias.size();
    }

    public static class InsigniaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        TextView tvDescripcion;
        TextView tvUsuarioTiene;

        public InsigniaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreInsignia);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionInsignia);
            tvUsuarioTiene = itemView.findViewById(R.id.tvUsuarioTieneInsignia);
        }
    }
}