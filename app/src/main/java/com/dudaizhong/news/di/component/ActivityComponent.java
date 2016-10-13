package com.dudaizhong.news.di.component;

import android.app.Activity;

import com.dudaizhong.news.di.module.ActivityModule;
import com.dudaizhong.news.di.scopes.ActivityScope;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Dudaizhong on 2016/10/12.
 */

@ActivityScope
@Component(dependencies = AppComponent.class,modules = ActivityModule.class)
public interface ActivityComponent {

   Activity getActivity();
}
