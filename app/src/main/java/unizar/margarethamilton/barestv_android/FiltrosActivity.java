package unizar.margarethamilton.barestv_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.util.HashMap;
import java.util.List;

import unizar.margarethamilton.connection.ClienteRest;
import unizar.margarethamilton.listViewConfig.ListHashAdapter;


public class FiltrosActivity extends AppCompatActivity {
    private static ClienteRest clienteRest;
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
        GridView categories = (GridView) findViewById(R.id.cat_grid);
        clienteRest = new ClienteRest();
        List<HashMap<String, String>> programacion = clienteRest.getProgramacion();

        // Crear un array donde se especifica los datos que se quiere mostrar
        String[] from = new String[] {"Categoria"};

        // Crear un array done sse especifica los campos de ListView que se quiere rellenar
        int[] to = new int[] { R.id.cat};

        categories.setAdapter(new ListHashAdapter(this, R.layout.category_grid,
                programacion, from, to));

    }

    @Override
    public void finish() {
        // Prepare data intent
        Intent data = new Intent();
        data.putExtra("Tab",1);
        // Activity finished ok, return the data
        setResult(RESULT_OK, data);
        super.finish();
    }

}
