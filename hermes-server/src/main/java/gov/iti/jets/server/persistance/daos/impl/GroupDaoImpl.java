package gov.iti.jets.server.persistance.daos.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gov.iti.jets.server.business.daos.GroupDao;
import gov.iti.jets.server.business.daos.UserDao;
import gov.iti.jets.server.persistance.DataSource;
import gov.iti.jets.server.persistance.entities.GroupEntity;
import gov.iti.jets.server.persistance.entities.UserEntity;
import gov.iti.jets.server.persistance.entities.enums.InvitationStatus;

/**
 * GroupDaoImpl
 */
public class GroupDaoImpl implements GroupDao {

	private DataSource dataSource;

	@Override
	public List<GroupEntity> getAllGroupdByUser(UserEntity userEntity) {
		var sql = "SELECT * from hermesdb.group_user gu, hermesdb.`group` g "
				+ "WHERE gu.user_phone_fk = ? "
				+ "AND g.id = gu.group_id_fk ;";
		List<GroupEntity> groupEntities = new ArrayList<>();
		try (var connection = DataSource.INSTANCE.getDataSource().getConnection();
				var preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, userEntity.phone);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				groupEntities.add(new GroupEntity(
						resultSet.getInt("id"),
						resultSet.getString("name"),
						resultSet.getString("image"),
						resultSet.getInt("particpiants_number")));
			}
			return groupEntities;
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return groupEntities;
	}

    @Override
    public int insertGroup(GroupEntity groupEntity) {
        //inserting new group for 2 specific users
        String sql = "insert into hermesdb.group (name, image, particpiants_number) values (?,?,?)";

        try (var connection = DataSource.INSTANCE.getDataSource().getConnection();
			 var preparedStmt = connection.prepareStatement(sql)) {

            preparedStmt.setString(1, groupEntity.name);
            preparedStmt.setString(2,groupEntity.image);
            preparedStmt.setInt(3, groupEntity.participantsNumber);

            // isInserted if returns 1 and in case insertion failed returns 0
            int isInserted = preparedStmt.executeUpdate();

            return isInserted;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return 0;

	}

    @Override
    public void updateGroup(GroupEntity groupEntity) {
        // TODO Auto-generated method stub
    }

	@Override
	public void deleteGroup(GroupEntity groupEntity) {
		// TODO Auto-generated method stub

	}

    @Override
    public int getGroupId(GroupEntity groupEntity) {
        String sql = "Select id from hermesdb.group where name = ? ";
        try (var connection = DataSource.INSTANCE.getDataSource().getConnection();
			 var preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setString(1, groupEntity.name);
            var resultSet = preparedStmt.executeQuery();
            if (resultSet.next()) {

                return  resultSet.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

	public List<String> getUsersByGroupId(Long groupID) {
		String query = "select * from group_user where group_id_fk = ? ";
		List<String> result = new ArrayList<>();
		try (var connection = DataSource.INSTANCE.getDataSource().getConnection();
				var preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setLong(1, groupID);
			var resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				result.add(resultSet.getString("user_phone_fk"));
				System.out.println("user attached to the group " + resultSet.getString("user_phone_fk"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public boolean checkPrivateChatEstablished(String client1, String client2) {

		String sql = "select particpiants_number from hermesdb.group where id in " +
				"(select a.group_id_fk " +
				"FROM  group_user a " +
				"INNER JOIN group_user b " +
				"ON a.group_id_fk = b.group_id_fk where " +
				"a.user_phone_fk = ? and b.user_phone_fk= ? ) and particpiants_number = 2;";

		try (var connection = DataSource.INSTANCE.getDataSource().getConnection();
			 var preparedStmt = connection.prepareStatement(sql)) {

			preparedStmt.setString(1, client1);
			preparedStmt.setString(2, client2);
			var resultSet = preparedStmt.executeQuery();
			if (resultSet.next()) {
				System.out.println("2 clients having a group and size is" + resultSet.getString("particpiants_number"));
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

    @Override
    public GroupEntity getGroupById(int id) {
		String sql = "Select * from `group` where id = ?";
		GroupEntity groupEntity = null;
		try(var connection = DataSource.INSTANCE.getDataSource().getConnection();
		var preparedStatement = connection.prepareStatement(sql)){
			var groups = preparedStatement.executeQuery();
			if(groups.next()) {
				groupEntity = new GroupEntity(groups.getInt("id"),groups.getString("name"),groups.getString("image"),groups.getInt("particpiants_number"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return groupEntity;
    }

}
