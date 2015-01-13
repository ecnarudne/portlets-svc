package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.feth.play.module.pa.providers.oauth2.facebook.FacebookAuthUser;
import com.feth.play.module.pa.providers.oauth2.google.GoogleAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.Logger;


@Entity
public class User extends Model {

	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	@Required
	private String fullName;
	@Required
	private String email;
	private String preferredName;
	private String googleId;
	private String facebookId;
	private String profileLink;
	private String profilePicture;
	private String locale;
	private Boolean male;
	private boolean emailVerified = false;
	private UserValidityState userValidity = UserValidityState.PENDING;
	private Integer timezone;
	private String birthday;

	/* Constructors */
	public User() {}
	public User(String provider, String providerId) {
		super();
		if(isAuthGoogle(provider)) {
			this.googleId = providerId;
		} else if(isAuthFacebook(provider)) {
			this.facebookId = providerId;
		} else {
			Logger.error("Unknown Auth Provider: " + provider);
		}
	}

	/* eBean Methods */
	public static User findByProvider(String provider, String providerId) {
		//TODO must cache
		if(provider == null || providerId == null)
			return null;
		if(isAuthGoogle(provider)) {
	    	List<User> users = find.where().eq("googleId", providerId).findList();
			if(users != null && !users.isEmpty())
				return users.get(0);
		} else if(isAuthFacebook(provider)) {
	    	List<User> users = find.where().eq("facebookId", providerId).findList();
			if(users != null && !users.isEmpty())
				return users.get(0);
		} else {
			Logger.error("Unknown provider: " + provider);
		}
		return null;
	}
	public static User create(AuthUser authUser) {
		String provider = authUser.getProvider();
		User newUser = new User();
		if(isAuthGoogle(provider)) {
			newUser.googleId = authUser.getId();
			GoogleAuthUser googleAuthUser = (GoogleAuthUser) authUser;
			newUser.fullName = googleAuthUser.getName();
			newUser.email = googleAuthUser.getEmail();
			newUser.preferredName = googleAuthUser.getFirstName();
			newUser.setMale(googleAuthUser.getGender());
			newUser.profilePicture = googleAuthUser.getPicture();
			newUser.profileLink = googleAuthUser.getProfileLink();
			if(googleAuthUser.getLocale() != null)
				newUser.locale = googleAuthUser.getLocale().toString();
		} else if(isAuthFacebook(provider)) {
			newUser.facebookId = authUser.getId();
			FacebookAuthUser facebookAuthUser = (FacebookAuthUser) authUser;
			newUser.fullName = facebookAuthUser.getName();
			newUser.email = facebookAuthUser.getEmail();
			newUser.preferredName = facebookAuthUser.getFirstName();
			newUser.profileLink = facebookAuthUser.getProfileLink();
			newUser.setMale(facebookAuthUser.getGender());
			newUser.profilePicture = facebookAuthUser.getPicture();
			newUser.timezone = facebookAuthUser.getTimezone();
			newUser.birthday = facebookAuthUser.getBirthday();
			if(facebookAuthUser.getLocale() != null)
				newUser.locale = facebookAuthUser.getLocale().toString();
		} else {
			Logger.error("Unknown Auth Provider: " + provider);
			return null;
		}
		newUser.save();
		Logger.debug("Saved newUser: " + newUser);
		System.out.println("Saved newUser: " + newUser);
		return newUser;
	}
	public static User findByAuthUserIdentity(AuthUserIdentity identity) {
		if(identity == null)
			return null;
		return findByProvider(identity.getProvider(), identity.getId());
	}
	public static boolean existsByAuthUserIdentity(AuthUser authUser) {
		if(authUser == null)
			return false;
		return (findByProvider(authUser.getProvider(), authUser.getId()) != null);
	}
	public static void addLinkedAccount(AuthUser oldUser, AuthUser newUser) {
		// TODO Auto-generated method stub
		
	}
	public static void merge(AuthUser oldUser, AuthUser newUser) {
		// TODO Auto-generated method stub
		
	}
	public static Finder<Long, User> find = new Finder<Long, User>(Long.class, User.class);

	/* Helpers */
	public static boolean isAuthFacebook(String provider) {
		return provider.equals("facebook");
	}
	public static boolean isAuthGoogle(String provider) {
		return provider.equals("google");
	}
	public void setMale(String gender) {
		if(gender != null) {
			if(gender.equalsIgnoreCase("male") || gender.startsWith("M")) {
				this.male = true;
			} else if(gender.equalsIgnoreCase("female") || gender.startsWith("F")) {
				this.male = false;
			} else {
				Logger.error("Unknown Gender: " + gender);
			}
		}
	}

	/* Boiler-plates */
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPreferredName() {
		return preferredName;
	}
	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}
	public String getGoogleId() {
		return googleId;
	}
	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	public String getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	public String getProfileLink() {
		return profileLink;
	}
	public void setProfileLink(String profileLink) {
		this.profileLink = profileLink;
	}
	public String getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public Boolean getMale() {
		return male;
	}
	public void setMale(Boolean male) {
		this.male = male;
	}
	public boolean isEmailVerified() {
		return emailVerified;
	}
	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
	public UserValidityState getUserValidity() {
		return userValidity;
	}
	public void setUserValidity(UserValidityState userValidity) {
		this.userValidity = userValidity;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public Integer getTimezone() {
		return timezone;
	}
	public void setTimezone(Integer timezone) {
		this.timezone = timezone;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", fullName=" + fullName + ", email=" + email
				+ ", preferredName=" + preferredName + ", googleId=" + googleId
				+ ", facebookId=" + facebookId + ", profileLink=" + profileLink
				+ ", profilePicture=" + profilePicture + ", locale=" + locale
				+ ", male=" + male + ", emailVerified=" + emailVerified
				+ ", userValidity=" + userValidity + ", timezone=" + timezone
				+ ", birthday=" + birthday + "]";
	}
}