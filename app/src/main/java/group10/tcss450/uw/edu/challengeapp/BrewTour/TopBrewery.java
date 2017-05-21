package group10.tcss450.uw.edu.challengeapp.BrewTour;

/**
 * Created by chris on 5/2/17.
 */

/**
 * The top level class 'TopBrewery' contains a nested class 'brewery' and 'country'.
 * The nested 'brewery' class contains another class 'images'
 *
 * it is intended for subclasses to have a lowercase class name, to aid with reflection
 */

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("unused")
public class TopBrewery implements Serializable {

    private String id;
    private String name;
    private String streetAddress;
    private String locality;
    private String region;
    private String postalCode;
    private String phone;
    private String website;
    private String hoursOfOperation;
    private Double latitude;
    private Double longitude;
    private String isPrimary;
    private String inPlanning;
    private String isClosed;
    private String openToPublic;
    private String locationType;
    private String locationTypeDisplay;
    private String countryIsoCode;
    private String status;
    private String statusDisplay;
    private String createDate;
    private String updateDate;
    private String breweryId;
    private brewery brewery; //this is a subclass
    private country country; //this is a subclass
    private String yearOpened;
    private Double distance;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Consider this a modified constructor, required to parse a JSON response.
     *
     * @param brewery: a JSONObject representing the response from our API
     * @return result: new TopBrewery object, which contains nested brewery and country objects
     */
    static TopBrewery create(JSONObject brewery) {
        TopBrewery result = new TopBrewery();

        try {
            Iterator<String> it = brewery.keys();
            while (it.hasNext()) {
                String next = it.next();
                Class<?> doubleType = Double.class;
                Class<?> stringType = String.class;
                Field f = null;
                try {
                    f = result.getClass().getDeclaredField(next);
                } catch (NoSuchFieldException e) { //nothing to do here but go to next iteration
                    Log.e("TOPBREWERY", e.toString());
                    continue;
                }
                if (f.getType().isAssignableFrom(doubleType) || f.getType().isAssignableFrom(stringType)) {
                    if ( brewery.get(next) != null) {
                        try {
                            f.set(result, brewery.get(next));
                        } catch (IllegalArgumentException e) {
                            /*
                             * This is caused by distance occasionally being recognized as an int when it needs to be a double
                             */
                            if (f.getType() == Double.class) {
                                double d = (double)((Integer)brewery.get(next)).intValue();;
                                f.set(result, d);
                            }
                            Log.e("TOPBREWERY", e.getMessage());
                        }
                    }
                } else {
                    //nasty stuff
                    Class tempClass = f.getType();
                    Class[] carg = new Class[1];
                    carg[0] = JSONObject.class;
                    Constructor<?> constructor = tempClass.getConstructor(JSONObject.class);
                    constructor.setAccessible(true);
                    Object c = constructor.newInstance(brewery.getJSONObject(next));
                    f.set(result,c);
                }
            }
        }
        catch (Exception e) {
            Log.e("TOPBREWERY", e.toString());
        }
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getHoursOfOperation() {
        return hoursOfOperation;
    }

    public void setHoursOfOperation(String hoursOfOperation) {
        this.hoursOfOperation = hoursOfOperation;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getInPlanning() {
        return inPlanning;
    }

    public void setInPlanning(String inPlanning) {
        this.inPlanning = inPlanning;
    }

    public String getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(String isClosed) {
        this.isClosed = isClosed;
    }

    public String getOpenToPublic() {
        return openToPublic;
    }

    public void setOpenToPublic(String openToPublic) {
        this.openToPublic = openToPublic;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getLocationTypeDisplay() {
        return locationTypeDisplay;
    }

    public void setLocationTypeDisplay(String locationTypeDisplay) {
        this.locationTypeDisplay = locationTypeDisplay;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getBreweryId() {
        return breweryId;
    }

    public void setBreweryId(String breweryId) {
        this.breweryId = breweryId;
    }

    public brewery getBrewery() {
        return brewery;
    }

    public void setBrewery(brewery brewery) {
        this.brewery = brewery;
    }

    public country getCountry() {
        return country;
    }

    public void setCountry(country country) {
        this.country = country;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
    public String getYearOpened() {
        return yearOpened;
    }

    public void setYearOpened(String yearOpened) {
        this.yearOpened = yearOpened;
    }

    public static class brewery implements Serializable {

        private String id;
        private String name;
        private String nameShortDisplay;
        private String description;
        private String isOrganic;
        private images images; //this is a subclass
        private String status;
        private String statusDisplay;
        private String createDate;
        private String updateDate;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();


        public brewery (JSONObject brewery) {

            try {
                Iterator<String> it = brewery.keys();
                while (it.hasNext()) {
                    String next = it.next();
                    Field f = null;
                    try {
                        f = this.getClass().getDeclaredField(next);
                    } catch (NoSuchFieldException e) {
                        Log.e("TOPBREWERY", e.toString());
                        continue;
                    }
                    Class<?> stringType = String.class;
                    if ( f.getType().isAssignableFrom(stringType)) {
                        try {
                            f.set(this, brewery.get(next));
                        } catch (IllegalArgumentException e) {
                            Log.e("TOPBREWERY", e.getMessage().toString());
                        }
                    } else {
                        //nasty stuff
                        Class tempClass = f.getType();
                        Class[] carg = new Class[1];
                        carg[0] = JSONObject.class;
                        Constructor<?> constructor = tempClass.getConstructor(JSONObject.class);
                        constructor.setAccessible(true);
                        Object c = constructor.newInstance(brewery.getJSONObject(next));
                        f.set(this,c);
                    }
                }
            }
            catch (Exception e) {
                Log.e("TOPBREWERY", e.toString());
            }
            //return result;
        }

        public brewery() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNameShortDisplay() {
            return nameShortDisplay;
        }

        public void setNameShortDisplay(String nameShortDisplay) {
            this.nameShortDisplay = nameShortDisplay;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIsOrganic() {
            return isOrganic;
        }

        public void setIsOrganic(String isOrganic) {
            this.isOrganic = isOrganic;
        }

        public images getImages() {
            return images;
        }

        public void setImages(images images) {
            this.images = images;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatusDisplay() {
            return statusDisplay;
        }

        public void setStatusDisplay(String statusDisplay) {
            this.statusDisplay = statusDisplay;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }

    public static class country implements Serializable {

        private String isoCode;
        private String name;
        private String displayName;
        private String isoThree;
        private Integer numberCode;
        private String createDate;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public country (JSONObject country) {

            try {
                Iterator<String> it = country.keys();
                while (it.hasNext()) {
                    String next = it.next();
                    Class<?> intType = Integer.class;
                    Class<?> stringType = String.class;
                    Field f = null;
                    try {
                        f = this.getClass().getDeclaredField(next);
                    } catch (NoSuchFieldException e) {
                        Log.e("TOPBREWERY", e.toString());
                        continue;
                    }
                    if (f.getType().isAssignableFrom(intType) || f.getType().isAssignableFrom(stringType)) {
                        try {
                            f.set(this, country.get(next));
                        } catch (IllegalArgumentException e) {
                            Log.e("TOPBREWERY", e.getMessage().toString());
                        }
                    } else {
                        //nasty stuff
                        Class tempClass = f.getType();
                        Class[] carg = new Class[1];
                        carg[0] = JSONObject.class;
                        Constructor<?> constructor = tempClass.getConstructor(JSONObject.class);
                        constructor.setAccessible(true);
                        Object c = constructor.newInstance(country.getJSONObject(next));
                        f.set(this,c);
                    }
                }
            }
            catch (Exception e) {
                Log.e("TOPBREWERY", e.toString());
            }
            //return result;
        }

        public country() {
        }

        public String getIsoCode() {
            return isoCode;
        }

        public void setIsoCode(String isoCode) {
            this.isoCode = isoCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getIsoThree() {
            return isoThree;
        }

        public void setIsoThree(String isoThree) {
            this.isoThree = isoThree;
        }

        public Integer getNumberCode() {
            return numberCode;
        }

        public void setNumberCode(Integer numberCode) {
            this.numberCode = numberCode;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }


    public static class images implements Serializable {

        private String icon;
        private String medium;
        private String large;
        private String squareMedium;
        private String squareLarge;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public images (JSONObject images) {

            try {
                Iterator<String> it = images.keys();
                while (it.hasNext()) {
                    String next = it.next();
                    Class<?> stringType = String.class;
                    Field f = null;
                    try {
                        f = this.getClass().getDeclaredField(next);
                    } catch (NoSuchFieldException e) {
                        Log.e("TOPBREWERY", e.toString());
                        continue;
                    }
                    if (f.getType().isAssignableFrom(stringType)) {
                        try {
                            f.set(this, images.get(next));
                        } catch (IllegalArgumentException e) {
                            Log.e("TOPBREWERY", e.getMessage().toString());
                        }
                    } else {
                        //nasty stuff
                        Class tempClass = f.getType();
                        Class[] carg = new Class[1];
                        carg[0] = JSONObject.class;
                        Constructor<?> constructor = tempClass.getConstructor(JSONObject.class);
                        constructor.setAccessible(true);
                        Object c = constructor.newInstance(images.getJSONObject(next));
                        f.set(this,c);
                    }
                }
            }
            catch (Exception e) {
                Log.e("TOPBREWERY", e.toString());
            }
            //return result;
        }
        public images() {

        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getSquareMedium() {
            return squareMedium;
        }

        public void setSquareMedium(String squareMedium) {
            this.squareMedium = squareMedium;
        }

        public String getSquareLarge() {
            return squareLarge;
        }

        public void setSquareLarge(String squareLarge) {
            this.squareLarge = squareLarge;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }


}


