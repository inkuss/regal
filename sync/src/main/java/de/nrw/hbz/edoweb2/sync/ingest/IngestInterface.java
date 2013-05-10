/*
 * Copyright 2012 hbz NRW (http://www.hbz-nrw.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.nrw.hbz.edoweb2.sync.ingest;

import de.nrw.hbz.edoweb2.datatypes.ContentModel;
import de.nrw.hbz.edoweb2.sync.extern.DigitalEntity;

/**
 * @author Jan Schnasse, schnasse@hbz-nrw.de
 * 
 */
public interface IngestInterface
{
	/**
	 * Ingests a digitool object in the archive
	 * 
	 * @param dtlBean
	 *            the java representation of a digitool object
	 */
	public abstract void ingest(DigitalEntity dtlBean);

	/**
	 * Deletes a object from the archive
	 * 
	 * @param pid
	 *            the pid of the object
	 */
	public abstract void delete(String pid);

	/**
	 * Updates a object in the archive
	 * 
	 * @param dtlBean
	 *            the digitool object
	 */
	public abstract void update(DigitalEntity dtlBean);

	/**
	 * Recreates only the used content models
	 * 
	 * @return the model
	 */
	public abstract ContentModel createContentModel();

	/**
	 * @param usr
	 *            a valid user
	 * @param pwd
	 *            the users password
	 * @param host
	 *            the host of the webapi
	 */
	public abstract void init(String host, String user, String password);
}