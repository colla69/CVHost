package info.colarietosti.demo.app.backend.util.mySeriesShuffler;


import java.util.ArrayList;

public class Serie {

    private String name = "";
    private String linkPart = "";

    private ArrayList<Season> seasonList = new ArrayList<>();

    public Serie(String name, String linkPart, Season ... aSeasonList) {
        this.name = name;
        this.linkPart = linkPart;

        Season[] var2 = aSeasonList;
        int var3 = aSeasonList.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Season s = var2[var4];
            seasonList.add(s);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkPart() {
        return linkPart;
    }

    public void setLinkPart(String linkPart) {
        this.linkPart = linkPart;
    }

    public ArrayList<Season> getSeasonList() {
        return seasonList;
    }

    public void setSeasonList(ArrayList<Season> seasonList) {
        this.seasonList = seasonList;
    }
}
