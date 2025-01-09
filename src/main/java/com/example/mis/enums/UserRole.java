package com.example.mis.enums;

public enum UserRole {
  ADMIN("Admin"),
  SUPPLIER("Supplier"),
  SUPER_ADMIN("SuperAdmin");
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
