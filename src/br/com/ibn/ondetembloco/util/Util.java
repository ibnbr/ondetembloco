package br.com.ibn.ondetembloco.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.string;
import android.content.Context;
import android.net.ConnectivityManager;

public class Util {

	public static final String URL_TUDO = "http://ondetembloco.com.br/json/action_full";
	public static final String URL_UPDATE = "http://ondetembloco.com.br/json/action_verifica-update";
	public static final String URL_DESTAQUE = "http://ondetembloco.com.br/json/";
	public static final String URL_LISTA_BAIRROS = "http://ondetembloco.com.br/json/action_lista-bairro";
	public static final String URL_LISTA_BLOCOS = "http://ondetembloco.com.br/json/action_lista-bloco";
	public static final String URL_LISTA_DATAS = "http://ondetembloco.com.br/json/action_lista-data";
	public static final String URL_BAIRROS = "http://ondetembloco.com.br/json/bairro_";
	public static final String URL_BLOCOS = "http://ondetembloco.com.br/json/bloco_";
	public static final String URL_DATAS = "http://ondetembloco.com.br/json/data_";	
	

	public static boolean isOnline(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return (cm.getActiveNetworkInfo() != null ? cm.getActiveNetworkInfo()
				.isConnectedOrConnecting() : false);
	}
	
	public static String dateToString(Date dt){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(dt);
	}
	
	public static Date stringToDate(String str) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.parse(str);
	}
	
	public static Date stringToDateEng(String str) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.parse(str);
	}
}
