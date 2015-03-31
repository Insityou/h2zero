package synapticloop.h2zero.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import synapticloop.h2zero.exception.H2ZeroParseException;
import synapticloop.h2zero.model.field.BaseField;
import synapticloop.h2zero.model.util.JSONKeyConstants;
import synapticloop.h2zero.util.NamingHelper;

public abstract class BaseSchemaObject {
	protected JSONObject jsonObject;
	protected String name = null;

	private ArrayList<Finder> finders = new ArrayList<Finder>(); // a list of all of the finders

	protected ArrayList<String> autoGeneratedUniqueFinders = new ArrayList<String>();
	protected ArrayList<String> autoGeneratedMultipleFinders = new ArrayList<String>();

	protected HashMap<String, BaseField> fieldLookup = new HashMap<String, BaseField>();
	protected HashMap<String, BaseField> inFieldLookup = new HashMap<String, BaseField>();

	public BaseSchemaObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public abstract boolean getIsTable();
	public abstract boolean getIsView();

	@SuppressWarnings("rawtypes")
	protected void populateFieldFinders(JSONObject jsonObject) throws H2ZeroParseException {
		// now for the autogenerated finders
		JSONArray finderJson = new JSONArray();
		try {
			finderJson = jsonObject.getJSONArray(JSONKeyConstants.FIELD_FINDERS);
		} catch (JSONException ojjsonex) {
			// do nothing - no finders is ok
			return;
		}

		for(int i = 0; i < finderJson.length(); i++) {
			JSONObject fieldFinderObject = finderJson.optJSONObject(i);
			if(null == fieldFinderObject) {
				throw new H2ZeroParseException("Found a '" + JSONKeyConstants.FIELD_FINDERS +  "' json array on table '" + this.name + "', however the value at index '" + i + "' is not a json object.");
			}

			Iterator keys = fieldFinderObject.keys();
			// should only be one key
			String name = (String)keys.next();
			String autoFinder = fieldFinderObject.optString(name);

			if(null != autoFinder) {
				if(autoFinder.compareToIgnoreCase(JSONKeyConstants.UNIQUE) == 0) {
					autoGeneratedUniqueFinders.add(name);
				} else if(autoFinder.compareToIgnoreCase(JSONKeyConstants.MULTIPLE) == 0) {
					autoGeneratedMultipleFinders.add(name);
				} else {
					// TODO - probably want to change this into a switch??
					throw new H2ZeroParseException("Found an auto generate finder on '" + this.name + "." + name + "' with a value of '" + autoFinder + "'.  The allowable values are 'unique' or 'multiple'.");
				}
			}
		}
	}

	protected void populateFinders(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray finderJson = new JSONArray();
		try {
			finderJson = jsonObject.getJSONArray(JSONKeyConstants.FINDERS);
		} catch (JSONException ojjsonex) {
			// do nothing - no finders is ok
		}

		// now go through and addFinders based on the multiple and unique finders
		for (String multipleFinder : autoGeneratedMultipleFinders) {
			JSONObject autoFinder = new JSONObject();
			try {
				autoFinder.put(JSONKeyConstants.NAME, "findBy" + NamingHelper.getFirstUpper(multipleFinder));
				autoFinder.put(JSONKeyConstants.WHERE_CLAUSE, "where " + multipleFinder + " = ?");
				autoFinder.put(JSONKeyConstants.UNIQUE, false);
				autoFinder.put(JSONKeyConstants.WHERE_FIELDS, new JSONArray().put(multipleFinder));

				finders.add(new Finder(autoFinder, this));
			} catch (JSONException jsonEx) {
				throw new H2ZeroParseException("Could not generate the multiple finder for '" + multipleFinder + "'.", jsonEx);
			}
		}

		for (String uniqueFinder : autoGeneratedUniqueFinders) {
			JSONObject autoFinder = new JSONObject();
			try {
				autoFinder.put(JSONKeyConstants.NAME, "findBy" + NamingHelper.getFirstUpper(uniqueFinder));
				autoFinder.put(JSONKeyConstants.WHERE_CLAUSE, "where " + uniqueFinder + " = ?");
				autoFinder.put(JSONKeyConstants.UNIQUE, true);
				autoFinder.put(JSONKeyConstants.WHERE_FIELDS, new JSONArray().put(uniqueFinder));

				finders.add(new Finder(autoFinder, this));
			} catch (JSONException jsonEx) {
				throw new H2ZeroParseException("Could not generate the multiple finder for '" + uniqueFinder + "'.", jsonEx);
			}
		}

		for (int i = 0; i < finderJson.length(); i++) {
			try {
				JSONObject finderObject = finderJson.getJSONObject(i);
				finders.add(new Finder(finderObject, this));
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse finder.");
			}
		}
	}

	public String getName() { return(this.name); }
	public String getUpperName() { return(this.name.toUpperCase()); }
	public String getJavaClassName() { return(NamingHelper.getFirstUpper(name)); }
	public String getJavaFieldName() { return(NamingHelper.getSecondUpper(name)); }

	public BaseField getField(String name) { return(fieldLookup.get(name)); }
	public BaseField getInField(String name) { return(inFieldLookup.get(name)); }

	public ArrayList<Finder> getFinders() { return(finders); }

}
