package org.neocampus.alertocampus.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.neocampus.alertocampus.R;
import org.neocampus.alertocampus.control.Conf;


public class PbMenuActivity extends AppCompatActivity {

    private String sujet;
    private String corps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pb_menu);

    }

    public void btnSoftware(View v) {
        sujet = Conf.ENTETE_MAIL;
        corps = getResources().getString(R.string.debut_corps_mail);
        Intent software = new Intent(PbMenuActivity.this, SoftwareActivity.class);
        sujet += " " + v.getContentDescription().toString() + " ";
        corps += "typePb : " + v.getContentDescription().toString() + "\n";
        software.putExtra("sujet", sujet);
        software.putExtra("corps", corps);
        //software.putExtra("typePb", v.getContentDescription());
        startActivity(software);
    }

    public void btnPbConnexion(View v) {
        sujet = Conf.ENTETE_MAIL;
        corps = getResources().getString(R.string.debut_corps_mail);
        Intent pb_connexion = new Intent(PbMenuActivity.this, PbConnexionActivity.class);
        sujet += " " + v.getContentDescription().toString() + " ";
        corps += v.getContentDescription().toString() + "\n";
        pb_connexion.putExtra("sujet", sujet);
        pb_connexion.putExtra("corps", corps);
        startActivity(pb_connexion);
    }

    public void btnMachine(View v) {
        sujet = Conf.ENTETE_MAIL;
        corps = getResources().getString(R.string.debut_corps_mail);
        Intent machine = new Intent(PbMenuActivity.this, MaterielActivity.class);
        sujet += " " + v.getContentDescription().toString() + " ";
        corps += v.getContentDescription().toString() + "\n";
        machine.putExtra("sujet", sujet);
        machine.putExtra("corps", corps);
        startActivity(machine);
    }

    public void btnAutres(View v) {
        sujet = Conf.ENTETE_MAIL;
        corps = getResources().getString(R.string.debut_corps_mail);
        Intent pb_autres = new Intent(PbMenuActivity.this, PbAutresActivity.class);
        sujet += " " + v.getContentDescription().toString() + " ";
        corps += v.getContentDescription().toString() + "\n";
        pb_autres.putExtra("sujet", sujet);
        pb_autres.putExtra("corps", corps);
        startActivity(pb_autres);
    }

    public void btnAppel(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0763972884"));
        startActivity(intent);
    }

    public void btnDeclaration(View view) {
        sujet = Conf.ENTETE_MAIL;
        corps = getResources().getString(R.string.debut_corps_mail);
        Intent fast = new Intent(PbMenuActivity.this, FormulaireInformatiqueActivity.class);
        sujet += " " + view.getContentDescription().toString() + " ";
        corps += view.getContentDescription().toString() + "\n";
        fast.putExtra("sujet", sujet);
        fast.putExtra("corps", corps);
        fast.putExtra("origine", "rapide");
        startActivity(fast);
    }

    public void btnapropos(View view) {
        Intent apropos = new Intent(PbMenuActivity.this, AProposActivity.class);
        startActivity(apropos);
    }
}
