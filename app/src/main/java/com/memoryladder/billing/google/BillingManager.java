package com.memoryladder.billing.google;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.memoryladder.billing.BillingUpdatesListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;

import java.util.Collections;
import java.util.List;

public class BillingManager implements PurchasesUpdatedListener {

    private static final String TAG = "BillingManager";

    private BillingClient billingClient;
    private final String sku;
    private Activity activity;
    private final BillingUpdatesListener updatesListener;

    public interface PurchaseCheckListener {
        void onPurchaseCheckResult(boolean isPurchased);
    }

    public BillingManager(Activity activity, String sku, final BillingUpdatesListener updatesListener) {
        this.activity = activity;
        this.updatesListener = updatesListener;

        billingClient = BillingClient
                .newBuilder(activity)
                .setListener(this)
                .build();

        this.sku = sku;
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
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

    public void isPurchased(final PurchaseCheckListener listener) {
        if (billingClient == null || !billingClient.isReady()) {
            listener.onPurchaseCheckResult(false);
        }

        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(),
                (billingResult, purchases) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Purchase purchase : purchases) {
                            if (purchase.getProducts().contains(sku)) {
                                listener.onPurchaseCheckResult(true);
                                return;
                            }
                        }
                    }
                    listener.onPurchaseCheckResult(false);
                }
        );
    }

    /* Used for debugging only
    public void clearAllPurchases() {
        Purchase.PurchasesResult purchases = billingClient.queryPurchases(BillingClient.ProductType.INAPP);
        if (purchases == null || purchases.getResponseCode() != BillingClient.BillingResponse.OK || purchases.getPurchasesList() == null || purchases.getPurchasesList().size() == 0) {
            return;
        }

        for (Purchase purchase : purchases.getPurchasesList()) {
            billingClient.consumeAsync(purchase.getPurchaseToken(), null);
        }
    }
    */

    public void launchPurchaseDialog() {

        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(Collections.singletonList(QueryProductDetailsParams.Product.newBuilder()
                                .setProductId(sku)
                                .setProductType(BillingClient.ProductType.INAPP)
                                .build()))
                        .build();

        billingClient.queryProductDetailsAsync(
                queryProductDetailsParams,
                (billingResult, productDetailsList) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (ProductDetails productDetails : productDetailsList) {
                            if (productDetails.getProductId().equals(sku)) {
                                List<BillingFlowParams.ProductDetailsParams> productDetailsParamsList = Collections.singletonList(
                                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                                .setProductDetails(productDetails)
                                                .build());

                                BillingFlowParams flowParams = BillingFlowParams
                                        .newBuilder()
                                        .setProductDetailsParamsList(productDetailsParamsList)
                                        .build();

                                billingClient.launchBillingFlow(activity, flowParams);

                                break;
                            }
                        }
                    }
                }
        );
    }

    public void destroy() {
        if (billingClient != null && billingClient.isReady()) {
            billingClient.endConnection();
            billingClient = null;
            activity = null;
        }
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                if (purchase.getProducts().contains(sku)) {
                    updatesListener.onUnlockChallenge();
                }
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.i(TAG, "onPurchasesUpdated() - user cancelled the purchase flow - skipping");
        } else {
            Log.w(TAG, "onPurchasesUpdated() got unknown resultCode: " + billingResult.getResponseCode());
        }
    }
}
