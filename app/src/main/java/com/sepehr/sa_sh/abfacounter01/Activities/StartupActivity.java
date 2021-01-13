package com.sepehr.sa_sh.abfacounter01.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sepehr.sa_sh.abfacounter01.Adopters.ViewPagerAdapterTab;
import com.sepehr.sa_sh.abfacounter01.BaseClasses.BaseActivity;
import com.sepehr.sa_sh.abfacounter01.DeviceSerialManager;
import com.sepehr.sa_sh.abfacounter01.DifferentCompanyManager;
import com.sepehr.sa_sh.abfacounter01.FlashIntorFragment;
import com.sepehr.sa_sh.abfacounter01.Karbari;
import com.sepehr.sa_sh.abfacounter01.LoadIntroFragment;
import com.sepehr.sa_sh.abfacounter01.OffLoadIntorFragment;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.ReportIntroFragment;
import com.sepehr.sa_sh.abfacounter01.models.UiElementInActivity;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.KarbariGroup;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StartupActivity extends BaseActivity {

    private LinearLayout startupContentWrapper;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_flash_on_black_36dp,
            R.drawable.ic_flash_on_black_36dp,
            R.drawable.ic_flash_on_black_36dp,
            R.drawable.ic_flash_on_black_36dp
    };

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity=new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.activity_startup);
        return uiElementInActivity;

    }

    @Override
    protected void initialize() {
        Context appContext=getApplicationContext();
        addKarbariesIfNotExists();
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);//right to left
        Log.e("serial", DeviceSerialManager.getSerial(this));

        LinearLayout loadLinearLayout=(LinearLayout)findViewById(R.id.load);
        loadLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoad = new Intent(v.getContext(), LoadActivity.class);
                startActivity(intentLoad);
            }
        });
        //
        startupContentWrapper=(LinearLayout) findViewById(R.id.startup_content_wrapper);
        int startupImageRecourceId= DifferentCompanyManager.getStartupImageRecourceId(DifferentCompanyManager.getActiveCompanyName());
        startupContentWrapper.setBackgroundResource(startupImageRecourceId);

        viewPager = (ViewPager) findViewById(R.id.startViewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.startTabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        manage_M_permissions();
    }
    //
    private void manage_M_permissions(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(StartupActivity.this, "مجوز ها داده شده", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(StartupActivity.this, "مجوز رد شد \n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                forceClose();
            }
        };


        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("جهت استفاده بهتر از برنامه مجوز های پیشنهادی را قبول فرمایید")
                .setDeniedMessage("در صورت رد این مجوز قادر با استفاده از این دستگاه نخواهید بود"+"\n"+
             "لطفا با فشار دادن دکمه"+" "+"اعطای دسترسی"+" "+"و سپس در بخش "+" دسترسی ها"+" "+" با این مجوز هاموافقت نمایید")
                .setGotoSettingButtonText("اعطای دسترسی")
                .setPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.CALL_PHONE
                        //,Manifest.permission.READ_LOGS
                        )
                .check();
    }
    //
    private void setupTabIcons() {
        View _load = getLayoutInflater().inflate(R.layout.custom_tab_start, null);
        _load.findViewById(R.id.icon).setBackgroundResource(R.drawable._load);

        View _flash = getLayoutInflater().inflate(R.layout.custom_tab_start, null);
        _flash.findViewById(R.id.icon).setBackgroundResource(R.drawable._flash3);

        View _report = getLayoutInflater().inflate(R.layout.custom_tab_start, null);
        _report.findViewById(R.id.icon).setBackgroundResource(R.drawable._report);

        View _offload = getLayoutInflater().inflate(R.layout.custom_tab_start, null);
        _offload.findViewById(R.id.icon).setBackgroundResource(R.drawable._offload);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]).setCustomView(_load);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]).setCustomView(_flash);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]).setCustomView(_report);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]).setCustomView(_offload);
    }
    //
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterTab adapter = new ViewPagerAdapterTab(getSupportFragmentManager());
        adapter.addFragment(new LoadIntroFragment(), "بارگیری");
        adapter.addFragment(new FlashIntorFragment(), "فلش");
        adapter.addFragment(new ReportIntroFragment(), "گزارش");
        adapter.addFragment(new OffLoadIntorFragment(), "تخلیه");

        viewPager.setAdapter(adapter);
    }
    //
    private void addKarbariesIfNotExists(){
        //region insert data
        if(Karbari.listAll(Karbari.class).size()<1) {
            KarbariGroup karbariGroup1 = new KarbariGroup(1, "خانگي");
            karbariGroup1.save();
            KarbariGroup karbariGroup2 = new KarbariGroup(2, "اشتراكي و عمومي");
            karbariGroup2.save();
            KarbariGroup karbariGroup3 = new KarbariGroup(3, "فضاي سبز");
            karbariGroup3.save();
            KarbariGroup karbariGroup4 = new KarbariGroup(4, "تجاري");
            karbariGroup4.save();
            KarbariGroup karbariGroup5 = new KarbariGroup(5, "توليدي-صنعتي");
            karbariGroup5.save();

            List<Karbari> karbaris = new ArrayList<>();
            karbaris.add(new Karbari(1, "مسكوني", 1));
            karbaris.add(new Karbari(11, "حمام", 1));
            karbaris.add(new Karbari(3, "تجاري مسکوني(مختلط)", 2));
            karbaris.add(new Karbari(14, "در حال ساخت", 1));
            karbaris.add(new Karbari(30, "انشعابات وابسته به شرکت آبفا", 1));
            karbaris.add(new Karbari(41, "چند خانواري", 1));
            karbaris.add(new Karbari(91, "خانگي خارج از محدوده", 1));
            karbaris.add(new Karbari(93, "تجاري مسكوني خارج از محدوده", 1));
            karbaris.add(new Karbari(96, "آب خام جنگل كاري", 1));
            karbaris.add(new Karbari(98, "درحال ساختمان تجاري مسکوني", 1));
            karbaris.add(new Karbari(151, "ساخت و ساز ويژه خانگي", 1));
            karbaris.add(new Karbari(153, "ساخت و ساز ويژه تجاري مسکوني", 1));

            karbaris.add(new Karbari(4, "اماکن مذهبي رايگان", 2));
            karbaris.add(new Karbari(5, "مسجد", 2));
            karbaris.add(new Karbari(6, "اماکن مذهبي و واحدهاي وابسته", 2));
            karbaris.add(new Karbari(7, "گرمابه ها", 2));
            karbaris.add(new Karbari(9, "دانشگاه ها", 2));
            karbaris.add(new Karbari(10, "مركز آموزشي و پرورشي", 2));
            karbaris.add(new Karbari(12, "مركز بهداشتي و درماني دولتي", 2));
            karbaris.add(new Karbari(16, "آب خام موسسات دولتي", 2));
            karbaris.add(new Karbari(18, "نانوايي غيرسنتي", 2));
            karbaris.add(new Karbari(19, "مراکز بهزيستي و کميته امداد", 2));
            karbaris.add(new Karbari(20, "عمومي اشتراکي", 2));
            karbaris.add(new Karbari(21, "مراكز ورزشي", 2));
            karbaris.add(new Karbari(22, "مراکز فرهنگي دولتي", 2));
            karbaris.add(new Karbari(23, "مراكز فرهنگي خصوصي", 2));
            karbaris.add(new Karbari(24, "سينماها", 2));
            karbaris.add(new Karbari(25, "عمومي دولتي", 2));
            karbaris.add(new Karbari(29, "نيروهاي مسلح", 2));
            karbaris.add(new Karbari(31, "حجمي محله", 2));
            karbaris.add(new Karbari(32, "مراکز نگهداري ايتام و افراد بي سرپرست و معلولين", 2));
            karbaris.add(new Karbari(34, "صدا و سيما", 2));
            karbaris.add(new Karbari(36, "شهرکهاي حومه", 2));
            karbaris.add(new Karbari(37, "آب تانكري", 2));
            karbaris.add(new Karbari(38, "آب خام فرهنگي آموزشي", 2));
            karbaris.add(new Karbari(39, "انشعاب آتش نشاني", 2));
            karbaris.add(new Karbari(40, "انشعابات موقت كارگاهي", 2));
            karbaris.add(new Karbari(43, "آب آزاد", 2));
            karbaris.add(new Karbari(44, "فصلي", 2));
            karbaris.add(new Karbari(45, "آب چاه", 2));
            karbaris.add(new Karbari(46, "نانوايي سنتي", 2));
            karbaris.add(new Karbari(90, "تركيدگي", 2));
            karbaris.add(new Karbari(94, "فروش آب به آبفاي روستايي", 2));
            karbaris.add(new Karbari(99, "آزاد توافقي", 2));
            karbaris.add(new Karbari(100, "تلف", 2));
            karbaris.add(new Karbari(101, "نشتي تجاري", 2));
            karbaris.add(new Karbari(102, "نشتي عمومي", 2));
            karbaris.add(new Karbari(103, "درحال ساختمان آزاد و بنايي", 2));
            karbaris.add(new Karbari(104, "در حال ساختمان تجاري", 2));
            karbaris.add(new Karbari(105, "درحال ساختمان آموزشي و اماکن مذهبي", 2));
            karbaris.add(new Karbari(150, "ساخت و ساز ويژه آزاد و بنايي", 2));

            karbaris.add(new Karbari(8, "فضاي سبز با كنتور", 3));
            karbaris.add(new Karbari(26, "گلزار شهدا و بقاع متبرکه", 3));

            karbaris.add(new Karbari(2, "تجاري ", 4));
            karbaris.add(new Karbari(13, "مركز بهداشتي و درماني غيردولتي", 4));
            karbaris.add(new Karbari(15, "آب خام تجاري", 4));
            karbaris.add(new Karbari(92, "تجاري خارج از محدوده", 4));
            karbaris.add(new Karbari(97, "فضاي سبز بدون كنتور", 4));
            karbaris.add(new Karbari(152, "ساخت و ساز ويژه تجاري", 4));

            karbaris.add(new Karbari(17, "آب خام صنعتي", 5));
            karbaris.add(new Karbari(35, "صنعتي", 5));
            karbaris.add(new Karbari(42, "بيمارستان آموزشي", 5));

            for (Karbari karbari : karbaris) {
                karbari.save();
            }
        }

        //endregion
    }
    //
    @Override
    protected void onResume() {
        turnOnGpsOrCloseApp();
        super.onResume();
    }

    //

    /**
     * <h1>check if GPS is enabled</h1>
     *<p>check whether GPS is Enabled or not , if is disabled , enable it or exit</p>
     *@since 2016/08/26
     * @author _1101
     */
    private void GpsEnabled(){
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.e("GPS IS:", enabled + "");
        if (!enabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("تنظیمات جی پی اس");
            // Setting Dialog Message
            alertDialog.setMessage("مکان یابی شما غیر فعال است ،آیا مایلید به قسمت تنظیمات مکان یابی منتقل شوید");
            alertDialog.setPositiveButton("تنظیمات", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("بستن برنامه", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            // Showing Alert Message
            alertDialog.show();
        }
    }
    //
    private void forceClose(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        // Setting Dialog Title
        alertDialog.setTitle("مجوز ها کامل نیست");

        // Setting Dialog Message
        alertDialog.setMessage("لطفا با راهبر سیستم تماس حاصل فرمایید");
        alertDialog.setNegativeButton("بستن برنامه", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
}
