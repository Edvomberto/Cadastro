package br.com.caelum.cadastro;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.model.Aluno;
import br.com.caelum.cadastro.util.GPS;
import br.com.caelum.cadastro.util.Localizador;

public class mostraAlunosActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    MarkerOptions markerOptions;
    LatLng latLng;
    public LatLng latLngAtual;

    GoogleApiClient cliente;
    private Location mCurrentLocation;

    private final int[] MAP_TYPES = { GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE };
    private int curMapTypeIndex = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_alunos);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //mapFragment.getMap().animateCamera( CameraUpdateFactory.newCameraPosition( position ), null );

        mapFragment.getMap().setMapType( MAP_TYPES[curMapTypeIndex] );
        //mapFragment.getMap().setTrafficEnabled( true );
        mapFragment.getMap().setMyLocationEnabled( true );
        mapFragment.getMap().getUiSettings().setZoomControlsEnabled( true );





        Button btn_find = (Button) findViewById(R.id.btn_find);

        // Defining button click event listener for the find button

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting reference to EditText to get the user input location
                EditText etLocation = (EditText) findViewById(R.id.et_location);

                // Getting user input location
                String location = etLocation.getText().toString();

                if (location != null && !location.equals("")) {
                    new GeocoderTask().execute(location);
                }
            }
        });

    }


    // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            //mMap.clear();

            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){

                Address address = (Address) addresses.get(i);

                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);

                mMap.addMarker(markerOptions);

                // Locate the first location
                if(i==0)
                    //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                centralizaNo(latLng);
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera




        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.getLista();
        dao.close();

        Localizador local = new Localizador(this);

        for(Aluno aluno : alunos)
        {
            LatLng coord = null;
            coord = local.getCoordenada(aluno.getEndereco());


            if(coord!=null)
            {
                mMap.addMarker(new MarkerOptions().title(aluno.getNome()).snippet(aluno.getTelefone()).position(coord));
                centralizaNo(coord);

            }
        }




        //new GPS(this);
        //getPosicao();








    }

    public void centralizaNo(LatLng coordenada)
    {
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(coordenada, 12);
        mMap.moveCamera(camera);


        Log.d("LOG", "centralizaNo: " + coordenada.toString());

        CameraPosition position = CameraPosition.builder()
                .target( new LatLng( coordenada.latitude, coordenada.longitude ) )
                .zoom( 16f )
                .bearing( 0.0f )
                .tilt( 0.0f )
                .build();



    }

    public void getPosicao()
    {

        mMap.setMyLocationEnabled(true);

        cliente = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        cliente.connect();

        Log.i("LOG", "getPosicao()");

        Location l = LocationServices.FusedLocationApi.getLastLocation(cliente);




        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);







        Log.i("LOG", "Passo 1");
        if(location !=null)
        {
            Log.i("LOG", "Latitude: " + location.getLatitude());
            Log.i("LOG", "Longitude: " + location.getLongitude());

            Log.i("LOG", "Passo 2");


            LatLng coord = null;
            coord = new LatLng(location.getLatitude(), location.getLongitude());

            Log.i("LOG", "Passo 3" + coord.latitude);
            centralizaNo(coord);
            Log.i("LOG", "Passo 4");

        }







    }


}
