package com.example.dell.listviewurlsmejoradodesliz2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listaDatos;
    ArrayList<Datos> lista;
    EditText etURL, etTitulo, etBorrar;
    Button btnGuardar;
    int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaDatos = findViewById(R.id.lvURL);

        etURL = findViewById(R.id.etURL);
        etTitulo = findViewById(R.id.etTitulo);
        etBorrar = findViewById(R.id.etBorrar);

        btnGuardar = findViewById(R.id.btnGuardar);

        lista = new ArrayList<Datos>();

        lista.add(new Datos(1, "Google", "http://www.google.com/"));
        lista.add(new Datos(2, "As", "http://www.as.com/"));
        lista.add(new Datos(3, "Facebook", "http://www.facebook.com/"));
        lista.add(new Datos(4, "SkyScanner", "http://www.SkyScanner.com/"));


        Adaptador miAdaptador = new Adaptador(getApplicationContext(), lista);

        listaDatos.setAdapter(miAdaptador);

        //Darse cuenta como para tratar el evento OnItemLongClick sobre todo el item del Listview(cualquiera de ellos), trato
        //el evento en el MainActivity. Sin embargo, para hacer el OnClick sobre el campo URL (no sobre todo el item del ListView), trato
        //el evento en el Adaptador.

        listaDatos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                registerForContextMenu(view);
                posicion = position; //De esta forma tengo determinado sobre que item del Listview estoy actuando

                return false;
            }
        });

/*
        //Este método funciona para todo el ListView, pero no distingue cada item del ListView, que sería lo deseable
        listaDatos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==event.ACTION_MOVE){
                    Toast.makeText(getApplicationContext(), "Delizando", Toast.LENGTH_LONG).show();

                }
                return false;
            }
        });
*/


    }

    public void ocultarTeclado(View v) {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void meterNuevo(View v) {
        String titu, direc;

        titu = etTitulo.getText().toString();
        direc = etURL.getText().toString();

        lista.add(new Datos(lista.size() + 1, titu, "http://" + direc + "/"));

        //Para actualizar el ListView tras la inserción, establecemos de nuevo el adaptador
        Adaptador miAdaptador = new Adaptador(getApplicationContext(), lista);
        listaDatos.setAdapter(miAdaptador);

    }

    public void borrarElemento(View v) {
        int titus;
        titus = Integer.valueOf(etBorrar.getText().toString()) - 1;
        if (lista.size() < titus + 1 || titus <= 0) //En realidad negativo no será admitido por el propio tipo de entrada elegido para el EditeText
        {
            Toast toast1 = Toast.makeText(getApplicationContext(), "ERROR, numero invalido", Toast.LENGTH_SHORT);
            toast1.show();
        } else {
            lista.remove(titus);
        }

        //Ahora debemos actualizar los números asociados a cada URL
        //Utilizaremos un array auxiliar sobre el que cargaremos los datos del original, excepto su numero, que será una secuencia natural empezando en 1 (1, 2, 3, 4, ...)
        ArrayList<Datos> listaAux = new ArrayList<Datos>();
        for (int i = 0; i < lista.size(); i++) {
            listaAux.add(new Datos(i + 1, lista.get(i).getNombre(), lista.get(i).getURL()));//get(i).setId(i+1);
            //listaAux.get(i).setNombre(lista.get(i).getNombre());
            //listaAux.get(i).setURL(lista.get(i).getURL());
        }
        //Ahora volcamos la lista auxiliar sobre la lista original
        lista = listaAux;

        //Para actualizar el ListView tras el borrado, establecemos de nuevo el adaptador
        Adaptador miAdaptador = new Adaptador(getApplicationContext(), lista);
        listaDatos.setAdapter(miAdaptador);
    }


    public void borrarElemento(int pos) {
        lista.remove(pos);
        //Ahora debemos actualizar los números asociados a cada URL
        //Utilizaremos un array auxiliar sobre el que cargaremos los datos del original, excepto su numero, que será una secuencia natural empezando en 1 (1, 2, 3, 4, ...)
        ArrayList<Datos> listaAux = new ArrayList<Datos>();
        for (int i = 0; i < lista.size(); i++) {
            listaAux.add(new Datos(i + 1, lista.get(i).getNombre(), lista.get(i).getURL()));
        }
        //Ahora volcamos la lista auxiliar sobre la lista original
        lista = listaAux;

        //Para actualizar el ListView tras el borrado, establecemos de nuevo el adaptador
        Adaptador miAdaptador = new Adaptador(getApplicationContext(), lista);
        listaDatos.setAdapter(miAdaptador);
    }


    public void visitar(int pos) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(lista.get(pos).getURL()));
        startActivity(intent);
    }


    public void subirElemento(int pos) {
        if (pos != 0) {
            Datos registroAux = new Datos(lista.get(pos).getId(), lista.get(pos).getNombre(), lista.get(pos).getURL());
            lista.set(pos, lista.get(pos - 1));
            lista.set(pos - 1, registroAux);
        } else
            Toast.makeText(getApplicationContext(), "No puede subir más la URL", Toast.LENGTH_LONG).show();

        //Ahora debemos actualizar los números asociados a cada URL
        //Utilizaremos un array auxiliar sobre el que cargaremos los datos del original, excepto su numero, que será una secuencia natural empezando en 1 (1, 2, 3, 4, ...)
        ArrayList<Datos> listaAux = new ArrayList<Datos>();
        for (int i = 0; i < lista.size(); i++) {
            listaAux.add(new Datos(i + 1, lista.get(i).getNombre(), lista.get(i).getURL()));//get(i).setId(i+1);
            //listaAux.get(i).setNombre(lista.get(i).getNombre());
            //listaAux.get(i).setURL(lista.get(i).getURL());
        }
        //Ahora volcamos la lista auxiliar sobre la lista original
        lista = listaAux;

        //Para actualizar el ListView tras el borrado, establecemos de nuevo el adaptador
        Adaptador miAdaptador = new Adaptador(getApplicationContext(), lista);
        listaDatos.setAdapter(miAdaptador);
    }

    public void bajarElemento(int pos) {
        if (pos != lista.size() - 1) {
            Datos registroAux = new Datos(lista.get(pos).getId(), lista.get(pos).getNombre(), lista.get(pos).getURL());
            lista.set(pos, lista.get(pos + 1));
            lista.set(pos + 1, registroAux);
        } else
            Toast.makeText(getApplicationContext(), "No puede bajar más la URL", Toast.LENGTH_LONG).show();

        //Ahora debemos actualizar los números asociados a cada URL
        //Utilizaremos un array auxiliar sobre el que cargaremos los datos del original, excepto su numero, que será una secuencia natural empezando en 1 (1, 2, 3, 4, ...)
        ArrayList<Datos> listaAux = new ArrayList<Datos>();
        for (int i = 0; i < lista.size(); i++) {
            listaAux.add(new Datos(i + 1, lista.get(i).getNombre(), lista.get(i).getURL()));//get(i).setId(i+1);
            //listaAux.get(i).setNombre(lista.get(i).getNombre());
            //listaAux.get(i).setURL(lista.get(i).getURL());
        }
        //Ahora volcamos la lista auxiliar sobre la lista original
        lista = listaAux;

        //Para actualizar el ListView tras el borrado, establecemos de nuevo el adaptador
        Adaptador miAdaptador = new Adaptador(getApplicationContext(), lista);
        listaDatos.setAdapter(miAdaptador);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Elija qué desea hacer:");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemVisitar:
                visitar(posicion);
                Toast.makeText(getApplicationContext(), "Visitando", Toast.LENGTH_LONG).show();
                break;
            case R.id.itemEliminar:
                borrarElemento(posicion);
                Toast.makeText(getApplicationContext(), "Registro eliminado", Toast.LENGTH_LONG).show();
                break;
            case R.id.itemSubir:
                subirElemento(posicion);
                break;
            case R.id.itemBajar:
                bajarElemento(posicion);
                break;
        }
        return true;
    }


    //Tengo que incorporar la clase Adaptador dentro de la clase MainActivity para que cuando haga el borrado
    //por deslizamiento (setOnTouchListener()), podamos acceder al método borrarElemento. Si se encontrara
    //en un fichero aparte, tendría problemas de visibilidad y no podrá acceder a dicho método
    public class Adaptador extends BaseAdapter {

        Context contexto; //contexto de la aplicacion
        List<Datos> ListaObjetos; //lista de datos a generar. Podemos usar tb un ArrayList
        Datos dt;

        public Adaptador(Context contexto, List<Datos> listaObj) {
            this.contexto = contexto;
            ListaObjetos = listaObj;
        }

        @Override
        public int getCount() {
            return ListaObjetos.size();
        }

        @Override
        public Object getItem(int i) {
            return ListaObjetos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return ListaObjetos.get(i).getId();
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View vista = view;
            LayoutInflater inflate = LayoutInflater.from(contexto);
            vista = inflate.inflate(R.layout.itemlist, null);


            TextView titulo = (TextView) vista.findViewById(R.id.tvNombre);
            final TextView detalle = (TextView) vista.findViewById(R.id.tvURL);
            TextView numero = (TextView) vista.findViewById(R.id.tvId);


            titulo.setText(ListaObjetos.get(i).getNombre().toString());
            detalle.setText(ListaObjetos.get(i).getURL().toString());
            numero.setText(String.valueOf(ListaObjetos.get(i).getId()));


            //Darse cuenta como para hacer el OnClick sobre el campo URL (no sobre todo el item del ListView), trato
            //el evento en el Adaptador. Sin embargo, para tratar el evento OnItemLongClick sobre todo el item
            // del Listview (sobre cualquiera de ellos), trato el evento en el MainActivity
            detalle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(detalle.getText().toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                    contexto.startActivity(intent);
                }
            });

            final View finalVista = vista;
            vista.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == event.ACTION_MOVE) {
                        borrarElemento(i);
                        Toast.makeText(finalVista.getContext(), "Elemento borrado", Toast.LENGTH_LONG).show();
                    }

                    return false;
                }
            });

            return vista;
        }
    }
}