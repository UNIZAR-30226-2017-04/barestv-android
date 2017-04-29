package unizar.margarethamilton.listViewConfig;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

import unizar.margarethamilton.barestv_android.DestacadoFragment;
import unizar.margarethamilton.barestv_android.R;


public class SetListViewTask extends AsyncTask<Params, Void, Params> {
    protected Params doInBackground(Params... e) {
        // Obtiene del BBDD remoto las programaciones destacadas
        List<HashMap<String, String>> programacion = e[0].getClienteRest().getProgramacionDestacada();

        // Crear un array donde se especifica los datos que se quiere mostrar
        String[] from = new String[] { "Titulo", "Categoria", "Bar", "Descr", "Inicio", "Fin"};

        // Crear un array donde se especifica los campos de ListView que se quiere rellenar
        int[] to = new int[] { R.id.titulo , R.id.categoria, R.id.bar, R.id.descr,
                R.id.inicio, R.id.fin};

        // Configurar el adapter
        ArrayAdapter adapter = new ListHashAdapter(e[0].getFragment().getActivity(), R.layout.destacado_listview_content,
                programacion, from, to);
        e[0].setAdapter(adapter);


        return e[0];
    }


    protected void onPostExecute(Params params) {
        params.getView().setAdapter(params.getAdapter());
        params.getSwipeRefreshLayout().setRefreshing(false);
    }


}
