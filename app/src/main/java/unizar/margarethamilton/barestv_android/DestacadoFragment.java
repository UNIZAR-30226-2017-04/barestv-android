package unizar.margarethamilton.barestv_android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.view.ViewGroup;

import junit.framework.Test;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DestacadoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DestacadoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DestacadoFragment extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TestDbAdapter mDbHelper; // Acceso a BBDD

    public DestacadoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlantillaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DestacadoFragment newInstance(String param1, String param2) {
        DestacadoFragment fragment = new DestacadoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mDbHelper = new TestDbAdapter(getActivity());
        mDbHelper.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.destacado_fragment_layout, container, false);

        populateListView();

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        populateListView();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        return view;
    }

    /**
     *  Rellena la lista de destacados con los datos de la BBDD
     */
    private void populateListView () {
        // Get all of the programs from the database and create the item list
        Cursor programsCursor = mDbHelper.fetchAllPrograms();
        //startManagingCursor(programsCursor);

        // Create an array to specify the fields we want to display in the list
        String[] from = new String[] { TestDbAdapter.KEY_TITULO, TestDbAdapter.KEY_BAR,
                TestDbAdapter.KEY_DESCR, TestDbAdapter.KEY_INICIO,  TestDbAdapter.KEY_FIN};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.titulo , R.id.bar, R.id.descr, R.id.inicio, R.id.fin};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(getActivity(), R.layout.destacado_listview_content, programsCursor, from, to);
        setListAdapter(notes);
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
