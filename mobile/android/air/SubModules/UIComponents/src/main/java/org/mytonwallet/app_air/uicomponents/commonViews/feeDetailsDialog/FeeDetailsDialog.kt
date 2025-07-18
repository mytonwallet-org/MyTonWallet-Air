package org.mytonwallet.app_air.uicomponents.commonViews.feeDetailsDialog

import android.content.Context
import org.mytonwallet.app_air.uicomponents.widgets.dialog.WDialog
import org.mytonwallet.app_air.walletcontext.R
import org.mytonwallet.app_air.walletcontext.helpers.LocaleController
import org.mytonwallet.app_air.walletcore.models.ExplainedTransferFee
import org.mytonwallet.app_air.walletcore.moshi.IApiToken

class FeeDetailsDialog {
    companion object {
        fun create(
            context: Context,
            token: IApiToken,
            feeDetails: ExplainedTransferFee,
            onClosePressed: () -> Unit
        ): WDialog {
            return WDialog(
                FeeDetailsContentView(context, token, feeDetails, onClosePressed),
                WDialog.Config(
                    title = LocaleController.getString(R.string.FeeDetails_Title),
                )
            )
        }
    }
}
