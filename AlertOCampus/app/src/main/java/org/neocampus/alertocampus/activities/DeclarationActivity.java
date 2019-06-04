package org.neocampus.alertocampus.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.neocampus.alertocampus.R;
import org.neocampus.alertocampus.control.Conf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeclarationActivity extends AppCompatActivity {


    private String currentPhotoPath;
    private byte[] compressedPhoto;
    private String sujet;
    private String corps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declaration);
    }

    public void scanner(View v) {
        startActivityForResult(new Intent(DeclarationActivity.this, QrCodeActivity.class), Conf.requestCodeQrCode);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Conf.requestCodePicture && resultCode == RESULT_OK) {
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
        textView.setVisibility(View.VISIBLE);
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

        TextView batiment = findViewById(R.id.formulaire_batimnent);
        TextView salle = findViewById(R.id.formulaire_salle);
        String desc = ((EditText) findViewById(R.id.edittext_pb_connexion_description_incident)).getText().toString();


        if (batiment.getText().length() == 0 || salle.getText().length() == 0) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
        } else {
            String bat = batiment.getText().toString();
            String sal = salle.getText().toString();
            sujet = Conf.ENTETE_MAIL;

            corps = getResources().getString(R.string.debut_corps_mail);
            sujet += getIntent().getStringExtra("typePb") + " " + bat + " " + sal;
            corps += "Description de l'incident : " + desc + "\n";
            corps += "\nBatiment : " + bat + "\nSalle : " + sal;

            Intent map = new Intent(DeclarationActivity.this, LocalisationActivity.class);
            map.putExtra("sujet", sujet);
            map.putExtra("corps", corps);
            map.putExtra("photo", compressedPhoto);
            map.putExtra("typePb", getIntent().getStringExtra("typePb"));
            startActivity(map);


        }
    }


    public void btnapropos(View view) {
        Intent apropos = new Intent(DeclarationActivity.this, AProposActivity.class);
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
