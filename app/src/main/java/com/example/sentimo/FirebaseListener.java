package com.example.sentimo;

public interface FirebaseListener {
/**
 * Listener interface to control behaviour after network request return
 */
    void onSuccess();
    void onFailure();
}
