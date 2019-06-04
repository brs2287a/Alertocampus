package org.neocampus.alertocampus.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import org.neocampus.alertocampus.R;
import org.neocampus.alertocampus.control.Conf;
import org.neocampus.alertocampus.control.EnvoiTicket;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class LocalisationActivity extends AppCompatActivity {

    private MapView map = null;
    private MyLocationNewOverlay mLocationOverlay;

    private String typePb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(18.0);

        GeoPoint startPoint = new GeoPoint(43.5612616, 1.4632823);
        mapController.setCenter(startPoint);

        this.map.setMultiTouchControls(true);
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        this.mLocationOverlay.enableMyLocation();

        map.getOverlays().add(this.mLocationOverlay);

        Intent intent = getIntent();
        typePb = intent.getStringExtra("typePb");

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    public void btnEnvoyer(View v) {
        Intent local = getIntent();
        String sujet = local.getStringExtra("sujet");
        String corps = local.getStringExtra("corps");
        byte[] photo = local.getByteArrayExtra("photo");
        String destinataire;
        if (typePb.equals(getString(R.string.desc_air))) {
            destinataire = Conf.DESTINATAIRE_AIR;
        } else if (typePb.equals(getString(R.string.desc_eau))) {
            destinataire = Conf.DESTINATAIRE_EAU;
        } else if (typePb.equals(getString(R.string.desc_lumiere))) {
            destinataire = Conf.DESTINATAIRE_LUMIERE;
        } else {
            destinataire = Conf.DESTINATAIRE_FENETRE;
        }
        IGeoPoint pos = map.getMapCenter();
        corps += "\nLongitude=" + pos.getLongitude() + "\n Latitude=" + pos.getLatitude();
        EnvoiTicket envoiTicket = new EnvoiTicket(sujet, corps, photo, this, new Handler(), destinataire);

        envoiTicket.envoyer();

    }

    public void btnMyLocation(View view) {
        this.mLocationOverlay.enableFollowLocation();
    }

}
