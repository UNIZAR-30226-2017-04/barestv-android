package unizar.margarethamilton.connection;

import android.database.Cursor;

import org.json.*;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import unizar.margarethamilton.dataBase.FavoritosDbAdapter;

import static unizar.margarethamilton.dataBase.FavoritosDbAdapter.KEY_BAR;
import static unizar.margarethamilton.dataBase.FavoritosDbAdapter.KEY_TITULO;


public class ClienteRest implements Serializable {
    // dirección por defecto de la api
    //private static final String URI = "http://192.168.0.154:8080/baresTvServicio/rest/server/";
    private static final String URI = "http://192.168.1.165:8080/baresTvServicio/rest/server/";

    /**
     * `Peticion HTTP para funcion getBares()
     */
    private interface Bares {
        @GET("bar")
        Call<ResponseBody> bares(@Query("lat") double lat, @Query("lng") double lng,
                                 @Query("radio") int radio);
    }

    /**
     * Obtener lista de diccionarios los bares cercanos dado los parametros
     * @param lat latitud
     * @param lng longitud
     * @param radio radio de cercania
     * @return lista de diccionarios de todos los bares cercanos
     */
    public List<HashMap<String, String>> getBares(double lat, double lng, int radio) {
        List<HashMap<String, String>> bares = new ArrayList<HashMap<String, String>>();

        // Conexion HTTP
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URI)
                .build();
        Bares clase = retrofit.create(Bares.class);
        Call<ResponseBody> call = clase.bares(lat, lng, radio);
        try {
            // Obtener respuesta
            String response = call.execute().body().string();

            JSONArray array = new JSONArray(response);
            JSONObject obj;

            for (int i=0; i < array.length(); i++) {
                try {
                    HashMap<String, String> hmp = new HashMap<String, String>();
                    obj = array.getJSONObject(i);
                    hmp.put("Nombre", obj.getString("Nombre"));
                    hmp.put("Lat", obj.getString("Lat"));
                    hmp.put("Lng", obj.getString("Lng"));
                    bares.add(hmp);

                } catch (JSONException e) {
                    e.printStackTrace();
                }}
        } catch (Exception e) {
            return null;
        }

        // Cerrar conexion
        call.cancel();

        return bares;
    }

    /**
     * `Peticion HTTP para funcion getFavoritos()
     */
    private interface Categorias {
        @GET("categorias")
        Call<ResponseBody> categorias();
    }

    /**
     * Obtener lista de diccionarios de todas las categorías existentes
     * @return lista de diccionarios de todas las categorías existentes
     */
    public List<HashMap<String, String>> getCategorias() {
        List<HashMap<String, String>> categorias = new ArrayList<HashMap<String, String>>();

        // Conexion HTTP
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URI)
                .build();
        Categorias clase = retrofit.create(Categorias.class);
        Call<ResponseBody> call = clase.categorias();
        try {
            // Obtener respuesta
            String response = call.execute().body().string();

            JSONArray array = new JSONArray(response);
            JSONObject obj;

            for (int i=0; i < array.length(); i++) {
                try {
                    HashMap<String, String> hmp = new HashMap<String, String>();
                    obj = array.getJSONObject(i);
                    hmp.put("Categoria", obj.getString("Nombre"));
                    categorias.add(hmp);

                } catch (JSONException e) {
                    e.printStackTrace();
                }}
        } catch (Exception e) {
            return null;
        }

        // Cerrar conexion
        call.cancel();

        return categorias;
    }

    /**
     * `Peticion HTTP para funcion getProgramacionProxima()
     */
    private interface ProgramacionProxima {
        @GET("programacion")
        Call<ResponseBody> programacionProxima();
    }

    /**
     * Obtener lista de diccionarios de todos los programas presentes o futuros
     *         y sus detalles ordenados de más próximos a más lejanos en el tiempo
     * @return lista de diccionarios de todos los programas presentes o futuros
     *         y sus detalles ordenados de más próximos a más lejanos en el tiempo
     */
    public List<HashMap<String, String>> getProgramacionProxima() {
        List<HashMap<String, String>> programas = new ArrayList<HashMap<String, String>>();

        // Conexion HTTP
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URI)
                .build();
        ProgramacionProxima clase = retrofit.create(ProgramacionProxima.class);
        Call<ResponseBody> call = clase.programacionProxima();
        try {
            // Obtener respuesta
            String response = call.execute().body().string();

            JSONArray array = new JSONArray(response);
            JSONObject obj;

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

        // Cerrar conexion
        call.cancel();

        return programas;
    }


    /**
     * `Peticion HTTP para funcion getProgramacionDadoBar()
     */
    private interface ProgramacionDadoBar {
        @GET("programasBar")
        Call<ResponseBody> programacionDadoBar(@Query("bar") String bar);
    }

    /**
     * @return lista de diccionarios de todos los programas presentes o futuros
     *         que se expondrán en un bar y sus detalles ordenados de más
     *         próximo a más lejano en el tiempo
     */
    public List<HashMap<String, String>> getProgramacionDadoBar(String nickbar) {
        List<HashMap<String, String>> programas = new ArrayList<HashMap<String, String>>();

        // Conexion HTTP
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URI)
                .build();
        ProgramacionDadoBar clase = retrofit.create(ProgramacionDadoBar.class);

        // Tratamiento de parametros a pasar
        if (nickbar == null) nickbar = "";
        Call<ResponseBody> call = clase.programacionDadoBar(nickbar);
        try {

            // Obtener respuesta
            String response = call.execute().body().string();
            JSONArray array = new JSONArray(response);
            JSONObject obj;

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

        // Cerrar conexion
        call.cancel();

        return programas;
    }

    /**
     * `Peticion HTTP para funcion getProgramacionDestacado()
     */
    private interface ProgramacionDestacado {
        @GET("destacados")
        Call<ResponseBody> programacionDestacado();
    }

    /**
     * Obtener lista de diccionarios de todos los programas presentes o futuros
     *         marcados como destacados y sus detalles ordenados de más
     *         próximos a más lejanos en el tiempo
     * @return lista de diccionarios de todos los programas presentes o futuros
     *         marcados como destacados y sus detalles ordenados de más
     *         próximos a más lejanos en el tiempo
     */
    public List<HashMap<String, String>> getProgramacionDestacada() {
        List<HashMap<String, String>> programas = new ArrayList<HashMap<String, String>>();

        // Conexion HTTP
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URI)
                .build();
        ProgramacionDestacado clase = retrofit.create(ProgramacionDestacado.class);
        Call<ResponseBody> call = clase.programacionDestacado();
        try {

            // Obtener respuesta
            String response = call.execute().body().string();
            JSONArray array = new JSONArray(response);
            JSONObject obj;

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

        // Cerrar conexion
        call.cancel();

        return programas;
    }

    /**
     * `Peticion HTTP para funcion buscar()
     */
    private interface Buscar {
        @GET("busqueda")
        Call<ResponseBody> buscar(@Query("find") String find, @Query("cat") String cat,
                                  @Query("day") int day, @Query("month") int month,
                                    @Query("year") int year);
    }

    /**
     * Obtener lista de diccionarios de todos los programas filtrados o no por los datos proporcionados
     * @param buscado bar o programa a buscar
     * @param cat categoria
     * @param dia diag
     * @param mes mes
     * @param anyo año
     * @return lista de diccionarios de todos los programas filtrados o no por los datos proporcionados
     */
    public List<HashMap<String, String>> buscar(String buscado, String cat, int dia, int mes, int anyo) {
        List<HashMap<String, String>> programas = new ArrayList<HashMap<String, String>>();

        // Conexion HTTP
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URI)
                .build();
        Buscar clase = retrofit.create(Buscar.class);

        // Tratamiento de parametros a pasar
        if (cat == null) cat = "";
        if (buscado == null) buscado = "";
        Call<ResponseBody> call = clase.buscar(buscado, cat, dia, mes, anyo);
        try {

            // Obtener respuesta
            String response = call.execute().body().string();
            JSONArray array = new JSONArray(response);
            JSONObject obj;

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

        // Cerrar conexion
        call.cancel();

        return programas;
    }


    /**
     * `Peticion HTTP para funcion getFavoritos()
     */
    private interface Favoritos {
        @GET("actualizar")
        Call<ResponseBody> favoritos(@Query("lista") String lista);
    }

    /**
     * Obtener lista de diccionarios de todos los favoritos del usuario
     * @param mDb BBDD local de favoritos
     * @return lista de diccionarios de todos los favoritos del usuario
     */
     public List<HashMap<String, String>> getFavoritos(FavoritosDbAdapter mDb) {
         List<HashMap<String, String>> favoritos = new ArrayList<HashMap<String, String>>();

         // Conexion HTTP
         Retrofit retrofit = new Retrofit.Builder()
                  .baseUrl(URI)
                  .build();


         // Preparar parametros a pasar
         JSONArray arrayIni = new JSONArray();

         // Extrar favoritos del BBDD local
         Cursor cursor = mDb.ExtraerFavoritosAPI();
         try {
             while (cursor.moveToNext()) {
                 try {
                     JSONObject obj = new JSONObject();
                     obj.put("Titulo", cursor.getString(cursor.getColumnIndex(KEY_TITULO)));
                     obj.put("Bar", cursor.getString(cursor.getColumnIndex(KEY_BAR)));
                     arrayIni.put(obj);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
         } finally {
             cursor.close();
         }

         Favoritos clase = retrofit.create(Favoritos.class);
         Call<ResponseBody> call = clase.favoritos(arrayIni.toString());
         try {
             // Obtener respuesta
             String response = call.execute().body().string();

             // Actualizar BBDD local;
             mDb.EliminarTodosFavoritos();

             JSONArray array = new JSONArray(response);
             JSONObject obj;

             for (int i=0; i < array.length(); i++) {

                 try {
                     HashMap<String, String> hmp = new HashMap<String, String>();
                     obj = array.getJSONObject(i);

                     mDb.introducirFavoritos(obj.getString("Titulo"), obj.getString("Bar"),
                              obj.getString("Descr"), obj.getString("Inicio"),
                              obj.getString("Fin"),obj.getString("Cat"));

                     hmp.put("Titulo", obj.getString("Titulo"));
                     hmp.put("Categoria", obj.getString("Cat"));
                     hmp.put("Bar", obj.getString("Bar"));
                     hmp.put("Descr", obj.getString("Descr"));
                     hmp.put("Inicio", obj.getString("Inicio"));
                     hmp.put("Fin", obj.getString("Fin"));
                     favoritos.add(hmp);

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
         } catch (Exception e) {
             e.printStackTrace();
              return null;
         }

         // Cerrar conexion
         call.cancel();

         return favoritos;
     }

}