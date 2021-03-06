package info.alarmap.us.alarmap._fragments;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
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
        RecyclerView.LayoutManager layou = new GridLayoutManager( getContext(), 2 );
        recicler.setLayoutManager( layou);
        recicler.addItemDecoration( new GridSpacingItemDecoration( 2, dpToPx(10), true ) );
        recicler.setItemAnimator( new DefaultItemAnimator() );

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

                adaptador = new AdaterRecy(getContext(), incidences);
                adaptador.notifyDataSetChanged();
                recicler.setAdapter( adaptador );

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

        return v;
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        public void getItemOffset(Rect outRect,View view, RecyclerView parent, RecyclerView.State state) {
            int positio = parent.getChildAdapterPosition( view );
            int column = positio % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (positio < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (positio > spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round( TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
