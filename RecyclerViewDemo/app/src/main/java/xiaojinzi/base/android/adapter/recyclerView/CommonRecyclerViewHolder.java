package xiaojinzi.base.android.adapter.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by cxj on 2016/4/28.
 */
public class CommonRecyclerViewHolder extends RecyclerView.ViewHolder {

    /**
     * 用来保存条目视图里面所有的控件
     */
    private SparseArray<View> mViews = null;

    /**
     * 构造函数
     *
     * @param itemView
     */
    public CommonRecyclerViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<View>();
    }

    /**
     * 根据控件id获取控件对象
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getView(int viewId) {

        // 从集合中根据这个id获取view视图对象
        View view = mViews.get(viewId);

        // 如果为空，说明是第一次获取，里面没有，那就在布局文件中找到这个控件，并且存进集合中
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }

        // 返回控件对象
        return (T) view;
    }

    /**
     * 为TextView设置文本,按钮也可以用这个方法,button是textView的子类
     *
     * @param textViewId
     * @param content
     */
    public void setText(int textViewId, String content) {
        ((TextView) getView(textViewId)).setText(content);
    }

    /**
     * 为ImageView设置图片
     *
     * @param iv
     * @param imageId
     */
    public void setImage(ImageView iv, int imageId) {
        iv.setImageResource(imageId);
    }


}
