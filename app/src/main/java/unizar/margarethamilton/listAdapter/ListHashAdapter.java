package unizar.margarethamilton.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Xian on 2017/4/9.
 */

/**
 * Clase Adapter que mapea los datos de entrada a los campos de un ListView
 */
public class ListHashAdapter extends ArrayAdapter {


    private static View[] v;        // Array de los campos del ListView

    private static int layout;     // Layout de campos del ListView
    private static String[] from;  // Atributos de HashMap que se desea mapear al LisView
    private static int[] to;       // IDs de los campos del ListView

    /**
     * Constructor
     * @param context El contexto donde se ejecuta
     * @param _layout Layout de campos del ListView
     * @param objects Lista de datos en forma de HashMap a mapear
     * @param _from   Atributos de HashMap que se desea mapear
     * @param _to      IDs de los campos del ListView
     */
    public ListHashAdapter(Context context, int _layout, List<HashMap<String, String>> objects,
                           String[] _from, int[] _to) {
        super(context, _layout, objects);
        layout = _layout;
        from = _from;
        to = _to;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
            v = new View[from.length];

            // Obtiene los campos del ListView atraves de sus IDs
            for (int i=0; i < to.length; i++) {
                v[i] = convertView.findViewById(to[i]);
            }
            convertView.setTag(v);
        } else
            v = (View[]) convertView.getTag();

        HashMap<String, String> entry = (HashMap<String, String>) this.getItem(position);

        // Mapea los datos a los campos del ListView
        for (int i=0; i < from.length; i++) {
            if (v[i] instanceof TextView) {
                setViewText((TextView) v[i], entry.get(from[i]));
            }

        }

        return convertView;
    }

    /**
     * Inserta el texto en el TextView dado
     * @param v TextView que sera insertado
     * @param text Texto que se inserta
     */
    public void setViewText(TextView v, String text) {
        v.setText(text);
    }
}