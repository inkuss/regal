package de.nrw.hbz.edoweb2.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.marc4j.MarcReader;
import org.marc4j.MarcXmlReader;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CacheSurvey
{
	final static Logger logger = LoggerFactory.getLogger(CacheSurvey.class);
	String cacheDir = "/opt/edoweb/edobase/";
	String uriBase = "http://orthos.hbz-nrw.de/objects";

	public CacheSurvey()
	{

	}

	public List<View> survey()
	{
		List<View> rows = new Vector<View>();
		File cacheDirFile = new File(cacheDir);
		int count = 1;
		for (File file : cacheDirFile.listFiles())
		{
			if (file.isDirectory())
			{

				View row = createSurvey(new File(file + File.separator + "."
						+ file.getName() + "_MARC.xml"), file.getName());

				row.addRights(getRights(new File(file + File.separator + "."
						+ file.getName() + "_RIGHTS.xml")));
				rows.add(row);
			}
		}
		return rows;
	}

	private String getRights(File file)
	{
		String result = "unknown";
		try
		{
			String data = FileUtils.readFileToString(file);
			if (data.contains("everyone"))
			{
				result = "everyone";
			}
			else
			{
				result = "restricted";
			}
		}
		catch (IOException e)
		{

		}
		return result;
	}

	private View createSurvey(File file, String pid)
	{
		// logger.info(file.getAbsolutePath());

		View view = new View();
		view.addPid(pid);
		view.setUri(uriBase + "/edoweb:" + pid);
		InputStream in;
		try
		{
			in = new FileInputStream(file);

			MarcReader reader = new MarcXmlReader(in);
			while (reader.hasNext())
			{
				Record record = reader.next();
				// logger.info(record.toString());

				try
				{
					ControlField alephId = (ControlField) record
							.getVariableField("001");
					view.addAlephId(alephId.getData());
				}
				catch (Exception e)
				{

				}

				try
				{
					DataField title = (DataField) record
							.getVariableField("245");
					view.addTitle(title.getSubfield('a').getData());
				}
				catch (Exception e)
				{

				}

				try
				{
					DataField urn = (DataField) record.getVariableField("856");
					view.addUrn(urn.getSubfield('u').getData());
				}
				catch (Exception e)
				{

				}

				try
				{
					List creators = record.getVariableFields(new String[] {
							"100", "110", "111", "700", "710", "711", "720" });
					for (Object c : creators)
					{
						view.addCreator(((DataField) (c)).getSubfield('a')
								.getData());
					}
				}
				catch (Exception e)
				{

				}

				try
				{
					List types = record.getVariableFields(new String[] { "655",
							"501" });
					List stypes = ((DataField) (types.get(0))).getSubfields();

					view.addType(((Subfield) stypes.get(0)).getData());
				}
				catch (Exception e)
				{

				}
				try
				{
					DataField ddc = (DataField) record.getVariableField("082");
					view.addDdc(ddc.getSubfield('a').getData());
				}
				catch (Exception e)
				{

				}
				try
				{
					DataField date = (DataField) record.getVariableField("260");
					view.addYear(date.getSubfield('c').getData());
				}
				catch (Exception e)
				{

				}
				try
				{
					DataField date = (DataField) record.getVariableField("005");
					view.addYear(date.getSubfield('a').getData());
				}
				catch (Exception e)
				{

				}

			}

		}
		catch (FileNotFoundException e)
		{
			logger.error("FileNotFoundException: " + e.getMessage());
			view.addMessage("FileNotFoundException: " + e.getMessage());
		}
		catch (Exception e)
		{
			logger.error("Exception: " + pid + e.getMessage());
			view.addMessage("Exception: " + pid + e.getMessage());

		}
		return view;
	}
}
