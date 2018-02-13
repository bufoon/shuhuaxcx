package com.rttx.zookeeper.support;

import com.rttx.commons.utils.StringUtils;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/16 13:36
 * @Desc: as follows.
 * 泛型节点值改变监听器
 */
public class NodeCacheGenericListener<V> implements NodeCacheListener {

    private V defaultValue;// 默认值
    private NodeCache nodeCache; // nodeCache
    private AtomicReference<V> v; // 原子引用

    public NodeCacheGenericListener(AtomicReference<V> v,  V defaultValue, NodeCache nodeCache){
        this.v = v;
        this.defaultValue = defaultValue;
        this.nodeCache = nodeCache;
    }

    @Override
    public void nodeChanged() throws Exception {
        V data = (V) new String(nodeCache.getCurrentData().getData(), Charset.defaultCharset());
        if (StringUtils.isEmpty(data)){
            data = defaultValue;
        }
        v.set(data);
    }
}
