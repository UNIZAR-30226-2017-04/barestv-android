package unizar.margarethamilton.barestv_android;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import unizar.margarethamilton.connection.ClienteRest;
import unizar.margarethamilton.dataBase.FavoritosDbAdapter;

public class activity_tabs extends AppCompatActivity implements
        MapaFragment.OnFragmentMapaInteractionListener,
        BusquedaFragment.OnFragmentBusquedaInteractionListener,
        DestacadoFragment.OnFragmentDestacadoInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ClienteRest clienteRest;
    private BusquedaFragment busquedaFragment;
    private DestacadoFragment destacadoFragment;
    private FavoritosFragment favoritosFragment;
    private MapaFragment mapaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        // Create the adapter that will return a fragment for each of the four
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(mSectionsPagerAdapter.getTabView(i));
        }

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        FavoritosDbAdapter mdb = new FavoritosDbAdapter(this);
        clienteRest = new ClienteRest(this);

        destacadoFragment = DestacadoFragment.newInstance(clienteRest);
        busquedaFragment = BusquedaFragment.newInstance(clienteRest);
        favoritosFragment = FavoritosFragment.newInstance(clienteRest);
        mapaFragment = MapaFragment.newInstance(clienteRest);
    }


    public void destacadosClick (View v) {
        destacadoFragment.destacadosClick(v);
    }

    public void busquedaClick (View v) {
        busquedaFragment.busquedaClick(v);
    }

    public void favoritosClick (View v) {
        favoritosFragment.favoritosClick(v);
    }
    /*
        Metodos de la interfaz de comunicacion busqueda-mapa
     */

    //Interfaz OnFragmentMapaInteractionListener
    //Bar pulsado en mapa para buscar sus programas
    public void barPulsado(String bar){
        mViewPager.setCurrentItem(1);
        busquedaFragment.programacionBar(bar);

    }

    //Interfaz OnFragmentBusquedaInteractionListener
    //Programa pulsado en busqueda para mostrar el bar
    public void programaPulsado(String bar){
        mViewPager.setCurrentItem(3);
        mapaFragment.mostrarBar(bar);
    }

    //Interfaz OnFragmentDestacadoInteractionListener
    //Programa pulsado en destacados para mostrar el bar
    public void programaPulsadoDes(String bar){
        mViewPager.setCurrentItem(3);
        mapaFragment.mostrarBar(bar);
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return destacadoFragment;
                case 1:
                    return  busquedaFragment;
                case 2:
                    return favoritosFragment;
                case 3: // Solo utilizar cuando los mapas estén implementados
                    return mapaFragment; // Solo utilizar cuando los mapas estén implementados
            }
            return null;
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4; //Solo utilizar cuando los mapas estén implementados
            //return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "DESTACADOS";
                case 1:
                    return "BÚSQUEDA";
                case 2:
                    return "FAVORITOS";
                case 3:
                    return "MAPA";
            }
            return null;
        }
        public View getTabView(int position) {
            View tab = LayoutInflater.from(activity_tabs.this).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setText(getPageTitle(position));
            return tab;
        }
    }
}
