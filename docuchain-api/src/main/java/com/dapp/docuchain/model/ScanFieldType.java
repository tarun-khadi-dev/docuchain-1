// // package com.dapp.docuchain.model;

// // public enum ScanFieldType {
// //     CERT_NO, ISSUE_PLACE, ISSUE_DATE, EXPIRY_DATE;
// // }

// package com.dapp.docuchain.model;

// public enum ScanFieldType {
//     CERT_NO,
//     ISSUE_PLACE,
//     ISSUE_DATE,
//     EXPIRY_DATE,
//     ISSUING_AUTHORITY; // ADDED THIS
// }

package com.dapp.docuchain.model;

public enum ScanFieldType {
    CERT_NO,
    ISSUE_PLACE,
    ISSUE_DATE,
    EXPIRY_DATE,
    ISSUING_AUTHORITY,

    // Add these new Vessel fields:
    // ... other fields
    PORT,
    DELIVERY_DATE,
    KEEL_LAID_DATE,
    IMO_NUMBER,
    LENGTH_OA,
    BREADTH,
    GROSS_TONNAGE_SUEZ,
    DWT_SUMMER
}
