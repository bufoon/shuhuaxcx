package com.rttx.commons.config.prop;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/2/9 18:07
 * @Desc: as follows.
 */
public class AsyncProperties {

    /**
     * 线程池维护线程的最少数量
     */
    private int corePoolSize = 5;

    /**
     * 线程池维护线程的最大数量
     */
    private int maxPoolSize = Integer.MAX_VALUE;

    /**
     * 允许的空闲时间
     */
    private int keepAliveSeconds = 60;

    /**
     * 缓存队列数
     */
    private int queueCapacity = Integer.MAX_VALUE;

    /**
     * 允许核心线程超时
     */
    private boolean allowCoreThreadTimeOut = false;

    /**
     * 线程名前缀
     */
    private String threadNamePrefix = "RTTXExecutor-";

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public boolean isAllowCoreThreadTimeOut() {
        return allowCoreThreadTimeOut;
    }

    public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }
}
