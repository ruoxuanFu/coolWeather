package com.quaie.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quaie.myapplication.custom.AppBarStateChangeListener;
import com.quaie.myapplication.fragments.Frag_City;
import com.quaie.myapplication.fragments.Frag_Weather;
import com.quaie.myapplication.utils.HttpUtil;
import com.quaie.myapplication.utils.SpUtils;

import static org.litepal.LitePalApplication.getContext;

public class Main_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawer;
    public CollapsingToolbarLayout collapsingToolbarLayout;
    public Toolbar toolbar;
    public AppBarLayout appBarLayout;
    public ActionBarDrawerToggle toggle;
    public NavigationView navigationView;

    private Frag_Weather frag_weather;
    private Frag_City frag_city;

    public TextView mTvNewTemperature;
    public ImageView mImgNewTemperature;
    public TextView mTvAqi;
    public TextView mTvDate;
    public TextView mTvPm25;
    public ImageView mImgAqi;
    public ImageView mImgPm25;
    public TextView mTvHighTemperature;
    public TextView mTvLowTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        frag_weather = new Frag_Weather();
        frag_city = new Frag_City();

        initView();


        if (SpUtils.get(getContext(), "Select_County", "").toString().equals("")) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("请先选择城市").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    replaceFragment(frag_city);
                }
            });
            dialog.show();
        } else {
            replaceFragment(frag_weather);
        }

    }

    private void initView() {

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //CollapsingToolbarLayout
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collToolBar);
        collapsingToolbarLayout.setTitle(getString(R.string.On_Loading));
        //AppBarLayout
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        //DrawerLayout
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //ActionBarDrawerToggle 的作用：
        //改变android.R.id.home返回图标。
        //DrawerLayout拉出、隐藏，带有android.R.id.home动画效果。
        //监听DrawerLayout拉出、隐藏；
        //参数依次为：context，DrawerLyout类（上图xml中配置的），图标（下图的矩形中图标），
        //String XML中的open和close（名称可以随便写）

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //设置监听
        drawer.addDrawerListener(toggle);
        //设置三个横岗，没设置的话是箭头
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mTvNewTemperature = (TextView) findViewById(R.id.tv_new_temperature);
        mImgNewTemperature = (ImageView) findViewById(R.id.img_new_temperature);
        mTvAqi = (TextView) findViewById(R.id.tv_aqi);
        mTvDate = (TextView) findViewById(R.id.tv_date);
        mTvPm25 = (TextView) findViewById(R.id.tv_pm25);
        mImgAqi = (ImageView) findViewById(R.id.img_aqi);
        mImgPm25 = (ImageView) findViewById(R.id.img_pm25);
        mTvHighTemperature = (TextView) findViewById(R.id.tv_high_temperature);
        mTvLowTemperature = (TextView) findViewById(R.id.tv_low_temperature);
        mImgAqi.setVisibility(View.INVISIBLE);
        mImgPm25.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onBackPressed() {
        HttpUtil.closeOkHttpRequest();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_weather) {
            replaceFragment(frag_weather);
            appBarExpandedListener(appBarLayout, "weather", "Location");
        } else if (id == R.id.nav_location) {
            replaceFragment(frag_city);
            appBarExpandedListener(appBarLayout, getString(R.string.Select_City), getString(R.string.Select_City));
        } else if (id == R.id.nav_share) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //切换fragment
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_frag, fragment);
        fragmentTransaction.commit();
    }

    //监听appbar的滑动状态，并给collapsingToolbarLayout修改文字
    public void appBarExpandedListener(AppBarLayout appBar, final String expandedText, final String collapsedText) {
        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    collapsingToolbarLayout.setTitle(expandedText);
                } else if (state == State.COLLAPSED) {
                    collapsingToolbarLayout.setTitle(collapsedText);
                }
            }
        });
    }
}
