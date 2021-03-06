package gov.iti.jets.server.business.services.impl;

import common.business.dtos.UserDto;
import common.business.services.Mapper;
import gov.iti.jets.server.persistance.entities.UserEntity;

public enum UserMapperImpl implements Mapper<UserEntity> {
	INSTANCE;

	@Override
	public UserDto mapToUserDto(UserEntity userData) {
		return null;
	}

	@Override
	public UserEntity mapFromUserDto(UserDto userDto) {
        if (userDto == null)
			return null;
		return new UserEntity(
			userDto.phoneNumber,
			userDto.name,
			userDto.email,
			userDto.password,
			userDto.gender,
			java.sql.Date.valueOf(userDto.dateOfBirth),
			userDto.country,
			userDto.bio);
	}

}
