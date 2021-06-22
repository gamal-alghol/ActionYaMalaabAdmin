package com.malaab.ya.action.actionyamalaab.admin.ui.users.playgrounds;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;


public interface OwnerPlaygroundsMvpView extends MvpView {

    void onGetPlayground(List<Playground> playgrounds);
}
