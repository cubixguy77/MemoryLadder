package com.MemoryLadder.Billing;

public interface BillingUpdatesListener {
    void onBillingSetupSuccess();
    void onBillingSetupFailed();
    void onUnlockChallenge();
}
