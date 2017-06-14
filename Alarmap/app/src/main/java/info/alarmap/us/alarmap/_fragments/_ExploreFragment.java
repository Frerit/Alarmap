package info.alarmap.us.alarmap._fragments;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.alarmap.us.alarmap.R;
import info.alarmap.us.alarmap._models.Post;

/**
 * A simple {@link Fragment} subclass.
 */
public class _ExploreFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;
    private Circle circle;
    private GeoQuery geoQuery;


    List<Post> positions;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Incidents");
    GeoFire geofire = new GeoFire(ref);

    private static final GeoLocation INITIAL_CEN = new GeoLocation(6.217, -75.567);
    private Map<String,Marker> markers;


    public _ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_explore, container, false);
        // Event buttons
        ImageButton add = (ImageButton) v.findViewById(R.id.add_post);
        add.setOnClickListener(new View.OnClickListener() {  @Override  public void onClick(View view) {  addPosts(); } });
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment1 = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment1.getMapAsync(this);

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        positions = new ArrayList<>();

        // Add a marker in Sydney and move the camera
        final LatLng position = new LatLng(6.217, -75.567);
        mMap.addMarker(new MarkerOptions().position(position).title("Mi positions"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,14));
        mMap.getUiSettings().setCompassEnabled(true);

        geofire.setLocation("Incidents", new GeoLocation(6.217, -75.567));

        geofire.getLocation("Incidents", new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                } else {
                    System.out.println(String.format("There is no location for key %s in GeoFire", key));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("---- for : "," entoooo ");
                String value =  dataSnapshot.getValue().toString();
                positions.removeAll(positions);
//                for (DataSnapshot snapshot:
//                     dataSnapshot.getChildren()) {
//                    Post post = snapshot.getValue(Post.class);
//                    positions.add(post);
//                    Log.d("---- for : ",positions.toString());
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("---- Fire","------ Error ----- ");
            }
        });
        if (positions != null) {
            for (Post pos : positions) {
                Circle draft = addCircle(pos);
                googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {

                    @Override
                    public void onCircleClick(Circle circle) {
                        int strokeColor = circle.getStrokeColor() ^ 0x00ffffff;
                        circle.setStrokeColor(strokeColor);
                    }
                });
            }
        }
    }
    public void updateMaps() {

    }

    public Circle addCircle(Post post) {
        circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(post.lat,post.lon))
                .radius(10)
                .strokeWidth(1)
                .strokeColor(Color.MAGENTA)
                .fillColor(Color.argb(128,255,0,0))
                .clickable(true));

        return circle;
    }

    public void addPosts() {
        Log.d("12","adsda");
    }
}
