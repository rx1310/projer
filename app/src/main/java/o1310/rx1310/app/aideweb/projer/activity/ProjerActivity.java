/*
 * @author      rx1310 <rx1310@inbox.ru>
 * @copyright   Copyright (c) o1310, 2020
 * @license     MIT License
 */

package o1310.rx1310.app.aideweb.projer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.preference.PreferenceManager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.text.TextUtils;

import o1310.rx1310.util.unzipper.Unzipper;

import o1310.rx1310.app.aideweb.projer.R;
import o1310.rx1310.app.aideweb.projer.utility.PacManUtils;

public class ProjerActivity extends AppCompatActivity implements View.OnClickListener {

	Toolbar mToolbar;
	Button mCreateProject;
	EditText mInputProjectName;
	ImageView mCreatorStatusView;
	TextView mProjectDescView;
	
	String mProjectAssetFile, mProjectDesc, mDefaultDir4Projects, mAideWebPackageName;
	boolean mRunAideAfterProjectCreation;
	
	SharedPreferences mSharedPreferences;
	Intent mIntent;
	
	CreatorTask mCreatorTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_projer);

		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mIntent = getIntent();
		
		mProjectAssetFile = mIntent.getStringExtra("PROJECT_ASSET_FILE");
		mProjectDesc = mIntent.getStringExtra("PROJECT_DESC");
		
		mDefaultDir4Projects = mSharedPreferences.getString("defaultDir", "AppProjects");
		mAideWebPackageName = mSharedPreferences.getString("dbg_aideCustomPackageName", "com.aide.web");
		
		mRunAideAfterProjectCreation = mSharedPreferences.getBoolean("runAideAfterProjectCreation", false);
		
		mToolbar = findViewById(R.id.ui_view_toolBar);
		
		setUI();
		
		mInputProjectName = findViewById(R.id.ui_projer_view_inputProjectName);
		mInputProjectName.setHint(R.string.hint_projer_inputProjectName);
		
		mCreateProject = findViewById(R.id.ui_projer_view_createProject);
		mCreateProject.setOnClickListener(this);
		
		mCreatorStatusView = findViewById(R.id.ui_projer_view_creatorStatus);
		mCreatorStatusView.setImageResource(R.drawable.ic_wait);
		
		mProjectDescView = findViewById(R.id.ui_projer_view_textProjectDesc);
		mProjectDescView.setText(mProjectDesc);
		
	}
	
	void setUI() {
		
		// Настройка Toolbar
		setSupportActionBar(mToolbar); 
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		
	}
	
	public void onClick(View v) {

		switch(v.getId()) {
			
			case R.id.ui_projer_view_createProject:
				
				if (TextUtils.isEmpty(mInputProjectName.getText().toString())) {
					
					mInputProjectName.setError(getString(R.string.msg_error_emptyProjectName));
					
				} else {
					
					mCreatorTask = new CreatorTask();
					mCreatorTask.execute();
					
				}
				
				break;

			default: break;

		}

	}
	
	public static void startWizard(Context context, String assetFileName, String projectDesc) {
		
		Intent i = new Intent(context, ProjerActivity.class);
		i.putExtra("PROJECT_ASSET_FILE", assetFileName);
		i.putExtra("PROJECT_DESC", projectDesc);
		context.startActivity(i);
		
	} 
	
	class CreatorTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			mCreatorStatusView.setVisibility(View.VISIBLE);
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			Unzipper.unzipFromAssets(ProjerActivity.this, mProjectAssetFile, "/sdcard/_projer/" + mDefaultDir4Projects + "/" + mInputProjectName.getText().toString());
			
			if (mRunAideAfterProjectCreation) {
				PacManUtils.startApp(ProjerActivity.this, mAideWebPackageName);
			}
			
			return null;
			
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			mCreatorStatusView.setImageResource(R.drawable.ic_done);
			
			// Скрываем иконку
			Handler handler = new Handler(); 
			handler.postDelayed(new Runnable() {
				public void run() {
					mCreatorStatusView.setVisibility(View.GONE);
				} 
			}, 2000);
			
		}
		
	}
	
}
