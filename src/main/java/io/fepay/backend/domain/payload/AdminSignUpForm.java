package io.fepay.backend.domain.payload;

import io.fepay.backend.domain.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminSignUpForm {

    private String email;

    private String password;

    private String name;

    private String phone;

    private String festival_name;

    private String closing_date;

    public Admin toAdmin() {
        return new Admin(email, password, name, phone, festival_name, LocalDate.parse(closing_date));
    }

}
