package smpt.proftaak.ggd;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class VragenlijstActivity extends BaseActivity {

    private Vragenlijst vragenlijst;
    private Ramp ramp;
    private ListView symptomenList;
    private ListView openvragenList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vragenlijst);

        ramp = getIntent().getParcelableExtra("ramp");

        APICallTask apiTest = new APICallTask(this, APICallType.GET_VRAGENLIJST, "http://stanjan.nl/smpt/API/vragen.php?id=" + ramp.getID());
        apiTest.execute();
    }

    /**
     * Deze methode wordt gebruikt om de hoogte van de listview met symptomen aan te geven.
     * Als deze methode niet wordt aangeroepen is de lsitview slechts 1 row hoog
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vragenlijst, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sendVragenlijst) {
            //TODO
            buildAntwoordURL();
            Toast.makeText(this, "VERSTUUR VRAGENLIJST", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private String buildAntwoordURL()
    {
        String baseURL = "http://http://stanjan.nl/smpt/API//antwoord.php?id=";

        //Add ramp id to url
        baseURL += ramp.getID();

        //Add user email to url
        baseURL += "&email=";//TODO

        //Add user postcode to url
        baseURL += "&postcode=";//TODO

        //Add antwoorden to url

        String symptomenAntwoord = "";

        for (int i = 0; i < symptomenList.getCount(); i++)
        {
            View currentRow = symptomenList.getChildAt(i);
            CheckBox symptoomCheck = (CheckBox)currentRow.findViewById(R.id.symptoomCheck);

            if (symptoomCheck.isChecked())
            {
                symptomenAntwoord += "Y";
            }
            else
            {
                symptomenAntwoord += "N";
            }
        }

        return symptomenAntwoord;
    }

    public void setData(String data)
    {
        //Execute when JSON data is retrieved
        JSONParser parser = new JSONParser(data);
        vragenlijst = parser.getVragenlijst();

        if (vragenlijst.getId() == -1)
        {
            Toast.makeText(this, "Er is op dit moment geen vragenlijst beschikbaar voor deze situatie.", Toast.LENGTH_SHORT).show();
            finish();
        }

        ArrayList<String> symptomen = new ArrayList<>();

        if (vragenlijst.getSymptoomVragen() != null)
        {
            for (Map.Entry<Integer, String> current: vragenlijst.getSymptoomVragen().entrySet())
            {
                symptomen.add(current.getValue());
            }
        }
        else
        {
            TextView symptomenTitle = (TextView)findViewById(R.id.vragenlijstTitle);
            symptomenTitle.setVisibility(View.GONE);
        }

        VragenlijstSymptomenAdapter symptomenAdapter = new VragenlijstSymptomenAdapter(this, symptomen);
        symptomenList = (ListView)findViewById(R.id.vragenlijstSymptomen);
        symptomenList.setAdapter(symptomenAdapter);
        setListViewHeightBasedOnChildren(symptomenList);

        symptomenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toggle checkbox on row click
                CheckBox symptoomCheck = (CheckBox) view.findViewById(R.id.symptoomCheck);
                symptoomCheck.toggle();
            }
        });

        ArrayList<Vraag> openVragen = new ArrayList<>();
        for (Vraag current: vragenlijst.getVragen())
        {
            if (current.getSoort().equals("open"))
            {
                openVragen.add(current);
            }
        }

        VragenlijstOpenVragenAdapter openAdapter = new VragenlijstOpenVragenAdapter(this, openVragen);
        openvragenList = (ListView)findViewById(R.id.vragenlijstOpenVragen);
        openvragenList.setAdapter(openAdapter);
        setListViewHeightBasedOnChildren(openvragenList);
    }
}
