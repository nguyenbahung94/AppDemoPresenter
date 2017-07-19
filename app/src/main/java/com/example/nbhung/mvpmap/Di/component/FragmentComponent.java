package com.example.nbhung.mvpmap.Di.component;

import com.example.nbhung.mvpmap.Di.module.ActivityModule;
import com.example.nbhung.mvpmap.Di.module.NetWorkModule;
import com.example.nbhung.mvpmap.Ui.main.FragmentDirector;

import dagger.Component;

/**
 * Created by nbhung on 7/19/2017.
 */
@Component(dependencies = ActivityComponent.class, modules = {NetWorkModule.class, ActivityModule.class})
public interface FragmentComponent {
    void inject(FragmentDirector director);
}
