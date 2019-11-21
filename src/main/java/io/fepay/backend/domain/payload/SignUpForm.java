package io.fepay.backend.domain.payload;

import io.fepay.backend.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpForm {

    private String user_id;

    private String password;

    private String name;

    private String phone;

    private String festival_id = "";

    public User toUser() {
        return new User(user_id, password, name, phone, 0, festival_id);
    }

}
