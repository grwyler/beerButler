package group10.tcss450.uw.edu.challengeapp.BeerList;

/**
 * Created by Garrett on 5/19/2017.
 */

public class Beer {
    private String mName;
    private String mStyle;
    private String mIsOrganic;
    private String mLabelLink;
    private String mBrewery;
    private String mAbv;
    private String mIbu;
    private String mDescription;
    private String mNotes;
    private String mRating;
    private TopBrew.labels mLabels;

    public Beer(String name, String style, String isOrganic, String labelLink, String brewery,
                String abv, String ibu, String description, String notes, String rating) {
        mName = name;
        mStyle = style;
        mIsOrganic = isOrganic;
        mLabelLink = labelLink;
        mBrewery = brewery;
        mAbv = abv;
        mIbu = ibu;
        mDescription = description;
        mNotes = notes;
        mRating = rating;
    }

    public Beer(TopBrew topBrew) {
        if (topBrew != null) {
            mName = topBrew.getName();
            mStyle = topBrew.getStyle() == null ? "" : topBrew.getStyle().getName();
            mIsOrganic = topBrew.getIsOrganic();
//            mLabelLink = topBrew.getLabels().getMedium();
            mLabels = topBrew.getLabels();
            mBrewery = topBrew.getNameDisplay();
            mAbv = topBrew.getAbv();
            mIbu = topBrew.getIbu();
            mDescription = topBrew.getDescription();
            mNotes = "";
            mRating = "";

        }

    }

    public String getmName() {
        return mName;
    }

    public String getStyle() {
        return mStyle;
    }

    public String getIsOrganic() {
        return mIsOrganic;
    }

    public String getLabelLink() {
        return mLabelLink;
    }

    public String getBrewery() {
        return mBrewery;
    }

    public String getAbv() {
        return mAbv;
    }

    public String getIbu() {
        return mIbu;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getNotes() {
        return mNotes;
    }

    public String getRating() {
        return mRating;
    }

    public TopBrew.labels getLabels() {
        return mLabels;
    }
}
