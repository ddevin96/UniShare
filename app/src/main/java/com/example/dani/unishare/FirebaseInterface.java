package com.example.dani.unishare;




import com.google.firebase.database.DatabaseReference;

public interface FirebaseInterface {
    void istance();
    void getUser();
    String getUserId();
    String getUserName();
    void logout();
    DatabaseReference istanceReference(String reference);
    DatabaseReference getChild(String reference, String childId);
    String getIdObject( DatabaseReference data);
    void addValue(DatabaseReference data, String idChild, Object object);
    void deleteVlaue(DatabaseReference data, String idChild);
}
