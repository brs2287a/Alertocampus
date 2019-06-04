package org.neocampus.alertocampus.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.neocampus.alertocampus.R;
import org.neocampus.alertocampus.control.EnvoiTicket;
import org.neocampus.alertocampus.control.SessionManager;


public class EchecActivity extends AppCompatActivity {

    private String sujet;
    private String corps;
    private byte[] compressedPhoto;
    private boolean pbMail;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echec);
        Intent local = getIntent();
        this.sessionManager = new SessionManager(getApplicationContext());
        pbMail = local.getBooleanExtra("mail", false);
        if (pbMail) {
            ((TextView) findViewById(R.id.textViewEchec)).setText(R.string.pbMail);
            findViewById(R.id.layoutPbMail).setVisibility(View.VISIBLE);
            findViewById(R.id.textViewEchecMail).setVisibility(View.VISIBLE);
        }
    }

    public void btnAcceuil(View v) {
        Intent main = new Intent(EchecActivity.this, MenuActivity.class);
        startActivity(main);
    }

    public void btnapropos(View view) {
    }

    public void btnQuitter(View view) {
        finishAffinity();

    }

    public void btnRéessayer(View view) {
        EditText editTextMail = findViewById(R.id.mail_univ);
        if (!editTextMail.getText().toString().equals("")) {
            sessionManager.setKeyMail(editTextMail.getText().toString().replace(" ", "") + getString(R.string.texte_fin_mail));
        }
        Intent intent = getIntent();
        sujet = intent.getStringExtra("sujet");
        corps = intent.getStringExtra("corps");
        compressedPhoto = intent.getByteArrayExtra("photo");

        EnvoiTicket envoiTicket = new EnvoiTicket(sujet, corps, this.compressedPhoto, this, new Handler(), intent.getStringExtra("dest"));

        envoiTicket.envoyer();
    }
}
