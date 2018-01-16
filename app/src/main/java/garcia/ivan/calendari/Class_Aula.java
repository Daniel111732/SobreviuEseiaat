package garcia.ivan.calendari;

import java.util.Arrays;
import java.util.Calendar;


//Danieel: La classe Class_Aula conté els artibuts i mètodes necessaris per gestionar les aules docents
public class Class_Aula {

    private String nom;
    private String[][] horari;

    public Class_Aula(){
        horari = new String[7][24];
        for (String[] row : horari)
            Arrays.fill(row, " ");
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getHorari(int day, int hour) {
        return horari[day][hour];
    }

    public void setHorari(String classe, int day, int hour_start, int hour_end) {
        for(int hour = hour_start; hour<hour_end;hour++)
        horari[day][hour] = classe;
    }

    public String classeActual(){
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if(day== Calendar.SATURDAY || day== Calendar.SUNDAY || hour<8 || hour>20)
            return " ";
        else{
            return horari[day-2][hour];
        }
    }
}
