package org.neocampus.alertocampus.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.neocampus.alertocampus.R;
import org.neocampus.alertocampus.control.Conf;
import org.neocampus.alertocampus.control.SessionManager;
import org.osmdroid.config.Configuration;

import java.util.HashMap;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private SessionManager sessionManager;
    private ProgressBar chargement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //on demande les permissions
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CAMERA, Manifest.permission.VIBRATE},
                10);
        this.sessionManager = new SessionManager(getApplicationContext());
        chargement = findViewById(R.id.progress_bar_main);
        chargement.setVisibility(View.GONE);
        checkConnexion();
        Spinner spinner = findViewById(R.id.spinner_main);
        spinner.setOnItemSelectedListener(this);
        TextView t2 = findViewById(R.id.attention);
        t2.setVisibility(View.GONE);
    }


    private void checkConnexion() {
        if (sessionManager.isLoggedIn()) {
            Intent menu = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(menu);
            this.finish();
        }
    }

    public void btnConnexion(View v) {
        EditText editLogin = findViewById(R.id.login_main);
        EditText editPass = findViewById(R.id.pass_main);
        EditText editMail = findViewById(R.id.mail_univ);
        final String login = editLogin.getText().toString();
        final String pass = editPass.getText().toString();
        final String mail = editMail.getText().toString();
        if (login.equals("") || pass.equals("") || mail.equals("")) {
            Toast.makeText(this, "Veuillez entrer votre login, votre mot de passe et le debut de votre adresse mail universitaire", Toast.LENGTH_LONG).show();
        } else {
            threadConnexion();
        }
    }

    private void threadConnexion() {
        final ProgressBar chargement = findViewById(R.id.progress_bar_main);
        final LinearLayout backLayout = findViewById(R.id.grey_layout);
        backLayout.setVisibility(View.VISIBLE);
        chargement.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {

                final int connexionOk = connexionOk();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        backLayout.setVisibility(View.GONE);
                        chargement.setVisibility(View.GONE);
                        switch (connexionOk) {
                            case Conf.connexionOK:
                                //new FetchTask().execute("https://auth.test.univ-toulouse.fr/cas_login_unr/");
                                Intent menu = new Intent(MainActivity.this, MenuActivity.class);
                                startActivity(menu);
                                MainActivity.this.finish();
                                break;
                            case Conf.connexionLogin:
                                Toast.makeText(MainActivity.this, "Identifiant ou mot de passe érroné", Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                Toast.makeText(MainActivity.this, "Probleme d'acces au serveur, êtes vous bien connecté à internet ?", Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }
                });
            }
        }).start();
    }

    private int connexionOk() {
        EditText editLogin = findViewById(R.id.login_main);
        EditText editPass = findViewById(R.id.pass_main);
        Spinner spinner = findViewById(R.id.spinner_main);
        final String login = spinner.getSelectedItem().toString() + "." + editLogin.getText().toString().replace(" ", "");
        final String pass = editPass.getText().toString();
        int port = Conf.PORT_SERVEUR;
        String host = Conf.SMTP_SERVEUR;
        Properties props = new Properties();
        props.put(getString(R.string.propHost), host); //SMTP Host
        props.put(getString(R.string.propProtocol), getString(R.string.protocolName)); //SMTP Host
        props.put(getString(R.string.propPort), port); //TLS Port
        props.put(getString(R.string.propAut), true); //enable authentication
        props.put(getString(R.string.propStarttls), true); //enable STARTTLS
        props.put(getString(R.string.propTimeout), "1000");
        props.put(getString(R.string.propCoTimeout), "1000");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(login, pass);
                    }
                });

        Transport transport = null;
        try {
            transport = session.getTransport();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return Conf.connexionNOK;
        }
        try {
            if (transport != null) {
                transport.connect(host, port, login, pass);
            } else {
                return Conf.connexionNOK;
            }
        } catch (javax.mail.AuthenticationFailedException e) {
            e.printStackTrace();
            return Conf.connexionLogin;
        } catch (MessagingException e) {
            e.printStackTrace();
            return Conf.connexionNOK;
        }

        try {
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
            return Conf.connexionNOK;
        }
        CheckBox checkBox = findViewById(R.id.checkbox_main);
        EditText mail = findViewById(R.id.mail_univ);
        if (checkBox.isChecked()) {
            sessionManager.createKeepLoginSession(login, pass, mail.getText().toString().replace(" ", "") + getString(R.string.texte_fin_mail));
        } else {
            sessionManager.createNoKeepLoginSession(login, pass, mail.getText().toString().replace(" ", "") + getString(R.string.texte_fin_mail));
        }
        return Conf.connexionOK;
    }

    public void btnUrgence(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0561557322"));
        startActivity(intent);
    }

    public void btnapropos(View view) {
        Intent apropos = new Intent(MainActivity.this, AProposActivity.class);
        startActivity(apropos);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView t1 = findViewById(R.id.tlse3);
        TextView t2 = findViewById(R.id.attention);
        if (i != 0) {
            t1.setVisibility(View.GONE);
            t2.setVisibility(View.VISIBLE);
        } else {
            t1.setVisibility(View.VISIBLE);
            t2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    /*private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }


        return result.toString();
    }

    private class FetchTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            InputStream inputStream = null;
            HttpURLConnection conn = null;
            StringBuffer response = new StringBuffer();



            String stringUrl = strings[0];
            try {
                URL url = new URL(stringUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                Log.e("connexion",MainActivity.this.params.get("username"));
                writer.write(getPostDataString(MainActivity.this.params));

                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();

                inputStream = conn.getInputStream();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                        response.append("\n");
                    }
                }
                conn.connect();
                return new String(response);


            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) {
                Log.e("connexion","Erreur");
            } else {
                Log.e("connexion",s);
            }
        }


    }*/
}
