package service;
import play.Application;
import play.GlobalSettings;
import play.Logger;


import com.feth.play.module.pa.PlayAuthenticate;

public class Global extends GlobalSettings {

    public void onStart(final Application app) {
        PlayAuthenticate.setResolver(new BootstrapAuthResolver());
    }

    public void onStop(Application app) {
        Logger.info("Global.onStop");
    }
}