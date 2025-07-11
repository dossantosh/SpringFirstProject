package com.dossantosh.springfirstproject.pref.models;

public interface PreferencesProjection {
    Long getUserId();
    boolean getEmailNotifications();
    boolean getSmsNotifications();
    String getTema();
    String getIdioma();
}
