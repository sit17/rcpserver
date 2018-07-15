package com.i5i58.service.im;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.im.IIMTeam;
import com.i5i58.util.JsonUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.CreateTeamResult;
import com.i5i58.yunxin.Utils.YXResultSet;

@Service(protocol = "dubbo")
public class IMTeamService implements IIMTeam {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtils;

	/**
	 * 获取群主ID
	 * 
	 * @param tid
	 * 
	 */
	private String getTeamOwner(String tid) {
		try {
			// String tids = "[\"" + tid + "\"]";
			// QueryTeamResult result = YunxinIM.queryTeam(tids, "0");
			// if (result == null || result.getTinfos().isEmpty()) {
			// return null;
			// }
			// return result.getTinfos().get(0).getOwner();
			return "";
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 判断是否是群主的accid
	 * 
	 * @param tid
	 *            群ID
	 * @param owner
	 *            外部传入的群主ID，如果非空，需要检验
	 * @param curAccId
	 *            当前用户的accId
	 * @return ResultDataSet @throws
	 */
	private ResultDataSet notOwnerError(String tid, String owner, String curAccId) {
		ResultDataSet rds = null;
		// String realOwner = getTeamOwner(tid);
		// // 传入的群主ID非法
		// if (!owner.isEmpty() && !realOwner.equalsIgnoreCase(owner)) {
		// rds = new ResultDataSet();
		// rds.setCode(ResultCode.SERVICE_ERROR.getCode());
		// rds.setMsg("yxException : owner is invalid!!!");
		// }
		// 当前用户不是群主
		if (!curAccId.equals(owner)) {
			rds = new ResultDataSet();
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("yxException : you are not the team owner!!!");
		}
		return rds;
	}

	@Override
	public ResultDataSet create(String accId, String tname, String members, String msg, String magree, String joinMode,
			String custom, String clientIp) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			CreateTeamResult result = null;
			try {
				List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
				arrayParams.add(new BasicNameValuePair("tname", tname));
				arrayParams.add(new BasicNameValuePair("owner", accId));
				arrayParams.add(new BasicNameValuePair("members", members));
				arrayParams.add(new BasicNameValuePair("msg", msg));
				arrayParams.add(new BasicNameValuePair("magree", magree));
				arrayParams.add(new BasicNameValuePair("joinmode", joinMode));
				arrayParams.add(new BasicNameValuePair("custom", custom));
				result = YunxinIM.createTeam(arrayParams);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:create team null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				String response = CodeToString.getString(code);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM create team failed msg : " + response);
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());

			String data = new String("");
			String tid = result.getTid();
			data = "{\"tid\":" + tid + "}";
			rds.setData(data);
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet remove(String accId, String tid, String clientIp) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			ResultDataSet resultDataSet = notOwnerError(tid, "", accId);
			if (resultDataSet != null) {
				rds = resultDataSet;
				return rds;
			}
			YXResultSet result = null;
			try {
				List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
				arrayParams.add(new BasicNameValuePair("owner", accId));
				arrayParams.add(new BasicNameValuePair("tid", tid));
				result = YunxinIM.removeTeam(arrayParams);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("remove team null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				String response = CodeToString.getString(code);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM remove team failed msg : " + response);
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet query(String tids, String ope) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			String result = null;
			try {
				result = YunxinIM.queryTeam(tids, ope);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("query team null");
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			rds.setData(result);
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet changeOwner(String accId, String tid, String newOwner) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			YXResultSet result = null;
			String leave = "2"; // 1:群主解除群主后离开群，2：群主解除群主后成为普通成员
			try {
				result = YunxinIM.changeTeamOwner(tid, accId, newOwner, leave);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("change owner null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				String response = CodeToString.getString(code);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM change owner failed msg : " + response);
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet addTeamMembers(String accId, String tid, String owner, String members, String magree,
			String msg, String attach) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			ResultDataSet resultDataSet = notOwnerError(tid, owner, accId);
			if (resultDataSet != null) {
				rds = resultDataSet;
				return rds;
			}
			YXResultSet result = null;
			try {
				result = YunxinIM.addTeamMembers(tid, owner, members, magree, msg, attach);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("change owner null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM add members failed msg : " + result.getError());
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet kickTeamMemeber(String accId, String tid, String owner, String member, String attach)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			ResultDataSet resultDataSet = notOwnerError(tid, owner, accId);
			if (resultDataSet != null) {
				rds = resultDataSet;
				return rds;
			}
			YXResultSet result = null;
			try {
				result = YunxinIM.kickTeamMemeber(tid, owner, member, attach);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("change owner null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM kick member failed msg : " + result.getError());
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet updateTeam(String accId, String tid, String tname, String owner, String announcement,
			String intro, String joinMode, String custom, String icon, String beinvteMode, String inviteMode,
			String uptinfoMode, String upcustomMode) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			ResultDataSet resultDataSet = notOwnerError(tid, owner, accId);
			if (resultDataSet != null) {
				rds = resultDataSet;
				return rds;
			}
			YXResultSet result = null;
			try {
				result = YunxinIM.updateTeam(tid, tname, owner, announcement, intro, joinMode, custom, icon,
						beinvteMode, inviteMode, uptinfoMode, upcustomMode);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("change owner null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM update team failed msg : " + result.getError());
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet addTeamManager(String accId, String tid, String owner, String members) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			ResultDataSet resultDataSet = notOwnerError(tid, owner, accId);
			if (resultDataSet != null) {
				rds = resultDataSet;
				return rds;
			}
			YXResultSet result = null;
			try {
				result = YunxinIM.addTeamManager(tid, owner, members);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("change owner null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM add manager failed msg : " + result.getError());
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet removeTeamManager(String accId, String tid, String owner, String members) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			ResultDataSet resultDataSet = notOwnerError(tid, owner, accId);
			if (resultDataSet != null) {
				rds = resultDataSet;
				return rds;
			}
			YXResultSet result = null;
			try {
				result = YunxinIM.removeTeamManager(tid, owner, members);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("change owner null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM remove manager failed msg : " + result.getError());
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet queryUserJoinedTeamsInfo(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			String result = null;
			try {
				result = YunxinIM.queryUserJoinedTeamsInfo(accId);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("change owner null");
				return rds;
			}

			rds.setCode(ResultCode.SUCCESS.getCode());
			rds.setData(result);
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet updateTeamNick(String tid, String owner, String accId, String nick) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			YXResultSet result = null;
			try {
				result = YunxinIM.updateTeamNick(tid, owner, accId, nick);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("change owner null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM update team nick failed msg : " + result.getError());
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet updateMuteTeamConfig(String accId, String tid, String targetAccId, String ope)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			ResultDataSet resultDataSet = notOwnerError(tid, "", accId);
			if (resultDataSet != null) {
				rds = resultDataSet;
				return rds;
			}
			YXResultSet result = null;
			try {
				result = YunxinIM.updateMuteTeamConfig(tid, targetAccId, ope);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("change owner null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM update team mute config failed msg : " + result.getError());
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet setTeamMuteList(String accId, String tid, String owner, String targetAccId, String mute)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			ResultDataSet resultDataSet = notOwnerError(tid, owner, accId);
			if (resultDataSet != null) {
				rds = resultDataSet;
				return rds;
			}
			YXResultSet result = null;
			try {
				result = YunxinIM.setTeamMuteList(tid, owner, targetAccId, mute);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("change owner null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM update team mute list failed msg : " + result.getError());
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet leaveTeam(String tid, String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			YXResultSet result = null;
			try {
				result = YunxinIM.leaveTeam(tid, accId);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("change owner null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM leave team failed msg : " + result.getError());
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

}
