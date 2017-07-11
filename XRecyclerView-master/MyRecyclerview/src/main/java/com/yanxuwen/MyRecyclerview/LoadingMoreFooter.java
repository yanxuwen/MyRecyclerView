package com.yanxuwen.MyRecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yanxuwen.DensityUtil;
import com.yanxuwen.MyRecyclerview.progressindicator.AVLoadingIndicatorView;
import com.yanxuwen.MyRecyclerview.progressindicator.materialprogressview.MaterialProgressView;

public class LoadingMoreFooter extends LinearLayout {

    private SimpleViewSwithcer progressCon;
    private Context mContext;
    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;
    private TextView mText;
    //没有加载显示的问题
    private String state_nomore;
    private int text_color;
	public LoadingMoreFooter(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public LoadingMoreFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
    public void initView(Context context ){
        mContext = context;
        setGravity(Gravity.CENTER);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        progressCon = new SimpleViewSwithcer(context);
        progressCon.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(this.getContext());
        progressView.setIndicatorColor(0xffB5B5B5);
        progressView.setIndicatorId(ProgressStyle.BallSpinFadeLoader);
        progressCon.setView(progressView);

        addView(progressCon);
        mText = new TextView(context);
        int padding=DensityUtil.dip2px(context,15);
        mText.setPadding(0,padding,padding,padding);
        mText.setText("正在加载...");

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins( (int)getResources().getDimension(R.dimen.textandiconmargin),0,0,0 );

        mText.setLayoutParams(layoutParams);
        addView(mText);
    }
    MaterialProgressView materialProgressView;
    public MaterialProgressView getMaterialProgressView(){
        return materialProgressView;
    }
    public void setProgressStyle(int style) {
        if(style == ProgressStyle.SysProgress){
            progressCon.setView(new ProgressBar(mContext, null, android.R.attr.progressBarStyle));
        }else{
            if(style == ProgressStyle.MaterialDesign){
                 materialProgressView = new MaterialProgressView(this.getContext());
                int color = Color.parseColor("#607D8B");
                materialProgressView.setColorSchemeColors(new int[]{color});
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, DensityUtil.dip2px(this.getContext(),5),0, DensityUtil.dip2px(this.getContext(),3));
                progressCon.setView(materialProgressView);
                progressCon.setLayoutParams(lp);
            }else{
                AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(this.getContext());
                progressView.setIndicatorColor(0xffB5B5B5);
                progressView.setIndicatorId(style);
                progressCon.setView(progressView);
            }
        }
    }
    public void setStateTextPadding(int paddingleft,int paddingtop,int paddingright,int paddingbottom){
        if(mText!=null)
        mText.setPadding(paddingleft,paddingtop,paddingright,paddingbottom);
    }
    public void setStateNoMoreText(String state_nomore){
        this.state_nomore=state_nomore;
    }
    public void setStateNoMoreText(String state_nomore,int color){
        this.state_nomore=state_nomore;
        this.text_color=color;
    }
    public void  setState(int state) {
        switch(state) {
            case STATE_LOADING:
                mText.setText(mContext.getText(R.string.listview_loading));
                if(materialProgressView!=null){
                    mText.setVisibility(View.GONE);
                    materialProgressView.setVisibility(View.VISIBLE);
                }
                progressCon.setVisibility(View.VISIBLE);
                this.setVisibility(View.VISIBLE);
                    break;
            case STATE_COMPLETE:
                mText.setText(mContext.getText(R.string.listview_loading));
                this.setVisibility(View.GONE);
                if(materialProgressView!=null){
                    materialProgressView.setVisibility(View.GONE);
                    mText.setVisibility(View.VISIBLE);
                }
                break;
            case STATE_NOMORE:
                String nomore=state_nomore;
                if(nomore==null){
                    nomore=mContext.getText(R.string.nomore_loading)+"";
                }
                if(text_color==0){
                    mText.setTextColor(Color.parseColor("#888888"));
                }else{
                    mText.setTextColor(mContext.getResources().getColor(text_color));
                }
                mText.setText(nomore);
                progressCon.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                if(materialProgressView!=null){
                    materialProgressView.setVisibility(View.GONE);
                    mText.setVisibility(View.VISIBLE);
                }
                break;
        }

    }
}
