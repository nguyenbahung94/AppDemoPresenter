package com.example.nbhung.mvpmap.Di.module;

import com.example.nbhung.mvpmap.Ui.Interactor.InteractorImp;
import com.example.nbhung.mvpmap.Ui.Presenter.PresenterImp;
import com.example.nbhung.mvpmap.Ui.View.MainView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nbhung on 6/29/2017.
 */
@Module
public class ActivityModule {
    private MainView mView;


    public ActivityModule(MainView mView) {
        this.mView = mView;
    }

    @Provides
    MainView provMainView() {
        return mView;
    }

    @Provides
    InteractorImp proInteractorImp() {
        return new InteractorImp();
    }

    @Provides
    PresenterImp provPresenterImp(InteractorImp interactorImp) {
        return new PresenterImp(mView, interactorImp);
    }

}
