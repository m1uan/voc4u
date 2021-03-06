package com.voc4u.controller;

import java.io.UTFDataFormatException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.voc4u.core.DBConfig;
import com.voc4u.setting.Consts;

public class DictionaryOpenHelper extends SQLiteOpenHelper
{

	private static final String OR = " OR ";

	public static final int CZEN_GROUP = 0;

	private static final String TAG = "DictionaryOpenHelper";

	SQLiteDatabase mDB = null;

	public enum NUM_WORDS_TYPE
	{
		KNOWS,
		UNKNOWS,
		UNUSED,
		ALL
	}
	
	public class RemovedWord
	{
		public long id;
		public String ws_id;
		public String learn;
		public String nativ;
	}
	
	public DictionaryOpenHelper(Context context)
	{
		super(context, DBConfig.WORD_TABLE_NAME, null,
				DBConfig.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{

		db.execSQL(DBConfig.DICTIONARY_TABLE_CREATE);
		db.execSQL(DBConfig.DICTIONARY_TABLE_UPDATE2);
		db.execSQL(DBConfig.REMOVE_WORD_TABLE_CREATE);
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
		if(oldVersion < 2)
		{
			//db.execSQL("drop table " + DBConfig.REMOVE_WORD_TABLE_NAME);
			//db.execSQL("alter table t_WORD drop column ws_id");
			db.execSQL(DBConfig.DICTIONARY_TABLE_UPDATE2);
			db.execSQL(DBConfig.REMOVE_WORD_TABLE_CREATE);
		}
		if(oldVersion < 3)
		{
			//db.execSQL(DBConfig.REMOVE_WORD_TABLE_UPDATE3_1);
			//db.execSQL(DBConfig.REMOVE_WORD_TABLE_UPDATE3_2);
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
			{
				c.close();
				return null;
			}
				
			String word = c.getString(firstWordColumn);
			String word2 = c.getString(secondWordColumn);
			int id = c.getInt(idWordColumn);
			int weight = c.getInt(weight1Column);
			int weight2 = c.getInt(weight2Column);
			pw = new Word(id, word, word2, weight, weight2);
		}
		c.close();
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
	
	public void updateWordWS(final long id, final String ws_id) 
	{
		SQLiteDatabase DB = getWritableDatabase();
		String query = String.format(DBConfig.DICTIONARY_TABLE_UPDATE_WSID,
				ws_id, id);
		execSQL(DB, query);
	}
	
	public void updateWord(final long id, final String learn, final String nativ) 
	{
		SQLiteDatabase DB = getWritableDatabase();
		String query = String.format(DBConfig.DICTIONARY_TABLE_UPDATE_WORD,
				learn, nativ, id);
		execSQL(DB, query);
	}
	
	public void removeWord(Word selectedWord) {
		SQLiteDatabase DB = getWritableDatabase();
		String query = String.format(DBConfig.DICTIONARY_TABLE_DELETE_WORD,
				selectedWord.getId());
		execSQL(DB, query);
		
	}

	public void removeWordWS(Word selectedWord) {
		String wsid =  selectedWord.getWSID();
		ContentValues initialValues = new ContentValues();
		initialValues.put(DBConfig.WORD_1_COLUMN, selectedWord.getLern());
		initialValues.put(DBConfig.WORD_2_COLUMN, selectedWord.getNative());
		initialValues.put(DBConfig.WS_WORD_ID, wsid);
		SQLiteDatabase DB = getWritableDatabase();
		DB.insert(DBConfig.REMOVE_WORD_TABLE_NAME, null, initialValues);
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

	public String createWhereFromIds(long[] ids, boolean not)
	{
		final String and = " AND ";
		String result = "";
		if(ids != null && ids.length > 0)
		{
			result = "WHERE ";
			
			if(not)
				result += " NOT ";
			
			result += "(";
			
			for(long id : ids)
			{
				result += DBConfig.ID_COLUMN + "=" + String.valueOf(id) + OR;
			}
			result = result.substring(0, result.length() - OR.length()) + ")";
			
		}
		//return " Where weight_1 > 20"; 
	
		return result;
	}
	
	
	/**
	 * get array with words sorted by weight1 or weight2 
	 * @param fromWeight2 - order by weight 2
	 * @param ids - ids which will be skiped
	 * @return array wiht words
	 */
	public ArrayList<Word> getPublicWords(long[] ids)
	{
		return getWords(true, Consts.MAX_LAST_LIST, ids);
	}

	
	public long[] getLastListIds(ArrayList<PublicWord> list) 
	{
		if (list == null || list.size() < 1)
			return null;

		long[] result = new long[list.size()];

		for (int i = 0; i != list.size(); i++) 
		{
			result[i] = list.get(i).getId();
		}

		return result;

	}
	
	public ArrayList<Word> getWords(boolean onlyUsed, int limit, long[] ids) 
	{
		mDB = getWritableDatabase();
		String where = createWhereFromIds(ids, true);
		String query; 
		
		query = createQueryForGetWords(onlyUsed, limit, where);
		
		Cursor c = rawQuerySQL(mDB, query);

		return createWordListFromCursor(c, false);
	}
	
	public ArrayList<Word> getUnaddedWords() 
	{
		mDB = getReadableDatabase();
		String where = " WHERE (" + DBConfig.LANG_LESSON + " = 0 AND " + DBConfig.WS_WORD_ID + " is null)";// AND " + DBConfig.LANG_LESSON + " = 0";
		String query; 
		
		query = createQueryForGetWords(false, 20, where);
		
		Cursor c = rawQuerySQL(mDB, query);

		return createWordListFromCursor(c, true);
	}

	public ArrayList<RemovedWord> getRemovedWords()
	{
		ArrayList<RemovedWord> removedWord = null;
		mDB = getReadableDatabase();
		Cursor c = rawQuerySQL(mDB, "SELECT * FROM " + DBConfig.REMOVE_WORD_TABLE_NAME);
		
		final int idC = c.getColumnIndex(DBConfig.ID_COLUMN);
		final int wsidC = c.getColumnIndex(DBConfig.WS_WORD_ID);
		final int word1C = c.getColumnIndex(DBConfig.WORD_1_COLUMN);
		final int word2C = c.getColumnIndex(DBConfig.WORD_2_COLUMN);
		
		if (c != null && c.moveToFirst())
		{
			removedWord = new ArrayList<RemovedWord>();
			
			do
			{
				RemovedWord rw = new RemovedWord();
				rw.id = c.getInt(idC);
				rw.ws_id = c.getString(wsidC);
				rw.learn = c.getString(word1C);
				rw.nativ = c.getString(word2C);
				
				
				removedWord.add(rw);
				
			} while (c.moveToNext());	
			
		}
		c.close();
		
		return removedWord;
	}
	
	private ArrayList<Word> createWordListFromCursor(Cursor c, boolean withWSID) {
		ArrayList<Word> list = null;
		
		final int firstWordColumn = c.getColumnIndex(DBConfig.WORD_1_COLUMN);
		final int secondWordColumn = c.getColumnIndex(DBConfig.WORD_2_COLUMN);
		final int idWordColumn = c.getColumnIndex(DBConfig.ID_COLUMN);
		final int weight1Column = c.getColumnIndex(DBConfig.WEIGHT_1_COLUMN);
		final int weight2Column = c.getColumnIndex(DBConfig.WEIGHT_2_COLUMN);
		final int wsidColumn = withWSID ? c.getColumnIndex(DBConfig.WS_WORD_ID) : 0;
	
		
		if (c != null && c.moveToFirst())
		{
			list = new ArrayList<Word>();
			
			do
			{
				String word = c.getString(firstWordColumn);
				String word2 = c.getString(secondWordColumn);
				int id = c.getInt(idWordColumn);
				int weight = c.getInt(weight1Column);
				int weight2 = c.getInt(weight2Column);
				
				Word w = new Word(id, word, word2, weight, weight2);
				list.add(w);
				if(withWSID)
				{
					w.setWSID(c.getString(wsidColumn));
				}
			} while (c.moveToNext());	
			
		}
		c.close();
		return list;
	}

	private String createQueryForGetWords(boolean onlyUsed, int limit,
			String where) {
		String query;
		if(onlyUsed)
		{
			if(where == null || where.length() < 1)
				where +=  " WHERE ";
			else
				where += " AND ";
			where += DBConfig.WEIGHT_1_COLUMN + " > 0 AND " + DBConfig.WEIGHT_2_COLUMN + " > 0 ";
		}
		
		query = String.format(DBConfig.DICTIONARY_TABLE_SELECT_20_WEIGHT1_WHERE, where);
		if(limit > 0)
			query += " LIMIT " + String.valueOf(limit);
		return query;
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
		return getNumWords(NUM_WORDS_TYPE.ALL);
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

	public long addWordEx(int lesson, String text, String text2,
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
		
		long id = mDB.insert(DBConfig.WORD_TABLE_NAME, null, values);
		//db.delete(DBAdapter.TableName, "Id=? AND QstnrId=? AND QstnId=?",
        //new String[] { Id.toString(), QuestionnaireId, QuestionId });
		
		Log.d(TAG, "add word: " + text + " with lesson = " + String.valueOf(lesson));
		
		return id;
	}
	
	/**
	 * remove lesson by her id
	 * @param lesson if -1 remove all lesson
	 */
	public void unloadLesson(int lesson)
	{
		mDB = getWritableDatabase();
		
		if(lesson == -1)
			mDB.delete(DBConfig.WORD_TABLE_NAME, null, null);
		else
			mDB.delete(DBConfig.WORD_TABLE_NAME, DBConfig.LANG_LESSON + "=?",
				new String[] { String.valueOf(lesson) });
		Log.d(TAG, "remove lesson " + lesson);
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
	    statement.close();
	    return count > 0;
	}
	
	
	/**
	 * test is any word in database have weight 1, 1
	 * this is mean that user not know this word yet
	 * @return true when exist any word with weight 1,1
	 */
	public boolean isAnyUnknownWord()
	{
		return numUnknownWord() == 0;
	}


	
	long numUnknownWord() 
	{
		return getNumWords(NUM_WORDS_TYPE.UNKNOWS);
	}
	
	
	public long getNumWords(NUM_WORDS_TYPE type) 
	{
		String str;
		switch(type)
		{
		case KNOWS:
			str = DBConfig.EXIST_KNOWS_WORDS;
			break;
		case UNKNOWS:
			str = DBConfig.EXIST_UNKNOWS_WORDS;
			break;
		case UNUSED:
			str = DBConfig.EXIST_UNUSED_WORDS;
			break;
		default:
			str = "SELECT COUNT(*) FROM " + DBConfig.WORD_TABLE_NAME;
		}
		
		SQLiteStatement statement = getReadableDatabase().compileStatement(str);
	    long count = statement.simpleQueryForLong();
	    statement.close();
	    Log.i(TAG, "num of unknow word (with weight1,waight2=1,1) : " + String.valueOf(count));
		return count;
	}
	
	
	/**
	 * setup first word with weight 0, 0 to 1, 1
	 */
	public void setupFirstWordWeight(int num)
	{
		ArrayList<Word> list = getWords(false, num, null);
		if (list == null || list.size() < 1)
			return;

		long[] ids = new long[list.size()];

		for (int i = 0; i != list.size(); i++) 
		{
			ids[i] = list.get(i).getId();
		}
		
		String query = String.format(DBConfig.SETUP_WEIGHT,
				 1, 1 );

		query += createWhereFromIds(ids, false);
			execSQL(getWritableDatabase(), query);
			
			Log.i(TAG, String.format("Add %d new word to round!", num));
	
	}

	public ArrayList<Word> getWordsInLesson(int lesson)
	{
		mDB = getReadableDatabase();
		
		final String query; 
		
		query = String.format(DBConfig.DICTIONARY_TABLE_SELECT_BY_LESSON, lesson);
		
		Cursor c = rawQuerySQL(mDB, query);

		return createWordListFromCursor(c, false);
	}

	public void removeFromDeleteList(long id) 
	{
		mDB = getWritableDatabase();
		mDB.execSQL("DELETE FROM " + DBConfig.REMOVE_WORD_TABLE_NAME + " WHERE " + DBConfig.ID_COLUMN + " = " + id);
	}




	
	
	
}