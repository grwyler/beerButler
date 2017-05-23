package group10.tcss450.uw.edu.challengeapp.BeerList;


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
    private String abv;
    private String ibu;
    private Integer glasswareId;
    private Integer availableId;
    private Integer styleId;
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
    public static TopBrew create(JSONObject brew) {
        TopBrew result = new TopBrew();

        try {
            Iterator<String> it = brew.keys();
            while (it.hasNext()) {
                String next = it.next();
                Class<?> doubleType = Double.class;
                Class<?> stringType = String.class;
                Class<?> intType = Integer.class;
                Field f = null;
                try {
                    f = result.getClass().getDeclaredField(next);
                } catch (NoSuchFieldException e) { //nothing to do here but go to next iteration
                    Log.e("TOPBREW", e.toString());
                    continue;
                }
                if (f.getType().isAssignableFrom(doubleType) || f.getType().isAssignableFrom(stringType)
                || f.getType().isAssignableFrom(intType)) {
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
                            //Log.e("TOPBREW", e.getMessage());
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
            //Log.e("TOPBREW", e.toString());
            Log.e("TOPBREW", "exception", e);
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

    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public String getIbu() {
        return ibu;
    }

    public void setIbu(String ibu) {
        this.ibu = ibu;
    }

    public Integer getGlasswareId() {
        return glasswareId;
    }

    public void setGlasswareId(Integer glasswareId) {
        this.glasswareId = glasswareId;
    }

    public Integer getAvailableId() {
        return availableId;
    }

    public void setAvailableId(Integer availableId) {
        this.availableId = availableId;
    }

    public Integer getStyleId() {
        return styleId;
    }

    public void setStyleId(Integer styleId) {
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


    public static class style implements Serializable {

        private Integer id;
        private Integer categoryId;
        private category category;  //this is a subclass
        private String name;
        private String shortName;
        private String description;
        private String ibuMin;
        private String ibuMax;
        private String abvMin;
        private String abvMax;
        private String srmMin;
        private String srmMax;
        private String ogMin;
        private String fgMin;
        private String fgMax;
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
                    Class<?> intType = Integer.class;
                    if (f.getType().isAssignableFrom(doubleType) || f.getType().isAssignableFrom(stringType)
                            || f.getType().isAssignableFrom(intType)) {
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

        public style() {

        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
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

        public String getIbuMin() {
            return ibuMin;
        }

        public void setIbuMin(String ibuMin) {
            this.ibuMin = ibuMin;
        }

        public String getIbuMax() {
            return ibuMax;
        }

        public void setIbuMax(String ibuMax) {
            this.ibuMax = ibuMax;
        }

        public String getAbvMin() {
            return abvMin;
        }

        public void setAbvMin(String abvMin) {
            this.abvMin = abvMin;
        }

        public String getAbvMax() {
            return abvMax;
        }

        public void setAbvMax(String abvMax) {
            this.abvMax = abvMax;
        }

        public String getSrmMin() {
            return srmMin;
        }

        public void setSrmMin(String srmMin) {
            this.srmMin = srmMin;
        }

        public String getSrmMax() {
            return srmMax;
        }

        public void setSrmMax(String srmMax) {
            this.srmMax = srmMax;
        }

        public String getOgMin() {
            return ogMin;
        }

        public void setOgMin(String ogMin) {
            this.ogMin = ogMin;
        }

        public String getFgMin() {
            return fgMin;
        }

        public void setFgMin(String fgMin) {
            this.fgMin = fgMin;
        }

        public String getFgMax() {
            return fgMax;
        }

        public void setFgMax(String fgMax) {
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

        public static class category implements Serializable {

            private Integer id;
            private String name;
            private String createDate;
            private Map<String, Object> additionalProperties = new HashMap<String, Object>();

            public category (JSONObject category) {

                try {
                    Iterator<String> it = category.keys();
                    while (it.hasNext()) {
                        String next = it.next();
                        Class<?> stringType = String.class;
                        Class<?> intType = Integer.class;
                        Field f = null;
                        try {
                            f = this.getClass().getDeclaredField(next);
                        } catch (NoSuchFieldException e) {
                            Log.e("TOPBREW", e.toString());
                            continue;
                        }
                        if (f.getType().isAssignableFrom(stringType) || f.getType().isAssignableFrom(intType)) {
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
            public category() {

            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
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

    public static class labels implements Serializable {

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

        public labels(){

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

    public static class available implements Serializable {

        private Integer id;
        private String name;
        private String description;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public available (JSONObject available) {

            try {
                Iterator<String> it = available.keys();
                while (it.hasNext()) {
                    String next = it.next();
                    Class<?> stringType = String.class;
                    Class<?> intType = Integer.class;
                    Field f = null;
                    try {
                        f = this.getClass().getDeclaredField(next);
                    } catch (NoSuchFieldException e) {
                        Log.e("TOPBREW", e.toString());
                        continue;
                    }
                    if (f.getType().isAssignableFrom(stringType) || f.getType().isAssignableFrom(intType)) {
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

        public available(){

        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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

    public static class glass implements Serializable {

        private Integer id;
        private String name;
        private String createDate;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public glass (JSONObject glass) {

            try {
                Iterator<String> it = glass.keys();
                while (it.hasNext()) {
                    String next = it.next();
                    Class<?> stringType = String.class;
                    Class<?> intType = Integer.class;
                    Field f = null;
                    try {
                        f = this.getClass().getDeclaredField(next);
                    } catch (NoSuchFieldException e) {
                        Log.e("TOPBREW", e.toString());
                        continue;
                    }
                    if (f.getType().isAssignableFrom(stringType) || f.getType().isAssignableFrom(intType)) {
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

        public glass(){

        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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




