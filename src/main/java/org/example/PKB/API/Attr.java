package org.example.PKB.API;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Attr {

    private String procName;
    private String varName;
    private Integer constantValue;
    private Integer line;
    /* Uzupełnia najlepiej Front!!! */

    //Znak lub liczba -> const 5 itp i zmienna np. 'xyz' = 5;

}
