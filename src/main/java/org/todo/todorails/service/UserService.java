package org.todo.todorails.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.todo.todorails.model.User;
import org.todo.todorails.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Registrar un nuevo usuario
     */
    public User registerUser(User user) throws Exception {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new Exception("❌ Username already exists: " + user.getUsername());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("❌ Email already exists: " + user.getEmail());
        }

        String encoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);
        user.setTermsAccepted(true);

        User saved = userRepository.save(user);
        System.out.println("✅ Usuario registrado: " + saved.getUsername() + " / " + saved.getEmail());
        return saved;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * Método de prueba para verificar contraseñas
     */
    public boolean testPassword(String rawPassword, String encodedPassword) {
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        System.out.println("🔐 TEST PASSWORD:");
        System.out.println("   Raw: " + rawPassword);
        System.out.println("   Encoded: " + encodedPassword);
        System.out.println("   Matches: " + matches);
        return matches;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            System.out.println("🔎 Intentando login con: " + username);

            User user = findByUsername(username);

            if (user == null) {
                System.out.println("⚠️ No encontrado por username, probando por email...");
                user = findByEmail(username);
            }

            if (user == null) {
                System.out.println("❌ Usuario no encontrado ni por username ni por email: " + username);
                throw new UsernameNotFoundException("User not found: " + username);
            }

            System.out.println("✅ Usuario encontrado: " + user.getUsername() + " / " + user.getEmail());
            System.out.println("🔑 Contraseña encriptada: " + user.getPassword());
            System.out.println("🔐 Roles: " + user.getAuthorities());

            // Como User ya implementa UserDetails, lo devolvemos directamente
            return user;

        } catch (Exception e) {
            System.err.println("💥 ERROR en loadUserByUsername: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
