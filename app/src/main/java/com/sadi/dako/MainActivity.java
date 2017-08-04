package com.sadi.dako;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.sadi.dako.utils.OnFragmentInteractionListener;


public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    Context con;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    // private ListView listcampus;
    private NavigationView navigationView;
//    private ImageView imgCampus;
//    private ScrollView scrollView;
//    private RelativeLayout campusbn;
//    private LinearLayout prochod,carrierjob,exclusiv,scholarshipbn,politicsbn,itbn,sprotsbn,shobizbn,opinion,nationalbn,internationalbn,ficher,others;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        setContentView(R.layout.activity_main);

        con = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.my_icon);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return false;
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name, R.string.app_name){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        //setContentFragment(new NewsFragement(), false,"");

        //scrollView = (ScrollView)findViewById(R.id.scrollView);
        final String[] number;

        //number = getResources().getStringArray(R.array.campus);

//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(con, android.R.layout.simple_list_item_1, number);
//        listcampus.setAdapter(adapter);

//        imgCampus = (ImageView)findViewById(R.id.imgCampus);
//        campusbn = (RelativeLayout)findViewById(R.id.campusbn);
//        campusbn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(listcampus.getVisibility() != View.VISIBLE){
//                    listcampus.setVisibility(View.VISIBLE);
//                    imgCampus.setImageResource(R.mipmap.back_show);
//                }else if (listcampus.getVisibility() == View.VISIBLE){
//                    listcampus.setVisibility(View.GONE);
//                    imgCampus.setImageResource(R.mipmap.back_gone);
//                }
//            }
//        });
//        listcampus.setOnTouchListener(new View.OnTouchListener() {
//            // Setting on Touch Listener for handling the touch inside ScrollView
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Disallow the touch request for parent scroll on touch of child view
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });



//        listcampus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(adapter.getItem(position).equalsIgnoreCase("ঢাকার ক্যাম্পাস")){
//                    drawerLayout.closeDrawers();
//                    setContentFragment(new NewsFragementDhakaCam(), false,"");
//                }else if(adapter.getItem(position).equalsIgnoreCase("চট্টগ্রামের ক্যাম্পাস")){
//                    drawerLayout.closeDrawers();
//                    setContentFragment(new NewsFragementChitagongCam(), false,"");
//                }
//
//
//            }
//        });
//
//
//        prochod = (LinearLayout) findViewById(R.id.prochod);


//        prochod.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                drawerLayout.closeDrawers();
//               // setContentFragment(new NewsFragement(), false,"");
//            }
//        });

//        carrierjob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.closeDrawers();
//
//            }
//        });
//
//
//        exclusiv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.closeDrawers();
//
//            }
//        });
//
//        scholarshipbn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.closeDrawers();
//
//            }
//        });
//
//        politicsbn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.closeDrawers();
//
//            }
//        });
//
//        itbn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        sprotsbn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.closeDrawers();
//
//            }
//        });
//
//        shobizbn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.closeDrawers();
//
//            }
//        });
//
//        opinion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.closeDrawers();
//
//            }
//        });
//
//        nationalbn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.closeDrawers();
//
//            }
//        });
//
//        internationalbn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.closeDrawers();
//
//            }
//        });
//
//        ficher.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.closeDrawers();
//
//            }
//        });
//
//        others.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.closeDrawers();
//
//            }
//        });


    }

    @Override
    public void setContentFragment(Fragment fragment, boolean addToBackStack,String title) {
        if (fragment == null) {
            return;
        }

        // tvToolBarTitle.setText(title);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.containerView);

        //show only if current fragment is not same as given fragment
        /*if(currentFragment != null && fragment.getClass().equals("SeguiSuFragment")){

        }else{
            return;
        }*/
        if (currentFragment != null && fragment.getClass().isAssignableFrom(currentFragment.getClass())) {
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.containerView, fragment, fragment.getClass().getName());
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();

        /*if (fragment == null) {
            finish();
            //Log.e(tag, "Content fragment cannot be null");
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        //Fragment currentFragment = fragmentManager.findFragmentById(R.id.content_frame);


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.content_frame, fragment, ((Object) fragment).getClass().getName());

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(((Object) fragment).getClass().getName());
        }

        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();*/
    }


    @Override
    public void addContentFragment(Fragment fragment, boolean addToBackStack,String title) {
        if (fragment == null) {
            return;
        }


        //tvToolBarTitle.setText(title);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.containerView);

        if (currentFragment != null && fragment.getClass().isAssignableFrom(currentFragment.getClass())) {
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerView, fragment, fragment.getClass().getName());
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();

    }

}
