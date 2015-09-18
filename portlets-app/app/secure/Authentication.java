package secure;

import models.User;
import models.UserToken;
import play.Logger;
import play.libs.F.Callback;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Authentication {
	public static final String AUTH_PROVIDER_FACEBOOK = "facebook";
	public static final String AUTH_PROVIDER_GOOGLE = "google";
	public static final String AUTH_VALIDATION_URL_GOOGLE = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=";

	public static void registerAuthToken(final String token) {
		Promise<WSResponse> responsePromise = WS.url(AUTH_VALIDATION_URL_GOOGLE + token).get();
		responsePromise.onRedeem(new Callback<WSResponse>() {
			@Override
			public void invoke(WSResponse resp) throws Throwable {
				Logger.debug("onRedeem WSResponse Body: " + resp.getBody());
			    JsonNode userInfoJson = (new ObjectMapper()).readTree(resp.getBody());
				String providerId = userInfoJson.findPath("user_id").asText();
				Logger.debug("onRedeem user_id: >" + providerId + "<");
				if(providerId != null && !providerId.isEmpty()) {
					User user = User.findByProvider(AUTH_PROVIDER_GOOGLE, providerId);
					Logger.debug("onRedeem user: " + user);
					if(user != null) {
						UserToken userToken = new UserToken(user, token, providerId);
						userToken.setFederatedExpirySeconds(userInfoJson.findPath("expires_in").asInt());
						Logger.debug("onRedeem userToken: " + userToken);
						userToken.save();
						//TODO inform user/client about success
					} else {
						Logger.error("Not a valid User. Auth provider ID: " + providerId);
						//TODO inform user/client about failure
					}
				} else {
					Logger.error("Invalid AuthToken: " + token + ". Provider Response: " + resp.getBody());
					//TODO inform user/client about failure
				}
			}
		});
		responsePromise.onFailure(new Callback<Throwable>() {
			@Override
			public void invoke(Throwable e) throws Throwable {
				Logger.error("Failed to validate/register AuthToken: " + e);
				//TODO inform user/client about failure
			}
		});
	}

	public static boolean isAuthFacebook(String provider) {
		return provider.equals(AUTH_PROVIDER_FACEBOOK);
	}

	public static boolean isAuthGoogle(String provider) {
		return provider.equals(AUTH_PROVIDER_GOOGLE);
	}
}