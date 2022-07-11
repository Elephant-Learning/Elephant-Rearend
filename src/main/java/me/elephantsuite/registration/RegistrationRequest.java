package me.elephantsuite.registration;

import java.util.List;

import me.elephantsuite.user.ElephantUserType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
//format for a rq
public record RegistrationRequest(String firstName, String lastName, String password,
								  String email, ElephantUserType type,
								  Integer pfpId, List<Long> friendIds) {

}
