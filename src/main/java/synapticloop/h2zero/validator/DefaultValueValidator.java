package synapticloop.h2zero.validator;

import java.util.ArrayList;

import synapticloop.h2zero.model.Database;
import synapticloop.h2zero.model.Options;
import synapticloop.h2zero.model.Table;
import synapticloop.h2zero.model.field.BaseField;

public class DefaultValueValidator extends Validator {

	@Override
	public void validate(Database database, Options options) {
		ArrayList<Table> tables = database.getTables();
		for (Table table : tables) {
			ArrayList<BaseField> fields = table.getFields();
			for (BaseField baseField : fields) {
				String defaultValue = baseField.getDefault();
				if(null != defaultValue) {
					if(!defaultValue.startsWith("'") && !defaultValue.endsWith("'")) {
						isValid = false;
						addFatalMessage("Field '" + table.getName() + "." + baseField.getName() + "' has an invalid attribute: \"default\" . It __MUST__ start and end with the single quote (') character.");
					}
				}
			}
		}
	}

}
