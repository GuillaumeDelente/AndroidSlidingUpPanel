package com.example.demo.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.livelovely.slidinguplist.SlidingUpPanelLayout;

import java.util.ArrayList;

public class DemoActivity extends Activity {
    private static final String TAG = "DemoActivity";

    public static final String SAVED_STATE_ACTION_BAR_HIDDEN = "saved_state_action_bar_hidden";

    private SlidingUpPanelLayout mLayout;
    private ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_demo);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setEnableDragViewTouchEvents(true);

        findViewById(R.id.single_element_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = new ArrayList<>();
                list.add("Plop");
                mArrayAdapter.clear();
                mArrayAdapter.addAll(list);
                mLayout.requestLayout();
                mLayout.showPanel();
            }
        });
        findViewById(R.id.multiple_element_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 10; i++)
                    list.add(i % 2 == 0 ? "plop" : "plip");
                mArrayAdapter.clear();
                mArrayAdapter.addAll(list);
                mLayout.requestLayout();
                mLayout.showPanel();
            }
        });
        findViewById(R.id.toggle_visibility).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLayout.isPanelHidden()) {
                    mLayout.showPanel();
                } else {
                    mLayout.hidePanel();
                }
            }
        });

        ListView lv = (ListView) findViewById(R.id.listview);
        ArrayList<String> list = new ArrayList<>();
        list.add("Plop");
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        //, "Plip", "Plop", "Plip","Plop", "Plip","Plop", "Plip","Plop", "Plip","Plop", "Plip",})
        lv.setAdapter(mArrayAdapter);

        boolean actionBarHidden = savedInstanceState != null && savedInstanceState.getBoolean(SAVED_STATE_ACTION_BAR_HIDDEN, false);
        if (actionBarHidden) {
            int actionBarHeight = getActionBarHeight();
            setActionBarTranslation(-actionBarHeight);//will "hide" an ActionBar
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_STATE_ACTION_BAR_HIDDEN, mLayout.isPanelExpanded());
    }

    private int getActionBarHeight(){
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public void setActionBarTranslation(float y) {
        // Figure out the actionbar height
        int actionBarHeight = getActionBarHeight();
        // A hack to add the translation to the action bar
        ViewGroup content = ((ViewGroup) findViewById(android.R.id.content).getParent());
        int children = content.getChildCount();
        for (int i = 0; i < children; i++) {
            View child = content.getChildAt(i);
            if (child.getId() != android.R.id.content) {
                if (y <= -actionBarHeight) {
                    child.setVisibility(View.GONE);
                } else {
                    child.setVisibility(View.VISIBLE);
                    child.setTranslationY(y);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null && mLayout.isPanelExpanded() || mLayout.isPanelAnchored()) {
            mLayout.collapsePanel();
        } else {
            super.onBackPressed();
        }
    }
}
