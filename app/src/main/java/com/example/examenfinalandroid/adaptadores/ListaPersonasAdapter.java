package com.example.examenfinalandroid.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.examenfinalandroid.R;
import com.example.examenfinalandroid.entidades.Usuario;

import java.util.ArrayList;

public class ListaPersonasAdapter
        extends RecyclerView.Adapter<ListaPersonasAdapter.PersonasViewHolder>
        implements View.OnClickListener {

    ArrayList<Usuario> listaUsuario;
    private View.OnClickListener listener;

    public ListaPersonasAdapter(ArrayList<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    @Override
    public PersonasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_personas, null, false);
        view.setOnClickListener(this);
        return new PersonasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonasViewHolder holder, int position) {
        holder.documento.setText(listaUsuario.get(position).getId().toString());
        holder.nombre.setText(listaUsuario.get(position).getNombre());
        holder.telefono.setText(listaUsuario.get(position).getTelefono());
    }

    @Override
    public int getItemCount() {
        return listaUsuario.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class PersonasViewHolder extends RecyclerView.ViewHolder {

        TextView documento, nombre, telefono;

        public PersonasViewHolder(View itemView) {
            super(itemView);
            documento = (TextView) itemView.findViewById(R.id.textDocumento);
            nombre = (TextView) itemView.findViewById(R.id.textNombre);
            telefono = (TextView) itemView.findViewById(R.id.textTelefono);
        }
    }
}
