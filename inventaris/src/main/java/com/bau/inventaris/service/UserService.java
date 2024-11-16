package com.bau.inventaris.service;

import com.bau.inventaris.dto.UserDTO;
import com.bau.inventaris.entity.User;
import com.bau.inventaris.exception.InvalidCredentialsException;
import com.bau.inventaris.exception.UserNotFoundException;
import com.bau.inventaris.repository.UserRepository;
import com.bau.inventaris.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setNim(userDTO.getNim());
        user.setKelas(userDTO.getKelas());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user);
    }

    public String loginUser(String nim, String password) {
        Optional<User> userOpt = userRepository.findByNim(nim);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return jwtUtil.generateToken(user.getNim());
            }
        }
        throw new InvalidCredentialsException("Invalid credentials");
    }

    public User getProfile(String nim) {
        return userRepository.findByNim(nim)
                .orElseThrow(() -> new UserNotFoundException("User not found with NIM: " + nim));
    }

    public User updateProfile(String nim, String name, String kelas) {
        User user = userRepository.findByNim(nim)
                .orElseThrow(() -> new UserNotFoundException("User not found with NIM: " + nim));

        if (name != null && !name.isEmpty()) {
            user.setName(name);
        }
        if (kelas != null && !kelas.isEmpty()) {
            user.setKelas(kelas);
        }
        return userRepository.save(user);
    }

    public String changePassword(String nim, String oldPassword, String newPassword) {
        User user = userRepository.findByNim(nim)
                .orElseThrow(() -> new UserNotFoundException("User not found with NIM: " + nim));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "Password updated successfully";
    }

    public String deleteAccount(String nim) {
    User user = userRepository.findByNim(nim)
            .orElseThrow(() -> new UserNotFoundException("User not found with NIM: " + nim));
    userRepository.delete(user);
    return "Account deleted successfully";
    }

    
        public User authenticateUser(String nim, String password) {
    Optional<User> userOpt = userRepository.findByNim(nim);
    if (userOpt.isPresent()) {
        User user = userOpt.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user; // Kembalikan objek User jika login berhasil
        }
    }
    throw new RuntimeException("Invalid credentials");
}
}
