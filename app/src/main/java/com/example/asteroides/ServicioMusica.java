package com.example.asteroides;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class ServicioMusica extends Service {
    MediaPlayer reproductor;
    private NotificationManager nm;  
    private static final int ID_NOTIFICACION_CREAR = 1;

    @Override
    public void onCreate() {
          reproductor = MediaPlayer.create(this, R.raw.audio);
          nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    @SuppressWarnings("deprecation")
	@Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
		  Notification notificacion = new Notification(R.drawable.ic_launcher, "Creando Servicio de Música", System.currentTimeMillis());
    	  PendingIntent intencionPendiente = PendingIntent.getActivity(
    	          this, 0, new Intent(this, Asteroides.class), 0);
    	  notificacion.setLatestEventInfo(this, "Reproduciendo música", "información adicional", intencionPendiente);
    	  nm.notify(ID_NOTIFICACION_CREAR, notificacion);
          reproductor.start();
          return START_STICKY;
    }
    
    /*@Override
    public void onStart(Intent intent, int startId) {
        reproductor.start();
    }*/

    @Override
    public void onDestroy() {
          nm.cancel(ID_NOTIFICACION_CREAR);
          reproductor.stop();
    }

    @Override
    public IBinder onBind(Intent intencion) {
          return null;
    }
}
