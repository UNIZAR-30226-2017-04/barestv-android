package unizar.margarethamilton.barestv_android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import unizar.margarethamilton.connection.ClienteRest;
import unizar.margarethamilton.listAdapter.ListHashAdapter;

/**
 * Created by ivo on 21/03/17.
 */

public class BusquedaFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "clienteRest";

    private SearchView searchView;
    private TextView listText;

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
        searchView = (SearchView) view.findViewById(R.id.sview);
        searchView.setIconifiedByDefault(false);
        //searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint(getString(R.string.search_hint));
        listText = (TextView) view.findViewById(R.id.listText);
        listText.setText(R.string.progProx);

        // Obtiene del BD remoto las programaciones destacadas
        List<HashMap<String, String>> programacion = clienteRest.getProgramacion();

        // Crear un array donde se especifica los datos que se quiere mostrar
        String[] from = new String[] { "Titulo", "Categoria", "Bar", "Descr", "Inicio", "Fin"};

        // Crear un array done sse especifica los campos de ListView que se quiere rellenar
        int[] to = new int[] { R.id.titulo , R.id.categoria, R.id.bar, R.id.descr,
                R.id.inicio, R.id.fin};

        // Configurar el adapter
        ArrayAdapter adapter = new ListHashAdapter(this.getActivity(), R.layout.destacado_listview_content,
                programacion, from, to);

        ListView mList = (ListView) view.findViewById(R.id.resList);
        mList.setAdapter(adapter);

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
}

