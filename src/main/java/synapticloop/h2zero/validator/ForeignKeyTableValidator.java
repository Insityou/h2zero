package synapticloop.h2zero.validator;

import java.util.ArrayList;

import synapticloop.h2zero.model.Database;
import synapticloop.h2zero.model.Options;
import synapticloop.h2zero.model.Table;
import synapticloop.h2zero.model.field.BaseField;

public class ForeignKeyTableValidator extends Validator {

	public void validate(Database database, Options options) {
		ArrayList<Table> tables = database.getTables();

		for (Table table : tables) {
			ArrayList<BaseField> baseFields = table.getFields();
			for (BaseField baseField : baseFields) {
				if(null != baseField.getForeignKeyTable() && null == baseField.getForeignKeyTableLookup()) {
					addFatalMessage("'" + table.getName() + "." + baseField.getName() + "' foreign key references table '" + baseField.getForeignKeyTable() + "', which does not exist.");
				}
			}
		}
	}
}
