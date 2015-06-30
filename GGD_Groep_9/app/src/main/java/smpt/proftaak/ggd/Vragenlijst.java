package smpt.proftaak.ggd;

import java.util.ArrayList;

/**
 * Created by Joep on 30-6-2015.
 */
public class Vragenlijst
{
    private int id;
    private String tijdstip;
    private ArrayList<Vraag> vragen;

    public Vragenlijst(int id, String tijdstip, ArrayList<Vraag> vragen)
    {
        this.id = id;
        this.tijdstip = tijdstip;
        this.vragen = vragen;
    }

    public int getId() {
        return id;
    }

    public String getTijdstip() {
        return tijdstip;
    }

    public ArrayList<Vraag> getVragen() {
        return vragen;
    }
}
