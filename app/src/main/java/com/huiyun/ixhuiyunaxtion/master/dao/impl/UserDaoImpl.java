package com.huiyun.ixhuiyunaxtion.master.dao.impl;

import java.util.List;

import android.content.Context;


import com.huiyun.ixhuiyunaxtion.master.bean.table.User;
import com.huiyun.ixhuiyunaxtion.master.dao.UserDao;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

/**
 * @Title: UserDao.java
 * @Package com.huiyun.ixconfig.dao
 * @Description: User表数据库的操作
 * @author Yangshao
 * @date 2015年1月6日 下午5:27:37
 * @version V1.0
 */
public class UserDaoImpl implements UserDao {
	private DbUtils db;

	public UserDaoImpl(Context mcontext) {
		db = DbUtils.create(mcontext);
		db.configAllowTransaction(true);
		db.configDebug(true);
	}

	@Override
	public boolean saveOrupdateForce(User user) {
		try {
			User us = db.findFirst(Selector.from(User.class).where("name", "=",
					user.getName()));
			if (us == null) {
				db.save(user);
			} else {
				if (us.getType() == 1 && user.getType() == 2) {
					return false;
				}
				user.setId(us.getId());
				db.update(user, new String[] { "name", "password", "type" });
			}
			return true;
		} catch (DbException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public User queryUser(User user) {
		try {
			User us = db.findFirst(Selector.from(User.class)
					.where("name", "=", user.getName())
					.and("password", "=", user.getPassword()));
			if (us != null) {
				return us;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	@Override
	public List<User> queryAllUser() {
		try {
			List<User> list = db.findAll(User.class);
			if (list == null)
				return null;
			return list;
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}

	}

}
