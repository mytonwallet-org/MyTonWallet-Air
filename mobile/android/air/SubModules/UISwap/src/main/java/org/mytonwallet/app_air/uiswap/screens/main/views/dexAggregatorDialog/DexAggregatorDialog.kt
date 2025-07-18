package org.mytonwallet.app_air.uiswap.screens.main.views.dexAggregatorDialog

import android.content.Context
import org.mytonwallet.app_air.uicomponents.widgets.dialog.WDialog
import org.mytonwallet.app_air.walletcontext.R
import org.mytonwallet.app_air.walletcontext.helpers.LocaleController
import org.mytonwallet.app_air.walletcore.moshi.IApiToken
import org.mytonwallet.app_air.walletcore.moshi.MApiSwapDexLabel
import org.mytonwallet.app_air.walletcore.moshi.MApiSwapEstimateVariant

class DexAggregatorDialog {
    companion object {
        fun create(
            context: Context,
            fromToken: IApiToken,
            toToken: IApiToken,
            variants: List<MApiSwapEstimateVariant>,
            bestDex: MApiSwapDexLabel,
            selectedDex: MApiSwapDexLabel,
            onSelect: (MApiSwapDexLabel) -> Unit
        ): WDialog {
            return WDialog(
                DexAggregatorContentView(
                    context,
                    fromToken,
                    toToken,
                    variants,
                    bestDex,
                    selectedDex,
                    onSelect
                ),
                WDialog.Config(
                    title = LocaleController.getString(R.string.DexAggregator_Title),
                )
            )
        }
    }
}
