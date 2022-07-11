package me.elephantsuite.user.friends;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class FriendRequest {

	private final long userId;

	private final long friendId;
}
