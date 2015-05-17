package models.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import models.Portlet;
import models.PortletStock;
import models.User;
import play.db.ebean.Model;

public class MockSets {
	public Map<String, MockSet> map;
	Portlet portletMock1 = new Portlet("PortletMock1", getAdminUser(), "/bower_components/jquery-flot/examples/image/hs-2004-27-a-large-web.jpg"); 
	Portlet portletMock2 = new Portlet("PortletMock2", getAdminUser(), "/bower_components/jquery-flot/examples/image/hs-2004-27-a-large-web.jpg"); 
	Portlet portletMock3 = new Portlet("PortletMock3", getAdminUser(), "/bower_components/jquery-flot/examples/image/hs-2004-27-a-large-web.jpg"); 
	Portlet portletMock4 = new Portlet("PortletMock4", getAdminUser(), "/bower_components/jquery-flot/examples/image/hs-2004-27-a-large-web.jpg"); 

	public MockSets() {
		map = new HashMap<String, MockSet>();
		map.put("Dummy2s",
				new MockSet(new ArrayList<Model>(Arrays.asList(
						portletMock1, 
						portletMock2, 
						new PortletStock(portletMock1, "StockMock1", 50), 
						new PortletStock(portletMock1, "StockMock2", 50), 
						new PortletStock(portletMock2, "StockMock3", 50), 
						new PortletStock(portletMock2, "StockMock4", 50)
		))));
		map.put("Dummy4s",
				new MockSet(new ArrayList<Model>(Arrays.asList(
						portletMock1, 
						portletMock2, 
						portletMock3, 
						portletMock4, 
						new PortletStock(portletMock1, "StockMock1", 25), 
						new PortletStock(portletMock1, "StockMock2", 25), 
						new PortletStock(portletMock1, "StockMock3", 25), 
						new PortletStock(portletMock1, "StockMock4", 25), 
						new PortletStock(portletMock2, "StockMock1", 25), 
						new PortletStock(portletMock2, "StockMock2", 25), 
						new PortletStock(portletMock2, "StockMock3", 25), 
						new PortletStock(portletMock2, "StockMock4", 25), 
						new PortletStock(portletMock3, "StockMock1", 25), 
						new PortletStock(portletMock3, "StockMock2", 25), 
						new PortletStock(portletMock3, "StockMock3", 25), 
						new PortletStock(portletMock3, "StockMock4", 25), 
						new PortletStock(portletMock4, "StockMock1", 25), 
						new PortletStock(portletMock4, "StockMock2", 25), 
						new PortletStock(portletMock4, "StockMock3", 25), 
						new PortletStock(portletMock4, "StockMock4", 25)
		))));
	}
	public MockSet persist(String nick) {
		MockSet toRun = map.get(nick);
		if(toRun != null) {
			for (Model model : toRun.models) {
				model.save();
			}
		}
		return toRun;
	}
	private User getAdminUser() {
		User rooted = User.find.byId(User.ROOTED_ADMIN_ID);
		if(rooted == null) {
			rooted = new User();
			rooted.setFullName(User.ROOTED_ADMIN_NAME);
			rooted.save();
		}
		return rooted;
	}
}
