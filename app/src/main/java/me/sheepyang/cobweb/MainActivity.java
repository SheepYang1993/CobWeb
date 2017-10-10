package me.sheepyang.cobweb;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import me.sheepyang.cobwebview.CobWebView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private LinearLayout mFlDrawer;
    private CobWebView mCobWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mCobWebView = (CobWebView) findViewById(R.id.cob_web_view);
        mFlDrawer = (LinearLayout) findViewById(R.id.left_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mDrawerLayout = new ActionBarDrawerToggle(this, mDrawerLayout, getSupportActionBar(), 1,
//                1);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mCobWebView.clearTouchPoint();
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

}
