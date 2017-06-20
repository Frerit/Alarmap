package info.alarmap.us.alarmap._fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.alarmap.us.alarmap.R;
import info.alarmap.us.alarmap._models.Incidence;
import info.alarmap.us.alarmap._models.Post;

/**
 * A simple {@link Fragment} subclass.
 */
public class _ExploreFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;
    private Circle circle;
    private GeoQuery geoQuery;

    List<Post> positions;
    LocationManager locationManager;
    double latitud;
    double longitud;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Madals
    CardView modalAdd;
    RadioButton type1, type2;
    LinearLayout ubiationManual;
    Spinner companyProvehedor;
    TextView incidentDes;

    // Forms
    String typeUser;
    String companyType;
    String incidenDesctip;
    Post localization;
    ArrayList<String> LocationCompuest;
    String incidenImage;


    // Dats
    String[] provehedor = {"Escoje un Servicio","Agua","Energia","Gas","Internet","Parabolica","Incidente Publico","Otros (Queja General)"};

    public _ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate( R.layout.fragment_explore, container, false );
        // Event buttons
        ImageButton add = (ImageButton) v.findViewById( R.id.add_post );
        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPosts();
            }
        } );

        // Modal
        modalAdd = (CardView) v.findViewById( R.id.modalAddIncident );
        ImageButton closeMo = (ImageButton) v.findViewById( R.id.closeModal );
        closeMo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleModal();
            }
        } );
        Button saveReg = (Button) v.findViewById( R.id.saveRegistro );
        saveReg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRegistrosFire();
            }
        } );

        type1 = (RadioButton) v.findViewById( R.id.type1 );
        type2 = (RadioButton) v.findViewById( R.id.type2 );

        Button manualLocal = (Button) v.findViewById( R.id.locationManual );
        ubiationManual = (LinearLayout) v.findViewById( R.id.ubicatioManual );
        companyProvehedor = (Spinner) v.findViewById( R.id.conpanyProve );
        ArrayAdapter<String> adapteCompany = new ArrayAdapter<String>( getContext(), android.R.layout.simple_spinner_item, provehedor );
        companyProvehedor.setAdapter( adapteCompany );

        manualLocal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {  if (ubiationManual.getVisibility() == View.GONE) {  ubiationManual.setVisibility( View.VISIBLE );
                } else {  ubiationManual.setVisibility( View.GONE );   }
            }
        } );

        companyProvehedor.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                companyType = provehedor[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                companyType = null;
            }
        } );
        incidentDes = (TextView) v.findViewById( R.id.incidenDescrip );
        Button locat = (Button) v.findViewById( R.id.locations );
        locat.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpMap();
            }
        } );

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        ActivityCompat.requestPermissions( (Activity) getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1 );
        locationManager = (LocationManager) getContext().getSystemService( Context.LOCATION_SERVICE );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        SupportMapFragment mapFragment1 = (SupportMapFragment) getChildFragmentManager().findFragmentById( R.id.mapView );
        mapFragment1.getMapAsync( this );

    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder( getContext() );
        dialog.setTitle( "Enable Location" )
                .setMessage( "Su ubicación esta desactivada.\npor favor active su ubicación " +
                        "usa esta app" )
                .setPositiveButton( "Configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent mi = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
                        startActivity( mi );
                    }
                } )
                .setNegativeButton( "Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                } );
        dialog.show();
    }

    private boolean isLocationEnabled() {
        boolean enableOrDis = locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER );
        boolean enOrdis =  locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER );
        Log.d( "Com ---- ", String.valueOf( enableOrDis ) );
        return  (enableOrDis || enOrdis);

    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void setUpMap() {
        checkLocation();

        if (ActivityCompat.checkSelfPermission( getContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( getContext(), Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled( true );
        mMap.setMapType( GoogleMap.MAP_TYPE_NORMAL );
        comenzarLocalizacion();
        final LatLng position = new LatLng( latitud, longitud );
        //mMap.addMarker( new MarkerOptions().position( position ).title( "Mi positions" ) );
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( position, 13 ) );
        mMap.getUiSettings().setCompassEnabled( true );

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void comenzarLocalizacion() {
        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getContext().getSystemService( context );

        Criteria crta = new Criteria();
        crta.setAccuracy( Criteria.ACCURACY_FINE );
        crta.setAltitudeRequired( false );
        crta.setBearingRequired( false );
        crta.setCostAllowed( true );
        crta.setPowerRequirement( Criteria.POWER_LOW );
        String provider = locationManager.getBestProvider( crta, true );

        if (ActivityCompat.checkSelfPermission( getContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( getContext(), Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation( provider );
        updateWithNewLocation(location);

        locationManager.requestLocationUpdates(provider, 15000, 0,
                locationListener);
    }

    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private void updateWithNewLocation(Location location) {

        if (location != null) {
            latitud = location.getLatitude();
            longitud = location.getLongitude();

            Log.e("LOCATION", "your Current Position is : " + latitud + " -  "+ longitud);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
        positions = new ArrayList<Post>();

        ref.child( "Incidents" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d( "---- for : ", " entoooo " );
                String value = dataSnapshot.getValue().toString();
                positions.removeAll( positions );
                for (DataSnapshot snapshot:
                     dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    positions.add(post);
                    addCircle(post);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d( "---- Fire", "------ Error ----- " );
            }
        } );
        if (positions != null) {
            for (Post pos : positions) {
                Circle draft = addCircle( pos );
                googleMap.setOnCircleClickListener( new GoogleMap.OnCircleClickListener() {

                    @Override
                    public void onCircleClick(Circle circle) {
                        int strokeColor = circle.getStrokeColor() ^ 0x00ffffff;
                        circle.setStrokeColor( strokeColor );
                    }
                } );
            }
        }
    }


    public Circle addCircle(Post post) {
        circle = mMap.addCircle( new CircleOptions()
                .center( new LatLng( post.lat, post.lon ) )
                .radius( 100 )
                .strokeWidth( 1 )
                .strokeColor( Color.argb( 70,255,75,75 ) )
                .fillColor( Color.argb( 40,255,75,75 ) )
                .clickable( true ) );

        return circle;
    }

    public void addPosts() {
         ToggleModal();
    }

    public  void ToggleModal() {
       if (modalAdd.getVisibility() == View.GONE) {
            modalAdd.setVisibility( View.VISIBLE );
       } else  {
           modalAdd.setVisibility( View.GONE );
       }
    }

    public void saveRegistrosFire() {
        typeUser = (type2.isChecked()) ?  "Anonimo" :  user.getUid();
        companyType = (companyType != null) ? companyType : "Other";
        incidenDesctip = incidentDes.getText().toString();
        localization = new Post(user.getUid(), latitud, longitud );
        incidenImage = "https://placeimg.com/400/300/arch";

        Incidence data = new Incidence(typeUser,companyType,incidenDesctip,localization,incidenImage );
        Map<String,Post> local = new HashMap<String, Post>(  );
        ref.child("Users").child( user.getUid() ).child( "Post" ).push().setValue( data );
        ref.child( "Incidents" ).push().setValue( localization, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                ToggleModal();
            }
        } );
    }
}
