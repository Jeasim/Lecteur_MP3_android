package jeansimonbondaz.tplecteurmp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.Hashtable;
import java.util.Vector;

public class MenuChansonsActivity extends AppCompatActivity {


    Ecouteur ec;
    Intent intentActuel;
    Intent intentVersLecture;
    ListView listeChansons;
    Vector<Chanson> chansons;
    String artiste;
    Vector<Hashtable<String, String>> hashChansons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chansons);

        intentActuel = getIntent();
        intentVersLecture = new Intent(this, LectureActivity.class);

        //hashtable pour stocker toutes les chansons
        hashChansons = new Vector<Hashtable<String, String>>();

        listeChansons = findViewById(R.id.listViewChansons);
        artiste = intentActuel.getStringExtra("artiste");


        if (artiste != null) {

            chansons = EnsembleChansons.getInstance().chansonsParArtiste(artiste);
        } else {

            chansons = EnsembleChansons.getInstance().getChansons();
        }


        for (Chanson c : chansons) {
            Hashtable<String, String> temp = new Hashtable<String, String>();
            temp.put("artiste", c.getNomArtiste());
            temp.put("chanson", c.getTitre());
            temp.put("album", c.getAlbum());
            temp.put("pochette", c.getPochette());

            hashChansons.add(temp);
        }

        SimpleAdapter adaptateur = new SimpleAdapter(this, hashChansons, R.layout.chanson, new String[]{"artiste", "chanson", "album", "pochette"}, new int[]{R.id.textViewArtiste, R.id.textViewChanson, R.id.textViewAlbum, R.id.imageViewPochetteAlbum});
        listeChansons.setAdapter(adaptateur);

        ec = new Ecouteur();
        listeChansons.setOnItemClickListener(ec);
    }


    private class Ecouteur implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            Chanson elementSelectionne = chansons.get(position);

            intentVersLecture.putExtra("index", elementSelectionne.getIndex());
            startActivity(intentVersLecture);
        }
    }

}
