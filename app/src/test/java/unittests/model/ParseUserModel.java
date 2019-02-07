package unittests.model;


import java.util.ArrayList;

/**
 * ParseUserModel Object class
 * @author android
 */
public class ParseUserModel {

    public ArrayList<ParseUserRecords> results;

    /**
     * @return The Object
     */
    public ArrayList<ParseUserRecords> getDetails() {
        return results;
    }

    /**
     * @param objectId
     */
    public void setDetails(ArrayList<ParseUserRecords> objectId) {
        this.results = objectId;
    }

    public class  ParseUserRecords{
        private String objectId;
        private String username;

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
         * @return The username
         */
        public String getUsername() {
            return username;
        }

        /**
         * @param username
         */
        public void setUsername(String username) {
            this.username = username;
        }

    }

}
