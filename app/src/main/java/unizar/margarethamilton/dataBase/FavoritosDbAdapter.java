package unizar.margarethamilton.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.DatabaseUtils;

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
    public static final String KEY_BAR = "bar";
    public static final String KEY_TITULO = "titulo";
    public static final String KEY_DESCR = "descr";
    public static final String KEY_INICIO = "inicio";
    public static final String KEY_FIN = "fin";
    public static final String KEY_CAT = "cat";

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
            "create table notes (titulo text not null, "
                    + "bar text not null, descr text not null, inicio text not null," +
                    "fin text not null, cat text not null,PRIMARY KEY (titulo, bar));";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "favoritos";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        return mDb.delete(DATABASE_TABLE, KEY_TITULO + "=" + titulo + "AND" +
                KEY_BAR + "=" + bar, null) > 0;
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

        return mDb.query(DATABASE_TABLE, new String[] {KEY_TITULO,
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

        return mDb.update(DATABASE_TABLE, updateValues, KEY_TITULO + "=" + tituloViejo + "AND" +
                KEY_BAR + "=" + barViejo, null) > 0;
    }
}

