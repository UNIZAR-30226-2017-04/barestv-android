package unizar.margarethamilton.connection;

import android.database.Cursor;

import org.json.*;


import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import unizar.margarethamilton.dataBase.FavoritosDbAdapter;

import static unizar.margarethamilton.dataBase.FavoritosDbAdapter.KEY_BAR;
import static unizar.margarethamilton.dataBase.FavoritosDbAdapter.KEY_CAT;
import static unizar.margarethamilton.dataBase.FavoritosDbAdapter.KEY_DESCR;
import static unizar.margarethamilton.dataBase.FavoritosDbAdapter.KEY_FIN;
import static unizar.margarethamilton.dataBase.FavoritosDbAdapter.KEY_INICIO;
import static unizar.margarethamilton.dataBase.FavoritosDbAdapter.KEY_TITULO;


/**
 * Created by Xian on 2017/4/10.
 */

public class ClienteRest implements Serializable {

    private static final String URI = "http://192.168.1.165:8080/baresTvServicio/rest/server/";

    public List<HashMap<String, String>> getBares() {
        List<HashMap<String, String>> bares = new ArrayList<HashMap<String, String>>();

        {
            HashMap<String, String> bar = new HashMap<String, String>();
            bar.put("nickbar", "bardeprueba");
            bar.put("nombre", "Bar de Manolo");
            bar.put("descrbar", "Las peores tapas, más caras que nunca.");
            bar.put("activado", "1");
            bar.put("lat", "41.65111400");
            bar.put("lng", "-0.89438000");
            bar.put("direccion", "Calle La Mala Muerte");
            bar.put("urlimagen", "https://encrypted.google.com/images/branding/googlelogo/2x/googlelogo_color_120x44dp.png");
            bares.add(bar);
        }
        {
            HashMap<String, String> bar = new HashMap<String, String>();
            bar.put("nickbar", "barejemplo");
            bar.put("nombre", "Un bar de ejemplo");
            bar.put("descrbar", "El mejor bar de tapas de ejemplo, con la mejor programación de ejemplo.");
            bar.put("activado", "0");
            bar.put("lat", "41.65111400");
            bar.put("lng", "-0.89438000");
            bar.put("direccion", "Calle Manuel Escoriaza y Fabro, 51 - 50010 Zaragoza");
            bar.put("urlimagen", "https://media-cdn.tripadvisor.com/media/photo-s/09/1e/e2/45/la-fusteria-bar-de-tapas.jpg");
            bares.add(bar);
        }

        return bares;
    }

    /**
     * @param nickbar
     * @return diccionario con los detalles de un bar con nick nickbar; estará
     *         vacío si el nick no corresponde a ningún bar
     */
    public HashMap<String, String> getBarDadoNick(String nickbar) {
        HashMap<String, String> bar = new HashMap<String, String>();
        return bar;
    }

    /**
     * @return lista de todas las categorías existentes
     */
    public List<String> getCategorias() {
        List<String> cats = new ArrayList<String>();

        cats.add("toros");
        cats.add("fútbol");

        return cats;
    }

    /**
     * @return lista de diccionarios de todos los programas presentes o futuros
     *         y sus detalles ordenados de más próximos a más lejanos en el tiempo
     */
    public List<HashMap<String, String>> getProgramacion() {
        List<HashMap<String, String>> programas = new ArrayList<HashMap<String, String>>();

        {
            HashMap<String, String> programa = new HashMap<String, String>();
            programa.put("Titulo", "El Hormiguero");
            programa.put("Categoria", "Entretenimiento");
            programa.put("Bar", "Taberna Kalandria");
            programa.put("Descr", "Programa el Hormiguero, presentado por Pablo Motos");
            programa.put("Inicio", "04/03/2017 21:00");
            programa.put("Fin", "04/03/2017 23:00");
            programas.add(programa);
        }

        {
            HashMap<String, String> programa = new HashMap<String, String>();
            programa.put("Titulo", "La hora de Jose Mota");
            programa.put("Categoria", "Comedia");
            programa.put("Bar", "Dantis");
            programa.put("Descr", "Programa de humor presentado por Jose Mota");
            programa.put("Inicio", "04/03/2017 21:00");
            programa.put("Fin", "04/03/2017 24:00");
            programas.add(programa);
        }

        {
            HashMap<String, String> programa = new HashMap<String, String>();
            programa.put("Titulo", "Real Madrid-A.T.Madrid");
            programa.put("Categoria", "Deporte");
            programa.put("Bar", "Almendros");
            programa.put("Descr", "Jornada 12,Liga BBVA");
            programa.put("Inicio", "05/03/2017 20:00");
            programa.put("Fin", "05/03/2017 22:30");
            programas.add(programa);
        }

        {
            HashMap<String, String> programa = new HashMap<String, String>();
            programa.put("Titulo", "Barcelona-Sevilla");
            programa.put("Categoria", "Deporte");
            programa.put("Bar", "Bar Buenavista");
            programa.put("Descr", "Jornada 12,Liga BBVA");
            programa.put("Inicio", "06/03/2017 17:00");
            programa.put("Fin", "06/03/2017 19:30");
            programas.add(programa);
        }

        return programas;
    }

    /**
     * @return lista de diccionarios de todos los programas presentes o futuros
     *         que se expondrán en un bar y sus detalles ordenados de más
     *         próximo a más lejano en el tiempo
     */
    public List<HashMap<String, String>> getProgramacionDadoBar(String nickbar) {
        List<HashMap<String, String>> programas = new ArrayList<HashMap<String, String>>();

        {
            HashMap<String, String> programa = new HashMap<String, String>();
            programa.put("Titulo", "La hora de Jose Mota");
            programa.put("Categoria", "Comedia");
            programa.put("Bar", "Dantis");
            programa.put("Descr", "Programa de humor presentado por Jose Mota");
            programa.put("Inicio", "04/03/2017 21:00");
            programa.put("Fin", "04/03/2017 24:00");
            programas.add(programa);
        }

        return programas;
    }


    private interface ProgramacionDestacado {
        @GET("destacados")
        Call<ResponseBody> programacionDestacado();
    }

    /**
     * @return lista de diccionarios de todos los programas presentes o futuros
     *         marcados como destacados y sus detalles ordenados de más
     *         próximos a más lejanos en el tiempo
     */
    public List<HashMap<String, String>> getProgramacionDestacada() {
        List<HashMap<String, String>> programas = new ArrayList<HashMap<String, String>>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URI)
                .build();
        ProgramacionDestacado pd = retrofit.create(ProgramacionDestacado.class);
        Call<ResponseBody> pds = pd.programacionDestacado();
        try {
            String response = pds.execute().body().string();

            JSONArray array = new JSONArray(response);
            JSONObject obj = null;

            for (int i=0; i < array.length(); i++) {
                try {
                    HashMap<String, String> hmp = new HashMap<String, String>();
                    obj = array.getJSONObject(i);
                    hmp.put("Titulo", obj.getString("Titulo"));
                    hmp.put("Categoria", obj.getString("Cat"));
                    hmp.put("Bar", obj.getString("Bar"));
                    hmp.put("Descr", obj.getString("Descr"));
                    hmp.put("Inicio", obj.getString("Inicio"));
                    hmp.put("Fin", obj.getString("Fin"));
                    programas.add(hmp);

                } catch (JSONException e) {
                    e.printStackTrace();
                }}
        } catch (Exception e) {
            return null;
        }

        pds.cancel();

        return programas;
    }


    private interface Favoritos {
        @GET("actualizar")
        Call<ResponseBody> favoritos(@Query("lista") String lista);
    }

    /**
     * @return lista de diccionarios de todos los favoritos del usuario
     */
//    public List<HashMap<String, String>> getFavoritos(FavoritosDbAdapter mDb) {
//        List<HashMap<String, String>> programas = new ArrayList<HashMap<String, String>>();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(URI)
//                .build();
//       Favoritos f = retrofit.create(Favoritos.class);
//
//        List<Favorito> flist = new ArrayList<Favorito>();
//        // Extrar favoritos del BBDD local
//        Cursor cursor = mDb.ExtraerFavoritosAPI();
//        try {
//            while (cursor.moveToNext()) {
//                flist.add(new Favorito(cursor.getString(cursor.getColumnIndex(KEY_TITULO)),
//                        cursor.getString(cursor.getColumnIndex(KEY_BAR)),
//                        cursor.getString(cursor.getColumnIndex(KEY_DESCR)),
//                        cursor.getString(cursor.getColumnIndex(KEY_CAT)),
//                        cursor.getString(cursor.getColumnIndex(KEY_INICIO)),
//                        cursor.getString(cursor.getColumnIndex(KEY_FIN))
//                        ));
//            }
//        } finally {
//            cursor.close();
//        }
//
//        String encoded = null;
//        try {
//            encoded = URLEncoder.encode(flist.toString(), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        Call<ResponseBody> fs = f.favoritos(encoded);
//        try {
//            String response = fs.execute().body().string();
//
//            // Actualizar BBDD local;
//            mDb.EliminarTodosFavoritos();
//
//
//            JSONArray array = new JSONArray(response);
//            JSONObject obj = null;
//
//            for (int i=0; i < array.length(); i++) {
//
//                try {
//                    HashMap<String, String> hmp = new HashMap<String, String>();
//                    obj = array.getJSONObject(i);
//
//                    mDb.introducirFavoritos(obj.getString("Titulo"), obj.getString("Cat"),
//                            obj.getString("Bar"), obj.getString("Descr"), obj.getString("Inicio"),
//                            obj.getString("Fin"));
//
//                    hmp.put("Titulo", obj.getString("Titulo"));
//                    hmp.put("Categoria", obj.getString("Cat"));
//                    hmp.put("Bar", obj.getString("Bar"));
//                    hmp.put("Descr", obj.getString("Descr"));
//                    hmp.put("Inicio", obj.getString("Inicio"));
//                    hmp.put("Fin", obj.getString("Fin"));
//                    programas.add(hmp);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }}
//        } catch (Exception e) {
//            return null;
//        }
//
//        fs.cancel();
//
//        return programas;
//    }


    /**
     * @return lista de diccionarios de todos los usuarios registrados y sus
     *         detalles ordenados alfabéticamente (a-z) por nick
     */
    public List<HashMap<String, String>> getUsuarios() {
        List<HashMap<String, String>> usuarios = new ArrayList<HashMap<String, String>>();
        return usuarios;
    }

    /**
     * @param nick
     * @param clave nueva contraseña
     * @param permisos 0 = usuario limitado; 1 = administrador
     * @return true si todo ha ido bien, falso en caso contrario
     */
    public Boolean setUsuario(String nick, String clave, Boolean permisos) {
        return false;
    }

    /**
     * @param nickbar
     * @param nombre
     * @param descrbar
     * @param activado 0 = deshabilitado; 1 = habilitado
     * @param lat
     * @param lng
     * @param direccion
     * @param urlimagen
     * @return true si todo ha ido bien, falso en caso contrario
     */
    public Boolean setBar(String nickbar, String nombre,
                          String descrbar, Boolean activado,
                          String lat, String lng,
                          String direccion, String urlimagen) {
        return false;
    }

    /**
     *
     * @param titulo
     * @param bar
     * @param descr
     * @param destacado 0 = no; 1 = sí
     * @param inicio
     * @param fin
     * @param cat
     * @return true si todo ha ido bien, falso en caso contrario
     */
    public Boolean setPrograma(String titulo, String bar,
                               String descr, Boolean destacado,
                               String inicio, String fin,
                               String cat) {
        return false;
    }

    /**
     * Renombra una categoría.
     * @param viejaCat
     * @param nuevaCat
     * @return true si todo ha ido bien, falso en caso contrario
     */
    public Boolean renombrarCategoria(String viejaCat, String nuevaCat) {
        return false;
    }

    /**
     * Crea una categoría.
     * @param cat
     * @return true si todo ha ido bien, falso en caso contrario
     */
    public Boolean crearCategoria(String cat) {
        return false;
    }

    /**
     * Borra una categoría.
     * @param cat
     * @return true si todo ha ido bien, falso en caso contrario
     */
    public Boolean borrarCategoria(String cat) {
        return false;
    }

    /**
     * Borra un usuario. En caso de ser un bar, borra también
     * el establecimiento como tal con toda su información.
     * @param nick
     * @return true si todo ha ido bien, falso en caso contrario
     */
    public Boolean borrarUsuario(String nick) {
        return false;
    }

    /**
     * Borra un programa.
     * @param titulo
     * @param bar
     * @return true si todo ha ido bien, falso en caso contrario
     */
    public Boolean borrarPrograma(String titulo, String bar) {
        return false;
    }
}