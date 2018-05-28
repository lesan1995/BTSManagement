package com.example.lequa.btsmanagement.di;

import com.example.lequa.btsmanagement.HomeActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract HomeActivity contributeMainActivity();
}
