package org.neocampus.alertocampus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.neocampus.alertocampus.R;

public class PbConnexionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String sujet;
    private String corps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pb_connexion);

        Spinner spinner = findViewById(R.id.spinner_connexion);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.liste_pb_connexion, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        RadioGroup radio = findViewById(R.id.radioboutons_connexion);
        TextView texte_liste = findViewById(R.id.texte_pb_connexion_liste);
        EditText edit_liste = findViewById(R.id.edittext_pb_connexion_liste_identifiants);
        TextView texte_incident = findViewById(R.id.texte_pb_connexion_description_incident);
        EditText edit_incident = findViewById(R.id.edittext_pb_connexion_description_incident);
        TextView texte_choix_os = findViewById(R.id.texte_os);
        TextView texte_champs_obligatoires = findViewById(R.id.texte_champs_obligatoires);
        radio.setVisibility(View.GONE);
        texte_liste.setVisibility(View.GONE);
        edit_liste.setVisibility(View.GONE);
        texte_incident.setVisibility(View.GONE);
        edit_incident.setVisibility(View.GONE);
        texte_choix_os.setVisibility(View.GONE);
        texte_champs_obligatoires.setVisibility(View.GONE);

        Button bouton = findViewById(R.id.scanner);
        bouton.setEnabled(false);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        RadioGroup radio = findViewById(R.id.radioboutons_connexion);
        TextView texte_liste = findViewById(R.id.texte_pb_connexion_liste);
        EditText edit_liste = findViewById(R.id.edittext_pb_connexion_liste_identifiants);
        TextView texte_incident = findViewById(R.id.texte_pb_connexion_description_incident);
        EditText edit_incident = findViewById(R.id.edittext_pb_connexion_description_incident);
        TextView texte_choix_os = findViewById(R.id.texte_os);
        TextView texte_champs_obligatoires = findViewById(R.id.texte_champs_obligatoires);
        Button bouton = findViewById(R.id.scanner);

        switch (i) {
            case 0:
                texte_choix_os.setVisibility(View.VISIBLE);
                radio.setVisibility(View.VISIBLE);
                texte_liste.setVisibility(View.GONE);
                edit_liste.setVisibility(View.GONE);
                texte_incident.setVisibility(View.GONE);
                edit_incident.setVisibility(View.GONE);
                texte_champs_obligatoires.setVisibility(View.GONE);
                bouton.setEnabled(true);
                break;
            case 1:
                texte_choix_os.setVisibility(View.GONE);
                radio.setVisibility(View.GONE);
                texte_liste.setVisibility(View.VISIBLE);
                edit_liste.setVisibility(View.VISIBLE);
                texte_incident.setVisibility(View.GONE);
                edit_incident.setVisibility(View.GONE);
                texte_champs_obligatoires.setVisibility(View.VISIBLE);
                bouton.setEnabled(true);
                break;
            case 2:
                texte_choix_os.setVisibility(View.GONE);
                radio.setVisibility(View.GONE);
                texte_liste.setVisibility(View.GONE);
                edit_liste.setVisibility(View.GONE);
                texte_incident.setVisibility(View.VISIBLE);
                edit_incident.setVisibility(View.VISIBLE);
                texte_champs_obligatoires.setVisibility(View.GONE);
                bouton.setEnabled(true);
                break;
            case 3:
                texte_choix_os.setVisibility(View.GONE);
                radio.setVisibility(View.GONE);
                texte_liste.setVisibility(View.GONE);
                edit_liste.setVisibility(View.GONE);
                texte_incident.setVisibility(View.VISIBLE);
                edit_incident.setVisibility(View.VISIBLE);
                texte_champs_obligatoires.setVisibility(View.VISIBLE);
                bouton.setEnabled(true);
                break;
            case 4:
                texte_choix_os.setVisibility(View.GONE);
                radio.setVisibility(View.GONE);
                texte_liste.setVisibility(View.GONE);
                edit_liste.setVisibility(View.GONE);
                texte_incident.setVisibility(View.VISIBLE);
                edit_incident.setVisibility(View.VISIBLE);
                texte_champs_obligatoires.setVisibility(View.VISIBLE);
                bouton.setEnabled(true);
                break;
            default:
                texte_choix_os.setVisibility(View.GONE);
                radio.setVisibility(View.GONE);
                texte_liste.setVisibility(View.GONE);
                edit_liste.setVisibility(View.GONE);
                texte_incident.setVisibility(View.GONE);
                edit_incident.setVisibility(View.GONE);
                texte_champs_obligatoires.setVisibility(View.GONE);
                bouton.setEnabled(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void suivant(View view) {
        RadioGroup radio = findViewById(R.id.radioboutons_connexion);

        if (radio.getVisibility() == View.VISIBLE && radio.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Veuillez sélectionner le systeme d'exploitation lié à l'incident", Toast.LENGTH_LONG).show();
        } else {

            Intent intent = getIntent();
            sujet = intent.getStringExtra("sujet");
            corps = intent.getStringExtra("corps");

            Spinner s = findViewById(R.id.spinner_connexion);
            EditText edit_liste = findViewById(R.id.edittext_pb_connexion_liste_identifiants);
            EditText edit_incident = findViewById(R.id.edittext_pb_connexion_description_incident);


            corps += s.getSelectedItem().toString() + "\n";

            if (radio.getVisibility() == View.VISIBLE) {
                RadioButton radioSelected = findViewById(radio.getCheckedRadioButtonId());
                corps += "OS : " + radioSelected.getText().toString() + "\n";
            } else if (edit_liste.getVisibility() == View.VISIBLE) {
                corps += "Liste des indentifiants : " + edit_liste.getText().toString() + "\n";
            } else {
                corps += "Description de l'incident : " + edit_incident.getText().toString() + "\n";
            }
            Intent formulaire = new Intent(PbConnexionActivity.this, FormulaireInformatiqueActivity.class);
            formulaire.putExtra("sujet", sujet);
            formulaire.putExtra("corps", corps);
            formulaire.putExtra("origine", "connexion");
            startActivity(formulaire);
        }
    }

    public void btnClicked(View v) {
        Button bouton = findViewById(R.id.scanner);

        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.radio1_pb_connexion:
                if (checked) {
                    bouton.setEnabled(true);
                    break;
                }
            case R.id.radio2_pb_connexion:
                if (checked) {
                    bouton.setEnabled(true);
                    break;
                }
            case R.id.radio3_pb_connexion:
                if (checked) {
                    bouton.setEnabled(true);
                    break;
                }
        }
    }

    public void btnapropos(View view) {
        Intent apropos = new Intent(PbConnexionActivity.this, AProposActivity.class);
        startActivity(apropos);
    }

    public void precedent(View view) {
        Intent precedent = new Intent(PbConnexionActivity.this, PbMenuActivity.class);
        startActivity(precedent);
    }
}
