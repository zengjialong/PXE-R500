package com.chs.mt.pxe_r500.cache.loader;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.chs.mt.pxe_r500.cache.base.DiskLruCache;
import com.chs.mt.pxe_r500.cache.util.LogUtil;

/**
 * ͼƬߢȡ��.
 *@Title:
 *@Description:
 *@Author:Justlcw
 *@Since:2014-3-6
 *@Version:
 */
public class ImageFetcher extends ImageWorker
{
    /** ��־TAG. */
    private static final String TAG = "ImageFetcher";

    /**
     * ���췽��.
     * @param context ������
     */
    public ImageFetcher(Context context)
    {
        super(context);
    }

    @Override
    protected Bitmap processBitmap(String url)
    {
        //����������ͼƬ.
        final File file = downloadBitmap(url);
        if (file != null)
        {
            // ������سɹ�,ת����Bitmap����.
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        return null;
    }

    /**
     * ����������һ��ͼƬ.
     * @param netUrl ͼƬ�����ַ.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    private File downloadBitmap(String netUrl)
    {
        DiskLruCache diskCache = getImageCache().getDiskCache();
        LogUtil.d(TAG, "load : " + netUrl);
        HttpURLConnection urlConnection = null;
        try
        {
            final URL url = new URL(netUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            //�ŵ�ͼƬ��Ӳ�̻�����.
            String filePath = diskCache.put(netUrl, urlConnection.getInputStream());
            if(!TextUtils.isEmpty(filePath))
            {
                return new File(filePath);
            }
        }
        catch (IOException e)
        {
            LogUtil.e(TAG, "load error :" + netUrl, e);
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
