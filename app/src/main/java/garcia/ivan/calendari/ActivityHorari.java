package garcia.ivan.calendari;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ActivityHorari extends AppCompatActivity {

    private static final String FILENAME = "Horari.txt";
    private static final int MAX_BYTES = 8000;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> abreviacions_list;
    private ArrayList<String> info_list;
    private ArrayList<Integer> color_list;
    private String[] lines;
    private String titol_info[] = {
            "Assignatura: ",
            "Grup: ",
            "Idioma: ",
            "Professor: ",
            "Tipus: ",
            "Aula: ",
            "Setmana: "
    };

    // Ivan: Llegir txt amb les dades de l'horari personalitzat

    private void readItemList(){
        abreviacions_list = new ArrayList<>();
        info_list = new ArrayList<>();
        color_list = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            byte[] buffer = new byte[MAX_BYTES];
            int nread = fis.read(buffer);
            if(nread>0) {
                String content = new String(buffer, 0, nread);
                // Ivan: Es separen les assignatures i es guarden a l'array de Strings line
                lines = content.split("\n");
                for(String line : lines){
                    String parts[] = line.split("/");
                    // Ivan: Es guarda l'abreviació de l'assignatura a la llista abreviacions_list
                    abreviacions_list.add(new String(parts[0]));
                    // Ivan: Es guarda la informació de l'assignatura a la llista info__list
                    info_list.add(new String(parts[1]));
                    // Ivan: Es guarda el color de l'assignatura a la llista color_list
                    color_list.add(Integer.parseInt(parts[2]));
                }
            }
            fis.close();
            // Ivan: Si encara no s'ha generat el TXT amb les dades obre l'activitat ActivityModificaHorari per generar-lo
        } catch (FileNotFoundException e) {
            Intent intent = new Intent(this, ActivityModificaHorari.class);
            startActivity(intent);
        } catch (IOException e) {
            Intent intent = new Intent(this, ActivityModificaHorari.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ivan: Llegir fitxer de l'horari
        readItemList();
        // Ivan: Mostrar el Layout de l'horari
        setContentView(R.layout.activity_horari);
        GridView gridview = (GridView) findViewById(R.id.gridview);

        /* Ivan: L'adaptador d'arrays posará a cada item del GridView l'abreviació de l'assignatura corresponent
                 Quan s'ha de mostrar un número l'abreviació té el valor del número i no s'afegeix color
                 L'adaptador posa a cada assignatura el color que li correspon de la llista color_list */
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, abreviacions_list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                int color[] = {
                        0,
                        getResources().getColor(R.color.colorA1),
                        getResources().getColor(R.color.colorA2),
                        getResources().getColor(R.color.colorA3),
                        getResources().getColor(R.color.colorA4),
                        getResources().getColor(R.color.colorA5),
                        getResources().getColor(R.color.colorA6),
                        getResources().getColor(R.color.colorA7)
                };

                View view = super.getView(position, convertView, parent);
                view.setBackgroundColor(color[color_list.get(position)]);
                return view;
            }
        };
        gridview.setAdapter(adapter);

        // Ivan: Al fer clic sobre un item mirará si es una assignatura i mostrará la informació en un AlertDialog
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String info = info_list.get(position);
                // Ivan: Si l'item clicat no es una assignatura, la seva posició a la llista info estarà buida
                if (!(TextUtils.isEmpty(info))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHorari.this);
                    // Ivan: El títol de l'AlertDialog serà l'abreviació de l'assignatura
                    builder.setTitle("                   " + abreviacions_list.get(position));
                    String[] parts = info.split(";");
                    String message = "";
                    /* Ivan: L'array d'Strings titol_info indica el titol de la informació a mostrar
                       El bucle només mostrará la informació disponible,
                       és a dir que si no hi ha setmana parell o senar no es mostrarà */
                    for (int i = 0; i < parts.length; i++) {
                        message = message + titol_info[i];
                        message = message + parts[i] + "\n";
                    }
                    builder.setMessage(message);
                    builder.setCancelable(true);
                    builder.create().show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_horari,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.modifica_horari:
                Intent intent = new Intent(this, ActivityModificaHorari.class);
                finish();
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
