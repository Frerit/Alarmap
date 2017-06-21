package info.alarmap.us.alarmap._helpers;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.alarmap.us.alarmap.R;
import info.alarmap.us.alarmap._models.Incidence;
import info.alarmap.us.alarmap._models.Post;



/**
 * Created by julianperez on 6/20/17.
 */

public class AdaterRecy extends RecyclerView.Adapter<AdaterRecy.RecyclerV> {

    Context contex;
    List<Incidence> incidences;

    public AdaterRecy(List<Incidence> incidences) {
        this.incidences = incidences;
    }

    public class RecyclerV extends RecyclerView.ViewHolder {

        TextView texDescrip;

        public RecyclerV(View itemView) {
            super( itemView );
            texDescrip = (TextView) itemView.findViewById( R.id.incidenDescrip );
        }
    }

    @Override
    public RecyclerV onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.fragment_dash_item_card, parent, false );
        return new RecyclerV( v );
    }

    @Override
    public void onBindViewHolder(RecyclerV holder, int position) {
        Incidence inciden = incidences.get( position );
        holder.texDescrip.setText( inciden.incidenDesctiption );

    }

    @Override
    public int getItemCount() {
        return incidences.size();
    }
}
