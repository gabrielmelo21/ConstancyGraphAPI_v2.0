package constacygraph.api.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "graphs")
public class Graphs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dataDia", nullable = false)
    private String dataDia;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "numeroDoDia", nullable = false)
    private int numeroDoDia;

    @Column(name = "objetivo", nullable = false)
    private String objetivo;




}
