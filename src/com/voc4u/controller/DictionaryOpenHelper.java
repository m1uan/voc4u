package com.voc4u.controller;

import java.util.ArrayList;

import com.voc4u.core.DBConfig;
import com.voc4u.core.InitData;






import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DictionaryOpenHelper extends SQLiteOpenHelper
{

	private static final String OR = " OR ";

	public static final int CZEN_GROUP = 0;

	private static final String TAG = "DictionaryOpenHelper";

	SQLiteDatabase mDB = null;

	public DictionaryOpenHelper(Context context)
	{
		super(context, DBConfig.WORD_TABLE_NAME, null,
				DBConfig.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{

		db.execSQL(DBConfig.DICTIONARY_TABLE_CREATE);
		// db.execSQL("INSERT INTO "+ DBConfig.DATABASE_NAME +
		// " ("+ DBConfig.LANG_ID_COLUMN +", "+ DBConfig.WORD_1_COLUMN +", "+
		// DBConfig.WORD_2_COLUMN +", "+ DBConfig.ITER_COLUMN +") VALUES" +
		// " (0, 'hello', 'ahoj', 0)");
//		int enable = 1;
//		
//		for (int i = 0; i != InitData.nati.length; i++)
//		{
//			
//			if(InitData.nati[i] == null || InitData.nati[i].length() < 1 ||
//					InitData.lern[i] == null || InitData.lern[i].length() < 1)
//				continue;
//			
//			String query = String.format(DBConfig.DICTIONARY_TABLE_INSERT, 0,
//					prepareString(InitData.nati[i]), prepareString(InitData.lern[i]), enable, enable);
//			Log.d("009", query);
//			db.execSQL(query);
//			
//			
//			if(i == 100)
//				enable = 0;
//		}

	}

	private Object prepareString(String string)
	{
		return string.replace("\'", ".");
	}

	public ArrayList<String> getWocabulary()
	{
		ArrayList<String> voc = new ArrayList<String>();
		mDB = getWritableDatabase();
		Cursor c = mDB.rawQuery("SELECT * FROM " + DBConfig.WORD_TABLE_NAME,
				null);

		if (c != null)
		{
			int firstWordColumn = c.getColumnIndex(DBConfig.WORD_1_COLUMN);
			c.moveToFirst();
			do
			{
				String s = c.getString(firstWordColumn);
				voc.add(s);
			} while (c.moveToNext());
		}

		return voc;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if(oldVersion == 1)
		{
			
		}
	}

	public void addWord(int group, String text, String text2)
	{
		addWordEx(group, text, text2, 0, 0);

	}

	/**
	 * first unsetup word for setup
	 * @param czenGroup
	 * @param b
	 * @return
	 */
	public Word getFirstUnsetupWord()
	{
		Word pw = null;

		mDB = getWritableDatabase();
		Cursor c = rawQuerySQL(mDB, DBConfig.DICTIONARY_TABLE_UNSETUP_WORD);

		if (c != null)
		{
			int firstWordColumn = c.getColumnIndex(DBConfig.WORD_1_COLUMN);
			int secondWordColumn = c.getColumnIndex(DBConfig.WORD_2_COLUMN);
			int idWordColumn = c.getColumnIndex(DBConfig.ID_COLUMN);
			int weight1Column = c.getColumnIndex(DBConfig.WEIGHT_1_COLUMN);
			int weight2Column = c.getColumnIndex(DBConfig.WEIGHT_2_COLUMN);
			
			if(!c.moveToFirst())
				return null;

			String word = c.getString(firstWordColumn);
			String word2 = c.getString(secondWordColumn);
			int id = c.getInt(idWordColumn);
			int weight = c.getInt(weight1Column);
			int weight2 = c.getInt(weight2Column);
			pw = new Word(id, word, word2, weight, weight2);
		}

		return pw;
	}

	public ArrayList<Word> getPublicWords(int czenGroup, boolean selectByWeight1)
	{
		
		mDB = getWritableDatabase();
		Cursor c = rawQuerySQL(mDB, DBConfig.DICTIONARY_TABLE_SELECT_20_WEIGHT1);

		ArrayList<Word> list = null;
		
		if (c != null && c.moveToFirst())
		{
			list = new ArrayList<Word>();
			int firstWordColumn = c.getColumnIndex(DBConfig.WORD_1_COLUMN);
			int secondWordColumn = c.getColumnIndex(DBConfig.WORD_2_COLUMN);
			int idWordColumn = c.getColumnIndex(DBConfig.ID_COLUMN);
			int weight1Column = c.getColumnIndex(DBConfig.WEIGHT_1_COLUMN);
			int weight2Column = c.getColumnIndex(DBConfig.WEIGHT_2_COLUMN);

			do
			{
				String word = c.getString(firstWordColumn);
				String word2 = c.getString(secondWordColumn);
				int id = c.getInt(idWordColumn);
				int weight = c.getInt(weight1Column);
				int weight2 = c.getInt(weight2Column);
				list.add(new Word(id, word, word2, weight, weight2));
			} while (c.moveToNext());
		}

		return list;
	}

	

	public ArrayList<Word> getPublicWords2(int czenGroup, boolean b)
	{
		mDB = getWritableDatabase();
		Cursor c = rawQuerySQL(mDB, DBConfig.DICTIONARY_TABLE_SELECT_20_WEIGHT2);

		ArrayList<Word> list = null;
		
		if (c != null && c.moveToFirst())
		{
			list = new ArrayList<Word>();
			int firstWordColumn = c.getColumnIndex(DBConfig.WORD_1_COLUMN);
			int secondWordColumn = c.getColumnIndex(DBConfig.WORD_2_COLUMN);
			int idWordColumn = c.getColumnIndex(DBConfig.ID_COLUMN);
			int weight1Column = c.getColumnIndex(DBConfig.WEIGHT_1_COLUMN);
			int weight2Column = c.getColumnIndex(DBConfig.WEIGHT_2_COLUMN);

			do
			{
				String word = c.getString(firstWordColumn);
				String word2 = c.getString(secondWordColumn);
				int id = c.getInt(idWordColumn);
				int weight = c.getInt(weight1Column);
				int weight2 = c.getInt(weight2Column);
				list.add(new Word(id, word, word2, weight, weight2));
			} while (c.moveToNext());
		}

		return list;
	}

	public void updateWordWeight(int id, int weight)
	{
		SQLiteDatabase DB = getWritableDatabase();
		String query = String.format(DBConfig.DICTIONARY_TABLE_UPDATE_WEIGHT1,
				weight, id);
		execSQL(DB, query);

	}

	private void execSQL(SQLiteDatabase DB, String query)
	{
		DB.execSQL(query);
		Log.d(TAG, query);
	}
	
	private Cursor rawQuerySQL(SQLiteDatabase DB, String query)
	{
		Log.d(TAG, query);
		
		return DB.rawQuery(query, null);
	}
	
	public void updateWordWeight2(int id, int weight)
	{
		mDB = getWritableDatabase();
		String query = String.format(DBConfig.DICTIONARY_TABLE_UPDATE_WEIGHT2,
				weight, id);
		execSQL(mDB, query);
	}

	public String createWhereFromIds(int[] ids)
	{
		String result = "WHERE ";
		if(ids != null && ids.length > 0)
		{
			result += " NOT (";
			for(int id : ids)
			{
				result += DBConfig.ID_COLUMN + "=" + String.valueOf(id) + OR;
			}
			result = result.substring(0, result.length() - OR.length()) + ")";
			
			result += " AND ";
		}
		//return " Where weight_1 > 20"; 
		
		
		return result +  DBConfig.WEIGHT_1_COLUMN + " > 0 AND " + DBConfig.WEIGHT_2_COLUMN + " > 0 ";
	}
	
	public ArrayList<Word> getPublicWords(int czenGroup, boolean b, int[] ids)
	{
		mDB = getWritableDatabase();
		String query = String.format(DBConfig.DICTIONARY_TABLE_SELECT_20_WEIGHT1_WHERE, createWhereFromIds(ids) );
		
		Cursor c = rawQuerySQL(mDB, query);

		ArrayList<Word> list = null;
		
		if (c != null && c.moveToFirst())
		{
			list = new ArrayList<Word>();
			int firstWordColumn = c.getColumnIndex(DBConfig.WORD_1_COLUMN);
			int secondWordColumn = c.getColumnIndex(DBConfig.WORD_2_COLUMN);
			int idWordColumn = c.getColumnIndex(DBConfig.ID_COLUMN);
			int weight1Column = c.getColumnIndex(DBConfig.WEIGHT_1_COLUMN);
			int weight2Column = c.getColumnIndex(DBConfig.WEIGHT_2_COLUMN);

			do
			{
				String word = c.getString(firstWordColumn);
				String word2 = c.getString(secondWordColumn);
				int id = c.getInt(idWordColumn);
				int weight = c.getInt(weight1Column);
				int weight2 = c.getInt(weight2Column);
				list.add(new Word(id, word, word2, weight, weight2));
			} while (c.moveToNext());
			
			c.close();
		}

		return list;
	}
	
	public ArrayList<Word> getPublicWords2(int czenGroup, boolean b, int[] ids)
	{
		mDB = getWritableDatabase();
		String query = String.format(DBConfig.DICTIONARY_TABLE_SELECT_20_WEIGHT2_WHERE, createWhereFromIds(ids));
		
		Cursor c = mDB.rawQuery(query, null);

		ArrayList<Word> list = null;
		
		if (c != null && c.moveToFirst())
		{
			list = new ArrayList<Word>();
			int firstWordColumn = c.getColumnIndex(DBConfig.WORD_1_COLUMN);
			int secondWordColumn = c.getColumnIndex(DBConfig.WORD_2_COLUMN);
			int idWordColumn = c.getColumnIndex(DBConfig.ID_COLUMN);
			int weight1Column = c.getColumnIndex(DBConfig.WEIGHT_1_COLUMN);
			int weight2Column = c.getColumnIndex(DBConfig.WEIGHT_2_COLUMN);

			do
			{
				String word = c.getString(firstWordColumn);
				String word2 = c.getString(secondWordColumn);
				int id = c.getInt(idWordColumn);
				int weight = c.getInt(weight1Column);
				int weight2 = c.getInt(weight2Column);
				list.add(new Word(id, word, word2, weight, weight2));
			} while (c.moveToNext());
			c.close();
		}

		return list;
	}

	public void updateWordWeights(Word pw)
	{
		SQLiteDatabase DB = getWritableDatabase();
		String query = String.format(DBConfig.DICTIONARY_TABLE_UPDATE_WEIGHTS,
				pw.getWeight(), pw.getWeight2(), pw.getId());
		execSQL(DB, query);
	}
	
	public long getCount()
	{
		 String sql = "SELECT COUNT(*) FROM " + DBConfig.WORD_TABLE_NAME;
		    SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
		    long count = statement.simpleQueryForLong();
		    return count;
	}
	
	
	public boolean getEnabled(int idFrom, int idTo)
	{
		 String sql = String.format( DBConfig.GET_ENABLED, idFrom, idTo) ;
		    SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
		    long count = statement.simpleQueryForLong();
		    return count > 0;
	}
	
	public void setEnabled(int idFrom, int idTo, boolean enable)
	{
		int enableint = enable ? 1 : 0;
		mDB = getWritableDatabase();
		String query = String.format(DBConfig.SET_ENABLED,
				 enableint, enableint, idFrom, idTo);
		execSQL(mDB, query);
	}

	public Word getPublicWordById(int whereid)
	{
		mDB = getWritableDatabase();
		String query = String.format(DBConfig.DICTIONARY_TABLE_SELECT_BY_ID, whereid);
		
		Cursor c = mDB.rawQuery(query, null);

		Word result = null;
		
		if (c != null && c.moveToFirst())
		{
			int firstWordColumn = c.getColumnIndex(DBConfig.WORD_1_COLUMN);
			int secondWordColumn = c.getColumnIndex(DBConfig.WORD_2_COLUMN);
			int idWordColumn = c.getColumnIndex(DBConfig.ID_COLUMN);
			int weight1Column = c.getColumnIndex(DBConfig.WEIGHT_1_COLUMN);
			int weight2Column = c.getColumnIndex(DBConfig.WEIGHT_2_COLUMN);

			do
			{
				String word = c.getString(firstWordColumn);
				String word2 = c.getString(secondWordColumn);
				int id = c.getInt(idWordColumn);
				int weight = c.getInt(weight1Column);
				int weight2 = c.getInt(weight2Column);
				result = new Word(id, word, word2, weight, weight2);
			} while (c.moveToNext());
			c.close();
		}

		return result;
	}

	public void addWordEx(int lesson, String text, String text2,
			int weight1, int weight2)
	{
		mDB = getWritableDatabase();
		//mDB.execSQL(String.format(DBConfig.DICTIONARY_TABLE_INSERT, group,
		//		text, text2, weight1, weight2));
		ContentValues values = new ContentValues();
		values.put(DBConfig.LANG_LESSON, lesson);
		values.put(DBConfig.WORD_1_COLUMN, text);
		values.put(DBConfig.WORD_2_COLUMN, text2);
		values.put(DBConfig.WEIGHT_1_COLUMN, weight1);
		values.put(DBConfig.WEIGHT_2_COLUMN, weight2);
		
		mDB.insert(DBConfig.WORD_TABLE_NAME, null, values);
		//db.delete(DBAdapter.TableName, "Id=? AND QstnrId=? AND QstnId=?",
        //new String[] { Id.toString(), QuestionnaireId, QuestionId });
		
		Log.d(TAG, "add word: " + text + " with lesson = " + String.valueOf(lesson));
	}
	
	public void unloadLesson(int lesson)
	{
		mDB = getWritableDatabase();
		
		mDB.delete(DBConfig.WORD_TABLE_NAME, DBConfig.LANG_LESSON + "=?",
				new String[] { String.valueOf(lesson) });
		
	}

	/**
	 * get the lesson in database
	 * @param lesson
	 * @return true when the lesson is in database
	 */
	public boolean isLessonLoaded(int lesson)
	{
		String sql = String.format( DBConfig.GET_LESSON_ENABLED, lesson) ;
	    SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
	    long count = statement.simpleQueryForLong();
	    return count > 0;
	}
	
	
	/**
	 * test is any word in database have weight 1, 1
	 * this is mean that user not know this word yet
	 * @return true when exist any word with weight 1,1
	 */
	public boolean isAnyUnknownWord()
	{
		SQLiteStatement statement = getReadableDatabase().compileStatement(DBConfig.EXIST_UNKNOWS_WORDS);
	    long count = statement.simpleQueryForLong();
	    return count > 0;
	}
	
	/**
	 * setup first word with weight 0, 0 to 1, 1
	 */
	public void setupFirstWordWeight()
	{
		Word w = getFirstUnsetupWord();
		
		if(w != null)
		{
			String query = String.format(DBConfig.SETUP_WEIGHT,
				 1, 1, 0, 0, w.getId());
		
			execSQL(getWritableDatabase(), query);
		}
	}
	
	
}