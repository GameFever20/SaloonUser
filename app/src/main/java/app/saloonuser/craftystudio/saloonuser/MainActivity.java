package app.saloonuser.craftystudio.saloonuser;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Visibility;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;

import io.fabric.sdk.android.Fabric;
import utils.ClickListener;
import utils.FireBaseHandler;
import utils.RecyclerTouchListener;
import utils.Saloon;
import utils.SaloonAdapter;
import utils.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FireBaseHandler fireBaseHandler;
    //Recycler View
    private RecyclerView mRecyclerView;
    private SaloonAdapter mAdapter;
    //private OrderAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //temporary arraylist for display

    ArrayList<Saloon> mSaloonArraylist;

    ProgressDialog progressDialog;

    boolean isLodingMoreSaloon;
    boolean isMoreSaloonAvailable = true;
    final int saloonFetchLimit = 30;


    Saloon saloon;

    Toolbar toolbar;

    User user = LoginActivity.USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //load recycler
        //Intializing adapter and recycler
        mRecyclerView = (RecyclerView) findViewById(R.id.saloon_list_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        //progress dialog show
        progressDialog = new ProgressDialog(this);

        // instantiating firebasehandler class
        fireBaseHandler = new FireBaseHandler();

        fireBaseHandler.downloadUser(LoginActivity.USER.getUserUID(), new FireBaseHandler.OnUserlistener() {
            @Override
            public void onUserDownLoad(User user, boolean isSuccessful) {
                LoginActivity.USER =user;
                MainActivity.this.user =user;


            }

            @Override
            public void onUserUpload(boolean isSuccessful) {

            }
        });

        //Downloading saloon list from firebase
        showProgressDialog();
        downloadingSaloonList();


        subscribeToTopic();

    }

    private void subscribeToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("user_" + user.getUserUID());
        FirebaseMessaging.getInstance().subscribeToTopic("user_common");

    }

    private void downloadingSaloonList() {

        //calling download saloon list
        fireBaseHandler.downloadSaloonList(saloonFetchLimit, new FireBaseHandler.OnSaloonListListner() {
            @Override
            public void onSaloonList(ArrayList<Saloon> saloonArrayList) {

                if (saloonArrayList.size() != saloonFetchLimit) {
                    isMoreSaloonAvailable = false;
                }

                hideProgressDialog();

                mSaloonArraylist = saloonArrayList;
                // specify an adapter (see also next example)
                mAdapter = new SaloonAdapter(saloonArrayList, MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);

                //Reverse a arraylist
                Collections.reverse(mSaloonArraylist);
                mAdapter.notifyDataSetChanged();


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    // Do something for lollipop and above versions
                    //animate toolbar
                    YoYo.with(Techniques.BounceInDown)
                            .duration(1000)
                            .repeat(1)
                            .playOn(toolbar);

                } else {
                    // do something for phones running an SDK before lollipop
                }


                //loading more
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                                                      @Override
                                                      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                                          super.onScrolled(recyclerView, dx, dy);
                                                          if (!recyclerView.canScrollVertically(1)) {
                                                              if (isMoreSaloonAvailable) {
                                                                  if (!isLodingMoreSaloon) {
                                                                      //get last Saloon ID
                                                                      int lastSaloonPoint = mSaloonArraylist.get(mSaloonArraylist.size() - 1).getSaloonPoint();
                                                                      onScrolledSaloonListToBottom(lastSaloonPoint);
                                                                      //  Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();

                                                                  } else {
                                                                      // Toast.makeText(MainActivity.this, "Loading", Toast.LENGTH_SHORT).show();

                                                                  }
                                                              }
                                                          }
                                                      }
                                                  }

                );

                mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        saloon = mSaloonArraylist.get(position);


                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            // Do something for lollipop and above versions
                            //animation
                            // ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);

                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, android.util.Pair.create(view.findViewById(R.id.saloonadapterrow_saloon_profilepic_imageView), "profile_image_shared"));
                            Intent intent = new Intent(MainActivity.this, SaloonDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Saloon_Class", saloon);
                            intent.putExtras(bundle);
                            startActivity(intent, options.toBundle());

                        } else {
                            // do something for phones running an SDK before lollipop
                            Intent intent = new Intent(MainActivity.this, SaloonDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Saloon_Class", saloon);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }


                        //calling new activity having orders of particular saloon
                       /*
                        Intent intent = new Intent(MainActivity.this, OrderListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Saloon Class", saloon);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        */
                        //Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));


            }

            @Override
            public void onCancel() {
                hideProgressDialog();

            }

        });


    }

    public void onScrolledSaloonListToBottom(int lastSaloonPoint) {
//store number of saloon with same saloon point in last
        int repeatedSaloonCount = 0;


        for (int i = mSaloonArraylist.size() - 1; i >= 0; i--) {


            if (mSaloonArraylist.get(i).getSaloonPoint() == lastSaloonPoint) {

                repeatedSaloonCount++;
            } else {
                break;
            }

        }

        if (repeatedSaloonCount == saloonFetchLimit) {
            //if all 30 saloon have same saloon point then skip that saloon point
            lastSaloonPoint--;
        }

        isLodingMoreSaloon = true;
        //Toast.makeText(MainActivity.this, "On Data calling ..", Toast.LENGTH_SHORT).show();

        final int finalRepeatedSaloonCount = repeatedSaloonCount;
        fireBaseHandler.downloadMoreSaloonList(saloonFetchLimit, lastSaloonPoint, new FireBaseHandler.OnSaloonListListner() {
            @Override
            public void onSaloonList(ArrayList<Saloon> saloonArrayList) {

                if (saloonArrayList.size() != saloonFetchLimit) {
                    isMoreSaloonAvailable = false;
                }

                if (saloonArrayList.size() >= 1) {

                    try {
                        //remove last item of arraylist due to redundancy
                        for (int i = finalRepeatedSaloonCount; i >= 0; i--) {
                            saloonArrayList.remove(saloonArrayList.size() - 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (saloonArrayList != null) {
                        //  String li = saloonArrayList.get(saloonArrayList.size() - 1).getOrderID();

                        //iterate to add orders in main arraylist
                        for (int i = saloonArrayList.size() - 1; i >= 0; i--) {
                            mSaloonArraylist.add(saloonArrayList.get(i));
                        }
                        mAdapter.notifyDataSetChanged();
                        //Toast.makeText(MainActivity.this, "On Data Refreshed", Toast.LENGTH_SHORT).show();
                        isLodingMoreSaloon = false;

                    }

                } else {
                    isLodingMoreSaloon = false;
                    //Toast.makeText(MainActivity.this, "Data Already Refreshed", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCancel() {

            }
        });


    }


    public void showProgressDialog() {
        progressDialog.setMessage("Getting Data..");
        progressDialog.show();
    }

    public void hideProgressDialog() {

        progressDialog.cancel();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                // Do something for lollipop and above versions
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent, options.toBundle());

            } else {
                // do something for phones running an SDK before lollipop

                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);

            }

            // Handle the camera action


        } else if (id == R.id.nav_order) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                // Do something for lollipop and above versions
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, UserOrderActivity.class);
                startActivity(intent, options.toBundle());

            } else {
                // do something for phones running an SDK before lollipop
                Intent intent = new Intent(MainActivity.this, UserOrderActivity.class);
                startActivity(intent);

            }


        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Link to share app", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "Link to share app", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_logout) {
            signout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signout() {


        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + user.getUserUID());

        mAuth.signOut();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();


    }
}
