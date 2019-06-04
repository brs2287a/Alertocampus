package org.neocampus.alertocampus.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.neocampus.alertocampus.R;
import org.neocampus.alertocampus.control.Conf;
import org.neocampus.alertocampus.control.EnvoiTicket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormulaireInformatiqueActivity extends AppCompatActivity {

    private String currentPhotoPath;
    private byte[] compressedPhoto;
    private String sujet;
    private String corps;
    private LinearLayout rapide_layout;
    private LinearLayout manuel_layout;
    private LinearLayout reste_layout;
    private TextView etoile_layout;
    private TextView retour_scan;
    private EnvoiTicket envoiTicket;
    private boolean rapide = false;
    private String bat;
    private String salle;
    private String machine;
    private String machineSpecial;
    private boolean qrCode = false;
    private boolean qrCodeSpecial = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_informatique);

        Spinner spinner = findViewById(R.id.spinner_declaration_rapide);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.liste_declaration_rapide, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        rapide_layout = findViewById(R.id.layout_declaration_rapide);
        retour_scan = findViewById(R.id.retour_scan);
        manuel_layout = findViewById(R.id.layout_manuel);
        etoile_layout = findViewById(R.id.texte_champs_obligatoires);
        reste_layout = findViewById(R.id.layout_fin_formulaire);


        Intent intent = getIntent();
        String origine = intent.getStringExtra("origine");
        if (origine != null) {
            switch (origine) {
                case "connexion":
                    findViewById(R.id.layout_connexion).setVisibility(View.VISIBLE);
                    break;
                case "rapide":
                    rapide = true;
                    findViewById(R.id.head_rapide).setVisibility(View.VISIBLE);

                    break;
                case "pas_info":
                    findViewById(R.id.pbinfo_formulaire).setVisibility(View.GONE);
                    break;

                default:
                    break;
            }
        }
    }

    public void scanner(View v) {
        manuel_layout.setVisibility(View.GONE);
        reste_layout.setVisibility(View.GONE);
        etoile_layout.setVisibility(View.GONE);
        retour_scan.setVisibility(View.GONE);
        if (rapide)
            rapide_layout.setVisibility(View.GONE);
        startActivityForResult(new Intent(FormulaireInformatiqueActivity.this, QrCodeActivity.class), Conf.requestCodeQrCode);
    }

    public void btnManuel(View view) {
        qrCode = false;
        qrCodeSpecial = false;
        manuel_layout.setVisibility(View.VISIBLE);
        reste_layout.setVisibility(View.VISIBLE);
        etoile_layout.setVisibility(View.VISIBLE);
        retour_scan.setVisibility(View.GONE);
        if (rapide)
            rapide_layout.setVisibility(View.VISIBLE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Conf.requestCodeQrCode == requestCode) {

            if (resultCode == RESULT_OK) {
                Pattern p = Pattern.compile(".*/\\?([a-zA-Z_0-9\\-]*)-([a-zA-Z_0-9\\-]*)-([a-zA-Z_0-9\\-]*)");
                Matcher m = p.matcher(data.getStringExtra("retour"));
                if (m.matches()) {
                    qrCode = true;
                    qrCodeSpecial = false;
                    bat = m.group(1);
                    salle = m.group(2);
                    machine = m.group(3);
                    retour_scan.setText("Résultat scan :\nBatiment :" + bat + " Salle :" + salle + " machine :" + machine);
                } else {
                    Pattern p2 = Pattern.compile(".*/\\?(.*)");
                    Matcher m2 = p2.matcher(data.getStringExtra("retour"));
                    if (m2.matches()) {
                        qrCode = true;
                        qrCodeSpecial = true;
                        machineSpecial = m2.group(1);
                        retour_scan.setText("Résultat scan :\n" + m2.group(1));
                    } else {
                        retour_scan.setText("Problème format texte QrCode");
                        retour_scan.setVisibility(View.VISIBLE);
                        return;
                    }
                }

                retour_scan.setVisibility(View.VISIBLE);
                reste_layout.setVisibility(View.VISIBLE);

                if (rapide) {
                    rapide_layout.setVisibility(View.VISIBLE);
                    etoile_layout.setVisibility(View.VISIBLE);
                }
            }
        } else if (requestCode == Conf.requestCodePicture && resultCode == RESULT_OK) {
            setPic();
        }
    }

    private void setPic() {
        ImageView imageView = findViewById(R.id.picture_taken);
        TextView textView = findViewById(R.id.size_photo);

        Bitmap bitmap = getResizedBitmap(848);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        compressedPhoto = stream.toByteArray();
        textView.setText("Poids de l'image à envoyer :" + (compressedPhoto.length / 1000) + "Ko");
        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);
        findViewById(R.id.deleteImg).setVisibility(View.VISIBLE);
        TextView textViewPhoto = findViewById(R.id.text_photo);
        textViewPhoto.setText("Reprendre photo");
    }

    public Bitmap getResizedBitmap(int maxWidth) {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxWidth;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxWidth;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public void envoi(View view) {

        Intent intent = getIntent();
        sujet = intent.getStringExtra("sujet");
        corps = intent.getStringExtra("corps");

        TextView txtViewBatiment = findViewById(R.id.formulaire_batimnent);
        TextView txtViewSalle = findViewById(R.id.formulaire_salle);
        TextView numMachine = findViewById(R.id.formulaire_machine);
        String desc = ((EditText) findViewById(R.id.edittext_pb_connexion_description_incident)).getText().toString();
        Spinner spinner = findViewById(R.id.spinner_declaration_rapide);

        if (!qrCode) {
            if (txtViewBatiment.getText().length() == 0 || txtViewSalle.getText().length() == 0) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
                return;
            } else {
                String bat = txtViewBatiment.getText().toString();
                String sal = txtViewSalle.getText().toString();
                String num = numMachine.getText().toString();

                sujet += bat + " " + sal + " " + num;
                if (!desc.isEmpty()) corps += "Description de l'incident : " + desc + "\n";
                corps += "\nBatiment : " + bat + "\nSalle : " + sal;
                if (!num.isEmpty()) corps += "\nMachine : " + num;
                if (findViewById(R.id.layout_declaration_rapide).getVisibility() == View.VISIBLE)
                    corps += "\nType probleme :" + spinner.getSelectedItem().toString();

            }
        } else {
            if (!qrCodeSpecial) {
                sujet += bat + " " + salle + " " + machine;
                if (!desc.isEmpty()) corps += "Description de l'incident : " + desc + "\n";
                corps += "\nBatiment : " + bat + "\nSalle : " + salle;
                corps += "\nMachine : " + machine;
                if (rapide)
                    corps += "\nType probleme :" + spinner.getSelectedItem().toString();
            } else {
                sujet += machineSpecial;
                if (!desc.isEmpty()) corps += "Description de l'incident : " + desc + "\n";
                corps += "\nMachine : " + machineSpecial;
                if (rapide)
                    corps += "\nType probleme :" + spinner.getSelectedItem().toString();
            }
        }

        final Handler handler = new Handler();

        envoiTicket = new EnvoiTicket(sujet, corps, this.compressedPhoto, this, handler, Conf.DESTINATAIRE_INFO, Conf.DESTINATAIRE_INFO_DOUBLON, Conf.DESTINATAIRE_INFO_TRIPLON);
        envoiTicket.envoyer();
    }

    public void btnapropos(View view) {
        Intent apropos = new Intent(FormulaireInformatiqueActivity.this, AProposActivity.class);
        startActivity(apropos);
    }

    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this.getApplicationContext(), R.string.toast_pb_save_picture, Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "org.neocampus.alertocampus.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Conf.requestCodePicture);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void deletePicture(View view) {
        compressedPhoto = null;
        findViewById(R.id.picture_taken).setVisibility(View.GONE);
        findViewById(R.id.deleteImg).setVisibility(View.GONE);
        findViewById(R.id.size_photo).setVisibility(View.GONE);
    }


}