package synapticloop.h2zero.validator.question;

import java.util.ArrayList;

import synapticloop.h2zero.model.Database;
import synapticloop.h2zero.model.Options;
import synapticloop.h2zero.model.Question;
import synapticloop.h2zero.model.Table;
import synapticloop.h2zero.validator.KeyValidator;

public class QuestionKeyValidator extends KeyValidator {

	@Override
	public void validate(Database database, Options options) {
		ArrayList<Table> tables = database.getTables();
		for (Table table : tables) {
			ArrayList<Question> questions = table.getQuestions();
			for (Question question : questions) {
				validate(question);
			}
		}
	}
}
