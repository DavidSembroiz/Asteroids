package com.example.asteroides;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class Puntuaciones extends ListActivity {
	
	@Override public void onCreate(Bundle savedInstanceState) {
		/*super.onCreate(savedInstanceState);
		setContentView(R.layout.puntuaciones);
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Localization.almacen.listaPuntuaciones(10)));*/
		super.onCreate(savedInstanceState);
		setContentView(R.layout.puntuaciones);
		//setListAdapter(new ArrayAdapter<String>(this, R.layout.elemento_lista, R.id.titulo, Localization.almacen.listaPuntuaciones(10)));
		setListAdapter(new MiAdaptador(this, Asteroides.almacen.listaPuntuaciones(10)));
	}
	
	@Override
	protected void onListItemClick(ListView listView, View view, int pos, long id) {
		super.onListItemClick(listView, view, pos, id);
		Object o = getListAdapter().getItem(pos);
		Toast.makeText(this, "Selección: " + Integer.toString(pos) + " - " + o.toString(), Toast.LENGTH_LONG).show();
	}
}
