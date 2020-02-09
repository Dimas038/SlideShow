package com.example.slideshowfinalversion.dagger.module;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class JsonModule {

    @Provides
    @Singleton
    Gson gson() {
        return new Gson();
    }
}
