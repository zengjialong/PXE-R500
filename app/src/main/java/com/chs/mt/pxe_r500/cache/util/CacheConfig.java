package com.chs.mt.pxe_r500.cache.util;

/**
 * ��������
 *@Title:
 *@Description:
 *@Author:Justlcw
 *@Since:2014-3-7
 *@Version:
 */
public interface CacheConfig
{
    /** �����ֽ���һ�ζ����С ( google �Ƽ� 8192 ). **/
    int IO_BUFFER_SIZE = 8 * 1024;
    
    /** ������Ŀ¼. **/
    String DISK_CACHE_NAME = "/CacheDir";
    
    /**
     * ͼƬ���������.
     *@Title:
     *@Description:
     *@Author:Justlcw
     *@Since:2014-3-7
     *@Version:
     */
    interface Image
    {
        /** ͼƬ�����Ŀ¼. **/
        String DISK_CACHE_NAME = "/images";
        
        /** ͼƬ���������С. (20MB)  **/
        int DISK_CACHE_MAX_SIZE = 1024 * 1024 * 20;
        
        /** ͼƬ������ڴ��С. (1/8�ڴ��С)  **/
        int MEMORY_SHRINK_FACTOR = 8;
    }
}
