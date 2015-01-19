package synapticloop.h2zero.ant;

/*
 * Copyright (c) 2013-2015 synapticloop.
 * All rights reserved.
 *
 * This source code and any derived binaries are covered by the terms and
 * conditions of the Licence agreement ("the Licence").  You may not use this
 * source code or any derived binaries except in compliance with the Licence.
 * A copy of the Licence is available in the file named LICENCE shipped with
 * this source code or binaries.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * Licence for the specific language governing permissions and limitations
 * under the Licence.
 */

import java.io.File;
import java.sql.SQLException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import synapticloop.h2zero.revenge.ModelBuilder;

public class H2ZeroRevengeTask extends Task {
	private String outFile = null;
	private String host = null;
	private String database = null;
	private String user = null; 
	private String password = null;

	@Override
	public void execute() throws BuildException {
		if(null == outFile || host == null || database == null || user == null || password == null) {
			getProject().log("Attributes 'host', 'database', 'user', 'password' and 'outFile' are required, exiting...", Project.MSG_ERR);
			return;
		}

		File outFileWrite = new File(outFile);
		if(outFileWrite.exists() || outFileWrite.isDirectory()) {
			getProject().log("'outFile' exists or is a directory, exiting...", Project.MSG_ERR);
			return;
		}

		// else we are good to go
		try {
			ModelBuilder modelBuilder = new ModelBuilder(host, database, user, password);
			System.out.println(modelBuilder.generate());
		} catch (ClassNotFoundException cnfex) {
			cnfex.printStackTrace();
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}

	}

	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}

	public String getOutFile() {
		return outFile;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
