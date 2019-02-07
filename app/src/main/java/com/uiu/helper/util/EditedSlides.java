package com.uiu.helper.util;

import java.util.HashSet;

/**
 * Created by Dor on 9/3/15.
 */
public class EditedSlides {

    private static HashSet<String> editedSlides;

    /**
     * Mark slide as it is in edit mode
     */
    public static synchronized void setEditedSlide(String slideId) {
        if (editedSlides == null) {
            editedSlides = new HashSet<>();
        }

        editedSlides.add(slideId);
    }

    /**
     * Check if slide has been edited, and remove from the map
     */
    public static synchronized boolean getAndRemove(String slideId) {
        return editedSlides != null && editedSlides.remove(slideId);
    }

    /**
     * Clear all edited slides
     */
    public static synchronized void clear() {
        if (editedSlides != null) {
            editedSlides.clear();
        }
    }
}
