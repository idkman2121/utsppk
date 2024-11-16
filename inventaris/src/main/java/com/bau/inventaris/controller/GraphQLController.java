package com.bau.inventaris.controller;

import com.bau.inventaris.dto.UserDTO;
import com.bau.inventaris.entity.Inventory;
import com.bau.inventaris.entity.User;
import com.bau.inventaris.service.InventoryService;
import com.bau.inventaris.service.UserService;
import com.bau.inventaris.utils.JwtUtil;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GraphQLController {

    private final UserService userService;
    private final InventoryService inventoryService;
    private final JwtUtil jwtUtil;

    public GraphQLController(UserService userService, InventoryService inventoryService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.inventoryService = inventoryService;
        this.jwtUtil = jwtUtil;
    }

    @MutationMapping
    public UserDTO registerUser(@Argument String name, @Argument String nim, @Argument String kelas,
                                @Argument String password) {
        var userDTO = new UserDTO();
        userDTO.setName(name);
        userDTO.setNim(nim);
        userDTO.setKelas(kelas);
        userDTO.setPassword(password);
        var user = userService.registerUser(userDTO);
        return userDTO;
    }

    @MutationMapping
    public User loginUser(@Argument String nim, @Argument String password) {
    User user = userService.authenticateUser(nim, password);
    if (user == null) {
        throw new RuntimeException("Invalid NIM or password");
    }

    String token = jwtUtil.generateToken(user.getNim());
    user.setToken(token); 

    return user;
}


    @QueryMapping
    public List<Inventory> getAllInventories() {
        return inventoryService.getAllInventories();
    }

    @MutationMapping
    public String useInventory(@Argument Long userId, @Argument Long inventoryId, @Argument int quantity) {
        return inventoryService.useInventory(userId, inventoryId, quantity);
    }
    
        @QueryMapping
    public User getProfile(@Argument String token) {
        String nim = jwtUtil.validateTokenAndRetrieveSubject(token);
        return userService.getProfile(nim);
    }

    @MutationMapping
    public User updateProfile(@Argument String token, @Argument String name, @Argument String kelas) {
        String nim = jwtUtil.validateTokenAndRetrieveSubject(token);
        return userService.updateProfile(nim, name, kelas);
    }

    @MutationMapping
    public String changePassword(@Argument String token, @Argument String oldPassword, @Argument String newPassword) {
        String nim = jwtUtil.validateTokenAndRetrieveSubject(token);
        return userService.changePassword(nim, oldPassword, newPassword);
    }

    @MutationMapping
    public String deleteAccount(@Argument String token) {
    String nim = jwtUtil.validateTokenAndRetrieveSubject(token);
    return userService.deleteAccount(nim);
    }

}
