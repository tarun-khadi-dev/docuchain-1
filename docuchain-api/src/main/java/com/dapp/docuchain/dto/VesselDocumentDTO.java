// // // package com.dapp.docuchain.dto;

// // // import java.util.Date;

// // // public class VesselDocumentDTO {
// // //     private Date deliveryDate;
// // //     private String deliveryDateString;
// // //     private Date keelLaidDate;
// // //     private String keelLaidDateString;
// // //     private String imoNumber;
// // //     private String lengthOA;
// // //     private String breadth;
// // //     private String grossTonnageSuez;
// // //     private String dwtSummer;

// // //     // Generate Getters and Setters for all fields...
// // // }

// // package com.dapp.docuchain.dto;

// // import java.util.Date;

// // public class VesselDocumentDTO {

// //     private Date deliveryDate;
// //     private String deliveryDateString;
// //     private Date keelLaidDate;
// //     private String keelLaidDateString;
// //     private String imoNumber;
// //     private String lengthOA;
// //     private String breadth;
// //     private String grossTonnageSuez;
// //     private String dwtSummer;
// //     // Inside VesselDocumentDTO.java
// //     private String port;
// //     private String nationality;

// // public String getPort() {
// //     return port;
// // }

// // public void setPort(String port) {
// //     this.port = port;
// // }

// //     // --- Getters and Setters ---

// //     public Date getDeliveryDate() {
// //         return deliveryDate;
// //     }

// //     public void setDeliveryDate(Date deliveryDate) {
// //         this.deliveryDate = deliveryDate;
// //     }

// //     public String getDeliveryDateString() {
// //         return deliveryDateString;
// //     }

// //     public void setDeliveryDateString(String deliveryDateString) {
// //         this.deliveryDateString = deliveryDateString;
// //     }

// //     public Date getKeelLaidDate() {
// //         return keelLaidDate;
// //     }

// //     public void setKeelLaidDate(Date keelLaidDate) {
// //         this.keelLaidDate = keelLaidDate;
// //     }

// //     public String getKeelLaidDateString() {
// //         return keelLaidDateString;
// //     }

// //     public void setKeelLaidDateString(String keelLaidDateString) {
// //         this.keelLaidDateString = keelLaidDateString;
// //     }

// //     public String getImoNumber() {
// //         return imoNumber;
// //     }

// //     public void setImoNumber(String imoNumber) {
// //         this.imoNumber = imoNumber;
// //     }

// //     public String getLengthOA() {
// //         return lengthOA;
// //     }

// //     public void setLengthOA(String lengthOA) {
// //         this.lengthOA = lengthOA;
// //     }

// //     public String getBreadth() {
// //         return breadth;
// //     }

// //     public void setBreadth(String breadth) {
// //         this.breadth = breadth;
// //     }

// //     public String getGrossTonnageSuez() {
// //         return grossTonnageSuez;
// //     }

// //     public void setGrossTonnageSuez(String grossTonnageSuez) {
// //         this.grossTonnageSuez = grossTonnageSuez;
// //     }

// //     public String getDwtSummer() {
// //         return dwtSummer;
// //     }

// //     public void setDwtSummer(String dwtSummer) {
// //         this.dwtSummer = dwtSummer;
// //     }
// // }

// package com.dapp.docuchain.dto;

// import java.util.Date;

// public class VesselDocumentDTO {

//     private Date deliveryDate;
//     private String deliveryDateString;
//     private Date keelLaidDate;
//     private String keelLaidDateString;
//     private String imoNumber;
//     private String lengthOA;
//     private String breadth;
//     private String grossTonnageSuez;
//     private String dwtSummer;
//     private String port;
//     private String nationality; // Add this

//     // --- Getters and Setters ---

//     public String getPort() { return port; }
//     public void setPort(String port) { this.port = port; }

//     public String getNationality() { return nationality; } // Add this
//     public void setNationality(String nationality) { this.nationality = nationality; } // Add this

//     public Date getDeliveryDate() { return deliveryDate; }
//     public void setDeliveryDate(Date deliveryDate) { this.deliveryDate = deliveryDate; }

//     public String getDeliveryDateString() { return deliveryDateString; }
//     public void setDeliveryDateString(String deliveryDateString) { this.deliveryDateString = deliveryDateString; }

//     public Date getKeelLaidDate() { return keelLaidDate; }
//     public void setKeelLaidDate(Date keelLaidDate) { this.keelLaidDate = keelLaidDate; }

//     public String getKeelLaidDateString() { return keelLaidDateString; }
//     public void setKeelLaidDateString(String keelLaidDateString) { this.keelLaidDateString = keelLaidDateString; }

//     public String getImoNumber() { return imoNumber; }
//     public void setImoNumber(String imoNumber) { this.imoNumber = imoNumber; }

//     public String getLengthOA() { return lengthOA; }
//     public void setLengthOA(String lengthOA) { this.lengthOA = lengthOA; }

//     public String getBreadth() { return breadth; }
//     public void setBreadth(String breadth) { this.breadth = breadth; }

//     public String getGrossTonnageSuez() { return grossTonnageSuez; }
//     public void setGrossTonnageSuez(String grossTonnageSuez) { this.grossTonnageSuez = grossTonnageSuez; }

//     public String getDwtSummer() { return dwtSummer; }
//     public void setDwtSummer(String dwtSummer) { this.dwtSummer = dwtSummer; }
// }

package com.dapp.docuchain.dto;

import java.util.Date;

public class VesselDocumentDTO {

    private Date deliveryDate;
    private String deliveryDateString;
    private Date keelLaidDate;
    private String keelLaidDateString;
    private String imoNumber;
    private String lengthOA;
    private String breadth;
    private String grossTonnageSuez;
    private String dwtSummer;
    private String port;
    private String nationality;

    // Add these new fields
    private String vesselName;
    private String builderYard;

    // --- New Getters and Setters ---

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getBuilderYard() {
        return builderYard;
    }

    public void setBuilderYard(String builderYard) {
        this.builderYard = builderYard;
    }

    // --- Existing Getters and Setters ---

    public String getPort() { return port; }
    public void setPort(String port) { this.port = port; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public Date getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(Date deliveryDate) { this.deliveryDate = deliveryDate; }

    public String getDeliveryDateString() { return deliveryDateString; }
    public void setDeliveryDateString(String deliveryDateString) { this.deliveryDateString = deliveryDateString; }

    public Date getKeelLaidDate() { return keelLaidDate; }
    public void setKeelLaidDate(Date keelLaidDate) { this.keelLaidDate = keelLaidDate; }

    public String getKeelLaidDateString() { return keelLaidDateString; }
    public void setKeelLaidDateString(String keelLaidDateString) { this.keelLaidDateString = keelLaidDateString; }

    public String getImoNumber() { return imoNumber; }
    public void setImoNumber(String imoNumber) { this.imoNumber = imoNumber; }

    public String getLengthOA() { return lengthOA; }
    public void setLengthOA(String lengthOA) { this.lengthOA = lengthOA; }

    public String getBreadth() { return breadth; }
    public void setBreadth(String breadth) { this.breadth = breadth; }

    public String getGrossTonnageSuez() { return grossTonnageSuez; }
    public void setGrossTonnageSuez(String grossTonnageSuez) { this.grossTonnageSuez = grossTonnageSuez; }

    public String getDwtSummer() { return dwtSummer; }
    public void setDwtSummer(String dwtSummer) { this.dwtSummer = dwtSummer; }
}
