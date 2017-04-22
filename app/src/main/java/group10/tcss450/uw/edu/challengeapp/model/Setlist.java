package group10.tcss450.uw.edu.challengeapp.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Setlist implements Serializable {

    private Integer mShowid;
    private String mShowdate;
    private String mShortDate;
    private String mLongDate;
    private String mRelativeDate;
    private String mUrl;
    private String mGapchart;
    private String mArtist;
    private Integer mArtistid;
    private Integer mVenueid;
    private String mVenue;
    private String mLocation;
    private String mSetlistdata;
    private Map<String, Object> mAdditionalProperties = new HashMap<String, Object>();

    private Setlist(JSONObject json) {

    }
    public static Setlist createSetlist(JSONObject json) {
        if (json == null) {
            throw new IllegalArgumentException();
        }
        return new Setlist(json);
    }

    public Integer getShowid() {
        return mShowid;
    }

    public String getShowdate() {
        return mShowdate;
    }

    public String getShortDate() {
        return mShortDate;
    }

    public String getLongDate() {
        return mLongDate;
    }

    public String getRelativeDate() {
        return mRelativeDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getGapchart() {
        return mGapchart;
    }

    public String getArtist() {
        return mArtist;
    }

    public Integer getArtistid() {
        return mArtistid;
    }

    public Integer getVenueid() {
        return mVenueid;
    }

    public String getVenue() {
        return mVenue;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getSetlistdata() {
        return mSetlistdata;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.mAdditionalProperties;
    }

}