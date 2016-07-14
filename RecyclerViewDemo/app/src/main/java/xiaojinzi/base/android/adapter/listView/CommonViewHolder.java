package xiaojinzi.base.android.adapter.listView;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CommonViewHolder {

    /**
     * 用来保存条目视图里面所有的控件
     */
    private SparseArray<View> mViews = null;

    /**
     * 这个viewHolder对应的条目视图
     */
    private View mConvertView = null;

    /**
     * 要使用的布局文件id
     */
    private int layoutId;

    /**
     * 构造函数私有化，不让创建
     *
     * @param context
     * @param parent
     * @param layoutId
     * @param position
     */
    private CommonViewHolder(Context context, ViewGroup parent, int layoutId,
                             int position) {

        this.layoutId = layoutId;

        // 初始化类似map集合的SparseArray集合
        this.mViews = new SparseArray<View>();

        // 对布局文件充气成View对象
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);

        // 把自己挂载到布局文件对应的视图上面
        mConvertView.setTag(this);

    }

    /**
     * 获取viewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static CommonViewHolder get(Context context, View convertView,
                                       ViewGroup parent, int layoutId, int position) {

        if (convertView == null) {
            return new CommonViewHolder(context, parent, layoutId, position);
        }

        CommonViewHolder commonViewHolder = (CommonViewHolder) convertView.getTag();

        if (commonViewHolder.getLayoutId() == layoutId) {
            return commonViewHolder;
        }

        return new CommonViewHolder(context, parent, layoutId, position);
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
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }

        // 返回控件对象
        return (T) view;
    }

    /**
     * 获取布局文件对应的view对象
     *
     * @return
     */
    public View getConvertView() {
        return mConvertView;
    }

    public int getLayoutId() {
        return layoutId;
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
