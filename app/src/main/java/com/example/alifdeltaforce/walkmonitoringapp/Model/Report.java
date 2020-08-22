package com.example.alifdeltaforce.walkmonitoringapp.Model;

import java.util.List;

/**
 * Created by Alif Delta Force on 11/24/2018.
 */

public class Report {


    /**
     * success : 1
     * massage : Get Detail
     * Report : [{"StepSum":"37","Email":"Acer@gmail.com","Date":"2018-11-24"}]
     */

    private int success;
    private String massage;
    private List<ReportBean> Report;

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

    public List<ReportBean> getReport() {
        return Report;
    }

    public void setReport(List<ReportBean> Report) {
        this.Report = Report;
    }

    public static class ReportBean {
        /**
         * StepSum : 37
         * Email : Acer@gmail.com
         * Date : 2018-11-24
         */

        private String StepSum;
        private String Email;
        private String Date;

        public String getStepSum() {
            return StepSum;
        }

        public void setStepSum(String StepSum) {
            this.StepSum = StepSum;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String Date) {
            this.Date = Date;
        }
    }
}
