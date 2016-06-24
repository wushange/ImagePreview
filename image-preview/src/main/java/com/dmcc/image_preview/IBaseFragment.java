package com.dmcc.image_preview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

public interface IBaseFragment {

    public int bindLayout();
    public View bindView();
    public void initParms(Bundle parms);
    public void initView(final View view);
    public void doBusiness(Context mContext);
    public void lazyInitBusiness(Context mContext);

}
