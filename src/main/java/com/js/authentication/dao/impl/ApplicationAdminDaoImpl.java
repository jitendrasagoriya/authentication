package com.js.authentication.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.js.authentication.dto.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.js.authentication.dao.ApplicationAdminDao;
import com.js.authentication.model.Application;
import com.js.authentication.model.Application.ApplicationBuilder;
import com.js.authentication.model.Authentication;
import com.js.authentication.model.Authentication.AuthenticationBuilder;
import com.js.authentication.repository.ApplicationAdminRepository;

@Service
public class ApplicationAdminDaoImpl implements ApplicationAdminDao {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;;

	@Autowired
	private ApplicationAdminRepository repository;

	@Override
	public ApplicationAdminRepository getRepository() {
		return repository;
	}

	@Override
	public List<Pair<Authentication, Application>> getListOfAuthenticationaWithApplicationByUserId(String userId) {

		List<Pair<Authentication, Application>> list = new ArrayList<>();

		String query = "SELECT au.*,app.* FROM authentication au inner join "
				+ "( select ap.* from  application ap inner join applicationadmin aa on ap.id=aa.appid "
				+ " where aa.userid = :id ) app on au.appid = app.id";

		namedParameterJdbcTemplate.query(query, new MapSqlParameterSource("id", userId),
				new ResultSetExtractor<Pair<Authentication, Application>>() {

					@Override
					public Pair<Authentication, Application> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						Authentication authentication = new AuthenticationBuilder()
								.appId(query)
								.build();
						Application application = new ApplicationBuilder().build();
						return Pair.of(authentication, application);
					}
				});

		return list;
	}

	@Override
	public Optional<ApplicationUser> getAuthenticationWithApplicationNameByUserId(String id , String userId) {
		String query = "SELECT au.*,app.appName,app.id FROM authentication au inner join " +
				"( select ap.* from  application ap inner join applicationadmin aa on ap.id=aa.appid  " +
				" where aa.userid = :id) app on au.appid = app.id and au.userid = :userId";

		return  namedParameterJdbcTemplate.query(query, new MapSqlParameterSource("id", id).addValue("userId",userId), new ResultSetExtractor<Optional<ApplicationUser>>() {
			@Override
			public Optional<ApplicationUser> extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next()) {
					return Optional.ofNullable(new ApplicationUser.Builder()
							.id(rs.getString("userid"))
							.date(rs.getDate("creationdate"))
							.active(rs.getBoolean("isactive"))
							.logout(rs.getBoolean("islogout"))
							.name(rs.getString("username"))
							.type(rs.getString("type"))
							.applicationName(rs.getString("appname"))
							.appId(rs.getString("id"))
							.build());
				}
				return Optional.empty();
			}
		});

	}

	@Override
	public List<ApplicationUser> getListOfAuthenticationWithApplicationNameByUserId(String userId) {

		List<ApplicationUser> list = new ArrayList<>();

		String query = "SELECT au.userid,au.creationdate,au.isactive,au.islogout,au.username,au.type,app.appname,app.id FROM authentication au inner join " +
				" ( select ap.* from  application ap inner join applicationadmin aa on ap.id=aa.appid " +
				" where aa.userid = :id) app on au.appid = app.id";

		return namedParameterJdbcTemplate.query(query, new MapSqlParameterSource("id", userId), new RowMapper<ApplicationUser>() {
			@Override
			public ApplicationUser mapRow(ResultSet rs, int i) throws SQLException {
				return  new ApplicationUser.Builder()
						.id(rs.getString("userid"))
						.date(rs.getDate("creationdate"))
						.active(rs.getBoolean("isactive"))
						.logout(rs.getBoolean("islogout"))
						.name(rs.getString("username"))
						.applicationName(rs.getString("appname"))
						.appId(rs.getString("id"))
						.build();
			}
		});
	}

}
