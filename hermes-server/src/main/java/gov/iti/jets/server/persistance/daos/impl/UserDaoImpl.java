package gov.iti.jets.server.persistance.daos.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import gov.iti.jets.server.business.daos.UserDao;
import gov.iti.jets.server.persistance.DataSource;
import gov.iti.jets.server.persistance.entities.UserEntity;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {

	private DataSource dataSource;

	public UserDaoImpl() {
		this.dataSource = DataSource.INSTANCE;
	}

	@Override
	public List<UserEntity> getAllUsers() {
		List<UserEntity> userEntities = new ArrayList<>();
		String query = "Select * from user";
		try (var connection = dataSource.getDataSource().getConnection();
				var preparedStatement = connection.prepareStatement(query);
				var resultSet = preparedStatement.executeQuery()) {
			while (resultSet.next()) {
				UserEntity userEntity = new UserEntity();
				fillUserEntity(resultSet, userEntity);
				userEntities.add(userEntity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userEntities;
	}

	@Override
	public Optional<UserEntity> getUserByPhone(String phone) {

		String sql = "Select * from user where phone = ?";

		try (var connection = DataSource.INSTANCE.getDataSource().getConnection(); var preparedStmt = connection.prepareStatement(sql)) {
			preparedStmt.setString(1, phone);
			var resultSet = preparedStmt.executeQuery();
			if (resultSet.next()) {
				UserEntity userEntity = new UserEntity();
				fillUserEntity(resultSet, userEntity);
				return Optional.of(userEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.<UserEntity>of(null);

	}

	private void fillUserEntity(ResultSet resultSet, UserEntity userEntity) throws SQLException {
		userEntity.phone = resultSet.getString("phone");
		userEntity.name = resultSet.getString("name");
		userEntity.email = resultSet.getString("email");
		userEntity.password = resultSet.getString("password");
		// userEntity.image = resultSet.getString("image");
		userEntity.gender = resultSet.getBoolean("gender");
		userEntity.dob = resultSet.getDate("dob");
		userEntity.country = resultSet.getString("country");
		userEntity.bio = resultSet.getString("bio");
	}

	@Override
	public String insertUser(UserEntity user) {
		boolean uniqueEmail = true;
		boolean uniquePhone = true;
		List<UserEntity> allUsers = getAllUsers();
		for (UserEntity userEntity : allUsers) {
			if (userEntity.phone.equals(user.phone)){
				uniquePhone = false;
				return "Phone Number Must Be Unique";
			}
			if (userEntity.email.equalsIgnoreCase(user.email)){
				uniqueEmail = false;
				return "Email Must Be Unique";
			}
		}

		if (uniquePhone && uniqueEmail) {
			int gender = user.gender ? 1 : 0;
			String query = "INSERT INTO hermesdb.user (name, phone, email, password, gender, dob, country) VALUES (?,?,?,?,?,?,?);";
			try (var connection = dataSource.getDataSource().getConnection();
					var preparedStatement = connection.prepareStatement(query);) {
				preparedStatement.setString(1, user.name.trim());
				preparedStatement.setString(2, user.phone.trim());
				preparedStatement.setString(3, user.email.trim());
				preparedStatement.setString(4, user.password.trim());
				preparedStatement.setInt(5, gender);
				preparedStatement.setDate(6, user.dob);
				preparedStatement.setString(7, user.country.trim());
				// System.out.println(preparedStatement.executeUpdate());
				preparedStatement.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void updateUser(UserEntity user) {

	}

	@Override
	public void deleteUser(UserEntity user) {

	}


	@Override
	public List<UserEntity> getAllMaleUsers() {
		List<UserEntity> userEntities = new ArrayList<>();
		String query = "Select * from user where gender = 1";
		try (var connection = dataSource.getDataSource().getConnection();
			 var preparedStatement = connection.prepareStatement(query);
			 var resultSet = preparedStatement.executeQuery()) {
			while (resultSet.next()) {
				UserEntity userEntity = new UserEntity();
				fillUserEntity(resultSet, userEntity);
				userEntities.add(userEntity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userEntities;
	}


	@Override
	public List<UserEntity> getAllFemaleUsers() {
		List<UserEntity> userEntities = new ArrayList<>();
		String query = "Select * from user where gender = 0";
		try (var connection = dataSource.getDataSource().getConnection();
			 var preparedStatement = connection.prepareStatement(query);
			 var resultSet = preparedStatement.executeQuery()) {
			while (resultSet.next()) {
				UserEntity userEntity = new UserEntity();
				fillUserEntity(resultSet, userEntity);
				userEntities.add(userEntity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userEntities;
	}

	public Map<String,Integer> getAllCountries(){
		Map<String,Integer> countriesWithUsers = new HashMap<>();
		String query = "Select * from user;";
		try (var connection = dataSource.getDataSource().getConnection();
			 var preparedStatement = connection.prepareStatement(query);
			 var resultSet = preparedStatement.executeQuery()) {
			String country;
			while (resultSet.next()) {
			 country=resultSet.getString("country");
			 if(resultSet.wasNull()){
				 continue;
			 }else {
				 if(countriesWithUsers.containsKey(country)){
					 countriesWithUsers.put(country,countriesWithUsers.get(country)+1);
				 }else{
					 countriesWithUsers.put(country,1);
				 }
			 }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return countriesWithUsers;
	}
}
