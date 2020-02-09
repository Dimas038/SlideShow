package com.example.slideshowfinalversion.dagger;

import com.example.slideshowfinalversion.dagger.module.ApplicationModule;
import com.example.slideshowfinalversion.dagger.module.JsonModule;
import com.example.slideshowfinalversion.dagger.module.NetworkModule;
import com.example.slideshowfinalversion.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        JsonModule.class,
        NetworkModule.class
})
public interface AppComponent {

    void inject(MainActivity activity);
}
