package smpt.proftaak.ggd;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geert on 18-6-2015
 */
public class RampAdapter extends BaseAdapter{
    private Context context;

    private List<Ramp> rampenLijst = new ArrayList<>();

    public RampAdapter(Context context, List<Ramp> rampenLijst) {
        this.context = context;
        this.rampenLijst = rampenLijst;
    }

    @Override
    public int getCount() {
        return rampenLijst.size();
    }

    @Override
    public Ramp getItem(int position) {
        return rampenLijst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.ramp_row, parent, false);
        } else {
            row = convertView;
        }

        TextView titelRamp = (TextView) row.findViewById(R.id.titelRamp);
        TextView laatsteUpdate = (TextView) row.findViewById(R.id.laatsteUpdate);
        TextView omschrijving = (TextView) row.findViewById(R.id.omschrijving);

        if(rampenLijst.get(position).getTitelRamp() == "Geen rampen momenteel")
        {
            ImageView warningImg = (ImageView) row.findViewById(R.id.warningImage);
            ImageView arrow = (ImageView) row.findViewById(R.id.arrow);

            titelRamp.setTextColor(Color.BLACK);
            warningImg.setImageResource(R.drawable.no_warning);
            arrow.setVisibility(View.INVISIBLE);
        }

        titelRamp.setText(rampenLijst.get(position).getTitelRamp());
        laatsteUpdate.setText("Laatste update: " + rampenLijst.get(position).getLaatsteUpdate());
        omschrijving.setText(rampenLijst.get(position).getOmschrijving());

        return row;
    }
}

