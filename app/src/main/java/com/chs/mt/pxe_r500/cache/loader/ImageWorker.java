package com.chs.mt.pxe_r500.cache.loader;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.ImageView;

import com.chs.mt.pxe_r500.operation.ImageUtil;
import com.chs.mt.pxe_r500.cache.ImageCache;
import com.chs.mt.pxe_r500.cache.util.LogUtil;

/**
 * ͼƬ������.<P>
 * ͼƬ�ļ��ع��̣�1.�������ͼƬ������ȡ;2.���ȡ����,��������л�ȡ.<P>
 * ͬʱ,���ǿ���ֹͣ��̨��ͼƬ��ȡ�߳�.
 *@Title:
 *@Description:
 *@Author:Justlcw
 *@Since:2014-3-6
 *@Version:
 */
public abstract class ImageWorker
{
	public static final int TypeCircular=1;
	public static final int TypeRectangle=0;
    /** ��־TAG. */
    private static final String TAG = "ImageWorker";
    /** ͼƬ����Ч��ʱ��<����ʱ��>. */
    private static final int FADE_IN_TIME = 200;
    /** ͼƬ����. */
    private ImageCache mImageCache;
    /** �Ƿ�Ҫ�Ƴ�����<��:Ӧ�ó��������ʱ��,�����߳̾�Ӧ��ֹͣ>. */
    private boolean mExitTasksEarly = false;
    /** ������Context. */
    protected Context mContext;
    /** ���ڼ���ʱ���ͼƬ. */
    private SparseArray<Bitmap> loadingImageMap;

    /**
     * ���췽��.
     * @param context context
     */
    protected ImageWorker(Context context)
    {
        mContext = context;
        loadingImageMap = new SparseArray<Bitmap>();
    }

    /**
     * ����url����һ��ͼƬ��ImageView��,δ���سɹ���ʱ����ʾ����ͼƬ<loadingImageId>
     * 
     * @param url ͼƬ�����ַ.
     * @param imageView ͼƬView
     * @param loadingImageId ����ͼƬ��ԴID
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public void loadImage(String url, ImageView imageView, int loadingImageId,int type){
        //���ͼƬ�����ַΪ��,ֱ�ӷ���.
        if(TextUtils.isEmpty(url)){
            return;
        }
        Bitmap bitmap = null;
        if (mImageCache != null){
            //�ȴ��ڴ滺����ȡ.
            bitmap = mImageCache.getBitmapFromMemCache(url);
        }
        
        if (bitmap != null){
            //���ȡ����,ֱ����ʾ.
        	if(type==TypeCircular){
            	imageView.setImageBitmap(ImageUtil.SetRoundBitmap(bitmap));
        	}else{
        		imageView.setImageBitmap(bitmap);
        	}
        	
            
        }else if (cancelPotentialWork(url, imageView)){
            //���û��ȡ��,ȡ��֮ǰ��ͬ�ļ����߳�,ȡ���ɹ�,������һ���µļ����߳�.
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            AsyncDrawable asyncDrawable=null;
            if(type==TypeCircular){
            	asyncDrawable = 
                        new AsyncDrawable(mContext.getResources(), ImageUtil.SetRoundBitmap(getLoadingImage(loadingImageId)), task);
        	}else{
        		asyncDrawable = 
                        new AsyncDrawable(mContext.getResources(), getLoadingImage(loadingImageId), task);
        	}
            
            imageView.setImageDrawable(asyncDrawable);
            task.execute(url);
        }
    }

    /**
     * ������ԴID��ȡ�����е�ͼƬ.
     * @param resId ��ԴID.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    private Bitmap getLoadingImage(int resId)
    {
        //�ȴ����м����е�ͼƬ�б���ȡ,���ȡ��,ֱ�ӷ���
        Bitmap loadingBitmap = null;
        loadingBitmap = loadingImageMap.get(resId);
        //���û��ȡ��
        if (loadingBitmap == null)
        {
            //����Դ�ļ��л�ȡ,���ҷŵ��б���,��������ȡ.
            loadingBitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
            loadingImageMap.put(resId, loadingBitmap);
        }
        return loadingBitmap;
    }


    /**
     * ����ͼƬ����.
     * @param imageCache ͼƬ����.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public void setImageCache(ImageCache imageCache)
    {
        mImageCache = imageCache;
    }

    /**
     * ���ͼƬ����.
     * @return imageCache
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public ImageCache getImageCache()
    {
        return mImageCache;
    }

    /**
     * �����Ƿ�ֹͣ����.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public void setExitTasksEarly(boolean exitTasksEarly)
    {
        mExitTasksEarly = exitTasksEarly;
    }

    /**
     * ����url�õ�bitmapͼƬ.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    protected abstract Bitmap processBitmap(String url);

    /**
     * ȡ��һ��ImageView�ļ����߳�.
     * @param imageView ��ȡ�������̵߳�ImageView.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public static void cancelWork(ImageView imageView)
    {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null)
        {
            bitmapWorkerTask.cancel(true);
            LogUtil.d(TAG, "cancel load : " + bitmapWorkerTask.url);
        }
    }

    /**
     * ȡ��ָ��url��ͼƬ�����߳�.
     * @param url ͼƬ�����ַ
     * @param imageView ͼƬView
     * @return �������ͬ���߳��ڽ���,����false,���򷵻� true.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public static boolean cancelPotentialWork(String url, ImageView imageView)
    {
        //��ImageView�л�ȡ�����߳�.
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null)
        {
            final String taskUrl = bitmapWorkerTask.url;
            if (TextUtils.isEmpty(taskUrl) || !taskUrl.equals(url))
            {
                //������ص�urlΪ��,��ü��ص�url����ָ����url,��ȡ����ǰ�ļ����߳�.
                bitmapWorkerTask.cancel(true);
                LogUtil.d(TAG, "cancel load : " + taskUrl);
            }
            else
            {
                // ������ص�url����ָ����url,��˵������Ҫ���¼���,����false.
                return false;
            }
        }
        return true;
    }

    /**
     * ��ImageView��ü����߳�.
     * @param imageView ͼƬView.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView)
    {
        //���IamgeView��Ϊ��.
        if (imageView != null)
        {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable)
            {
                //���ImageView��drawable,�����AsyncDrawable��ʵ��,���ؼ����߳�.
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    /**
     * ͼƬ�����߳�.
     *@Title:
     *@Description:
     *@Author:Justlcw
     *@Since:2014-3-6
     *@Version:
     */
    private class BitmapWorkerTask extends AsyncTask<String, String, Bitmap>
    {
        /** ͼƬ�����ַ. */
        private String url;
        /** ��������ʾͼƬ��ImageView. */
        private final WeakReference<ImageView> imageViewReference;

        /**
         * ���췽��
         * @param imageView ��ʾͼƬ��ImageView
         */
        public BitmapWorkerTask(ImageView imageView)
        {
            //����������
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params)
        {
            url = params[0];
            Bitmap bitmap = null;
            
            // 1.���ͼƬ���治Ϊnull.
            // 2.�����ǰ�߳�û�б�ȡ��.
            // 3.����������õ�ImageViewû�б�����.
            // 4.������س��������˳�.
            if (mImageCache != null && !isCancelled() && getAttachedImageView() != null && !mExitTasksEarly)
            {
                //��Ӳ�̻�����ȥ.
                bitmap = mImageCache.getBitmapFromDiskCache(url);
            }

            // 1.�����Ӳ�̻����л�ȡ��ͼƬΪNull.
            // 2.�����ǰ�߳�û�б�ȡ��.
            // 3.����������õ�ImageViewû�б�����.
            // 4.������س��������˳�.
            if (bitmap == null && !isCancelled() && getAttachedImageView() != null && !mExitTasksEarly)
            {
                //��̨��ȡͼƬ<�����ȡ>
                bitmap = processBitmap(params[0]);
            }

            // 1.������ع����ͼƬ��Ϊnull.
            // 2.���ͼƬ���治Ϊnull.
            if (bitmap != null && mImageCache != null)
            {
                //��ӵ�������.
                mImageCache.addBitmapToCache(url, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result)
        {
            // 1.�����ǰ�߳�<��ȡ��>.
            // 2.������س���<�������˳�>.
            if (isCancelled() || mExitTasksEarly)
            {
                result = null;
            }
            final ImageView imageView = getAttachedImageView();
            if (result != null && imageView != null)
            {
                //��������õ�ImageView����,���Ҽ��ص�ͼƬ��Ϊ��,����ʾͼƬ.
                setImageBitmap(imageView, result);
            }
        };

        /**
         * ��������õ�ImageView
         * @Description:
         * @Author Justlcw
         * @Date 2014-3-6
         */
        private ImageView getAttachedImageView()
        {
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
            //��õ�ImageView�еļ����߳� Ϊ ��ǰ�߳�
            if (this == bitmapWorkerTask)
            {
                //�򷵻����IamgeView.
                return imageView;
            }
            return null;
        }
    }

    /**
     * �����ü����̵߳�BitmapDrawable
     *@Title:
     *@Description:
     *@Author:Justlcw
     *@Since:2014-3-6
     *@Version:
     */
    private class AsyncDrawable extends BitmapDrawable
    {
        /** �����ü����߳�. */
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        /**
         * ���췽��.
         * @param res ϵͳ��Դ
         * @param bitmap ���ع����е�ͼƬ.
         * @param bitmapWorkerTask ͼƬ�����߳�.
         */
        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask)
        {
            super(res, bitmap);
            //�����ü����߳�.
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        /**
         * ���������õ�ͼƬ�����߳�.
         * @Description:
         * @Author Justlcw
         * @Date 2014-3-6
         */
        public BitmapWorkerTask getBitmapWorkerTask()
        {
            return bitmapWorkerTaskReference.get();
        }
    }

    /**
     * ��ͼƬ���õ�ImageView��.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    private void setImageBitmap(ImageView imageView, Bitmap bitmap)
    {
        //��ͼƬ���غõ�ʱ��,����һ����ת��ʽ,��һ��<͸��ͼƬ>����<���غõ�ͼƬ>,��ʱ<FADE_IN_TIME><200ms>
        final Drawable[] layers = new Drawable[] {
                new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)),
                new BitmapDrawable(mContext.getResources(), bitmap) };
        
        final TransitionDrawable transDrawable = new TransitionDrawable(layers);
        
        imageView.setImageDrawable(transDrawable);
        //��ʼչʾ.
        transDrawable.startTransition(FADE_IN_TIME);
    }
}
