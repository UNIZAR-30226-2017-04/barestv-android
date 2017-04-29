package unizar.margarethamilton.listViewConfig;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import unizar.margarethamilton.connection.ClienteRest;

/**
 * Created by Xian on 2017/4/29.
 */

/**
 * Parametros que se pasan al task cuando se configura el ListView
 */
public class Params {
    private Fragment Fragment;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayAdapter adapter;
    private ClienteRest clienteRest;
    private ListView view;

    public Params (Fragment Fragment, SwipeRefreshLayout swipeRefreshLayout, ClienteRest clienteRest, ListView view) {
        this.Fragment = Fragment;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.clienteRest = clienteRest;
        this.view = view;
    }

    public Fragment getFragment() {
        return Fragment;
    }

    public void setFragment(Fragment Fragment) {
        this.Fragment = Fragment;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public ArrayAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ArrayAdapter adapter) {
        this.adapter = adapter;
    }

    public ClienteRest getClienteRest() {
        return clienteRest;
    }

    public void setClienteRest(ClienteRest clienteRest) {
        this.clienteRest = clienteRest;
    }

    public ListView getView() {
        return view;
    }

    public void setView(ListView view) {
        this.view = view;
    }
}