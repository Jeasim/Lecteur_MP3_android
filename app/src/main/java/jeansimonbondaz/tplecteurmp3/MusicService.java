package jeansimonbondaz.tplecteurmp3;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

/**
 * Created by eric on 2017-03-01.
 */
public class MusicService extends Service  {

    private MediaPlayer diffuseur; //diffuseur
    private final IBinder musicBind = new MusicBinder(); //IBinder nécessaire pour le service, la classe MusicBinder est définie comme clase interne ci-bas


    int depl; //peut être utile pour chercher un moment dans la chanson avec la seekBar


    public void onCreate(){
        super.onCreate();

        diffuseur = new MediaPlayer(); // crée le MediaPlayer

        initMusicPlayer();
    }

    public void initMusicPlayer()
    {
        diffuseur.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        diffuseur.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }


    // méthode abstraite de la classe Service qu'on doit redéfinir
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }


    public boolean onUnbind( Intent intent)
    {
        // si on détache le service de notre app, on doit arrêter le MediaPlayer
        diffuseur.stop();
        diffuseur.release();
        return false;
    }

    public MediaPlayer getDiffuseur() {
        return diffuseur; // retourne le MediaPlayer
    }


    // à appeler pour qu'une chanson soit choisie, milli peut être utile quand on avance la chanson avec la SeeekBar
    public void choisirChanson(int milli, long idChanson){
        diffuseur.reset();
        depl = milli; // utile pour seekBar
        // passer id de la chanson courante provenant du Vecteur de votre singleton
        Uri trackUri = ContentUris.withAppendedId( MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, idChanson);

        try {
            diffuseur.setDataSource(getApplicationContext(), trackUri);
        } catch (IOException e) {
            Log.e("MUSIC SERVICE", "problème", e);
        }
        diffuseur.prepareAsync();


    }



    public int getDuree()
    {
        return diffuseur.getDuration();
    }

    public boolean diffuseActuellement()
    {
        return diffuseur.isPlaying();
    }
    public void pause()
    {
        diffuseur.pause();

    }
    //méthod epeut être utile avec la SeekBar
    public void cherche(int position)
    {
        diffuseur.seekTo(position);
    }

    // classe interne servant à avoir un Binder
    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }


}
