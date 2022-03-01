package gov.iti.jets.server.business.services.impl;

import common.business.dtos.GroupDto;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.business.dtos.InvitationResponse;
import common.business.dtos.InvitationSentDto;
import common.business.dtos.MessageDto;
import common.business.dtos.UserAuthDto;
import common.business.dtos.UserDto;
import common.business.services.Client;
import common.business.services.Server;
import gov.iti.jets.server.business.daos.UserDao;
import gov.iti.jets.server.business.services.GroupService;
import gov.iti.jets.server.business.services.InvitationService;
import gov.iti.jets.server.persistance.entities.UserEntity;
import gov.iti.jets.server.persistance.util.DaosFactory;
import gov.iti.jets.server.business.services.MessageService;

public class ServerImpl extends UnicastRemoteObject implements Server {

	// connected Clients will be used for getting online users
	private Map<String, Client> connectedClients;

	public ServerImpl() throws RemoteException {
		super();
		connectedClients = new HashMap<>();
	}

	@Override
	public void login(Client connectedClient, UserAuthDto userAuthDto) {

		// call another class for authenticating db
		// checking if user exists or not

		connectedClients.put(userAuthDto.phoneNumber, connectedClient);
		System.out.println("User phone added to online users " + userAuthDto.phoneNumber);
		try {
			connectedClient.loginSuccess();
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void register(Client connectedClient, UserDto userDto) {
		// registered user will be connected?
		// map userDto to userEntity
		// call userDao to insert user data
		UserDao userDao = DaosFactory.INSTANCE.getUserDao();
		connectedClients.put(userDto.phoneNumber, connectedClient);
		UserEntity userEntity = UserMapperImpl.INSTANCE.mapFromUserDto(userDto);
		userDao.insertUser(userEntity);
		if (true) {
			try {
				connectedClient.registerationSuccess();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void logout(UserAuthDto userAuthDto) {
		// maybe add additional check to see if he is connected or not
		connectedClients.remove(userAuthDto.phoneNumber);
	}

	@Override
	public void sendMessage(MessageDto messageDto) {

		// GroupDao group = new GroupDaoImpl();
		// group.getUsersByGroupId(messageDto.receiverId);

		// ask db to get all users joined to this group id from messageDto
		/*
		 * for (ConnectedClient connectedClient : connectedClients) {
		 * 
		 * 
		 * if(message.groupID.equals(connectedClient.)){
		 * connectedClient.recieveInvitation(invitationDto.senderPhone);
		 * }
		 * }
		 * 
		 */
	}

	@Override
	public void invitationResponse(InvitationResponse invitationResponse) throws RemoteException {
		InvitationService invitation = new InvitationServiceImpl();
		invitation.updatingInvitation(invitationResponse);
	}

	@Override
	public void sendInvitation(InvitationSentDto invitationDto) {
		InvitationService invitation = new InvitationServiceImpl();
		invitation.sendInvitation(invitationDto, connectedClients);
	}

	@Override
	public List<GroupDto> getAllGroupsByUser(UserDto userDto) throws RemoteException {
		GroupService groupService = new GroupServiceImpl();
		return groupService.getAllGroupsByUser(userDto);
	}

	@Override
	public List<MessageDto> getAllMessagesByGroup(Integer groupId) {
		MessageService messageService = new MessageServiceImpl();
		return messageService.getAllMessagesByGroup(groupId);
	}

}
