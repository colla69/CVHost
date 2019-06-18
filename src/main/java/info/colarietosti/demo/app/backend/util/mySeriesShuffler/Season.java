package info.colarietosti.demo.app.backend.util.mySeriesShuffler;

public class Season {

    private int seasonsNo = 0;
    private int episodesNo = 0;

    public int getSeasonsNo() {
        return seasonsNo;
    }

    public void setSeasonsNo(int seasonsNo) {
        this.seasonsNo = seasonsNo;
    }

    public Season(int seasonsNo, int episodesNo) {
        this.seasonsNo = seasonsNo;
        this.episodesNo = episodesNo;
    }

    public int getEpisodesNo() {
        return this.episodesNo;
    }

    public void setEpisodesNo(int episodesNo) {
        this.episodesNo = episodesNo;
    }

}
