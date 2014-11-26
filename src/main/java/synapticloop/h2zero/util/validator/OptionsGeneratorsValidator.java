package synapticloop.h2zero.util.validator;

import synapticloop.h2zero.model.Database;
import synapticloop.h2zero.model.Options;

public class OptionsGeneratorsValidator extends Validator {

	public boolean isValid(Database database, Options options) {
		boolean isValid = options.hasGenerators();

		if(isValid) {
			addInfoMessage("Valid.");
		} else {
			addFatalMessage("You __MUST__ have at least one item in the generators array.");
		}
		return(isValid);
	}
}