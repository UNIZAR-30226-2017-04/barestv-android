package unizar.margarethamilton.barestv_android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import unizar.margarethamilton.connection.ClienteRest;
import unizar.margarethamilton.listViewConfig.ListHashAdapter;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ivo on 21/03/17.
 */

public class BusquedaFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "clienteRest";

    private SearchView searchView;
    private TextView listText;
    private Toolbar mToolbar;
    private ListView mList;
    private boolean hayBusqueda = false;
    private boolean hayFiltro = false;
    private boolean hayFiltroFecha = false;
    private String ultimaBusqueda = "";
    private String filtroCategoria="";

    private String filtroFechaDia;
    private String filtroFechaMes;
    private String filtroFechaAño;

    private OnFragmentInteractionListener mListener;
    private static ClienteRest clienteRest;

    public BusquedaFragment(){}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param _clienteRest _clienteRest API para conectar a la BBDD remota.
     * @return A new instance of fragment BusquedaFragment.
     */
    public static BusquedaFragment newInstance(ClienteRest _clienteRest) {
        BusquedaFragment fragment = new BusquedaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, _clienteRest);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clienteRest = (ClienteRest) getArguments().getSerializable(ARG_PARAM1);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.busqueda_fragment_layout, container, false);
        mList = (ListView) view.findViewById(R.id.resList);
        searchView = (SearchView) view.findViewById(R.id.sview);
        searchView.setIconifiedByDefault(false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_toolbar);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                abrirFiltro();
                return false;
            }
        });
        searchView.setQueryHint(getString(R.string.search_hint));

        listText = (TextView) view.findViewById(R.id.listText);
        listText.setText(R.string.progProx);
        //Se obtiene la programacion proxima
        final ArrayAdapter adapter = progProx();

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshProx);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        ArrayAdapter adapter = progProx();
                        mList.setAdapter(adapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );



        mList.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listText.setText("Búsqueda: "+query);
                ArrayAdapter adapter = busqueda(query);
                mList.setAdapter(adapter);
                swipeRefreshLayout.setEnabled(false);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                listText.setText(newText);
                return false;
            }
        });
        ImageView closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);

        // Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            listText.setText(R.string.progProx);
                searchView.setQuery("",false);
                ArrayAdapter adapter = progProx();
                mList.setAdapter(adapter);
                swipeRefreshLayout.setEnabled(true);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                return false;
            }
        });

        return view;
    }

    private void abrirFiltro() {
        Intent i = new Intent(getActivity(), FiltrosActivity.class);
        i.putExtra("ClitenteRest",clienteRest);
        startActivityForResult(i, 1);
    }

    public ArrayAdapter progProx(){
        //TODO: Se llama a la API para que devuelva la programacion proxima
        // Obtiene del BD remoto las programaciones destacadas
        List<HashMap<String, String>> programacion = clienteRest.getProgramacion();
        hayBusqueda=false;
        // Crear un array donde se especifica los datos que se quiere mostrar
        String[] from = new String[] { "Titulo", "Categoria", "Bar", "Descr", "Inicio", "Fin"};

        // Crear un array done sse especifica los campos de ListView que se quiere rellenar
        int[] to = new int[] { R.id.titulo , R.id.categoria, R.id.bar, R.id.descr,
                R.id.inicio, R.id.fin};
        ListHashAdapter res = new ListHashAdapter(this.getActivity(), R.layout.destacado_listview_content,
                programacion, from, to);
        if(hayFiltro){
            quitarFiltros();
            List<HashMap<String, String>> programacionFiltrada = new ArrayList<HashMap<String, String>>();
            if(!filtroCategoria.isEmpty()) {
                for (HashMap<String, String> e : programacion) {
                    if (e.containsValue(filtroCategoria)) {
                        programacionFiltrada.add(e);
                    }
                }
                // Configurar el adapter
                res =  new ListHashAdapter(this.getActivity(), R.layout.destacado_listview_content,
                        programacionFiltrada, from, to);
            }
        }
        return res;
    }


    public ArrayAdapter busqueda(String query){
        //TODO: Llama a la API para realizar la busqueda, devuelve List<HashMap<String, String>>
        List<HashMap<String, String>> programacion = clienteRest.getProgramacionDadoBar("Dantis");
        String[] from = new String[] { "Titulo", "Categoria", "Bar", "Descr", "Inicio", "Fin"};

        int[] to = new int[] { R.id.titulo , R.id.categoria, R.id.bar, R.id.descr,
                R.id.inicio, R.id.fin};
        hayBusqueda=true;
        ultimaBusqueda=query;
        ListHashAdapter res = new ListHashAdapter(this.getActivity(), R.layout.destacado_listview_content,
                programacion, from, to);
        if(hayFiltro){
            quitarFiltros();
            List<HashMap<String, String>> programacionFiltrada = new ArrayList<HashMap<String, String>>();
            if(!filtroCategoria.isEmpty()) {
                for (HashMap<String, String> e : programacion) {
                    if (e.containsValue(filtroCategoria)) {
                        programacionFiltrada.add(e);
                    }
                }
                // Configurar el adapter
                res = new ListHashAdapter(this.getActivity(), R.layout.destacado_listview_content,
                        programacionFiltrada, from, to);
            }else if(hayFiltroFecha){
                List<HashMap<String, String>> programacionConFecha = new ArrayList<HashMap<String, String>>();
                if(programacionFiltrada.isEmpty()){
                    programacionFiltrada=programacion;
                }
                for (HashMap<String, String> e : programacionFiltrada) {
                    String inicio=e.get("Inicio");
                    String fin=e.get("Fin");
                    String fechaInicio=inicio.split(" ")[0];
                    String fechaFin=fin.split(" ")[0];
                    e.put("FechaInicio",fechaInicio);
                    e.put("FechaFin",fechaFin);
                    programacionConFecha.add(e);
                }
                List<HashMap<String, String>> programacionFiltrada2 = new ArrayList<HashMap<String, String>>();
                for (HashMap<String, String> e : programacionConFecha) {
                    if (e.containsValue(filtroFechaAño+"-"+
                                        filtroFechaMes+"-"+
                            filtroFechaDia)) {
                        programacionFiltrada2.add(e);
                    }
                }
                res = new ListHashAdapter(this.getActivity(), R.layout.destacado_listview_content,
                        programacionFiltrada2, from, to);
            }

        }
        return res;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void quitarFiltros(){
        Snackbar snackbar = Snackbar
                .make(mList, "Filtros Activos", Snackbar.LENGTH_LONG)
                .setAction("Quitar Filtros", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar snackbar1 = Snackbar.make(getView(), "Filtros eliminados", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                        hayFiltro=false;
                        hayFiltroFecha=false;
                        filtroCategoria="";
                        if(hayBusqueda){
                            mList.setAdapter(busqueda(ultimaBusqueda));
                        }else {
                            mList.setAdapter(progProx());
                        }
                    }
                });
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }
/*Aplica el filtros a la programacion resultado*/
    public void aplicarFiltros(String categoria, int dia, int mes, int año){
        if(!categoria.isEmpty()) {
            filtroCategoria = categoria;
        }
        if(dia!=0){
            hayFiltroFecha=true;
            filtroFechaDia=String.valueOf(dia);
            filtroFechaMes=String.valueOf(mes+1);
            filtroFechaAño=String.valueOf(año);
            if(dia<10){
                filtroFechaDia="0"+filtroFechaDia;
            }
            if(mes<10){
                filtroFechaMes="0"+filtroFechaMes;
            }
        }
        ArrayAdapter adapter;
        if(hayBusqueda){
             adapter = busqueda(ultimaBusqueda);
        }else {
             adapter = progProx();
        }
        mList.setAdapter(adapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        //progProx();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
//            if (data.hasExtra("Tab")) {
//                mViewPager.setCurrentItem(3);
//            }

            if(data.hasExtra("FiltroCategoria")){
                hayFiltro=true;
               aplicarFiltros(data.getStringExtra("FiltroCategoria"),data.getIntExtra("FiltroFechaDia",0),
                                data.getIntExtra("FiltroFechaMes",0),
                                data.getIntExtra("FiltroFechaAño",0));
            }else{
                hayFiltro=true;
                aplicarFiltros("",data.getIntExtra("FiltroFechaDia",0),
                        data.getIntExtra("FiltroFechaMes",0),
                        data.getIntExtra("FiltroFechaAño",0));
            }
    }
}

