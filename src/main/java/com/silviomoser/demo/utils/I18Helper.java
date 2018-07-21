package com.silviomoser.demo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created by silvio on 20.07.18.
 */
public class I18Helper {

    private static final String RESOURCES = "gui_resources";

    private final  ResourceBundle resourceBundle;

    public I18Helper(Locale locale){
        resourceBundle = ResourceBundle.getBundle(RESOURCES,locale, new UTF8Control());
    }

    public I18Helper(){
        resourceBundle = ResourceBundle.getBundle(RESOURCES,Locale.getDefault(), new UTF8Control());
    }

    public String getMessage(String key) {
        try{
            return  resourceBundle.getString(key);
        }catch(MissingResourceException e){
            //LOGGER.warn("Resource "+key+" was not found in the resources file");
            return key;
        }
    }

    private class UTF8Control extends ResourceBundle.Control {
        public ResourceBundle newBundle
                (String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException
        {
            // The below is a copy of the default implementation.
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    // Only this line is changed to make it to read properties files as UTF-8.
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }
}
