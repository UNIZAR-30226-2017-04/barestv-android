package unizar.margarethamilton.barestv_android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import unizar.margarethamilton.connection.ClienteRest;
import unizar.margarethamilton.listViewConfig.ListHashAdapter;


public class FiltrosActivity extends AppCompatActivity {
    private static ClienteRest clienteRest;

    private View ultimaCategoria =null;

    private int numCat = -1;
    private boolean hayFecha = false;
    private boolean hayCategoria = false;

    private Snackbar snackbar;
    private Calendar ultimaFecha=Calendar.getInstance();
    final Calendar fechaActual = Calendar.getInstance();
    private GridView categories;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);
        view=findViewById(R.id.filtros);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        categories = (GridView) findViewById(R.id.cat_grid);
        DatePicker fecha = (DatePicker) findViewById(R.id.datePicker);
        fecha.init(fechaActual.get(Calendar.YEAR), fechaActual.get(Calendar.MONTH), fechaActual.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                hayFecha=true;
                ultimaFecha.set(year,monthOfYear,dayOfMonth);

            }
        });
        Bundle extras = getIntent().getExtras();
        clienteRest=(ClienteRest) extras.get("ClitenteRest");
        //clienteRest = new ClienteRest();
//        List<HashMap<String, String>> programacion = clienteRest.getProgramacion();
//
//        // Crear un array donde se especifica los datos que se quiere mostrar
//        String[] from = new String[] {"Categoria"};
//
//        // Crear un array done sse especifica los campos de ListView que se quiere rellenar
//        int[] to = new int[] { R.id.cat};
//
//        categories.setAdapter(new ListHashAdapter(this, R.layout.category_grid,
//                programacion, from, to));

        new SetCategoryTask(this).execute();

        categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(ultimaCategoria !=null) {
                    View oldv = ultimaCategoria;
                    //oldv.findViewById(R.id.cat);
                    TextView oldtv = (TextView) oldv.findViewById(R.id.cat);
                    oldtv.setBackground(getResources().getDrawable(R.drawable.grid_round_corners));
                    View newv = categories.getAdapter().getView(position,view,parent);
                    TextView newtv = (TextView) newv.findViewById(R.id.cat);
                    if(numCat!=position) {
                        newtv.setBackground(getResources().getDrawable(R.drawable.grid_round_corners_selected));
                        numCat=position;
                        ultimaCategoria =newv;
                        hayCategoria=true;
                    }else {
                        numCat=-1;
                        ultimaCategoria=null;
                        hayCategoria=false;
                    }

                }else {
                    View newv = categories.getAdapter().getView(position, view, parent);
                    //newv.findViewById(R.id.cat);
                    TextView newtv = (TextView) newv.findViewById(R.id.cat);
                    newtv.setBackground(getResources().getDrawable(R.drawable.grid_round_corners_selected));
                    numCat = position;
                    ultimaCategoria = newv;
                    hayCategoria=true;
                }


            }
        });

    }

    @Override
    public void finish() {
        // Prepare data intent
        Intent data = new Intent();
        if (ultimaFecha.compareTo(fechaActual)==0){
            hayFecha=false;
        }
        if(hayFecha){
            data.putExtra("FiltroFechaDia",ultimaFecha.get(Calendar.DAY_OF_MONTH));
            data.putExtra("FiltroFechaMes",ultimaFecha.get(Calendar.MONTH));
            data.putExtra("FiltroFechaAño",ultimaFecha.get(Calendar.YEAR));
        }
        if(hayCategoria){
            data.putExtra("FiltroCategoria",((TextView) ultimaCategoria.findViewById(R.id.cat)).getText());
        }
        data.putExtra("Tab",1);
        // Activity finished ok, return the data
        setResult(1, data);
        super.finish();
    }

    private class SetCategoryTask extends AsyncTask<Void, Void, ArrayAdapter> {

        private Activity thisActivity;

        public SetCategoryTask(Activity a){
            thisActivity=a;
        }
        /**
         * Comunicacion asincrona
         * @param e void
         * @return ArrayAdapter
         */
        protected ArrayAdapter doInBackground(Void... e) {

            // Eliminamos el snackbar anterior si no esta elinimado
            if (snackbar != null) snackbar.dismiss();

            // Obtiene del BBDD remoto las programaciones destacadas
            List<HashMap<String, String>> categorias = clienteRest.getCategorias();
            // Si no se ha podido establecer la conexion
            if (categorias == null) return null;

            // Crear un array donde se especifica los datos que se quiere mostrar
            String[] from = new String[] {"Categoria"};

            // Crear un array done sse especifica los campos de ListView que se quiere rellenar
            int[] to = new int[] { R.id.cat};


            return new ListHashAdapter(thisActivity, R.layout.category_grid,
                    categorias, from, to);
        }

        /**
         * Una vez obtenido los datos, se rellena el listview
         * @param adapter
         */
        protected void onPostExecute(ArrayAdapter adapter) {
            if (adapter == null) {
                try {
                    // Mensaje error en caso de no poder conectar con la BBDD
                    snackbar = Snackbar.make(view, R.string.error_conexion, Snackbar.LENGTH_INDEFINITE)
                            .setAction("Action", null);
                    snackbar.show();
                } catch (Exception x) { x.printStackTrace();}
            } else {
                categories.setAdapter(adapter);
            }
        }
    }
}
