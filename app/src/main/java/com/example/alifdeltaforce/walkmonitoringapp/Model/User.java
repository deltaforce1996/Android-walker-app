package com.example.alifdeltaforce.walkmonitoringapp.Model;

/**
 * Created by Alif Delta Force on 7/4/2018.
 */

public class User {


    /**
     * success : 1
     * massage : Login success
     * userDetail : {"userID":"20","userName":"cccc","userEmail":"ccc","userPass":"cccc","userAge":"2","userGender":"FEMALE","userLevel":"1","userImg":null,"deviceID":null}
     */

    private int success;
    private String massage;
    private UserDetailBean userDetail;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public UserDetailBean getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetailBean userDetail) {
        this.userDetail = userDetail;
    }

    public static class UserDetailBean {
        /**
         * userID : 20
         * userName : cccc
         * userEmail : ccc
         * userPass : cccc
         * userAge : 2
         * userGender : FEMALE
         * userLevel : 1
         * userImg : null
         * deviceID : null
         */

        private String userID;
        private String userName;
        private String userEmail;
        private String userPass;
        private String userAge;
        private String userGender;
        private String userLevel;
        private String userImg;
        private String deviceID;

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public String getUserPass() {
            return userPass;
        }

        public void setUserPass(String userPass) {
            this.userPass = userPass;
        }

        public String getUserAge() {
            return userAge;
        }

        public void setUserAge(String userAge) {
            this.userAge = userAge;
        }

        public String getUserGender() {
            return userGender;
        }

        public void setUserGender(String userGender) {
            this.userGender = userGender;
        }

        public String getUserLevel() {
            return userLevel;
        }

        public void setUserLevel(String userLevel) {
            this.userLevel = userLevel;
        }

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String userImg) {
            this.userImg = userImg;
        }

        public String getDeviceID() {
            return deviceID;
        }

        public void setDeviceID(String deviceID) {
            this.deviceID = deviceID;
        }
    }
}