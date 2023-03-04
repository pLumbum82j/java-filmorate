package ru.yandex.practicum.filmorate.storage.like;

public interface LikeStorage {
    void addLike(Long filmId, Long userId);
}
