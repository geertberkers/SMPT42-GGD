package smpt.proftaak.ggd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joep on 30-6-2015.
 */
public class JSONParser {
    private String jsonString;

    public JSONParser(String jsonString) {
        this.jsonString = jsonString;
    }

    public ArrayList<Ramp> getRampen() {

        ArrayList<Ramp> resultRampen = new ArrayList<>();

        try {
            JSONArray rootArray = new JSONArray(jsonString);

            for(int i = 0; i<rootArray.length();i++)
            {
                JSONObject jsonObject = (JSONObject) rootArray.get(i);

                int id = jsonObject.getInt("id");
                String naam = jsonObject.getString("naam");
                String beschrijving = jsonObject.getString("beschrijving");
                String laatsteupdate = jsonObject.getString("laatsteupdate");

                resultRampen.add(new Ramp(id,naam,laatsteupdate,beschrijving));
            }
        }
        catch(JSONException ex)
        {
            ex.printStackTrace();
        }

        return resultRampen;
    }

    public Vragenlijst getVragenlijst() {
        //init vragenlijst variables
        int resultId = -1;
        String resultTijdstip = "";
        ArrayList<Vraag> resultVragen = new ArrayList<>();

        //create JSON  object
        try {
            //init root object
            JSONArray rootArray = new JSONArray(jsonString);
            JSONObject jsonObject = (JSONObject) rootArray.get(0);

            //get vragenlijst info
            resultId = jsonObject.getInt("id");
            resultTijdstip = jsonObject.getString("tijdstip");

            //get vragen
            JSONArray vragenArray = jsonObject.getJSONArray("vragen");
            for (int i = 0; i < vragenArray.length(); i++) {
                //init vraag variables
                int vraagId = -1;
                String vraagSoort = "";
                String vraag = "";
                Map<Integer, String> vraagSymptomen = new HashMap<Integer, String>();

                //get info for each vraag
                JSONObject currentVraag = vragenArray.getJSONObject(i);
                vraagId = currentVraag.getInt("id");
                vraagSoort = currentVraag.getString("soort");
                vraag = currentVraag.getString("vraag");

                //get symptomen
                if (currentVraag.has("symptomen")) {
                    JSONArray symptomenArray = currentVraag.getJSONArray("symptomen");
                    for (int j = 0; j < symptomenArray.length(); j++) {
                        //get info for each symptoom
                        JSONObject currentSymptoom = symptomenArray.getJSONObject(j);
                        vraagSymptomen.put(currentSymptoom.getInt("id"), currentSymptoom.getString("symptoom"));
                    }
                }

                //create new vraag based on info and add to vragenlijst
                Vraag newVraag = new Vraag(vraagId, vraagSoort, vraag, vraagSymptomen);
                resultVragen.add(newVraag);
            }

        }
        catch(JSONException ex)
        {
            ex.printStackTrace();
        }

        // Return vragenlijst
        return new Vragenlijst(resultId, resultTijdstip, resultVragen);
    }

    public ArrayList<TijdlijnItem> getTijdlijnItems() {
        ArrayList<TijdlijnItem> result = new ArrayList<>();
        try
        {
            JSONArray rootArray = new JSONArray(jsonString);
            for (int i = 0; i < rootArray.length(); i++)
            {
                //prepare tijdlijnitem variables
                String itemTitle = "";
                String itemImgSrc = "";
                String itemTimestamp = "";
                String itemDescription = "";
                JSONObject currentItem = rootArray.getJSONObject(i);

                itemTitle = currentItem.getString("titel");
                itemImgSrc = currentItem.getString("afbeelding");
                itemTimestamp = currentItem.getString("tijdstip").substring(11, 16);
                itemDescription = currentItem.getString("beschrijving");

                //Add new item to list to return
                TijdlijnItem newTijdlijnItem = new TijdlijnItem(itemTitle, itemImgSrc, itemTimestamp, itemDescription);

                //revoke animation because item is loaded on fragment entrance
                newTijdlijnItem.revokeAnimationPermission();

                result.add(newTijdlijnItem);
            }
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    public Informatie getInformatie() {

        try {
            JSONArray rootArray = new JSONArray(jsonString);
            JSONObject jsonObject = (JSONObject) rootArray.get(0);

            String titel = jsonObject.getString("titel");
            String beschrijving = jsonObject.getString("beschrijving");
            String afbeelding = jsonObject.getString("afbeelding");
            String tijdstip = jsonObject.getString("tijdstip");

            return new Informatie(titel, beschrijving, afbeelding, tijdstip);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
