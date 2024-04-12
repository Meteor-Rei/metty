package org.meteor.metty.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: User
 * @Created Time: 2024-04-09 15:53
 **/


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String username;

    private String password;

    private Integer age;
}
