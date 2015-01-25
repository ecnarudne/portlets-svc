package converters;

import java.text.ParseException;
import java.util.Locale;

import models.Portlet;
import play.data.format.Formatters.SimpleFormatter;

public class PortletFormatter extends SimpleFormatter<Portlet> {

	@Override
	public Portlet parse(String id, Locale locale) throws ParseException {
		if(id == null)
			return null;
		return Portlet.find.byId(Long.parseLong(id));
	}

	@Override
	public String print(Portlet portlet, Locale locale) {
		if(portlet == null)
			return null;
		return portlet.getId().toString();
	}

}
