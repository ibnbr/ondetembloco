package br.com.ibn.ondetembloco;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentMapActivity;
import android.support.v4.view.MenuItem;
import android.widget.Toast;
import br.com.ibn.ondetembloco.bo.BlocoBO;
import br.com.ibn.ondetembloco.db.BlocosDAO;
import br.com.ibn.ondetembloco.util.Util;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapActivity extends FragmentMapActivity {
	private List<Overlay> mapOverlays;
	private BlocoOverlay blocos;
	private MapView map;
	private ArrayList<BlocoBO> arrayBl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_map);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		map = (MapView) findViewById(R.id.mapview);
		map.setBuiltInZoomControls(true);
		MapController mapControl = map.getController();
		mapControl.setCenter(new GeoPoint(-22870000, -43210000));
//		mapControl.setZoom(12);

		Drawable draw = getResources().getDrawable(R.drawable.map_pin2);
		blocos = new BlocoOverlay(draw, MapActivity.this, map);

		mapOverlays = map.getOverlays();

		int tipo = getIntent().getIntExtra("tipo", 0);
		String busca = getIntent().getStringExtra("texto");
		BlocosDAO dao = BlocosDAO.getInstance(MapActivity.this);
		switch (tipo) {
		case 1:
			arrayBl = dao
					.consulta(BlocosDAO.COL_BAIRRO, new String[] { busca });
			break;
		case 2:
			Date dt = null;
			try {
				dt = Util.stringToDate(busca);
				arrayBl = dao.consulta(BlocosDAO.COL_DATA,
						new String[] { String.valueOf(dt.getTime()) });
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case 3:
			arrayBl = dao.consulta(BlocosDAO.COL_NOME, new String[] { busca });
			break;
		default:
			Toast.makeText(getApplicationContext(),
					"Mostrando apenas blocos em destaque", Toast.LENGTH_LONG)
					.show();
			arrayBl = dao
					.consulta(BlocosDAO.COL_DESTAQUE, new String[] { "1" });
			break;
		}

		// new Task().execute(MapActivity.this);

		int maxLat = Integer.MIN_VALUE, minLat = Integer.MAX_VALUE, maxLon = Integer.MIN_VALUE, minLon = Integer.MAX_VALUE;

		for (BlocoBO bl : arrayBl) {
			GeoPoint point = new GeoPoint(bl.getLat(), bl.getLon());

			String nome = bl.getNome();
			String descr = bl.getRua() + " - " + bl.getBairro() + " às "
					+ bl.getHora() + " de " + Util.dateToString(bl.getData());

			OverlayItem i = new OverlayItem(point, nome, descr);
			blocos.addOverlay(i);

			int lat = bl.getLat();
			int lon = bl.getLon();

			maxLat = Math.max(lat, maxLat);
			minLat = Math.min(lat, minLat);
			maxLon = Math.max(lon, maxLon);
			minLon = Math.min(lon, minLon);

		}

		mapOverlays.add(blocos);
		map.invalidate();

		mapControl.zoomToSpan(Math.abs(maxLat - minLat),
				Math.abs(maxLon - minLon));
		mapControl.animateTo(new GeoPoint((maxLat + minLat) / 2,
				(maxLon + minLon) / 2));

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.main_menu, menu);
	// return true;
	// }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent i = new Intent(MapActivity.this, HomeActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 1) {
			ProgressDialog progressDialog = new ProgressDialog(MapActivity.this);
			progressDialog.setMessage("Buscando Informações...");
			progressDialog.setCancelable(false);
			return progressDialog;
		} else {
			return super.onCreateDialog(id);
		}
	}

	// private class Task extends AsyncTask<Context, Integer, Integer> {
	// private ArrayList<BlocoBO> arrayBl;
	//
	// @Override
	// protected Integer doInBackground(Context... arg0) {
	// BuscaInfos info = BuscaInfos.getInstance();
	// arrayBl = info.getPoints(url);
	// return 0;
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// showDialog(1);
	// super.onPreExecute();
	// }
	//
	// @Override
	// protected void onPostExecute(Integer result) {
	// super.onPostExecute(result);
	// if (arrayBl.size() > 0) {
	//
	// for (BlocoBO bl : arrayBl) {
	// GeoPoint point = new GeoPoint(bl.getLat(), bl.getLon());
	//
	// String nome = bl.getNome();
	// String descr = bl.getRua() + " - " + bl.getBairro()
	// + " às " + bl.getHora() + " de " + bl.getData();
	//
	// OverlayItem i = new OverlayItem(point, nome, descr);
	// blocos.addOverlay(i);
	// }
	//
	// mapOverlays.add(blocos);
	// map.invalidate();
	// } else {
	// Toast.makeText(MapActivity.this,
	// "Não foi possivel acessar o Site", Toast.LENGTH_LONG)
	// .show();
	// }
	// removeDialog(1);
	//
	// }
	// }
}