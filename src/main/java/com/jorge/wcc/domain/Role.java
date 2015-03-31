package com.jorge.wcc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "T_ROLE")
@XmlRootElement(name = "role")
@XmlAccessorType(XmlAccessType.FIELD)
public class Role {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
   @NotNull
   @Column(length = 50)
   private String roleName;

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getRoleName() {
      return roleName;
   }

   public void setRoleName(String roleName) {
      this.roleName = roleName;
   }

   @Override
   public String toString() {
      return "Role [id=" + id //
            + ", roleName=" + roleName + "]";
   }
}
