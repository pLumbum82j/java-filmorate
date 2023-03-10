package ru.yandex.practicum.filmorate.storage.like;

public interface LikeStorage {

    /**
     * Метод добавления Like фильму
     *
     * @param filmId id фильма
     * @param userId id пользователя
     */
    void addLike(Long filmId, Long userId);

    /**
     * Метод удаления Like фильму
     *
     * @param filmId id фильма
     * @param userId id пользователя
     */
    void deleteLike(long filmId, long userId);
}
