package garcia.ivan.calendari;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;

public class ActivityModificaHorari extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    public  ArrayList<String> titulacions_list;
    private static int titulacio_pos;

    public static int getTitulacio_pos() {
        return titulacio_pos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titulacio);

        String[] titulacions = getResources().getStringArray(R.array.titulacions);
        this.titulacions_list = new ArrayList<>(Arrays.asList(titulacions));
        ListView list = (ListView) findViewById(R.id.titulacions_list);

        // Ivan: ListView que mostra les titulacions
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.titulacions_list);
        list.setAdapter(adapter);

        /* Ivan: Al clicar sobre una titulació guarda la posició corresponent a aquesta titulació,
                 la posició guardada s'utilitzarà a l'activitat següent, ActivityAssignatures*/
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                titulacio_pos = position;
                Intent intent = new Intent(getApplicationContext(), ActivityAssignatures.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
