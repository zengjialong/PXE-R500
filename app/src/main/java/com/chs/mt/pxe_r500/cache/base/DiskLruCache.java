package com.chs.mt.pxe_r500.cache.base;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;

import com.chs.mt.pxe_r500.cache.util.CacheConfig;
import com.chs.mt.pxe_r500.cache.util.CacheUtils;
import com.chs.mt.pxe_r500.cache.util.LogUtil;

/**
 * Ӳ�̻���.
 *@Title:
 *@Description:
 *@Author:Justlcw
 *@Since:2014-3-5
 *@Version:
 */
public class DiskLruCache
{
    /** ��־TAG. */
    private static final String TAG = "DiskLruCache";
    /** �����ļ�������ʼ��. */
    private static final String CACHE_FILENAME_PREFIX = "cache_";
    /** һ������Ƴ���. */
    private static final int MAX_REMOVALS = 4;
    /** �����ļ�·��. */
    private final File mCacheDir;
    /** ��ǰ�����ļ�����. */
    private int cacheNumSize = 0;
    /** ��ǰ�����ļ��ֽڴ�С. */
    private int cacheByteSize = 0;
    /** ��������ļ�����    <Ĭ��:512��> */
    private static final int maxCacheNumSize = 512;
    /** ��������ֽ���     <Ĭ��:10MB> */
    private long maxCacheByteSize = 1024 * 1024 * 10;
    /** map<key, �����ļ�·��>. */
    protected final Map<String, String> mLinkedHashMap = 
            Collections.synchronizedMap(new LinkedHashMap<String, String>(32, 0.75f, true));

    /**
     * �����ļ���ȡ������.
     * �ļ����� {@link #CACHE_FILENAME_PREFIX} ��ͷ
     */
    private static final FilenameFilter CAHE_FILE_FILTER = new FilenameFilter()
    {
        @Override
        public boolean accept(File dir, String filename)
        {
            //�����Ƕ����<�����ļ�������ʼ��>��ʼ���ļ���,����Ϊ�ǻ����ļ�.
            return filename.startsWith(CACHE_FILENAME_PREFIX);
        }
    };

    /**
     * ��һ��Ӳ�̻���.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-5
     */
    public final static DiskLruCache openCache(Context context, String cacheName, long maxByteSize)
    {
        File cacheDir = CacheUtils.getEnabledCacheDir(context, cacheName);
        if (cacheDir.isDirectory() && cacheDir.canWrite() && CacheUtils.getUsableSpace(cacheDir) > maxByteSize)
        {
            return new DiskLruCache(cacheDir, maxByteSize);
        }
        return null;
    }

    /**
     * ���췽��.
     * @param cacheDir ����Ŀ¼.
     * @param maxByteSize ��󻺴��ֽ���.
     */
    protected DiskLruCache(File cacheDir, long maxByteSize)
    {
        mCacheDir = cacheDir;
        maxCacheByteSize = maxByteSize;
    }
    
    /**
     * ��һ���ֽ��������ļ�����ڻ�����.
     * @param key �ؼ���
     * @param inputStream �ֽ���
     * @return �����ļ�·��.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-5
     */
    public final String put(String key, InputStream inputStream)
    {
        BufferedInputStream bufferedInputStream = null;
        OutputStream bufferOps= null;
        try
        {
            bufferedInputStream = new BufferedInputStream(inputStream);
            String filePath = createFilePath(key);
            bufferOps = new BufferedOutputStream(new FileOutputStream(filePath));

            byte[] b = new byte[CacheConfig.IO_BUFFER_SIZE];
            int count;
            while ((count = bufferedInputStream.read(b)) > 0)
            {
                bufferOps.write(b, 0, count);
            }
            bufferOps.flush();
            LogUtil.d(TAG, "put success : " + key);
            onPutSuccess(key, filePath);
            flushCache();
            return filePath;
        }
        catch (IOException e)
        {
            LogUtil.d(TAG, "store failed to store: " + key, e);
        }
        finally
        {
            try
            {
                if(bufferOps != null)
                {
                    bufferOps.close();
                }
                if(bufferedInputStream != null)
                {
                    bufferedInputStream.close();
                }
                if(inputStream != null)
                {
                    inputStream.close();
                }
            }
            catch (IOException e)
            {
                LogUtil.d(TAG, "close stream error : "+e.getMessage());
            }
        }
        return "";
    }
    
    /**
     * ��һ���ֽ��������ļ�����ڻ�����.
     * @param key �ؼ���
     * @param data �ֽ�����
     * @return �����ļ�·��.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-5
     */
    public final String put(String key, byte[] data)
    {
        if (data != null)
        {
            OutputStream bufferOps= null;
            try
            {
                String filePath = createFilePath(key);
                bufferOps = new BufferedOutputStream(new FileOutputStream(filePath));
                bufferOps.write(data, 0, data.length);
                bufferOps.flush();
                LogUtil.d(TAG, "put success : " + key);
                onPutSuccess(key, filePath);
                flushCache();
                return filePath;
            }
            catch (IOException e)
            {
                LogUtil.d(TAG, "put fail : " + key, e);
            }
            finally
            {
                try
                {
                    if(bufferOps != null)
                    {
                        bufferOps.close();
                    }
                }
                catch (IOException e)
                {
                    LogUtil.d(TAG, "close outputStream error : "+e.getMessage());
                }
            }
        }
        return "";
    }
    
    /**
     * ����һ���ļ�.
     * @param key �ؼ���
     * @param filePath �ļ�·��
     */
    protected final void onPutSuccess(String key, String filePath)
    {
        mLinkedHashMap.put(key, filePath);
        cacheNumSize = mLinkedHashMap.size();
        cacheByteSize += new File(filePath).length();
    }
    

    /**
     * �����С�������.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-5
     */
    protected final void flushCache()
    {
        Entry<String, String> eldestEntry;
        File eldestFile;
        long eldestFileSize;
        int count = 0;
        //������󻺴��ļ�����   or  �������ռ��С    �Ƴ������õ��ļ�,����һ�����ֻ���Ƴ�4��.
        while (count < MAX_REMOVALS && (cacheNumSize > maxCacheNumSize || cacheByteSize > maxCacheByteSize))
        {
            eldestEntry = mLinkedHashMap.entrySet().iterator().next();
            eldestFile = new File(eldestEntry.getValue());
            eldestFileSize = eldestFile.length();
            mLinkedHashMap.remove(eldestEntry.getKey());
            eldestFile.delete();
            cacheNumSize = mLinkedHashMap.size();
            cacheByteSize -= eldestFileSize;
            count++;
            LogUtil.d(TAG, "flushCache - Removed :" + eldestFile.getAbsolutePath()+ ", " + eldestFileSize);
        }
    }
    
    /**
     * ����key���ػ����ļ�.
     * @param key key
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public final File get(String key)
    {
        if(containsKey(key))
        {
            final File file = new File(createFilePath(key));
            if(file.exists())
            {
                return file;
            }
        }
        return null;
    }

    /**
     * �������Ƿ���key�����ݱ���
     * @param key �ؼ���
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-5
     */
    public final boolean containsKey(String key)
    {
        //�Ƿ���map��.
        if (mLinkedHashMap.containsKey(key))
        {
            return true;
        }
        //�������map��,���ǻ�����ļ�����.
        final String existingFile = createFilePath(key);
        if (new File(existingFile).exists())
        {
            //�������,����map����ֱ����ȡ.
            onPutSuccess(key, existingFile);
            return true;
        }
        return false;
    }

    /**
     * �������.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-5
     */
    public final void clearCache()
    {
        final File[] files = mCacheDir.listFiles(CAHE_FILE_FILTER);
        for (int i = 0; i < files.length; i++)
        {
            files[i].delete();
        }
    }

    /**
     * ����key��û����ļ�·��.
     * @param key �ؼ���
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-5
     */
    public final String createFilePath(String key)
    {
        return new StringBuffer().append(mCacheDir.getAbsolutePath())
                .append(File.separator).append(CACHE_FILENAME_PREFIX)
                .append(key.hashCode()).toString();
    }
}
