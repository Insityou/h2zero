package synapticloop.h2zero.model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import synapticloop.h2zero.exception.H2ZeroParseException;
import synapticloop.h2zero.model.field.BaseField;
import synapticloop.h2zero.util.JsonHelper;
import synapticloop.h2zero.util.NamingHelper;

public class Counter {
	private String name;
	private String selectClause;
	private String whereClause;
	private String orderBy;

	private ArrayList<BaseField> whereFields = new ArrayList<BaseField>();
	private LinkedHashMap<String, BaseField> uniqueWhereFields = new LinkedHashMap<String, BaseField>();
	private ArrayList<BaseField> selectFields = new ArrayList<BaseField>();

	private boolean unique = false;
	private boolean cache = false;
	private boolean hasInFields = false;
	private ArrayList<BaseField> inWhereFields = new ArrayList<BaseField>();

	public Counter(JSONObject counterObject, Table table) throws H2ZeroParseException {
		this.name = JsonHelper.getStringValue(counterObject, "name", null);
		this.orderBy = JsonHelper.getStringValue(counterObject, "orderBy", null);
		this.selectClause = JsonHelper.getStringValue(counterObject, "selectClause", null);
		// if we have a select clause then we are returning a bean...

		// now for the select fields
		if(null != selectClause) {
			populateSelectFields(counterObject);
		}

		this.unique = JsonHelper.getBooleanValue(counterObject, "unique", unique);
		this.cache = JsonHelper.getBooleanValue(counterObject, "cache", cache);

		// now for the where clauses
		this.whereClause = JsonHelper.getStringValue(counterObject, "whereClause", null);
		// we may not have any whereFields
		try {
			JSONArray whereFieldArray = counterObject.getJSONArray("whereFields");
			for (int i = 0; i < whereFieldArray.length(); i++) {
				String whereFieldName = whereFieldArray.getString(i);

				BaseField baseField = getBaseField(table, whereFieldName);

				if(null == baseField) {
					throw new H2ZeroParseException("Could not look up where field '" + whereFieldName + "', for counter '" + name + "'.");
				}

				whereFields.add(baseField);
				if(baseField.getIsInField()) {
					inWhereFields.add(baseField);
				}

				if(!uniqueWhereFields.containsKey(whereFieldName)) {
					uniqueWhereFields.put(whereFieldName, baseField);
				}
			}
		} catch (JSONException ojjsonex) {
			// do nothing
		}

		if(null == name) {
			throw new H2ZeroParseException("The counter 'name' attribute cannot be null.");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void populateSelectFields(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray fieldJson = new JSONArray();
		try {
			fieldJson = jsonObject.getJSONArray("selectFields");
		} catch (JSONException ojjsonex) {
			throw new H2ZeroParseException("Cannot create the selectClause counter of '" + name + "' counter without 'fields'.");
		}

		for (int i = 0; i < fieldJson.length(); i++) {
			String type = null;
			String name = null;
			JSONObject fieldObject = null;
			try {
				fieldObject = fieldJson.getJSONObject(i);
				type = fieldObject.getString("type");
				name = fieldObject.getString("name");
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse the 'fields' array.");
			}

			if(null != type) {
				String firstUpper = NamingHelper.getFirstUpper(type);
				try {
					Class forName = Class.forName("synapticloop.h2zero.model.field." + firstUpper + "Field");
					Constructor constructor = forName.getConstructor(JSONObject.class);
					BaseField baseField = (BaseField)constructor.newInstance(fieldObject);

					selectFields.add(baseField);

				} catch (Exception ex) {
					throw new H2ZeroParseException(ex.getMessage());
				}
			}
		}
	}

	private BaseField getBaseField(Table table, String fieldName) {
		BaseField baseField = null;
		if(fieldName.startsWith("in:")) {
			fieldName = fieldName.substring(3);

			if(fieldName.contains(".")) {
				// we are doing a table lookup
				String[] splits = fieldName.split("\\.", 2);
				String tableName = splits[0];
				String tableFieldName = splits[1];
				Table tableLookup = Database.getTableLookup(tableName);
				if(null == tableLookup) {
					return(null);
				} else {
					return(tableLookup.getInField(tableFieldName));
				}
			}

			baseField = table.getInField(fieldName);
			this.hasInFields = true;
		} else {
			if(fieldName.contains(".")) {
				// we are doing a table lookup
				String[] splits = fieldName.split("\\.", 2);
				String tableName = splits[0];
				String tableFieldName = splits[1];

				Table tableLookup = Database.getTableLookup(tableName);
				if(null == tableLookup) {
					return(null);
				} else {
					return(tableLookup.getField(tableFieldName));
				}
			}
			baseField = table.getField(fieldName);
		}
		return(baseField);
	}

	public String getName() { return(name); }
	public String getCounterTagName() { return(NamingHelper.getFirstUpper(name)); }
	public String getWhereClause() { return(whereClause); }
	public String getOrderBy() { return(orderBy); }
	public boolean getUnique() { return(unique); }
	public ArrayList<BaseField> getWhereFields() { return(whereFields); }
	public ArrayList<BaseField> getSelectFields() { return(selectFields); }
	public ArrayList<BaseField> getInWhereFields() { return(inWhereFields); }
	public boolean getCache() { return(cache); }
	public boolean getHasInFields() { return(hasInFields); }
	public String getSelectClause() { return selectClause; }
	public void setSelectClause(String selectClause) { this.selectClause = selectClause; }

	public String getStaticName() { return(NamingHelper.getStaticName(name)); }
	public Collection<BaseField> getUniqueWhereFields() { return(this.uniqueWhereFields.values()); }
}
