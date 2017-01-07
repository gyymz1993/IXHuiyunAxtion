package com.huiyun.ixhuiyunaxtion.master.json.analyst;

import com.huiyun.ixhuiyunaxtion.master.activity.MainActivity;

/**
 * 分析json的analyst的管理者 单例模式
 * 
 * @author lzy_torah
 * 
 */
public class AnalystManager {
	private static AnalystManager instance;

	private AnalystManager() {
	}

	public static AnalystManager getInstance() {
		if (instance == null) {
			synchronized (AnalystManager.class) {
				if (instance == null) {
					instance = new AnalystManager();
				}
			}
		}
		return instance;
	}

	/**
	 * 初始化把所有的分析者设置生效
	 */
	public void initAnalysts() {
		MainActivity application = MainActivity.context;
		application.setAnalyst(1, analyst_1);
		application.setAnalyst(3, analyst_3);
		application.setAnalyst(5, analyst_5);
		application.setAnalyst(7, analyst_7);
		application.setAnalyst(9, analyst_9);
		application.setAnalyst(11, analyst_11);
		application.setAnalyst(13, analyst_13);
		application.setAnalyst(15, analyst_15);
		application.setAnalyst(17, analyst_17);
		application.setAnalyst(19, analyst_19);
		application.setAnalyst(21, analyst_21);
		application.setAnalyst(23, analyst_23);
		application.setAnalyst(25, analyst_25);
		application.setAnalyst(27, analyst_27);
		application.setAnalyst(29, analyst_29);
		application.setAnalyst(31, analyst_31);
		application.setAnalyst(33, analyst_33);
		application.setAnalyst(35, analyst_35);
		application.setAnalyst(37, analyst_37);
		application.setAnalyst(39, analyst_39);
		application.setAnalyst(41, analyst_41);
		application.setAnalyst(43, analyst_43);

		application.setAnalyst(51, analyst_51);
		application.setAnalyst(53, analyst_53);
		application.setAnalyst(55, analyst_55);
		application.setAnalyst(57, analyst_57);
		application.setAnalyst(59, analyst_59);
		
		application.setAnalyst(61, analyst_61);
		application.setAnalyst(63, analyst_63);

		application.setAnalyst(67, analyst_67);
		application.setAnalyst(71, analyst_71);
		application.setAnalyst(73, analyst_73);
		application.setAnalyst(75, analyst_75);
		application.setAnalyst(77, analyst_77);
		
		application.setAnalyst(206, analyst_206);
		application.setAnalyst(202, analyst_202);
		application.setAnalyst(204, analyst_204);
		application.setAnalyst(208, analyst_208);
		application.setAnalyst(210, analyst_210);
		application.setAnalyst(212, analyst_212);
		application.setAnalyst(212, analyst_214);
		application.setAnalyst(222, analyst_222);
	}

	public final RegisterAdminResult_1 analyst_1 = new RegisterAdminResult_1();
	public final RegisterUserResult_3 analyst_3 = new RegisterUserResult_3();
	public final ContrlLoginResult_5 analyst_5 = new ContrlLoginResult_5();
	public final NotificationStudyResult_7 analyst_7 = new NotificationStudyResult_7();
	public final StartRedRayResult_9 analyst_9 = new StartRedRayResult_9();
	public final ContrlRedRayResult_11 analyst_11 = new ContrlRedRayResult_11();
	public final DownloadRedRayResult_13 analyst_13 = new DownloadRedRayResult_13();
	public final DownloadRedRay_15 analyst_15 = new DownloadRedRay_15();
	public final UploadRedRayResult_17 analyst_17 = new UploadRedRayResult_17();
	public final DeleteRemoteResult_19 analyst_19 = new DeleteRemoteResult_19();
	public final DownloadDevices_21 analyst_21 = new DownloadDevices_21();
	public final DownloadOutDevices_23 analyst_23 = new DownloadOutDevices_23();
	public final SettingCom_Result_25 analyst_25 = new SettingCom_Result_25();
	public final ControlClosedAndOpenResult_27 analyst_27 = new ControlClosedAndOpenResult_27();
	public final ReadStateResult_29 analyst_29 = new ReadStateResult_29();
	public final DownloadGateway_31 analyst_31 = new DownloadGateway_31();
	public final SettingGatewayResult_33 analyst_33 = new SettingGatewayResult_33();
	public final UploadAreaResult_35 analyst_35 = new UploadAreaResult_35();
	public final DownloadAllEare_37 analyst_37 = new DownloadAllEare_37();
	public final UploadDevicesResult_39 analyst_39 = new UploadDevicesResult_39();
	public final BlinkBox_41 analyst_41 = new BlinkBox_41();
	public final SeachDeviceResult_43 analyst_43 = new SeachDeviceResult_43();
	
	public final DownloadAllSceneResult_51 analyst_51 = new DownloadAllSceneResult_51();
	public final UploadSceneResult_53 analyst_53 = new UploadSceneResult_53();
	public final ContrlSceneResult_55 analyst_55 = new ContrlSceneResult_55();
	public final DownloadSlave_57 analyst_57 = new DownloadSlave_57();
	public final DeleteSlave_59 analyst_59 = new DeleteSlave_59();
	public final UploadWebcam_61 analyst_61 = new UploadWebcam_61();
	public final DownloadWebcam_63 analyst_63 = new DownloadWebcam_63();

	public final TimerTaskResult_67 analyst_67 = new TimerTaskResult_67();
	public final DownloadTimerTask_71 analyst_71 = new DownloadTimerTask_71();
	public final DelTimerTaskResult_73 analyst_73 = new DelTimerTaskResult_73();
	public final SetPanKey_75 analyst_75 = new SetPanKey_75();
	public final WifiConnectResult_77 analyst_77 = new WifiConnectResult_77();
	
	public final MasterLoginBack_206 analyst_206 = new MasterLoginBack_206();
	public final MasterLoadUserBack_202 analyst_202 = new MasterLoadUserBack_202();
	public final MasterLoadDeviceBack_204 analyst_204 = new MasterLoadDeviceBack_204();
	public final MasterLoadRedcodeBack_208 analyst_208 = new MasterLoadRedcodeBack_208();
	public final MasterLoadAreaBack_210 analyst_210 = new MasterLoadAreaBack_210();
	public final MasterLoadSceneBack_212 analyst_212 = new MasterLoadSceneBack_212();
	public final MasterUploadCameraName_214 analyst_214 = new MasterUploadCameraName_214();
	public final HeartbeatResult_222 analyst_222 = new HeartbeatResult_222();
}
