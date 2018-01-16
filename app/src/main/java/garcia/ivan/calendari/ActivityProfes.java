package garcia.ivan.calendari;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class ActivityProfes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profes);
    }

    public void buscar_PDIPAS(View view) {

        String url_busca, busca_nom, busca_cognom1, busca_cognom2;
        EditText nom_prof= (EditText) findViewById(R.id.edit_nom);
        EditText cognom1_prof= (EditText) findViewById(R.id.edit_cognom1);
        EditText cognom2_prof= (EditText) findViewById(R.id.edit_cognom2);

        //Guillem: Obtenim dades dels EditText
        busca_nom= nom_prof.getText().toString();
        busca_cognom1= cognom1_prof.getText().toString();
        busca_cognom2= cognom2_prof.getText().toString();

        //Guillem: Dividim URL en diferents parts per afergir els camps de cerca del professor:
        String url1= "http://directori.upc.edu/directori/cercaPersonesServlet?ambitCerca=UPC&modeCerca=A&nom_p=";
        String url2= "&telefon_p=&cognom1=";
        String url3= "&adreca=&cognom2=";
        String url4= "&ue_p=&Cerca=Cerca";

        //Guillem: Unim les diferents parts de l'URL:
        url_busca= url1+busca_nom+url2+busca_cognom1+url3+busca_cognom2+url4;

        //Guillem: Introduim al navegador la URL del directori:
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url_busca));
        startActivity(browserIntent);
    }
}