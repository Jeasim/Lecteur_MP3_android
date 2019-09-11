package jeansimonbondaz.tplecteurmp3;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Vector;

public class EnsembleChansons {
    private static EnsembleChansons instance = new EnsembleChansons();

    private Vector<Chanson> chansons;
    private ContentResolver cr;
    private int index;

//----------------- Constructeur ------------------------------

    private EnsembleChansons() {
        chansons = new Vector<Chanson>();
    }

//----------------- Methodes ----------------------------------

    public int chansonPrecedente(int indexChanson) {


        if (indexChanson == 0) {
            index = chansons.size() - 1;
        } else {
           index = --indexChanson;
        }

        return index;
    }

    public int chansonSuivante(int indexChanson) {


        if (indexChanson == chansons.size() - 1) {
            index = 0;
        } else {
            index = ++indexChanson;
        }

        return index;
    }

    public int chansonAleatoire()
    {

        int min = 0;
        int max = chansons.size() - 1;

        index = min + (int) (Math.random() * ((max - min) + 1));

        return index;
    }


    public void remplirVecteurChansons(Activity a) {

        cr = a.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri imageUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Chanson ajout;
        int indexChanson = 0;

        Cursor musicCursor = cr.query(musicUri, new String[]{"_id", "title", "album", "artist", "duration"}, null, null, "title");

        if (musicCursor != null) {
            while (musicCursor.moveToNext()) {

                if (musicCursor.getString(1) != null && musicCursor.getString(2) != null && musicCursor.getString(3) != null) {

                    ajout = new Chanson((int) musicCursor.getLong(0), indexChanson, musicCursor.getString(1), musicCursor.getString(3), musicCursor.getString(2), (int) musicCursor.getLong(4));

                    Cursor pochetteCursor = cr.query(imageUri, new String[]{"album_art"}, "album = ?", new String[]{musicCursor.getString(2)}, null);

                    if (pochetteCursor != null) {
                        pochetteCursor.moveToFirst();
                        ajout.setPochette(pochetteCursor.getString(0));
                        pochetteCursor.close();
                    }

                    chansons.add(ajout);

                    indexChanson++;
                }
            }

            musicCursor.close();
        }

    }

    public ArrayList<String> listeArtistes(Activity a) {
        ArrayList<String> artistes = new ArrayList<String>();

        cr = a.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = cr.query(musicUri, new String[]{"_id", "artist"}, null, null, "artist");

        if (musicCursor != null) {
            while (musicCursor.moveToNext()) {

                String temp = musicCursor.getString(1);

                if (!artistes.contains(temp)) {
                    artistes.add(musicCursor.getString(1));
                }
            }
        }
        musicCursor.close();

        return artistes;
    }

    public Vector<Chanson> chansonsParArtiste(String artiste) {
        Vector<Chanson> chansonsArtiste = new Vector<Chanson>();

        for (Chanson c : chansons) {
            if (c.getNomArtiste().equals(artiste)) {
                chansonsArtiste.add(c);
            }
        }

        return chansonsArtiste;
    }


//----------------- Getters ------------------------------------

    public static EnsembleChansons getInstance() {
        return instance;
    }

    public Vector<Chanson> getChansons() {
        return chansons;
    }

    public int getIndex() {
        return index;
    }
}
