package com.suwec.passwordexample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suwec.framework.utils.AndroidUtil;

public class PasswordInputTextView extends LinearLayout {
    private Context context;
    private TextView[] textViews;
    private int lastIndex = 0; //记录上一个显示的点
    private Drawable dot; //点图片
    private int lenght = 6; //默认长度
    private OnPwdCompleteListener listener;
    private StringBuffer password = new StringBuffer();

    public PasswordInputTextView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public PasswordInputTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public void setOnPwdCompleteListener(OnPwdCompleteListener listener){
        this.listener = listener;
    }

    public void setMaxLenght(int lenght) throws Exception {
        if (lenght <= 2)
            throw new Exception("参数错误！");
        this.lenght = lenght;
        initView();
    }

    //初始化view
    private void initView() {
        removeAllViews();
        dot =getResources().getDrawable(R.drawable.suwec_pass_dot);
        dot.setBounds(0, 0, dot.getMinimumWidth(), dot.getMinimumHeight());
        LinearLayout view = new LinearLayout(context);
        final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        int height = AndroidUtil.dip2px(context, 50);
        params.height = height;
        if (lenght > 6)
            params.height = height - (lenght-6)*AndroidUtil.dip2px(context,3);
        params.gravity = Gravity.CENTER;
        view.setOrientation(HORIZONTAL);
        view.setGravity(Gravity.CENTER);
        view.setLayoutParams(params);
        textViews = new TextView[lenght];
        initTextViewLayout(view);
        addView(view);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    private void initTextViewLayout(LinearLayout view) {
        int width = AndroidUtil.dip2px(context, 45)*6/lenght;
        LayoutParams params = new LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        for (int i = 0;i < lenght;i++){
            textViews[i] = new TextView(context);
            textViews[i].setGravity(Gravity.CENTER);
            textViews[i].setPadding(AndroidUtil.dip2px(context,18)*6/lenght,0,0,0);
            textViews[i].setLayoutParams(params);
            textViews[i].setBackgroundResource(R.drawable.suwec_pass_input_center_background);
            view.addView(textViews[i]);
        }
        textViews[0].setBackgroundResource(R.drawable.suwec_pass_input_left_background);
        textViews[lenght-1].setBackgroundResource(R.drawable.suwec_pass_input_right_background);
    }

    public void setShowIndex(int index){
        if (index == -1) {
            lastIndex = 0;
            clearPasswordDot();
            return;
        }
        if (index < lenght) {
            if (index < lastIndex)
                clearPasswordDot();
            textViews[index].setCompoundDrawables(dot, null, null, null);
            lastIndex = index;
            if (index == lenght-1 && listener != null)
                listener.onComplete();
        }
    }

    private void clearPasswordDot() {
        for (int i = lastIndex;i < lenght;i++){
            textViews[i].setCompoundDrawables(null, null, null, null);
        }
    }

    public interface OnPwdCompleteListener {
        void onComplete();
    }
}
