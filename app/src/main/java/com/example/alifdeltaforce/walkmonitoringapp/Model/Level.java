package com.example.alifdeltaforce.walkmonitoringapp.Model;

import java.util.List;

/**
 * Created by Alif Delta Force on 11/23/2018.
 */

public class Level {


    /**
     * success : 1
     * massage : Get Detail save
     * userDetail : [{"Number":"6","Email":"Acer@gmail.com","StepsNumber":"0","Time":"0","Distance":"0","Date":"2018-11-24","Level":"1"}]
     */

    private int success;
    private String massage;
    private List<UserDetailBean> userDetail;

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

    public List<UserDetailBean> getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(List<UserDetailBean> userDetail) {
        this.userDetail = userDetail;
    }

    public static class UserDetailBean {
        /**
         * Number : 6
         * Email : Acer@gmail.com
         * StepsNumber : 0
         * Time : 0
         * Distance : 0
         * Date : 2018-11-24
         * Level : 1
         */

        private String Number;
        private String Email;
        private String StepsNumber;
        private String Time;
        private String Distance;
        private String Date;
        private String Level;

        public String getNumber() {
            return Number;
        }

        public void setNumber(String Number) {
            this.Number = Number;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getStepsNumber() {
            return StepsNumber;
        }

        public void setStepsNumber(String StepsNumber) {
            this.StepsNumber = StepsNumber;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        public String getDistance() {
            return Distance;
        }

        public void setDistance(String Distance) {
            this.Distance = Distance;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String Date) {
            this.Date = Date;
        }

        public String getLevel() {
            return Level;
        }

        public void setLevel(String Level) {
            this.Level = Level;
        }
    }
}
