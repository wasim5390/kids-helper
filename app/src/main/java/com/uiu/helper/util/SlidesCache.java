package com.uiu.helper.util;

/**
 * Created by anastasia on 9/27/15.
 */

/**
 * Created by anastasia on 9/24/15.
 */
public class SlidesCache {
//    private static final String TAG = SlidesCache.class.getSimpleName();
//
//    private static SlidesCache sInstance = null;
//    Map<String,List<ParseObject>> slides;
//    Map<String,Date> slidesUpdated;
//
//
//    private SlidesCache() {
//        slides = new HashMap<>();
//        slidesUpdated = new HashMap<>();
//    }
//
//    public static SlidesCache getInstance() {
//        if (sInstance == null) {
//            synchronized (SlidesCache.class) {
//                if (sInstance == null) {
//                    sInstance = new SlidesCache();
//                }
//            }
//        }
//        return sInstance;
//    }
//
//    public Map<String,List<ParseObject>> getSlides() {
//        return slides;
//    }
//
//    public Date getLastUpdated(String iSlideId){
//        return slidesUpdated.get(iSlideId);
//    }
//
//    public List<ParseObject> getSlideDetails(String iSlideId){
//        return slides.get(iSlideId);
//    }
//
//    public void addSlide(String iSlideId, List<ParseObject> iListSlideObject, Date lastUpdated){
//        slides.put(iSlideId,  iListSlideObject);
//        slidesUpdated.put(iSlideId,lastUpdated);
//    }
//
//    public void addSlide(String iSlideId, ParseObject iListSlideObject, Date lastUpdated){
//        boolean exist=false;
//        if(slides.get(iSlideId)!=null ){
//            for(ParseObject obj :slides.get(iSlideId) ){
//                if(obj instanceof ParseContact && ((ParseContact)obj).equalContact((ParseContact)iListSlideObject)) {
//                    exist = true;
//                }
//                else if (obj instanceof App && ((App) obj).equalContact((App) iListSlideObject)){
//                    exist = true;
//                }
//                else if (obj instanceof Direction && ((Direction) obj).equalContact((Direction) iListSlideObject)){
//                    exist = true;
//                }
//            }
//
//            if(!exist) {
//                slides.get(iSlideId).add(iListSlideObject);
//            }
//        }
//        else{
//            List<ParseObject> lst= new ArrayList<>();
//            lst.add(iListSlideObject);
//            slides.put(iSlideId,lst );
//        }
//        slidesUpdated.put(iSlideId,lastUpdated);
//    }


}
