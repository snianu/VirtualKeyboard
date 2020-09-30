package com.example.samplevk;

import android.graphics.Insets;
import android.inputmethodservice.InputMethodService;
import android.os.Build;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.R)
public class imeSyncCallback extends WindowInsetsAnimation.Callback implements View.OnApplyWindowInsetsListener {
    private int overlayInsetTypes;
    private int deferredInsetTypes;

    private View view;
    private WindowInsets lastWindowInsets;
    private boolean started = false;

    imeSyncCallback(
            @NonNull View view, int overlayInsetTypes, int deferredInsetTypes) {
        super(WindowInsetsAnimation.Callback.DISPATCH_MODE_STOP);
        this.overlayInsetTypes = overlayInsetTypes;
        this.deferredInsetTypes = deferredInsetTypes;
        this.view = view;
    }

    @Override
    public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        this.view = view;
        if (started) {
            return WindowInsets.CONSUMED;
        }

        lastWindowInsets = windowInsets;
        return view.onApplyWindowInsets(windowInsets);
    }

    @Override
    public WindowInsetsAnimation.Bounds onStart(
            WindowInsetsAnimation animation, WindowInsetsAnimation.Bounds bounds) {
        if ((animation.getTypeMask() & deferredInsetTypes) != 0) {
            started = true;
        }
        return bounds;
    }

    @Override
    public WindowInsets onProgress(
            WindowInsets insets, List<WindowInsetsAnimation> runningAnimations) {
        if (!started) {
            return insets;
        }
        boolean matching = false;
        for (WindowInsetsAnimation animation : runningAnimations) {
            if ((animation.getTypeMask() & deferredInsetTypes) != 0) {
                matching = true;
                continue;
            }
        }
        if (!matching) {
            return insets;
        }
        WindowInsets.Builder builder = new WindowInsets.Builder(lastWindowInsets);
        Insets newImeInsets =
                Insets.of(
                        0,
                        0,
                        0,
                        Math.max(
                                insets.getInsets(deferredInsetTypes).bottom
                                        - insets.getInsets(overlayInsetTypes).bottom,
                                0));
        builder.setInsets(deferredInsetTypes, newImeInsets);
        view.onApplyWindowInsets(builder.build());
        return insets;
    }

    @Override
    public void onEnd(WindowInsetsAnimation animation) {
        if (started && (animation.getTypeMask() & deferredInsetTypes) != 0) {
            started = false;
            if (lastWindowInsets != null && view != null) {
                view.dispatchApplyWindowInsets(lastWindowInsets);
            }
        }
    }
}
