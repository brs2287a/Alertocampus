package org.neocampus.alertocampus.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.neocampus.alertocampus.R;
import org.neocampus.alertocampus.control.Conf;
import org.neocampus.alertocampus.control.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MenuActivity extends AppCompatActivity {
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.sessionManager = new SessionManager(getApplicationContext());
        if (!Conf.fuiteEau) {
            findViewById(R.id.eau).setVisibility(View.GONE);
        }

        if (!Conf.fuiteAir) {
            findViewById(R.id.air).setVisibility(View.GONE);
        }

        if (!Conf.lumiere) {
            findViewById(R.id.lumiere).setVisibility(View.GONE);
        }

        if (!Conf.fenetre) {
            findViewById(R.id.fenetre).setVisibility(View.GONE);
        }

        if (!Conf.informatique) {
            findViewById(R.id.informatique).setVisibility(View.GONE);
        }

        if (!Conf.autres) {
            findViewById(R.id.autre).setVisibility(View.GONE);
        }
    }

    public void btnDeclaration(View v) {
        if (v.getContentDescription().toString().equals("Lumière allumée") || v.getContentDescription().toString().equals("Fenêtre ouverte")) {
            SimpleDateFormat heure = new SimpleDateFormat("HH");
            Calendar c = Calendar.getInstance();
            String heure1 = heure.format(c.getTime());
            Integer i = Integer.parseInt(heure1);
            if (i >= 20 || i <= 6) {
                Intent declaration = new Intent(MenuActivity.this, DeclarationActivity.class);
                declaration.putExtra("typePb", v.getContentDescription());
                startActivity(declaration);
            } else {
                Toast.makeText(this, "Déclaration possible uniquement entre 20h et 6h", Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent declaration = new Intent(MenuActivity.this, DeclarationActivity.class);
            declaration.putExtra("typePb", v.getContentDescription());
            startActivity(declaration);
        }
    }

    public void btnMachine(View v) {

        Intent pb_materiel = new Intent(MenuActivity.this, PbMenuActivity.class);
        pb_materiel.putExtra("typePb", v.getContentDescription());
        startActivity(pb_materiel);
    }

    public void btnDeconnexion(View v) {
        sessionManager.logout();
    }

    public void btnUrgence(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0561557322"));
        startActivity(intent);
    }

    public void btnapropos(View view) {
        Intent apropos = new Intent(MenuActivity.this, AProposActivity.class);
        startActivity(apropos);
    }

    public void btnAutre(View view) {
        Intent autre = new Intent(MenuActivity.this, AutreActivity.class);
        startActivity(autre);
    }
}
