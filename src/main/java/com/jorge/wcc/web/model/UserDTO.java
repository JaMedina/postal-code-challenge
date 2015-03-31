package com.jorge.wcc.web.model;

import java.util.List;

public class UserDTO {

  private String name;
  private String password;
  private String username;
  private List<String> permissions;

  public UserDTO() {}

  public UserDTO(String name, String password, String username, List<String> permissions) {
    this.name = name;
    this.password = password;
    this.username = username;
    this.permissions = permissions;
  }

  public String getPassword() {
    return password;
  }

  public String getName() {
    return name;
  }

  public String getUsername() {
    return username;
  }

  public List<String> getPermissions() {
    return permissions;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("UserDTO{");
    sb.append("name='").append(name).append('\'');
    sb.append(", username='").append(username).append('\'');
    sb.append(", permissions=").append(permissions);
    sb.append('}');
    return sb.toString();
  }
}
