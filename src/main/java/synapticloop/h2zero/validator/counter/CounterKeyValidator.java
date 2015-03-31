package synapticloop.h2zero.validator.counter;

import java.util.ArrayList;

import synapticloop.h2zero.model.Counter;
import synapticloop.h2zero.model.Database;
import synapticloop.h2zero.model.Options;
import synapticloop.h2zero.model.Table;
import synapticloop.h2zero.validator.KeyValidator;

public class CounterKeyValidator extends KeyValidator {

	@Override
	public boolean isValid(Database database, Options options) {
		ArrayList<Table> tables = database.getTables();
		for (Table table : tables) {
			ArrayList<Counter> counters = table.getCounters();
			for (Counter counter : counters) {
				validate(counter);
			}
		}
		return(isValid);
	}
}
