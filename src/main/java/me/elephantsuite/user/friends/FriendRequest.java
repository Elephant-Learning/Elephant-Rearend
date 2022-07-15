package me.elephantsuite.user.friends;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class FriendRequest {

	private final long userId;

	private final long friendId;

}
