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
	Portlet portletMock1 = new Portlet("Software Giants", getAdminUser(), "https://www.google.com.sg/images/srpr/logo11w.png"); 
	Portlet portletMock2 = new Portlet("Big Data Giants", getAdminUser(), "https://www.google.com.sg/images/srpr/logo11w.png"); 
	Portlet portletMock3 = new Portlet("Mobile Giants", getAdminUser(), "https://www.google.com.sg/images/srpr/logo11w.png"); 
	Portlet portletMock4 = new Portlet("Robotics Giants", getAdminUser(), "https://www.google.com.sg/images/srpr/logo11w.png"); 

	public MockSets() {
		map = new HashMap<String, MockSet>();
		map.put("Dummy2s",
				new MockSet(new ArrayList<Model>(Arrays.asList(
						portletMock1, 
						portletMock2, 
						new PortletStock(portletMock1, "MSFT", 50), 
						new PortletStock(portletMock1, "GOGL", 50), 
						new PortletStock(portletMock2, "AMZN", 50), 
						new PortletStock(portletMock2, "YHOO", 50)
		))));
		map.put("Dummy4s",
				new MockSet(new ArrayList<Model>(Arrays.asList(
						portletMock1, 
						portletMock2, 
						portletMock3, 
						portletMock4, 
						new PortletStock(portletMock1, "MSFT", 25), 
						new PortletStock(portletMock1, "GOGL", 25), 
						new PortletStock(portletMock1, "AMZN", 25), 
						new PortletStock(portletMock1, "YHOO", 25), 
						new PortletStock(portletMock2, "MSFT", 25), 
						new PortletStock(portletMock2, "GOGL", 25), 
						new PortletStock(portletMock2, "AMZN", 25), 
						new PortletStock(portletMock2, "YHOO", 25), 
						new PortletStock(portletMock3, "MSFT", 25), 
						new PortletStock(portletMock3, "GOGL", 25), 
						new PortletStock(portletMock3, "AMZN", 25), 
						new PortletStock(portletMock3, "YHOO", 25), 
						new PortletStock(portletMock4, "MSFT", 25), 
						new PortletStock(portletMock4, "GOGL", 25), 
						new PortletStock(portletMock4, "AMZN", 25), 
						new PortletStock(portletMock4, "YHOO", 25)
		))));
	}
	public MockSet persist(String nick) {
		MockSet toPersist = map.get(nick);
		if(toPersist != null) {
			for (Model model : toPersist.models) {
				model.save();
			}
		}
		return toPersist;
	}
	private User getAdminUser() {
		User rooted = User.find.byId(User.ROOTED_ADMIN_ID);
		if(rooted == null) {
			rooted = new User();
			rooted.setId(User.ROOTED_ADMIN_ID);
			rooted.setFullName(User.ROOTED_ADMIN_NAME);
			rooted.save();
		}
		return rooted;
	}
}
