package garcia.ivan.calendari;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityPC extends AppCompatActivity {

    private Adapter_AulaPC adapter;
    private ListView list;
    private ArrayList<Class_AulaPC> aulesPC;
    private String[] data_aules_pc;
    private int[] ids_maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pc);

        //Danieel: Primer extraiem les dades del les aules ubicades a Resources
        data_aules_pc = getResources().getStringArray(R.array.data_aules_pc);

        createAulesPC(); //Crea un ArrayList amb els objectes Class_AulaPC buits
        extractData(); // Extreu les dades i les insereix als objectes

        //Danieel: Creem el adapter i l'hi afegim el layout i l'array d'aulesPC
        adapter = new Adapter_AulaPC(this,R.layout.activity_pc,aulesPC);
        list = (ListView) findViewById(R.id.listPC);
        list.setAdapter(adapter);
    }

    //Danieel: A partir de les dades crea els objectes Class_AulaPC necessaris
    private void createAulesPC() {
        aulesPC = new ArrayList<>();
        for(int i = 0; i< data_aules_pc.length; i++){
            aulesPC.add(new Class_AulaPC());
        }
    }

    //Danieel: Extreu les dades i fa el tractament necessari per després introduir-les als
    //objectes Class_AulaPC
    private void extractData() {
        for(int aula = 0; aula< data_aules_pc.length; aula++){

            String[] info = data_aules_pc[aula].split(";");

            aulesPC.get(aula).setEdifici(info[0]);
            aulesPC.get(aula).setAula(info[1]);

            for(int day=2;day<info.length;day++)
            {
                if (!(info[day].equals("null")))
                {
                    String[] timetable = info[day].split(",");
                    for (int i = 0; i < timetable.length; i++)
                    {
                        String[] hours = timetable[i].split("-");
                        int hour_start = Integer.valueOf(hours[0]);
                        int hour_end = Integer.valueOf(hours[1]);
                        aulesPC.get(aula).addClass(day-2, hour_start, hour_end);
                    }
                }
            }
        }
    }

    //Danieel: Crea el menú
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_actualitza, menu);
        return true;
    }

    //Danieel: El menú té una única opció que permet refrescar l'activitat
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
        Toast.makeText(this,getResources().getString(R.string.refresh_data), Toast.LENGTH_SHORT).show();
        return true;
    }
}
