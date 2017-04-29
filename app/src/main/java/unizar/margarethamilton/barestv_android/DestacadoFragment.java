package unizar.margarethamilton.barestv_android;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;

import unizar.margarethamilton.connection.ClienteRest;
import unizar.margarethamilton.listViewConfig.ListHashAdapter;
import unizar.margarethamilton.listViewConfig.Params;
import unizar.margarethamilton.listViewConfig.SetListViewTask;

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
        View view = inflater.inflate(R.layout.destacado_fragment_layout, container, false);

        final ListView mList = (ListView) view.findViewById(R.id.list);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setRefreshing(true);

        final Params params = new Params(this, swipeRefreshLayout, clienteRest, mList);
        new SetListViewTask().execute(params);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        new SetListViewTask().execute(params);

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



}
