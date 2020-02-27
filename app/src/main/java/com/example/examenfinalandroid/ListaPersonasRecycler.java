package com.example.examenfinalandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.examenfinalandroid.Utilidades.Utilidades;
import com.example.examenfinalandroid.adaptadores.ListaPersonasAdapter;
import com.example.examenfinalandroid.entidades.Usuario;

import java.util.ArrayList;
import java.util.Collections;

public class ListaPersonasRecycler extends AppCompatActivity /*implements View.OnClickListener*/ {
    ArrayList<Usuario> listaUsuario;
    RecyclerView recyclerViewUsuarios;

    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_personas_recycler);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_usuarios", null, 1);

        listaUsuario = new ArrayList<>();

        recyclerViewUsuarios = (RecyclerView) findViewById(R.id.recyclerPersonas);
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(this));

        consultarListaPersonas();

        final ListaPersonasAdapter adapter = new ListaPersonasAdapter(listaUsuario);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String informacion = "id: " + listaUsuario.get(recyclerViewUsuarios.getChildAdapterPosition(v)).getId() + "\n";
                informacion += "Nombre: " + listaUsuario.get(recyclerViewUsuarios.getChildAdapterPosition(v)).getNombre() + "\n";
                informacion += "Telefono: " +listaUsuario.get(recyclerViewUsuarios.getChildAdapterPosition(v)).getTelefono() + "\n";
                Toast.makeText(getApplicationContext(), informacion, Toast.LENGTH_LONG).show();

                Usuario user = listaUsuario.get(recyclerViewUsuarios.getChildAdapterPosition(v));
                Intent intent = new Intent(ListaPersonasRecycler.this, DetalleUsuarioActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("usuario", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        recyclerViewUsuarios.setAdapter(adapter);


        borrarDeslizar(adapter);



    }

    private void consultarListaPersonas() {
        SQLiteDatabase db = conn.getReadableDatabase();

        Usuario usuario = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_USUARIO, null);

        while (cursor.moveToNext()) {
            usuario = new Usuario();
            usuario.setId(cursor.getInt(0));
            usuario.setNombre(cursor.getString(1));
            usuario.setTelefono(cursor.getString(2));

            listaUsuario.add(usuario);

        }
    }

    public void borrarDeslizar(final ListaPersonasAdapter adapter) {
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
                ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT) {



            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                int from = viewHolder.getAdapterPosition();
                int to = viewHolder1.getAdapterPosition();
                Collections.swap(listaUsuario, from, to);
                adapter.notifyItemMoved(from, to);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                SQLiteDatabase db = conn.getWritableDatabase();
                Integer id = listaUsuario.get(viewHolder.getAdapterPosition()).getId();
                String[] parametros = {String.valueOf(id)};

                db.delete(Utilidades.TABLA_USUARIO, Utilidades.CAMPO_ID + "=?", parametros);
                Toast.makeText(getApplicationContext(), "Se ha eliminado", Toast.LENGTH_LONG).show();

                listaUsuario.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

        });


        helper.attachToRecyclerView(recyclerViewUsuarios);
    }



}
