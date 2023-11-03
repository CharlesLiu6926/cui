package org.charlesliu.c.ui.tab.common;

import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ICTabLayout<Tab extends ViewGroup, D> {
    Tab findTab(@NotNull D data);

    void addTabSelectedChangeListener(OnTabSelectedListener<D> listener);

    void defaultSelected(@NotNull D defaultInfo);

    void inflateInfo(@NotNull List<D> infoList);

    interface OnTabSelectedListener<D> {
        void onTabSelectedChange(int index, D prevInfo, @NotNull D nextInfo);
    }
}
