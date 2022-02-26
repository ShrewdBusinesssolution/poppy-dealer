package com.shrewd.poppydealers.model;

public class User {

    int id;
    String name;
    String mobile;
    String email;
    String yearlyExperience;
    String monthlyExperience;
    String address;
    String district;
    String state;
    String pincode;
    String landline;
    String business;
    String status;
    Boolean kyc_status;
    String pancard;
    String gst;
    String pancardDoc;
    String gstDoc;

    public User() {

    }

    public User(String name, String mobile, String email, String yearlyExperience, String monthlyExperience, String address, String district, String state, String pincode, String landline, String business, Boolean kyc_status) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.yearlyExperience = yearlyExperience;
        this.monthlyExperience = monthlyExperience;
        this.address = address;
        this.district = district;
        this.state = state;
        this.pincode = pincode;
        this.landline = landline;
        this.business = business;
        this.kyc_status =kyc_status;

    }
    public User(String name, String mobile, String email, String yearlyExperience, String monthlyExperience, String address, String district, String state, String pincode, String landline, String business, String pancard, String pancardDoc, String gst, String gstDoc) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.yearlyExperience = yearlyExperience;
        this.monthlyExperience = monthlyExperience;
        this.address = address;
        this.district = district;
        this.state = state;
        this.pincode = pincode;
        this.landline = landline;
        this.business = business;
        this.pancard = pancard;
        this.gst = gst;
        this.pancardDoc = pancardDoc;
        this.gstDoc = gstDoc;
    }

    public User(int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getYearlyExperience() {
        return yearlyExperience;
    }

    public void setYearlyExperience(String yearlyExperience) {
        this.yearlyExperience = yearlyExperience;
    }

    public String getMonthlyExperience() {
        return monthlyExperience;
    }

    public void setMonthlyExperience(String monthlyExperience) {
        this.monthlyExperience = monthlyExperience;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getPancard() {
        return pancard;
    }

    public void setPancard(String pancard) {
        this.pancard = pancard;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getPancardDoc() {
        return pancardDoc;
    }

    public void setPancardDoc(String pancardDoc) {
        this.pancardDoc = pancardDoc;
    }

    public String getGstDoc() {
        return gstDoc;
    }

    public void setGstDoc(String gstDoc) {
        this.gstDoc = gstDoc;
    }


    public Boolean getKyc_status() {
        return kyc_status;
    }

    public void setKyc_status(Boolean kyc_status) {
        this.kyc_status = kyc_status;
    }
}
