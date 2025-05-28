package exe201.Refashion.mapper;

import exe201.Refashion.dto.request.UserCreationRequest;
import exe201.Refashion.dto.response.UserResponse;
import exe201.Refashion.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toUser(UserCreationRequest request);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "profilePicture", target = "profilePicture")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "emailVerified", target = "emailVerified")
    @Mapping(source = "verificationToken", target = "verificationToken")
    @Mapping(source = "active", target = "active")
    UserResponse toUserResponse(Users user);
}
