/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.examples.sb.enums;

/**
 *
 * @author brijeshdhaker
 */
public enum UserStatus implements DBEnumeration<Long> {

  ACTIVE(1, "ACTIVE", "Active"),
  BLOCKED(2, "BLOCKED", "Blocked"),
  INACTIVE(3, "INACTIVE", "In Active");

  private long id;
  private String name;
  private String description;

  UserStatus(long id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public static UserStatus getByCode(long id) {
    for (UserStatus type : UserStatus.values()) {
      if (type.getId() == id) {
        return type;
      }
    }
    return null;
  }

  public static UserStatus getByName(String name) {
    for (UserStatus type : UserStatus.values()) {
      if (type.getName().equalsIgnoreCase(name)) {
        return type;
      }
    }
    return null;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public Long getDBCode() {
    return id;
  }

}
