package org.charlesliu.c.ui.tab.common;

import androidx.annotation.Px;

import org.jetbrains.annotations.NotNull;

public interface ICTab<D> extends ICTabLayout.OnTabSelectedListener<D> {
    void setCTabInfo(@NotNull D data);

    /**
     * 动态修改某个item的大小
     * @param height 高度
     */
    void resetHeight(@Px int height);
}
