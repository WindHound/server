package com.windhound.server.race;

public class BoatInfo {
    private Long sailNo;
    private Long displacement;
    private Long length;
    private Long beam;
    private Long draft;
    private Long gph;
    private Long offshoreToD;
    private Long offshoreToT;
    private Long offshoreTnoL;
    private Long offshoreTnoM;
    private Long offshoreTnoH;
    private Long inshoreToD;
    private Long inshoreToT;
    private Long inshoreTnoL;
    private Long inshoreTnoM;
    private Long inshoreTnoH;
    private String skipper;
    private String boatClass;
    private String type;

    public BoatInfo(Long sailNo, Long displacement, Long length, Long beam, Long draft, Long gph, Long offshoreToD,
                    Long offshoreToT, Long offshoreTnoL, Long offshoreTnoM, Long offshoreTnoH, Long inshoreToD,
                    Long inshoreToT, Long inshoreTnoL, Long inshoreTnoM, Long inshoreTnoH, String skipper,
                    String boatClass, String type) {

        this.sailNo = sailNo;
        this.displacement = displacement;
        this.length = length;
        this.beam = beam;
        this.draft = draft;
        this.gph = gph;
        this.offshoreToD = offshoreToD;
        this.offshoreToT = offshoreToT;
        this.offshoreTnoL = offshoreTnoL;
        this.offshoreTnoM = offshoreTnoM;
        this.offshoreTnoH = offshoreTnoH;
        this.inshoreToD = inshoreToD;
        this.inshoreToT = inshoreToT;
        this.inshoreTnoL = inshoreTnoL;
        this.inshoreTnoM = inshoreTnoM;
        this.inshoreTnoH = inshoreTnoH;
        this.skipper = skipper;
        this.boatClass = boatClass;
        this.type = type;
    }

    //Minimal constructor for testing
    public BoatInfo(Long sailNo, String skipper, String boatClass, String type) {
        this.sailNo = sailNo;
        this.skipper = skipper;
        this.boatClass = boatClass;
        this.type = type;
    }


    //
    //  GETTERS for all attributes
    //


    public Long getSailNo() {
        return sailNo;
    }

    public Long getDisplacement() {
        return displacement;
    }

    public Long getLength() {
        return length;
    }

    public Long getBeam() {
        return beam;
    }

    public Long getDraft() {
        return draft;
    }

    public Long getGph() {
        return gph;
    }

    public Long getOffshoreToD() {
        return offshoreToD;
    }

    public Long getOffshoreToT() {
        return offshoreToT;
    }

    public Long getOffshoreTnoL() {
        return offshoreTnoL;
    }

    public Long getOffshoreTnoM() {
        return offshoreTnoM;
    }

    public Long getOffshoreTnoH() {
        return offshoreTnoH;
    }

    public Long getInshoreToD() {
        return inshoreToD;
    }

    public Long getInshoreToT() {
        return inshoreToT;
    }

    public Long getInshoreTnoL() {
        return inshoreTnoL;
    }

    public Long getInshoreTnoM() {
        return inshoreTnoM;
    }

    public Long getInshoreTnoH() {
        return inshoreTnoH;
    }

    public String getSkipper() {
        return skipper;
    }

    public String getBoatClass() {
        return boatClass;
    }

    public String getType() {
        return type;
    }
}
