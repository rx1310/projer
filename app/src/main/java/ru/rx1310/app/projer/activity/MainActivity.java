/*
 * @author      rx1310 <rx1310@inbox.ru>
 * @copyright   Copyright (c) rx1310, 2020
 * @license     MIT License
 */

package ru.rx1310.app.projer.activity;

import android.Manifest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;

import android.preference.PreferenceManager;

import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ru.rx1310.app.projer.R;
import ru.rx1310.app.projer.activity.MainActivity;
import ru.rx1310.app.projer.adapter.TheFragmentPagerAdapter;
import ru.rx1310.app.projer.fragment.SimpleProjectsFragment;
import ru.rx1310.app.projer.utility.AppUtils;
import ru.rx1310.app.projer.fragment.BootstrapProjectsFragment;

public class MainActivity extends AppCompatActivity {

	Toolbar mToolbar;
	ViewPager mViewPager;
	TabLayout mTabLayout;
	
	boolean dbg_showTestTabs;
	
	SharedPreferences mSharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		if (AppUtils.getAppVersion(this, getPackageName(), false).contains("b")) {
			setTitle(getString(R.string.activity_main) + " (beta)");
		}
		
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		dbg_showTestTabs = mSharedPreferences.getBoolean("dbg_showTestTabs", false);
		
		mToolbar = findViewById(R.id.ui_view_toolBar);
		mToolbar.setNavigationIcon(R.drawable.ic_close);
		
		mViewPager = findViewById(R.id.ui_view_viewPager);
		mTabLayout = findViewById(R.id.ui_view_tabLayout);
		
		setSupportActionBar(mToolbar);
		setViewPager(mViewPager);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		
		mTabLayout.setupWithViewPager(mViewPager);
		
		// Запрос разрешений
		if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String []{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
		
	}
	
	private void setViewPager(ViewPager vp) {

		TheFragmentPagerAdapter a = new TheFragmentPagerAdapter(getSupportFragmentManager());

		a.addFragment(new SimpleProjectsFragment(), getString(R.string.tab_projects_simple));
		
		if (dbg_showTestTabs) {
			a.addFragment(new BootstrapProjectsFragment(), getString(R.string.tab_projects_bootstrap));
		}
		
		vp.setAdapter(a);

	}
	
	public boolean onCreateOptionsMenu(Menu m) {
		
		getMenuInflater().inflate(R.menu.main, m);
		
		return super.onCreateOptionsMenu(m);
		
	}
    
    public boolean onOptionsItemSelected(MenuItem mi) {

        int id = mi.getItemId();

        switch(id) {
			
			case android.R.id.home:
				finish();
				return true;

            case R.id.menu_main_settings :
				startActivity(new Intent(this, SettingsActivity.class));
				return true;
				
			case R.id.menu_main_report : 
				AppUtils.sendReport(this);
				return true;
				
        }

        return super.onOptionsItemSelected(mi);
		
    }
	
}
