package synapticloop.sample.h2zero.finder;

// - - - - thoughtfully generated by synapticloop h2zero - - - - 
//    with the use of synapticloop templar templating language
//                (java-create-finder.templar)

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Clob;
import java.sql.Blob;
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
import synapticloop.sample.h2zero.bean.FindNmUserDtmSignupBean;
import synapticloop.sample.h2zero.bean.FindGroupNumAgeBean;

import synapticloop.sample.h2zero.model.User;
public class UserFinder {
	// the binder is unused in code, but will generate compile problems if this 
	// class is no longer referenced in the h2zero file. Just a nicety for
	// removing dead code
	@SuppressWarnings("unused")
	private static final String BINDER = Constants.USER_binder;

	private static final Logger LOGGER = Logger.getLogger(UserFinder.class);
	private static final String SQL_SELECT_START = "select id_user, id_user_type, fl_is_alive, num_age, nm_username, txt_address_email, txt_password, dtm_signup from user";
	private static final String SQL_BUILTIN_FIND_BY_PRIMARY_KEY = SQL_SELECT_START + " where id_user = ?";
	private static final String SQL_BUILTIN_PRIMARY_KEY_EXISTS = "select count(*) from user where id_user = ?";

	private static final String SQL_FIND_BY_NUM_AGE = SQL_SELECT_START + " where num_age = ?";
	private static final String SQL_FIND_BY_FL_IS_ALIVE_NUM_AGE = SQL_SELECT_START + " where fl_is_alive = ?num_age = ?, ";
	private static final String SQL_FIND_BY_NM_USERNAME = SQL_SELECT_START + " where nm_username = ?";
	private static final String SQL_FIND_BY_TXT_ADDRESS_EMAIL = SQL_SELECT_START + " where txt_address_email = ?";
	private static final String SQL_FIND_BY_TXT_ADDRESS_EMAIL_TXT_PASSWORD = SQL_SELECT_START + " where txt_address_email = ? and txt_password = ?";
	private static final String SQL_FIND_NM_USER_DTM_SIGNUP = "select nm_user, dtm_signup from user";
	private static final String SQL_FIND_GROUP_NUM_AGE = "select count(*) as num_count, num_age from user group by num_count";

	private static HashMap<String, String> findAll_limit_statement_cache = new HashMap<String, String>();
	private static HashMap<String, String> findByNumAge_limit_statement_cache = new HashMap<String, String>();
	private static HashMap<String, String> findByFlIsAliveNumAge_limit_statement_cache = new HashMap<String, String>();
	private static HashMap<String, String> findByNmUsername_limit_statement_cache = new HashMap<String, String>();
	private static HashMap<String, String> findByTxtAddressEmail_limit_statement_cache = new HashMap<String, String>();
	private static HashMap<String, String> findByTxtAddressEmailTxtPassword_limit_statement_cache = new HashMap<String, String>();
	/**
	 * Find a User by its primary key
	 * 
	 * @param connection the connection item
	 * @param idUser the primary key
	 * 
	 * @return the unique result or throw an exception if one couldn't be found
	 * 
	 * @throws H2ZeroFinderException if one couldn't be found
	 * @throws SQLException if there was an error in the SQL statement
	 */
	public static User findByPrimaryKey(Connection connection, Long idUser) throws H2ZeroFinderException, SQLException {
		User user = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		if(null == idUser) {
			throw new H2ZeroFinderException("Could not find result as the primary key field [idUser] was null.");
		}

		try {
			preparedStatement = connection.prepareStatement(SQL_BUILTIN_FIND_BY_PRIMARY_KEY);
			preparedStatement.setLong(1, idUser);
			resultSet = preparedStatement.executeQuery();
			user = uniqueResult(resultSet);
		} catch (SQLException sqlex) {
			throw sqlex;
		} catch (H2ZeroFinderException h2zfex) {
			throw new H2ZeroFinderException(h2zfex.getMessage() + "  Additionally, the parameters were [idUser:" + idUser + "].");
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement);
		}

		if(null == user) {
			throw new H2ZeroFinderException("Could not find result the parameters were [idUser:" + idUser + "].");
		}
		return(user);
	}

	/**
	 * Find a User by its primary key
	 * 
	 * @param idUser the primary key
	 * 
	 * @return the unique result or throw an exception if one coudn't be found.
	 * 
	 * @throws H2ZeroFinderException if one couldn't be found
	 * @throws SQLException if there was an error in the SQL statement
	 */
	public static User findByPrimaryKey(Long idUser) throws H2ZeroFinderException, SQLException {
		User user = null;
		Connection connection = null;

		if(null == idUser) {
			throw new H2ZeroFinderException("Could not find result as the primary key field [idUser] was null.");
		}

		try {
			connection = ConnectionManager.getConnection();
			user = findByPrimaryKey(connection, idUser);
		} catch (SQLException sqlex) {
			throw sqlex;
		} catch (H2ZeroFinderException h2zfex) {
			throw new H2ZeroFinderException(h2zfex.getMessage() + "  Additionally, the parameters were [idUser:" + idUser + "].");
		} finally {
			ConnectionManager.closeAll(connection);
		}

		if(null == user) {
			throw new H2ZeroFinderException("Could not find result the parameters were [idUser:" + idUser + "].");
		}
		return(user);
	}

	/**
	 * Find a User by its primary key and silently fail.
	 * I.e. Do not throw an exception on error.
	 * 
	 * @param connection the connection item
	 * @param idUser the primary key
	 * 
	 * @return the unique result or null if it couldn't be found
	 * 
	 */
	public static User findByPrimaryKeySilent(Connection connection, Long idUser) {
		try {
			return(findByPrimaryKey(connection, idUser));
		} catch(H2ZeroFinderException h2zfex){
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findByPrimaryKeySilent(" + idUser + "): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(null);
		} catch(SQLException sqlex){
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findByPrimaryKeySilent(" + idUser + "): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(null);
		}
	}

	/**
	 * Find a User by its primary key and silently fail.
	 * I.e. Do not throw an exception on error.
	 * 
	 * @param idUser the primary key
	 * 
	 * @return the unique result or null if it couldn't be found
	 * 
	 */
	public static User findByPrimaryKeySilent(Long idUser) {
		try {
			return(findByPrimaryKey(idUser));
		} catch(H2ZeroFinderException h2zfex){
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findByPrimaryKeySilent(" + idUser + "): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(null);
		} catch(SQLException sqlex){
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findByPrimaryKeySilent(" + idUser + "): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(null);
		}
	}

	/**
	 * Find all User objects
	 * 
	 * @return a list of all of the User objects
	 * 
	 * @throws SQLException if there was an error in the SQL statement
	 */
	public static List<User> findAll(Integer limit, Integer offset) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		List<User> results = new ArrayList<User>();

		try {
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(SQL_SELECT_START);
			resultSet = preparedStatement.executeQuery();
			results = list(resultSet);
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findAll(): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}

		return(results);
	}

	public static List<User> findAll() throws SQLException {
		return(findAll(null, null));
	}
	public static List<User> findAllSilent() {
		try {
			return(findAll());
		} catch(SQLException sqlex){
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findAllSilent(): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(new ArrayList<User>());
		}
	}

	public static List<User> findByNumAge(Integer numAge) throws H2ZeroFinderException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<User> results = null;
		try {
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(SQL_FIND_BY_NUM_AGE);
			ConnectionManager.setInt(preparedStatement, 1, numAge);

			resultSet = preparedStatement.executeQuery();
			results = list(resultSet);
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}


		if(null == results) {
			throw new H2ZeroFinderException("Could not find result.");
		}
		return(results);
	}

	public static List<User> findByNumAge(Connection connection, Integer numAge) throws H2ZeroFinderException, SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<User> results = null;
		try {
			preparedStatement = connection.prepareStatement(SQL_FIND_BY_NUM_AGE);
			ConnectionManager.setInt(preparedStatement, 1, numAge);

			resultSet = preparedStatement.executeQuery();
			results = list(resultSet);
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement);
		}


		if(null == results) {
			throw new H2ZeroFinderException("Could not find result.");
		}
		return(results);
	}

	public static List<User> findByNumAgeSilent(Integer numAge) {
		try {
			return(findByNumAge(numAge));
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findByNumAgeSilent(" + numAge + "): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(new ArrayList<User>());
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findByNumAgeSilent(" + numAge + "): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(new ArrayList<User>());
		}
	}

	public static List<User> findByNumAgeSilent(Connection connection, Integer numAge) {
		try {
			return(findByNumAge(numAge));
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findByNumAgeSilent(" + numAge + "): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(new ArrayList<User>());
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findByNumAgeSilent(" + numAge + "): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(new ArrayList<User>());
		}
	}

	public static List<User> findByFlIsAliveNumAge(Boolean flIsAlive, Integer numAge) throws H2ZeroFinderException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<User> results = null;
		try {
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(SQL_FIND_BY_FL_IS_ALIVE_NUM_AGE);
			ConnectionManager.setBoolean(preparedStatement, 1, flIsAlive);
			ConnectionManager.setInt(preparedStatement, 2, numAge);

			resultSet = preparedStatement.executeQuery();
			results = list(resultSet);
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}


		if(null == results) {
			throw new H2ZeroFinderException("Could not find result.");
		}
		return(results);
	}

	public static List<User> findByFlIsAliveNumAge(Connection connection, Boolean flIsAlive, Integer numAge) throws H2ZeroFinderException, SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<User> results = null;
		try {
			preparedStatement = connection.prepareStatement(SQL_FIND_BY_FL_IS_ALIVE_NUM_AGE);
			ConnectionManager.setBoolean(preparedStatement, 1, flIsAlive);
			ConnectionManager.setInt(preparedStatement, 2, numAge);

			resultSet = preparedStatement.executeQuery();
			results = list(resultSet);
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement);
		}


		if(null == results) {
			throw new H2ZeroFinderException("Could not find result.");
		}
		return(results);
	}

	public static List<User> findByFlIsAliveNumAgeSilent(Boolean flIsAlive, Integer numAge) {
		try {
			return(findByFlIsAliveNumAge(flIsAlive, numAge));
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findByFlIsAliveNumAgeSilent(" + flIsAlive + ", " + numAge + "): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(new ArrayList<User>());
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findByFlIsAliveNumAgeSilent(" + flIsAlive + ", " + numAge + "): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(new ArrayList<User>());
		}
	}

	public static List<User> findByFlIsAliveNumAgeSilent(Connection connection, Boolean flIsAlive, Integer numAge) {
		try {
			return(findByFlIsAliveNumAge(flIsAlive, numAge));
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findByFlIsAliveNumAgeSilent(" + flIsAlive + ", " + numAge + "): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(new ArrayList<User>());
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findByFlIsAliveNumAgeSilent(" + flIsAlive + ", " + numAge + "): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(new ArrayList<User>());
		}
	}

	public static User findByNmUsername(String nmUsername) throws H2ZeroFinderException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User result = null;
		try {
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(SQL_FIND_BY_NM_USERNAME);
			ConnectionManager.setVarchar(preparedStatement, 1, nmUsername);

			resultSet = preparedStatement.executeQuery();
			result = uniqueResult(resultSet);
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		} catch (SQLException sqlex) {
			throw sqlex;
		} catch (H2ZeroFinderException h2zfex) {
			throw new H2ZeroFinderException(h2zfex.getMessage() + "  Additionally, the parameters were "  + "[nmUsername:" + nmUsername + "].");
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}


		if(null == result) {
			throw new H2ZeroFinderException("Could not find result.");
		}
		return(result);
	}

	public static User findByNmUsername(Connection connection, String nmUsername) throws H2ZeroFinderException, SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User result = null;
		try {
			preparedStatement = connection.prepareStatement(SQL_FIND_BY_NM_USERNAME);
			ConnectionManager.setVarchar(preparedStatement, 1, nmUsername);

			resultSet = preparedStatement.executeQuery();
			result = uniqueResult(resultSet);
			ConnectionManager.closeAll(resultSet, preparedStatement);
		} catch (SQLException sqlex) {
			throw sqlex;
		} catch (H2ZeroFinderException h2zfex) {
			throw new H2ZeroFinderException(h2zfex.getMessage() + "  Additionally, the parameters were "  + "[nmUsername:" + nmUsername + "].");
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement);
		}


		if(null == result) {
			throw new H2ZeroFinderException("Could not find result.");
		}
		return(result);
	}

	public static User findByNmUsernameSilent(String nmUsername) {
		try {
			return(findByNmUsername(nmUsername));
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findByNmUsernameSilent(" + nmUsername + "): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(null);
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findByNmUsernameSilent(" + nmUsername + "): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(null);
		}
	}

	public static User findByNmUsernameSilent(Connection connection, String nmUsername) {
		try {
			return(findByNmUsername(connection, nmUsername));
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findByNmUsernameSilent(" + nmUsername + "): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(null);
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findByNmUsernameSilent(" + nmUsername + "): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(null);
	}
	}

	public static User findByTxtAddressEmail(String txtAddressEmail) throws H2ZeroFinderException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User result = null;
		try {
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(SQL_FIND_BY_TXT_ADDRESS_EMAIL);
			ConnectionManager.setVarchar(preparedStatement, 1, txtAddressEmail);

			resultSet = preparedStatement.executeQuery();
			result = uniqueResult(resultSet);
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		} catch (SQLException sqlex) {
			throw sqlex;
		} catch (H2ZeroFinderException h2zfex) {
			throw new H2ZeroFinderException(h2zfex.getMessage() + "  Additionally, the parameters were "  + "[txtAddressEmail:" + txtAddressEmail + "].");
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}


		if(null == result) {
			throw new H2ZeroFinderException("Could not find result.");
		}
		return(result);
	}

	public static User findByTxtAddressEmail(Connection connection, String txtAddressEmail) throws H2ZeroFinderException, SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User result = null;
		try {
			preparedStatement = connection.prepareStatement(SQL_FIND_BY_TXT_ADDRESS_EMAIL);
			ConnectionManager.setVarchar(preparedStatement, 1, txtAddressEmail);

			resultSet = preparedStatement.executeQuery();
			result = uniqueResult(resultSet);
			ConnectionManager.closeAll(resultSet, preparedStatement);
		} catch (SQLException sqlex) {
			throw sqlex;
		} catch (H2ZeroFinderException h2zfex) {
			throw new H2ZeroFinderException(h2zfex.getMessage() + "  Additionally, the parameters were "  + "[txtAddressEmail:" + txtAddressEmail + "].");
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement);
		}


		if(null == result) {
			throw new H2ZeroFinderException("Could not find result.");
		}
		return(result);
	}

	public static User findByTxtAddressEmailSilent(String txtAddressEmail) {
		try {
			return(findByTxtAddressEmail(txtAddressEmail));
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findByTxtAddressEmailSilent(" + txtAddressEmail + "): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(null);
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findByTxtAddressEmailSilent(" + txtAddressEmail + "): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(null);
		}
	}

	public static User findByTxtAddressEmailSilent(Connection connection, String txtAddressEmail) {
		try {
			return(findByTxtAddressEmail(connection, txtAddressEmail));
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findByTxtAddressEmailSilent(" + txtAddressEmail + "): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(null);
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findByTxtAddressEmailSilent(" + txtAddressEmail + "): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(null);
	}
	}

	public static User findByTxtAddressEmailTxtPassword(String txtAddressEmail, String txtPassword) throws H2ZeroFinderException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User result = null;
		try {
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(SQL_FIND_BY_TXT_ADDRESS_EMAIL_TXT_PASSWORD);
			ConnectionManager.setVarchar(preparedStatement, 1, txtAddressEmail);
			ConnectionManager.setVarchar(preparedStatement, 2, txtPassword);

			resultSet = preparedStatement.executeQuery();
			result = uniqueResult(resultSet);
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		} catch (SQLException sqlex) {
			throw sqlex;
		} catch (H2ZeroFinderException h2zfex) {
			throw new H2ZeroFinderException(h2zfex.getMessage() + "  Additionally, the parameters were "  + "[txtAddressEmail:" + txtAddressEmail + "], " + "[txtPassword:" + txtPassword + "].");
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}


		if(null == result) {
			throw new H2ZeroFinderException("Could not find result.");
		}
		return(result);
	}

	public static User findByTxtAddressEmailTxtPassword(Connection connection, String txtAddressEmail, String txtPassword) throws H2ZeroFinderException, SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User result = null;
		try {
			preparedStatement = connection.prepareStatement(SQL_FIND_BY_TXT_ADDRESS_EMAIL_TXT_PASSWORD);
			ConnectionManager.setVarchar(preparedStatement, 1, txtAddressEmail);
			ConnectionManager.setVarchar(preparedStatement, 2, txtPassword);

			resultSet = preparedStatement.executeQuery();
			result = uniqueResult(resultSet);
			ConnectionManager.closeAll(resultSet, preparedStatement);
		} catch (SQLException sqlex) {
			throw sqlex;
		} catch (H2ZeroFinderException h2zfex) {
			throw new H2ZeroFinderException(h2zfex.getMessage() + "  Additionally, the parameters were "  + "[txtAddressEmail:" + txtAddressEmail + "], " + "[txtPassword:" + txtPassword + "].");
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement);
		}


		if(null == result) {
			throw new H2ZeroFinderException("Could not find result.");
		}
		return(result);
	}

	public static User findByTxtAddressEmailTxtPasswordSilent(String txtAddressEmail, String txtPassword) {
		try {
			return(findByTxtAddressEmailTxtPassword(txtAddressEmail, txtPassword));
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findByTxtAddressEmailTxtPasswordSilent(" + txtAddressEmail + ", " + txtPassword + "): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(null);
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findByTxtAddressEmailTxtPasswordSilent(" + txtAddressEmail + ", " + txtPassword + "): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(null);
		}
	}

	public static User findByTxtAddressEmailTxtPasswordSilent(Connection connection, String txtAddressEmail, String txtPassword) {
		try {
			return(findByTxtAddressEmailTxtPassword(connection, txtAddressEmail, txtPassword));
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findByTxtAddressEmailTxtPasswordSilent(" + txtAddressEmail + ", " + txtPassword + "): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(null);
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findByTxtAddressEmailTxtPasswordSilent(" + txtAddressEmail + ", " + txtPassword + "): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(null);
	}
	}

	public static List<FindNmUserDtmSignupBean> findNmUserDtmSignup() throws H2ZeroFinderException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(SQL_FIND_NM_USER_DTM_SIGNUP);

			resultSet = preparedStatement.executeQuery();
			List<FindNmUserDtmSignupBean> results = listFindNmUserDtmSignupBean(resultSet);
			return(results);
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}

	}

	public static List<FindNmUserDtmSignupBean> findNmUserDtmSignup(Connection connection) throws H2ZeroFinderException, SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement(SQL_FIND_NM_USER_DTM_SIGNUP);

			resultSet = preparedStatement.executeQuery();
			List<FindNmUserDtmSignupBean> results = listFindNmUserDtmSignupBean(resultSet);
			return(results);
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement);
		}

	}

	public static List<FindNmUserDtmSignupBean> findNmUserDtmSignupSilent() {
		try {
			return(findNmUserDtmSignup());
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findNmUserDtmSignupSilent(): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(new ArrayList<FindNmUserDtmSignupBean>());
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findNmUserDtmSignupSilent(): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(new ArrayList<FindNmUserDtmSignupBean>());
		}
	}

	public static List<FindNmUserDtmSignupBean> findNmUserDtmSignupSilent(Connection connection) {
		try {
			return(findNmUserDtmSignup());
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findNmUserDtmSignupSilent(): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(new ArrayList<FindNmUserDtmSignupBean>());
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findNmUserDtmSignupSilent(): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(new ArrayList<FindNmUserDtmSignupBean>());
		}
	}

	public static List<FindGroupNumAgeBean> findGroupNumAge() throws H2ZeroFinderException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(SQL_FIND_GROUP_NUM_AGE);

			resultSet = preparedStatement.executeQuery();
			List<FindGroupNumAgeBean> results = listFindGroupNumAgeBean(resultSet);
			return(results);
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}

	}

	public static List<FindGroupNumAgeBean> findGroupNumAge(Connection connection) throws H2ZeroFinderException, SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement(SQL_FIND_GROUP_NUM_AGE);

			resultSet = preparedStatement.executeQuery();
			List<FindGroupNumAgeBean> results = listFindGroupNumAgeBean(resultSet);
			return(results);
		} catch (SQLException sqlex) {
			throw sqlex;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement);
		}

	}

	public static List<FindGroupNumAgeBean> findGroupNumAgeSilent() {
		try {
			return(findGroupNumAge());
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findGroupNumAgeSilent(): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(new ArrayList<FindGroupNumAgeBean>());
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findGroupNumAgeSilent(): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(new ArrayList<FindGroupNumAgeBean>());
		}
	}

	public static List<FindGroupNumAgeBean> findGroupNumAgeSilent(Connection connection) {
		try {
			return(findGroupNumAge());
		} catch(H2ZeroFinderException h2zfex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("H2ZeroFinderException findGroupNumAgeSilent(): " + h2zfex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					h2zfex.printStackTrace();
				}
			}
			return(new ArrayList<FindGroupNumAgeBean>());
		} catch(SQLException sqlex) {
			if(LOGGER.isEnabledFor(Level.WARN)) {
				LOGGER.warn("SQLException findGroupNumAgeSilent(): " + sqlex.getMessage());
				if(LOGGER.isEnabledFor(Level.DEBUG)) {
					sqlex.printStackTrace();
				}
			}
			return(new ArrayList<FindGroupNumAgeBean>());
		}
	}

	/**
	 * Return a unique result for the query - in effect just the first result of
	 * query.
	 * 
	 * @param resultSet The result set of the query
	 * 
	 * @return The User that represents this result
	 * 
	 * @throws H2ZeroFinderException if no results were found
	 * @throws SQLException if there was a problem retrieving the results
	 */
	private static User uniqueResult(ResultSet resultSet) throws H2ZeroFinderException, SQLException {
		if(resultSet.first()) {
			// we have a result
			Long idUser = resultSet.getLong(1);
			Long idUserType = resultSet.getLong(2);
			Boolean flIsAlive = resultSet.getBoolean(3);
			if(resultSet.wasNull()) {
				flIsAlive = null;
			}
			Integer numAge = resultSet.getInt(4);
			String nmUsername = resultSet.getString(5);
			String txtAddressEmail = resultSet.getString(6);
			String txtPassword = resultSet.getString(7);
			Timestamp dtmSignup = resultSet.getTimestamp(8);
			if(resultSet.wasNull()) {
				dtmSignup = null;
			}

			User user = new User(idUser, idUserType, flIsAlive, numAge, nmUsername, txtAddressEmail, txtPassword, dtmSignup);

			if(resultSet.next()) {
				throw new H2ZeroFinderException("More than one result in resultset for unique finder.");
			} else {
				return(user);
			}
		} else {
			// could not get a result;
			return(null);
		}
	}

	/**
	 * Return the results as a list of User, this will be empty if
	 * none are found.
	 * 
	 * @param resultSet the results as a list of User
	 * 
	 * @return the list of results
	 * 
	 * @throws SQLException if there was a problem retrieving the results
	 */
	private static List<User> list(ResultSet resultSet) throws SQLException {
		List<User> arrayList = new ArrayList<User>();
		while(resultSet.next()) {
			arrayList.add(new User(
					resultSet.getLong(1),
					resultSet.getLong(2),
					ConnectionManager.getNullableResultBoolean(resultSet, 3),
					resultSet.getInt(4),
					resultSet.getString(5),
					resultSet.getString(6),
					resultSet.getString(7),
					ConnectionManager.getNullableResultTimestamp(resultSet, 8)));
		}
		return(arrayList);
	}

	/**
	 * Return the results as a list of FindNmUserDtmSignupBeans, this will be empty if
	 * none are found.
	 * 
	 * @param resultSet the results as a list of FindNmUserDtmSignupBean
	 * 
	 * @return the list of results
	 * 
	 * @throws SQLException if there was a problem retrieving the results
	 */
	private static List<FindNmUserDtmSignupBean> listFindNmUserDtmSignupBean(ResultSet resultSet) throws SQLException {
		List<FindNmUserDtmSignupBean> arrayList = new ArrayList<FindNmUserDtmSignupBean>();
		while(resultSet.next()) {
			arrayList.add(new FindNmUserDtmSignupBean(
					resultSet.getString(1),
					resultSet.getTimestamp(2)));
		}
		return(arrayList);
	}

	/**
	 * Return the results as a list of FindGroupNumAgeBeans, this will be empty if
	 * none are found.
	 * 
	 * @param resultSet the results as a list of FindGroupNumAgeBean
	 * 
	 * @return the list of results
	 * 
	 * @throws SQLException if there was a problem retrieving the results
	 */
	private static List<FindGroupNumAgeBean> listFindGroupNumAgeBean(ResultSet resultSet) throws SQLException {
		List<FindGroupNumAgeBean> arrayList = new ArrayList<FindGroupNumAgeBean>();
		while(resultSet.next()) {
			arrayList.add(new FindGroupNumAgeBean(
					resultSet.getInt(1),
					resultSet.getInt(2)));
		}
		return(arrayList);
	}

}