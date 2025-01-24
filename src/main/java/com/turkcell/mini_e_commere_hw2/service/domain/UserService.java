package com.turkcell.mini_e_commere_hw2.service.domain;
import com.turkcell.mini_e_commere_hw2.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
  User create(User user);
  User getById(UUID userId);
  User getByUsername(String username);
  List<User> getAll();
  User getActiveUser();
  UUID getActiveUserId();
}
