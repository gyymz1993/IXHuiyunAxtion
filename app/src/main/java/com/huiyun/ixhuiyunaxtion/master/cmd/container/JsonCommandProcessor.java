package com.huiyun.ixhuiyunaxtion.master.cmd.container;

import com.huiyun.ixhuiyunaxtion.master.cmd.command.Data;
import com.huiyun.ixhuiyunaxtion.master.cmd.command.JsonPack;

import java.util.ArrayList;
import java.util.List;



/**   
 * @Description: 命令处理器 
 * @date 2015-2-4 下午1:36:25 
 * @version V1.0   
 */
public class JsonCommandProcessor {
	private byte[] pack, cmd;
	private JsonCommandBuffer buffer;
	private PackContainer container;
	
	public JsonCommandProcessor(){
		buffer = new JsonCommandBuffer(Data.BUFFER_CAPACITY);
		container = new PackContainer();
	}
	
	
	 /**
	 *  Function: 把收到的命令输进去，会输出解包后的命令
	 *  @author Yangshao 
	 *  2015-2-4 下午1:56:29
	 *  @param read
	 *  @param lenRead
	 *  @return
	 */
	public List<byte[]> processData(byte[] read, int lenRead){
		// 将接受到的数据添进缓存
		buffer.importByte(read, lenRead);
		
		// 从缓存中提取命令，放到容器中
		while((pack=buffer.exportPack()) != null){
			container.add(pack);
		}
		
		// 从容器中逐个提取指令并处理，最后清空容器
		List<byte[]> listCommand = new ArrayList<byte[]>();
		for(byte[] b:container.getContainer()){
			// 解包
			if(!JsonPack.Check_CustomCode(b, Data.CUSTOM_CODE_RF)) continue;
			cmd = JsonPack.Data_Unpack(b);
			if(cmd == null) continue;
			
			// 对解包后的命令进行处理
			listCommand.add(cmd);
		}
		container.clear();
		
		// 检查缓存的状态，处理异常
		switch(buffer.check()){
		case CommandBuffer.RESULT_ERROR_DATA:
			buffer.clean();
			break;
		}
		
		return listCommand;
	}
}
