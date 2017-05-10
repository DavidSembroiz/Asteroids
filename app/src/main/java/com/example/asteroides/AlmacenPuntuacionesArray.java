package com.example.asteroides;

import java.util.Vector;

public class AlmacenPuntuacionesArray implements AlmacenPuntuaciones {

	private Vector<String> puntuaciones;
	
	public AlmacenPuntuacionesArray() {
		puntuaciones = new Vector<String>();
		puntuaciones.add("9999 David");
		puntuaciones.add("7777 Pedro");
		puntuaciones.add("5555 Juan");
	}
	
	@Override
	public void guardarPuntuacion(int puntos, String nombre, long fecha) {
		puntuaciones.add(0, puntos + " " + nombre);
	}

	@Override
	public Vector<String> listaPuntuaciones(int cantidad) {
		return puntuaciones;
	}
}
