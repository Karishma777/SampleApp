package in.explicate.fcm.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.explicate.fcm.model.EmergencyContact;
import in.explicate.fcm.model.NotificationModel;


public class DbHandler {

   // Database Version,database name and table name;
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "fcmdemo";


	//EB tables

	public static final String NOTIFICATION_TABLE = "notification";
	public static final String CONTACT_TABLE = "contacttb";



	private static final String COL_ID = "id";
	private static final String COL_TEXT = "text";
	private static final String COL_TITLE = "title";
	private static final String COL_CLICK_ACTION = "click_action";
	private static final String COL_END_DATE= "end_date";
	private static final String COL_PK= "pk";
	private static final String COL_PRIORITY= "priority";
	private static final String COL_FLAG_READ= "readflag";





	private static final String COL_CREATED_AT="created_at";

	private static final String COL_NAME="name";
	private static final String COL_PHONE="phoneno";
	private static final String COL_MOBILE="mobile";
	private static final String COL_EMAIL="email";
	private static final String COL_DESC="description";
	private static final String COL_CREATED="createdon";
	private static final String COL_MODIFIED="modifiedon";


	//creation of object from DbHelper
	private DbHelper myHelper;
	private final Context myContext;
	private SQLiteDatabase sqlLiteDatabase;
	
	
	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			//like tables


			db.execSQL("CREATE TABLE " + NOTIFICATION_TABLE + " (" +
					COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COL_TITLE + " TEXT, " +
					COL_TEXT + " TEXT, " +
					COL_CLICK_ACTION + " TEXT, " +
					COL_END_DATE + " TEXT, " +
					COL_PK + " TEXT, " +
					COL_PRIORITY + " TEXT, " +
					COL_FLAG_READ + " TEXT," +
					COL_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP);"
			);

			Log.e("QUERY","CREATE TABLE " + CONTACT_TABLE + " (" +
					COL_ID + " INTEGER, " +
					COL_NAME + " TEXT, " +
					COL_PHONE + " TEXT, " +
					COL_MOBILE + " TEXT, " +
					COL_EMAIL + " TEXT, " +
					COL_DESC + " TEXT, " +
					COL_CREATED + " TIMESTAMP, "+
					COL_MODIFIED + " TIMESTAMP);");


			db.execSQL("CREATE TABLE " + CONTACT_TABLE + " (" +
					COL_ID + " INTEGER, " +
					COL_NAME + " TEXT, " +
					COL_PHONE + " TEXT, " +
					COL_MOBILE + " TEXT, " +
					COL_EMAIL + " TEXT, " +
					COL_DESC + " TEXT, " +
					COL_CREATED + " TIMESTAMP, "+
					COL_MODIFIED + " TIMESTAMP);"
			);



		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub


			db.execSQL("DROP TABLE IF EXISTS " +NOTIFICATION_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " +CONTACT_TABLE);
			onCreate(db);

		}
		
		
	}

   public DbHandler(Context c){
	   
	   myContext=c;
   }
   
   public DbHandler open() throws SQLException {
	   
	   myHelper =new DbHelper(myContext);
	   sqlLiteDatabase=myHelper.getWritableDatabase();
	   return this;
   }

   public void close(){
	   
	   myHelper.close();
	   
   }

   /****************BOOKMARK_TABLE Table Tasks*****************************/

	public long insertNotification(NotificationModel item) {

		Log.e("MSG:",item.getBody());
		ContentValues values = new ContentValues();
		values.put(COL_TITLE,item.getTitle());
		values.put(COL_TEXT,item.getBody());
		values.put(COL_CLICK_ACTION,item.getClick_action());
		values.put(COL_END_DATE,item.getEnd_date());
		values.put(COL_PK,item.getPk());
		values.put(COL_PRIORITY,item.getPriority());
		long i = sqlLiteDatabase.insert(NOTIFICATION_TABLE, null, values);

		Log.e("MSG:",i+"");


		return i;

	}

	public List<NotificationModel> getNotifications() throws SQLException
	{

		Log.e("AT","getNotifications");
		List<NotificationModel> list=new ArrayList<>();

		String query="select * from "+NOTIFICATION_TABLE;

		Cursor rs=sqlLiteDatabase.rawQuery(query, null);

		while(rs.moveToNext()){

			Log.e("AT","moveToNext");
			NotificationModel model=new NotificationModel();
			model.setTitle(rs.getString(rs.getColumnIndex(COL_TITLE)));
			model.setBody(rs.getString(rs.getColumnIndex(COL_TEXT)));
			model.setClick_action(rs.getString(rs.getColumnIndex(COL_CLICK_ACTION)));
			model.setEnd_date(rs.getString(rs.getColumnIndex(COL_END_DATE)));
			model.setPk(rs.getString(rs.getColumnIndex(COL_PK)));
			model.setPriority(rs.getString(rs.getColumnIndex(COL_PRIORITY)));
			list.add(model);
		}

		if(!rs.isClosed()){

			rs.close();
		}

		return  list;
	}



	/****************CONTACT_TABLE Table Tasks*****************************/

	public long insertContactTable(EmergencyContact contact) {
		ContentValues values = new ContentValues();
		values.put(COL_ID,contact.getId());
		values.put(COL_NAME,contact.getName());
		values.put(COL_EMAIL,contact.getEmail());
		values.put(COL_PHONE,contact.getPhone());
		values.put(COL_MOBILE,contact.getMobile());
		values.put(COL_DESC,contact.getDescription());
		values.put(COL_CREATED,contact.getCreated());
		values.put(COL_MODIFIED,contact.getModified());
		long i = sqlLiteDatabase.insert(CONTACT_TABLE, null, values);
		return i;

	}

	public List<EmergencyContact> getContacts() throws SQLException
	{

		List<EmergencyContact> list=new ArrayList<>();
		String query="select * from "+CONTACT_TABLE;

		Cursor rs=sqlLiteDatabase.rawQuery(query, null);

		while(rs.moveToNext()){

			EmergencyContact model=new EmergencyContact();
			model.setId(rs.getString(rs.getColumnIndex(COL_ID)));
			model.setName(rs.getString(rs.getColumnIndex(COL_NAME)));
			model.setEmail(rs.getString(rs.getColumnIndex(COL_EMAIL)));
			model.setPhone(rs.getString(rs.getColumnIndex(COL_PHONE)));
			model.setMobile(rs.getString(rs.getColumnIndex(COL_MOBILE)));
			model.setDescription(rs.getString(rs.getColumnIndex(COL_DESC)));
			model.setCreated(rs.getString(rs.getColumnIndex(COL_CREATED)));
			model.setModified(rs.getString(rs.getColumnIndex(COL_MODIFIED)));

			list.add(model);
		}

		if(!rs.isClosed()){

			rs.close();
		}

		return  list;
	}


	public long updateContact(EmergencyContact contact){

		ContentValues values = new ContentValues();
		values.put(COL_ID,contact.getId());
		values.put(COL_NAME,contact.getName());
		values.put(COL_EMAIL,contact.getEmail());
		values.put(COL_PHONE,contact.getPhone());
		values.put(COL_MOBILE,contact.getMobile());
		values.put(COL_DESC,contact.getDescription());
		values.put(COL_CREATED,contact.getCreated());
		values.put(COL_MODIFIED,contact.getModified());

		// updating row
		return sqlLiteDatabase.update(CONTACT_TABLE, values, COL_ID + " = ?",
				new String[] { String.valueOf(contact.getId()) });

	}
}

