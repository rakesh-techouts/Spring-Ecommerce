package com.techouts.repositoryimpl;

import com.techouts.entity.User;
import com.techouts.repository.UserRespository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImplementation implements UserRespository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User save(User user) {
        return em.merge(user);
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> users = em.createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", email)
                .setMaxResults(1)
                .getResultList();
        return users.stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> users = em.createNamedQuery("User.findByUsername", User.class)
                .setParameter("username", username)
                .setMaxResults(1)
                .getResultList();
        return users.stream().findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count != null && count > 0;
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String identity) {
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.username = :identity OR u.email = :identity", User.class)
                .setParameter("identity", identity)
                .setMaxResults(1)
                .getResultList();
        return users.stream().findFirst();
    }
}
