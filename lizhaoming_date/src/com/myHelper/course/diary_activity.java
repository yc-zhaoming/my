package com.myHelper.course;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.myHelper.R;


public class diary_activity extends ListActivity {
	
	//回复的关键字
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	//菜单的选择
	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private static final int update = Menu.FIRST + 2;

	private DbAdapter mDbHelper;
	private Cursor mDiaryCursor;
	//AlertDialog.Builder buider=new AlertDialog.Builder(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_list);
		mDbHelper = new DbAdapter(this);
		updateListView();
		

	}

	//更新当前的listacvitity
	private void updateListView() {
		mDbHelper.open();
		mDiaryCursor = mDbHelper.getAllNotes();
		startManagingCursor(mDiaryCursor);
		String[] from = new String[] { DbAdapter.KEY_TITLE,
				DbAdapter.KEY_CREATED };
		int[] to = new int[] { R.id.text1, R.id.created };
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.diary_row, mDiaryCursor, from, to);
		setListAdapter(notes);
		mDbHelper.closeclose();
	}

	//创建一个菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0,"新建").setIcon(R.drawable.new_course);
		//menu.add(0, DELETE_ID, 0, "删除").setIcon(R.drawable.delete);
		menu.add(0, update, 0, "改密").setIcon(R.drawable.delete);
		
		return true;
	}

	//菜单选择
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INSERT_ID:
			createDiary();
			return true;
		case DELETE_ID:
			mDbHelper.open();
			mDbHelper.deleteDiary(getListView().getSelectedItemId());
			mDbHelper.closeclose();
			updateListView();
			return true;
		case update:
			MainActivity.first_week_message.setFirstDate(1, "isfirsttime");
			Intent intent=new Intent(diary_activity.this,diary_login_activity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void createDiary() {
		Intent i = new Intent(this, DiaryEditActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	@Override
	// 需要对position和id进行一个很好的区分
	// position指的是点击的这个ViewItem在当前ListView中的位置
	// 每一个和ViewItem绑定的数据，肯定都有一个id，通过这个id可以找到那条数据。
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Cursor c = mDiaryCursor;
		c.moveToPosition(position);
		Intent i = new Intent(this, DiaryEditActivity.class);
		i.putExtra(DbAdapter.KEY_ROWID, id);
		i.putExtra(DbAdapter.KEY_TITLE, c.getString(c
				.getColumnIndexOrThrow(DbAdapter.KEY_TITLE)));
		i.putExtra(DbAdapter.KEY_BODY, c.getString(c
				.getColumnIndexOrThrow(DbAdapter.KEY_BODY)));
		startActivityForResult(i, ACTIVITY_EDIT);
	}
	
		
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
		//do something
			Intent intent = new Intent();			
			intent.setClass(diary_activity.this, MainActivity.class);
			startActivity(intent);
		
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {

		//do something
			
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {

		//这里操作是没有返回结果的
		}
		return super.onKeyDown(keyCode, event);
		}
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		updateListView();
	}
}
