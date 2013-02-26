package live.hz.lanchat.util;

import android.app.Activity;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: rain
 * Date: 2/24/13
 * Time: 5:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class Scale {

    private Activity ctx;

    public Scale(){}

    public Scale(Activity ctx) {
        this.ctx = ctx;
    }

    public boolean setHeightOnScreen(Activity ctx,View view,float decimal){
        int scaleHeight =ctx.getWindow().getAttributes().height;
        view.getLayoutParams().height =scaleHeight;
        return true;
    }
}
