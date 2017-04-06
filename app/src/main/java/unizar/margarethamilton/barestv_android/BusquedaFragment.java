package unizar.margarethamilton.barestv_android;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by ivo on 21/03/17.
 */

public class BusquedaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SearchView searchView;
    private TextView listText;
    private TestDbAdapter mDbHelper; // Acceso a BBDD

    private OnFragmentInteractionListener mListener;

    public BusquedaFragment(){}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlantillaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BusquedaFragment newInstance(String param1, String param2) {
        BusquedaFragment fragment = new BusquedaFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.busqueda_fragment_layout, container, false);
        searchView = (SearchView) view.findViewById(R.id.sview);
        searchView.setIconifiedByDefault(false);
        //searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint(getString(R.string.search_hint));
        listText = (TextView) view.findViewById(R.id.listText);
        listText.setText(R.string.progProx);
        Cursor programsCursor = mDbHelper.fetchProgProx();
        //startManagingCursor(programsCursor);

        // Create an array to specify the fields we want to display in the list
        String[] from = new String[] { TestDbAdapter.KEY_TITULO, TestDbAdapter.KEY_BAR,
                TestDbAdapter.KEY_DESCR, TestDbAdapter.KEY_INICIO,  TestDbAdapter.KEY_FIN};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.titulo , R.id.bar, R.id.descr, R.id.inicio, R.id.fin};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(getActivity(), R.layout.busqueda_listview_content, programsCursor, from, to);
        ListView mList = (ListView) view.findViewById(R.id.resList);
        mList.setAdapter(notes);

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

