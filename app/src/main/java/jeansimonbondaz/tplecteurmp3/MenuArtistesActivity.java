package jeansimonbondaz.tplecteurmp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MenuArtistesActivity extends AppCompatActivity {

    Intent intentActuel;
    ListView listViewArtistes;
    ArrayList<String> artistes;
    Intent intentVersChanssons;
    Ecouteur ec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_artistes);

        intentActuel = getIntent();
        intentVersChanssons = new Intent(this, MenuChansonsActivity.class);

        //recupperer la liste de tous les artistes
        artistes = intentActuel.getStringArrayListExtra("artistes");
        listViewArtistes = findViewById(R.id.listViewArtistes);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, artistes);
        listViewArtistes.setAdapter(adapter);

        ec = new Ecouteur();
        listViewArtistes.setOnItemClickListener(ec);

    }


    private class Ecouteur implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View selection, int index, long l) {

            String choixArtiste =  adapterView.getItemAtPosition(index).toString();
            intentVersChanssons.putExtra("artiste", choixArtiste);
            startActivity(intentVersChanssons);

        }
    }

}
