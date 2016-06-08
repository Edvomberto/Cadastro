package br.com.caelum.cadastro.util;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import br.com.caelum.cadastro.mostraAlunosActivity;

/**
 * Created by android6017 on 03/06/16.
 */
public class GPS implements GoogleApiClient.ConnectionCallbacks, LocationListener {
    GoogleApiClient cliente;
    mostraAlunosActivity activity;


    public GPS(mostraAlunosActivity activity) {
        cliente = new GoogleApiClient.Builder(activity)
                     .addApi(LocationServices.API)
                     .addConnectionCallbacks(this)
                     .build();
        this.activity = activity;
        cliente.connect();
    }

    @Override
    public void onConnected(Bundle bundle)
    {

        LocationRequest request = LocationRequest.create()
                                 .setInterval(3000)
                                 .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                 .setSmallestDisplacement(10);
        LocationServices.FusedLocationApi.requestLocationUpdates(cliente, request,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(Location location) {
        activity.centralizaNo(new LatLng(location.getLatitude(),location.getLongitude()));

    }
}
