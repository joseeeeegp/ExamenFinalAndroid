package com.example.examenfinalandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examenfinalandroid.Utilidades.Utilidades;
import com.example.examenfinalandroid.entidades.Usuario;

public class DetalleUsuarioActivity extends AppCompatActivity {

    TextView campoId,campoNombre,campoTelefono;
    ConexionSQLiteHelper conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_usuario);

        campoId=(TextView) findViewById(R.id.campoId);
        campoNombre=(TextView) findViewById(R.id.campoNombre);
        campoTelefono=(TextView) findViewById(R.id.campoTelefono);

        Bundle objetoEnviado= getIntent().getExtras();
        Usuario user=null;

        if(objetoEnviado!=null){
            user= (Usuario) objetoEnviado.getSerializable("usuario");
            campoId.setText(user.getId().toString());
            campoNombre.setText(user.getNombre());
            campoTelefono.setText(user.getTelefono());

        }
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.btnConsultar:
                //consultar();
                consultarSql();
                break;
            case R.id.btnActualizar:
                actualizarUsuario();
                break;
            case R.id.btnEliminar:
                eliminarUsuario();
                break;
        }
    }


    private void eliminarUsuario() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros = {campoId.getText().toString()};

        db.delete(Utilidades.TABLA_USUARIO,Utilidades.CAMPO_ID+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Se ha eliminado",Toast.LENGTH_LONG).show();

        //para borrar el campoId
        campoId.setText("");
        // para borrar los campos de nombre y telefono
        limpiar();
        db.close();
    }

    private void actualizarUsuario() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros = {campoId.getText().toString()};
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE,campoNombre.getText().toString());
        values.put(Utilidades.CAMPO_TELEFONO,campoTelefono.getText().toString());

        db.update(Utilidades.TABLA_USUARIO,values,Utilidades.CAMPO_ID+"=?",parametros);

        Toast.makeText(getApplicationContext(),"Se ha actualizado",Toast.LENGTH_LONG).show();
        db.close();
    }

    private void consultarSql() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros = {campoId.getText().toString()};

        try{

            //Select nombre,telefono from usuario where codigo =?
            Cursor cursor = db.rawQuery("SELECT "+Utilidades.CAMPO_NOMBRE+","+Utilidades.CAMPO_TELEFONO+
                    " FROM "+Utilidades.TABLA_USUARIO+" WHERE "+Utilidades.CAMPO_ID+"=? ",parametros);

            cursor.moveToFirst();
            campoNombre.setText(cursor.getString(0));
            campoTelefono.setText(cursor.getString(1));
            cursor.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El documento no existe",Toast.LENGTH_LONG).show();
            limpiar();
        }
    }
}
