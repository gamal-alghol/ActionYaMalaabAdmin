package com.malaab.ya.action.actionyamalaab.admin.ui.users;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;


public interface UsersMvpView extends MvpView {

    void onGetUsers(List<User> users);

    void onSearch(User user);
}
