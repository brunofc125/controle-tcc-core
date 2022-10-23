package com.controletcc.model.entity;

import com.controletcc.model.entity.base.Pessoa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Professor extends Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supervisor_tcc")
    private boolean supervisorTcc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private User usuario;

    public Long getIdUsuario() {
        return this.usuario != null ? this.usuario.getId() : null;
    }

    public void setIdUsuario(Long idUsuario) {
        if (this.usuario == null) {
            this.usuario = new User();
        }
        this.usuario.setId(idUsuario);
    }
}
