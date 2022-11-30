package com.controletcc.model.entity;

import com.controletcc.model.entity.base.Pessoa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "professor")
public class Professor extends Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supervisor_tcc")
    private boolean supervisorTcc;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private User usuario;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "professor_area_tcc", joinColumns = @JoinColumn(name = "id_professor"), inverseJoinColumns = @JoinColumn(name = "id_area_tcc"))
    private List<AreaTcc> areas;

    public Long getIdUsuario() {
        return this.usuario != null ? this.usuario.getId() : null;
    }

    public void setIdUsuario(Long idUsuario) {
        if (idUsuario != null) {
            if (this.usuario == null) {
                this.usuario = new User();
            }
            this.usuario.setId(idUsuario);
        } else {
            this.usuario = null;
        }
    }

    public List<Long> getIdAreaList() {
        return areas != null && !areas.isEmpty() ? areas.stream().map(AreaTcc::getId).toList() : Collections.emptyList();
    }

    public void setIdAreaList(List<Long> idAreaList) {
        if (idAreaList != null && !idAreaList.isEmpty()) {
            this.areas = idAreaList.stream().map(id -> {
                var area = new AreaTcc();
                area.setId(id);
                return area;
            }).toList();
        } else {
            this.areas = Collections.emptyList();
        }
    }
}
