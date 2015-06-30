package smpt.proftaak.ggd;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Joep on 30-6-2015.
 */
public class APICallTask extends AsyncTask<String, Void, String>
{

    private Fragment parent;

    public APICallTask(Fragment parent)
    {
        this.parent = parent;
    }

    @Override
    protected String doInBackground(String... strings) {
        String urlString = strings[0];
        URL url;
        try
        {
            url = new URL(urlString);
        }
        catch (MalformedURLException ex)
        {
            throw new RuntimeException("URL " + urlString + " is malformed");
        }

        String result = "";

        URLConnection con;
        try
        {
            con = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
            {
                result += inputLine;
            }
            in.close();
        }
        catch (IOException ex)
        {
            throw new RuntimeException("Connection failed");
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        TijdlijnFragment t = (TijdlijnFragment)parent;
        t.setData(result);
    }
}