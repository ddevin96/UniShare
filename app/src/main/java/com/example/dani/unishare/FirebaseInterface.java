package com.example.dani.unishare;

import com.google.firebase.database.DatabaseReference;

/**
 * <p>Interfaccia usata per l'interazione del Sistema con il database.</p>
 * <p>Le prime quattro firme sono pensate per l'interazione con il
 * riferimento al database "FirebaseAuth" (Autentication)</p>
 * <p>Le ultime sette sono pensate per l'accesso ai campi del degli oggetti nel database,
 * dunque l'iserimento, la modifica e l'emiminazione di dati,
 * tramite il riferimento al database "DatabaseReference".</p>
 */
public interface FirebaseInterface {
  void istance();

  void getUser();

  String getUserId();

  String getUserName();

  void logout();

  DatabaseReference istanceReference(String reference);

  DatabaseReference getChild(String reference, String childId);

  String getIdObject(DatabaseReference data);

  void addValue(DatabaseReference data, String idChild, Object object);

  void addValue(DatabaseReference data, Object object);

  void deleteValue(DatabaseReference data, String idChild);

  void deleteValue(DatabaseReference data);
}
