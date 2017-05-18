package unizar.margarethamilton.barestv_android;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import unizar.margarethamilton.connection.ClienteRest;

@RuntimePermissions
public class MapaFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private static final String ARG_PARAM1 = "clientRest";
    private ClienteRest clienteRest;
    private Snackbar snackbar = null;

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    View view;

    OnFragmentInteractionListener mListener;


    public static MapaFragment newInstance(ClienteRest _clienteRest) {
        MapaFragment fragment = new MapaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, _clienteRest);
        fragment.setArguments(args);

        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null) {
            view = inflater.inflate(R.layout.activity_maps, container, false);
        }

        if (TextUtils.isEmpty(getResources().getString(R.string.google_maps_api_key))) {
            throw new IllegalStateException("You forgot to supply a Google Maps API key");
        }

        if (getArguments() != null) {
            clienteRest = (ClienteRest) getArguments().getSerializable(ARG_PARAM1);
        }

        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {loadMap(map);
                }

            });

        } else {
            Toast.makeText(getActivity(), "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
            Toast.makeText(getActivity(), "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            MapaFragmentPermissionsDispatcher.getMyLocationWithCheck(this);
            LatLng barejemplo = new LatLng(46, 0);
            googleMap.addMarker(new MarkerOptions().position(barejemplo)
                    .title("barejemplo"));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    mListener.barPulsado(marker.getTitle());
                    return false;
                }
            });

        } else {
            Toast.makeText(getActivity(), "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MapaFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @SuppressWarnings("all")
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void getMyLocation() {
        if (map != null) {
            // Now that map has loaded, let's get our location!
            if (mGoogleApiClient==null){
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();
            }
            map.setMyLocationEnabled(true);
            connectClient();
        }
    }

    protected void connectClient() {
        // Connect the client.
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    /*
     * Called when the Activity becomes visible.
    */
    @Override
    public void onStart() {
        super.onStart();
        connectClient();
    }

    /*
	 * Called when the Activity is no longer visible.
	 */
    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener=null;
    }

    /*Interfaz para comunicar a la busqueda el bar pulsado*/
    public interface OnFragmentInteractionListener {
        void barPulsado(String bar);
    }
    /*
     * Handle results returned to the FragmentActivity by Google Play services
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mGoogleApiClient.connect();
                        break;
                }

        }
    }

    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getFragmentManager(), "Location Updates");
            }

            return false;
        }
    }

    /*
     * Called by Location Services when the request to connect the client
     * finishes successfully. At this point, you can request the current
     * location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        if (mGoogleApiClient==null){
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
        }
        mGoogleApiClient.connect();
        startLocationUpdates();
        // Display the connection status
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            map.clear();
            Toast.makeText(getActivity(), "GPS location was found!", Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            map.animateCamera(cameraUpdate);
            añadirMarcadores(location.getLatitude(),location.getLongitude());
        } else {
            Toast.makeText(getActivity(), "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

    }

    /*
     * Called by Location Services if the connection to the location client
     * drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(getActivity(), "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(getActivity(), "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * Called by Location Services if the attempt to Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(),
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(),
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    public void onDestroy()
    {

        super.onDestroy();

        Fragment fragment = (getFragmentManager()
                .findFragmentById(R.id.map));
        FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    protected void añadirMarcadores(double latitud, double longitud) {
// Eliminamos el snackbar anterior si no esta elinimado
        if (snackbar != null) snackbar.dismiss();

        // Obtiene del BBDD remoto las programaciones destacadas
        List<HashMap<String, String>> bares = clienteRest.getBares(latitud,longitud,100);

        // Si no se ha podido establecer la conexion
        if (bares == null) {
            try {
                // Mensaje error en caso de no poder conectar con la BBDD
                snackbar = Snackbar.make(view, R.string.error_conexion, Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null);
                snackbar.show();
            } catch (Exception x) {
                x.printStackTrace();
            }
        } else {
            String nombre;
            Double lat;
            Double lng;
            for (int i = 0; i < bares.size(); i++) {
                nombre = bares.get(i).get("Nombre");
                lat = Double.parseDouble(bares.get(i).get("Lat"));
                lng = Double.parseDouble(bares.get(i).get("Lng"));
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(nombre));
            }
        }
    }

}
