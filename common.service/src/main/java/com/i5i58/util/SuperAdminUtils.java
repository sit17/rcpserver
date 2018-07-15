package com.i5i58.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.i5i58.data.superAdmin.SuperAdmin;
import com.i5i58.data.superAdmin.SuperAdminAuth;
import com.i5i58.data.superAdmin.SuperAdminRecord;
import com.i5i58.primary.dao.superAdmin.SuperAdminPriDao;
import com.i5i58.primary.dao.superAdmin.SuperAdminRecordPriDao;
import com.i5i58.secondary.dao.superAdmin.SuperAdminSecDao;

@Component
public class SuperAdminUtils {

	@Autowired
	AuthVerify<SuperAdminAuth> superAuthVerify;

	@Autowired
	SuperAdminPriDao superAdminPriDao;

	@Autowired
	SuperAdminSecDao superAdminSecDao;

	@Autowired
	SuperAdminRecordPriDao superAdminRecordPriDao;

	public boolean verifyAuth(String superAccid, SuperAdminAuth auth) {

		SuperAdmin superAdmin = superAdminPriDao.findOne(superAccid);
		if (superAdmin != null && superAuthVerify.Verify(auth, superAdmin.getAdminRight())) {
			System.out.println("super right:" + superAdmin.getAdminRight() + ", auth:" + auth.getValue());
			return true;
		}
		System.out.println("not super admin");
		return false;
	}

	public boolean isSuperAdmin(String accId) {
		SuperAdmin superAdmin = superAdminPriDao.findOne(accId);
		if (superAdmin == null) {
			return false;
		}
		return true;
	}

	public SuperAdmin createSuperAdmin(String accId, long brithday, String depart, String email, byte gender,
			String location, String openId, String password, String phoneNo, String realName, int auth) {
		SuperAdmin superAdmin = new SuperAdmin();
		superAdmin.setAccId(accId);
		superAdmin.setAdminRight(auth);
		superAdmin.setBirthDate(brithday);
		superAdmin.setDepart(depart);
		superAdmin.setEmailAddress(email);
		superAdmin.setGender(gender);
		superAdmin.setLocation(location);
		superAdmin.setOpenId(openId);
		superAdmin.setPassword(password);
		superAdmin.setPhoneNo(phoneNo);
		superAdmin.setRealName(realName);
		superAdmin.setNullity(false);
		superAdminPriDao.save(superAdmin);
		return superAdmin;
	}

	public void disableSuperAdmin(String accId) {
		SuperAdmin superAdmin = superAdminPriDao.findOne(accId);
		superAdmin.setNullity(true);
		superAdminPriDao.save(superAdmin);
	}

	public List<SuperAdmin> getAllSuperAdmin() {
		return superAdminPriDao.findByNullity(false);
	}

	public void superAdminRecord(String superAccId, String format, Object... args) {
		SuperAdmin sa = superAdminPriDao.findOne(superAccId);
		SuperAdminRecord superAdminRecord = new SuperAdminRecord();
		superAdminRecord.setAccId(superAccId);
		superAdminRecord.setOperateTime(DateUtils.getNowTime());
		if (sa != null) {
			superAdminRecord.setRealName(sa.getRealName());
		}
		superAdminRecord.setRecordContent(String.format(format, args));
		superAdminRecordPriDao.save(superAdminRecord);
	}
}
