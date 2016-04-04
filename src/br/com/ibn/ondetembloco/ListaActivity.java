package br.com.ibn.ondetembloco;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.com.ibn.ondetembloco.db.BlocosDAO;
import br.com.ibn.ondetembloco.util.Util;

public class ListaActivity extends FragmentActivity {
	private ArrayList<String> itens;
	private ListView lstView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_listas);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		lstView = (ListView) findViewById(R.id.lstView);

		lstView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View item,
					int position, long arg3) {
				Intent i = new Intent(getApplicationContext(),
						MapActivity.class);
				i.putExtra("texto", itens.get(position));
				i.putExtra("tipo", getIntent().getIntExtra("tipo", 1));
				startActivity(i);
			}
		});

		Toast.makeText(getApplicationContext(),
				"Selecione o item para ver no Mapa", Toast.LENGTH_SHORT).show();

		// new Task().execute();

		BlocosDAO dao = BlocosDAO.getInstance(ListaActivity.this);
		int tipo = getIntent().getIntExtra("tipo", 0);
		switch (tipo) {
		case 1:
			itens = dao.distinctColuna(BlocosDAO.COL_BAIRRO);
			break;

		case 2:
			itens = dao.distinctColuna(BlocosDAO.COL_DATA);
			break;

		case 3:
			itens = dao.distinctColuna(BlocosDAO.COL_NOME);
			break;

		default:
			itens = dao.distinctColuna(BlocosDAO.COL_BAIRRO);
			break;
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.list_row, R.id.txtNome, itens);
		lstView.setAdapter(adapter);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent i = new Intent(ListaActivity.this, HomeActivity.class);
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
			ProgressDialog progressDialog = new ProgressDialog(
					ListaActivity.this);
			progressDialog.setMessage("Buscando Informações...");
			progressDialog.setCancelable(false);
			return progressDialog;
		} else {
			return super.onCreateDialog(id);
		}
	}

	private class Task extends AsyncTask<Context, Integer, Integer> {
		private String url;

		@Override
		protected Integer doInBackground(Context... arg0) {
			BuscaInfos info = BuscaInfos.getInstance();
			itens = info.getLista(url);
			removeDialog(1);
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			if (getIntent().getIntExtra("tipo", 0) == 2) {
				Collections.sort(itens, new Comparator<String>() {

					@Override
					public int compare(String lhs, String rhs) {
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						try {
							long d1 = df.parse(lhs).getTime();
							long d2 = df.parse(rhs).getTime();
							if (d1 < d2)
								return -1;
							else if (d1 > d2)
								return 1;

						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return 0;

					}
				});
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getApplicationContext(), R.layout.list_row, R.id.txtNome,
					itens);
			lstView.setAdapter(adapter);

		}

		@Override
		protected void onPreExecute() {
			int tipo = getIntent().getIntExtra("tipo", 0);
			switch (tipo) {
			case 1:
				url = Util.URL_LISTA_BAIRROS;
				break;

			case 2:
				url = Util.URL_LISTA_DATAS;
				break;

			case 3:
				url = Util.URL_LISTA_BLOCOS;
				break;

			default:
				url = Util.URL_LISTA_BAIRROS;
				break;
			}
			showDialog(1);
			super.onPreExecute();
		}

	}

}
