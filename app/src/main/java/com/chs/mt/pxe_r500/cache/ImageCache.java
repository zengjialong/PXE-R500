package com.chs.mt.pxe_r500.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.chs.mt.pxe_r500.cache.base.DiskLruCache;
import com.chs.mt.pxe_r500.cache.base.ImageDiskLruCache;
import com.chs.mt.pxe_r500.cache.util.CacheConfig;
import com.chs.mt.pxe_r500.cache.util.CacheUtils;
import com.chs.mt.pxe_r500.cache.util.LogUtil;

/**
 * ͼƬ����.
 *@Title:
 *@Description:
 *@Author:Justlcw
 *@Since:2014-3-6
 *@Version:
 */
public class ImageCache
{
    /** ��־TAG. **/
    private static final String TAG = "ImageCache";

    /** ͼƬӲ�̻���. */
    private ImageDiskLruCache mImageDiskCache;
    
    /** ͼƬ�ڴ滺��. */
    private LruCache<String, Bitmap> mMemoryCache;
    
    /** ͼƬ����ʵ��. */
    private static ImageCache mInstance;
    
    /**
     * ���һ��ͼƬ����ʵ��.
     * @param context context
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public static ImageCache getInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = new ImageCache(context); 
        }
        return mInstance;
    }
    
    /**
     * ���췽��.
     * @param context context
     */
    private ImageCache(Context context)
    {
        init(context);
    }

    /**
     * ��ʼ��<Ӳ�̻���>��<�ڴ滺��>
     * @param context context
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    private void init(Context context)
    {
        //����Ӳ�̻���
        mImageDiskCache = 
                ImageDiskLruCache.openImageCache(context, CacheConfig.Image.DISK_CACHE_NAME, CacheConfig.Image.DISK_CACHE_MAX_SIZE);

        // �����ڴ滺��.
        final int imageMemorySize = CacheUtils.getMemorySize(context, CacheConfig.Image.MEMORY_SHRINK_FACTOR);
        LogUtil.d(TAG, "memory size : " + imageMemorySize);
        mMemoryCache = new LruCache<String, Bitmap>(imageMemorySize)
                {
                    @Override
                    protected int sizeOf(String key, Bitmap bitmap)
                    {
                        return CacheUtils.getBitmapSize(bitmap);
                    }

                    @Override
                    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue)
                    {
                    }
                };
    }

    /**
     * ���ͼƬ������
     * @param key key
     * @param bitmap ͼƬ.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public void addBitmapToCache(String key, Bitmap bitmap)
    {
        //���key����bitmap��һ��Ϊnull,ֱ�ӷ���.
        if (key == null || bitmap == null)
        {
            return;
        }
        // ��ӵ��ڴ滺��.
        if (mMemoryCache != null && mMemoryCache.get(key) == null)
        {
            mMemoryCache.put(key, bitmap);
        }
        // ��ӵ�Ӳ�̻���.
        if (mImageDiskCache != null && !mImageDiskCache.containsKey(key))
        {
            mImageDiskCache.putImage(key, bitmap);
        }
    }

    /**
     * �Ƿ����.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public boolean exists(String key)
    {
        if (mImageDiskCache != null && mImageDiskCache.containsKey(key))
        {
            return true;
        }
        return false;
    }

    /**
     * ���ڴ滺����ȥͼƬ.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public Bitmap getBitmapFromMemCache(String key)
    {
        if (mMemoryCache != null)
        {
            final Bitmap bitmap = mMemoryCache.get(key);
            if (bitmap != null)
            {
                LogUtil.d(TAG, "memory cache hit : " + key);
                return bitmap;
            }
        }
        return null;
    }

    /**
     * ��Ӳ�̻�����ȡͼƬ.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public Bitmap getBitmapFromDiskCache(String key)
    {
        if (mImageDiskCache != null)
        {
            return mImageDiskCache.getImage(key);
        }
        return null;
    }

    /**
     * ����ڴ滺��.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public void clearMemoryCache()
    {
        mMemoryCache.evictAll();
    }

    /**
     * �õ�Ӳ�̻���.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public DiskLruCache getDiskCache()
    {
        return mImageDiskCache;
    }
}
