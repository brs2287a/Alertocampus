package org.neocampus.alertocampus.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.neocampus.alertocampus.R;
import org.neocampus.alertocampus.control.Conf;


public class AutreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autre);
    }


    public void btnpdf(View v) {
        Uri webpage = Uri.parse(Conf.LIEN_PDF);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(intent);
    }

    public void btnapropos(View view) {
        Intent apropos = new Intent(AutreActivity.this, AProposActivity.class);
        startActivity(apropos);
    }


}
