package garcia.ivan.calendari;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityWebs extends AppCompatActivity {

    String[] webs_nom;
    String[] webs_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webs);

        String[] consulta_webs = getResources().getStringArray(R.array.consulta_webs);
        ArrayList<String> webs_list = new ArrayList<>(Arrays.asList(consulta_webs));

        ListView list = (ListView) findViewById(R.id.list_webs);
        webs_nom = new String[consulta_webs.length];
        webs_url = new String[consulta_webs.length];

        for (int i=0;i<consulta_webs.length;i++) {
            String part[] = consulta_webs[i].split(";");
            webs_nom[i] = part[0];
            webs_url[i] = part[1];
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, webs_nom);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, final int pos, long id) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(webs_url[pos]));
                startActivity(browserIntent);
            }
        });
    }
}
