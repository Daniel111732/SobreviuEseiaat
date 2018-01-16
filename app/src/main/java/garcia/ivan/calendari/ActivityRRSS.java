package garcia.ivan.calendari;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ActivityRRSS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rrss);
    }

    public void rrss_fbk(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.facebook.com/universitatUPC"));
        startActivity(browserIntent);
    }

    public void rrss_twt(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://twitter.com/la_UPC"));
        startActivity(browserIntent);
    }

    public void rrss_insta(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.instagram.com/la_upc/"));
        startActivity(browserIntent);
    }

    public void rrss_yb(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/user/upc"));
        startActivity(browserIntent);
    }

    public void rrss_tv(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://tv.upc.edu/"));
        startActivity(browserIntent);
    }
}
