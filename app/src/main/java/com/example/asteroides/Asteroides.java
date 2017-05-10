package com.example.asteroides;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Asteroides extends Activity {
	
	private Button bAcercaDe;
	private Button exit;
	private Button preferences;
	private Button rank;
	private Button juego;
	public static AlmacenPuntuaciones almacen;
	private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        setContentView(R.layout.main);
        
        startService(new Intent(Asteroides.this,
                ServicioMusica.class));
        
        /*mp = MediaPlayer.create(this, R.raw.audio);
        mp.start();*/
        
        //almacen = new AlmacenPuntuacionesArray();
        //almacen = new AlmacenPuntuacionesPreferencias(this);
        //almacen = new AlmacenPuntuacionesFicheroInterno(this);
        //almacen = new AlmacenPuntuacionesFicheroExterno(this);
        //almacen = new AlmacenPuntuacionesRecurso(this);
        //almacen = new AlmacenPuntuacionesXML_SAX(this);
        almacen = new AlmacenPuntuacionesSocket();
        bAcercaDe = (Button) findViewById(R.id.button3);
        bAcercaDe.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		initAbout(null);
        	}
        });
        
        exit = (Button) findViewById(R.id.button4);
        exit.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		finish();
        	}
        });
        
        preferences = (Button) findViewById(R.id.button2);
        preferences.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		initPreferences(null);
        	}
        });
        
        rank = (Button) findViewById(R.id.button5);
        rank.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		initRanking(null);
        	}
        });
        
        juego = (Button) findViewById(R.id.button1);
        juego.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		initJuego(null);
        	}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = this.getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.acercaDe:
    		this.initAbout(null);
    		break;
    	case R.id.config:
    		this.initPreferences(null);
    		break;
    	}
    	return true;
    }
    
    public void initAbout(View view) {
		Intent i = new Intent(this, AcercaDe.class);
		startActivity(i);
	}
    
    public void initPreferences(View view) {
    	Intent i = new Intent(this, Preferencias.class);
    	startActivity(i);
    }
    
    public void initRanking(View view) {
    	Intent i = new Intent(this, Puntuaciones.class);
    	startActivity(i);
    }
    
    public void initJuego(View view) {
    	Intent i = new Intent(this, Juego.class);
    	startActivityForResult(i, 1234);
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode==1234 & resultCode==RESULT_OK & data!=null) {
    		int puntuacion = data.getExtras().getInt("puntuacion");
    		String nombre = "Yo";
    		almacen.guardarPuntuacion(puntuacion, nombre, System.currentTimeMillis());
    		initRanking(null);
    	}
    }
    
    @Override 
    protected void onStart() {
    	   super.onStart();
    	   //Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }
    	 
    @Override 
    protected void onResume() {
    	   super.onResume();
    	   //mp.start();
    	   //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }
    	 
    @Override
    protected void onPause() {
    	   //mp.pause();
    	   //Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
    	   super.onPause();
    }
    	 
    @Override 
    protected void onStop() {
    	   //Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
    	   super.onStop();
    }
    	 
    @Override 
    protected void onRestart() {
    	   super.onRestart();
    	   //Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }
    	 
    @Override 
    protected void onDestroy() {
    	   //Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    	  stopService(new Intent(Asteroides.this,
                ServicioMusica.class));
    	   super.onDestroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle estadoGuardado){
           super.onSaveInstanceState(estadoGuardado);
           if (mp != null) {
                  int pos = mp.getCurrentPosition();
                  estadoGuardado.putInt("posicion", pos);
           }
    }
  
    @Override
    protected void onRestoreInstanceState(Bundle estadoGuardado){
           super.onRestoreInstanceState(estadoGuardado);
           if (estadoGuardado != null && mp != null) {
                  int pos = estadoGuardado.getInt("posicion");
                  mp.seekTo(pos);
           }
    }
}
