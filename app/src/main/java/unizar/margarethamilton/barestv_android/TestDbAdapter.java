package unizar.margarethamilton.barestv_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.DatabaseUtils;

/**
 * Simple programs database access helper class. Defines the basic CRUD operations
 * for the baresTV app, and gives the ability to list all programs
 *
 */
public class TestDbAdapter {

    /** Columnas de la tabla programa */
    public static final String KEY_TITULO = "titulo";
    public static final String KEY_BAR = "bar";
    public static final String KEY_DESCR = "descr";
    public static final String KEY_DESTACADO = "destacado";
    public static final String KEY_INICIO = "inicio";
    public static final String KEY_FIN = "fin";
    public static final String KEY_CAT = "cat";

    /** Tags para depuración */
    private static final String TAG = "TestDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table programa (" +
                    "titulo text not null, " +
                    "bar text not null, " +
                    "descr text not null, " +
                    "destacado integer not null," +
                    "inicio text not null, " +
                    "fin text not null, " +
                    "cat text, " +
                    "PRIMARY KEY (titulo, bar));";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "programa";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("TAG", DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
            db.execSQL("INSERT INTO programa (titulo, bar, descr, destacado, inicio, fin, cat) " +
                    "VALUES ('titulo1', 'bar1', 'descr1','1', 'inicio1', 'fin1', 'cat1')");
            db.execSQL("INSERT INTO programa (titulo, bar, descr, destacado, inicio, fin, cat) " +
                    "VALUES ('titulo2', 'bar2', 'descr2','1', 'inicio2', 'fin2', 'cat2')");
            db.execSQL("INSERT INTO programa (titulo, bar, descr, destacado, inicio, fin, cat) " +
                    "VALUES ('titulo3', 'bar3', 'descr3','1', 'inicio3', 'fin3', 'cat3')");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS programa");
            onCreate(db);
            ;
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public TestDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public TestDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new program using the information provided. If the program is
     * successfully created return the new rowId for that program, otherwise return
     * a -1 to indicate failure.
     *
     * @return rowId or -1 if failed
     */
    public long createProgram(String titulo, String bar, String descr, String inicio, String fin ) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_TITULO, titulo);
        initialValues.put(KEY_BAR, bar);
        initialValues.put(KEY_DESCR, descr);
        initialValues.put(KEY_DESTACADO, 1);
        initialValues.put(KEY_INICIO, inicio);
        initialValues.put(KEY_FIN, fin);
        initialValues.put(KEY_CAT, "deporte");

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }


    /**
     * Return a Cursor over the list of all programs in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllPrograms() {
        return mDb.rawQuery( "select rowid _id,* from programa", null);
    }

}