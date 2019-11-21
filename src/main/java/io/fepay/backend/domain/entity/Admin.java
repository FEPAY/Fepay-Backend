package io.fepay.backend.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.fepay.backend.domain.payload.FestivalInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id
    private String email;

    @JsonIgnore
    private String password;

    private String name;

    private String phone;

    private String festivalName;

    private LocalDate closingDate;

    private String festivalId;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public Admin(String email, String password, String name, String phone, String festivalName, LocalDate closingDate) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.festivalName = festivalName;
        this.closingDate = closingDate;
    }

    public FestivalInfo toFestivalInfo() {
        return new FestivalInfo(festivalId, festivalName);
    }
}
