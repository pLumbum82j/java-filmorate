package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    void addFriends(long friend1, long friend2);
    void deleteFriends(long friend1, long friend2);
    List<User> getUserFriends(long id);
    List<User> getCommonFriends(long friend1, long friend2);
}
