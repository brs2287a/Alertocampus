package org.neocampus.alertocampus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.neocampus.alertocampus.R;


public class PbAutresActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String sujet;
    private String corps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pb_autres);

        Spinner spinner = findViewById(R.id.spinner_autres_incidents);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.liste_pb_autres_incidents, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        TextView texte_description = findViewById(R.id.texte_autres_incidents_description);
        EditText edit_texte_description = findViewById(R.id.edittext_autres_incidents_description);
        TextView texte_champs_obligatoires = findViewById(R.id.texte_champs_obligatoires);
        texte_description.setVisibility(View.GONE);
        edit_texte_description.setVisibility(View.GONE);
        texte_champs_obligatoires.setVisibility(View.GONE);

        Button bouton = findViewById(R.id.scanner);
        bouton.setEnabled(false);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView texte_description = findViewById(R.id.texte_autres_incidents_description);
        EditText edit_texte_description = findViewById(R.id.edittext_autres_incidents_description);
        TextView texte_champs_obligatoires = findViewById(R.id.texte_champs_obligatoires);
        Button bouton = findViewById(R.id.scanner);

        switch (i) {
            case 0:
                texte_description.setVisibility(View.VISIBLE);
                edit_texte_description.setVisibility(View.VISIBLE);
                texte_champs_obligatoires.setVisibility(View.VISIBLE);
                bouton.setEnabled(true);
                break;
            case 1:
                texte_description.setVisibility(View.VISIBLE);
                edit_texte_description.setVisibility(View.VISIBLE);
                texte_champs_obligatoires.setVisibility(View.VISIBLE);
                bouton.setEnabled(true);
                break;
            case 2:
                texte_description.setVisibility(View.VISIBLE);
                edit_texte_description.setVisibility(View.VISIBLE);
                texte_champs_obligatoires.setVisibility(View.VISIBLE);
                bouton.setEnabled(true);
                break;
            case 3:
                texte_description.setVisibility(View.VISIBLE);
                edit_texte_description.setVisibility(View.VISIBLE);
                texte_champs_obligatoires.setVisibility(View.VISIBLE);
                bouton.setEnabled(true);
                break;
            default:
                texte_description.setVisibility(View.GONE);
                edit_texte_description.setVisibility(View.GONE);
                texte_champs_obligatoires.setVisibility(View.GONE);
                bouton.setEnabled(false);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void suivant(View v) {
        Intent intent = getIntent();
        sujet = intent.getStringExtra("sujet");
        corps = intent.getStringExtra("corps");
        Spinner s = findViewById(R.id.spinner_autres_incidents);
        EditText edit_texte_description = findViewById(R.id.edittext_autres_incidents_description);
        Intent fast = new Intent(PbAutresActivity.this, FormulaireInformatiqueActivity.class);
        corps += s.getSelectedItem().toString() + "\n" + "Description de l'incident : " + edit_texte_description.getText().toString() + "\n";
        fast.putExtra("sujet", sujet);
        fast.putExtra("corps", corps);
        //fast.putExtra("fastMode",true);
        startActivity(fast);
    }

    public void btnapropos(View view) {
        Intent apropos = new Intent(PbAutresActivity.this, AProposActivity.class);
        startActivity(apropos);
    }

    public void precedent(View view) {
        Intent precedent = new Intent(PbAutresActivity.this, PbMenuActivity.class);
        startActivity(precedent);
    }
}
