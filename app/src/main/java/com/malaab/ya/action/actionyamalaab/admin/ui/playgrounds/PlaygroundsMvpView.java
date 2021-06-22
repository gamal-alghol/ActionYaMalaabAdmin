package com.malaab.ya.action.actionyamalaab.admin.ui.playgrounds;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;

public interface PlaygroundsMvpView extends MvpView {

    void onUserLoggedIn(User user);

    void onGetPlayground(List<Playground> playgrounds);
}
