package org.charlesliu.c.ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.charlesliu.c.app.util.CDisplayUtil;
import org.charlesliu.c.app.util.CViewUtil;
import org.charlesliu.c.ui.R;
import org.charlesliu.c.ui.tab.common.ICTabLayout;

import java.util.ArrayList;
import java.util.List;

public class CTabBottomLayout extends FrameLayout implements ICTabLayout<CTabBottom, CTabBottomInfo<?>> {
    private static final String C_TAB_BOTTOM = "C_TAB_BOTTOM";
    private List<OnTabSelectedListener<CTabBottomInfo<?>>> tabSelectedListeners = new ArrayList<>();
    private CTabBottomInfo<?> selectedInfo;
    private float bottomAlpha = 1f;
    private float tabBottomHeight = 50;
    private float bottomLineHeight = 0.5f;
    private String bottomLineColor = "#dfe0e1";

    private List<CTabBottomInfo<?>> infoList;
    public CTabBottomLayout(@NonNull Context context) {
        super(context);
    }

    public CTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public CTabBottom findTab(@NonNull CTabBottomInfo<?> info) {
        ViewGroup ll = findViewWithTag(C_TAB_BOTTOM);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = ll.getChildAt(i);
            if (child instanceof CTabBottom) {
                CTabBottom tabBottom = (CTabBottom) child;
                if (tabBottom.getTabInfo() == info) {
                    return tabBottom;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<CTabBottomInfo<?>> listener) {
        tabSelectedListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull CTabBottomInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<CTabBottomInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        //移除之前已经添加的View
        for (int i = getChildCount() - 1; i > 0; i --) {
            removeViewAt(i);
        }
        selectedInfo = null;
        addBackground();
        tabSelectedListeners.removeIf(cTabBottomInfoOnTabSelectedListener -> cTabBottomInfoOnTabSelectedListener instanceof CTabBottom);
        FrameLayout ll = new FrameLayout(getContext());
        ll.setTag(C_TAB_BOTTOM);
        int displayWidthInPx = CDisplayUtil.getDisplayWidthInPx(getContext());
        int width = displayWidthInPx / infoList.size();
        for (int i = 0; i < infoList.size(); i++) {
            CTabBottomInfo<?> info = infoList.get(i);
            LayoutParams params = new LayoutParams(width, CDisplayUtil.dp2px(tabBottomHeight, getResources()));
            params.gravity = Gravity.BOTTOM;
            params.leftMargin = i * width;
            CTabBottom cTabBottom = new CTabBottom(getContext());
            tabSelectedListeners.add(cTabBottom);
            cTabBottom.setCTabInfo(info);
            ll.addView(cTabBottom, params);
            cTabBottom.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelected(info);
                }
            });
        }
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        addTabBottomLine();
        addView(ll, params);
        fixContentView();
    }

    public void setTabAlpha(float tabAlpha) {
        this.bottomAlpha = tabAlpha;
    }

    public void setTabHeight(float tabHeight) {
        this.tabBottomHeight = tabHeight;
    }

    public void setTabLineHeight(float tabLineHeight) {
        this.bottomLineHeight = tabLineHeight;
    }

    public void setTabLineColor(String tabLineColor) {
        this.bottomLineColor = tabLineColor;
    }

    private void addTabBottomLine() {
        View view = new View(getContext());
        view.setBackgroundColor(Color.parseColor(bottomLineColor));
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, CDisplayUtil.dp2px(bottomLineHeight, getResources()));
        params.gravity = Gravity.BOTTOM;
        params.bottomMargin = CDisplayUtil.dp2px(tabBottomHeight - bottomLineHeight, getResources());
        addView(view, params);
        view.setAlpha(bottomAlpha);
    }

    private void onSelected(@NonNull CTabBottomInfo<?> nextInfo) {
        for (OnTabSelectedListener<CTabBottomInfo<?>> listener:
        tabSelectedListeners){
            listener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        selectedInfo = nextInfo;
    }

    private void addBackground() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.c_bottom_layout_bg, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, CDisplayUtil.dp2px(tabBottomHeight, getResources()));
        params.gravity = Gravity.BOTTOM;
        addView(view, params);
        view.setAlpha(bottomAlpha);
    }

    private void fixContentView() {
        if (!(getChildAt(0) instanceof ViewGroup)) {
            return;
        }
        ViewGroup rootView = (ViewGroup) getChildAt(0);
        ViewGroup targetView = CViewUtil.findTypeView(rootView, RecyclerView.class);
        if (targetView == null) {
            targetView = CViewUtil.findTypeView(rootView, ScrollView.class);
        }
        if (targetView == null) {
            targetView = CViewUtil.findTypeView(rootView, AbsListView.class);
        }
        if (targetView != null) {
            targetView.setPadding(0, 0, 0, CDisplayUtil.dp2px(tabBottomHeight, getResources()));
            targetView.setClipToPadding(false);
        }
    }
}
