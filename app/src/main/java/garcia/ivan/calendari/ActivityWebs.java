package garcia.ivan.calendari;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityWebs extends AppCompatActivity {

    String[] webs_nom;
    String[] webs_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webs);

        //Guillem: Obtenim les dades de les diferents webs d'un arraylist ja creat a values:
        String[] consulta_webs = getResources().getStringArray(R.array.consulta_webs);
        ArrayList<String> webs_list = new ArrayList<>(Arrays.asList(consulta_webs));

        //Guillem: Declarem el nostre list i el referenciem amb le layout:
        ListView list = (ListView) findViewById(R.id.list_webs);

        //Guillem: Definim la longitut dels arrays per introduir la informació:
        webs_nom = new String[consulta_webs.length];
        webs_url = new String[consulta_webs.length];

        //Guillem: Fem un split del "array mare" amb l'identificador ";" i assignem les parts resultants
        //als dos strings que utilitzarem a l'activitat:
        for (int i=0;i<consulta_webs.length;i++) {
            String part[] = consulta_webs[i].split(";");
            webs_nom[i] = part[0];
            webs_url[i] = part[1];
        }

        //Guillem: Creem l'adapter amb un model per defecte d'android:
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, webs_nom);
        list.setAdapter(adapter);

        //Guillem: Obtenim l'URL segons la posició escollida a la llista i ho carregue al navegador:
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, final int pos, long id) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(webs_url[pos]));
                startActivity(browserIntent);
            }
        });
    }
}