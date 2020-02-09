package com.example.slideshowfinalversion;

import android.app.Application;

import com.example.slideshowfinalversion.dagger.AppComponent;
import com.example.slideshowfinalversion.dagger.DaggerAppComponent;
import com.example.slideshowfinalversion.dagger.module.ApplicationModule;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class App extends Application {

    public static App INSTANCE;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
    }

    public AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder().applicationModule(new ApplicationModule(this)).build();
        }
        return appComponent;
    }
}
