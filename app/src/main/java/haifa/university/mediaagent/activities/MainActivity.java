package haifa.university.mediaagent.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.security.MessageDigest;

import haifa.university.mediaagent.R;
import haifa.university.mediaagent.activities.Adapters.MainFragmentAdapter;
import haifa.university.mediaagent.common.AppHelper;
import haifa.university.mediaagent.common.AppContext;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private MainFragmentAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        System.out.println(TAG + " onCreate");

        GetAllPermissions();

        super.onCreate(savedInstanceState);


        // View package HashCode for facebook
        //        try {
        //            PackageInfo info = getPackageManager().getPackageInfo(
        //                    "haifa.university.mediaagent",
        //                    PackageManager.GET_SIGNATURES);
        //            for (Signature signature : info.signatures) {
        //                MessageDigest md = MessageDigest.getInstance("SHA");
        //                md.update(signature.toByteArray());
        //                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        //            }
        //        } catch (Exception e) {
        //
        //        }

        String str = AppHelper.getInstance().getDeviceIdentificationID();

        ((AppContext) getApplicationContext()).setMainActivity(this);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.fa_user_icon) + " " + getString(R.string.app_name));
        toolbar.setTitle("");

        //toolbar.setTitleTextAppearance(this, R.style.MyTitleTextApperance);
        if (toolbar.getChildCount() > 0) {
            View tabViewChild = toolbar.getChildAt(0);
            if (tabViewChild instanceof TextView) {
                ((TextView) tabViewChild).setTypeface(AppHelper.getInstance().awsomeFont);
            }
        }
        setSupportActionBar(toolbar);

        // Create the adapter that
        mSectionsPagerAdapter = new MainFragmentAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        setTabmenuItemsFont(tabLayout);

        ifHuaweiAlert();

        //((ApplicationContextProvider)getApplicationContext()).exportAppFilesToPubblicDir();
    }

    private void setTabmenuItemsFont(TabLayout tabLayout) {
        Log.i(TAG, "setTabmenuItemsFont");
        //System.out.println(TAG + " onCreateOptionsMenu");

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(AppHelper.getInstance().awsomeFont);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;
        Log.i(TAG, "onCreateOptionsMenu");
        //System.out.println(TAG + " onCreateOptionsMenu");
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected");
        //System.out.println(TAG + " onOptionsItemSelected");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        //System.out.println(TAG + " onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the fragment.
        Fragment fragment = mSectionsPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem());
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void onRestart() {
        Log.i(TAG, "onRestart executes ...");
        //System.out.println(TAG + " onRestart");
        super.onRestart();
    }

    protected void onStart() {
        Log.i(TAG, "onStart executes ...");
        //System.out.println(TAG + " onStart");
        super.onStart();
    }

    protected void onResume() {
        Log.i(TAG, "onResume executes ...");
        //System.out.println(TAG + " onResume");
        super.onResume();
    }

    protected void onPause() {
        Log.i(TAG, "onPause executes ...");
        //System.out.println(TAG + " onPause");
        super.onPause();
    }

    protected void onStop() {
        Log.i(TAG, "onStop executes ...");
        //System.out.println(TAG + " onStop");
        super.onStop();
    }

    protected void onDestroy() {
        Log.i(TAG, "onDestroy executes ...");
        //System.out.println(TAG + " onDestroy");
        super.onDestroy();
    }

    private void ifHuaweiAlert() {
        final SharedPreferences settings = getSharedPreferences("ProtectedApps", MODE_PRIVATE);
        final String saveIfSkip = "skipProtectedAppsMessage";
        boolean skipMessage = settings.getBoolean(saveIfSkip, false);
        if (!skipMessage) {
            final SharedPreferences.Editor editor = settings.edit();
            Intent intent = new Intent();
            intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            if (isIntentCallable(intent)) {
                final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(this);
                dontShowAgain.setText("Do not show again");
                dontShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        editor.putBoolean(saveIfSkip, isChecked);
                        editor.apply();
                    }
                });

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Huawei Protected Apps")
                        .setMessage(String.format("%s requires to be enabled in 'Protected Apps' to function properly.%n", getString(R.string.app_name)))
                        .setView(dontShowAgain)
                        .setPositiveButton("Protected Apps", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            } else {
                editor.putBoolean(saveIfSkip, true);
                editor.apply();
            }
        }
    }
    public void ChangePageNumber(int position){
        mViewPager.setCurrentItem(position);
    }
    private boolean isIntentCallable(Intent intent) {
        try{
            android.content.pm.PackageManager manager = this.getPackageManager();
            java.util.List<android.content.pm.ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
            if (infos.size() > 0) {
                return true;
            }
            return false;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    private void GetAllPermissions(){

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.DISABLE_KEYGUARD,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.VIBRATE
        };

        if(!hasPermissions(PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
    private boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
