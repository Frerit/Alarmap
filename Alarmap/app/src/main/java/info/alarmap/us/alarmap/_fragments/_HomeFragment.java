package info.alarmap.us.alarmap._fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

import info.alarmap.us.alarmap.Login;
import info.alarmap.us.alarmap.Main;
import info.alarmap.us.alarmap.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class _HomeFragment extends Fragment {


    public _HomeFragment() {
        // Required empty public constructor
    }

    ImageButton config;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        config = (ImageButton) v.findViewById( R.id.configButtom );
        config.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent( getContext(), Login.class );
                startActivity( intent );
            }
        } );

        return v;
    }

}
