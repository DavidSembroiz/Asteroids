package com.example.asteroides;

import java.util.Vector;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MiAdaptador extends BaseAdapter {

	private final Activity actividad;
	private final Vector<String> lista;
	
	public MiAdaptador(Activity a, Vector<String> l) {
		super();
		this.actividad = a;
		this.lista = l;
	}
	
	public View getView(int pos, View convertView, ViewGroup parent) {
		LayoutInflater inflater = this.actividad.getLayoutInflater();
		View view = inflater.inflate(R.layout.elemento_lista, null, true);
		TextView textView = (TextView) view.findViewById(R.id.titulo);
		textView.setText(lista.elementAt(pos));
		ImageView imageView = (ImageView) view.findViewById(R.id.icono);
		imageView.setImageResource(R.drawable.asteroide2);
		return view;
	}
	
	public int getCount() {
		return lista.size();
	}
	
	public Object getItem(int a) {
		return lista.elementAt(a);
	}


	public long getItemId(int pos) {
		return pos;
	}
}
