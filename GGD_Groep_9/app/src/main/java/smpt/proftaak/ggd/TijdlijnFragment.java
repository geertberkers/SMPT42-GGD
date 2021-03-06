package smpt.proftaak.ggd;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TijdlijnFragment extends ListFragment {

    private boolean added;

    private ListView listView;
    private TijdlijnAdapter adapter;
    private ArrayList<TijdlijnItem> tijdlijnItems;
    private Ramp ramp;

    public TijdlijnFragment() {

        adapter = null;
        tijdlijnItems = new ArrayList<>();
    }

    public void onStart() {
        super.onStart();

        this.added = false;

        listView = (ListView) getView().findViewById(android.R.id.list);

        populateTijdlijnItems();
    }

    private void populateTijdlijnItems()
    {
        APICallTask apiTest = new APICallTask(this, APICallType.GET_TIJDLIJN, "http://stanjan.nl/smpt/API/nieuws.php?id=" + ramp.getID());
        apiTest.execute();
    }

    private void setUpNewsUpdate() {
        Timer timer = new Timer();
        TimerTask addNewItemTask;
        final Handler handler = new Handler();

        addNewItemTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // ADDED WEGHALEN OM MAAR EEN KEER TOE TE VOEGEN
                        if(!added) {
                            added = true;

                            tijdlijnItems.add(0, new TijdlijnItem("Gebouw B ontruimd", "brandweer", "23:36", "Lorem Ipsum is slechts een proeftekst uit het drukkerij- en zetterijwezen. Lorem Ipsum is de standaard proeftekst in deze bedrijfstak sinds de 16e eeuw, toen een onbekende drukker een zethaak met letters nam en ze door elkaar husselde om een font-catalogus te maken. Het heeft niet alleen vijf eeuwen overleefd maar is ook, vrijwel onveranderd, overgenomen in elektronische letterzetting. Het is in de jaren '60 populair geworden met de introductie van Letraset vellen met Lorem Ipsum passages en meer recentelijk door desktop publishing software zoals Aldus PageMaker die versies van Lorem Ipsum bevatten."));

                            listView.post(new Runnable() {
                                @Override
                                public void run() {
                                    listView.smoothScrollToPositionFromTop(0, 0, 1000);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
            }
        };

        //voeg 1 nieuw item toe
        timer.schedule(addNewItemTask, 1500);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent vragenlijstIntent = new Intent(v.getContext(), VragenlijstActivity.class);
        vragenlijstIntent.putExtra("ramp", ramp);
        v.getContext().startActivity(vragenlijstIntent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ramp = this.getArguments().getParcelable("ramp");
        return inflater.inflate(R.layout.tijdlijn_fragment, container, false);
    }

    public void setData(String data)
    {
        //Execute when JSON data is retrieved
        JSONParser parser = new JSONParser(data);
        tijdlijnItems = parser.getTijdlijnItems();

        //Geef een melding als er nog geen nieuws beschikbaar is
        if (tijdlijnItems.size() == 0)
        {
            tijdlijnItems.add(new TijdlijnItem("Er is op dit moment geen nieuws beschikbaar", "", new SimpleDateFormat("HH:mm").format(new Date()), "Probeer het later opnieuw."));
            tijdlijnItems.get(0).revokeAnimationPermission();
        }

        adapter = new TijdlijnAdapter(getActivity(),tijdlijnItems);
        setListAdapter(adapter);
    }
}
