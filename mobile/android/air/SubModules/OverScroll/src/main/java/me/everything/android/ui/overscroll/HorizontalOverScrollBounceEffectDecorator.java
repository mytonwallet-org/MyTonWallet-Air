package me.everything.android.ui.overscroll;

import android.view.MotionEvent;
import android.view.View;

import me.everything.android.ui.overscroll.adapters.IOverScrollDecoratorAdapter;

/**
 * A concrete implementation of {@link OverScrollBounceEffectDecoratorBase} for a horizontal orientation.
 */
public class HorizontalOverScrollBounceEffectDecorator extends OverScrollBounceEffectDecoratorBase {

    /**
     * C'tor, creating the effect with default arguments:
     * <br/>Deceleration factor (for the bounce-back effect) will be set to DEFAULT_DECELERATE_FACTOR.
     *
     * @param viewAdapter The view's encapsulation.
     */
    public HorizontalOverScrollBounceEffectDecorator(IOverScrollDecoratorAdapter viewAdapter) {
        this(viewAdapter, DEFAULT_DECELERATE_FACTOR);
    }

    /**
     * C'tor, creating the effect with explicit arguments.
     *
     * @param viewAdapter      The view's encapsulation.
     *                         direction (opposite to initial one).
     * @param decelerateFactor Deceleration factor used when decelerating the motion to create the
     *                         bounce-back effect.
     */
    public HorizontalOverScrollBounceEffectDecorator(IOverScrollDecoratorAdapter viewAdapter, float decelerateFactor) {
        super(viewAdapter, decelerateFactor);
    }

    @Override
    protected MotionAttributes createMotionAttributes() {
        return new MotionAttributesHorizontal();
    }

    @Override
    protected AnimationAttributes createAnimationAttributes() {
        return new AnimationAttributesHorizontal();
    }

    @Override
    protected void translateView(View view, float offset) {
        view.setTranslationX(offset);
    }

    @Override
    protected void translateViewAndEvent(View view, float offset, MotionEvent event) {
        view.setTranslationX(offset);
        event.offsetLocation(offset - event.getX(0), 0f);
    }

    protected static class MotionAttributesHorizontal extends MotionAttributes {

        public boolean init(View view, MotionEvent event) {

            // We must have history available to calc the dx. Normally it's there - if it isn't temporarily,
            // we declare the event 'invalid' and expect it in consequent events.
            if (event.getHistorySize() == 0) {
                return false;
            }

            // Allow for counter-orientation-direction operations (e.g. item swiping) to run fluently.
            final float dy = event.getY(0) - event.getHistoricalY(0, 0);
            final float dx = event.getX(0) - event.getHistoricalX(0, 0);
            if (Math.abs(dx) < Math.abs(dy)) {
                return false;
            }

            mAbsOffset = view.getTranslationX();
            mDeltaOffset = dx;
            mDir = mDeltaOffset > 0;

            return true;
        }
    }

    protected static class AnimationAttributesHorizontal extends AnimationAttributes {

        public AnimationAttributesHorizontal() {
            mProperty = View.TRANSLATION_X;
        }

        @Override
        protected void init(View view) {
            mAbsOffset = view.getTranslationX();
            mMaxOffset = view.getWidth();
        }
    }
}
