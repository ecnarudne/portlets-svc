package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.joda.time.LocalDate;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class PriceImportHistory extends Model{
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	@Required
	private String exchange;
	private LocalDate localDate;
	private Date importDate;
	private String filepath;

	public PriceImportHistory(String exchange, LocalDate localDate,
			String filepath) {
		super();
		this.exchange = exchange;
		this.localDate = localDate;
		this.filepath = filepath;
		this.importDate = new Date();
	}

	public static Finder<Long, PriceImportHistory> find = new Finder<Long, PriceImportHistory>(Long.class, PriceImportHistory.class);

	public static boolean existsOnDate(String exchange, LocalDate onDate) {
		return PriceImportHistory.find.where()
				.ieq("exchange", exchange)
				.eq("local_date", onDate)
				.findRowCount() > 0;
	}
	public static List<PriceImportHistory> findDatesDuring(String exchange, LocalDate startDate, LocalDate endDate) {
		return PriceImportHistory.find.where()
				.ieq("exchange", exchange)
				.ge("local_date", startDate)
				.le("local_date", endDate)
				.orderBy("local_date").findList();
	}
	public static List<PriceImportHistory> findDatesAfter(String exchange, LocalDate afterDate) {
		return PriceImportHistory.find.where()
				.ieq("exchange", exchange)
				.ge("local_date", afterDate)
				.orderBy("local_date").findList();
	}
	public static boolean existsInDb(String exchange, LocalDate localDate) {
		int count = PriceImportHistory.find.where()
				.ieq("exchange", exchange)
				.eq("local_date", localDate)
				.findRowCount();
		return(count > 0);
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public LocalDate getLocalDate() {
		return localDate;
	}
	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}
	public Date getImportDate() {
		return importDate;
	}
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
}
