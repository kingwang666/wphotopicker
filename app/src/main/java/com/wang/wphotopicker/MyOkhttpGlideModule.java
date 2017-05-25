package com.wang.wphotopicker;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created on 2016/2/25.
 * Author: wang
 */
public class MyOkhttpGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "567", DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE));
//        builder.setMemoryCache(new LruResourceCache(50 * 1024));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }

}
