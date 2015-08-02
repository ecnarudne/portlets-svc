package models.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import models.Portlet;
import models.PortletStock;
import models.Sector;
import models.User;
import models.UserPortletStock;
import play.Logger;
import play.db.ebean.Model;

public class MockSets {
	public static final String SECTOR_MOCK_TECH = "Technology";
	public static final String SECTOR_MOCK_FIN = "Financial";
	public Map<String, MockSet> map;
	Sector cat;
	Sector cat2;
	Portlet portletMock1;
	Portlet portletMock2;
	Portlet portletMock3;
	Portlet portletMock4;

	public void persistSectors() {
		this.cat = ensureSectorExists(new Sector(SECTOR_MOCK_TECH));
		this.cat2 = ensureSectorExists(new Sector(SECTOR_MOCK_FIN));
	}
	public MockSet persist(String nick, User user) {
		persistMockPortlets(user);
		map = new HashMap<String, MockSet>();
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
		MockSet toPersist = map.get(nick);
		if(toPersist != null) {
			for (Model model : toPersist.models) {
				Logger.debug("Saving model: " + model);
				model.save();
			}
		}
		new UserPortletStock(user, portletMock1, "MSFT", 10, 41).save();
		new UserPortletStock(user, portletMock1, "GOOG", 1, 520).save();
		new UserPortletStock(user, portletMock1, "AMZN", 1, 370).save();
		new UserPortletStock(user, portletMock1, "YHOO", 10, 44).save();
		return toPersist;
	}
	private void persistMockPortlets(User user) {
		this.cat = ensureSectorExists(new Sector(SECTOR_MOCK_TECH));
		this.cat2 = ensureSectorExists(new Sector(SECTOR_MOCK_FIN));
		if(user == null)
			user = getAdminUser();
		this.portletMock1 = ensurePortletExists(new Portlet("Software Giants" + " - " + user.getFullName(), user, "https://www.google.com.sg/images/srpr/logo11w.png", Arrays.asList(cat, cat2)));
		this.portletMock2 = ensurePortletExists(new Portlet("Big Data Giants" + " - " + user.getFullName(), user, "https://www.google.com.sg/images/srpr/logo11w.png", Arrays.asList(cat, cat2))); 
		this.portletMock3 = ensurePortletExists(new Portlet("Mobile Giants" + " - " + user.getFullName(), user, "https://www.google.com.sg/images/srpr/logo11w.png", Arrays.asList(cat, cat2))); 
		this.portletMock4 = ensurePortletExists(new Portlet("Robotics Giants" + " - " + user.getFullName(), user, "https://www.google.com.sg/images/srpr/logo11w.png", Arrays.asList(cat, cat2))); 
	}
	private Portlet ensurePortletExists(Portlet p) {
		Portlet fetched = Portlet.findByName(p.getName());
		if(fetched != null) {
			return fetched;
		} else {
			Logger.debug("Saving Portlet: " + p);
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
