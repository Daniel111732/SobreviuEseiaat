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

public class ActivityCalendarHorari extends AppCompatActivity {

    private ArrayList<String> CH_list;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_horari);

        //Guillem: Creem i carreguem les dades a un array d'strings
        String[] consulta_CH = getResources().getStringArray(R.array.consulta_CH);
        CH_list = new ArrayList<>(Arrays.asList(consulta_CH));

        //Guillem: Creem i referenciem la llista amb el layout
        final ListView list = (ListView) findViewById(R.id.list_CHmenu);

        //Guillem: Inicialitzem l'adapter amb un model de llista per defecte
        adapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CH_list);
        list.setAdapter(adapter);

        //Guillem: Afegim un listener que segons l'item de la llista escollit obrirà una web o una altre amb un browserIntent
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                CH_list.get(pos);
                if (pos==0){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://eseiaat.upc.edu/ca/estudis/calendaris-academics/curs-2017-18/calendari-graus-2017-2018-eseiaat.pdf"));
                    startActivity(browserIntent);
                }else if(pos==1){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://eseiaat.upc.edu/ca/estudis/calendaris-academics/curs-2017-18/calendari-masters-2017-2018-eseiaat.pdf"));
                    startActivity(browserIntent);
                }else if (pos==2){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://eseiaat.upc.edu/ca/estudis/calendaris-dexamens/pdf/calendari-examens-parcials_17-18_1"));
                    startActivity(browserIntent);
                }else if(pos==3){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://eseiaat.upc.edu/ca/estudis/calendaris-dexamens/pdf/calendari-examens-finals_17-18_1"));
                    startActivity(browserIntent);
                }else if (pos==4){
                    Intent intent = new Intent(view.getContext(), ActivityHorari.class);
                    startActivity(intent);
                }
            }
        });
    }
}