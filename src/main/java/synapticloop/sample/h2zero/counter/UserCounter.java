package synapticloop.sample.h2zero.counter;

// - - - - thoughtfully generated by synapticloop h2zero - - - - 
//    with the use of synapticloop templar templating language
//                (java-create-counter.templar)

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import synapticloop.h2zero.base.exception.H2ZeroFinderException;
import synapticloop.h2zero.base.manager.ConnectionManager;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import synapticloop.sample.h2zero.model.util.Constants;

public class UserCounter {
	private static final String BINDER = Constants.USER_binder;

	private static final Logger LOGGER = Logger.getLogger(UserCounter.class);

	private static final String SQL_BUILTIN_COUNT_ALL = "select count(*) from user";

	private static final String SQL_COUNT_NUMBER_OF_USERS = "select count(*) from user";
	private static final String SQL_COUNT_NUMBER_OF_USERS_OVER_AGE = "select count(*) from user" + " where num_age > ?";
	private static final String SQL_COUNT_NUMBER_OF_USERS_BETWEEN_AGE = "select count(*) from user" + " where num_age > ? and num_age < ?";
	private static final String SQL_COUNT_USERS_IN_AGES = "select count(*) from user" + " where num_age in (...)";

	private static HashMap<String, String> countUsersInAges_statement_cache = new HashMap<String, String>();
	/**
	 * Find the count of all User objects
	 * 
	 * @return the count of User objects
	 * 
	 * @throws SQLException if there was an error in the SQL statement
	 */
	public static int countAll() throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		int count = -1;

		try {
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(SQL_BUILTIN_COUNT_ALL);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				count = resultSet.getInt(1);
			}
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException countAll(): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}

		return(count);
	}

	public static int countAllSilent() {
		try {
			return(countAll());
		} catch(SQLException sqlex){
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException countAllSilent(): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(-1);
		}
	}

	public static int countNumberOfUsers() throws H2ZeroFinderException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = -1;
		try {
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(SQL_COUNT_NUMBER_OF_USERS);

			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				count = resultSet.getInt(1);
			}
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		return(count);
	}

	public static int countNumberOfUsersSilent() {
		try {
			return(countNumberOfUsers());
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException countNumberOfUsersSilent(): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(-1);
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException countNumberOfUsersSilent(): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(-1);
		}
	}

	public static int countNumberOfUsersOverAge(Integer numAge) throws H2ZeroFinderException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = -1;
		try {
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(SQL_COUNT_NUMBER_OF_USERS_OVER_AGE);
			ConnectionManager.setInt(preparedStatement, 1, numAge);

			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				count = resultSet.getInt(1);
			}
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		return(count);
	}

	public static int countNumberOfUsersOverAgeSilent(Integer numAge) {
		try {
			return(countNumberOfUsersOverAge(numAge));
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException countNumberOfUsersOverAgeSilent(" + numAge + "): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(-1);
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException countNumberOfUsersOverAgeSilent(" + numAge + "): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(-1);
		}
	}

	public static int countNumberOfUsersBetweenAge(Integer numAgeFrom, Integer numAgeTo) throws H2ZeroFinderException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = -1;
		try {
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(SQL_COUNT_NUMBER_OF_USERS_BETWEEN_AGE);
			ConnectionManager.setInt(preparedStatement, 1, numAgeFrom);
			ConnectionManager.setInt(preparedStatement, 2, numAgeTo);

			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				count = resultSet.getInt(1);
			}
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		return(count);
	}

	public static int countNumberOfUsersBetweenAgeSilent(Integer numAgeFrom, Integer numAgeTo) {
		try {
			return(countNumberOfUsersBetweenAge(numAgeFrom, numAgeTo));
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException countNumberOfUsersBetweenAgeSilent(" + numAgeFrom + ", " + numAgeTo + "): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(-1);
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException countNumberOfUsersBetweenAgeSilent(" + numAgeFrom + ", " + numAgeTo + "): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(-1);
		}
	}

	public static int countUsersInAges(List<Integer> numAgeList) throws H2ZeroFinderException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = -1;
		try {
			connection = ConnectionManager.getConnection();
			if(countUsersInAges_statement_cache.containsKey(numAgeList.size() + ":" )) {
				preparedStatement = connection.prepareStatement(countUsersInAges_statement_cache.get(numAgeList.size() + ":" ));
			} else {
				String preparedStatementTemp = SQL_COUNT_USERS_IN_AGES;
				StringBuilder stringBuilder = null;
				stringBuilder = new StringBuilder();
				for(int i = 0; i < numAgeList.size(); i++) {
					if(i > 0) {
						stringBuilder.append(", ");
					}
					stringBuilder.append("?");
				}
				preparedStatementTemp = SQL_COUNT_USERS_IN_AGES.replaceFirst("\\.\\.\\.", stringBuilder.toString());
				countUsersInAges_statement_cache.put(numAgeList.size() + ":" , preparedStatementTemp);
				preparedStatement = connection.prepareStatement(preparedStatementTemp);
			}
			int i = 1;
			for (Integer numAgeIn : numAgeList) {
				ConnectionManager.setInt(preparedStatement, i, numAgeIn);
				i++;
			}

			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				count = resultSet.getInt(1);
			}
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		return(count);
	}

	public static int countUsersInAgesSilent(List<Integer> numAgeList) {
		try {
			return(countUsersInAges(numAgeList));
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException countUsersInAgesSilent(" + numAgeList + "): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(-1);
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException countUsersInAgesSilent(" + numAgeList + "): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(-1);
		}
	}

}