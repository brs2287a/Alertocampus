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


public class MaterielActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int requestCodeQrCode = 10;
    private String sujet;
    private String corps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materiel);

        Spinner spinner = findViewById(R.id.spinner_materiel);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.liste_pb_materiel, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        RadioGroup radio = findViewById(R.id.radioboutons);
        radio.setVisibility(View.GONE);

        Button bouton = findViewById(R.id.scanner);
        bouton.setEnabled(true);

        EditText edit_texte = findViewById(R.id.edittext_pb_materiel);
        edit_texte.setVisibility(View.GONE);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        RadioGroup radio = findViewById(R.id.radioboutons);
        radio.setVisibility(View.VISIBLE);
        radio.clearCheck();
        TextView champs_obligatoires = findViewById(R.id.texte_champs_obligatoires);
        champs_obligatoires.setText("");
        TextView texte_pb_materiel = findViewById(R.id.texte_pb_materiel);
        texte_pb_materiel.setText("");
        EditText edit_texte = findViewById(R.id.edittext_pb_materiel);
        edit_texte.setVisibility(View.GONE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void btnClicked(View v) {
        TextView texte = findViewById(R.id.texte_pb_materiel);
        TextView champs_obligatoires = findViewById(R.id.texte_champs_obligatoires);
        Button bouton = findViewById(R.id.scanner);
        EditText edit_texte = findViewById(R.id.edittext_pb_materiel);
        Spinner spin = findViewById(R.id.spinner_materiel);
        Integer pos = spin.getSelectedItemPosition();

        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.radio1_pb_materiel:
                if (checked) {
                    bouton.setEnabled(true);
                    texte.setText("");
                    champs_obligatoires.setText("");
                    edit_texte.setVisibility(View.GONE);
                    break;
                }
            case R.id.radio2_pb_materiel:
                if (checked) {
                    champs_obligatoires.setText("");
                    bouton.setEnabled(true);
                    if (pos == 4) {
                        texte.setText("");
                    } else {
                        texte.setText(getString(R.string.solution_defectueux_materiel));
                    }
                    edit_texte.setVisibility(View.GONE);
                    break;
                }
            case R.id.radio3_pb_materiel:
                if (checked) {
                    bouton.setEnabled(true);
                    texte.setText(getString(R.string.solution_autre_materiel));
                    champs_obligatoires.setText((getString(R.string.champs_obligatoires)));
                    edit_texte.setVisibility(View.VISIBLE);
                    break;
                }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (MaterielActivity.requestCodeQrCode == requestCode) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, intent.getStringExtra("retour"), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void btnapropos(View view) {
        Intent apropos = new Intent(MaterielActivity.this, AProposActivity.class);
        startActivity(apropos);
    }

    public void suivant(View view) {
        Intent intent = getIntent();
        sujet = intent.getStringExtra("sujet");
        corps = intent.getStringExtra("corps");
        Spinner s = findViewById(R.id.spinner_materiel);
        RadioGroup radio = findViewById(R.id.radioboutons);
        EditText edit_texte = findViewById(R.id.edittext_pb_materiel);
        RadioButton radioSelected = findViewById(radio.getCheckedRadioButtonId());

        if (radio.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Veuillez sélectionner la nature de votre problème", Toast.LENGTH_LONG).show();
        } else {

            if (radio.getCheckedRadioButtonId() == R.id.radio3_pb_materiel) {
                if (edit_texte.getText().length() == 0) {
                    Toast.makeText(this, "Veuillez saisir la nature de votre problème", Toast.LENGTH_LONG).show();
                } else {
                    Intent formulaire = new Intent(MaterielActivity.this, FormulaireInformatiqueActivity.class);
                    corps += "Composant : " + s.getSelectedItem().toString() + "\n" +
                            "Problème : " + radioSelected.getText().toString() +
                            "Description du problème : " + edit_texte.getText().toString() + "\n";
                    formulaire.putExtra("sujet", sujet);
                    formulaire.putExtra("corps", corps);
                    startActivity(formulaire);
                }
            } else {

                Intent formulaire = new Intent(MaterielActivity.this, FormulaireInformatiqueActivity.class);
                corps += "Composant : " + s.getSelectedItem().toString() + "\n" +
                        "Problème : " + radioSelected.getText().toString() + "\n";
                formulaire.putExtra("sujet", sujet);
                formulaire.putExtra("corps", corps);
                startActivity(formulaire);
            }

        }
    }

    public void precedent(View view) {
        Intent precedent = new Intent(MaterielActivity.this, PbMenuActivity.class);
        startActivity(precedent);
    }
}