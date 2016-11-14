package com.example.administrator.mytestdemo.popwindow.pop;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.administrator.mytestdemo.R;


/**
 * PopupWindow工具类
 */
public class PopupWindowUtils {


    /**
     * 从anchor的底部弹出popwindow
     *
     * @param anchor
     * @param view   popwindow中的内容
     * @return
     */
    public static PopupWindow createPopWindowFromBottom(View anchor, Object view) {
        return new PopupWindowUtils.Builder(anchor, view)
                .setAnimationStyle(R.style.AnimationFromBottom)
                .setWidth(-1)
                .setGravity(Gravity.BOTTOM)
                .setBgAlpha(0.4f)
                .create();
    }

    public static class Builder {
        private View anchor;
        private Object view;
        private int gravity = Gravity.CENTER;
        private boolean outside_touchable = true;
        private boolean focusable = true;
        private boolean showAtCurrentPosition = false;
        private int width = -2;    //宽度默认包裹内容
        private int height = -2;   //高度默认包裹内容
        private int offset_x = 0;
        private int offset_y = 0;
        private float bgAlpha = 0.0f;  //背景透明度
        private int anim_id = -1;


        public Builder(View anchor, Object view) {
            this.anchor = anchor;
            this.view = view;
        }


        /**
         * 设置弹出位置
         *
         * @param gravity
         * @return
         */
        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * 是否在当前位置显示
         *
         * @param showAtCurrentPosition
         * @return
         */
        public Builder setShowAtCurrentPosition(boolean showAtCurrentPosition) {
            this.showAtCurrentPosition = showAtCurrentPosition;
            return this;
        }

        /**
         * 设置宽度，默认是包裹内容
         *
         * @param width
         * @return
         */
        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }


        /**
         * 设置高度，默认是包裹内容
         *
         * @param height
         * @return
         */
        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        /**
         * 水平偏移量
         *
         * @param offset_x
         * @return
         */
        public Builder setOffsetX(int offset_x) {
            this.offset_x = offset_x;
            return this;
        }

        /**
         * 垂直偏移量
         *
         * @param offset_y
         * @return
         */
        public Builder setOffsetY(int offset_y) {
            this.offset_y = offset_y;
            return this;
        }

        /**
         * 外部是否可点击
         *
         * @param outside_touchable
         * @return
         */
        public Builder setOutsideTouchable(boolean outside_touchable) {
            this.outside_touchable = outside_touchable;
            return this;
        }

        public Builder setFocusable(boolean focusable) {
            this.focusable = focusable;
            return this;
        }

        /**
         * 设置动画
         *
         * @param anim_id
         * @return
         */
        public Builder setAnimationStyle(int anim_id) {
            this.anim_id = anim_id;
            return this;
        }

        /**
         * 设置弹出背景透明度，0.0f完全透明，1.0f完全不透明
         *
         * @param bgAlpha
         * @return
         */
        public Builder setBgAlpha(float bgAlpha) {
            this.bgAlpha = bgAlpha;
            return this;
        }


        public PopupWindow createDefault(final View anchor,
                                         Object view,
                                         int gravity,
                                         boolean outside_touchable,
                                         boolean focusable,
                                         boolean showAtCurrentPosition,
                                         int width, int height,
                                         int offset_x,
                                         int offset_y,
                                         int anim_id) {
            View pop_view = null;
            if (view instanceof Integer) {
                pop_view = LayoutInflater.from(anchor.getContext()).inflate((Integer) view, null, false);
            }
            if (view instanceof View) {
                pop_view = (View) view;
            }

            final PopupWindow popWindow = new PopupWindow(pop_view, width, height, focusable);
            popWindow.setOutsideTouchable(outside_touchable);
            popWindow.update();
            if (outside_touchable) {
                ColorDrawable dw = new ColorDrawable(0x00000000);
                popWindow.setBackgroundDrawable(dw);
            }
            if (anim_id != -1) {
                popWindow.setAnimationStyle(anim_id);
            }

            if (bgAlpha > 0.0f && bgAlpha <= 1.0f) {
                WindowManager.LayoutParams params = ((Activity) popWindow.getContentView().getContext()).getWindow().getAttributes();
                params.alpha = 0.4f;
                ((Activity) popWindow.getContentView().getContext()).getWindow().setAttributes(params);
            }


            popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (bgAlpha > 0.0f && bgAlpha <= 1.0f) {
                        closePopupWindow(popWindow);
                    }
                }
            });

            if (showAtCurrentPosition) {
                popWindow.showAsDropDown(anchor, offset_x, offset_y);
            } else {
                popWindow.showAtLocation(anchor, gravity, offset_x, offset_y);
            }
            return popWindow;
        }


        public PopupWindow create() {
            return createDefault(anchor, view, gravity, outside_touchable, focusable, showAtCurrentPosition, width, height, offset_x, offset_y, anim_id);
        }

        private void closePopupWindow(PopupWindow popWindow) {
            if (popWindow != null) {
                WindowManager.LayoutParams params = ((Activity) popWindow.getContentView().getContext()).getWindow().getAttributes();
                params.alpha = 1f;
                ((Activity) popWindow.getContentView().getContext()).getWindow().setAttributes(params);
            }
        }

    }
}


