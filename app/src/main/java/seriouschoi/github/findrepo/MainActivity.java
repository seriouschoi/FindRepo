package seriouschoi.github.findrepo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickShowRepo(View view) {
        String url = "test://repos/google";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        this.startActivity(intent);
    }
}
