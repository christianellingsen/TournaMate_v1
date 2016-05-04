package com.dtu.tournamate_v1.Adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ce on 04-05-2016.
 */
public class MarginDecorator extends RecyclerView.ItemDecoration {
    private int margin;

    public MarginDecorator(Context context) {
        margin = 8;
    }

    @Override
    public void getItemOffsets(
            Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(margin, margin, margin, margin);
    }
}