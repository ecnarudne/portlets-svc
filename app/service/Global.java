package service;
import play.Application;
import play.GlobalSettings;

import com.feth.play.module.pa.PlayAuthenticate;

public class Global extends GlobalSettings {

    public void onStart(final Application app) {
        PlayAuthenticate.setResolver(new BootstrapAuthResolver());
    }
}