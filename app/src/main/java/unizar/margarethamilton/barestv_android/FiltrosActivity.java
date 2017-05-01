package unizar.margarethamilton.barestv_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;

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
    private Calendar ultimaFecha=Calendar.getInstance();
    final Calendar fechaActual = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);
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
        final GridView categories = (GridView) findViewById(R.id.cat_grid);
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
        List<HashMap<String, String>> programacion = clienteRest.getProgramacion();

        // Crear un array donde se especifica los datos que se quiere mostrar
        String[] from = new String[] {"Categoria"};

        // Crear un array done sse especifica los campos de ListView que se quiere rellenar
        int[] to = new int[] { R.id.cat};

        categories.setAdapter(new ListHashAdapter(this, R.layout.category_grid,
                programacion, from, to));

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
            data.putExtra("FiltroFecha",ultimaFecha);
        }
        if(hayCategoria){
            data.putExtra("FiltroCategoria",((TextView) ultimaCategoria.findViewById(R.id.cat)).getText());
        }
        data.putExtra("Tab",1);
        // Activity finished ok, return the data
        setResult(1, data);
        super.finish();
    }

}
