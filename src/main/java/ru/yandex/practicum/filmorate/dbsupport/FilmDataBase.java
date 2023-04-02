package ru.yandex.practicum.filmorate.dbsupport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.util.*;

@Component
@Slf4j
public class FilmDataBase
{
	@Autowired
	UtilityDataBase utilityDataBase;
	final private JdbcTemplate jdbcTemplate;

	public long getLastId()
	{
		long id = 0;

		SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from films order by id desc");
		if(sqlRows.next())
		{
			id = sqlRows.getLong("id");
		}
		return id;
	}

	public Optional<Film> getFilmById(long id)
	{
		SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);

		// обрабатываем результат выполнения запроса
		if(sqlRows.next()) {
			Film film = new Film(sqlRows.getLong("id"),
					sqlRows.getString("name"),
					sqlRows.getString("description"),
					sqlRows.getDate("release_date").toLocalDate(),
					sqlRows.getInt("duration"),
					utilityDataBase.getMpaById(sqlRows.getLong("mpa_id")).get(),
					sqlRows.getInt("rate"),
					utilityDataBase.getGenresForFilm(sqlRows.getLong("id")),
					getLikes(sqlRows.getLong("id")));
			log.info("Найден фильм в БД: {} {}", film.getId(), film.getName());

			return Optional.of(film);
		} else {
			log.info("Фильм с идентификатором {} не найден в БД.", id);
			return Optional.empty();
		}
	}

	public void uploadFilm(Film film)
	{
		String sqlQuery = "insert into films(id, name, description, release_date, duration, mpa_id, rate) " +
				"values (?, ?, ?, ?, ?, ?, ?)";
		film.setId(getLastId() + 1);
		jdbcTemplate.update(sqlQuery, film.getId(), film.getName(), film.getDescription(),
				Date.valueOf(film.getReleaseDate()), film.getDuration(), film.getMpa().getId(), film.getRate());
		uploadLikes(film);
		utilityDataBase.uploadGenresForFilm(film);
		film.setMpa(utilityDataBase.getMpaById(film.getMpa().getId()).get());
		film.setGenres(utilityDataBase.getGenresForFilm(film.getId()));
		log.info("Новый фильм создан в БД.");
	}
	public void updateFilm(Film film)
	{
		String sqlQuery = "update films set name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?, rate = ? where id = ?";
		jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
				film.getDuration(), film.getMpa().getId(), film.getRate(), film.getId());
		uploadLikes(film);
		utilityDataBase.uploadGenresForFilm(film);
		film.setMpa(utilityDataBase.getMpaById(film.getMpa().getId()).get());
		film.setGenres(utilityDataBase.getGenresForFilm(film.getId()));
		log.info("Фильм под номером {} был обновлён.", film.getId());
	}

	public List<Film> getAllFilms()
	{
		List<Film> films = new ArrayList<>();
		SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from films");
		while(sqlRows.next())
		{
			Film film = new Film(sqlRows.getLong("id"),
					sqlRows.getString("name"),
					sqlRows.getString("description"),
					sqlRows.getDate("release_date").toLocalDate(),
					sqlRows.getInt("duration"),
					utilityDataBase.getMpaById(sqlRows.getLong("mpa_id")).get(),
					sqlRows.getInt("rate"),
					utilityDataBase.getGenresForFilm(sqlRows.getLong("id")),
					getLikes(sqlRows.getLong("id")));
			films.add(film);
		}
		log.info("Возвращён список всех фильмов размером: {}", films.size());
		return films;
	}

	public Set<Long> getLikes(long id)
	{
		Set<Long> likes = new HashSet<>();
		SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from films_to_likes where film_id = ?", id);
		while(sqlRows.next())
		{
			likes.add(sqlRows.getLong("like_id"));
		}
		return likes;
	}

	public void uploadLikes(Film film)
	{
		String sqlQuery = "delete from films_to_likes where film_id = ?";
		jdbcTemplate.update(sqlQuery, film.getId()); // Это хоть и не самый лучший,
													// но самый простой способ избежать повторов.

		for(Long id : film.getLikes())
		{
			sqlQuery = "insert into films_to_likes(film_id, like_id) " +
					"values (?, ?)";
			jdbcTemplate.update(sqlQuery, film.getId(), id);
		}
	}

	@Autowired
	public FilmDataBase(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}
}
