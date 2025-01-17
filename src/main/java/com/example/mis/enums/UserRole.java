package com.example.mis.enums;

public enum UserRole {
  ROLE_ADMIN("ROLE_ADMIN"),
  SUPPLIER("ROLE_SUPPLIER"),
  SUPER_ADMIN("ROLE_SUPPER_ADMIN");
  // CUSTOMER("Customer"),
  // EMPLOYEE("Employee");

  private String displayName;

  UserRole(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

  @Override
  public String toString() {
    return name();
  }
}
