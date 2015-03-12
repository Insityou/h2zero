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

public class UserPetCounter {
	// the binder is unused in code, but will generate compile problems if this 
	// class is no longer referenced in the h2zero file. Just a nicety for
	// removing dead code
	@SuppressWarnings("unused")
	private static final String BINDER = Constants.USER_PET_binder;

	private static final Logger LOGGER = Logger.getLogger(UserPetCounter.class);

	private static final String SQL_BUILTIN_COUNT_ALL = "select count(*) from user_pet";


	/**
	 * Find the count of all UserPet objects
	 * 
	 * @return the count of UserPet objects
	 * 
	 * @throws SQLException if there was an error in the SQL statement
	 */
	public static int countAll() throws SQLException {
		Connection connection = null;

		try {
			connection = ConnectionManager.getConnection();
			return(countAll(connection));
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException countAll(): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(connection);
		}
	}

	public static int countAll(Connection connection) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
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
				LOGGER.warn("SQLException countAll(connection): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement);
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

	public static int countAllSilent(Connection connection) {
		try {
			return(countAll(connection));
		} catch(SQLException sqlex){
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException countAllSilent(connection): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(-1);
		}
	}

}