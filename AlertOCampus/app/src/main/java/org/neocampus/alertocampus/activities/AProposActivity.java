package org.neocampus.alertocampus.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.neocampus.alertocampus.R;


public class AProposActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apropos);
    }


    public void btnsite(View v) {
        Uri webpage = Uri.parse("https://www.irit.fr/neocampus");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(intent);
    }


    public void btnapropos(View view) {
    }

    public void btnFSI(View view) {
        Uri webpage = Uri.parse("http://www.fsi.univ-tlse3.fr/");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(intent);
    }

    public void btnDI(View view) {
        Uri webpage = Uri.parse("http://www.fsi.univ-tlse3.fr/");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(intent);
    }
}
