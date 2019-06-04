package org.neocampus.alertocampus.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.neocampus.alertocampus.R;
import org.neocampus.alertocampus.control.Conf;

public class SoftwareActivity extends AppCompatActivity {

    private String sujet;
    private String corps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (Conf.requestCodeQrCode == requestCode) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, intent.getStringExtra("retour"), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void btnAppel(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0561557322"));
        startActivity(intent);
    }

    public void btnapropos(View view) {
        Intent apropos = new Intent(SoftwareActivity.this, AProposActivity.class);
        startActivity(apropos);
    }

    public void suivant(View view) {

        Intent intent = getIntent();
        sujet = intent.getStringExtra("sujet");
        corps = intent.getStringExtra("corps");

        RadioGroup radioGroup = findViewById(R.id.radioGroup_os_software);
        EditText editLogiciel = findViewById(R.id.edittext_nomLogiciel_software);
        EditText editDesc = findViewById(R.id.edittext_desc_software);
        String logiciel = editLogiciel.getText().toString();
        String desc = editDesc.getText().toString();
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Veuillez sélectionner le système d'exploitation", Toast.LENGTH_SHORT).show();
        } else if (desc.equals("") || logiciel.equals("")) {
            Toast.makeText(this, "Veuillez saisir le nom du logiciel en question et une description du problème rencontré", Toast.LENGTH_SHORT).show();
        } else {
            Intent formulaire = new Intent(SoftwareActivity.this, FormulaireInformatiqueActivity.class);
            RadioButton radioSelected = findViewById(radioGroup.getCheckedRadioButtonId());

            corps += "OS : " + radioSelected.getText().toString() + "\n" + "Logiciel : " + logiciel + "\n" + "Description du problème : " + desc + "\n";
            formulaire.putExtra("sujet", sujet);
            formulaire.putExtra("corps", corps);

            //formulaire.putExtra("os",radioSelected.getText().toString());
            //formulaire.putExtra("logiciel",logiciel);
            //formulaire.putExtra("desc",desc);
            startActivity(formulaire);
        }

    }

    public void precedent(View view) {
        Intent precedent = new Intent(SoftwareActivity.this, PbMenuActivity.class);
        startActivity(precedent);
    }
}
