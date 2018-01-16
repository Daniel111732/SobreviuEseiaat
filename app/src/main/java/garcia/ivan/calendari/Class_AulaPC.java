package garcia.ivan.calendari;

import java.util.Arrays;
import java.util.Calendar;

//La classe Class_AulaPC conté els artibuts i mètodes necessaris per gestionar les aules d'informàtica
public class Class_AulaPC {

    private String aula;
    private String edifici;
    private boolean disponible;
    private boolean[][] horari;

    public Class_AulaPC(){
        horari = new boolean[7][24];
        for (boolean[] row : horari)
            Arrays.fill(row, true);
        updateAvailable();
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    private void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getEdifici() {
        return edifici;
    }

    public void setEdifici(String edifici) {
        this.edifici = edifici;
    }

    public boolean isDisponible() {
        updateAvailable();
        return disponible;
    }

    public void addClass(int day, int hour_start, int hour_end){
        for(int i = hour_start; i<hour_end;i++)
        horari[day][i] = false;
        updateAvailable();
    }

    //Agafa l'hora local del mòbil i actualitza la disponibilitat actual de l'aula
    private void updateAvailable(){

        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if(day== Calendar.SATURDAY || day== Calendar.SUNDAY || hour<8 || hour>20)
            setDisponible(false);
        else{
            setDisponible(horari[day-2][hour]);
        }

    }

    public boolean isDisponible(int day, int hour){
        return horari[day][hour];
    }

}

