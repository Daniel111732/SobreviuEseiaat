package garcia.ivan.calendari;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

public class AdapterAssignatures extends ArrayAdapter<Class_Assignatura_item> {

    private int id_grups[] = {
            R.id.grup1_btn,R.id.grup2_btn,R.id.grup3_btn,R.id.grup4_btn
    };

    private int name_grups[] = {
            R.id.grup1_name,R.id.grup2_name,R.id.grup3_name,R.id.grup4_name
    };

    public AdapterAssignatures(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View result = convertView;
        if (result==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            result = inflater.inflate(R.layout.activity_assignatura, null);
        }
        TextView assignatura_name = (TextView) result.findViewById(R.id.assignatura_name);
        final RadioGroup groups = (RadioGroup) result.findViewById(R.id.assignatures_grups);

        // Ivan: S'afegeix el nom de l'assignatura
        String item_text = getItem(position).getNom();
        assignatura_name.setText(item_text);
        // Ivan: S'agafa el número de grups per saber quants radio buttons s'han de mostrar
        int n_groups = getItem(position).getN_grups();
        String [] grups = getItem(position).getGrups();
        for (int i=0; i < (id_grups.length); i++) {
            final RadioButton rb = (RadioButton) result.findViewById(id_grups[i]);
            TextView g_name = (TextView) result.findViewById(name_grups[i]);
            // Ivan: Si hi ha grup el radio buton serà visible
            if(i<n_groups){
                rb.setVisibility(View.VISIBLE);
                g_name.setVisibility(View.VISIBLE);
                g_name.setText(grups[i]);
            }
            // Ivan: Si no hi ha grup el radio button serà invisible i no ocuparà espai dins del layout (GONE)
            else {
                rb.setVisibility(View.GONE);
                g_name.setVisibility(View.GONE);
            }

            /* Ivan: Si s'havia escollit un grup previament, el radio button corresponent al grup el mostrarà activat
                     si no es fa això, quan es un radio button també es selecciona
                     en l'assignatura que hi ha 12 posicions més abaix
             */
            int grup_escollit;
            grup_escollit = (getItem(position).getGrup_escollit());
            // Ivan: si s'ha escollit grup (grup escollit es 1,2,3,4) el radiogroup que activa el radiobutton corresponent
            if((grup_escollit>0)&&(grup_escollit<=4)) {
                groups.check(id_grups[(grup_escollit)-1]);
            }
            // Ivan: si no s'ha escollit grup (grup escollit es 0) el radiogroup que desactiva tots els radiobutton
            else groups.clearCheck();

            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Ivan: al fer  clic sobre un radio  button es damana al radio group la referencia del radio button clicat
                    int new_id = groups.getCheckedRadioButtonId();
                    //Ivan: Buscar l'index corresponent a la referencia del RadioButton seleccionat
                    for (int i=0; i<id_grups.length;i++){
                        if(new_id==id_grups[i]){
                            new_id = i+1;
                            break;
                        }
                    }
                    // Ivan: si el radiobutton ja estava activa previament, el desactiva
                    if (new_id==(getItem(position).getGrup_escollit())){
                        getItem(position).setGrup_escollit(0);
                        groups.clearCheck();
                    }
                    // Ivan: si el radiobutton no estava activa previament, desa el numero del radiobutton selecionat
                    else getItem(position).setGrup_escollit(new_id);
                }
            });
        }

        return result;
    }

}
