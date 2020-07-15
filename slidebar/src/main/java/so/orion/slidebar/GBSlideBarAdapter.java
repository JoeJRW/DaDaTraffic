/*
*author: 李俊
*create: time: 2020-07-11
*update: time:
*/

package so.orion.slidebar;

import android.graphics.drawable.StateListDrawable;

public interface GBSlideBarAdapter {

    int getCount();
    String getText(int position);
    StateListDrawable getItem(int position);
    int getTextColor(int position);
}
