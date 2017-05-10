package com.example.asteroides;

import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class VistaJuego extends View implements SensorEventListener {
	
	
	class ThreadJuego extends Thread {
		
		private boolean pausa,corriendo;
		 
		public synchronized void pausar() {
		      pausa = true;
		}
		 
		public synchronized void reanudar() {
		      pausa = false;
		      notify();
		}
		 
		public void detener() {
				corriendo = false;
		    	if (pausa) reanudar();
		}
		  
		@Override    
		public void run() {
			corriendo = true;
			while (corriendo) {
				actualizaFisica();
			    synchronized (this) {
			    	while (pausa) {
			    		try {
			    			wait();
			            } catch (Exception e) {
			        }
			    }
			}
		}
	}
		/*
		@Override
		public void run() {
			while(true) {
				actualizaFisica();
				try {
					Thread.sleep(PERIODO_PROCESO);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}*/
	}
	
	
	private Vector<Grafico> Asteroides;
	
	private Grafico nave;
	private int giroNave;
	private float aceleracionNave;
	private static final int PASO_GIRO_NAVE = 5;
	private static final float PASO_ACELERACION_NAVE = 0.5f;
	private boolean disparo = false;
	private float mX = 0, mY = 0;
	
	private Grafico misil;
	private static int PASO_VELOCIDAD_MISIL = 12;
	private boolean misilActivo = false;
	private int tiempoMisil;
	
	
	
	private ThreadJuego thread = new ThreadJuego();
	private static int PERIODO_PROCESO = 50;
	private long ultimoProceso = 0;
	
	private int numAsteroides = 5;
	private int numFragmentos = 3;
	private int puntuacion = 0;
	private Activity padre;
	
	private boolean hayValorInicial = false;
	private float valorInicial;
	
	public SensorManager mSensorManager;
	
	public VistaJuego(Context context, AttributeSet attrs) {
		super(context, attrs);
		Drawable drawableNave, drawableAsteroide, drawableMisil;
		
		/*ShapeDrawable dMisil = new ShapeDrawable(new RectShape());
		dMisil.getPaint().setColor(Color.WHITE);
		dMisil.getPaint().setStyle(Style.STROKE);
		dMisil.setIntrinsicWidth(15);
		dMisil.setIntrinsicHeight(3);
		drawableMisil = dMisil;*/
		
		drawableMisil = context.getResources().getDrawable(R.drawable.misil1);
		
		drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
		drawableNave = context.getResources().getDrawable(R.drawable.nave);
		Asteroides = new Vector<Grafico>();
		nave = new Grafico(this, drawableNave);
		misil = new Grafico(this, drawableMisil);
		for (int i = 0; i < numAsteroides; ++i) {
			Grafico asteroide = new Grafico(this, drawableAsteroide);
			asteroide.setIncX(Math.random()*4 - 2);
			asteroide.setIncY(Math.random()*4 - 2);
			asteroide.setAngulo((int) (Math.random() * 360));
			asteroide.setRotacion((int) (Math.random() * 8 - 4));
			this.Asteroides.add(asteroide);
		}
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		@SuppressWarnings("deprecation")
		List<Sensor> listSensors = mSensorManager.getSensorList( 
		              Sensor.TYPE_ORIENTATION);
		if (!listSensors.isEmpty()) {
		   Sensor orientationSensor = listSensors.get(0);
		   mSensorManager.registerListener(this, orientationSensor,
		                              SensorManager.SENSOR_DELAY_GAME);
		}
	}
	
	@Override
	protected void onSizeChanged(int ancho, int alto, int ancho_anterior, int alto_anterior) {
		super.onSizeChanged(ancho, alto, ancho_anterior, alto_anterior);
		for (Grafico asteroide : Asteroides) {
			do {
				asteroide.setPosX(Math.random()*(ancho - asteroide.getAncho()));
				asteroide.setPosY(Math.random()*(alto - asteroide.getAlto()));
			} while (asteroide.distancia(nave) < (ancho + alto)/5);
		}
		nave.setPosX((ancho - nave.getAncho())/2);
		nave.setPosY((alto - nave.getAlto())/2);
		ultimoProceso = System.currentTimeMillis();
		Log.d(VIEW_LOG_TAG, "Ancho "+ancho + " Alto " + alto);
		for (Grafico a : Asteroides) {
			Log.d(VIEW_LOG_TAG, "X "+a.getPosX()+" Y "+a.getPosY());
		}
		thread.start();
	}
	
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (Grafico asteroide : Asteroides) {
			asteroide.dibujaGrafico(canvas);
		}
		nave.dibujaGrafico(canvas);
		if (misilActivo) misil.dibujaGrafico(canvas);
	}
	
	protected synchronized void actualizaFisica() {
		long ahora = System.currentTimeMillis();
		if (ultimoProceso + PERIODO_PROCESO > ahora) {
			return;
		}
		double retardo = (ahora - ultimoProceso)/PERIODO_PROCESO;
		ultimoProceso = ahora;
		nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
		double nIncX = nave.getIncX() + aceleracionNave * 
										Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
		double nIncY = nave.getIncY() + aceleracionNave * 
										Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
		if (Math.hypot(nIncX, nIncY) <= Grafico.getMaxVelocidad()) {
			nave.setIncX(nIncX);
			nave.setIncY(nIncY);
		}
		nave.incrementaPos(retardo);
		for (Grafico asteroide : Asteroides) {
			asteroide.incrementaPos(retardo);
		}
		if (misilActivo) {
		       misil.incrementaPos(retardo);
		       tiempoMisil-=retardo;
		       if (tiempoMisil < 0) {
		             misilActivo = false;
		       } else {
		for (int i = 0; i < Asteroides.size(); i++)
		             if (misil.verificaColision(Asteroides.elementAt(i))) {
		                    destruyeAsteroide(i);
		                    break;
		             }
		       }
		}
		for (Grafico asteroide : Asteroides) {
			if (asteroide.verificaColision(nave)) {
			       salir();
			}
		}
	}
	
	private void destruyeAsteroide(int i) {
	       Asteroides.remove(i);
	       puntuacion += 1000;
	       misilActivo = false;
	       if (Asteroides.isEmpty()) {
               salir();
	       }
	}
	 
	private void activaMisil() {
	       misil.setPosX(nave.getPosX()+ nave.getAncho()/2-misil.getAncho()/2);
	       misil.setPosY(nave.getPosY()+ nave.getAlto()/2-misil.getAlto()/2);
	       misil.setAngulo(nave.getAngulo());
	       misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo())) *
	                        PASO_VELOCIDAD_MISIL);
	       misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo())) *
	                        PASO_VELOCIDAD_MISIL);
	       tiempoMisil = (int) Math.min(this.getWidth() / Math.abs( misil.
	          getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 2;
	       misilActivo = true;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		float x = event.getX();
		float y = event.getY();
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			disparo = true;
			break;
		case MotionEvent.ACTION_MOVE:
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dy < 6 && dx > 6) {
				giroNave = Math.round((x - mX) / 2);
				disparo = false;
			}
			else if (dx < 6 && dy > 6) {
				aceleracionNave = Math.round((mY - y) / 25);
				disparo = false;
			}
			break;
		case MotionEvent.ACTION_UP:
			giroNave = 0;
			aceleracionNave = 0;
			if (disparo) {
				activaMisil();
			}
			break;
		}
		mX = x;
		mY = y;
		
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	

	@Override
	public void onSensorChanged(SensorEvent event) {
		float valor = event.values[1];
		if (!hayValorInicial) {
			valorInicial = valor;
			hayValorInicial = true;
		}
		giroNave = (int) (valor - valorInicial)/3;
	}

	public ThreadJuego getThread() {
		return thread;
	}
	
	public void setPadre(Activity padre) {
		this.padre = padre;
	}
	
	private void salir() {
		Bundle bundle = new Bundle();
		bundle.putInt("puntuacion", puntuacion);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		padre.setResult(Activity.RESULT_OK, intent);
		padre.finish();
	}
}
