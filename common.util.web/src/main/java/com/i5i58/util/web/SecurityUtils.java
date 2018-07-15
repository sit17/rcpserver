package com.i5i58.util.web;

import java.util.Date;

import com.i5i58.util.Constant;
import com.i5i58.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class SecurityUtils {

	public static String createJsonWebToken(String accId, int device, String serialNum) {
		Date date = new Date();
		String jwtToken = Jwts.builder().setIssuer("www.i5i58.com").setId("1").setSubject(accId).setAudience("user")
				.setIssuedAt(date).claim("serialNum", serialNum).claim("device", String.valueOf(device))
				.signWith(SignatureAlgorithm.HS256, Constant.JWT_SECRET_KEY + serialNum).compact();
		return jwtToken;
	}

	public static JsonWebTokenParams validJsonWebToken(String token, String serialNum) {
		JsonWebTokenParams jwt = new JsonWebTokenParams();
		try {
			final Claims claims = Jwts.parser().setSigningKey(Constant.JWT_SECRET_KEY + serialNum)
					.parseClaimsJws(token.trim()).getBody();
			jwt.setAccId(claims.getSubject());
			jwt.setDevice(claims.get("device", String.class));
			jwt.setSerialNum(claims.get("serialNum", String.class));
			jwt.setAudience(claims.getAudience());
			jwt.setId(claims.getId());
			jwt.setIssuedAt(claims.getIssuedAt());
			jwt.setIssuer(claims.getIssuer());
			if (StringUtils.StringIsEmptyOrNull(jwt.getAccId()) || StringUtils.StringIsEmptyOrNull(jwt.getDevice())
					|| StringUtils.StringIsEmptyOrNull(jwt.getSerialNum())
					|| StringUtils.StringIsEmptyOrNull(jwt.getAudience())
					// || DateUtils.getTime(jwt.getExpiration()) <
					// DateUtils.addTimeToTime(jwt.getIssuedAt(),
					// Constant.ACC_TOKEN_TIME_TO_LIVE)
					|| StringUtils.StringIsEmptyOrNull(jwt.getId())
					|| StringUtils.StringIsEmptyOrNull(jwt.getIssuer())) {
				jwt.setValid(false);
			} else {
				jwt.setValid(true);
			}
		} catch (Exception e) {
			jwt.setValid(false);
			System.out.println(e.toString());
		}
		return jwt;
	}

}
