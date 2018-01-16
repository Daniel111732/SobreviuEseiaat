package garcia.ivan.calendari;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ActivityInfo extends AppCompatActivity {

    private ArrayList<String> options_list;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }

    public void consulta_profes(View view) {
        Intent intent = new Intent(this, ActivityProfes.class);
        startActivity(intent);
    }

    public void consulta_webs(View view) {
        Intent intent = new Intent(this, ActivityWebs.class);
        startActivity(intent);
    }

    public void consulta_rrss(View view) {
        Intent intent = new Intent(this, ActivityRRSS.class);
        startActivity(intent);
    }
}