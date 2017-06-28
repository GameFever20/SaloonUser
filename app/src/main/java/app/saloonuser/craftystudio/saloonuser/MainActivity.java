package app.saloonuser.craftystudio.saloonuser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import utils.ClickListener;
import utils.FireBaseHandler;
import utils.RecyclerTouchListener;
import utils.Saloon;
import utils.SaloonAdapter;

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


    Saloon saloon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        //Downloading saloon list from firebase
        showProgressDialog();
        downloadingSaloonList();


    }

    private void downloadingSaloonList() {

        //calling download saloon list
        fireBaseHandler.downloadSaloonList(20, new FireBaseHandler.OnSaloonListListner() {
            @Override
            public void onSaloonList(ArrayList<Saloon> saloonArrayList) {

                hideProgressDialog();

                mSaloonArraylist = saloonArrayList;
                // specify an adapter (see also next example)
                mAdapter = new SaloonAdapter(saloonArrayList, MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);

                //Reverse a arraylist
                Collections.reverse(mSaloonArraylist);
                mAdapter.notifyDataSetChanged();

                //loading more
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                                                      @Override
                                                      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                                          super.onScrolled(recyclerView, dx, dy);
                                                          if (!recyclerView.canScrollVertically(1)) {
                                                              if (!isLodingMoreSaloon) {

                                                                  //get last Saloon ID
                                                                  int lastSaloonPoint = mSaloonArraylist.get(mSaloonArraylist.size() - 1).getSaloonPoint();
                                                                  onScrolledSaloonListToBottom(lastSaloonPoint);
                                                                  //  Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();

                                                              } else {
                                                                  Toast.makeText(MainActivity.this, "Loaded", Toast.LENGTH_SHORT).show();

                                                              }
                                                          }
                                                      }
                                                  }

                );

                mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        saloon = mSaloonArraylist.get(position);

                        //calling new activity having orders of particular saloon
                       /*
                        Intent intent = new Intent(MainActivity.this, OrderListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Saloon Class", saloon);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        */
                        Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();

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

        isLodingMoreSaloon = true;
        Toast.makeText(MainActivity.this, "On Data calling ..", Toast.LENGTH_SHORT).show();

        fireBaseHandler.downloadMoreSaloonList(20, lastSaloonPoint, new FireBaseHandler.OnSaloonListListner() {
            @Override
            public void onSaloonList(ArrayList<Saloon> saloonArrayList) {

                if (saloonArrayList.size() >= 1) {
                    //remove last item of arraylist due to redundancy
                    saloonArrayList.remove(saloonArrayList.size() - 1);

                    if (saloonArrayList != null) {
                        //  String li = saloonArrayList.get(saloonArrayList.size() - 1).getOrderID();

                        //iterate to add orders in main arraylist
                        for (int i = saloonArrayList.size() - 1; i >= 0; i--) {
                            mSaloonArraylist.add(saloonArrayList.get(i));
                        }
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "On Data Refreshed", Toast.LENGTH_SHORT).show();
                        isLodingMoreSaloon = false;

                    }

                } else {
                    isLodingMoreSaloon = false;
                    Toast.makeText(MainActivity.this, "Data Already Refreshed", Toast.LENGTH_SHORT).show();

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
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
