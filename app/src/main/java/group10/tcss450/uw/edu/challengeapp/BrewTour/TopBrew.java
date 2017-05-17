package group10.tcss450.uw.edu.challengeapp.BrewTour;


import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by chris on 5/17/17.
 */

/**
 * The top level class 'TopBrew' contains a nested classes: 'Glass', 'Style', 'Labels', 'Available'
 * The nested subclass 'Style' has its own nested subclass 'Category'
 * it is intended for subclasses to have a lowercase class name, to aid with reflection
 */
public class TopBrew implements Serializable {

    private String id;
    private String name;
    private String nameDisplay;
    private String description;
    private Double abv;
    private Double ibu;
    private String glasswareId;
    private String availableId;
    private String styleId;
    private String isOrganic;
    private labels labels;      //this is a subclass
    private String status;
    private String statusDisplay;
    private String createDate;
    private String updateDate;
    private glass glass;        //this is a subclass
    private available available;//this is a subclass
    private style style;        //this is a subclass
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    /**
     * Consider this a modified constructor, required to parse a JSON response.
     *
     * @param brew: a JSONObject representing the response from our API
     * @return: new TopBrew object, which contains nested objects
     */
    public static TopBrewery create(JSONObject brew) {
        TopBrewery result = new TopBrewery();

        try {
            Iterator<String> it = brew.keys();
            while (it.hasNext()) {
                String next = it.next();
                Class<?> doubleType = Double.class;
                Class<?> stringType = String.class;
                Field f = null;
                try {
                    f = result.getClass().getDeclaredField(next);
                } catch (NoSuchFieldException e) { //nothing to do here but go to next iteration
                    Log.e("TOPBREW", e.toString());
                    continue;
                }
                if (f.getType().isAssignableFrom(doubleType) || f.getType().isAssignableFrom(stringType)) {
                    if (brew.get(next) != null) {
                        try {
                            f.set(result, brew.get(next));
                        } catch (IllegalArgumentException e) {
                            /*
                             * This is caused by 'next' occasionally being recognized as an int when it needs to be a double
                             */
                            if (f.getType() == Double.class) {
                                double d = (double)((Integer)brew.get(next)).intValue();;
                                f.set(result, d);
                            }
                            Log.e("TOPBREW", e.getMessage());
                        }
                    }
                } else {
                    //nasty stuff
                    Class tempClass = f.getType();
                    Class[] carg = new Class[1];
                    carg[0] = JSONObject.class;
                    Constructor<?> constructor = tempClass.getConstructor(JSONObject.class);
                    constructor.setAccessible(true);
                    Object c = constructor.newInstance(brew.getJSONObject(next));
                    f.set(result, c);
                }
            }
        } catch (Exception e) {
            Log.e("TOPBREW", e.toString());
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

    public String getNameDisplay() {
        return nameDisplay;
    }

    public void setNameDisplay(String nameDisplay) {
        this.nameDisplay = nameDisplay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAbv() {
        return abv;
    }

    public void setAbv(Double abv) {
        this.abv = abv;
    }

    public Double getIbu() {
        return ibu;
    }

    public void setIbu(Double ibu) {
        this.ibu = ibu;
    }

    public String getGlasswareId() {
        return glasswareId;
    }

    public void setGlasswareId(String glasswareId) {
        this.glasswareId = glasswareId;
    }

    public String getAvailableId() {
        return availableId;
    }

    public void setAvailableId(String availableId) {
        this.availableId = availableId;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getIsOrganic() {
        return isOrganic;
    }

    public void setIsOrganic(String isOrganic) {
        this.isOrganic = isOrganic;
    }

    public labels getLabels() {
        return labels;
    }

    public void setLabels(labels labels) {
        this.labels = labels;
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

    public glass getGlass() {
        return glass;
    }

    public void setGlass(glass glass) {
        this.glass = glass;
    }

    public available getAvailable() {
        return available;
    }

    public void setAvailable(available available) {
        this.available = available;
    }

    public style getStyle() {
        return style;
    }

    public void setStyle(style style) {
        this.style = style;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


    public class style implements Serializable {

        private String id;
        private String categoryId;
        private category category;  //this is a subclass
        private String name;
        private String shortName;
        private String description;
        private Double ibuMin;
        private Double ibuMax;
        private Double abvMin;
        private Double abvMax;
        private Double srmMin;
        private Double srmMax;
        private Double ogMin;
        private Double fgMin;
        private Double fgMax;
        private String createDate;
        private String updateDate;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public style(JSONObject style) {

            try {
                Iterator<String> it = style.keys();
                while (it.hasNext()) {
                    String next = it.next();
                    Field f = null;
                    try {
                        f = this.getClass().getDeclaredField(next);
                    } catch (NoSuchFieldException e) {
                        Log.e("TOPBREW", e.toString());
                        continue;
                    }
                    Class<?> doubleType = Double.class;
                    Class<?> stringType = String.class;
                    if (f.getType().isAssignableFrom(doubleType) || f.getType().isAssignableFrom(stringType)) {
                        if (style.get(next) != null) {
                            try {
                                f.set(this, style.get(next));
                            } catch (IllegalArgumentException e) {
                            /*
                             * This is caused by distance occasionally being recognized as an int when it needs to be a double
                             */
                                if (f.getType() == Double.class) {
                                    double d = (double)((Integer)style.get(next)).intValue();;
                                    f.set(this, d);
                                }
                                Log.e("TOPBREW", e.getMessage());
                            }
                        }
                    } else {
                        //nasty stuff
                        Class tempClass = f.getType();
                        Class[] carg = new Class[1];
                        carg[0] = JSONObject.class;
                        Constructor<?> constructor = tempClass.getConstructor(JSONObject.class);
                        constructor.setAccessible(true);
                        Object c = constructor.newInstance(style.getJSONObject(next));
                        f.set(this, c);
                    }
                }
            } catch (Exception e) {
                Log.e("TOPBREW", e.toString());
            }
            //return result;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public category getCategory() {
            return category;
        }

        public void setCategory(category category) {
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Double getIbuMin() {
            return ibuMin;
        }

        public void setIbuMin(Double ibuMin) {
            this.ibuMin = ibuMin;
        }

        public Double getIbuMax() {
            return ibuMax;
        }

        public void setIbuMax(Double ibuMax) {
            this.ibuMax = ibuMax;
        }

        public Double getAbvMin() {
            return abvMin;
        }

        public void setAbvMin(Double abvMin) {
            this.abvMin = abvMin;
        }

        public Double getAbvMax() {
            return abvMax;
        }

        public void setAbvMax(Double abvMax) {
            this.abvMax = abvMax;
        }

        public Double getSrmMin() {
            return srmMin;
        }

        public void setSrmMin(Double srmMin) {
            this.srmMin = srmMin;
        }

        public Double getSrmMax() {
            return srmMax;
        }

        public void setSrmMax(Double srmMax) {
            this.srmMax = srmMax;
        }

        public Double getOgMin() {
            return ogMin;
        }

        public void setOgMin(Double ogMin) {
            this.ogMin = ogMin;
        }

        public Double getFgMin() {
            return fgMin;
        }

        public void setFgMin(Double fgMin) {
            this.fgMin = fgMin;
        }

        public Double getFgMax() {
            return fgMax;
        }

        public void setFgMax(Double fgMax) {
            this.fgMax = fgMax;
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

        public class category implements Serializable {

            private String id;
            private String name;
            private String createDate;
            private Map<String, Object> additionalProperties = new HashMap<String, Object>();

            public category (JSONObject category) {

                try {
                    Iterator<String> it = category.keys();
                    while (it.hasNext()) {
                        String next = it.next();
                        Class<?> stringType = String.class;
                        Field f = null;
                        try {
                            f = this.getClass().getDeclaredField(next);
                        } catch (NoSuchFieldException e) {
                            Log.e("TOPBREW", e.toString());
                            continue;
                        }
                        if (f.getType().isAssignableFrom(stringType)) {
                            try {
                                f.set(this, category.get(next));
                            } catch (IllegalArgumentException e) {
                                Log.e("TOPBREW", e.getMessage().toString());
                            }
                        } else {
                            //nasty stuff
                            Class tempClass = f.getType();
                            Class[] carg = new Class[1];
                            carg[0] = JSONObject.class;
                            Constructor<?> constructor = tempClass.getConstructor(JSONObject.class);
                            constructor.setAccessible(true);
                            Object c = constructor.newInstance(category.getJSONObject(next));
                            f.set(this,c);
                        }
                    }
                }
                catch (Exception e) {
                    Log.e("TOPBREW", e.toString());
                }
                //return result;
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

    }

    public class labels implements Serializable {

        private String icon;
        private String medium;
        private String large;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public labels (JSONObject labels) {

            try {
                Iterator<String> it = labels.keys();
                while (it.hasNext()) {
                    String next = it.next();
                    Class<?> stringType = String.class;
                    Field f = null;
                    try {
                        f = this.getClass().getDeclaredField(next);
                    } catch (NoSuchFieldException e) {
                        Log.e("TOPBREW", e.toString());
                        continue;
                    }
                    if (f.getType().isAssignableFrom(stringType)) {
                        try {
                            f.set(this, labels.get(next));
                        } catch (IllegalArgumentException e) {
                            Log.e("TOPBREW", e.getMessage().toString());
                        }
                    } else {
                        //nasty stuff
                        Class tempClass = f.getType();
                        Class[] carg = new Class[1];
                        carg[0] = JSONObject.class;
                        Constructor<?> constructor = tempClass.getConstructor(JSONObject.class);
                        constructor.setAccessible(true);
                        Object c = constructor.newInstance(labels.getJSONObject(next));
                        f.set(this,c);
                    }
                }
            }
            catch (Exception e) {
                Log.e("TOPBREW", e.toString());
            }
            //return result;
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

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }

    public class available implements Serializable {

        private String id;
        private String name;
        private String description;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public available (JSONObject available) {

            try {
                Iterator<String> it = available.keys();
                while (it.hasNext()) {
                    String next = it.next();
                    Class<?> stringType = String.class;
                    Field f = null;
                    try {
                        f = this.getClass().getDeclaredField(next);
                    } catch (NoSuchFieldException e) {
                        Log.e("TOPBREW", e.toString());
                        continue;
                    }
                    if (f.getType().isAssignableFrom(stringType)) {
                        try {
                            f.set(this, available.get(next));
                        } catch (IllegalArgumentException e) {
                            Log.e("TOPBREW", e.getMessage().toString());
                        }
                    } else {
                        //nasty stuff
                        Class tempClass = f.getType();
                        Class[] carg = new Class[1];
                        carg[0] = JSONObject.class;
                        Constructor<?> constructor = tempClass.getConstructor(JSONObject.class);
                        constructor.setAccessible(true);
                        Object c = constructor.newInstance(available.getJSONObject(next));
                        f.set(this,c);
                    }
                }
            }
            catch (Exception e) {
                Log.e("TOPBREW", e.toString());
            }
            //return result;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }

    public class glass implements Serializable {

        private String id;
        private String name;
        private String createDate;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public glass (JSONObject glass) {

            try {
                Iterator<String> it = glass.keys();
                while (it.hasNext()) {
                    String next = it.next();
                    Class<?> stringType = String.class;
                    Field f = null;
                    try {
                        f = this.getClass().getDeclaredField(next);
                    } catch (NoSuchFieldException e) {
                        Log.e("TOPBREW", e.toString());
                        continue;
                    }
                    if (f.getType().isAssignableFrom(stringType)) {
                        try {
                            f.set(this, glass.get(next));
                        } catch (IllegalArgumentException e) {
                            Log.e("TOPBREW", e.getMessage().toString());
                        }
                    } else {
                        //nasty stuff
                        Class tempClass = f.getType();
                        Class[] carg = new Class[1];
                        carg[0] = JSONObject.class;
                        Constructor<?> constructor = tempClass.getConstructor(JSONObject.class);
                        constructor.setAccessible(true);
                        Object c = constructor.newInstance(glass.getJSONObject(next));
                        f.set(this,c);
                    }
                }
            }
            catch (Exception e) {
                Log.e("TOPBREW", e.toString());
            }
            //return result;
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

}




