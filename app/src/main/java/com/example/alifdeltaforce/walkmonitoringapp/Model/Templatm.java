package com.example.alifdeltaforce.walkmonitoringapp.Model;

import java.util.List;

/**
 * Created by Alif Delta Force on 11/23/2018.
 */

public class Templatm {


    /**
     * success : 1
     * massage : Get Detail save
     * userDetail : [{"LevelNumber":"1","StepNumber":"8","CounterTime":"1500","Distance":"3"},{"LevelNumber":"2","StepNumber":"15","CounterTime":"2000","Distance":"5"},{"LevelNumber":"3","StepNumber":"22","CounterTime":"2500","Distance":"12"},{"LevelNumber":"4","StepNumber":"29","CounterTime":"3000","Distance":"15"}]
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
         * LevelNumber : 1
         * StepNumber : 8
         * CounterTime : 1500
         * Distance : 3
         */

        private String LevelNumber;
        private String StepNumber;
        private String CounterTime;
        private String Distance;

        public String getLevelNumber() {
            return LevelNumber;
        }

        public void setLevelNumber(String LevelNumber) {
            this.LevelNumber = LevelNumber;
        }

        public String getStepNumber() {
            return StepNumber;
        }

        public void setStepNumber(String StepNumber) {
            this.StepNumber = StepNumber;
        }

        public String getCounterTime() {
            return CounterTime;
        }

        public void setCounterTime(String CounterTime) {
            this.CounterTime = CounterTime;
        }

        public String getDistance() {
            return Distance;
        }

        public void setDistance(String Distance) {
            this.Distance = Distance;
        }
    }
}
