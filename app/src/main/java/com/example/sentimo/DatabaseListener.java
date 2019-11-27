package com.example.sentimo;

/**
 * Listener interface to control behaviour after network request return
 */
public interface DatabaseListener {
    void onSuccess();
    void onFailure();
}
