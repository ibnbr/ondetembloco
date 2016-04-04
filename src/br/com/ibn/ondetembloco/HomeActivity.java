package br.com.ibn.ondetembloco;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;
import br.com.ibn.ondetembloco.bo.BlocoBO;
import br.com.ibn.ondetembloco.db.BlocosDAO;
import br.com.ibn.ondetembloco.util.Util;

public class HomeActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_dashboard);

//		if (BlocosDAO.getInstance(HomeActivity.this).totalRegistros() == 0) {
			if (!Util.isOnline(getApplicationContext())) {
				Toast.makeText(getApplicationContext(),
						"Habilite a Conexão de Dados", Toast.LENGTH_LONG)
						.show();
			} else {
				new UpdateTask().execute();
			}
//		}

		findViewById(R.id.home_btn_mapa).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(HomeActivity.this,
								MapActivity.class);
						i.putExtra("url", Util.URL_DESTAQUE);
						startActivity(i);

					}
				});

		findViewById(R.id.home_btn_bairro).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(HomeActivity.this,
								ListaActivity.class);
						i.putExtra("tipo", 1);
						startActivity(i);
					}
				});

		findViewById(R.id.home_btn_datas).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(HomeActivity.this,
								ListaActivity.class);
						i.putExtra("tipo", 2);
						startActivity(i);
					}
				});

		findViewById(R.id.home_btn_blocos).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(HomeActivity.this,
								ListaActivity.class);
						i.putExtra("tipo", 3);
						startActivity(i);
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuRefresh:
			if (Util.isOnline(HomeActivity.this)) {
				new Task().execute();
			} else {
				Toast.makeText(getApplicationContext(),
						"Habilite a Conexão de Dados", Toast.LENGTH_LONG)
						.show();
			}

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 1) {
			ProgressDialog progressDialog = new ProgressDialog(
					HomeActivity.this);
			progressDialog.setMessage("Buscando Informações...");
			progressDialog.setCancelable(false);
			return progressDialog;
		} else if (id == 2) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					HomeActivity.this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("Atualização");
			builder.setMessage("Existem novas informações de Blocos. Deseja Atualizar?");
			builder.setPositiveButton("Sim",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.cancel();
							new Task().execute();
						}
					});
			builder.setNegativeButton("Não", null);
			return builder.create();
		} else {
			return super.onCreateDialog(id);
		}
	}

	private class Task extends AsyncTask<Context, Integer, Integer> {
		ArrayList<BlocoBO> blocos = new ArrayList<BlocoBO>();

		@Override
		protected Integer doInBackground(Context... arg0) {
			BuscaInfos info = BuscaInfos.getInstance();
			blocos = info.getPoints(Util.URL_TUDO);
			if (blocos.size() > 0) {
				BlocosDAO dao = BlocosDAO.getInstance(HomeActivity.this);
				dao.limpaTabela();
				for (BlocoBO bloco : blocos) {
					dao.insert(bloco);
				}
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			removeDialog(1);
			if (result == 0)
				Toast.makeText(getApplicationContext(), "Dados Atualizados...",
						Toast.LENGTH_LONG).show();
			else
				Toast.makeText(getApplicationContext(),
						"Falha ao Atualizar.Tente Novamente!",
						Toast.LENGTH_LONG).show();
			
			SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit();
			edit.putLong("lastUpdate", new Date().getTime());
			edit.commit();

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(1);
		}

	}

	private class UpdateTask extends AsyncTask<Context, Integer, Integer> {
		private Date dt;

		@Override
		protected Integer doInBackground(Context... arg0) {
			BuscaInfos info = BuscaInfos.getInstance();
			dt = info.getUpdate(Util.URL_UPDATE);
			return null;
		}

		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (dt != null) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(HomeActivity.this);
				Long time = prefs.getLong("lastUpdate", 0);

				if (dt.getTime() > time) {
					showDialog(2);
				}

			}
		}

	}
}
