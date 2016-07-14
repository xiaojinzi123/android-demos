package xiaojinzi.base.android.adapter.listView;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 通用的adapter
 *
 * @param <T>
 * @author Administrator
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    /**
     * 要显示的数据
     */
    protected List<T> data = null;

    /**
     * 上下文对象
     */
    protected Context context = null;

    /**
     * 条目对应的布局文件
     */
    protected int layout_id = 0;

    /**
     * 构造函数
     *
     * @param context   上下文
     * @param data      显示的数据
     * @param layout_id listview使用的条目的布局文件id
     */
    public CommonAdapter(Context context, List<T> data, int layout_id) {
        super();
        this.context = context;
        this.data = data;
        this.layout_id = layout_id;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int arg0) {
        return data.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        CommonViewHolder holder = CommonViewHolder.get(context, view, parent,
                layout_id, position);

        convert(holder, data.get(position), position);

        return holder.getConvertView();
    }

    /**
     * 需要用户自己实现的方法，这个方法，用来处理条目的显示效果
     *
     * @param h 里面有布局文件的控件
     * @param item   每个条目对应的数据对象
     */
    public abstract void convert(CommonViewHolder h, T item, int position);

}
