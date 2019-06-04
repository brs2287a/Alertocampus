package org.neocampus.alertocampus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.neocampus.alertocampus.R;


public class ThankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank);
        boolean fromInfo = getIntent().getBooleanExtra("fromInfo", false);
        if (!fromInfo) {
            TextView textView = findViewById(R.id.textViewRemerciements);
            textView.setText(getString(R.string.remerciementsNotInfo));
            findViewById(R.id.thank_signature).setVisibility(View.GONE);
        }

    }

    public void btnAcceuil(View v) {
        Intent main = new Intent(ThankActivity.this, MenuActivity.class);
        startActivity(main);
    }

    public void btnapropos(View view) {
    }

    public void btnQuitter(View view) {
        finishAffinity();
        /*moveTaskToBack(true);

        this.finish();*/
        //android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(0);
    }
}
