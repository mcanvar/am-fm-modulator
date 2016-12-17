package com.mcnvr.amfmmodulator.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.mcnvr.amfmmodulator.R;
import com.mcnvr.amfmmodulator.adapters.PagerAdapter;
import com.mcnvr.amfmmodulator.models.DoubleParcelable;


public class DisplayActivity extends AppCompatActivity {

    Integer selection;
    DoubleParcelable parcelable;
    DoubleParcelable parcelableCarrier;
    DoubleParcelable parcelableModulated;
    PagerAdapter adapter;
    InterstitialAd mInterstitialAd;
    AdView mAdView;
    Integer selected;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        MobileAds.initialize(this, "ca-app-pub-2926708254200421~2569596021");
        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2926708254200421/3987453628");

        requestNewInterstitial();

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("DATA"));
        tabLayout.addTab(tabLayout.newTab().setText("CARR"));
        tabLayout.addTab(tabLayout.newTab().setText("MOD"));
        tabLayout.addTab(tabLayout.newTab().setText("MUL"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    viewPager.setCurrentItem(tab.getPosition());
                    selected = tab.getPosition();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        parcelable = getIntent().getExtras().getParcelable("data");
        parcelableCarrier = getIntent().getExtras().getParcelable("datac");
        parcelableModulated = getIntent().getExtras().getParcelable("datam");
        selection = new Integer(getIntent().getExtras().getInt("selection"));


        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Snackbar snackbar = Snackbar.make(view, getSignalData(viewPager.getCurrentItem()), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("GOT IT", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                            }
                        });
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(5);
                snackbar.setActionTextColor(Color.RED).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intentMain = new Intent(this, MainActivity.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.activity_animation, R.anim.activity_animation2).toBundle();
            startActivity(intentMain, bundle);
        }
    }

    public DoubleParcelable getParcelable() {
        return parcelable;
    }

    public DoubleParcelable getParcelableCarrier() {
        return parcelableCarrier;
    }

    public DoubleParcelable getParcelableModulated() {
        return parcelableModulated;
    }

    public void initializeGraph(final GraphView graph, final DoubleParcelable parcelable) {

        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        graph.removeAllSeries();

                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(parcelable.getDataPoints());

                        series.setThickness(4);
                        series.setColor(Color.parseColor("#3E828F"));
                        graph.addSeries(series);

                        graph.getViewport().setScalable(true);
                        graph.getViewport().setScrollable(true);
                    }
                });
            }
        }).start();
    }

    //Override for mixed type
    public void initializeGraph(final GraphView graph, final DoubleParcelable parcelable, final DoubleParcelable parcelable2) {

        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        graph.removeAllSeries();

                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(parcelable.getDataPoints());
                        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(parcelable2.getDataPoints());

                        series.setThickness(4);
                        series.setColor(Color.parseColor("#3E828F"));
                        series2.setThickness(2);
                        series2.setColor(Color.parseColor("#091928"));
                        graph.addSeries(series);
                        graph.addSeries(series2);


                        graph.getViewport().setScalable(true);
                        graph.getViewport().setScrollable(true);
                    }
                });
            }
        }).start();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    private String getSignalData(int page) {
        String signaldata[] = {
                "The signal that based on the first formula. This signal transmits information and should be almost same form after transmission complated.",
                "The signal that based on the second formula. This signal carries the actual signal.",
                "This signal is an addition of data signal and carrier signal. This signal will transmit.",
                "This graph shows us two signals to see the differences."
        };

        return signaldata[page];
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}