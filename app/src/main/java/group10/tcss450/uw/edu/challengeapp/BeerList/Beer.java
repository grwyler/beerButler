package group10.tcss450.uw.edu.challengeapp.BeerList;

/**
 * A beer class to represent beers that will be displayed in the beer list. This is necessary
 * in addition to the TopBrew class, because we need to be able to instantiate Beer objects from
 * the Recycler View adapter class and TopBrew only allows you to use .create.
 */

public class Beer {
    private String mName;
    private String mStyle;
    private String mIsOrganic;
    private String mLabelLink;
    private String mAbv;
    private String mIbu;
    private String mDescription;
    private String mNotes;
    private String mRating;

    public Beer(String name, String style, String isOrganic, String labelLink, String brewery,
                String abv, String ibu, String description, String notes, String rating) {
        mName = name;
        mStyle = style;
        mIsOrganic = isOrganic;
        mLabelLink = labelLink;
        mAbv = abv;
        mIbu = ibu;
        mDescription = description.equals("0") ? "" : description;
        mNotes = notes;
        mRating = rating;
    }

    public Beer(TopBrew topBrew) {
        if (topBrew != null) {
            mName = topBrew.getName();
            mStyle = topBrew.getStyle() == null ? "" : topBrew.getStyle().getName();
            mIsOrganic = topBrew.getIsOrganic();
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
}
