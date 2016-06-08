package br.com.caelum.cadastro.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by android6017 on 03/06/16.
 */
public class Localizador {
    private Geocoder geo;


    public Localizador(Context ctx) {
        this.geo = new Geocoder(ctx);
    }

    public LatLng getCoordenada(String endereco)
    {
        try {
            List<Address> ends = geo.getFromLocationName(endereco,1);

            if(!ends.isEmpty())
            {
                Address end = ends.get(0);
                return new LatLng(end.getLatitude(), end.getLongitude());
            }
            else return null;

        }
        catch (IOException e)
        {
            return null;
        }


    }
}
