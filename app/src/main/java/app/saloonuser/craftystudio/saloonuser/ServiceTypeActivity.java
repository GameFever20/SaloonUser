package app.saloonuser.craftystudio.saloonuser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import utils.FireBaseHandler;
import utils.Order;
import utils.Saloon;
import utils.Service;
import utils.ServiceTypeExpandableAdapter;
import utils.User;

public class ServiceTypeActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    TabLayout tabLayout;

    //static HashMap<String, ArrayList<Service>> mServiceHashMap = new HashMap<>();
    //static ArrayList<String> mServiceSubType = new ArrayList<>();

    ArrayList<Service> serviceArrayList = new ArrayList<>();

    public static Order CURRENTORDER;
    Saloon saloon;


    static TextView mPriceNServiceTextview;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        try {
            saloon = (Saloon) getIntent().getSerializableExtra("Saloon");
        } catch (Exception e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(this);
        showProgressDialog("Fetching Service");

        CURRENTORDER = new Order();
        CURRENTORDER.setSaloonName(saloon.getSaloonName());
        CURRENTORDER.setSaloonID(saloon.getSaloonUID());


        mPriceNServiceTextview = (TextView) findViewById(R.id.serviceType_priceAndServiceOrder_textview);


        tabLayout = (TabLayout) findViewById(R.id.serviceType_tabLayout);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        //serviceArrayList = createServiceList();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(ServiceTypeActivity.this, UserOrderPlacementActivity.class);
                intent.putExtra("Saloon", saloon);
                startActivity(intent);
            }
        });


        new FireBaseHandler().downloadServiceList(saloon.getSaloonUID(), 50, new FireBaseHandler.OnServiceListener() {
            @Override
            public void onSeviceUpload(boolean isSuccesful) {

            }

            @Override
            public void onServiceList(ArrayList<Service> serviceArrayList, boolean isSuccesful) {

                ServiceTypeActivity.this.serviceArrayList = serviceArrayList;
                initializeActivity();
                closeProgressDialog();

            }
        });


        //createServiceHashMap();


    }


    public void initializeActivity() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), serviceArrayList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        tabLayout.setupWithViewPager(mViewPager);

    }


    public static void updateOrderDetail() {

        mPriceNServiceTextview.setText(CURRENTORDER.getOrderPrice() + " and " + CURRENTORDER.getOrderTotalServiceCount());

    }


    private void createServiceHashMap() {

       /* mServiceHashMap.put("Hair Cut", createServiceList());
        mServiceHashMap.put("Hair color", createServiceColorList());
        mServiceHashMap.put("Hair stra", createServiceList());


        mServiceSubType.add("Hair Cut");
        mServiceSubType.add("Hair color");
        mServiceSubType.add("Hair stra");
*/

    }

    private ArrayList<Service> createServiceColorList() {
        ArrayList<Service> serviceArrayList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Service service = new Service();
            service.setServiceName("Brown color");
            serviceArrayList.add(service);
        }

        return serviceArrayList;
    }

    private ArrayList<Service> createServiceList() {
        ArrayList<Service> serviceArrayList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Service service = new Service();
            service.setServiceName("cutting");
            service.setServiceType(1);
            service.setServiceSubType(0);
            service.setServiceUID("service_" + (100 + i));
            service.setServicePrice(i * 50 + 125);
            serviceArrayList.add(service);
        }


        for (int i = 0; i < 5; i++) {
            Service service = new Service();
            service.setServiceName("Bold cut");
            service.setServiceType(1);
            service.setServiceSubType(2);
            service.setServiceUID("service_" + (120 + i));
            service.setServicePrice(i * 50 + 25);
            serviceArrayList.add(service);

        }


        for (int i = 0; i < 5; i++) {
            Service service = new Service();
            service.setServiceName("awesome cut");
            service.setServiceType(2);
            service.setServiceSubType(1);
            service.setServiceUID("service_" + (i + 210));
            service.setServicePrice(i * 50 + 75);
            serviceArrayList.add(service);
        }


        return serviceArrayList;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_service_type, menu);
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


    public void proceedOrderPlacement(View view) {
        boolean isServiceSelected = false;
        for (String service : CURRENTORDER.getOrderServiceIDList().values()) {
            isServiceSelected = true;
            break;
        }

        if (!isServiceSelected) {
            Toast.makeText(this, " Add Service to proceed", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(ServiceTypeActivity.this, UserOrderPlacementActivity.class);
        intent.putExtra("Saloon", saloon);
        startActivity(intent);
    }



    public void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void closeProgressDialog() {
        progressDialog.dismiss();
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private static ArrayList<Service> mServiceArrayList;

        ArrayList<String> mServiceSubTypeList;
        HashMap<String, ArrayList<Service>> mServiceHashMap;

        public PlaceholderFragment() {
        }


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, ArrayList<Service> serviceArrayList) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            mServiceArrayList = serviceArrayList;
            fragment.setArguments(args);

            return fragment;

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            final int serviceTypeIndex = getArguments().getInt(ARG_SECTION_NUMBER);

            View rootView = inflater.inflate(R.layout.fragment_service_type, container, false);
            createServiceHashMap(serviceTypeIndex);

            ExpandableListView expandableListView = (ExpandableListView) rootView.findViewById(R.id.serviceTypeFragment_ExpandableListView);

            ServiceTypeExpandableAdapter serviceTypeExpandableAdapter = new ServiceTypeExpandableAdapter(mServiceHashMap
                    , mServiceSubTypeList, getContext()
            );


            expandableListView.setAdapter(serviceTypeExpandableAdapter);


            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Toast.makeText(getContext(), "position is " + serviceTypeIndex + " , " + groupPosition + " , " + childPosition, Toast.LENGTH_SHORT).show();

                    return false;

                }
            });

            return rootView;
        }


        public void createServiceHashMap(int serviceTypeIndex) {


            String[] serviceTypeName ;

            switch (serviceTypeIndex) {
                case 1:
                    serviceTypeName = getResources().getStringArray(R.array.haircare_service_sub_type);
                    break;
                case 2:
                    serviceTypeName = getResources().getStringArray(R.array.skincare_service_sub_type);
                    break;
                case 3:
                    serviceTypeName = getResources().getStringArray(R.array.manicure_service_sub_type);
                    break;
                case 4:
                    serviceTypeName = getResources().getStringArray(R.array.bodywrap_service_sub_type);
                    break;
                case 5:
                    serviceTypeName = getResources().getStringArray(R.array.massage_service_sub_type);
                    break;
                case 6:
                    serviceTypeName = getResources().getStringArray(R.array.makeup_service_sub_type);
                default:
                    serviceTypeName = getResources().getStringArray(R.array.haircare_service_sub_type);
                    break;


            }


            ArrayList<String> serviceSubTypeList = new ArrayList<String>();
            HashMap<String, ArrayList<Service>> serviceHashMap = new HashMap<>();

            for (String serviceSubTypeName : serviceTypeName) {

                serviceSubTypeList.add(serviceSubTypeName);

                serviceHashMap.put(serviceSubTypeName, new ArrayList<Service>());

            }


            for (Service service : mServiceArrayList) {

                if (service.getServiceType() == serviceTypeIndex) {

                    serviceHashMap.get(serviceSubTypeList.get(service.getServiceSubType())).add(service);

                }

            }

            mServiceHashMap = serviceHashMap;
            mServiceSubTypeList = serviceSubTypeList;

        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Service> mServiceArrayList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Service> serviceArrayList) {
            super(fm);
            mServiceArrayList = serviceArrayList;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).


            return PlaceholderFragment.newInstance(position + 1, mServiceArrayList);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Hair Care";
                case 1:
                    return "Skin Care";
                case 2:
                    return "MANICURE";
                case 3:
                    return "Body Scrub";
                case 4:
                    return "MASSAGES";
                case 5:
                    return "Make up";
            }
            return null;
        }
    }
}
