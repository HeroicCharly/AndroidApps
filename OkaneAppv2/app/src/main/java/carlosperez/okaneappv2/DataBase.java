package carlosperez.okaneappv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos Perez on 14/05/2015.
 */
public class DataBase extends SQLiteOpenHelper {

    //LogCat tag
    private static final String LOG = "DataBase";

    //Si se cambia la BD deben cambiarse las variables
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "OkaneApp.db";

    //Common columns
    public static final String KEY_ID = "id";

    //Definicion tbl_gastos
    public static final String TABLE_GASTOS = "tbl_gastos";
    public static final String COLUMN_CONCEPTOGASTO = "conceptoGasto";
    public static final String COLUMN_COSTOGASTO = "costoGasto";


    //Definicion tbl_balance
    public static final String TABLE_BALANCE = "tbl_balance";
    public static final String COLUMN_TOTALGASTADO = "totalGastado";
    public static final String COLUMN_SALDOINICIAL = "saldoInicial";
    public static final String COLUMN_SALDOACTUAL = "saldoActual";

    //Sentencias de creacion SQL
    private static final String DATABASE_CREATE_TBLGASTOS = "create table "
            + TABLE_GASTOS
            + "("
            + KEY_ID + " integer primary key autoincrement, "
            + COLUMN_CONCEPTOGASTO + " text not null, "
            + COLUMN_COSTOGASTO + " text not null"
            + ");";

    private static final String DATABASE_CREATE_TBLBALANCE = "create table "
        + TABLE_BALANCE
        + "("
        + KEY_ID + " integer primary key autoincrement, "
        + COLUMN_TOTALGASTADO + " text not null, "
        + COLUMN_SALDOINICIAL + " text not null, "
        + COLUMN_SALDOACTUAL + " text not null"
        + ");";


    public DataBase(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_TBLGASTOS);
        db.execSQL(DATABASE_CREATE_TBLBALANCE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataBase.class.getName(), "Unos duendes estan actualizando tu base de datos de la" +
                "version "
                + oldVersion + " a la " + newVersion
                + ", esto le dara en su madre a la actual");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BALANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GASTOS);
        onCreate(db);
    }

    //Crear dbGasto
    public long createDBGasto(dbGasto nuevoGasto){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID, nuevoGasto.getId());
        values.put(COLUMN_CONCEPTOGASTO, nuevoGasto.getConcepto());
        values.put(COLUMN_COSTOGASTO, nuevoGasto.getPrecio());

        //insertar registro
        long gasto_id = db.insert(TABLE_GASTOS,null,values);

        Log.e(LOG, "Gasto creado..." + nuevoGasto.getConcepto() + nuevoGasto.getPrecio());

        return gasto_id;
    }

    //Crear dbBalance
    public long createDBBalance(dbBalance nuevoBalance){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, nuevoBalance.getId());
        values.put(COLUMN_TOTALGASTADO, nuevoBalance.getTotalGastado());
        values.put(COLUMN_SALDOINICIAL, nuevoBalance.getSaldoInicial());
        values.put(COLUMN_SALDOACTUAL, nuevoBalance.getSaldoActual());

        //insertar registro
        long balance_id = db.insert(TABLE_BALANCE,null,values);

        return balance_id;
    }

    //Fetch gasto - Obtener un "gasto" de la tabla gastos
    //TODO mejorar sentencia SQL
    public dbGasto getGasto(long gasto_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_GASTOS + " WHERE "
                + KEY_ID + " = " + gasto_id;

        Log.e(LOG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        dbGasto registroGasto = new dbGasto();
        registroGasto.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        registroGasto.setConcepto((cursor.getString(cursor.getColumnIndex(COLUMN_CONCEPTOGASTO))));
        registroGasto.setPrecio(cursor.getString(cursor.getColumnIndex(COLUMN_COSTOGASTO)));

        return registroGasto;
    }

    //TODO mejorar sentencia SQL
    public dbBalance getBalance(long balance_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_BALANCE + " WHERE "
                + KEY_ID + " = " + balance_id;

        Log.e(LOG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        dbBalance registroBalance = new dbBalance();
        registroBalance.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        registroBalance.setTotalGastado((cursor.getString(cursor.getColumnIndex(COLUMN_TOTALGASTADO))));
        registroBalance.setSaldoInicial(cursor.getString(cursor.getColumnIndex(COLUMN_SALDOINICIAL)));
        registroBalance.setSaldoActual(cursor.getString(cursor.getColumnIndex(COLUMN_SALDOACTUAL)));

        return registroBalance;
    }

    //Obtener todos los gastos
    public List<dbGasto> getAllGastos(){
        List<dbGasto> listaGastos = new ArrayList<dbGasto>();
        String selectQuery = "SELECT * FROM " + TABLE_GASTOS;

        Log.e(LOG,selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        Log.e(LOG, "Cantidad de elemntos:" + Integer.toString(cursor.getCount()));

        if(cursor.moveToFirst()){
            do{
                dbGasto tmpGasto = new dbGasto();
                tmpGasto.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                tmpGasto.setConcepto(cursor.getString(cursor.getColumnIndex(COLUMN_CONCEPTOGASTO)));
                tmpGasto.setPrecio(cursor.getString(cursor.getColumnIndex(COLUMN_COSTOGASTO)));

                listaGastos.add(tmpGasto);
            }while (cursor.moveToNext());
        }
        return listaGastos;
    }

    public int getTotalGastado(){
        List<dbGasto> dummyList = getAllGastos();
        int totalGastado = 0;

        for(dbGasto dummyGasto : dummyList){
            totalGastado  += Integer.parseInt(dummyGasto.getPrecio());
        }

        return totalGastado;
    }

    //CERRAR BD
    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null && db.isOpen()){
            db.close();
        }
    }

}
