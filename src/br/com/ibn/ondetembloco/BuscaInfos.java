package br.com.ibn.ondetembloco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.text.Html;
import br.com.ibn.ondetembloco.bo.BlocoBO;
import br.com.ibn.ondetembloco.util.Util;

public class BuscaInfos {

	private static BuscaInfos singleton;

	public synchronized static BuscaInfos getInstance() {
		if (singleton == null) {
			singleton = new BuscaInfos();
		}
		return singleton;
	}

	private BuscaInfos() {

	}

	public Date getUpdate(String url) {
		Date retorno = null;
		try {
			URL link = new URL(url);

			HttpURLConnection con = (HttpURLConnection) link.openConnection();
			con.setConnectTimeout(1000 * 10); // 10 Segundos
			con.setReadTimeout(1000 * 10);
			BufferedReader r = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			StringBuilder total = new StringBuilder();
			String line, jsonStr;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
			r.close();
			jsonStr = total.toString().replace("\"", "").trim();

		
			retorno = Util.stringToDateEng(Html.fromHtml(jsonStr).toString());

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retorno;

	}

	public ArrayList<String> getLista(String url) {
		ArrayList<String> retorno = new ArrayList<String>();

		try {
			URL link = new URL(url);

			HttpURLConnection con = (HttpURLConnection) link.openConnection();
			con.setConnectTimeout(1000 * 10); // 10 Segundos
			con.setReadTimeout(1000 * 10);
			BufferedReader r = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			StringBuilder total = new StringBuilder();
			String line, jsonStr;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
			r.close();
			jsonStr = total.toString();

			JSONObject obj = (JSONObject) new JSONTokener(jsonStr).nextValue();

			Iterator<String> iter = obj.keys();

			while (iter.hasNext()) {
				retorno.add(Html.fromHtml(obj.getString(iter.next()))
						.toString());
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Collections.sort(retorno);
		return retorno;
	}

	public ArrayList<BlocoBO> getPoints(String url) {
		ArrayList<BlocoBO> retorno = new ArrayList<BlocoBO>();

		try {
			URL link = new URL(url);

			HttpURLConnection con = (HttpURLConnection) link.openConnection();
			con.setConnectTimeout(1000 * 10); // 10 Segundos
			con.setReadTimeout(1000 * 10);
			BufferedReader r = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			StringBuilder total = new StringBuilder();
			String line, jsonStr;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
			r.close();
			jsonStr = total.toString();

			JSONArray array = (JSONArray) new JSONTokener(jsonStr).nextValue();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				BlocoBO bl = new BlocoBO();

				// obj = obj.getJSONObject("171");

				bl.setId(obj.getInt("id_bloco"));
				bl.setNome(Html.fromHtml(obj.getString("nome")).toString());
				bl.setRua(Html.fromHtml(obj.getString("rua")).toString());
				bl.setBairro(Html.fromHtml(obj.getString("bairro")).toString());
				bl.setLat((int) (obj.getDouble("lat") * 1e6));
				bl.setLon((int) (obj.getDouble("lon") * 1e6));
				String data = Html.fromHtml(obj.getString("data")).toString();
				bl.setData(Util.stringToDate(data));
				bl.setHora(Html.fromHtml(obj.getString("hora")).toString());
				bl.setDestaque(Html.fromHtml(obj.getString("destaque"))
						.toString());

				retorno.add(bl);

			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retorno;
	}
}
