package com.xsh.android.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xsh.android.R;
import com.xsh.android.utils.UIUtils;

/**
 * Created by Administrator on 2016/6/18.
 *
 * 现在流行Volley + OkHttp 请改
 *
 * 把解析做好
 *
 */
public abstract class LoadingPage extends FrameLayout {

    private AsyncHttpClient client = new AsyncHttpClient();

    private static int PAGE_LOADING_STATE = 1;
    private static int PAGE_ERROR_STATE = 2;
    private static int PAGE_EMPTY_STATE = 3;
    private static int PAGE_SUCCESS_STATE = 4;

    private int PAGE_CURRENT_STATE = 1;

    private LayoutParams layoutParams;

    private Context context;

    private View errorView;
    private View emptyView;
    private View successView;
    private View loadingView;
    private ViewState viewState;

    public LoadingPage(Context context) {
        this(context, null);
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (errorView == null) {
            errorView = UIUtils.getXmlView(R.layout.page_error);
            addView(errorView, layoutParams);
        }
        if (emptyView == null) {
            emptyView = UIUtils.getXmlView(R.layout.page_empty);
            addView(emptyView, layoutParams);
        }
        if (loadingView == null) {
            loadingView = UIUtils.getXmlView(R.layout.page_loading);
            addView(loadingView, layoutParams);
        }

        showSafePage();
    }

    protected void showSafePage() {
        UIUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                showPage();
            }
        });
    }

    private void showPage() {
        errorView.setVisibility(PAGE_CURRENT_STATE == PAGE_ERROR_STATE ? View.VISIBLE : View.GONE);
        emptyView.setVisibility(PAGE_CURRENT_STATE == PAGE_EMPTY_STATE ? View.VISIBLE : View.GONE);
        loadingView.setVisibility(PAGE_CURRENT_STATE == PAGE_LOADING_STATE ? View.VISIBLE : View.GONE);

        if (successView == null) {
            successView = View.inflate(context, LayoutId(), null);
            addView(successView, layoutParams);
        }
        successView.setVisibility(PAGE_CURRENT_STATE == PAGE_SUCCESS_STATE ? View.VISIBLE : View.GONE);
    }

    protected abstract int LayoutId();


    public void show() {
        if(PAGE_CURRENT_STATE != PAGE_LOADING_STATE){
            PAGE_CURRENT_STATE = PAGE_LOADING_STATE;
        }
        String url = Url();
        if (TextUtils.isEmpty(url)) {
            viewState = ViewState.EMPTY;
            viewState.setContent("");
            loadPage();
        } else {
            client.get(url, getParams(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String content) {
                    if (TextUtils.isEmpty(content)) {
                        viewState = ViewState.EMPTY;
                        viewState.setContent("");
                    } else {
                        viewState = ViewState.SUCCESS;
                        viewState.setContent(content);
                    }
                    loadPage();
                }

                @Override
                public void onFailure(Throwable error, String content) {
                    viewState = ViewState.ERROR;
                    super.onFailure(error, content);
                    loadPage();
                }
            });
        }

    }

    private void loadPage() {
        switch (viewState) {
            case EMPTY:
                PAGE_CURRENT_STATE = PAGE_EMPTY_STATE;
                break;

            case LOADING:
                PAGE_CURRENT_STATE = PAGE_LOADING_STATE;
                break;

            case SUCCESS:
                PAGE_CURRENT_STATE = PAGE_SUCCESS_STATE;
                break;

            case ERROR:
                PAGE_CURRENT_STATE = PAGE_ERROR_STATE;
                break;
        }
        showSafePage();

        if(PAGE_CURRENT_STATE == PAGE_ERROR_STATE){
            SUCCESS(viewState, successView);
        }
    }

    protected abstract RequestParams getParams();

    protected abstract String Url();

    protected abstract void SUCCESS(ViewState viewState, View successView);

    public enum ViewState {
        LOADING(1), ERROR(2), EMPTY(3), SUCCESS(4);
        int state;
        String content;

        ViewState(int state) {
            this.state = state;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }

}
