package models.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import models.Sector;
import models.Portlet;
import models.PortletStock;
import models.User;
import play.db.ebean.Model;

public class MockSets {
	public static final String SECTOR_MOCK_NAME = "Technology";
	public Map<String, MockSet> map;
	Sector cat;
	Portlet portletMock1;
	Portlet portletMock2;
	Portlet portletMock3;
	Portlet portletMock4;

	public MockSets() {
		persistMockPortlets();
		map = new HashMap<String, MockSet>();
		map.put("Dummy2s",
				new MockSet(new ArrayList<Model>(Arrays.asList(
						new PortletStock(portletMock1, "MSFT", 50), 
						new PortletStock(portletMock1, "GOOG", 50), 
						new PortletStock(portletMock2, "AMZN", 50), 
						new PortletStock(portletMock2, "YHOO", 50)
		))));
		map.put("Dummy4s",
				new MockSet(new ArrayList<Model>(Arrays.asList(
						new PortletStock(portletMock1, "MSFT", 25), 
						new PortletStock(portletMock1, "GOOG", 25), 
						new PortletStock(portletMock1, "AMZN", 25), 
						new PortletStock(portletMock1, "YHOO", 25), 
						new PortletStock(portletMock2, "MSFT", 25), 
						new PortletStock(portletMock2, "GOOG", 25), 
						new PortletStock(portletMock2, "AMZN", 25), 
						new PortletStock(portletMock2, "YHOO", 25), 
						new PortletStock(portletMock3, "MSFT", 25), 
						new PortletStock(portletMock3, "GOOG", 25), 
						new PortletStock(portletMock3, "AMZN", 25), 
						new PortletStock(portletMock3, "YHOO", 25), 
						new PortletStock(portletMock4, "MSFT", 25), 
						new PortletStock(portletMock4, "GOOG", 25), 
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
	private void persistMockPortlets() {
		this.cat = ensureSectorExists(new Sector(SECTOR_MOCK_NAME));
		this.portletMock1 = ensurePortletExists(new Portlet("Software Giants", getAdminUser(), "https://www.google.com.sg/images/srpr/logo11w.png", cat));
		this.portletMock2 = ensurePortletExists(new Portlet("Big Data Giants", getAdminUser(), "https://www.google.com.sg/images/srpr/logo11w.png", cat)); 
		this.portletMock3 = ensurePortletExists(new Portlet("Mobile Giants", getAdminUser(), "https://www.google.com.sg/images/srpr/logo11w.png", cat)); 
		this.portletMock4 = ensurePortletExists(new Portlet("Robotics Giants", getAdminUser(), "https://www.google.com.sg/images/srpr/logo11w.png", cat)); 
	}
	private Portlet ensurePortletExists(Portlet p) {
		Portlet fetched = Portlet.findByName(p.getName());
		if(fetched != null) {
			return fetched;
		} else {
			p.save();
			return Portlet.findByName(p.getName());
		}
	}
	private Sector ensureSectorExists(Sector c) {
		Sector fetched = Sector.findByName(c.getName());
		if(fetched != null) {
			return fetched;
		} else {
			c.save();
			return Sector.findByName(c.getName());
		}
	}
	private User getAdminUser() {
		User rooted = User.findByName(User.ROOTED_ADMIN_NAME);
		if(rooted == null) {
			rooted = new User();
			rooted.setFullName(User.ROOTED_ADMIN_NAME);
			rooted.save();
			rooted = User.findByName(User.ROOTED_ADMIN_NAME);
		}
		return rooted;
	}
}
