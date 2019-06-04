package org.neocampus.alertocampus.control;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.sun.mail.smtp.SMTPSendFailedException;

import org.neocampus.alertocampus.R;
import org.neocampus.alertocampus.activities.EchecActivity;
import org.neocampus.alertocampus.activities.ThankActivity;

import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;


public class EnvoiTicket {
    private Session mySession;
    private SessionManager sessionManager;
    private String corps;
    private byte[] photo;
    private String sujet;
    private AppCompatActivity ctx;
    private Handler handler;
    private LinearLayout back_layout;
    private ProgressBar chargement;
    private String destinataire;
    private String destinataire2;
    private String destinataire3;


    public EnvoiTicket(String sujet, String corps, byte[] photo, AppCompatActivity ctx, Handler handler, String destinataire) {
        this.corps = corps;
        this.photo = photo;
        this.sujet = sujet;
        this.ctx = ctx;
        this.sessionManager = new SessionManager(ctx.getApplicationContext());
        this.mySession = initialisationSession();
        this.handler = handler;
        this.back_layout = ctx.findViewById(R.id.grey_layout_formulaire);
        this.chargement = ctx.findViewById(R.id.progress_bar_formulaire);
        this.destinataire = destinataire;
    }

    public EnvoiTicket(String sujet, String corps, byte[] photo, AppCompatActivity ctx, Handler handler, String destinataire, String destinataire2, String destinataire3) {
        this.corps = corps;
        this.photo = photo;
        this.sujet = sujet;
        this.ctx = ctx;
        this.sessionManager = new SessionManager(ctx.getApplicationContext());
        this.mySession = initialisationSession();
        this.handler = handler;
        this.back_layout = ctx.findViewById(R.id.grey_layout_formulaire);
        this.chargement = ctx.findViewById(R.id.progress_bar_formulaire);
        this.destinataire = destinataire;
        this.destinataire2 = destinataire2;
        this.destinataire3 = destinataire3;
    }


    public void envoyer() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        back_layout.setVisibility(View.VISIBLE);
                        chargement.setVisibility(View.VISIBLE);
                    }
                });
                MimeMessage mimeMessage = null;
                try {
                    mimeMessage = initialisationMessage(mySession, destinataire);
                } catch (MessagingException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            back_layout.setVisibility(View.GONE);
                            chargement.setVisibility(View.GONE);
                            envoiNOK();
                        }
                    });

                    e.printStackTrace();
                } catch (IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            back_layout.setVisibility(View.GONE);
                            chargement.setVisibility(View.GONE);
                            envoiNOK();
                        }
                    });
                    e.printStackTrace();
                }

                sendMessageThread(mySession, mimeMessage);
            }
        });
        t.start();

    }

    private void sendMessageThread(final Session mSession, final MimeMessage email) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Transport transport = null;
                try {
                    transport = mSession.getTransport();
                    transport.connect(Conf.SMTP_SERVEUR, EnvoiTicket.this.sessionManager.getLogin(), EnvoiTicket.this.sessionManager.getKeyPass());
                    transport.sendMessage(email, new Address[]{new InternetAddress(destinataire)});
                    if (destinataire2 != null)
                        transport.sendMessage(email, new Address[]{new InternetAddress(destinataire2)});
                    if (destinataire3 != null)
                        transport.sendMessage(email, new Address[]{new InternetAddress(destinataire3)});
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            back_layout.setVisibility(View.GONE);
                            chargement.setVisibility(View.GONE);
                            envoiOK();
                        }
                    });

                } catch (SMTPSendFailedException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            back_layout.setVisibility(View.GONE);
                            chargement.setVisibility(View.GONE);
                            envoiNOKMail();
                        }
                    });
                } catch (MessagingException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            back_layout.setVisibility(View.GONE);
                            chargement.setVisibility(View.GONE);
                            envoiNOK();
                        }
                    });

                } finally {
                    try {
                        if (transport != null) {
                            transport.close();
                        }
                    } catch (MessagingException e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                back_layout.setVisibility(View.GONE);
                                chargement.setVisibility(View.GONE);
                                envoiNOK();
                            }
                        });
                        e.printStackTrace();

                    }
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void envoiNOKMail() {
        Context ctxGlobal = ctx.getBaseContext();
        Intent echec = new Intent(ctxGlobal, EchecActivity.class);
        echec.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Closing all the Activities
        echec.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Add new Flag to start new Activity
        echec.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        echec.putExtra("corps", corps);
        echec.putExtra("sujet", sujet);
        echec.putExtra("photo", photo);
        echec.putExtra("mail", true);
        echec.putExtra("dest", destinataire);
        ctxGlobal.startActivity(echec);
    }

    private void envoiOK() {
        Context ctxGlobal = ctx.getBaseContext();
        Intent i = new Intent(ctxGlobal, ThankActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Add new Flag to start new Activity
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        if (destinataire.equals(Conf.DESTINATAIRE_INFO))
            i.putExtra("fromInfo", true);

        // Staring Login Activity
        ctxGlobal.startActivity(i);
    }

    private void envoiNOK() {
        Context ctxGlobal = ctx.getBaseContext();
        Intent echec = new Intent(ctxGlobal, EchecActivity.class);
        echec.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Closing all the Activities
        echec.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Add new Flag to start new Activity
        echec.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        echec.putExtra("corps", corps);
        echec.putExtra("sujet", sujet);
        echec.putExtra("photo", photo);
        echec.putExtra("dest", destinataire);
        ctxGlobal.startActivity(echec);
    }

    private Session initialisationSession() {
        Properties properties = new Properties();
        properties.setProperty(ctx.getString(R.string.propProtocol), ctx.getString(R.string.protocolName));
        properties.setProperty(ctx.getString(R.string.propHost), Conf.SMTP_SERVEUR);
        properties.setProperty(ctx.getString(R.string.propUser), this.sessionManager.getLogin());
        return Session.getInstance(properties);
    }

    private MimeMessage initialisationMessage(Session mSession, String mDestinataire) throws MessagingException, IOException {

        MimeMessage email = new MimeMessage(mSession);
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(corps, ctx.getString(R.string.encodage));
        Multipart mMultipart = new MimeMultipart();
        mMultipart.addBodyPart(messageBodyPart);
        if (photo != null) {
            addAttachement(mMultipart);
        }
        email.setContent(mMultipart);
        email.setSubject(sujet, ctx.getString(R.string.encodage));
        email.setFrom(new InternetAddress(this.sessionManager.getKeyMail()));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(mDestinataire));
        return email;
    }

    private void addAttachement(Multipart mMultipart) throws MessagingException {
        MimeBodyPart imageBodyPart = new MimeBodyPart();
        DataSource fds = new ByteArrayDataSource(photo, ctx.getString(R.string.typeImg));
        imageBodyPart.setDataHandler(new DataHandler(fds));
        imageBodyPart.setFileName("photo.jpeg");
        mMultipart.addBodyPart(imageBodyPart);
    }
}
