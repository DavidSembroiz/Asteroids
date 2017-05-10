package com.example.asteroides;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;

public class Juego extends Activity {
	
	private VistaJuego vistaJuego;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.juego);
		vistaJuego = (VistaJuego) findViewById(R.id.VistaJuego);
		vistaJuego.setPadre(this);
	}
	
	@Override 
	protected void onPause() {
		   super.onPause();
		   vistaJuego.getThread().pausar();
		   vistaJuego.mSensorManager.unregisterListener(vistaJuego);
	}
		 
	@Override 
	protected void onResume() {
		   super.onResume();
		   vistaJuego.getThread().reanudar();
	}
		 
	@Override 
	protected void onDestroy() {
		   vistaJuego.getThread().detener();
		   super.onDestroy();
	}
}
