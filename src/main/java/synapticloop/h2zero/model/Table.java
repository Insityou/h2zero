package synapticloop.h2zero.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import synapticloop.h2zero.exception.H2ZeroParseException;
import synapticloop.h2zero.model.field.BaseField;
import synapticloop.h2zero.model.util.JSONKeyConstants;
import synapticloop.h2zero.util.JsonHelper;
import synapticloop.h2zero.util.NamingHelper;
import synapticloop.h2zero.util.SimpleLogger;


public class Table extends BaseSchemaObject {
	private String engine = "innodb";
	private String charset = "UTF8";
	private ArrayList<String> comments = new ArrayList<String>();

	private boolean cacheable = false;
	private boolean cacheFindAll = false;
	private boolean hasLargeObject = false;

	// a list of all of the fields that this table has
	private ArrayList<BaseField> fields = new ArrayList<BaseField>();
	// all fields that are not marked as secure
	private ArrayList<BaseField> nonSecureFields = new ArrayList<BaseField>();
	// all fields that are marked as secure
	private ArrayList<BaseField> secureFields = new ArrayList<BaseField>();

	private HashMap<String, BaseField> setFieldLookup = new HashMap<String, BaseField>();
	private HashMap<String, BaseField> whereFieldLookup = new HashMap<String, BaseField>();

	private ArrayList<BaseField> nonNullFields = new ArrayList<BaseField>();
	private ArrayList<BaseField> nonPrimaryFields = new ArrayList<BaseField>();

	private ArrayList<Updater> updaters = new ArrayList<Updater>(); // a list of all of the updaters
	private ArrayList<Inserter> inserters = new ArrayList<Inserter>(); // a list of all of the inserters
	private ArrayList<Deleter> deleters = new ArrayList<Deleter>(); // a list of all of the deleters
	private ArrayList<Constant> constants = new ArrayList<Constant>(); // a list of all of the constants
	private ArrayList<Counter> counters = new ArrayList<Counter>(); // a list of all of the counters
	private ArrayList<Question> questions = new ArrayList<Question>(); // a list of all of the questions

	// TODO - remove this please
	private boolean hasDeprecatedFinder = false;
	private LinkedHashMap<String, String> deprecatedFinders = new LinkedHashMap<String, String>();

	public Table(JSONObject jsonObject) throws H2ZeroParseException {
		super(jsonObject);

		this.name = JsonHelper.getStringValue(jsonObject, JSONKeyConstants.NAME, null);
		this.engine = JsonHelper.getStringValue(jsonObject, "engine", engine);
		this.charset = JsonHelper.getStringValue(jsonObject, "charset", charset);
		this.cacheable = JsonHelper.getBooleanValue(jsonObject, "cacheable", cacheable);
		this.cacheFindAll = JsonHelper.getBooleanValue(jsonObject, "cacheFindAll", cacheFindAll);
		String tempComments = JsonHelper.getStringValue(jsonObject, "comment", null);

		if(null != tempComments) {
			String[] split = tempComments.split("\\n");
			for (int i = 0; i < split.length; i++) {
				comments.add(split[i]);
			}
		}

		if(null == name) {
			throw new H2ZeroParseException("The table 'name' attribute cannot be null.");
		}

		// now for the fields
		populateFields(jsonObject);
	}

	public void populateActions() throws H2ZeroParseException {
		populateFieldFinders(jsonObject);
		populateFinders(jsonObject);
		populateUpdaters(jsonObject);
		populateDeleters(jsonObject);
		populateInserters(jsonObject);
		populateConstants(jsonObject);
		populateCounters(jsonObject);
		populateQuestions(jsonObject);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void populateFields(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray fieldJson = new JSONArray();
		try {
			fieldJson = jsonObject.getJSONArray(JSONKeyConstants.FIELDS);
		} catch (JSONException ojjsonex) {
			throw new H2ZeroParseException("Cannot create a table without '" + JSONKeyConstants.FIELDS + "'.");
		}

		for (int i = 0; i < fieldJson.length(); i++) {
			String type = null;
			String name = null;
//			String foreignKey = null;

			JSONObject fieldObject = null;
			try {
				fieldObject = fieldJson.getJSONObject(i);
				name = fieldObject.getString(JSONKeyConstants.NAME);
				if(null == name) {
					throw new H2ZeroParseException("Cannot have a field with a null name.");
				}

				// at this point we want to see if it is a foreign key...
				// TODO - put this back
//				foreignKey = fieldObject.optString(JSONKeyConstants.FOREIGN_KEY, null);
//
				type = fieldObject.optString("type", null);
//
//				if(null != foreignKey && type != null) {
//					throw new H2ZeroParseException("Foreign key reference '" + this.name + "." + name + "' has a 'foreignKey' and 'type' keys.  The 'type' key cannot over-ride the reference.");
//				}
//
//				if(null != foreignKey) {
//					// get the foreignkey
//					BaseField foreignBaseField = FieldLookupHelper.getBaseField(this, foreignKey);
//					type = foreignBaseField.getType();
//					// now override the current field with the values of the foreign key
//					fieldObject.put(JSONKeyConstants.TYPE, foreignBaseField.getType());
//				}

				// now for the autogenerated finders
				String autoFinder = fieldObject.optString("finder", null);
				if(null != autoFinder) {
					this.hasDeprecatedFinder  = true;
					if(autoFinder.compareToIgnoreCase("unique") == 0) {
						deprecatedFinders.put(name, "unique");
						autoGeneratedUniqueFinders.add(name);
					} else if(autoFinder.compareToIgnoreCase("multiple") == 0) {
						deprecatedFinders.put(name, "multiple");
						autoGeneratedMultipleFinders.add(name);
					} else {
						// TODO - probably want to change this into a switch??
						throw new H2ZeroParseException("Found an auto generate finder on '" + this.name + "." + name + "' with a value of '" + autoFinder + "'.  The allowable values are 'unique' or 'multiple'.");
					}
				}
			} catch (JSONException jsonex) {
				throw new H2ZeroParseException("Could not parse the '" + JSONKeyConstants.FIELDS + "' array.", jsonex);
			}

			if(null != type) {
				String firstUpper = NamingHelper.getFirstUpper(type);
				try {
					Class forName = Class.forName("synapticloop.h2zero.model.field." + firstUpper + "Field");
					Constructor constructor = forName.getConstructor(JSONObject.class, boolean.class);

					BaseField inBaseField = (BaseField)constructor.newInstance(fieldObject, true);

					constructor = forName.getConstructor(JSONObject.class);
					BaseField baseField = (BaseField)constructor.newInstance(fieldObject);

					if(!baseField.getNullable()) {
						nonNullFields.add(baseField);
					}

					if(!baseField.getPrimary()) {
						nonPrimaryFields.add(baseField);
					}

					if(baseField.getIsLargeObject()) {
						hasLargeObject = true;
					}

					fields.add(baseField);
					fieldLookup.put(name, baseField);
					inFieldLookup.put(name, inBaseField);

					BaseField setBaseField = (BaseField)constructor.newInstance(fieldObject);
					setBaseField.suffixJavaName("Set");
					BaseField whereBaseField = (BaseField)constructor.newInstance(fieldObject);
					whereBaseField.suffixJavaName("Where");

					setFieldLookup.put(name, setBaseField);
					whereFieldLookup.put(name, whereBaseField);
				} catch (ClassNotFoundException cnfex) {
					SimpleLogger.logFatal(SimpleLogger.LoggerType.PARSE, "ClassNotFoundException: on table '" + this.name + "', throwing upwards..., for field synapticloop.h2zero.model.field." + firstUpper + "Field");
					throw new H2ZeroParseException(cnfex.getMessage());
				} catch (SecurityException sex) {
					SimpleLogger.logFatal(SimpleLogger.LoggerType.PARSE, "SecurityException: on table '" + this.name + "', throwing upwards..., for field synapticloop.h2zero.model.field." + firstUpper + "Field");
					throw new H2ZeroParseException(sex.getMessage());
				} catch (NoSuchMethodException nsmex) {
					SimpleLogger.logFatal(SimpleLogger.LoggerType.PARSE, "NoSuchMethodException: on table '" + this.name + "', throwing upwards..., for field synapticloop.h2zero.model.field." + firstUpper + "Field");
					throw new H2ZeroParseException(nsmex.getMessage());
				} catch (IllegalArgumentException iaex) {
					SimpleLogger.logFatal(SimpleLogger.LoggerType.PARSE, "IllegalArgumentException: on table '" + this.name + "', throwing upwards..., for field synapticloop.h2zero.model.field." + firstUpper + "Field");
					throw new H2ZeroParseException(iaex.getMessage());
				} catch (InstantiationException iex) {
					SimpleLogger.logFatal(SimpleLogger.LoggerType.PARSE, "InstantiationException: on table '" + this.name + "', throwing upwards..., for field synapticloop.h2zero.model.field." + firstUpper + "Field");
					throw new H2ZeroParseException(iex.getMessage());
				} catch (IllegalAccessException iaex) {
					SimpleLogger.logFatal(SimpleLogger.LoggerType.PARSE, "IllegalAccessException: on table '" + this.name + "', throwing upwards..., for field synapticloop.h2zero.model.field." + firstUpper + "Field");
					throw new H2ZeroParseException(iaex.getMessage());
				} catch (InvocationTargetException itex) {
					SimpleLogger.logFatal(SimpleLogger.LoggerType.PARSE, "InvocationTargetException: on table '" + this.name + "', throwing upwards..., for field synapticloop.h2zero.model.field." + firstUpper + "Field");
					throw new H2ZeroParseException(itex.getCause().getMessage());
				}
			}
		}
	}

	private void populateUpdaters(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray updaterJson = new JSONArray();
		try {
			updaterJson = jsonObject.getJSONArray(JSONKeyConstants.UPDATERS);
		} catch (JSONException ojjsonex) {
			// do nothing - no finders is ok
		}

		for (int i = 0; i < updaterJson.length(); i++) {
			try {
				JSONObject updaterObject = updaterJson.getJSONObject(i);
				updaters.add(new Updater(this, updaterObject));
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse updaters.");
			}
		}
	}

	private void populateDeleters(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray deleterJson = new JSONArray();
		try {
			deleterJson = jsonObject.getJSONArray(JSONKeyConstants.DELETERS);
		} catch (JSONException ojjsonex) {
			// do nothing - no finders is ok
		}

		for (int i = 0; i < deleterJson.length(); i++) {
			try {
				JSONObject deleterObject = deleterJson.getJSONObject(i);
				deleters.add(new Deleter(this, deleterObject));
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse deleters.");
			}
		}
	}

	private void populateInserters(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray inserterJson = new JSONArray();
		try {
			inserterJson = jsonObject.getJSONArray(JSONKeyConstants.INSERTERS);
		} catch (JSONException ojjsonex) {
			// do nothing - no finders is ok
		}

		for (int i = 0; i < inserterJson.length(); i++) {
			try {
				JSONObject inserterObject = inserterJson.getJSONObject(i);
				inserters.add(new Inserter(this, inserterObject));
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse inserters.");
			}
		}
	}

	private void populateConstants(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray constantJson = new JSONArray();
		try {
			constantJson = jsonObject.getJSONArray(JSONKeyConstants.CONSTANTS);
		} catch (JSONException ojjsonex) {
			// do nothing - no constants is ok
		}

		for (int i = 0; i < constantJson.length(); i++) {
			try {
				JSONObject constantsObject = constantJson.getJSONObject(i);
				constants.add(new Constant(constantsObject, this));
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse constants.");
			}
		}
	}

	/**
	 * Populate all of the counters that are being generated for a table.  A counter is a simple query that returns one
	 * and only one integer value for the query which is assumed to be a count
	 * 
	 * @param jsonObject The jsonObject to parse for questions
	 * @throws H2ZeroParseException if something went wrong with the parsing
	 */
	private void populateCounters(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray counterJson = new JSONArray();
		try {
			counterJson = jsonObject.getJSONArray(JSONKeyConstants.COUNTERS);
		} catch (JSONException ojjsonex) {
			// do nothing - no finders is ok
		}

		for (int i = 0; i < counterJson.length(); i++) {
			try {
				JSONObject counterObject = counterJson.getJSONObject(i);
				counters.add(new Counter(this, counterObject));
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse counter JSON Array.");
			}
		}
	}

	/**
	 * Populate all of the questions that are being generated for a table.  A question is a simple query that returns one
	 * and only one boolean true/false value
	 * 
	 * @param jsonObject The jsonObject to parse for questions
	 * @throws H2ZeroParseException if something went wrong with the parsing
	 */
	private void populateQuestions(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray questionJson = new JSONArray();
		try {
			questionJson = jsonObject.getJSONArray(JSONKeyConstants.QUESTIONS);
		} catch (JSONException ojjsonex) {
			// do nothing - no questions is ok
		}

		for (int i = 0; i < questionJson.length(); i++) {
			try {
				JSONObject questionObject = questionJson.getJSONObject(i);
				questions.add(new Question(this, questionObject));
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse questions JSON Array.");
			}
		}
	}

	public String getEngine() { return(this.engine); }
	public String getCharset() { return(this.charset); }
	public ArrayList<String> getComments() { return comments; }

	public ArrayList<BaseField> getFields() { return(fields); }

	public ArrayList<Updater> getUpdaters() { return(updaters); }
	public ArrayList<Inserter> getInserters() { return(inserters); }
	public ArrayList<Deleter> getDeleters() { return(deleters); }
	public ArrayList<Constant> getConstants() { return(constants); }
	public ArrayList<Counter> getCounters() { return(counters); }
	public ArrayList<Question> getQuestions() { return(questions); }

	public ArrayList<BaseField> getNonNullFields() { return(nonNullFields); }
	public ArrayList<BaseField> getNonPrimaryFields() { return(nonPrimaryFields); }
	public boolean getCacheable() { return(cacheable); }
	public boolean getCacheFindAll() { return(cacheFindAll); }
	public BaseField getSetField(String name) { return(setFieldLookup.get(name)); }
	public BaseField getWhereField(String name) { return(whereFieldLookup.get(name)); }
	public String getJavaClassName() { return(NamingHelper.getFirstUpper(name)); }
	public String getJavaFieldName() { return(NamingHelper.getSecondUpper(name)); }
	public boolean getHasNonNullConstructor() { return(nonNullFields.size() != fields.size()); }
	public boolean getHasLargeObject() { return hasLargeObject; }
	public boolean getHasQuestions() { return(questions.size() > 0); }

	public boolean getIsConstant() { return(constants.size() > 0); }

	// TODO - this needs to be removed - when deprecation is complete
	public boolean getHasDeprecatedFinder() { return hasDeprecatedFinder; }
	public LinkedHashMap<String, String> getDeprecatedFinders() { return(deprecatedFinders); }

	public boolean getIsTable() { return(true); }
	public boolean getIsView() { return(false); }
}
