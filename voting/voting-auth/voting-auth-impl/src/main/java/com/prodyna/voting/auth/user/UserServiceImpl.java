package com.prodyna.voting.auth.user;

import com.prodyna.voting.common.Reject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;

@Service
@Log4j
public class UserServiceImpl implements UserService {

    private static final String HASH_ALGORITHM_NAME = "MD5";

    @Value("${voting.app.secret.key}")
    private String secretKey;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findUserByUserName(final String userName) {
        User user = userRepository.findByUserName(userName);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> login(User userToLogin) {
        Reject.ifNull(userToLogin, "Invalid login data");

        Optional<User> dbUser = findUserByUserName(userToLogin.getUserName());
        Reject.ifAbsent(dbUser, "No user with the specified userName exists. UserName=" + userToLogin.getUserName());

        String hashedPass = hashPassword(userToLogin.getPassword());

        User existingUser = dbUser.get();
        if (!hashedPass.equals(existingUser.getPassword())) {
            return Optional.empty();
        }
        existingUser.setToken(issueToken(existingUser));

        return Optional.of(existingUser);
    }

    private String issueToken(User existingUser) {
        return Jwts.builder().setSubject(existingUser.getUserName()).claim("role", existingUser.getRole())
                .setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    @Override
    public User saveUser(final User user) {
        Reject.ifNull(user, "Please provide the user object to save.");
        Reject.ifNull(user.getUserName(), "Please provide the userName.");
        Reject.ifNull(user.getPassword(), "Please provide the password.");
        Reject.ifNull(user.getRole(), "User must be assigned a role.");

        user.setPassword(hashPassword(user.getPassword()));

        return userRepository.save(user);
    }

    private String hashPassword(String password) {
        try {
            byte[] bytesOfMessage = password.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM_NAME);
            byte[] digest = md.digest(bytesOfMessage);
            String hashedPass = new BigInteger(1, digest).toString(16);

            // pad the string
            while (hashedPass.length() < 32) {
                hashedPass = "0" + hashedPass;
            }

            return hashedPass;
        } catch (NoSuchAlgorithmException e) {
            log.error("No such algorithm found. Should never happen.", e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void deleteAllUsers(User user) {
        if (!Role.isUserAdmin(user)) {
            Reject.always("No permission to delete all users!");
        }
        userRepository.deleteAll();
    }
}
