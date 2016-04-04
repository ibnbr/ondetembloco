package br.com.ibn.ondetembloco.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Classe para trabalho com o Banco de dados(SQLite)
 * 
 * @author italo
 * 
 */
public class OndeDBAdapter {
	private static final String DATABASE_NAME = "OndeTemDB";
	private static final int DATABASE_VERSION = 1;

//	private SQLiteDatabase ondeDB;
//	private Context ctx;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context ctx) {
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(BlocosDAO.DATABASE_CR_BLOCOS);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}

	public static SQLiteDatabase getDB(Context ctx){
		return new DatabaseHelper(ctx).getWritableDatabase();
	}
	
}