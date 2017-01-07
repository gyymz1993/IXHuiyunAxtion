package com.huiyun.ixhuiyunaxtion.master.cmd.process;

import java.util.ArrayList;
import java.util.List;

/**   
 * @Description: 用于检查命令是否有反馈 
 * @date 2015-5-12 下午3:19:59 
 * @version V1.0   
 */
public class CmdFeedbackChecker {
	
	List<CmdFeedback> list;
	
	public CmdFeedbackChecker(){
		list = new ArrayList<CmdFeedback>();
	}
	
	
	 /**
	 *  Function:添加指定记录
	 *  @author lzn 
	 *  2015-5-12 下午3:30:09
	 *  @param addr
	 *  @param funcCode
	 */
	public void add(int addr, int funcCode){
		CmdFeedback cf = new CmdFeedback(addr,funcCode);
		if(!list.contains(cf)){
			list.add(cf);
		}
	}
	
	
	 /**
	 *  Function:删除指定记录
	 *  @author lzn 
	 *  2015-5-12 下午3:30:12
	 *  @param addr
	 *  @param funcCode
	 */
	public void remove(int addr, int funcCode){
		CmdFeedback cf = new CmdFeedback(addr,funcCode);
		list.remove(cf);
	}
	
	
	 /**
	 *  Function:查看指定记录是否存在
	 *  @author lzn 
	 *  2015-5-12 下午3:30:14
	 *  @param addr
	 *  @param funcCode
	 *  @return
	 */
	public boolean have(int addr, int funcCode){
		CmdFeedback cf = new CmdFeedback(addr,funcCode);
		return list.contains(cf);
	}
	
	static class CmdFeedback {
		
		CmdFeedback(int addr, int funcCode){
			this.addr = addr;
			this.funcCode = funcCode;
		}
		
		int addr;
		int funcCode;
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + addr;
			result = prime * result + funcCode;
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CmdFeedback other = (CmdFeedback) obj;
			if (addr != other.addr)
				return false;
			if (funcCode != other.funcCode)
				return false;
			return true;
		}
		
	}
}
