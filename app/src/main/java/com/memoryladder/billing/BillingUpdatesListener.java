package com.memoryladder.billing;

public interface BillingUpdatesListener {
    void onBillingSetupSuccess();
    void onBillingSetupFailed();
    void onUnlockChallenge();
}
