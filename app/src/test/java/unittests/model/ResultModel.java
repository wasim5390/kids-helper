package unittests.model;


import java.util.ArrayList;

/**
 * ParseUserModel Object class
 * @author android
 */
public class ResultModel {

    public ArrayList<Records> results;

    /**
     * @return The Object
     */
    public ArrayList<Records> getTotalRecords() {
        return results;
    }

    /**
     * @param objectId
     */
    public void setTotalRecords(ArrayList<Records> objectId) {
        this.results = objectId;
    }

    public class Records {
        private String objectId;
        private String wiser_id;

        /**
         * @return The Object
         */
        public String getObjectId() {
            return objectId;
        }

        /**
         * @param objectId
         */
        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        /**
         * @return The wiser_id
         */
        public String getWiserId() {
            return wiser_id;
        }

        /**
         * @param wiser_id
         */
        public void setWiserId(String wiser_id) {
            this.wiser_id = wiser_id;
        }

    }

}
