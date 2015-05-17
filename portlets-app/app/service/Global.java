package service;
import models.Portlet;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.data.format.Formatters;

import com.feth.play.module.pa.PlayAuthenticate;

import converters.PortletFormatter;

public class Global extends GlobalSettings {

    public void onStart(final Application app) {
        PlayAuthenticate.setResolver(new BootstrapAuthResolver());
        Formatters.register(Portlet.class, new PortletFormatter());
    }

    public void onStop(Application app) {
        Logger.info("Global.onStop");
    }
}