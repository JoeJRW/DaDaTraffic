package so.orion.slidebar;

import android.graphics.drawable.StateListDrawable;

public interface GBSlideBarAdapter {

    int getCount();
    String getText(int position);
    StateListDrawable getItem(int position);
    int getTextColor(int position);
}
