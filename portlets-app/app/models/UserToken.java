package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import secure.Authentication;

@Entity
public class UserToken extends Model {

	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	@Required
	@ManyToOne
	private User user;
	@Required
	private String token;
	@Required
	private Date firstSigninTime;
	private String provider = Authentication.AUTH_PROVIDER_GOOGLE;
	private String federatedUserId;
	private Date federatedcreationTime;
	private Date applicationExpiryTime;
	private int federatedExpirySeconds;
	
	public UserToken(){}

	public UserToken(User user, String token, String federatedUserId) {
		super();
		this.user = user;
		this.token = token;
		this.federatedUserId = federatedUserId;
		this.firstSigninTime = new Date();
	}

	public static Finder<Long, UserToken> find = new Finder<Long, UserToken>(Long.class, UserToken.class);

	public static List<UserToken> findByUser(User user) {
		if(user == null)
			return null;
		return find.where().eq("user", user).findList();
	}

	public static UserToken findLatestByUserId(Long userId) {
		//TODO cache
		if(userId == null)
			return null;
		return find.where().eq("user_id", userId).orderBy("first_signin_time desc").setMaxRows(1).findUnique();
	}

	public static UserToken findByToken(String token) {
		//TODO cache
		if(token == null)
			return null;
		return find.where().eq("token", token).findUnique();
	}

	public static UserToken register(UserToken newToken) {
		if(newToken == null)
			return null;
		UserToken existing = findLatestByUserId(newToken.getUser().getId());
		if(existing == null) {
			newToken.save();
			return newToken;
		} else if(existing.getToken().equals(newToken.getToken())) {
			//existing.setFederatedUserId(token.getFederatedUserId());
			existing.setToken(newToken.getToken());
			existing.save();
			return existing;
		} else {
			//existing.setFederatedUserId(token.getFederatedUserId());
			existing.delete();;
			newToken.save();
			return newToken;
		}
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getFirstSigninTime() {
		return firstSigninTime;
	}
	public void setFirstSigninTime(Date firstSigninTime) {
		this.firstSigninTime = firstSigninTime;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getFederatedUserId() {
		return federatedUserId;
	}
	public void setFederatedUserId(String federatedUserId) {
		this.federatedUserId = federatedUserId;
	}
	public Date getFederatedcreationTime() {
		return federatedcreationTime;
	}
	public void setFederatedcreationTime(Date federatedcreationTime) {
		this.federatedcreationTime = federatedcreationTime;
	}
	public int getFederatedExpirySeconds() {
		return federatedExpirySeconds;
	}
	public void setFederatedExpirySeconds(int federatedExpirySeconds) {
		this.federatedExpirySeconds = federatedExpirySeconds;
	}
	public Date getApplicationExpiryTime() {
		return applicationExpiryTime;
	}
	public void setApplicationExpiryTime(Date applicationExpiryTime) {
		this.applicationExpiryTime = applicationExpiryTime;
	}
}
