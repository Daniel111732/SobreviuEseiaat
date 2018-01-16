package garcia.ivan.calendari;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


//Danieel: Aquesta és l'activitat principal de l'aplicació
//Danieel: Els mètodes obren les activitats corresponents a cada apartat de l'aplicació
public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void consulta_info(View view) {
        Intent intent = new Intent(this, ActivityInfo.class);
        startActivity(intent);
    }

    public void consulta_pc(View view) {
        Intent intent = new Intent(this, ActivityPC.class);
        startActivity(intent);
    }

    public void consulta_aules(View view) {
        Intent intent = new Intent(this, ActivityAules.class);
        startActivity(intent);
    }

    public void consulta_calendar(View view) {
        Intent intent = new Intent(this, ActivityCalendarHorari.class);
        startActivity(intent);
    }

    //Danieel: Obre la web d'atenea al navegador d'internet del mòbil
    public void login_atenea(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://atenea.upc.edu"));
        startActivity(browserIntent);
    }
}
