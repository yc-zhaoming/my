

package com.myHelper.course;

import com.myHelper.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class DiaryEditActivity extends Activity {

	private EditText mTitleText;
	private EditText mBodyText;
	private Long mRowId;
	private DbAdapter mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = new DbAdapter(this);
		setContentView(R.layout.diary_edit);

		mTitleText = (EditText) findViewById(R.id.title);
		mBodyText = (EditText) findViewById(R.id.body);

		Button confirmButton = (Button) findViewById(R.id.confirm);
		Button bt_delete=(Button) findViewById(R.id.buttondelete);

		mRowId = null;
		final Bundle extras = getIntent().getExtras();
		//判断是否为编辑状态
		if (extras != null) {
			String title = extras.getString(DbAdapter.KEY_TITLE);
			String body = extras.getString(DbAdapter.KEY_BODY);
			mRowId = extras.getLong(DbAdapter.KEY_ROWID);

			if (title != null) {
				mTitleText.setText(title);
			}
			if (body != null) {
				mBodyText.setText(body);
			}
		}
		else{
			bt_delete.setVisibility(View.INVISIBLE);
		}
		bt_delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
			
				if(extras!=null){
					mDbHelper.open();				
				mDbHelper.deleteDiary(mRowId);
				mDbHelper.closeclose();
				finish();
				}
				else{
					Toast.makeText(getApplicationContext(), "当前是新增状态，不可删除", 0).show();
				}
			}
			
		});
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mDbHelper.open();
				String title = mTitleText.getText().toString();
				String body = mBodyText.getText().toString();
				if (mRowId != null) {
					mDbHelper.updateDiary(mRowId, title, body);
				} else
					mDbHelper.createDiary(title, body);
				Intent mIntent = new Intent();
				setResult(RESULT_OK, mIntent);
				mDbHelper.closeclose();
				finish();
			}

		});
	}
}
