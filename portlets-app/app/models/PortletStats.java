package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.joda.time.LocalDate;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.SqlUpdate;

import play.Logger;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import stats.Calculations;

@Entity
public class PortletStats extends Model {
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	@ManyToOne
	private Portlet portlet;
	@Required
	private LocalDate localDate;
	private double value;
	private double dailyReturn;
	private double annualReturn;

	public PortletStats(){}
	public PortletStats(Portlet portlet, LocalDate localDate, double value) {
		super();
		this.portlet = portlet;
		this.localDate = localDate;
		this.value = value;
	}

	public static Finder<Long, PortletStats> find = new Finder<Long, PortletStats>(Long.class, PortletStats.class);

	public static Double calcDailyVolatility(Long portletId, LocalDate startDate, LocalDate endDate) {
		StringBuilder sql = new StringBuilder("SELECT STD(daily_return) as sd FROM portlet_stats WHERE portlet_id=:portletId");
		if(startDate != null)
			sql.append(" AND local_date > :startDate");
		if(endDate != null)
			sql.append(" AND local_date < :endDate");
		SqlQuery query = Ebean.createSqlQuery(sql.toString());
		query.setParameter("portletId", portletId);
		if(startDate != null)
			query.setParameter("startDate", startDate);
		if(endDate != null)
			query.setParameter("endDate", endDate);
		SqlRow row = query.findUnique();
		return row.getDouble("sd");
	}
	public static List<PortletStats> findAllByPortlet(Long portletId) {
		return PortletStats.find.where().eq("portlet_id", portletId).orderBy("local_date").findList();
	}

	public static PortletStats findLatestByPortlet(Long portletId) {
		return PortletStats.find.where().eq("portlet_id", portletId).orderBy("local_date desc").setMaxRows(1).findUnique();
	}

	public static PortletStats findPortletStatsOnDate(Long portletId, LocalDate onDate) {
		return PortletStats.find.where().le("localDate", onDate).eq("portlet_id", portletId).orderBy("local_date desc").setMaxRows(1).findUnique();
	}

	public static boolean existsOnDate(Long portletId, LocalDate onDate) {
		return find.where().eq("portlet_id", portletId).eq("local_date", onDate).findRowCount() > 0;
	}
	
	/**
	 * Warning: It creates stats, even for dates before portlet creation date
	 */
	public static void overwriteDuring(Portlet portlet, LocalDate startDate, LocalDate endDate) {
		List<PriceImportHistory> dates = PriceImportHistory.findDatesDuring(portlet.getPrimaryExchange(), startDate, endDate);
		deleteDuring(portlet.getId(), startDate, endDate);
		for (PriceImportHistory priceImportHistory : dates) {
			//deleteOnDate(portletId, onDate);
			persist(portlet, priceImportHistory.getLocalDate());
		}
	}

	public static void deleteOnDate(Long portletId, LocalDate onDate) {
		List<PortletStats> list = PortletStats.find.where().eq("localDate", onDate).eq("portlet_id", portletId).findList();
		for (PortletStats portletStats : list) {
			portletStats.delete();
		}
	}

	public static void deleteDuring(Long portletId, LocalDate startDate, LocalDate endDate) {
		SqlUpdate sql = Ebean.createSqlUpdate("DELETE FROM portlet_stats WHERE portlet_id=:portletId AND local_date > :startDate AND local_date < :endDate");
		sql.setParameter("portletId", portletId);
		sql.setParameter("startDate", startDate);
		sql.setParameter("endDate", endDate);
		sql.execute();
	}
	
	public static void persistPortletsforDate(String exchange, LocalDate onDate) {
		if(!PriceImportHistory.existsOnDate(exchange, onDate)) {
			Logger.warn("Price data doesn't exist for: " + exchange + " on: " + onDate);
			return;
		}
		List<Portlet> portlets = Portlet.findCreatedBefore(exchange, onDate.plusDays(1).toDate());
		for (Portlet portlet : portlets) {
			if(existsOnDate(portlet.getId(), onDate)) {
				Logger.info("PortletStats already exists, so skipping update for portlet: " + portlet.getName() + " for Date: " + onDate);
				return;
			}
			persist(portlet, onDate);
		}
	}
	
	public static void persistPortletHistory(Portlet portlet) {
		List<PriceImportHistory> dates = PriceImportHistory.findDatesAfter(portlet.getPrimaryExchange(), new LocalDate(portlet.getCreatedOn()));
		for (PriceImportHistory priceImportHistory : dates) {
			if(!existsOnDate(portlet.getId(), priceImportHistory.getLocalDate())) {
				persist(portlet, priceImportHistory.getLocalDate());
			}
		}
	}

	public static void persist(Portlet portlet, LocalDate onDate) {
		List<PortletStock> psl = PortletStock.findByPortlet(portlet);
		double value = Calculations.calcPortletValue(psl , onDate);
		PortletStats stats = new PortletStats(portlet, onDate, value);
		PortletStats dayBack = PortletStats.findPortletStatsOnDate(portlet.getId(), onDate.minusDays(1));
		if(dayBack != null)
			stats.setDailyReturn((value - dayBack.getValue())/dayBack.getValue());
		PortletStats yearBack = PortletStats.findPortletStatsOnDate(portlet.getId(), onDate.minusYears(1));
		if(yearBack != null)
			stats.setAnnualReturn((value - yearBack.getValue())/yearBack.getValue());
		stats.save();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Portlet getPortlet() {
		return portlet;
	}
	public void setPortlet(Portlet portlet) {
		this.portlet = portlet;
	}
	public LocalDate getLocalDate() {
		return localDate;
	}
	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public double getDailyReturn() {
		return dailyReturn;
	}
	public void setDailyReturn(double dailyReturn) {
		this.dailyReturn = dailyReturn;
	}
	public double getAnnualReturn() {
		return annualReturn;
	}
	public void setAnnualReturn(double annualReturn) {
		this.annualReturn = annualReturn;
	}
}
