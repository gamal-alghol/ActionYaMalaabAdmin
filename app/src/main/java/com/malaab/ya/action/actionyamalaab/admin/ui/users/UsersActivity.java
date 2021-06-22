package com.malaab.ya.action.actionyamalaab.admin.ui.users;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.SortOption;
import com.malaab.ya.action.actionyamalaab.admin.custom.DialogSort;
import com.malaab.ya.action.actionyamalaab.admin.custom.SimpleDividerItemDecoration;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventRefresh;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventSort;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.details.UsersDetailsActivity;
import com.malaab.ya.action.actionyamalaab.admin.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.KeyboardUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.MultiTextWatcher;
import com.yayandroid.theactivitymanager.TheActivityManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UsersActivity extends BaseActivity implements UsersMvpView, MultiTextWatcher.OnTextWatcher {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.search_ln_sort)
    public LinearLayout search_ln_sort;
    @BindView(R.id.search_txt_search)
    public TextView search_txt_search;
    @BindView(R.id.search_edt_search)
    public EditText search_edt_search;

    @BindView(R.id.pBar_loading)
    public ProgressBar pBar_loading;

    @BindView(R.id.rv_items)
    public RecyclerView rv_items;

    @Inject
    UsersMvpPresenter<UsersMvpView> mPresenter;
    @Inject
    DialogSort mDialogSort;

    private List<User> users;
    private UsersAdapter adapter;
    private boolean isOwner = false;
    private boolean isLoaded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);

        if (getIntent() != null && getIntent().hasExtra(Constants.INTENT_KEY_IS_OWNER)) {
            isOwner = getIntent().getBooleanExtra(Constants.INTENT_KEY_IS_OWNER, false);
        }
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */

        if (isOwner) {
            header_txt_title.setText(getString(R.string.title_owners));
        } else {
            header_txt_title.setText(getString(R.string.title_players));
        }

        rv_items.setHasFixedSize(true);
        rv_items.setLayoutManager(new LinearLayoutManager(this));
        rv_items.addItemDecoration(new SimpleDividerItemDecoration(this));

        mDialogSort.build();

        new MultiTextWatcher()
                .setCallback(this)
                .registerEditText(search_edt_search);
    }


    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.search_ln_sort)
    public void openSortDialog() {
        mDialogSort.show();
    }

    @OnClick(R.id.search_txt_search)
    public void openSearch() {
        search_txt_search.setVisibility(View.GONE);
        search_edt_search.setVisibility(View.VISIBLE);

        KeyboardUtils.showSoftInput(search_edt_search, this);
    }


    @Override
    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
        if (adapter != null) {
            adapter.getFilter().filter(s);
        }
    }

    @Override
    public void afterTextChanged(EditText editText, Editable editable) {

    }

    @Override
    public void onEditorAction(EditText editText, int action, Editable editable) {

    }


    @Override
    public void showLoading() {
        pBar_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pBar_loading.setVisibility(View.GONE);
    }


    @Override
    public void onGetUsers(List<User> users) {
        this.users = users;

        if (isOwner) {
            header_txt_title.setText(getString(R.string.title_owners) + " (" + users.size() + ")");
        } else {
            header_txt_title.setText(getString(R.string.title_players) + " (" + users.size() + ")");
        }

        adapter = new UsersAdapter(users, isOwner);
        adapter.registerListener(new UsersAdapter.OnUserClicked() {
            @Override
            public void setOnUserClicked(User user, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_KEY_USER_UID, user.uId);
                bundle.putBoolean(Constants.INTENT_KEY_IS_OWNER, isOwner);

                ActivityUtils.goTo(UsersActivity.this, UsersDetailsActivity.class, false, bundle);
            }
        });

        rv_items.setAdapter(adapter);
        rv_items.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearch(User user) {

    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(OnEventSort event) {
        if (TheActivityManager.getInstance().getCurrentActivity() instanceof UsersActivity) {

            if (adapter == null) {
                return;
            }

            if (event.getSortOption() == SortOption.RECENT) {
                adapter.setItems(ListUtils.sortUsersList(users, SortOption.RECENT));

            } else if (event.getSortOption() == SortOption.DEACTIVATED) {
                List<User> list = ListUtils.sortUsersList(users, SortOption.DEACTIVATED);
                adapter.setItems(list);

            } else if (event.getSortOption() == SortOption.NO_OF_BOOKINGS) {
                adapter.setItems(ListUtils.sortUsersList(users, SortOption.NO_OF_BOOKINGS));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventRefresh(OnEventRefresh event) {
        mPresenter.getUsers(isOwner);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        mPresenter.onResume();

        if (!isLoaded) {
            isLoaded = true;
            mPresenter.getUsers(isOwner);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        hideKeyboard();

        mPresenter.onPause();
    }

    @Override
    public void onBackPressed() {
        if (search_edt_search.getVisibility() == View.VISIBLE) {
            search_edt_search.setText("");
            search_edt_search.setVisibility(View.GONE);
            search_txt_search.setVisibility(View.VISIBLE);
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mDialogSort.onDestroy();
        mPresenter.onDetach();
        super.onDestroy();
    }
}
