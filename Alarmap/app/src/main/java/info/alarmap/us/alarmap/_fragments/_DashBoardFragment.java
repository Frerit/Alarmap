package info.alarmap.us.alarmap._fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import info.alarmap.us.alarmap.R;
import info.alarmap.us.alarmap._helpers.AdaterRecy;
import info.alarmap.us.alarmap._models.Incidence;

/**
 * A simple {@link Fragment} subclass.
 */
public class _DashBoardFragment extends Fragment {

    RecyclerView recicler;
    List<Incidence> incidences;

    AdaterRecy adaptador;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public _DashBoardFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fragment_dash_board, container, false );

        incidences = new ArrayList<Incidence>(  );

        recicler = (RecyclerView) v.findViewById( R.id.recyclerDash );
        recicler.setLayoutManager( new LinearLayoutManager(getContext()));

        database.getReference().child( "Users" ).child( user.getUid()).child( "Post" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (incidences != null) {
                    incidences.removeAll( incidences );
                }
                for (DataSnapshot snapshop: dataSnapshot.getChildren()) {
                    Incidence inciden = snapshop.getValue(Incidence.class);
                    incidences.add( inciden );
                    Log.d( "---------- ",inciden.toString() );
                }

                adaptador = new AdaterRecy(incidences);
                adaptador.notifyDataSetChanged();
                recicler.setAdapter( adaptador );

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );





        return v;
    }

    public String toStrin() {
        return "fragment_dash_board";
    }

}
