package garcia.ivan.calendari;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class ActivityAssignatures extends AppCompatActivity {

    private static final String FILENAME = "Horari.txt";
    private ArrayList<Class_Assignatura_item> item_list;
    private AdapterAssignatures adapter;
    private Button btn_confirmar;
    private ListView assignatures_list;
    String[] assignatures;
    private ArrayList<String> horari_parts;
    String line;
    private List<String> horariFirebase;
    private static FirebaseDatabase database;

    private int id_titulacions[] = {
            R.array.assignatures_disseny,R.array.assignatures_tecnologies,R.array.assignatures_electrica,
            R.array.assignatures_electronica,R.array.assignatures_mecanica,R.array.assignatures_quimica,
            R.array.assignatures_textil,R.array.assignatures_tecaero,R.array.assignatures_vehaero,
            R.array.assignatures_audiovisuals
    };


    private void writeItemList(){

        int grup_escollit;

        // Ivan: Es miren cuantes assigntures s'han seleccionat i guarda a la variable n_indexs
        int n_indexs = 0;
        for(int i=0; i < assignatures.length; i++){
            grup_escollit = item_list.get(i).getGrup_escollit();
            // Ivan: Si es grup escollit es 0, significa que NO s'ha seleccionat l'assignatura
            if(grup_escollit>0) n_indexs++;
        }

        // Ivan: Es crea l'array amb els índexs de les assignatures seleccionades
        int [] index = new int[n_indexs];
        n_indexs = 0;
        /* Ivan: Es guarden els índexs a l'array
        L'index correspón a la posició del grup seleccionat en la Llista on estan totes les assignatures de la ESEIAAT */
        for(int i=0; i < assignatures.length; i++) {
            grup_escollit = item_list.get(i).getGrup_escollit();
            if(grup_escollit>0){
                index[n_indexs] = item_list.get(i).getIndex(grup_escollit);
                n_indexs++;
            }
        }

        /* Ivan: Es guarda el String corresponent a l'índex de cada assignatura,
                 l'array d'Strings horari item tindrà la informació de totes les assignatures seleccionades*/
        String[] horari_item = new String[index.length];
        for(int i=0; i < horari_item.length; i++) {
            horari_item[i] = horariFirebase.get(index[i]);
        }

        horari_parts = new ArrayList<>();
        int color;

        /* Ivan: S'assigna el color de fons que tindrà l'assignatura cuan es mostri a l'horari,
           el color és un número del 0 al 7, si l'usuari escull més de 7 assignatures es repetirà el color,
           en teoria un alumne no pot fer més de 7 assignatures per quatrimestre.
           Després d'afegir el color es guarda cada hora cursada dins la llista d'Strings horari_parts
        */

        for(int i=0; i < horari_item.length; i++) {
            String[] parts = horari_item[i].split("\n");
            for (int j=0; j < parts.length; j++) {
                color = (i%6)+1;
                horari_parts.add(new String(parts[j] + "/" + color));
            }
        }

        // Ivan: line serà es String que es guardarà al TXT de l'horari personalitzat
        line = "";
        int hora, n_horas, horas, inici, acaba;
        String text_hora;

        // Ivan: Bucle de 16 iteracions, ja que des de les 8h fins les 22h hi ha 16 hores
        for(int z=0; z < 16; z++) {
            inici = z*5;
            acaba = inici + 5;
            horas = 0;
            // Ivan: Es mira quantes files hi ha per a cada hora, ja que si hi ha assignatures solapades s'han de mostrar
            for (int j = inici; j < acaba; j++) {
                n_horas = 0;
                for (int i = 0; i < horari_parts.size(); i++) {
                    // Ivan: la primera part equival la posició del GridView on va l'assinatura
                    String[] parts = horari_parts.get(i).split("__");
                    hora = Integer.parseInt(parts[0]);
                    if (hora == j) n_horas++;
                }
                // Ivan: hores serà el valor màxim de de files per hora
                if (n_horas > horas) horas = n_horas;
            }

            boolean escrit;
            /* Ivan: s'escriuen les files que ha d'haver per a cada hora
                     Si en una hora no hi ha cap assignatura no s'escriu cap fila(no entra en el bucle)
                     Si en una hora hi ha una assignatura escriu una fila
                     Si en una hora d'una mateix dia hi ha dos assignatures solapades,
                     escriurà dos files per mostrar les dos assignatures
            */
            for (int i = 0; i < horas; i++) {
                // Ivan: S'escriu el valor de la hora de la fila que anirà en la primera columna del GridView
                text_hora = String.valueOf(z+8);
                // Ivan: No té color ni informació(no sortirà AlertDialog al clicar sobre la hora)
                line = line + text_hora + "//0\n";
                // Ivan: S'escriu la primera assignatura de la llista amb tingui la hora corresponent
                for (int k = inici; k < acaba; k++) {
                    escrit = false;
                    for (int j = 0; j < horari_parts.size(); j++) {
                        String[] parts = horari_parts.get(j).split("__");
                        hora = Integer.parseInt(parts[0]);
                        /* Ivan: La primera assignatura amb la hora corresponent s'esciurà a line
                                 Com no es pot borrar de llista l'assignatura escrita ja que hi haurà error de bucle,
                                 (el tamany del bucle no correspondria a el nou tamany de la llista)
                                 es sobresciurà l'assinatura amb un valor de hora que mai s'agafarà 9999
                                 */
                        if (hora == k) {
                            line = line + parts[1] + "\n";
                            escrit = true;
                            horari_parts.set(j, "9999__");
                            j = horari_parts.size();
                        }
                    }
                    // Ivan: Si no hi ha assignatura en aquesta hora s'escriu una cassella en blanc sense color i sense informació
                    if (!escrit) line = line + "//0\n";
                }
            }
        }

        // Ivan: Es genera el TXT amb l'String lines, amb aquest TXT es genera l'horari a HorariActivity
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(line.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Error:FileNotFound", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error:FileNotFound", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignatures);
        if (database == null) {
            //Ivan: Conectar al Firebase
            database = FirebaseDatabase.getInstance();
            //Ivan: La base de dades es guarda per poder treballar amb ella sense connexió
            database.setPersistenceEnabled(true);
        }
        //Ivan: Es guarda la base de horaris del Firebase el primer cop o cuan hi hagi un canvi
        DatabaseReference myRef = database.getReference("horaris");


            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    // Ivan: Crear llista de tipus generic (String)
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                    };
                    //Ivan: Carregar a la llista local horariFirebase, la llista de l'horari del Firebase
                    horariFirebase = dataSnapshot.getValue(t);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Firebase", "Failed to read value.", error.toException());
                }
            });


        // Ivan: S'agafa la posició que es va guardar a l'activitat anterior, ActivityModificaHorari
        int titulacio_position = 0;
        titulacio_position = ActivityModificaHorari.getTitulacio_pos();

        assignatures_list = (ListView) findViewById(R.id.assignatures_list);
        btn_confirmar = (Button) findViewById(R.id.btn_confirmar);

        /* Ivan: Al fer clic sobre el botó confirmar s'escriurà el TXT de l'horari segons les assignatures seleccionades,
                 després s'obrirà l'activitat HorariActivity on es mostra l'horari*/
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeItemList();
                Intent intent = new Intent(getApplicationContext(), ActivityHorari.class);
                finish();
                startActivity(intent);
            }
        });

        /* Ivan: S'agafa l'array d'Strings amb les assignatures corresponents a la titulació sel·leccionada
                 Amb la posició obtinguda de l'activitat anterior s'agafa la referencia de la titulació de
                 l'array de referenecies id_titulacions*/
        assignatures = getResources().getStringArray(id_titulacions[titulacio_position]);

        item_list = new ArrayList<>();
        // Ivan: Es construeix una llista item_list de tipues assignatura_item
        for (int i=0; i<assignatures.length; i++) {
            item_list.add(new Class_Assignatura_item(assignatures[i]));
        }
        // Ivan: S'utilitza el adaptador d'asssignatures amb la llista item list
        adapter = new AdapterAssignatures(this, R.layout.activity_assignatura, item_list);
        assignatures_list.setAdapter(adapter);

    }

}