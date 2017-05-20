package group10.tcss450.uw.edu.challengeapp.BeerList;

/**
 * Created by Garrett on 5/19/2017.
 */

public class Beer {
    private String mName;
    private String mStyle;
    private boolean mIsOrganic;
    private String mLabelLink;
    private String mBrewery;
    private double mAbv;
    private double mIbu;
    private String mDescription;
    private String mNotes;
    private int mRating;

    public Beer(String name, String style, boolean isOrganic, String labelLink, String brewery,
                double abv, double ibu, String description, String notes, int rating) {
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

    public String getmName() {
        return mName;
    }

    public String getStyle() {
        return mStyle;
    }

    public boolean getIsOrganic() {
        return mIsOrganic;
    }

    public String getLabelLink() {
        return mLabelLink;
    }

    public String getBrewery() {
        return mBrewery;
    }

    public double getAbv() {
        return mAbv;
    }

    public double getIbu() {
        return mIbu;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getNotes() {
        return mNotes;
    }

    public int getRating() {
        return mRating;
    }
}
