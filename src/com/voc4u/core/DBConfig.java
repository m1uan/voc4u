package com.voc4u.core;

public class DBConfig
{
	public static final String WORD_TABLE_NAME = "t_WORD";
	public static final int DATABASE_VERSION = 1;
	public static final String KEY_WORD = "name";
	public static final String KEY_DEFINITION = "list";
	public static final String ID_COLUMN = "ID";
	public static final String LANG_LESSON = "lang_id";
	public static final String WORD_1_COLUMN = "word_1";
	public static final String WORD_2_COLUMN = "word_2";
	public static final String WEIGHT_1_COLUMN = "weight_1";
	public static final String WEIGHT_2_COLUMN = "weight_2";

	public static final String DICTIONARY_TABLE_CREATE = "CREATE TABLE "
			+ DBConfig.WORD_TABLE_NAME + "( " + ID_COLUMN
			+ " INTEGER PRIMARY KEY " + ", " + DBConfig.LANG_LESSON + " INT"
			+ ", " + DBConfig.WORD_1_COLUMN + " VARCHAR(50)" + ", "
			+ DBConfig.WORD_2_COLUMN + " VARCHAR(50)" + ", "
			+ DBConfig.WEIGHT_1_COLUMN + " INT" + ", "
			+ DBConfig.WEIGHT_2_COLUMN + " INT" + ");";

	public static final String DICTIONARY_TABLE_INSERT = "INSERT INTO "
			+ DBConfig.WORD_TABLE_NAME + " (" + DBConfig.LANG_LESSON + ", "
			+ DBConfig.WORD_1_COLUMN + ", " + DBConfig.WORD_2_COLUMN + ", "
			+ DBConfig.WEIGHT_1_COLUMN + "," + DBConfig.WEIGHT_2_COLUMN
			+ ") VALUES (%d, '%s', '%s', %d, %d)";

	public static final String DICTIONARY_TABLE_SELECT = "SELECT "
			+ DBConfig.ID_COLUMN + "," 
			+ DBConfig.WORD_1_COLUMN + ","
			+ DBConfig.WORD_2_COLUMN + ","
			+ DBConfig.WEIGHT_1_COLUMN + ","
			+ DBConfig.WEIGHT_2_COLUMN 
			+ " FROM " + DBConfig.WORD_TABLE_NAME;
	public static final String DICTIONARY_TABLE_SELECT_WEIGHT1 =
		DICTIONARY_TABLE_SELECT + " WHERE "+ WEIGHT_1_COLUMN +" > 0 ORDER BY "+ WEIGHT_1_COLUMN +" LIMIT 1";
	
	public static final String DICTIONARY_TABLE_SELECT_20_WEIGHT1 =
		DICTIONARY_TABLE_SELECT + " ORDER BY "+ WEIGHT_1_COLUMN +" LIMIT 20";
	public static final String DICTIONARY_TABLE_SELECT_20_WEIGHT1_WHERE =
		DICTIONARY_TABLE_SELECT + " %s ORDER BY "+ WEIGHT_1_COLUMN +" LIMIT 20";
	
	public static final String DICTIONARY_TABLE_SELECT_20_WEIGHT2_WHERE =
		DICTIONARY_TABLE_SELECT + " %s ORDER BY "+ WEIGHT_2_COLUMN +" LIMIT 20";
	
	public static final String DICTIONARY_TABLE_SELECT_20_WEIGHT2 =
		DICTIONARY_TABLE_SELECT + " ORDER BY "+ WEIGHT_2_COLUMN +" LIMIT 20";
	
	public static final String DICTIONARY_TABLE_UPDATE_WEIGHT1 = "UPDATE "
		+ DBConfig.WORD_TABLE_NAME + " SET " + WEIGHT_1_COLUMN + " = %d WHERE " + ID_COLUMN + " = %d";
	
	public static final String DICTIONARY_TABLE_UPDATE_WEIGHT2 = "UPDATE "
		+ DBConfig.WORD_TABLE_NAME + " SET " + WEIGHT_2_COLUMN + " = %d WHERE " + ID_COLUMN + " = %d";
	public static final String DICTIONARY_TABLE_UPDATE_WEIGHTS = "UPDATE "
		+ DBConfig.WORD_TABLE_NAME + " SET " + WEIGHT_1_COLUMN + " = %d ," + WEIGHT_2_COLUMN + " = %d WHERE " + ID_COLUMN + " = %d";;
		
		public static final String GET_ENABLED = "SELECT COUNT(*) FROM " + DBConfig.WORD_TABLE_NAME + " WHERE " + DBConfig.ID_COLUMN + " >= %d AND " + DBConfig.ID_COLUMN + " < %d AND " + DBConfig.WEIGHT_1_COLUMN + " > 0";
		public static final String GET_LESSON_ENABLED = "SELECT COUNT(*) FROM " + DBConfig.WORD_TABLE_NAME + " WHERE " + DBConfig.LANG_LESSON + " = %d";
			
public static final String SET_ENABLED = "UPDATE "
		+ DBConfig.WORD_TABLE_NAME + " SET " + WEIGHT_1_COLUMN + " = %d ," + WEIGHT_2_COLUMN + " = %d WHERE " + DBConfig.ID_COLUMN + " >= %d AND " + DBConfig.ID_COLUMN + " < %d";
	public static final String DICTIONARY_TABLE_SELECT_BY_ID = DICTIONARY_TABLE_SELECT + " WHERE " + ID_COLUMN + " = %d";
}
