package garcia.ivan.calendari;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Adapter_AulaPC extends ArrayAdapter<Class_AulaPC> {

    private static final int COLOR_WHITE = 0x00ffffff;
    private static final int COLOR_LIGHT_GRAY = 0x50808080;
    private static final int COLOR_GRAY = 0x80808080;
    private static final int COLOR_RED = 0x90ff0000;
    private static final int COLOR_GREEN = 0x9000ff00;

    public Adapter_AulaPC(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    //Danieel: Posa l'etiqueta de l'aula i edifici de cada item i canvia el color en funció de la
    //disponibilitat de l'aula en aquell moment
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View result = convertView;
        if (result == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            result = inflater.inflate(R.layout.layout_pcitem, null);
        }

        final Class_AulaPC item = getItem(position);
        TextView text = (TextView) result.findViewById(R.id.text);
        Button btn_horari = (Button) result.findViewById(R.id.btn_horari);
        Button btn_mapa = (Button) result.findViewById(R.id.btn_mapa);

        text.setText("[ "+item.getEdifici()+" - "+item.getAula()+" ]");

        if(item.isDisponible())
            result.setBackgroundColor(COLOR_GREEN);
        else
            result.setBackgroundColor(COLOR_RED);

        btn_horari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimetable(item);
            }
        });

        btn_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMap(item);
            }
        });

        return result;
    }

    //Danieel: A partir d'un array de booleans genera gridView on mostra en una taula la disponibilitat
    // de l'aula d'informàtica
    private void showTimetable(final Class_AulaPC item) {

        final int files = 14;
        final int columnes = 6;
        int celes = files*columnes;

        final GridView gridView = new GridView(getContext());

        List<String> mList = new ArrayList<String>();
        for (int i = 0; i < celes; i++) {

            if(i<columnes) {
                if (i == 0) mList.add(i, " ");
                if (i == 1) mList.add(i, gridView.getResources().getString(R.string.label_monday));
                if (i == 2) mList.add(i, gridView.getResources().getString(R.string.label_tuesday));
                if (i == 3) mList.add(i, gridView.getResources().getString(R.string.label_wednesday));
                if (i == 4) mList.add(i, gridView.getResources().getString(R.string.label_thursday));
                if (i == 5) mList.add(i, gridView.getResources().getString(R.string.label_friday));
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

        gridView.setClickable(false);
        gridView.setAdapter(new ArrayAdapter
                (getContext(), android.R.layout.simple_list_item_1, mList){
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
                    if(item.isDisponible(dia,hora)) color = COLOR_GREEN;
                    else color = COLOR_RED;
                }

                view.setBackgroundColor(color);
                return view;
            }
        });

        gridView.setNumColumns(columnes);

        //Danieel:  Inserim el GridView en un AlertDialog i el mostrem
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(gridView);
        builder.setTitle(item.getEdifici()+" - "+item.getAula());
        builder.setMessage(
                gridView.getResources().getString(R.string.classroom_available)+" "+item.getEdifici()+" - "+item.getAula());
        builder.show();
    }

    //Danieel: Verifica la connexió a Internet i mitjançant les dades de l'aula accedeix al web
    // i mostra el mapa amb l'ubicació de l'aula
    public void showMap(Class_AulaPC item){
        //Primer revisa si hi ha connexió a Internet
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            String url = "http://mapes.terrassa.upc.edu";
            url += "?aula=";
            url += item.getAula();
            url += "&edifici=";
            url += item.getEdifici();

            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            getContext().startActivity(browserIntent);
        }
        else
            Toast.makeText(
                    getContext(),getContext().getResources().getString(R.string.no_internet),
                    Toast.LENGTH_SHORT).show();
    }
}

