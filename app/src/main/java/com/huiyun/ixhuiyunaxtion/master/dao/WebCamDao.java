package com.huiyun.ixhuiyunaxtion.master.dao;

import com.huiyun.ixhuiyunaxtion.master.bean.table.Webcam;

import java.util.List;


/**
 * @Description: TODO(用一句话描述该文件做什么)
 * @date 2015年1月19日 下午2:30:07
 * @version V1.0
 */
public interface WebCamDao {

	/**
	 * Function:添加和更新摄像头
	 * 
	 * @author Yangshao 2015年1月19日 下午2:35:01
	 */
	public boolean addAndUpdateWebCam(List<Webcam> webcams);

	/**
	 * Function:查询所有摄像头
	 * 
	 * @author Yangshao 2015年1月19日 下午2:38:43
	 */
	public List<Webcam> getAllWebCam();

}
