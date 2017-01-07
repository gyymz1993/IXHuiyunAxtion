package com.huiyun.ixhuiyunaxtion.master.bean.table;
public class Version {
		public int version_id;
		public String desc;

		public int getVersion_id() {
			return version_id;
		}

		public void setVersion_id(int version_id) {
			this.version_id = version_id;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public Version(int version_id, String desc) {
			super();
			this.version_id = version_id;
			this.desc = desc;
		}
	}