package jeansimonbondaz.tplecteurmp3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LectureActivity extends AppCompatActivity {

    Ecouteur ec;

    Intent intentActuel;

    TextView textViewChanson;
    TextView textViewArtiste;
    TextView textViewAlbum;
    ImageView imageViewPochette;
    TextView textViewTempsDebut;
    TextView textViewTempsFin;

    ImageButton imageButtonPrecedent;
    ImageButton imageButtonSuivant;
    ImageButton imageButtonPause;
    ImageButton imageButtonAleatoire;
    ImageButton imageButtonRepete;

    Chanson enCours;
    boolean lectureEnCours;
    boolean lectureAleatoire;
    boolean lectureRepete;

    int indexChanson;
    String artiste;
    String album;
    String titre;
    String pochette;

    MusicService musicSrv;
    MediaPlayer mediaPlayer;
    boolean musicBound;
    Intent playIntent;
    MusiqueConnection musiqueConnection;
    GestionDiffuseur gd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);


        intentActuel = getIntent();

        musicSrv = new MusicService();


        textViewChanson = findViewById(R.id.textViewChanson);
        textViewArtiste = findViewById(R.id.textViewArtiste);
        textViewAlbum = findViewById(R.id.textViewAlbum);
        imageViewPochette = findViewById(R.id.imageViewPochette);
        textViewTempsDebut = findViewById(R.id.textViewTempsDebut);
        textViewTempsFin = findViewById(R.id.textViewTempsFin);

        imageButtonPrecedent = findViewById(R.id.imageButtonPrecedent);
        imageButtonSuivant = findViewById(R.id.imageButtonSuivant);
        imageButtonPause = findViewById(R.id.imageButtonPause);
        imageButtonAleatoire = findViewById(R.id.imageButtonAleatoire);
        imageButtonRepete = findViewById(R.id.imageButtonRepete);


        indexChanson = intentActuel.getIntExtra("index", 0);
        afficherChansonEnCours(indexChanson);

        ec = new Ecouteur();

        imageButtonPrecedent.setOnClickListener(ec);
        imageButtonSuivant.setOnClickListener(ec);
        imageButtonPause.setOnClickListener(ec);
        imageButtonAleatoire.setOnClickListener(ec);
        imageButtonRepete.setOnClickListener(ec);

        lectureEnCours = false;

        //lecture aleatoire: si selctionne, les prochaines chansons seront choisi au hasard dans le vecteur
        lectureAleatoire = false;

        //lecture repeat: si selectionne, la chanson en cours recommencera a la fin de sa lecture
        lectureRepete = false;

     ;
    }

    protected void onStart() {
        super.onStart();
        playIntent = new Intent(this, MusicService.class);
        musiqueConnection = new MusiqueConnection();
        bindService(playIntent, musiqueConnection, Context.BIND_AUTO_CREATE);
    }

    private class Ecouteur implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if (view == imageButtonPrecedent) {
                if (lectureAleatoire) {
                    indexChanson = EnsembleChansons.getInstance().chansonAleatoire();
                }

                else {
                    indexChanson = EnsembleChansons.getInstance().chansonPrecedente(indexChanson);
                    imageButtonPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                    lectureEnCours = true;
                }

                afficherChansonEnCours(indexChanson);
                musicSrv.choisirChanson(enCours.getDuree(), enCours.getId());



            } else if (view == imageButtonSuivant) {

                if (lectureAleatoire) {
                    indexChanson = EnsembleChansons.getInstance().chansonAleatoire();
                }
                else {
                    indexChanson = EnsembleChansons.getInstance().chansonSuivante(indexChanson);
                    imageButtonPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                    lectureEnCours = true;
                }

                afficherChansonEnCours(indexChanson);
                musicSrv.choisirChanson(enCours.getDuree(), enCours.getId());



            } else if (view == imageButtonAleatoire) {

                if (!lectureAleatoire) {

                    imageButtonAleatoire.setImageDrawable(getResources().getDrawable(R.drawable.shuffleselec));
                    lectureAleatoire = true;
                    lectureEnCours = true;
                }

                else {
                    imageButtonAleatoire.setImageDrawable(getResources().getDrawable(R.drawable.shuffle));
                    lectureAleatoire = false;
                }

            } else if (view == imageButtonRepete) {

                if (!lectureRepete) {
                    imageButtonRepete.setImageDrawable(getResources().getDrawable(R.drawable.repeatselec));
                    lectureRepete = true;
                }
                else {
                    imageButtonRepete.setImageDrawable(getResources().getDrawable(R.drawable.repeat));
                    lectureRepete = false;
                }
            }
            else if (view == imageButtonPause) {

                if (!lectureEnCours) {
                    imageButtonPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                    musicSrv.choisirChanson(enCours.getDuree(), enCours.getId());
                    lectureEnCours = true;
                }
                else {
                    imageButtonPause.setImageDrawable(getResources().getDrawable(R.drawable.play));
                    mediaPlayer.pause();
                    lectureEnCours = false;
                }
            }


        }
    }

    private class GestionDiffuseur implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

            if (lectureAleatoire) {
                indexChanson = EnsembleChansons.getInstance().chansonAleatoire();
                afficherChansonEnCours(indexChanson);
                musicSrv.choisirChanson(enCours.getDuree(), enCours.getId());
            } else if (lectureRepete) {
                afficherChansonEnCours(indexChanson);
                musicSrv.choisirChanson(enCours.getDuree(), enCours.getId());
            } else {
                indexChanson = EnsembleChansons.getInstance().chansonSuivante(indexChanson);
                afficherChansonEnCours(indexChanson);
                musicSrv.choisirChanson(enCours.getDuree(), enCours.getId());
            }
        }

        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            return false;
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {

            mediaPlayer.start();
            enCours.getDuree();

        }

        @Override
        public void onSeekComplete(MediaPlayer mediaPlayer) {


        }
    }


    private void afficherChansonEnCours(int index) {
        enCours = EnsembleChansons.getInstance().getChansons().get(index);

        artiste = enCours.getNomArtiste();
        album = enCours.getAlbum();
        titre = enCours.getTitre();
        pochette = enCours.getPochette();

        textViewChanson.setText(titre);
        textViewArtiste.setText(artiste);
        textViewAlbum.setText(album);

        int duree = enCours.getDuree();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duree);
        long secondes = TimeUnit.MILLISECONDS.toSeconds(duree) % 60;


        textViewTempsFin.setText(minutes + ":" + secondes);

        File imgFile = new File(pochette);

        if (imgFile.exists()) {

            Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView iv = (ImageView) findViewById(R.id.imageViewPochette);
            iv.setImageBitmap(bm);
        }
    }

    private class MusiqueConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicSrv = binder.getService(); //permet d’initialiser variable déclaréee
            mediaPlayer = musicSrv.getDiffuseur(); // permet d’initialiser variable déclarée

            gd = new GestionDiffuseur();
            mediaPlayer.setOnPreparedListener(gd);
            mediaPlayer.setOnCompletionListener(gd);
            mediaPlayer.setOnErrorListener(gd);
            mediaPlayer.setOnSeekCompleteListener(gd);


            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    }







}




