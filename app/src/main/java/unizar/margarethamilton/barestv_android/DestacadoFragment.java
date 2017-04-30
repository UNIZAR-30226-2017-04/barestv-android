package unizar.margarethamilton.barestv_android;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import unizar.margarethamilton.connection.ClienteRest;
import unizar.margarethamilton.listViewConfig.ListHashAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DestacadoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DestacadoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DestacadoFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "clientRest";


    private OnFragmentInteractionListener mListener;
    private ClienteRest clienteRest;
    private View view ;
    private ListView mList;
    private SwipeRefreshLayout swipeRefreshLayout;


    public DestacadoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param _clienteRest API para conectar a la BBDD remota.
     * @return A new instance of fragment DestacadoFragment.
     */
    public static DestacadoFragment newInstance(ClienteRest _clienteRest) {
        DestacadoFragment fragment = new DestacadoFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.destacado_fragment_layout, container, false);

        mList = (ListView) view.findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setRefreshing(true);

        new SetPDestacadosTask().execute();

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        new SetPDestacadosTask().execute();

                    }
                }
        );
        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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


    /**
     * Rellena el listview con datods dados por el API de forma asincrona
     */
    private class SetPDestacadosTask extends AsyncTask<Void, Void, ArrayAdapter> {

        /**
         * Comunicacion asincrona
         * @param e
         * @return
         */
        protected ArrayAdapter doInBackground(Void... e) {
            if(DestacadoFragment.this.getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                DestacadoFragment.this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else DestacadoFragment.this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            // Obtiene del BBDD remoto las programaciones destacadas
            List<HashMap<String, String>> programacion = clienteRest.getProgramacionDestacada();
            if (programacion == null) return null;
            // Crear un array donde se especifica los datos que se quiere mostrar
            String[] from = new String[] { "Titulo", "Categoria", "Bar", "Descr", "Inicio", "Fin"};

            // Crear un array donde se especifica los campos de ListView que se quiere rellenar
            int[] to = new int[] { R.id.titulo , R.id.categoria, R.id.bar, R.id.descr,
                    R.id.inicio, R.id.fin};

            // Configurar el adapter
            ArrayAdapter adapter = new ListHashAdapter(DestacadoFragment.this.getActivity(), R.layout.destacado_listview_content,
                        programacion, from, to);

            return adapter;
        }

        /**
         * Una vez obtenido los datos, se rellena el listview
         * @param adapter
         */
        protected void onPostExecute(ArrayAdapter adapter) {
            if (adapter == null) {
                Snackbar.make(view, "No se ha podido establecer la conexion con la base de datos remota", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null).show();
                swipeRefreshLayout.setRefreshing(false);
            } else {
                mList.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }
            DestacadoFragment.this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

}
