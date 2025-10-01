package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.example.exception.DataAccessException;
import org.example.model.Match;
import org.example.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class MatchDAOImpl implements MatchDAO {
    @Override
    public void save(Match match) {
        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            manager.merge(match);
        } catch (PersistenceException e) {
            throw new DataAccessException("Ошибка сохранения матча: " + match, e);
        }
    }

    @Override
    public Optional<Match> findById(int id) {
        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            Match match = manager.find(Match.class, id);
            return Optional.ofNullable(match);
        } catch (PersistenceException e) {
            throw new DataAccessException("Ошибка поиска матча по id: " + id, e);
        }
    }

    @Override
    public List<Match> findAll() {
        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            return manager.createQuery("SELECT m FROM Match m", Match.class).getResultList();
        } catch (PersistenceException e) {
            throw new DataAccessException("Ошибка поиска всех матчей", e);
        }
    }

    @Override
    public List<Match> findByPlayerName(String name) {
        String jpql = "SELECT m FROM Match m WHERE m.player1.name = :playerName OR m.player2.name = :playerName";

        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            return manager.createQuery(jpql, Match.class)
                    .setParameter("playerName", name)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new DataAccessException("Ошибка поиска матчей по имени игрока: " + name, e);
        }
    }
}

