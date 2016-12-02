package com.topiasoft.notepia;

import android.content.Context;
import android.graphics.drawable.Drawable;
//import android.support.v7.widget.ActivityChooserView;
import android.app.Activity;
import android.view.View;
import android.widget.ShareActionProvider;

/**
 * Created by fm on 29/11/2016.
 */

public class CustomShareActionProvider extends ShareActionProvider {


    private final Context mContext;

    public CustomShareActionProvider(Context context) {
        super(context);
        mContext = context;
    }
/*
    @Override
    public View onCreateActionView() {
        ActivityChooserView chooserView =
                (ActivityChooserView) super.onCreateActionView();

        // Set your drawable here
        Drawable icon =
                mContext.getResources().getDrawable(R.drawable.btn_share);

        chooserView.setExpandActivityOverflowButtonDrawable(icon);

        return chooserView;
    }
*/
}
