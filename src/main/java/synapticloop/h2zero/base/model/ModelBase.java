package synapticloop.h2zero.base.model;

/*
 * Copyright (c) 2012-2015 synapticloop.
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

import java.sql.Connection;
import java.sql.SQLException;

import synapticloop.h2zero.base.exception.H2ZeroPrimaryKeyException;

public abstract class ModelBase {
	protected boolean isDirty = false;

	protected abstract void insert(Connection connection) throws SQLException, H2ZeroPrimaryKeyException;
	protected abstract void update(Connection connection) throws SQLException, H2ZeroPrimaryKeyException;
	protected abstract void delete(Connection connection) throws SQLException, H2ZeroPrimaryKeyException;

	protected boolean isDifferent(Object one, Object two) {
		if(null == one) {
			return(two != null);
		} else {
			if(null == two) {
				return(true);
			} else {
				return(!one.equals(two));
			}
		}
	}
}