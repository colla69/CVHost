package info.colarietosti.demo.app.backend.util.mySeriesShuffler;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class SeriesShuffler {


    private ArrayList<Serie> seriesList = new ArrayList<>();

    public SeriesShuffler() {
        init();
    }

    public String shuffle(Optional<Serie> serie){
        if (serie.isPresent()){
            Random r = new Random();
            int seasonNo = serie.get().getSeasonList().size();
            int index = r.nextInt(seasonNo+1);

            for (Season s : serie.get().getSeasonList()) {
                if (s.getSeasonsNo() == index) {
                    int indexEpi = r.nextInt(s.getEpisodesNo() + 1);
                    return String.format("http://mywatchseries.to/episode/%s_s%d_e%d.html", serie.get().getLinkPart(), s.getSeasonsNo(), indexEpi);
                }
            }
        }
        return "";
    }

    public ArrayList<Serie> getSeriesList() {
        return seriesList;
    }

    public void setSeriesList(ArrayList<Serie> seriesList) {
        this.seriesList = seriesList;
    }



    private void init(){
        seriesList.add(
            new Serie("South Park","south_park",
                new Season(1,13),
                new Season(2,18),
                new Season(3,17),
                new Season(4,17),
                new Season(5,14),
                new Season(6,17),
                new Season(7,15),
                new Season(8,14),
                new Season(9,14),
                new Season(10,14),
                new Season(11,14),
                new Season(12,14),
                new Season(13,14),
                new Season(14,14),
                new Season(15,14),
                new Season(16,14),
                new Season(17,10),
                new Season(18,10),
                new Season(18,10),
                new Season(18,10)
            ));

            seriesList.add(
                new Serie("The Simpsons","the_simpsons",
                    new Season(1,13),
                    new Season(2,22),
                    new Season(3,24),
                    new Season(4,22),
                    new Season(5,22),
                    new Season(6,25),
                    new Season(7,25),
                    new Season(8,25),
                    new Season(9,25),
                    new Season(10,23),
                    new Season(11,22),
                    new Season(12,21),
                    new Season(13,22),
                    new Season(14,22),
                    new Season(15,22),
                    new Season(16,21),
                    new Season(17,22),
                    new Season(18,22),
                    new Season(19,20),
                    new Season(20,21),
                    new Season(21,23),
                    new Season(22,22),
                    new Season(23,22),
                    new Season(24,22),
                    new Season(25,22),
                    new Season(26,22),
                    new Season(27,22),
                    new Season(27,22),
                    new Season(27,21)
                ));

        seriesList.add(
            new Serie("The Big Bang Theory","big_bang_theory",
                new Season(1,17),
                new Season(2,23),
                new Season(3,23),
                new Season(4,24),
                new Season(5,24),
                new Season(6,24),
                new Season(7,24),
                new Season(8,24),
                new Season(9,24),
                new Season(10,24),
                new Season(11,24)
            ));
    }
}
