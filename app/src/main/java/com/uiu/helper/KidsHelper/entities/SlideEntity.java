package com.uiu.helper.KidsHelper.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.uiu.helper.KidsHelper.constants.AppConstants;
import com.uiu.helper.KidsHelper.constants.KeyConstants;
import com.uiu.helper.KidsHelper.framework.utilities.JsonUtility;


import java.util.ArrayList;

public class SlideEntity extends BaseEntity implements Comparable {

    private String name;
    private String userId;
    private int type, serial;
    public int order;
    public String slideId;

    public void setName(String slideName) {
        this.name = slideName;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public String getSlideId() {
        return slideId;
    }

    public void setSlideId(String slideId) {
        this.slideId = slideId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public void addAllSlides(ArrayList<SlideEntity> entities) {
        list = entities;
    }

    public ArrayList<SlideEntity> getAllSlides() {
        return list;
    }

    public void set(String jsonString) {

        JsonArray jsonArray = JsonUtility.parseToJsonArray(jsonString);
        for (JsonElement jsonElement : jsonArray) {
            JsonObject object = jsonElement.getAsJsonObject();
            SlideEntity entity = new SlideEntity();
            entity.setName(JsonUtility.getString(object, KeyConstants.KEY_NAME));
            entity.setUserId(JsonUtility.getString(object, KeyConstants.KEY_ID));
            entity.setType(JsonUtility.getInt(object, KeyConstants.KEY_TYPE));
            entity.setSerial(JsonUtility.getInt(object, KeyConstants.KEY_SERIAL));
            entity.setSlideId(JsonUtility.getString(object, KeyConstants.KEY_ID));
            list.add(entity);
        }
    }

    public String getBaseSlidesRequest() {
        JsonObject requestJson = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (SlideEntity entity : getAllSlides()) {
            JsonObject object = new JsonObject();
            object.addProperty(KeyConstants.KEY_NAME, entity.getName());
            object.addProperty(KeyConstants.KEY_TYPE, entity.getType());
            object.addProperty(KeyConstants.KEY_USER_ID, entity.getUserId());
            object.addProperty(KeyConstants.KEY_SERIAL, entity.getSerial());
            jsonArray.add(object);
        }
        requestJson.add(KeyConstants.KEY_SLIDE, jsonArray);
        return requestJson.toString();
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param otherSlide the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Object otherSlide) {
        return Integer.valueOf(getType())-Integer.valueOf(((SlideEntity) otherSlide).getType());
    }
    public Class getClss(int slideType){
        switch (slideType) {
//            case AppConstants.SLIDE_TYPE_HOME:
//                return HomeSlideFragment.class;
//            case AppConstants.SLIDE_TYPE_PEOPLE:
//                return FavouritePeopleFragment.class;
        }
        return null;
    }
}
