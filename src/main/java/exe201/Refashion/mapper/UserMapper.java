package exe201.Refashion.mapper;

import exe201.Refashion.dto.request.UserCreationRequest;
import exe201.Refashion.dto.response.UserResponse;
import exe201.Refashion.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toUser(UserCreationRequest request);

    @Mapping(source = "verificationToken", target = "verificationToken")
    @Mapping(source = "active", target = "active")
    UserResponse toUserResponse(Users user);
}
