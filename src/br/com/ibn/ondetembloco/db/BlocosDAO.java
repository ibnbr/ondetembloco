package br.com.ibn.ondetembloco.db;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import br.com.ibn.ondetembloco.bo.BlocoBO;
import br.com.ibn.ondetembloco.util.Util;

public class BlocosDAO {
	public static final String TABLE_BLOCOS = "BLOCOS";
	public static final String COL_ID = "id";
	public static final String COL_NOME = "nome";
	public static final String COL_RUA = "rua";
	public static final String COL_BAIRRO = "bairro";
	public static final String COL_LAT = "lat";
	public static final String COL_LON = "lon";
	public static final String COL_DATA = "data";
	public static final String COL_HORA = "hora";
	public static final String COL_DESTAQUE = "destaque";
//	public static final String COL_FAVORITO = "favorito";
	public static final String DATABASE_CR_BLOCOS = "CREATE TABLE "
			+ TABLE_BLOCOS + "( " + COL_ID + " INTEGER PRIMARY KEY, "
			+ COL_NOME + " TEXT, " + COL_RUA + " TEXT, " + COL_BAIRRO
			+ " TEXT, " + COL_LAT + " INTEGER , " + COL_LON + " INTEGER, "
			+ COL_DATA + " INTEGER, " + COL_HORA + " TIME, " + COL_DESTAQUE
			+ " BOOLEAN);";

	private static BlocosDAO singleton;

	public synchronized static BlocosDAO getInstance(Context ctx) {
		if (singleton == null) {
			singleton = new BlocosDAO(ctx);
		}
		return singleton;
	}

	private SQLiteDatabase ondeDB;
	private Context ctx;

	private BlocosDAO(Context ctx) {
		this.ctx = ctx;
	}

	public synchronized void open() {
		if (ondeDB == null || !ondeDB.isOpen()) {
			ondeDB = OndeDBAdapter.getDB(ctx);
		}
	}

	public synchronized void close() {
		if (ondeDB == null || !ondeDB.isOpen()) {
			ondeDB.close();
		}
	}

	public void insert(BlocoBO bloco) {
		ondeDB.insert(TABLE_BLOCOS, null, BOtoCV(bloco));
	}

	public long totalRegistros() {
		open();
		long num = DatabaseUtils.queryNumEntries(ondeDB, TABLE_BLOCOS);
		close();
		return num;
	}

	public ArrayList<BlocoBO> consulta(String colSel, String[] valSel) {
		open();
		Cursor c = ondeDB.query(TABLE_BLOCOS, null, colSel+"=?", valSel, null, null,
				null);
		ArrayList<BlocoBO> retorno = new ArrayList<BlocoBO>();
		if (c.moveToFirst()) {
			do {
				retorno.add(cursorToBO(c));
			} while (c.moveToNext());

		}
		c.close();
		close();
		return retorno;
	}

	public ArrayList<String> distinctColuna(String coluna) {
		open();
		Cursor c = ondeDB.query(true, TABLE_BLOCOS, new String[] { coluna },
				null, null, null, null, coluna, null);
		ArrayList<String> retorno = new ArrayList<String>();
		if (c.moveToFirst()) {
			do {
				if (coluna.equals(COL_DATA)) {
					retorno.add(Util.dateToString(new Date(c.getLong(0))));
				} else {
					retorno.add(c.getString(0));
				}
			} while (c.moveToNext());

		}
		c.close();
		close();

		return retorno;
	}

	private ContentValues BOtoCV(BlocoBO bloco) {
		ContentValues cv = new ContentValues();
		cv.put(COL_ID, bloco.getId());
		cv.put(COL_NOME, bloco.getNome());
		cv.put(COL_RUA, bloco.getRua());
		cv.put(COL_BAIRRO, bloco.getBairro());
		cv.put(COL_LAT, bloco.getLat());
		cv.put(COL_LON, bloco.getLon());
		cv.put(COL_DATA, bloco.getData().getTime());
		cv.put(COL_HORA, bloco.getHora());
		cv.put(COL_DESTAQUE, bloco.getDestaque());
//		cv.put(COL_FAVORITO, bloco.getFavorito());
		return cv;
	}

	public BlocoBO cursorToBO(Cursor c) {
		BlocoBO bloco = new BlocoBO();
		bloco.setId(c.getInt(c.getColumnIndex(COL_ID)));
		bloco.setNome(c.getString(c.getColumnIndex(COL_NOME)));
		bloco.setRua(c.getString(c.getColumnIndex(COL_RUA)));
		bloco.setBairro(c.getString(c.getColumnIndex(COL_BAIRRO)));
		bloco.setLat(c.getInt(c.getColumnIndex(COL_LAT)));
		bloco.setLon(c.getInt(c.getColumnIndex(COL_LON)));
		bloco.setData(new Date(c.getLong(c.getColumnIndex(COL_DATA))));
		bloco.setHora(c.getString(c.getColumnIndex(COL_HORA)));
		bloco.setDestaque(c.getString(c.getColumnIndex(COL_DESTAQUE)));
//		bloco.setFavorito(c.getString(c.getColumnIndex(COL_FAVORITO)));

		return bloco;

	}

	public boolean limpaTabela() {
		open();
		int blocos = ondeDB.delete(TABLE_BLOCOS, null, null);
		close();
		return (blocos > 0) ? true : false;
	}

}
