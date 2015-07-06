package models.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import models.Sector;
import models.Portlet;
import models.PortletStock;
import models.User;
import models.UserPortletStock;
import play.db.ebean.Model;

public class MockSets {
	public static final String SECTOR_MOCK_TECH = "Technology";
	public static final String SECTOR_MOCK_INT = "Internet";
	public Map<String, MockSet> map;
	Sector cat;
	Sector cat2;
	Portlet portletMock1;
	Portlet portletMock2;
	Portlet portletMock3;
	Portlet portletMock4;

	public MockSets() {
		persistMockPortlets();
		map = new HashMap<String, MockSet>();
/*		map.put("Dummy2s",
				new MockSet(new ArrayList<Model>(Arrays.asList(
						new PortletStock(portletMock1, "MSFT", 50), 
						new PortletStock(portletMock1, "GOOG", 50), 
						new PortletStock(portletMock2, "AMZN", 50), 
						new PortletStock(portletMock2, "YHOO", 50)
		))));*/
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
	public MockSet persist(String nick, User user) {
		MockSet toPersist = map.get(nick);
		if(toPersist != null) {
			for (Model model : toPersist.models) {
				model.save();
			}
		}
		new UserPortletStock(user, portletMock1, "MSFT", 10, 41).save();
		new UserPortletStock(user, portletMock1, "GOOG", 1, 520).save();
		new UserPortletStock(user, portletMock1, "AMZN", 1, 370).save();
		new UserPortletStock(user, portletMock1, "YHOO", 10, 44).save();
		return toPersist;
	}
	private void persistMockPortlets() {
		this.cat = ensureSectorExists(new Sector(SECTOR_MOCK_TECH));
		this.cat2 = ensureSectorExists(new Sector(SECTOR_MOCK_INT));
		this.portletMock1 = ensurePortletExists(new Portlet("Software Giants", getAdminUser(), "https://www.google.com.sg/images/srpr/logo11w.png", Arrays.asList(cat, cat2)));
		this.portletMock2 = ensurePortletExists(new Portlet("Big Data Giants", getAdminUser(), "https://www.google.com.sg/images/srpr/logo11w.png", Arrays.asList(cat, cat2))); 
		this.portletMock3 = ensurePortletExists(new Portlet("Mobile Giants", getAdminUser(), "https://www.google.com.sg/images/srpr/logo11w.png", Arrays.asList(cat, cat2))); 
		this.portletMock4 = ensurePortletExists(new Portlet("Robotics Giants", getAdminUser(), "https://www.google.com.sg/images/srpr/logo11w.png", Arrays.asList(cat, cat2))); 
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
	public static Number[][] mockGraphData() {
		return new Number[][] {
				  {1001887200000l, 368.38},
				  {1004569200000l, 369.66},
				  {1007161200000l, 371.11},
				  {1009839600000l, 372.36},
				  {1012518000000l, 373.09},
				  {1014937200000l, 373.81},
				  {1017612000000l, 374.93},
				  {1020204000000l, 375.58},
				  {1022882400000l, 375.44},
				  {1025474400000l, 373.86},
				  {1028152800000l, 371.77},
				  {1030831200000l, 370.73},
				  {1033423200000l, 370.50},
				  {1036105200000l, 372.18},
				  {1038697200000l, 373.70},
				  {1041375600000l, 374.92},
				  {1044054000000l, 375.62},
				  {1046473200000l, 376.51},
				  {1049148000000l, 377.75},
				  {1051740000000l, 378.54},
				  {1054418400000l, 378.20},
				  {1057010400000l, 376.68},
				  {1059688800000l, 374.43},
				  {1062367200000l, 373.11},
				  {1064959200000l, 373.10},
				  {1067641200000l, 374.77},
				  {1070233200000l, 375.97},
				  {1072911600000l, 377.03},
				  {1075590000000l, 377.87},
				  {1078095600000l, 378.88},
				  {1080770400000l, 380.42},
				  {1083362400000l, 380.62},
				  {1086040800000l, 379.70},
				  {1088632800000l, 377.43},
				  {1091311200000l, 376.32},
				  {1093989600000l, 374.19},
				  {1096581600000l, 374.47},
				  {1099263600000l, 376.15},
				  {1101855600000l, 377.51},
				  {1104534000000l, 378.43},
				  {1107212400000l, 379.70},
				  {1109631600000l, 380.92},
				  {1112306400000l, 382.18},
				  {1114898400000l, 382.45},
				  {1117576800000l, 382.14},
				  {1120168800000l, 380.60},
				  {1122847200000l, 378.64},
				  {1125525600000l, 376.73},
				  {1128117600000l, 376.84},
				  {1130799600000l, 378.29},
				  {1133391600000l, 380.06},
				  {1136070000000l, 381.40},
				  {1138748400000l, 382.20},
				  {1141167600000l, 382.66},
				  {1143842400000l, 384.69},
				  {1146434400000l, 384.94},
				  {1149112800000l, 384.01},
				  {1151704800000l, 382.14},
				  {1154383200000l, 380.31},
				  {1157061600000l, 378.81},
				  {1159653600000l, 379.03},
				  {1162335600000l, 380.17},
				  {1164927600000l, 381.85},
				  {1167606000000l, 382.94},
				  {1170284400000l, 383.86},
				  {1172703600000l, 384.49},
				  {1175378400000l, 386.37},
				  {1177970400000l, 386.54},
				  {1180648800000l, 385.98},
				  {1183240800000l, 384.36},
				  {1185919200000l, 381.85},
				  {1188597600000l, 380.74},
				  {1191189600000l, 381.15},
				  {1193871600000l, 382.38},
				  {1196463600000l, 383.94},
				  {1199142000000l, 385.44}
				};
	}
}
