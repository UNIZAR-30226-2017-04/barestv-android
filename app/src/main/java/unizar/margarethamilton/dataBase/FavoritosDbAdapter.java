package unizar.margarethamilton.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 *
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class FavoritosDbAdapter {

    /**
     * Columnas de la tabla favoritos
     */
    public static final String KEY_BAR = "Bar";
    public static final String KEY_TITULO = "Titulo";
    public static final String KEY_DESCR = "Descr";
    public static final String KEY_INICIO = "Inicio";
    public static final String KEY_FIN = "Fin";
    public static final String KEY_CAT = "Categoria";

    /**
     * Tags para depuración
     */
    private static final String TAG = "FavoritosDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table favoritos ("+KEY_TITULO+ " text not null, "
                    + KEY_BAR + " text not null, "+KEY_DESCR+" text not null, " +
                    KEY_INICIO + " text not null," + KEY_FIN +" text not null, "+ KEY_CAT +
                    " text not null,PRIMARY KEY (titulo, bar));";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "favoritos";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);

            // Al mismo tiempo, creamos fichero para conectar a la BBDD remota
            try {
                File file = new File(context.getFilesDir() + "/IP.txt");
                if (!file.exists()) {
                    FileOutputStream os = context.openFileOutput("IP.txt", MODE_PRIVATE);
                    OutputStreamWriter writer = new OutputStreamWriter(os);
                    writer.write("192.168.0.154:8080");
                    writer.close();
                    os.close();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            android.util.Log.d("TAG", DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS favoritos");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public FavoritosDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     * initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public FavoritosDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    /**
     * Crea un nuevo favorito con los datos proporcionado. Devuelve rowId en caso de exito
     * y -1 en caso de fallo
     * @param titulo el titulo de la programacion favorita
     * @param bar el bar de la programacion favorita
     * @param descr
     * @param inicio
     * @param fin
     * @param cat
     * @return rowID en caso de exito o -1 en caso de fallo
     */
    public long introducirFavoritos(String titulo, String bar, String descr, String inicio,
                                    String fin, String cat) {
        ContentValues initialValues = new ContentValues();

        /*
        if (DatabaseUtils.queryNumEntries(mDb, DATABASE_TABLE) >= 1000) {
            return -1;
        }*/
        initialValues.put(KEY_TITULO, titulo);
        initialValues.put(KEY_BAR, bar);
        initialValues.put(KEY_DESCR, descr);
        initialValues.put(KEY_INICIO, inicio);
        initialValues.put(KEY_FIN, fin);
        initialValues.put(KEY_CAT, cat);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Eliminar un nuevo favorito con el titulo y el bar proporcionado. Devuelve true en caso de
     * eliminar con exito
     * @param titulo el titulo de la programacion favorita
     * @param bar el bar de la programacion favorita
     * @return true en caso de eliminar con exito
     */
    public boolean EliminarFavoritos(String titulo, String bar) {
        String[] args = {titulo, bar};
        return mDb.delete(DATABASE_TABLE, KEY_TITULO + "=?" + " AND " +
                KEY_BAR + "=?", args) > 0;
    }

    /**
     * Eliminar todos los favorito. Devuelve true en caso de
     * eliminar con exito
     * @return true en caso de eliminar con exito
     */
    public boolean EliminarTodosFavoritos() {
        return mDb.delete(DATABASE_TABLE, null, null) > 0;
    }

    /**
     * Devuelve un cursor sobre todos los favoritos para el uso de la API
     *
     * @return Cursor sobre todos los favoritos
     */
    public Cursor ExtraerFavoritosAPI() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_TITULO,
                KEY_BAR}, null, null, null, null, KEY_BAR);
    }

    /**
     * Devuelve un cursor sobre todos los favoritos
     *
     * @return Cursor sobre todos los favoritos
     */
    public Cursor ExtraerFavoritos() {

        return mDb.query(DATABASE_TABLE, new String[] {"rowid _id", KEY_TITULO,
                KEY_BAR, KEY_DESCR, KEY_INICIO, KEY_FIN, KEY_CAT}, null, null, null, null, KEY_BAR);
    }

    /**
     * Actualiza un favorito con los datos proporcionado. Devuelve true en caso de exito
     * y false en caso de fallo
     * @param tituloViejo el titulo de la programacion favorita
     * @param barViejo el bar de la programacion favorita
     * @param titulo
     * @param bar
     * @param descr
     * @param inicio
     * @param fin
     * @param cat
     * @return true en caso de exito o false en caso de fallo
     */
    public boolean actualizarFavoritos(String tituloViejo, String barViejo, String titulo,
                                String bar, String descr, String inicio, String fin, String cat) {
        ContentValues updateValues = new ContentValues();

        /*
        if (DatabaseUtils.queryNumEntries(mDb, DATABASE_TABLE) >= 1000) {
            return -1;
        }*/
        updateValues.put(KEY_TITULO, titulo);
        updateValues.put(KEY_BAR, bar);
        updateValues.put(KEY_DESCR, descr);
        updateValues.put(KEY_INICIO, inicio);
        updateValues.put(KEY_FIN, fin);
        updateValues.put(KEY_CAT, cat);

        String[] args = {tituloViejo, barViejo};
        return mDb.update(DATABASE_TABLE, updateValues, KEY_TITULO + "=?" + " AND " +
                KEY_BAR + "=?", args) > 0;
    }

    /**
     * Comprueba si existe el favorito con los datos proporcionados. Devuelve true en caso de que existe
     * y falso en caso contrario
     * @param titulo
     * @param bar
     * @return true en caso de que existe y falso en caso contrario
     */
    public Boolean comprobarFavorito(String titulo, String bar) {
        String[] args = {titulo, bar};
        Cursor query = mDb.query(DATABASE_TABLE, new String[] {KEY_TITULO}, KEY_TITULO + "=?" +
                " AND " + KEY_BAR + "=?", args, null, null, null, null);
        boolean esFav = query.getCount()>0;
        query.close();
        return esFav;
    }
}

