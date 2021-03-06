package synapticloop.h2zero.validator.updater;

import java.util.ArrayList;

import synapticloop.h2zero.model.Database;
import synapticloop.h2zero.model.Options;
import synapticloop.h2zero.model.Table;
import synapticloop.h2zero.model.Updater;
import synapticloop.h2zero.validator.Validator;

public class UpdaterSetClauseValidator extends Validator {

	@Override
	public void validate(Database database, Options options) {
		ArrayList<Table> tables = database.getTables();
		for (Table table : tables) {
			ArrayList<Updater> updaters = table.getUpdaters();
			for (Updater updater : updaters) {
				String setClause = updater.getSetClause();
				if(null != setClause) {
					if(!setClause.toLowerCase().contains("set ")) {
						addWarnMessage("Updater '" + table.getName() + "." + updater.getName() + "' has a setClause that does not start with 'set', so I am going to add one.");
						updater.setSetClause(" set " + setClause);
					}
				}
			}
		}
	}

}
