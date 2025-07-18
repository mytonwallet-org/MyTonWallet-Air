package org.mytonwallet.app_air.uisend.send.lauouts

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import me.vkryl.android.AnimatorUtils
import me.vkryl.android.animatorx.BoolAnimator
import org.mytonwallet.app_air.icons.R
import org.mytonwallet.app_air.uicomponents.drawable.WRippleDrawable
import org.mytonwallet.app_air.uicomponents.extensions.dp
import org.mytonwallet.app_air.uicomponents.extensions.getTextFromClipboard
import org.mytonwallet.app_air.uicomponents.extensions.setPaddingDp
import org.mytonwallet.app_air.uicomponents.extensions.setTextIfDiffer
import org.mytonwallet.app_air.uicomponents.helpers.WFont
import org.mytonwallet.app_air.uicomponents.helpers.typeface
import org.mytonwallet.app_air.uicomponents.widgets.WThemedView
import org.mytonwallet.app_air.walletcontext.helpers.LocaleController
import org.mytonwallet.app_air.walletcontext.theme.WColor
import org.mytonwallet.app_air.walletcontext.theme.color
import org.mytonwallet.app_air.walletcontext.theme.colorStateList

class AddressInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attrs, defStyle), WThemedView {
    private val buttonsVisible = BoolAnimator(
        220L,
        AnimatorUtils.DECELERATE_INTERPOLATOR,
        true
    ) { state, value, changed, _ ->
        pasteTextView.alpha = value
        qrScanImageView.alpha = value
        if (changed) {
            pasteTextView.isEnabled = state == BoolAnimator.State.TRUE
            pasteTextView.isVisible = state != BoolAnimator.State.FALSE
            qrScanImageView.isEnabled = state == BoolAnimator.State.TRUE
            qrScanImageView.isVisible = state != BoolAnimator.State.FALSE
        }
    }

    val editTextView = AppCompatEditText(context).apply {
        background = null
        hint =
            LocaleController.getString(org.mytonwallet.app_air.walletcontext.R.string.SendTo_AddressOrDomain)
        typeface = WFont.Regular.typeface
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        maxLines = 3
        setPaddingDp(20, 8, 20, 20)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            setLineHeight(TypedValue.COMPLEX_UNIT_SP, 24f)
        }
        doOnTextChanged { t, _, _, _ -> buttonsVisible.animatedValue = t.isNullOrEmpty() }
    }

    private val qrScanImageViewRipple = WRippleDrawable.create(8f.dp)
    val qrScanImageView = AppCompatImageView(context).apply {
        background = qrScanImageViewRipple
        setImageResource(R.drawable.ic_qr_code_scan_16_24)
        layoutParams = LayoutParams(24.dp, 24.dp, Gravity.TOP or Gravity.RIGHT).apply {
            topMargin = 8.dp
            rightMargin = 20.dp
        }
    }

    private val pasteTextViewRipple = WRippleDrawable.create(8f.dp)
    val pasteTextView = AppCompatTextView(context).apply {
        background = pasteTextViewRipple
        setPaddingDp(4, 0, 4, 0)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        setLineHeight(TypedValue.COMPLEX_UNIT_SP, 24f)

        text = LocaleController.getString(org.mytonwallet.app_air.walletcontext.R.string.Paste)
        typeface = WFont.Regular.typeface
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            Gravity.TOP or Gravity.RIGHT
        ).apply {
            topMargin = 8.dp
            rightMargin = (20 + 24 + 12).dp
        }
    }

    init {
        addView(editTextView)
        addView(qrScanImageView)
        addView(pasteTextView)

        pasteTextView.setOnClickListener {
            context.getTextFromClipboard()?.let {
                editTextView.setTextIfDiffer(it, selectionToEnd = true)
            }
        }

        updateTheme()
    }

    override fun updateTheme() {
        qrScanImageViewRipple.rippleColor = WColor.TintRipple.color
        pasteTextViewRipple.rippleColor = WColor.TintRipple.color
        qrScanImageView.imageTintList = WColor.Tint.colorStateList
        pasteTextView.setTextColor(WColor.Tint.color)
        editTextView.setTextColor(WColor.PrimaryText.color)
        editTextView.setHintTextColor(WColor.SecondaryText.color)
    }
}
