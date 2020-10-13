package com.chs.mt.pxe_r500.encrypt;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import com.chs.mt.pxe_r500.datastruct.Define;

/**�Զ���ʵ�ּ򵥵��ļ����ܽ��ܹ���
 */
@SuppressLint("DefaultLocale") public class SeffFileCipherUtil {
    /**
     * �ӽ���ʱ��32K���ֽ�Ϊ��λ���мӽ��ܼ���
     */
    private static final int CIPHER_BUFFER_LENGHT = 32 * 1024;

    /**
     * ���ܣ�������Ҫ����ʾ���ܵ�ԭ��û����ʲôʵ�ʵļ����㷨
     *
     * @param filePath �����ļ�����·��
     * @return
     */
    
    
    public static boolean encrypt(String filePath) {/*, CipherListener listener) {*/
        try {
            long startTime = System.currentTimeMillis();
            File f = new File(filePath);
            RandomAccessFile raf = new RandomAccessFile(f, "rw");
            long totalLenght = raf.length();
            FileChannel channel = raf.getChannel();

            long multiples = totalLenght / CIPHER_BUFFER_LENGHT;
            long remainder = totalLenght % CIPHER_BUFFER_LENGHT;

            MappedByteBuffer buffer = null;
            byte tmp;
            byte rawByte;

            //�ȶ��������ּ���
            for(int i = 0; i < multiples; i++){
                buffer = channel.map(
                        FileChannel.MapMode.READ_WRITE, i * CIPHER_BUFFER_LENGHT, (i + 1) * CIPHER_BUFFER_LENGHT);

                //�˴��ļ��ܷ����ܼ򵥣�ֻ�Ǽ򵥵�������
                for (int j = 0; j < CIPHER_BUFFER_LENGHT; ++j) {
                    rawByte = buffer.get(j);
                    tmp = (byte) (rawByte ^ j);
                    buffer.put(j, tmp);

//                    if(null != listener){
//                        listener.onProgress(i * CIPHER_BUFFER_LENGHT + j, totalLenght);
//                    }
                }
                buffer.force();
                buffer.clear();
            }

            //���������ּ���
            buffer = channel.map(
                    FileChannel.MapMode.READ_WRITE, multiples * CIPHER_BUFFER_LENGHT, multiples * CIPHER_BUFFER_LENGHT + remainder);

            for (int j = 0; j < remainder; ++j) {
                rawByte = buffer.get(j);
                tmp = (byte) (rawByte ^ j);
                buffer.put(j, tmp);

//                if(null != listener){
//                    listener.onProgress(multiples * CIPHER_BUFFER_LENGHT + j, totalLenght);
//                }
            }
            buffer.force();
            buffer.clear();

            channel.close();
            raf.close();

            //�Լ��ܺ���ļ�������������.cipher��׺
//            f.renameTo(new File(f.getPath() + CIPHER_TEXT_SUFFIX));
            Log.d("������ʱ��", (System.currentTimeMillis() - startTime) /1000 + "s");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * ���ܣ�������Ҫ����ʾ���ܵ�ԭ��û����ʲôʵ�ʵļ����㷨
     *
     * @param filePath �����ļ�����·�����ļ���Ҫ��.cipher��β�Ż���Ϊ��ʵ�ɽ�������
     * @return
     */
    public static boolean decrypt(String filePath ){/*, CipherListener listener) {*/
        try {
//            long startTime = System.currentTimeMillis();
//            File f = new File(filePath);
//            if(!f.getPath().toLowerCase().endsWith(CIPHER_TEXT_SUFFIX)){
//                //��׺��ͬ����Ϊ�ǲ��ɽ��ܵ�����
//                return false;
//            }
            //���ƶ�������ļ�
            String srcFile = filePath;  
            String destFile = filePath+Define.CIPHER_TEXT_SUFFIX;  
            RandomAccessFile rafi = new RandomAccessFile(srcFile, "r");  
            RandomAccessFile rafo = new RandomAccessFile(destFile, "rw");  
              
            byte[] buf = new byte[1024 * 8];               
            int c = rafi.read(buf);  
              
            while (c > 0) {  
                if (c == buf.length) {  
                    rafo.write(buf);  
                } else {  
                    rafo.write(buf, 0, c);  
                }  
              
                c = rafi.read(buf);  
            }  
            rafi.close();  
            rafo.close();  
        	File f = new File(destFile);
            //------------------
        	//File f = new File(filePath);
            
            RandomAccessFile raf = new RandomAccessFile(f, "rw");
            long totalLenght = raf.length();
            FileChannel channel = raf.getChannel();

            long multiples = totalLenght / CIPHER_BUFFER_LENGHT;
            long remainder = totalLenght % CIPHER_BUFFER_LENGHT;

            MappedByteBuffer buffer = null;
            byte tmp;
            byte rawByte;

            //�ȶ��������ֽ���
            for(int i = 0; i < multiples; i++){
                buffer = channel.map(
                        FileChannel.MapMode.READ_WRITE, i * CIPHER_BUFFER_LENGHT, (i + 1) * CIPHER_BUFFER_LENGHT);

                //�˴��Ľ��ܷ����ܼ򵥣�ֻ�Ǽ򵥵�������
                for (int j = 0; j < CIPHER_BUFFER_LENGHT; ++j) {
                    rawByte = buffer.get(j);
                    tmp = (byte) (rawByte ^ j);
                    buffer.put(j, tmp);
//
//                    if(null != listener){
//                        listener.onProgress(i * CIPHER_BUFFER_LENGHT + j, totalLenght);
//                    }
                }
                buffer.force();
                buffer.clear();
            }

            //���������ֽ���
            buffer = channel.map(
                    FileChannel.MapMode.READ_WRITE, multiples * CIPHER_BUFFER_LENGHT, multiples * CIPHER_BUFFER_LENGHT + remainder);

            for (int j = 0; j < remainder; ++j) {
                rawByte = buffer.get(j);
                tmp = (byte) (rawByte ^ j);
                buffer.put(j, tmp);

//                if(null != listener){
//                    listener.onProgress(multiples * CIPHER_BUFFER_LENGHT + j, totalLenght);
//                }
            }
            buffer.force();
            buffer.clear();

            channel.close();
            raf.close();

            //�Լ��ܺ���ļ�������������.cipher��׺
//            f.renameTo(new File(f.getPath().substring(f.getPath().toLowerCase().indexOf(DataStruct.CIPHER_TEXT_SUFFIX))));

//            Log.d("������ʱ��", (System.currentTimeMillis() - startTime) / 1000 + "s");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ���ڼӽ��ܽ��ȵļ�����
     */
//    public interface CipherListener{
//        void onProgress(long current, long total);
//    }
}
