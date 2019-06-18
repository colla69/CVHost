package info.colarietosti.demo.app.ui.CV;

public enum QualificationEnum {
    threePZ("3Points Reference", "ArbeitsZeugnis.pdf"),
    AusbZ("Apprenticeship certificate", "AUSB_IHK_Zeugnis.pdf"),
    ausb3pz("Apprenticeship Reference", "AUSB_3P_Zeugnis.pdf"),
    ABI("Abitur", " ABI.pdf"),
    daf("TestDaf", " TESTDAF.pdf"),
    db1("German B1", " ZD.pdf"),
    da2("German A2", " FID2.pdf"),
    eb1("English B1", " PET.pdf"),
    ea2("English A2", " KET.pdf");

    QualificationEnum(String name, String link){
        this.name = name;
        this.link = link;
    }

    private String name;

    private String link;

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

}

