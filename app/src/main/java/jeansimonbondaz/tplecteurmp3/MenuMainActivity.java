package jeansimonbondaz.tplecteurmp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MenuMainActivity extends AppCompatActivity {

    Ecouteur ec;
    Intent intentVersMenuChansons;
    Intent intentVersMenuArtistes;
    ArrayList<String> artistes;
    Button btnArtistes;
    Button btnChansons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        //On va chercher toutes les donnees necessaires sur la carte SD au demarage de l'application
        EnsembleChansons.getInstance().remplirVecteurChansons(this);
        artistes = EnsembleChansons.getInstance().listeArtistes(this);

        btnArtistes = findViewById(R.id.btnArtistes);
        btnChansons = findViewById(R.id.btnChansons);

        intentVersMenuArtistes = new Intent(this, MenuArtistesActivity.class);
        intentVersMenuChansons = new Intent(this, MenuChansonsActivity.class);

        ec = new Ecouteur();

        btnArtistes.setOnClickListener(ec);
        btnChansons.setOnClickListener(ec);
    }

    private class Ecouteur implements View.OnClickListener
    {
        @Override
        public void onClick(View selection) {

            if(selection == btnArtistes)
            {
                //passe le ArrayList de tous les artistes a l'activite MenuArtistes
                intentVersMenuArtistes.putExtra("artistes", artistes);
                startActivity(intentVersMenuArtistes);
            }
            else if(selection == btnChansons)
            {
                startActivity(intentVersMenuChansons);
            }

        }
    }

}
