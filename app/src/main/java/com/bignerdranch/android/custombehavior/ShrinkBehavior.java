package com.bignerdranch.android.custombehavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;


/**
 * todo 1 - So how does CoordinatorLayout know what to do with the floating action button? How does it know that it should move up the screen when the snack bar comes in?
 * CoordinatorLayout makes this happen by making use of a CoordinatorLayout.Behavior implemented and declared by FloatingActionButton
 *
 * trong class FAB có hàm này:
 * @CoordinatorLayout.DefaultBehavior(FloatingActionButton.Behavior.class)
public class FloatingActionButton extends ImageView {
...
}
 * nó chỉ ra rằng fab có cách hành xử được quy định trong class  CoordinatorLayout.Behavior<FloatingActionButton>,
 * và bây giờ ta đè lên phương pháp default của nó.
 *
 *
 */
public class ShrinkBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    public ShrinkBehavior() { }

    public ShrinkBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        float translationY = getFabTranslationYForSnackbar(parent, child);
        float percentComplete = -translationY / dependency.getHeight();
        float scaleFactor = 1 - percentComplete;

        child.setScaleX(scaleFactor);
        child.setScaleY(scaleFactor);
        return false;
    }

    private float getFabTranslationYForSnackbar(CoordinatorLayout parent,
                                                FloatingActionButton fab) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(fab);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset,
                        ViewCompat.getTranslationY(view) - view.getHeight());
            }
        }

        return minOffset;
    }
}
