package com.chs.mt.pxe_r500.common;

/**
 * @author Administrator
 *
 */
public class Common {
	//测试用�?，不�?��设备
	public static final boolean NO_DEVICE = false;
	
	//�?���?
	public static final int EXITCODE_EXCEPTION = 1;
	
	
	//�?��场景ID
	public static final int MAXSID = 9998;
	//运动概要场景ID
	public static final int SummarySID = 9999;
	
	//场景购买状�?̬
	public static final int NotPurchase = 0;
	public static final int Purchasing = 1;
	public static final int Purchased = 2;
	public static final int NotYetReleased = 3;
	
	//场景购买阶段
	public static final int PurchaseStatusStart = 0;
	public static final int PurchaseStatusFinish = 1;
	public static final int PurchaseStatusError = 2;
	public static final int PurchaseStatusInvalidID = 3;
	
	//场景类型
	public static final int SceneTypeNormal = 0;
	public static final int SceneTypeExtend = 1;
	
	//无效GPS
	public static final int GPSLatUndefined = 500;
	public static final int GPSLngUndefined = 500;
	
	//默认身高，体�?
	public static final double DefaultHeight = 170;
	public static final double DefaultWeight = 60;
	
	//设备连接状�?
	public static final int BTConnectStatusDisconnect = 1<<0;
	public static final int BTConnectStatusConnecting = 1<<1;
	public static final int BTConnectStatusConnect = 1<<2;
	public static final int BTConnectStatusAccept = 1<<3;
	
	//健康水平
	public static final int SportStatusReach = 0;
	public static final int SportStatusLack = 1;
	public static final int SportStatusUndefined = 2;
	
	//设备提醒
	public static final int SportNotifyStatusSITLONG = 0;
	public static final int SportNotifyStatusLack = 1;
	public static final int SportNotifyStatusUndefined = 2;
	
	//蓝牙数据类型
	public static final int SyncSceneRespoonse = 0;
	public static final int RealtimeData = 1;
	public static final int PlayCmdResponse = 2;
	
	//播放命令
	public static final int PLAY_CMD_TERMINATE = -1;
	public static final int PLAY_CMD_PAUSE = 0;
	public static final int PLAY_CMD_START = 1;
	public static final int PLAY_CMD_READY = 2;
	public static final int PLAY_CMD_UNDEFINED = 3;
	
	//GPS
	public static final int GPS_FLAG_OFF = 0;
	public static final int GPS_FLAG_ON = 1;
	
	//单工模式
	public static final boolean SIMPLEX = true;
	
	//Atuat主机�?
	public static final String AtuatHostName = "www.reago.net";
	
	//http请求方式
	public static final int RequestMethodGet = 0;
	public static final int RequestMethodPost = 1;
	public static final int RequestMethodPut = 2;
	
	//http body分界
	public static final String HttpBodyBoundary = "---------------------------14737809831466499882746641449";
	
	//界面动作
	public static final int SubmitReceipt = 0;
	public static final int GetSceneList = 1;
	public static final int GetSceneDetail = 2;
	public static final int UploadSportSummary = 3;
	public static final int DownloadSportSummary = 4;
	public static final int UploadSceneHistory = 5;
	public static final int GetRankInfo = 6;
	public static final int GetLastSceneHistory = 7;
	public static final int GetSceneHistory = 8;
	public static final int AddFeedback = 9;
	
	public static final int GetRequestToken = 10;
	public static final int LoadAuthPage = 11;
	public static final int GetAccessToken = 12;
	
	public static final int GetUserInfo = 13;
	public static final int UpdateUserInfo = 14;
	public static final int GetFriends = 15;
	public static final int ShareExperience = 16;
	public static final int AddRootComment = 17;
	public static final int ReplyComment = 18;
	public static final int GetRootComment = 19;
	public static final int GetRootCommentReplies = 20;
	public static final int UpdateUserAvatar = 21;
	public static final int NoneAction=22;
	
	//http request
	public static final int AtuatGetSceneList = 0;
	public static final int AtuatGetSceneDetail = 1;
	public static final int AtuatUploadSceneHistory = 2;
	public static final int AtuatDownloadSceneHistory = 3;
	public static final int AtuatSubmitFeedback = 4;
	public static final int AtuatSubmitReceipts = 5;
	public static final int TwitterGetRequestToken = 6;
	public static final int TwitterGetAccessToken = 7;
	public static final int TwitterGetUserInfo = 8;
	public static final int TwitterReqUpdateUserInfo = 9;
	public static final int TwitterUpdateUserAvatar = 10;
	public static final int TwitterGetFriensID = 11;
	public static final int TwitterGetFriensInfo = 12;
	public static final int TwitterAddPrivate = 13;
	public static final int TwitterReqAddTweet = 14;
	public static final int TwitterReqGetTweets = 15;
	public static final int TwitterReqGetTweetReply = 16;
	public static final int TwitterReqReplyTweet = 17;
	public static final int WeiboGetRequestToken = 18;
	public static final int WeiboGetAccessToken = 19;
	public static final int WeiboGetUserInfo = 20;
	public static final int WeiboReqUpdateUserInfo = 21;
	public static final int WeiboUpdateUserAvatar = 22;
	public static final int WeiboGetFriends = 23;
	public static final int WeiboReqAddTweet = 24;
	public static final int WeiboReqGetTweets = 25;
	public static final int WeiboAddPrivate = 26;
	public static final int WeiboReqGetTweetReply = 27;
	public static final int WeiboReqReplyTweet = 28;
	public static final int TencentGetRequestToken = 29;
	public static final int TencentGetAccessToken = 30;
	public static final int TencentGetIdolList = 31;
	public static final int TencentGetUserInfo = 32;
	public static final int TencentReqUpdateUserInfo = 33;
	public static final int TencentUpdateUserAvatar = 34;
	public static final int TencentReqAddPrivate = 35;
	public static final int TencentReqAddTweet = 36;
	public static final int TencentReqGetTweets = 37;
	public static final int TencentReqReplyTweet = 38;
	public static final int TencentReqGetTweetReply = 39;
	public static final int FacebookGetUserInfo = 40;
	public static final int FacebookGetFriends = 41;
	public static final int FacebookReqAddPost = 42;
	public static final int FacebookReqGetPost = 43;
	public static final int FacebookReqGetPostComment = 44;
	public static final int FacebookReqAddComment = 45;
	public static final int FacebookReqAddPrivate = 46;
	
	//http service
	public static final int AtuatSceneList = 0;
	public static final int AtuatSceneHistory = 1;
	public static final int AtuatFeedback = 2;
	public static final int AtuatReceipt = 3;
	public static final int TwitterRequestToken = 4;
	public static final int TwitterAccessToken = 5;
	public static final int TwitterUserInfo = 6;
	public static final int TwitterUpdateUserInfo = 7;
	public static final int TwitterPrivate = 8;
	public static final int TwitterUserAvatar = 9;
	public static final int TwitterFriensID = 10;
	public static final int TwitterFriendsInfo = 11;
	public static final int TwitterAddTweet = 12;
	public static final int TwitterGetTweets = 13;
	public static final int TwitterGetTweetReply = 14;
	public static final int TwitterReplyTweet = 15;
	public static final int WeiboRequestToken = 16;
	public static final int WeiboAccessToken = 17;
	public static final int WeiboUserInfo = 18;
	public static final int WeiboUpdateUserInfo = 19;
	public static final int WeiboUserAvatar = 20;
	public static final int WeiboFriends = 21;
	public static final int WeiboAddTweet = 22;
	public static final int WeiboGetTweets = 23;
	public static final int WeiboPrivate =24;
	public static final int WeiboGetTweetReply = 25;
	public static final int WeiboReplyTweet = 26;
	public static final int TencentRequestToken = 27;
	public static final int TencentAccessToken = 28;
	public static final int TencentIdolList = 29;
	public static final int TencentUserInfo = 30;
	public static final int TencentUpdateUserInfo = 31;
	public static final int TencentUserAvatar = 32;
	public static final int TencentAddTweet = 33;
	public static final int TencentGetTweets = 34;
	public static final int TencentReplyTweet = 35;
	public static final int TencentGetTweetReply = 36;
	public static final int TencentAddPrivate = 37;
	
	//通知种类
	public static final String EventBTConnectStatusChanged = "EventBTConnectStatusChanged";
	public static final String EventBTDeviceConnectStatusChanged = "EventBTDeviceConnectStatusChanged";
	public static final String EventBTDataReceived = "EventBTDataReceived";
	public static final String EventNotifyStatusChanged = "EventNotifyStatusChanged";
	public static final String EventHealthLevelChanged = "EventHealthLevelChanged";
	
	public static final String EventNetworkDataReceived = "EventNetworkDataReceived";
	public static final String EventNetworkURLLoaded = "EventNetworkURLLoaded";
	public static final String EventNetworkError = "EventNetworkError";
	public static final String EventNetworkTimeout = "EventNetworkTimeout";
	public static final String EventNetworkGPSParsed = "EventNetworkGPSParsed";
	public static final String EventNetworkUserInfoUpdated = "EventNetworkUserInfoUpdated";
	public static final String EventNetworkFriendListUpdated = "EventNetworkFriendListUpdated";
	
	public static final String EventNetworkPurchaseStatusChanged = "EventNetworkPurchaseStatusChanged";
	
	public static final String EventGPSFail = "EventGPSFail";
	public static final String EventGPSChange = "EventGPSChange";
	
	public static final String EventNetworkThemeLoaded = "EventNetworkThemeLoaded";
	public static final String EventThemeChanged = "EventThemeChanged";
	
	public static final String EventAppEnterBackground = "EventAppEnterBackground";
	public static final String EventAppEnterForeground = "EventAppEnterForeground";
	
	//活动�?
	public static final int ActivityLevelLight = 0;
	public static final int ActivityLevelModerate = 1;
	public static final int ActivityLevelRigor = 2;
	public static final int ActivityLevelAll = 9999;
	
	//地点
	public static final int PlaceOffice = 0;
	public static final int PlaceSubway = 1;
	public static final int PlaceCar = 2;
	public static final int PlaceGym = 3;
	public static final int PlaceHome = 4;
	public static final int PlaceOutdoors = 5;
	public static final int PlaceNearby = 6;
	public static final int PlaceAll = 9999;
	
	//目的
	public static final int TargetLoseWeight = 0;
	public static final int TargetKeepHealthy = 1;
	public static final int TargetDiseasecontrol = 2;
	public static final int TargetAll = 9999;
	
	//目标单位
	public static final int Time = 0;
	public static final int Times = 1;
	public static final int Km = 2;
	public static final int Turns = 3;
	public static final int Foots = 4;
}
