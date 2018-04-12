package com.MemoryLadder.Billing;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import java.util.List;

public class BillingManager implements PurchasesUpdatedListener {

    private static final String TAG = "BillingManager";

    private BillingClient billingClient;
    private String sku;
    private Activity activity;
    private final BillingUpdatesListener updatesListener;

    public interface BillingUpdatesListener {
        void onBillingSetupSuccess();
        void onBillingSetupFailed();
        void onUnlockChallenge();
    }

    public BillingManager(Activity activity, String sku, final BillingUpdatesListener updatesListener) {
        this.activity = activity;
        this.updatesListener = updatesListener;

        billingClient = BillingClient.newBuilder(activity).setListener(this).build();
        this.sku = sku;
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    updatesListener.onBillingSetupSuccess();
                }
                else {
                    updatesListener.onBillingSetupFailed();
                }
            }
            @Override
            public void onBillingServiceDisconnected() {

            }
        });
    }

    public boolean isPurchased() {
        if (billingClient == null || !billingClient.isReady()) {
            return false;
        }

        Purchase.PurchasesResult purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
        if (purchases == null || purchases.getResponseCode() != BillingClient.BillingResponse.OK || purchases.getPurchasesList() == null || purchases.getPurchasesList().size() == 0) {
            return false;
        }

        for (Purchase purchase : purchases.getPurchasesList()) {
            if (purchase.getSku().equals(sku)) {
                return true;
            }
        }

        return false;
    }

    /* Used for debugging only
    public void clearAllPurchases() {
        Purchase.PurchasesResult purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
        if (purchases == null || purchases.getResponseCode() != BillingClient.BillingResponse.OK || purchases.getPurchasesList() == null || purchases.getPurchasesList().size() == 0) {
            return;
        }

        for (Purchase purchase : purchases.getPurchasesList()) {
            billingClient.consumeAsync(purchase.getPurchaseToken(), null);
        }
    }
    */

    public void launchPurchaseDialog() {
        BillingFlowParams flowParams = BillingFlowParams.newBuilder().setSku(sku).setType(BillingClient.SkuType.INAPP).build();
        billingClient.launchBillingFlow(activity, flowParams);
    }

    public void destroy() {
        if (billingClient != null && billingClient.isReady()) {
            billingClient.endConnection();
            billingClient = null;
            activity = null;
        }
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                if (purchase.getSku().equals(sku)) {
                    updatesListener.onUnlockChallenge();
                }
            }

        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            Log.i(TAG, "onPurchasesUpdated() - user cancelled the purchase flow - skipping");
        } else {
            Log.w(TAG, "onPurchasesUpdated() got unknown resultCode: " + responseCode);
        }
    }
}
