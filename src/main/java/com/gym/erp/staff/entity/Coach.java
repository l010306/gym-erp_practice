package com.gym.erp.staff.entity;



import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "coaches")
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String gender;

    private String phone;

    private LocalDate hireDate; // 入职日期

    private Long homeBranchId;     // 所属门店ID

    private String specialization; // 负责领域（如：增肌、瑜伽、康复等）

    // 备注：目前 homeBranchId 先用 Long，后期如果有了 Branch 实体类，
    // 我们可以将其改为 @ManyToOne 关联。
}
