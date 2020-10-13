package com.chs.mt.pxe_r500.MusicBox;

/**
 * Created by Administrator on 2017/10/20.
 */

public class MData {
    public static int MDataMax = 512;
    /*
    1）	MAGIC：第一个字节0x01即SOH，第二个字节对其按位取反。
    2）	CHAN：通道模式，FLAGS第一个字节的bit[0-1]，0h00表示SPP通道，0h01表示TWI或SPI通道，其他未定义。
    3）	SEG：分段标志，FLAGS第一个字节的bit[2-3]，0h00表示不用分段，0h01表示第一段，0h10表示后续段但非最后一段，0h11表示最后一段。分段一般需要配合ACK使用。
    4）	ACK：ACK标志，FLAGS第一个字节的bit[4]，0h0表示不需要ACK，0h1表示需要ACK。可以使用该标志置位来确保命令被分发处理，如果收不到ACK，则对方可以重发命令。
    5）	FLAGS其他BIT保留。
    */
    //------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------  通讯数据结构定义   一帧的数据定义 ----------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------
    public  int MAGIC;      //
    public  int FLAGS_Temp;      //
    public  int ACK;      //
    public  int SEG;      //
    public  int CHAN;      //
    public  int CMD_TYPE;   //
    public  int CMD_ID;     //CMD ID：命令号，各种命令类型都有不同的命令号空间，详细见下面命令总表。
    public  int CMD_LENGTH; //CMD LENGTH：命令长度，统计范围为整条命令，即16字节的固定命令头部再加上不定长参数
    public  int PARAMLENGTH; //不定长参数
    public  int PARAM1;     //
    public  int PARAM2;     //
    public  int[] BUF = new int[MDataMax];	//
    public  int[] DataBUF = new int[MDataMax];	//

    public MData(){


    }

}
