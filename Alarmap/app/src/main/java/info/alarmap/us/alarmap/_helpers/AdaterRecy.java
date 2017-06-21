package info.alarmap.us.alarmap._helpers;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import info.alarmap.us.alarmap.R;
import info.alarmap.us.alarmap._models.Incidence;
import info.alarmap.us.alarmap._models.Post;



/**
 * Created by julianperez on 6/20/17.
 */

public class AdaterRecy extends RecyclerView.Adapter<AdaterRecy.RecyclerV> {

    private Context contex;
    List<Incidence> incidences;

    public AdaterRecy(Context context, List<Incidence> incidences) {
        this.contex = context;
        this.incidences = incidences;
    }

    public class RecyclerV extends RecyclerView.ViewHolder {

        TextView texDescrip;
        ImageView thumbnail, overflow;

        public RecyclerV(View itemView) {
            super( itemView );
            texDescrip = (TextView) itemView.findViewById( R.id.titledexcrip );
            thumbnail = (ImageView) itemView.findViewById( R.id.thumbnail );
            overflow = (ImageView) itemView.findViewById( R.id.overflow );
        }
    }

    @Override
    public RecyclerV onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.fragment_dash_item_card, parent, false );
        return new RecyclerV( v );
    }

    @Override
    public void onBindViewHolder(final RecyclerV holder, int position) {
        Incidence inciden = incidences.get( position );
        holder.texDescrip.setText( inciden.incidenDesctiption );
        Glide.with( contex ).load(inciden.incidenImage ).into( holder.thumbnail );

        holder.overflow.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPoputMenu( holder.overflow );
                Log.d( "a Pres","asdas" );
            }
        } );

    }

    private void showPoputMenu(View view) {
        PopupMenu popup = new PopupMenu( contex, view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate( R.menu.menu_post,popup.getMenu() );
        popup.setOnMenuItemClickListener( new MenuItenClicListener() );
        popup.show();
    }

    class MenuItenClicListener implements PopupMenu.OnMenuItemClickListener {

        public MenuItenClicListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Log.d( "--- Press", String.valueOf( item.getItemId() ) );
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return incidences.size();
    }
}
