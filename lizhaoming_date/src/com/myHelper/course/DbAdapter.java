package com.myHelper.course;

import java.util.Calendar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {

	public static final String KEY_TITLE = "title";
	public static final String KEY_BODY = "body";
	public static final String KEY_ROWID = "_id";
	public static final String KEY_CREATED = "created";
	
	
	public static final String KEY_ROWID_COURSE = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_START = "start";
	public static final String KEY_END = "end";
	public static final String KEY_INDEX = "course_index";
	public static final String KEY_PLACE = "place";
	public static final String KEY_WEEK_INDEX = "week_index";
	
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	//����diary��
	private static final String DATABASE_CREATE = "create table diary " +
			"(_id integer primary key autoincrement, "
			+ "title text not null, body text not null, created text not null);";
	//����course��
	private static final String DATABASE_CREATE_COURSE = "create table course " +
	"(_id integer not null primary key autoincrement, name text not null, " +
	"start integer not null, end integer not null, " + "course_index text not null," +
	" place text not null, week_index text not null);";

	//������ʼֵ
	private static final String DATABASE_NAME = "database.db";
	private static final String DATABASE_TABLE_DIARY = "diary";
	private static final String DATABASE_TABLE_COURSE = "course";
	private static final int DATABASE_VERSION = 1;

	private final Context mCtx;

	//���ݿ���
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
			db.execSQL(DATABASE_CREATE_COURSE);
			Log.e("create", "111");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS diary");
			db.execSQL("DROP TABLE IF EXISTS course");
			onCreate(db);
		}
	}

	public DbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	//�����ݿ�
	public DbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		Log.e("open", "222");
		return this;
	}

	//�ر����ݿ�
	public void closeclose() {
		Log.e("close", "asdf");
		mDbHelper.close();
	}

	//����һ���ռ�
	public long createDiary(String title, String body) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_BODY, body);
		Calendar calendar = Calendar.getInstance();
		String created = calendar.get(Calendar.YEAR) + "��"
				+ calendar.get(Calendar.MONTH) + "��"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "��"
				+ calendar.get(Calendar.HOUR_OF_DAY) + "ʱ"
				+ calendar.get(Calendar.MINUTE) + "��";
		initialValues.put(KEY_CREATED, created);
		return mDb.insert(DATABASE_TABLE_DIARY, null, initialValues);
	}

	//ɾ��ѡ���ռ�
	public boolean deleteDiary(long rowId) {

		return mDb.delete(DATABASE_TABLE_DIARY, KEY_ROWID + "=" + rowId, null) > 0;
	}

	//�õ������ռ�
	public Cursor getAllNotes() {

		return mDb.query(DATABASE_TABLE_DIARY, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_BODY, KEY_CREATED }, null, null, null, null, null);
	}

	//�õ��ƶ��ռ�
	public Cursor getDiary(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_TABLE_DIARY, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_BODY, KEY_CREATED }, KEY_ROWID + "=" + rowId, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	//����diary��
	public boolean updateDiary(long rowId, String title, String body) {
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		args.put(KEY_BODY, body);
		Calendar calendar = Calendar.getInstance();
		String created = calendar.get(Calendar.YEAR) + "��"
				+ calendar.get(Calendar.MONTH) + "��"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "��"
				+ calendar.get(Calendar.HOUR_OF_DAY) + "ʱ"
				+ calendar.get(Calendar.MINUTE) + "��";
		args.put(KEY_CREATED, created);

		return mDb.update(DATABASE_TABLE_DIARY, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	//����course
	public long createCourse(String name, int start, int end, String course_index, String place,String week_index_str) 
	{
		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_START, start);
		initialValues.put(KEY_END, end);
		initialValues.put(KEY_INDEX, course_index);
		initialValues.put(KEY_PLACE, place);
		initialValues.put(KEY_WEEK_INDEX, week_index_str);
		Log.e("createCourse", "333");
		return mDb.insert(DATABASE_TABLE_COURSE, null, initialValues);
	}

	//ɾ��һ��course
	public boolean deleteCourse(long rowId) {

		return mDb.delete(DATABASE_TABLE_COURSE, KEY_ROWID_COURSE + "=" + rowId, null) > 0;
	}

	//�õ��ڼ��ܡ��ܼ��Ŀγ�
	public Cursor getCourseOfWeek(int week,String index){
		Log.e("getAllCourses", "startgetAllCourses");

		return mDb.query(DATABASE_TABLE_COURSE, new String[] { KEY_ROWID_COURSE, KEY_NAME, KEY_PLACE, KEY_INDEX, KEY_WEEK_INDEX },
				KEY_START + "<=" + week +" and " +KEY_END + ">=" + week +" and " +KEY_WEEK_INDEX+"='"+index+"'", null,null, null, KEY_INDEX );
	}
	//�õ���ָ�������ڵĿγ�
	public Cursor getAllCourses(int weeks) {
		Log.e("getAllCourses", "startgetAllCourses");

		return mDb.query(DATABASE_TABLE_COURSE, new String[] { KEY_ROWID_COURSE, KEY_NAME, KEY_PLACE, KEY_INDEX, KEY_WEEK_INDEX },
				KEY_START + "<=" + weeks +" and " +KEY_END + ">=" + weeks , null,null, null, KEY_WEEK_INDEX );
	}

	//�õ����еĿγ���Ϣ
	public Cursor getCourse(long rowId, String name) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_TABLE_COURSE, new String[] { KEY_NAME, KEY_PLACE, KEY_INDEX }, 
				KEY_NAME + "=" + name, null, null,null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		Log.e("getCourse", "555");
		return mCursor;

	}
}
