package garcia.ivan.calendari;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityAules extends AppCompatActivity {

    private String data_aules[];
    private ArrayAdapter<String> adapter;
    private ArrayList<Class_Aula> aules;
    private ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aules);

        //Danieel: Primer extraiem les dades del les aules ubicades a Resources
        data_aules = getResources().getStringArray(R.array.data_aules);

        createAules(); //Danieel: Crea un ArrayList amb els objectes Class_Aula buits
        extractData(); //Danieel: Extreu les dades i inserta els noms i horaris als objectes

        //Danieel: Creem el adapter i l'hi afegim el layout i l'array d'aules
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,aules)
        {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View result = convertView;
                if (result == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    result = inflater.inflate(android.R.layout.simple_list_item_1, null);
                }
                Class_Aula item = (Class_Aula) getItem(position);
                TextView txt = (TextView) result.findViewById(android.R.id.text1);

                String label;
                label = "[" + item.getNom() + "]";
                if(item.classeActual().equals(" ")){
                    label = label + "   " +getResources().getString(R.string.no_classe);
                }
                else{
                    label = label + "   " +item.classeActual();
                }
                txt.setText(label);

                return result;
            }
        };
        list = (ListView) findViewById(R.id.list_aules);
        list.setAdapter(adapter);

        //Danieel: Creem metode on item click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                mostraHorariAula(adapterView,view,pos,id);
            }
        });

    }

    //Danieel: Extreu les dades de les aules i els seus respectius horaris
    private void extractData() {
        for(int aula=0;aula<data_aules.length;aula++){

            String[] info = data_aules[aula].split(";");

            //Introdueix els noms
            aules.get(aula).setNom(info[0]);

            for(int assignatura=1;assignatura<info.length;assignatura++){

                String[] info_assignatura = info[assignatura].split("/");

                String nom_assignatura = info_assignatura[0];

                String[] horaris_assignatura = info_assignatura[1].split(",");

                for(int horari=0;horari<horaris_assignatura.length;horari++){
                    String S_dia = horaris_assignatura[horari].split(":")[0];
                    String S_hora_inici = horaris_assignatura[horari].split(":")[1].split("-")[0];
                    String S_hora_final = horaris_assignatura[horari].split(":")[1].split("-")[1];

                    int dia = -1;
                    if(S_dia.equals("L")) dia = 0;
                    if(S_dia.equals("M")) dia = 1;
                    if(S_dia.equals("X")) dia = 2;
                    if(S_dia.equals("J")) dia = 3;
                    if(S_dia.equals("V")) dia = 4;

                    int hora_inici = Integer.valueOf(S_hora_inici);
                    int hora_final = Integer.valueOf(S_hora_final);

                    aules.get(aula).setHorari(nom_assignatura,dia,hora_inici,hora_final);
                }
            }
        }
    }

    //Danieel: A partir de les dades crea els objectes Class_Aula necessaris
    private void createAules() {
        aules = new ArrayList<>();
        for(int i=0; i<data_aules.length;i++){
            aules.add(new Class_Aula());
        }
    }

    //Danieel: Revisa si hi ha connexió a Internet i mitjançant les dades de l'aula accedeix a la web
    //mapes upc per mostrar l'ubicació de l'aula en un plànol
    public void show_planol(View view) {
        //Danieel: Primer revisa si hi ha connexió a Internet
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            String url = "http://mapes.terrassa.upc.edu";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            startActivity(browserIntent);
        }
        else
            Toast.makeText(
                    this,this.getResources().getString(R.string.no_internet),
                    Toast.LENGTH_SHORT).show();
    }

    //Danieel: A partir de les dades de l'item Class_Aula, es genera gridView
    // on mostra en una taula l'horari de l'aula, si seleccionem una hora veurem l'assignatura
    private void mostraHorariAula(AdapterView<?> adapterView, View view, int pos, long id) {
        final Class_Aula classAula = (Class_Aula) adapterView.getItemAtPosition(pos);

        final int files = 14;
        final int columnes = 6;
        int celes = files*columnes;

        final GridView gridView = new GridView(this);

        List<String> mList = new ArrayList<String>();
        for (int i = 0; i < celes; i++) {

            if(i<columnes) {
                if (i == 0) mList.add(i, " ");
                if (i == 1) mList.add(i, getResources().getString(R.string.label_monday));
                if (i == 2) mList.add(i, getResources().getString(R.string.label_tuesday));
                if (i == 3) mList.add(i, getResources().getString(R.string.label_wednesday));
                if (i == 4) mList.add(i, getResources().getString(R.string.label_thursday));
                if (i == 5) mList.add(i, getResources().getString(R.string.label_friday));
            }
            else if(i%columnes==0){
                int n= i/columnes;
                String hora = String.valueOf(n+7);
                mList.add(i,hora);
            }
            else{
                int fila = i/columnes;
                int columna = i%columnes;

                int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1;
                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)-7;

                if(day == columna && hour == fila &&
                        day>=1 && hour>=1 && day<=columnes-1  && hour<=files-1)
                    mList.add(i,"X");
                else
                    mList.add(i, " ");
            }

        }

        gridView.setAdapter(new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, mList){

            private static final int COLOR_WHITE = 0x00ffffff;
            private static final int COLOR_LIGHT_GRAY = 0x50808080;
            private static final int COLOR_GRAY = 0x80808080;
            private static final int COLOR_DARK_GRAY = 0xA0404040;
            private static final int COLOR_GREEN = 0x9000ff00;

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = 0;

                if(position==0) color = COLOR_WHITE;
                else if(position<columnes){
                    if((position%columnes)%2==0) color = COLOR_LIGHT_GRAY;
                    else color = COLOR_GRAY;
                }
                else if(position%columnes==0){
                    if((position/columnes)%2==0) color = COLOR_LIGHT_GRAY;
                    else color = COLOR_GRAY;
                }
                else{
                    int hora = (position/columnes)+7;
                    int dia = (position%columnes)-1;
                    if(classAula.getHorari(dia,hora).equals(" "))
                        color = COLOR_WHITE;
                    else
                        color = COLOR_DARK_GRAY;
                }

                view.setBackgroundColor(color);
                return view;
            }
        });

        gridView.setNumColumns(columnes);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                if(pos>columnes && pos%columnes!=0){
                    int hora = (pos/columnes)+7;
                    int dia = (pos%columnes)-1;

                    String assignatura = classAula.getHorari(dia,hora);
                    String hora_inici = String.valueOf(hora);
                    String hora_fi = String.valueOf(hora+1);
                    String nom_dia = "";

                    if(dia+2== Calendar.MONDAY)nom_dia = getResources().getString(R.string.monday);
                    if(dia+2== Calendar.TUESDAY)nom_dia = getResources().getString(R.string.tuesday);
                    if(dia+2== Calendar.WEDNESDAY)nom_dia = getResources().getString(R.string.wednesday);
                    if(dia+2== Calendar.THURSDAY)nom_dia = getResources().getString(R.string.thursday);
                    if(dia+2== Calendar.FRIDAY)nom_dia = getResources().getString(R.string.friday);

                    if(!classAula.getHorari(dia,hora).equals(" ")){
                        Toast.makeText(getBaseContext(),
                                "["+nom_dia+" @ "+hora_inici+"-"+hora_fi+"] "+assignatura
                                , Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //Danieel: Inserim el GridView en un AlertDialog i el mostrem
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(gridView);
        builder.setTitle(getResources().getString(R.string.classroom)+" "+ classAula.getNom());
        builder.setMessage(getResources().getString(R.string.select_cell));
        builder.show();

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
