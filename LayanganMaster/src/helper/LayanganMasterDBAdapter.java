package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LayanganMasterDBAdapter {

	private static final String TAG = "LayanganMasterDBAdapter";
	private static final String DATABASE_NAME = "LayanganMaster";
	private static final String DATABASE_TABLE1 = "USER";
	private static final String DATABASE_TABLE2 = "ACHIEVEMENT";
	private static final String DATABASE_TABLE3 = "BENGKEL";

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE1 = "CREATE TABLE USER(username TEXT PRIMARY KEY,"
			+ "exp INTEGER, level INTEGER, speed INTEGER, defense INTEGER,"
			+ "attack INTEGER, HP INTEGER, isSound INTEGER, isCamera INTEGER, coin INTEGER);";

	private static final String DATABASE_CREATE2 =  "CREATE TABLE ACHIEVEMENT(name TEXT PRIMARY KEY, isAchieve INTEGER);";
	
	private static final String DATABASE_CREATE3 =  "CREATE TABLE BENGKEL(name TEXT PRIMARY KEY, isBuy INTEGER);";
	
	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public LayanganMasterDBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	public void dropDatabase(){
		DBHelper.onUpgrade(db, DATABASE_VERSION, DATABASE_VERSION);
	}
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE1);
			db.execSQL(DATABASE_CREATE2);
			db.execSQL(DATABASE_CREATE3);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE1);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE2);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE3);
			onCreate(db);
		}
	}

	// ---opens the database---
	public LayanganMasterDBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// ---insert a title into the database---
	
	public long insertUser(String username, int exp, int level, int speed, int defense
			, int attack, int HP, int isSound, int isCamera, int coin) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("username", username);
		initialValues.put("exp", exp);
		initialValues.put("level", level);
		initialValues.put("speed", speed);
		initialValues.put("defense", defense);
		initialValues.put("attack", attack);
		initialValues.put("HP", HP);
		initialValues.put("isSound", isSound);
		initialValues.put("isCamera", isCamera);
		initialValues.put("coin", coin);
		return db.insert(DATABASE_TABLE1, null, initialValues);
		
	}

	public long insertAchievement(String name,	int isAchieve) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("name", name);
		initialValues.put("isAchieve", isAchieve);
		return db.insert(DATABASE_TABLE2, null, initialValues);
	}

	
	public long insertBengkel(String name,	int isBuy) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("name", name);
		initialValues.put("isBuy", isBuy);
		return db.insert(DATABASE_TABLE3, null, initialValues);
	}
	
	public void updatePower(String username, int speed, int defense, int attack, int HP){
		db.execSQL("update "+DATABASE_TABLE1+" set speed='"+speed+"' , defense='"+defense+"' , attack='"+attack+"' , HP='"+HP+"' where username ='"+username+"'");
	}
	
	public void updateEXP(String username, int exp){
		db.execSQL("update "+DATABASE_TABLE1+" set exp='"+exp+"' where username ='"+username+"'");
	}
	
	public void updateCoin(String username, int coin){
		db.execSQL("update "+DATABASE_TABLE1+" set coin='"+coin+"' where username ='"+username+"'");
	}
	
	public void updateLevel(String username, int level){
		db.execSQL("update "+DATABASE_TABLE1+" set level='"+level+"' where username ='"+username+"'");
	}
	
	public void updateCamera(String username, int camera){
		db.execSQL("update "+DATABASE_TABLE1+" set isCamera='"+camera+"' where username ='"+username+"'");
	}

	public void updateSound(String username, int sound){
		db.execSQL("update "+DATABASE_TABLE1+" set isSound='"+sound+"' where username ='"+username+"'");
	}
	
	public void updateAchievement(String name, int isAchieve){
		db.execSQL("update "+DATABASE_TABLE2+" set isAchieve='"+isAchieve+"' where name ='"+name+"'");
	}
	
	public void updateBengkel(String name, int isBuy){
		db.execSQL("update "+DATABASE_TABLE3+" set isBuy='"+isBuy+"' where name ='"+name+"'");
	}
	
	public boolean isCamera(String username){
		final String MY_QUERY = "SELECT isCamera FROM USER where "
				+ "username=?";

		Cursor c = db.rawQuery(MY_QUERY, new String[] { String.valueOf(username)});
		c.moveToFirst();
		if(Integer.parseInt(c.getString(0)) == 1) return true;
		else return false;
	}
	
	public boolean isSound(String username){
		final String MY_QUERY = "SELECT isSound FROM USER where "
				+ "username=?";

		Cursor c = db.rawQuery(MY_QUERY, new String[] { String.valueOf(username)});
		c.moveToFirst();
		if(Integer.parseInt(c.getString(0)) == 1) return true;
		else return false;
	}
	
	// ---retrieves all data---

	public int getHP(String username) {
		final String MY_QUERY = "SELECT HP FROM USER "
				+ "where username=?";
		Cursor c = db.rawQuery(MY_QUERY, new String[] { String.valueOf(username) });
		c.moveToFirst();
		return Integer.parseInt(c.getString(0));
	}
	
	public int getSpeed(String username) {
		final String MY_QUERY = "SELECT speed FROM USER "
				+ "where username=?";
		Cursor c = db.rawQuery(MY_QUERY, new String[] { String.valueOf(username) });
		c.moveToFirst();
		return Integer.parseInt(c.getString(0));
	}
	
	public int getAttack(String username) {
		final String MY_QUERY = "SELECT attack FROM USER "
				+ "where username=?";
		Cursor c = db.rawQuery(MY_QUERY, new String[] { String.valueOf(username) });
		c.moveToFirst();
		return Integer.parseInt(c.getString(0));
	}
	
	public int getDefense(String username) {
		final String MY_QUERY = "SELECT defense FROM USER "
				+ "where username=?";
		Cursor c = db.rawQuery(MY_QUERY, new String[] { String.valueOf(username) });
		c.moveToFirst();
		return Integer.parseInt(c.getString(0));
	}
	
	public int getLevel(String username) {
		final String MY_QUERY = "SELECT level FROM USER "
				+ "where username=?";
		Cursor c = db.rawQuery(MY_QUERY, new String[] { String.valueOf(username) });
		c.moveToFirst();
		return Integer.parseInt(c.getString(0));
	}
	
	public int getEXP(String username) {
		final String MY_QUERY = "SELECT exp FROM USER "
				+ "where username=?";
		Cursor c = db.rawQuery(MY_QUERY, new String[] { String.valueOf(username) });
		c.moveToFirst();
		return Integer.parseInt(c.getString(0));
	}
	
	public int getCoin(String username) {
		final String MY_QUERY = "SELECT coin FROM USER "
				+ "where username=?";
		Cursor c = db.rawQuery(MY_QUERY, new String[] { String.valueOf(username) });
		c.moveToFirst();
		return Integer.parseInt(c.getString(0));
	}
	
	public boolean getAchievement(String name) {
		final String MY_QUERY = "SELECT isAchieve FROM ACHIEVEMENT where name=?";
		Cursor c = db.rawQuery(MY_QUERY, new String[] { String.valueOf(name)});
		c.moveToFirst();
		if(Integer.parseInt(c.getString(0)) == 1) return true;
		else return false;
	}

	public boolean getBengkel(String name) {
		final String MY_QUERY = "SELECT isBuy FROM BENGKEL where name=?";
		Cursor c = db.rawQuery(MY_QUERY, new String[] { String.valueOf(name)});
		c.moveToFirst();
		if(Integer.parseInt(c.getString(0)) == 1) return true;
		else return false;
	}
	
	public boolean isNew(String username){
		final String MY_QUERY = "SELECT * FROM USER where "
				+ "username=?";

		Cursor c = db.rawQuery(MY_QUERY, new String[] { String.valueOf(username)});
		if(c.moveToFirst()) return false;
		else return true;
	}
	
	public int getAchievementCount(){
		final String MY_QUERY = "SELECT * FROM Achievement ";

		Cursor c = db.rawQuery(MY_QUERY, new String[] { });
		return c.getCount();
	}
}